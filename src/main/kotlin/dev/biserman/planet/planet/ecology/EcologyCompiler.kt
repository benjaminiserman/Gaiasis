package dev.biserman.planet.planet.ecology

data class CompiledEcology(
    val species: List<CompiledSpecies>,
    val niches: List<NicheDefinition>,
    val interactions: InteractionMatrix,
) {
    private val indexById = species.associate { it.id to it.index }

    fun speciesIndex(id: String): Int =
        indexById[id] ?: error("Unknown species id: $id")
}

object EcologyCompiler {
    fun compile(
        definitions: List<SpeciesDefinition>,
        niches: List<NicheDefinition> = EcologyNiches.defaults,
    ): CompiledEcology {
        require(definitions.isNotEmpty())
        require(definitions.map { it.id }.distinct().size == definitions.size) {
            "Species ids must be unique"
        }
        require(niches.distinct().size == niches.size) {
            "Niche definitions must be unique"
        }

        val compiledSpecies = definitions.mapIndexed { index, definition ->
            compileSpecies(index, definition, niches)
        }
        return CompiledEcology(
            species = compiledSpecies,
            niches = niches,
            interactions = FoodWebCompiler.compile(definitions, compiledSpecies),
        )
    }

    private fun compileSpecies(
        index: Int,
        definition: SpeciesDefinition,
        niches: List<NicheDefinition>,
    ): CompiledSpecies {
        val traitProfile = definition.traitProfile()
        val commonTraits = traitProfile.traits.filterIsInstance<CommonTrait>().toSet()
        val traitsByGroup = traitProfile.traits
            .mapNotNull { trait -> trait.group?.let { group -> group to trait } }
            .groupBy({ it.first }, { it.second })
        traitsByGroup.forEach { (group, traits) ->
            require(traits.size <= 1) {
                "${definition.displayName} has conflicting $group traits: " +
                    traits.joinToString { it.displayName }
            }
        }
        TraitDependencies.requireSatisfied(definition)
        val capabilities = traitProfile.capabilities
        require(TraitCapability.REPRODUCTION in capabilities) {
            "${definition.displayName} must have at least one reproductive strategy"
        }
        require(TraitCapability.RESPIRATION in capabilities) {
            "${definition.displayName} must have at least one respiratory strategy"
        }
        require(
            definition.kind == SpeciesKind.INVARIANT ||
                traitProfile.traits.none { it.invariantOnly },
        ) {
            "${definition.displayName} uses a trait reserved for invariant aggregate guilds"
        }
        require(
            definition.kind != SpeciesKind.INVARIANT ||
                CommonTrait.INVARIANT_RESISTANCE in commonTraits,
        ) {
            "${definition.displayName} is invariant and must have invariant guild resilience"
        }
        require(traitsByGroup[TraitGroup.BIOCHEMISTRY]?.size == 1) {
            "${definition.displayName} must have exactly one biochemistry foundation"
        }
        require(!definition.motile || traitsByGroup[TraitGroup.THERMOREGULATION]?.size == 1) {
            "${definition.displayName} is motile and must have exactly one thermal strategy"
        }
        require(!definition.motile || traitsByGroup[TraitGroup.SOCIAL_ORGANIZATION]?.size == 1) {
            "${definition.displayName} is motile and must have exactly one social organization"
        }
        require(definition.motile || TraitGroup.THERMOREGULATION !in traitsByGroup) {
            "${definition.displayName} is not motile but has a motile thermal strategy"
        }
        require(definition.motile || TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE !in traitsByGroup) {
            "${definition.displayName} is not motile and cannot have a terrestrial movement structure"
        }
        require(definition.motile || TraitCapability.LOCOMOTION !in capabilities) {
            "${definition.displayName} is sessile but has a locomotion capability"
        }

        val context = SpeciesCompilationContext(
            speciesDisplayName = definition.displayName,
            sizeTemperatureTolerance = sizeTemperatureTolerance(definition.sizeClass),
        )
        traitProfile.entries.forEach { (trait, level) -> context.apply(trait, level) }
        traitProfile.entries.forEach { (trait, level) ->
            trait.effectsAt(level).forEach { effect ->
                if (effect is ConditionalTraitEffect && effect.condition.matches(definition, traitProfile)) {
                    context.apply(effect.effects)
                }
            }
        }
        context.applyCrossTraitRules(definition.sizeClass, commonTraits)
        return context.finish(index, definition, niches, commonTraits, traitProfile)
    }

    private fun sizeTemperatureTolerance(sizeClass: SizeClass): Double = when (sizeClass) {
        SizeClass.MINUSCULE, SizeClass.TINY, SizeClass.SMALL -> 0.0
        SizeClass.MEDIUM -> 0.5
        SizeClass.LARGE -> 1.5
        SizeClass.HUGE -> 3.0
        SizeClass.COLOSSAL -> 4.0
    }
}
