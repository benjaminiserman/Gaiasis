package dev.biserman.planet.planet.ecology

data class UnmetTraitRequirement(
    val trait: SpeciesTrait,
    val requirement: TraitRequirement,
)

/** Shared validation used by compilation now and by evolutionary mutation later. */
object TraitDependencies {
    fun unmetRequirements(definition: SpeciesDefinition): List<UnmetTraitRequirement> {
        val capabilities = definition.traits.flatMapTo(linkedSetOf()) { it.capabilities }
        return definition.traits.flatMap { trait ->
            trait.requirements
                .filterNot { it.isSatisfiedBy(definition, capabilities) }
                .map { UnmetTraitRequirement(trait, it) }
        }
    }

    fun requireSatisfied(definition: SpeciesDefinition) {
        val failures = unmetRequirements(definition)
        require(failures.isEmpty()) {
            failures.joinToString(separator = "; ") { failure ->
                "${definition.displayName}'s '${failure.trait.displayName}' trait " +
                    failure.requirement.describe()
            }
        }
    }
}
