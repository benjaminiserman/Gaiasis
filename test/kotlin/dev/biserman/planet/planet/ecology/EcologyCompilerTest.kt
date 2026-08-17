package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class EcologyCompilerTest {
    @Test
    fun `compiled niche profile owns its optimized arrays`() {
        val habitatSupport = DoubleArray(Habitat.entries.size).also {
            it[Habitat.LAND_SURFACE.ordinal] = 0.75
        }
        val strategySupport = DoubleArray(EcoStrategy.entries.size).also {
            it[EcoStrategy.GRAZING.ordinal] = 0.60
        }
        val camouflage = DoubleArray(Habitat.entries.size).also {
            it[Habitat.LAND_SURFACE.ordinal] = 0.40
        }
        val nicheFit = doubleArrayOf(0.45)
        val profile = NicheProfile(
            producerCompetitionLayer = ProducerCompetitionLayer.NONE,
            photosyntheticColor = null,
            camouflageColor = BiologicalColor.BROWN,
            habitatSupport = habitatSupport,
            strategySupport = strategySupport,
            camouflage = camouflage,
            nicheFit = nicheFit,
        )

        habitatSupport[Habitat.LAND_SURFACE.ordinal] = 0.0
        strategySupport[EcoStrategy.GRAZING.ordinal] = 0.0
        camouflage[Habitat.LAND_SURFACE.ordinal] = 0.0
        nicheFit[0] = 0.0

        assertEquals(0.75, profile.supportFor(Habitat.LAND_SURFACE))
        assertEquals(0.60, profile.supportFor(EcoStrategy.GRAZING))
        assertEquals(0.40, profile.camouflageFor(Habitat.LAND_SURFACE))
        assertEquals(0.45, profile.fitFor(0))
    }

    @Test
    fun `traits compile into climate and niche parameters`() {
        val producer = producer(
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.PHOTOSYNTHETIC_SURFACE,
                CommonTrait.ROOTED_BODY,
                CommonTrait.FUR,
                CommonTrait.DENSE_UNDERCOAT,
            ),
        )

        val compiled = EcologyCompiler.compile(listOf(producer)).species.single()

        assertEquals(-4.0, compiled.physiology.thermal.outerLowC)
        assertEquals(27.0, compiled.physiology.thermal.outerHighC)
        assertTrue(compiled.niche.hasViableNiche())
        assertTrue(compiled.physiology.maintenanceDemand > 0.0)
    }

    @Test
    fun `size foundation establishes mass and slightly widens temperature range`() {
        val small = EcologyCompiler.compile(listOf(predator("small", SizeClass.SMALL))).species.single()
        val huge = EcologyCompiler.compile(listOf(predator("huge", SizeClass.HUGE))).species.single()

        assertEquals(SizeClass.SMALL.typicalMassKg, small.physiology.massKg)
        assertEquals(SizeClass.HUGE.typicalMassKg, huge.physiology.massKg)
        assertTrue(huge.physiology.thermal.outerLowC < small.physiology.thermal.outerLowC)
        assertTrue(huge.physiology.thermal.outerHighC > small.physiology.thermal.outerHighC)
    }

    @Test
    fun `motile species require exactly one thermal strategy`() {
        val invalid = SpeciesDefinition(
            id = "invalid",
            displayName = "Invalid swimmer",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.AQUATIC_OVOSPORE,
                CommonTrait.BUOYANCY_BLADDER,
                CommonTrait.GILL_PADS,
            ),
        )

        assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }
    }

    @Test
    fun `traits in the same biological group are mutually exclusive`() {
        val invalid = predator("conflicting-coverings").copy(
            traits = predator("conflicting-coverings").traits +
                listOf(CommonTrait.FUR, CommonTrait.FEATHERS),
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("DOMINANT_BODY_COVERING"))
        assertTrue(failure.message.orEmpty().contains(CommonTrait.FUR.displayName))
        assertTrue(failure.message.orEmpty().contains(CommonTrait.FEATHERS.displayName))
    }

    @Test
    fun `every declared trait group has multiple authored alternatives`() {
        val groupedTraits =
            (CommonTrait.entries + ColorTrait.entries)
                .filter { it.group != null }
                .groupBy { it.group }

        assertEquals(TraitGroup.entries.toSet(), groupedTraits.keys.filterNotNull().toSet())
        groupedTraits.forEach { (group, traits) ->
            assertTrue(traits.size >= 2, "$group has fewer than two alternatives")
        }
    }

    @Test
    fun `biological color is compiled from mutually exclusive traits`() {
        val brownPredator = predator("brown-predator")
        val compiled = EcologyCompiler.compile(listOf(brownPredator)).species.single()
        assertEquals(BiologicalColor.BROWN, compiled.niche.camouflageColor)

        val adaptivePredator =
            brownPredator.copy(
                id = "adaptive-predator",
                traits =
                brownPredator.traits - ColorTrait.BROWN_CAMOUFLAGE +
                    ColorTrait.ADAPTIVE_CAMOUFLAGE,
            )
        val adaptiveCompiled = EcologyCompiler.compile(listOf(adaptivePredator)).species.single()
        assertTrue(
            adaptiveCompiled.physiology.maintenanceDemand > compiled.physiology.maintenanceDemand,
        )

        val conflicting = brownPredator.copy(
            traits = brownPredator.traits + ColorTrait.WHITE_CAMOUFLAGE,
        )
        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(conflicting))
        }
        assertTrue(failure.message.orEmpty().contains("BIOLOGICAL_COLOR"))
    }

    @Test
    fun `authored species satisfy every trait dependency`() {
        val failures =
            (EarthSpeciesCatalog.ALL + EarthSpeciesCatalog.EXTINCT_SPECIES + InvariantSpecies.ALL)
                .flatMap { definition ->
                    TraitDependencies.unmetRequirements(definition).map { failure ->
                        "${definition.displayName}: ${failure.trait.displayName} " +
                            failure.requirement.describe()
                    }
                }

        assertTrue(failures.isEmpty(), failures.joinToString(separator = "\n"))
    }

    @Test
    fun `every compiled species requires at least one reproductive strategy`() {
        val invalid = SpeciesDefinition(
            id = "reproduction-missing",
            displayName = "Reproduction missing",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.WALKING_LIMBS,
            ),
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("reproductive strategy"))
    }

    @Test
    fun `reproductive strategies compose and aerial dispersal requires ovospores`() {
        val mixedStrategy = predator("mixed-reproduction").copy(
            traits = predator("mixed-reproduction").traits + CommonTrait.CLONAL_PROPAGATION,
        )
        EcologyCompiler.compile(listOf(mixedStrategy))

        listOf(CommonTrait.TERRESTRIAL_OVOSPORE, CommonTrait.AQUATIC_OVOSPORE).forEach { ovospore ->
            assertTrue(TraitCapability.REPRODUCTION in ovospore.capabilities)
            assertTrue(TraitCapability.OVOSPORE_REPRODUCTION in ovospore.capabilities)
            assertTrue(TraitCapability.OVOSPORE_BROODING in ovospore.capabilities)
        }
        assertTrue(TraitCapability.REPRODUCTION in CommonTrait.VIVIPARITY.capabilities)
        assertTrue(TraitCapability.REPRODUCTION in CommonTrait.CLONAL_PROPAGATION.capabilities)

        val invalidAerialDisperser = predator("invalid-aerial-disperser").copy(
            traits = predator("invalid-aerial-disperser").traits
                .filterNot { TraitCapability.REPRODUCTION in it.capabilities } +
                CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
        )
        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalidAerialDisperser))
        }
        assertTrue(failure.message.orEmpty().contains("OVOSPORE_REPRODUCTION"))
    }

    @Test
    fun `thermal foundation compiles to an explicit runtime strategy`() {
        val compiled = EcologyCompiler.compile(
            listOf(predator("explicit-thermal-strategy")),
        ).species.single()

        assertEquals(ThermalStrategy.ENDOTHERMY, compiled.physiology.thermal.regulation)
    }

    @Test
    fun `floating body provides aerial habitat independently of photosynthesis`() {
        val floating = SpeciesDefinition(
            id = "floating-body",
            displayName = "Floating body",
            sizeClass = SizeClass.MINUSCULE,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.CLONAL_PROPAGATION,
                CommonTrait.FLOATING_BODY,
            ),
        )
        val photosynthetic = floating.copy(
            id = "photosynthetic-floating-body",
            displayName = "Photosynthetic floating body",
            traits =
            floating.traits +
                CommonTrait.PHOTOSYNTHETIC_SURFACE +
                ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        )

        val ecology = EcologyCompiler.compile(listOf(floating, photosynthetic))
        val floatingProfile = ecology.species[0].niche
        val photosyntheticProfile = ecology.species[1].niche

        assertTrue(floatingProfile.supportFor(Habitat.AERIAL) > 0.0)
        assertEquals(0.0, floatingProfile.supportFor(EcoStrategy.PHOTOSYNTHESIS))
        assertTrue(photosyntheticProfile.supportFor(Habitat.AERIAL) > 0.0)
        assertTrue(photosyntheticProfile.supportFor(EcoStrategy.PHOTOSYNTHESIS) > 0.0)
    }

    @Test
    fun `floating body requires minuscule size`() {
        val invalid = SpeciesDefinition(
            id = "oversized-floater",
            displayName = "Oversized floater",
            sizeClass = SizeClass.TINY,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.CLONAL_PROPAGATION,
                CommonTrait.FLOATING_BODY,
            ),
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("requires MINUSCULE size"))
    }

    @Test
    fun `deep diving accepts either underwater respiration or breath holding`() {
        val base = SpeciesDefinition(
            id = "deep-diver",
            displayName = "Deep diver",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.AQUATIC_OVOSPORE,
                CommonTrait.AQUATIC_FLIPPERS,
                CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            ),
        )

        val missingRespiration = TraitDependencies.unmetRequirements(base)
        assertEquals(1, missingRespiration.size)
        assertTrue(missingRespiration.single().requirement is TraitRequirement.AnyOf)
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.GILLS),
            ).isEmpty(),
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.PROLONGED_BREATH_HOLDING),
            ).isEmpty(),
        )
    }

    @Test
    fun `pelagic soaring wings require a flight structure`() {
        val invalid = predator("flightless-soarer").copy(
            traits = predator("flightless-soarer").traits + CommonTrait.PELAGIC_SOARING_WINGS,
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("requires ACTIVE_FLIGHT"))
    }

    @Test
    fun `specialized anatomy requires its underlying structure`() {
        val base = predator("anatomy")

        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.DENSE_UNDERCOAT),
            ).single().requirement is TraitRequirement.AllOf,
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.SWIFT_LEGS),
            ).isEmpty(),
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(
                    traits = base.traits
                        .filterNot { it == CommonTrait.WALKING_LIMBS } +
                        CommonTrait.SWIFT_LEGS,
                ),
            ).isNotEmpty(),
        )
    }

    @Test
    fun `expanded archetype traits require their underlying physiology`() {
        val terrestrial = predator("archetype-dependencies")
        fun unmet(vararg traits: SpeciesTrait): List<UnmetTraitRequirement> =
            TraitDependencies.unmetRequirements(
                terrestrial.copy(traits = terrestrial.traits + traits),
            )

        assertTrue(unmet(CommonTrait.BEHAVIORAL_THERMOREGULATION).isNotEmpty())
        assertTrue(unmet(CommonTrait.SCHOOLING).isNotEmpty())
        assertTrue(unmet(CommonTrait.HOST_PENETRATING_FILAMENTS).isNotEmpty())
        assertTrue(unmet(CommonTrait.REEF_BUILDING).isNotEmpty())

        val ectotherm = terrestrial.copy(
            traits = terrestrial.traits
                .filterNot { it == CommonTrait.ENDOTHERMY } +
                CommonTrait.ECTOTHERMY +
                CommonTrait.BEHAVIORAL_THERMOREGULATION,
        )
        assertTrue(TraitDependencies.unmetRequirements(ectotherm).isEmpty())
        assertTrue(unmet(CommonTrait.ABSORPTIVE_FILAMENTS, CommonTrait.HOST_PENETRATING_FILAMENTS).isEmpty())
        assertTrue(unmet(CommonTrait.RIGID_COLONY_FRAMEWORK, CommonTrait.REEF_BUILDING).isEmpty())

        val aquatic = terrestrial.copy(
            traits = terrestrial.traits + CommonTrait.AQUATIC_FLIPPERS + CommonTrait.SCHOOLING,
        )
        assertTrue(TraitDependencies.unmetRequirements(aquatic).isEmpty())
    }

    @Test
    fun `motile species cannot use rooted body as a land habitat shortcut`() {
        val invalid = predator("rooted-predator").copy(
            traits = predator("rooted-predator").traits
                .filterNot { it == CommonTrait.WALKING_LIMBS } +
                CommonTrait.ROOTED_BODY,
        )

        assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }
    }

    @Test
    fun `surface-attached sessile life does not require roots`() {
        val surfaceAttached = producer(
            "surface-attached-producer",
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.PHOTOSYNTHETIC_SURFACE,
                CommonTrait.SURFACE_HOLDFAST,
                CommonTrait.INTERWOVEN_MAT,
            ),
        )

        val compiled = EcologyCompiler.compile(listOf(surfaceAttached)).species.single()

        assertTrue(CommonTrait.ROOTED_BODY !in surfaceAttached.traits)
        assertTrue(compiled.niche.supportFor(Habitat.LAND_SURFACE) > 0.0)
        assertTrue(TraitDependencies.unmetRequirements(surfaceAttached).isEmpty())
    }

    @Test
    fun `leaf structures independently provide photosynthetic tissue`() {
        val structures = listOf(
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.LARGE_EVERGREEN_LEAVES,
            CommonTrait.NEEDLE_LEAVES,
            CommonTrait.DROUGHT_DECIDUOUS_LEAVES,
        )

        structures.forEach { structure ->
            assertEquals(TraitGroup.PHOTOSYNTHETIC_STRUCTURE, structure.group)
            assertTrue(TraitCapability.PHOTOSYNTHETIC_TISSUE in structure.capabilities)

            val definition = producer(
                "${structure.name.lowercase()}-producer",
                traits = listOf(
                    CommonTrait.TEMPERATE_BIOCHEMISTRY,
                    structure,
                    CommonTrait.ROOTED_BODY,
                ),
            )
            val compiled = EcologyCompiler.compile(listOf(definition)).species.single()

            assertTrue(TraitDependencies.unmetRequirements(definition).isEmpty())
            assertTrue(compiled.niche.supportFor(EcoStrategy.PHOTOSYNTHESIS) > 0.0)
        }
    }

    @Test
    fun `habitat and strategy jointly derive the strongest niche`() {
        val species = SpeciesDefinition(
            id = "cloud-sieve",
            displayName = "Cloud sieve",
            sizeClass = SizeClass.TINY,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.MEMBRANOUS_WINGS,
                CommonTrait.GILL_PADS,
            ),
        )
        val ecology = EcologyCompiler.compile(listOf(species))
        val compiled = ecology.species.single()
        val strongest = ecology.niches[compiled.niche.bestNicheIndex()]

        assertEquals(Habitat.AERIAL, strongest.habitat)
        assertEquals(EcoStrategy.FILTER_FEEDING, strongest.strategy)
    }

    @Test
    fun `plant and animal feeding adaptations derive a generalist niche`() {
        val plant = producer("plant")
        val herbivore = SpeciesDefinition(
            id = "herbivore",
            displayName = "herbivore",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.VIVIPARITY,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
            ),
        )
        val omnivore = predator("omnivore").copy(
            traits = predator("omnivore").traits + CommonTrait.GRAZING_MOUTHPARTS,
        )

        val ecology = EcologyCompiler.compile(listOf(plant, herbivore, omnivore))
        val compiled = ecology.species[ecology.speciesIndex("omnivore")]
        val strongest = ecology.niches[compiled.niche.bestNicheIndex()]

        assertEquals(Habitat.LAND_SURFACE, strongest.habitat)
        assertEquals(EcoStrategy.GENERALIST_FORAGING, strongest.strategy)
        assertEquals(
            InteractionKind.GRAZING,
            ecology.interactions.get(compiled.index, ecology.speciesIndex("plant")).kind,
        )
        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(compiled.index, ecology.speciesIndex("herbivore")).kind,
        )
        assertEquals(
            0.0,
            ecology.species[ecology.speciesIndex("herbivore")]
                .niche.supportFor(EcoStrategy.GENERALIST_FORAGING),
        )
    }

    @Test
    fun `brooding traits require external ovospores and provisioning requires a brood site`() {
        val ovospore = predator("ovospore-brooder")
        val viviparous = ovospore.copy(
            id = "viviparous-brooder",
            displayName = "viviparous brooder",
            traits = ovospore.traits
                .filterNot { it == CommonTrait.TERRESTRIAL_OVOSPORE } +
                CommonTrait.VIVIPARITY,
        )
        val namedOvospore = ovospore.copy(
            id = "ovospore-brooder",
            displayName = "ovospore brooder",
        )

        assertTrue(
            TraitDependencies.unmetRequirements(
                viviparous.copy(traits = viviparous.traits + CommonTrait.OVOSPORE_NEST),
            ).isNotEmpty(),
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                namedOvospore.copy(
                    traits = namedOvospore.traits + CommonTrait.BROOD_PROVISIONING,
                ),
            ).isNotEmpty(),
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                namedOvospore.copy(
                    traits = namedOvospore.traits +
                        CommonTrait.OVOSPORE_NEST +
                        CommonTrait.BROOD_PROVISIONING,
                ),
            ).isEmpty(),
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                namedOvospore.copy(
                    traits = namedOvospore.traits + CommonTrait.BODY_CARRIED_OVOSPORES,
                ),
            ).isEmpty(),
        )
    }

    @Test
    fun `brood parasitism requires its authored host relationship`() {
        val host = producer("brood-host")
        val parasiteBase = SpeciesDefinition(
            id = "brood-parasite",
            displayName = "brood parasite",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.BROOD_PARASITISM,
            ),
        )

        assertTrue(TraitDependencies.unmetRequirements(parasiteBase).isNotEmpty())

        val parasite = parasiteBase.copy(
            traits = parasiteBase.traits + broodParasitismOf(host.id, host.displayName),
        )
        val ecology = EcologyCompiler.compile(listOf(host, parasite))
        val parasiteIndex = ecology.speciesIndex(parasite.id)
        val hostIndex = ecology.speciesIndex(host.id)

        assertTrue(TraitDependencies.unmetRequirements(parasite).isEmpty())
        assertTrue(ecology.interactions.get(parasiteIndex, hostIndex).targetRequired)
        assertEquals(
            listOf(parasite, host).map { it.id }.toSet(),
            EcologyAssembly.completeRequiredTargets(
                ecology = ecology,
                selected = listOf(ecology.species[parasiteIndex]),
                availableTargets = ecology.species,
            ).map { it.id }.toSet(),
        )
        assertTrue(
            EcologyAssembly.completeRequiredTargets(
                ecology = ecology,
                selected = listOf(ecology.species[parasiteIndex]),
                availableTargets = emptyList(),
            ).isEmpty(),
        )
    }

    @Test
    fun `specific food creates only the requested directed edge`() {
        val cucumber = producer("aardvark-cucumber")
        val otherProducer = producer("other-producer")
        val aardvark = predator("aardvark").copy(
            traits = predator("aardvark").traits + TargetedRelationshipTrait(
                displayName = "aardvark-cucumber digestion",
                description = "A digestive specialization that makes one otherwise unusual fruit a useful supplemental food.",
                maintenanceCost = 0.03,
                relationships = listOf(
                    RelationshipEffect.SupplementalFood(
                        target = SpeciesSelector.ExactSpecies("aardvark-cucumber"),
                        attackRate = 0.04,
                        assimilationEfficiency = 0.55,
                    ),
                ),
            ),
        )

        val ecology = EcologyCompiler.compile(listOf(cucumber, otherProducer, aardvark))
        val consumer = ecology.speciesIndex("aardvark")
        val cucumberEdge = ecology.interactions.get(consumer, ecology.speciesIndex("aardvark-cucumber"))
        val otherEdge = ecology.interactions.get(consumer, ecology.speciesIndex("other-producer"))

        assertEquals(InteractionKind.SUPPLEMENTAL_FEEDING, cucumberEdge.kind)
        assertEquals(InteractionKind.NONE, otherEdge.kind)
    }

    @Test
    fun `authored relationship effects compose on one interaction edge`() {
        val producer = producer("flowering-producer")
        val consumer = predator("specialist").copy(
            traits = predator("specialist").traits + TargetedRelationshipTrait(
                displayName = "specialist relationship",
                description = "Multiple authored effects on the same target.",
                maintenanceCost = 0.03,
                relationships = listOf(
                    RelationshipEffect.BenefitsTargetWhenFeeding(
                        SpeciesSelector.ExactSpecies(producer.id),
                        benefitRate = 0.07,
                    ),
                    RelationshipEffect.RequiresTarget(
                        SpeciesSelector.ExactSpecies(producer.id),
                    ),
                    RelationshipEffect.SupplementalFood(
                        SpeciesSelector.ExactSpecies(producer.id),
                        attackRate = 0.04,
                        assimilationEfficiency = 0.55,
                    ),
                ),
            ),
        )

        val ecology = EcologyCompiler.compile(listOf(producer, consumer))
        val interaction = ecology.interactions.get(
            ecology.speciesIndex(consumer.id),
            ecology.speciesIndex(producer.id),
        )

        assertEquals(InteractionKind.SUPPLEMENTAL_FEEDING, interaction.kind)
        assertEquals(0.07, interaction.targetBenefitRate)
        assertTrue(interaction.targetRequired)
    }

    @Test
    fun `filter feeders target minuscule motile life and huge or colossal feeders also target tiny life`() {
        val mediumFilter = aquaticFilter("medium-filter", SizeClass.MEDIUM)
        val hugeFilter = aquaticFilter("huge-filter", SizeClass.HUGE)
        val colossalFilter = aquaticFilter("colossal-filter", SizeClass.COLOSSAL)
        val minusculePrey = aquaticPrey("ordinary-minuscule-prey", SizeClass.MINUSCULE)
        val tinyPrey = aquaticPrey("ordinary-tiny-prey", SizeClass.TINY)
        val ecology = EcologyCompiler.compile(
            listOf(
                minusculePrey,
                tinyPrey,
                mediumFilter,
                hugeFilter,
                colossalFilter,
            ),
        )
        val minuscule = ecology.speciesIndex(minusculePrey.id)
        val tiny = ecology.speciesIndex(tinyPrey.id)

        assertEquals(
            InteractionKind.FILTER_FEEDING,
            ecology.interactions.get(ecology.speciesIndex(mediumFilter.id), minuscule).kind,
        )
        assertEquals(
            InteractionKind.NONE,
            ecology.interactions.get(ecology.speciesIndex(mediumFilter.id), tiny).kind,
        )
        assertEquals(
            InteractionKind.FILTER_FEEDING,
            ecology.interactions.get(ecology.speciesIndex(hugeFilter.id), minuscule).kind,
        )
        assertEquals(
            InteractionKind.FILTER_FEEDING,
            ecology.interactions.get(ecology.speciesIndex(hugeFilter.id), tiny).kind,
        )
        assertEquals(
            InteractionKind.FILTER_FEEDING,
            ecology.interactions.get(ecology.speciesIndex(colossalFilter.id), tiny).kind,
        )
    }

    @Test
    fun `huge and colossal aquatic species cannot occupy river habitat`() {
        listOf(SizeClass.HUGE, SizeClass.COLOSSAL).forEach { sizeClass ->
            val base = aquaticFilter("oversized-river-filter-${sizeClass.name}", sizeClass)
            val definition = base.copy(
                traits = base.traits + CommonTrait.EURYHALINE_OSMOREGULATION,
            )
            val ecology = EcologyCompiler.compile(listOf(definition))
            val species = ecology.species.single()
            val river = SeasonalCellEnvironment.create(
                areaKm2 = 40_000.0,
                temperatureC = 20.0,
                insolation = 0.8,
                precipitationMm = 80.0,
                isLand = true,
                adjacentToMajorRiver = 1.0,
            )

            assertEquals(0.0, species.niche.supportFor(Habitat.FRESHWATER))
            assertEquals(-1, NicheSelection.choose(species, ecology, river))
        }
    }

    @Test
    fun `medium predators do not use tiny aggregate insects as prey`() {
        val mediumPredator = predator("medium-predator", SizeClass.MEDIUM)
        val smallPrey = predator("small-prey", SizeClass.SMALL).copy(
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.VIVIPARITY,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
            ),
        )
        val ecology = EcologyCompiler.compile(
            listOf(InvariantSpecies.BUGS, smallPrey, mediumPredator),
        )
        val consumer = ecology.speciesIndex(mediumPredator.id)

        assertEquals(
            InteractionKind.NONE,
            ecology.interactions.get(consumer, ecology.speciesIndex(InvariantSpecies.BUGS.id)).kind,
        )
        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(consumer, ecology.speciesIndex(smallPrey.id)).kind,
        )
    }

    @Test
    fun `medium aquatic predators can use tiny aquatic life without opening that prey to large hunters`() {
        val mediumPredator = aquaticPredator("medium-aquatic-predator", SizeClass.MEDIUM)
        val largePredator = aquaticPredator("large-aquatic-predator", SizeClass.LARGE)
        val ecology = EcologyCompiler.compile(
            listOf(InvariantSpecies.SMALL_AQUATIC_LIFE, mediumPredator, largePredator),
        )
        val prey = ecology.speciesIndex(InvariantSpecies.SMALL_AQUATIC_LIFE.id)

        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(ecology.speciesIndex(mediumPredator.id), prey).kind,
        )
        assertEquals(
            InteractionKind.NONE,
            ecology.interactions.get(ecology.speciesIndex(largePredator.id), prey).kind,
        )
    }

    @Test
    fun `terrestrial grazers consume the modeled carpet plant population`() {
        val grazer = predator("grazer", SizeClass.SMALL).copy(
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.VIVIPARITY,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
            ),
        )
        val ecology = EcologyCompiler.compile(listOf(InvariantSpecies.CARPET_PLANTS, grazer))

        assertEquals(
            InteractionKind.GRAZING,
            ecology.interactions.get(
                ecology.speciesIndex(grazer.id),
                ecology.speciesIndex(InvariantSpecies.CARPET_PLANTS.id),
            ).kind,
        )
    }

    @Test
    fun `separated marine predator tiers are not treated as intraguild competitors`() {
        val definitions = listOf("antarctic-silverfish", "harbor-seal", "orca").map { id ->
            EarthSpeciesCatalog.ALL.single { it.id == id }
        }
        val ecology = EcologyCompiler.compile(definitions)
        val orca = ecology.species.single { it.id == "orca" }
        val seal = ecology.species.single { it.id == "harbor-seal" }
        val silverfish = ecology.species.single { it.id == "antarctic-silverfish" }

        fun undiscountedAttack(consumer: CompiledSpecies, target: CompiledSpecies): Double {
            val support = maxOf(
                consumer.niche.supportFor(EcoStrategy.AMBUSH_PREDATION),
                consumer.niche.supportFor(EcoStrategy.PURSUIT_PREDATION),
            )
            val pursuit =
                consumer.niche.supportFor(EcoStrategy.PURSUIT_PREDATION) >
                    consumer.niche.supportFor(EcoStrategy.AMBUSH_PREDATION)
            val capture = consumer.interactions.captureAbility + if (pursuit) consumer.interactions.pursuitSpeed else 0.0
            val defense = target.interactions.defense + if (pursuit) target.interactions.pursuitSpeed else 0.0
            return (0.07 * support * capture / maxOf(0.25, defense)).coerceIn(0.0, 0.25)
        }

        listOf(orca to seal, seal to silverfish).forEach { (consumer, target) ->
            val interaction = ecology.interactions.get(consumer.index, target.index)
            val attack = undiscountedAttack(consumer, target)
            assertEquals(InteractionKind.PREDATION, interaction.kind)
            assertEquals(attack, interaction.targetLossRate, 1.0e-12)
            assertEquals(attack * 1.30, interaction.consumerGainRate, 1.0e-12)
        }
    }

    @Test
    fun `attached and suspended photosynthesizers compile to separate competition layers`() {
        val waterLily = EarthSpeciesCatalog.ALL.single { it.id == "white-water-lily" }
        val ecology = EcologyCompiler.compile(listOf(waterLily, InvariantSpecies.PLANKTON))

        assertEquals(
            ProducerCompetitionLayer.ATTACHED,
            ecology.species.single { it.id == waterLily.id }.niche.producerCompetitionLayer,
        )
        assertEquals(
            ProducerCompetitionLayer.SUSPENDED,
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }
                .niche.producerCompetitionLayer,
        )
    }

    @Test
    fun `invariant traits are unavailable to evolving species`() {
        val invalid = predator("invalid-invariant").copy(
            traits = predator("invalid-invariant").traits + CommonTrait.INVARIANT_RESISTANCE,
        )

        assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }
    }

    @Test
    fun `invariant guilds compile as ordinary populations with explicit metadata`() {
        val ecology = EcologyCompiler.compile(InvariantSpecies.ALL)

        assertEquals(5, ecology.species.size)
        assertTrue(ecology.species.all { it.kind == SpeciesKind.INVARIANT })
        assertTrue(ecology.species.all { it.lifeHistory.dormancyKind == DormancyKind.PROPAGULE })
        assertTrue(ecology.species.all { it.lifeHistory.nicheCompetitionSensitivity < 0.20 })
        assertEquals(
            0.10,
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }
                .lifeHistory.dormantEntryBiomassRetention,
        )
        assertEquals(
            10.0,
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }
                .lifeHistory.dormantReactivationMultiplier,
        )
        assertTrue(
            ecology.species
                .filterNot { it.id == InvariantSpecies.PLANKTON.id }
                .all { it.lifeHistory.dormantEntryBiomassRetention == 1.0 },
        )
    }

    @Test
    fun `all authored non-foundation traits declare an effect and a maintenance adjustment`() {
        val authoredTraits: List<SpeciesTrait> = CommonTrait.entries + ColorTrait.entries
        authoredTraits.forEach { trait ->
            assertTrue(
                trait.description.isNotBlank(),
                "${trait.displayName} has no player-facing description",
            )
        }
        authoredTraits.filterNot { it.isFoundation }.forEach { trait ->
            assertTrue(
                trait.effects.any { it is TraitEffect.MaintenanceCost && it.fraction != 0.0 },
                "${trait.displayName} has no explicit non-zero maintenance adjustment",
            )
            assertTrue(
                trait.effects.any { it !is TraitEffect.MaintenanceCost },
                "${trait.displayName} has no non-maintenance effect",
            )
        }
    }

    private fun producer(
        id: String = "producer",
        traits: List<SpeciesTrait> = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
        ),
    ) = SpeciesDefinition(
        id = id,
        displayName = id,
        sizeClass = SizeClass.SMALL,
        motile = false,
        traits = withReproduction(traits, CommonTrait.TERRESTRIAL_OVOSPORE) +
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
    )

    private fun predator(
        id: String,
        sizeClass: SizeClass = SizeClass.MEDIUM,
    ) = SpeciesDefinition(
        id = id,
        displayName = id,
        sizeClass = sizeClass,
        motile = true,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.TERRESTRIAL_OVOSPORE,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_CAMOUFLAGE,
        ),
    )

    private fun aquaticFilter(
        id: String,
        sizeClass: SizeClass,
    ) = SpeciesDefinition(
        id = id,
        displayName = id,
        sizeClass = sizeClass,
        motile = true,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.BALEEN,
        ),
    )

    private fun aquaticPrey(
        id: String,
        sizeClass: SizeClass,
    ) = SpeciesDefinition(
        id = id,
        displayName = id,
        sizeClass = sizeClass,
        motile = true,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.BUOYANCY_BLADDER,
        ),
    )

    private fun aquaticPredator(
        id: String,
        sizeClass: SizeClass,
    ) = SpeciesDefinition(
        id = id,
        displayName = id,
        sizeClass = sizeClass,
        motile = true,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.AMBUSH_MUSCULATURE,
        ),
    )

    private fun withReproduction(
        traits: List<SpeciesTrait>,
        default: CommonTrait,
    ): List<SpeciesTrait> = if (traits.any { TraitCapability.REPRODUCTION in it.capabilities }) {
        traits
    } else {
        traits + default
    }
}
