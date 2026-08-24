package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

private val selfCrowdingTestTrait = EffectTrait(
    displayName = "self-crowding behavior",
    description = "Behavior that makes conspecific resource use overlap more strongly.",
    effects = listOf(
        TraitEffect.SelfCrowdingSensitivity(1.35),
        TraitEffect.MaintenanceCost(0.01),
    ),
)

class EcologyCompilerTest {
    @Test
    fun `self crowding traits compile independently of niche competition`() {
        val ordinary = predator("ordinary-self-crowding")
        val crowdingSensitive = ordinary.copy(
            id = "crowding-sensitive",
            displayName = "crowding-sensitive",
            traits = ordinary.traits + selfCrowdingTestTrait,
        )

        val compiled = EcologyCompiler.compile(listOf(ordinary, crowdingSensitive)).species
        val ordinaryCompiled = compiled.single { it.id == ordinary.id }
        val crowdingSensitiveCompiled = compiled.single { it.id == crowdingSensitive.id }

        assertEquals(1.0, ordinaryCompiled.lifeHistory.selfCrowdingSensitivity)
        assertEquals(1.35, crowdingSensitiveCompiled.lifeHistory.selfCrowdingSensitivity)
        assertEquals(
            ordinaryCompiled.lifeHistory.nicheCompetitionSensitivity,
            crowdingSensitiveCompiled.lifeHistory.nicheCompetitionSensitivity,
        )
    }

    @Test
    fun `territorial behavior increases self crowding`() {
        val ordinary = predator("ordinary-territory")
        val territorial = ordinary.copy(
            id = "territorial",
            displayName = "territorial",
            traits = ordinary.traits + CommonTrait.TERRITORIAL,
        )

        val compiled = EcologyCompiler.compile(listOf(ordinary, territorial)).species

        assertEquals(1.0, compiled.single { it.id == ordinary.id }.lifeHistory.selfCrowdingSensitivity)
        assertEquals(1.35, compiled.single { it.id == territorial.id }.lifeHistory.selfCrowdingSensitivity)
    }

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

        assertEquals(0.75, profile.supportFor(Habitat.LAND_SURFACE), message = "Compiled niche profile owns its optimized arrays: expected `profile.supportFor(Habitat.LAND_SURFACE)` to match `0.75`")
        assertEquals(0.60, profile.supportFor(EcoStrategy.GRAZING), message = "Compiled niche profile owns its optimized arrays: expected `profile.supportFor(EcoStrategy.GRAZING)` to match `0.60`")
        assertEquals(0.40, profile.camouflageFor(Habitat.LAND_SURFACE), message = "Compiled niche profile owns its optimized arrays: expected `profile.camouflageFor(Habitat.LAND_SURFACE)` to match `0.40`")
        assertEquals(0.45, profile.fitFor(0), message = "Compiled niche profile owns its optimized arrays: expected `profile.fitFor(0)` to match `0.45`")
    }

    @Test
    fun `traits compile into climate and niche parameters`() {
        val producer = producer(
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.PHOTOSYNTHETIC_SURFACE,
                CommonTrait.ROOTED_BODY,
                CommonTrait.FUR,
                CommonTrait.DENSE_UNDERCOAT,
            ),
        )

        val compiled = EcologyCompiler.compile(listOf(producer)).species.single()

        assertEquals(-4.0, compiled.physiology.thermal.outerLowC, message = "Traits compile into climate and niche parameters: expected `compiled.physiology.thermal.outerLowC` to match `-4.0`")
        assertEquals(26.0, compiled.physiology.thermal.outerHighC, message = "Traits compile into climate and niche parameters: expected `compiled.physiology.thermal.outerHighC` to match `26.0`")
        assertTrue(compiled.niche.hasViableNiche(), message = "Traits compile into climate and niche parameters: expected `compiled.niche.hasViableNiche()` to be true")
        assertTrue(compiled.physiology.maintenanceDemand > 0.0, message = "Traits compile into climate and niche parameters: expected `compiled.physiology.maintenanceDemand > 0.0` to be true")
    }

    @Test
    fun `motile species require an explicitly authored social organization`() {
        val unsocial = predator("unsocial").copy(
            traits = predator("unsocial").traits.filter {
                it.group != TraitGroup.SOCIAL_ORGANIZATION
            },
        )

        val exception = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(unsocial))
        }

        assertTrue(exception.message.orEmpty().contains("social organization"))
    }

    @Test
    fun `size foundation establishes mass and slightly widens temperature range`() {
        val small = EcologyCompiler.compile(listOf(predator("small", SizeClass.SMALL))).species.single()
        val huge = EcologyCompiler.compile(listOf(predator("huge", SizeClass.HUGE))).species.single()

        assertEquals(SizeClass.SMALL.typicalMassKg, small.physiology.massKg, message = "Size foundation establishes mass and slightly widens temperature range: expected `small.physiology.massKg` to match `SizeClass.SMALL.typicalMassKg`")
        assertEquals(SizeClass.HUGE.typicalMassKg, huge.physiology.massKg, message = "Size foundation establishes mass and slightly widens temperature range: expected `huge.physiology.massKg` to match `SizeClass.HUGE.typicalMassKg`")
        assertTrue(
            huge.physiology.thermal.outerLowC < small.physiology.thermal.outerLowC,
            message = "Size foundation establishes mass and slightly widens temperature range: expected `huge.physiology.thermal.outerLowC < small.physiology.thermal.outerLowC` to be true"
        )
        assertTrue(
            huge.physiology.thermal.outerHighC > small.physiology.thermal.outerHighC,
            message = "Size foundation establishes mass and slightly widens temperature range: expected `huge.physiology.thermal.outerHighC > small.physiology.thermal.outerHighC` to be true"
        )
    }

    @Test
    fun `body build adjusts mass without changing size class`() {
        val ordinary = predator("ordinary-build", SizeClass.MEDIUM)
        val slender = predator("slender-build", SizeClass.MEDIUM).copy(
            traits = predator("slender-build", SizeClass.MEDIUM).traits +
                CommonTrait.SLENDER_PHYSIQUE,
        )
        val bulky = predator("bulky-build", SizeClass.MEDIUM).copy(
            traits = predator("bulky-build", SizeClass.MEDIUM).traits +
                CommonTrait.BULKY_PHYSIQUE,
        )
        val ecology = EcologyCompiler.compile(listOf(ordinary, slender, bulky))

        fun compiled(id: String) = ecology.species[ecology.speciesIndex(id)]

        assertEquals(
            SizeClass.MEDIUM.typicalMassKg,
            compiled(ordinary.id).physiology.massKg,
            message = "Body build adjusts mass without changing size class: expected `compiled(ordinary.id).physiology.massKg` to match `SizeClass.MEDIUM.typicalMassKg`"
        )
        assertEquals(
            SizeClass.MEDIUM.typicalMassKg * 0.5,
            compiled(slender.id).physiology.massKg,
            message = "Body build adjusts mass without changing size class: expected `compiled(slender.id).physiology.massKg` to match `SizeClass.MEDIUM.typicalMassKg * 0.5`"
        )
        assertEquals(
            SizeClass.MEDIUM.typicalMassKg * 2.0,
            compiled(bulky.id).physiology.massKg,
            message = "Body build adjusts mass without changing size class: expected `compiled(bulky.id).physiology.massKg` to match `SizeClass.MEDIUM.typicalMassKg * 2.0`"
        )
        assertEquals(SizeClass.MEDIUM, compiled(slender.id).sizeClass, message = "Body build adjusts mass without changing size class: expected `compiled(slender.id).sizeClass` to match `SizeClass.MEDIUM`")
        assertEquals(SizeClass.MEDIUM, compiled(bulky.id).sizeClass, message = "Body build adjusts mass without changing size class: expected `compiled(bulky.id).sizeClass` to match `SizeClass.MEDIUM`")
    }

    @Test
    fun `specialized senses compile distinct hunting and reproductive benefits`() {
        val ordinary = predator("ordinary-senses").copy(
            traits = predator("ordinary-senses").traits + CommonTrait.MOTION_TRACKING_SENSES,
        )
        val scent = predator("scent-specialist").copy(
            traits = predator("scent-specialist").traits +
                listOf(CommonTrait.MOTION_TRACKING_SENSES, CommonTrait.KEEN_SCENT_SENSE),
        )
        val sight = predator("sight-specialist").copy(
            traits = predator("sight-specialist").traits +
                listOf(CommonTrait.MOTION_TRACKING_SENSES, CommonTrait.KEEN_EYESIGHT),
        )
        val prey = predator("sensory-prey", SizeClass.SMALL)
        val ecology = EcologyCompiler.compile(listOf(ordinary, scent, sight, prey))

        fun compiled(id: String) = ecology.species[ecology.speciesIndex(id)]
        fun predationRate(predator: SpeciesDefinition) =
            ecology.interactions.get(ecology.speciesIndex(predator.id), ecology.speciesIndex(prey.id)).targetLossRate

        assertTrue(
            compiled(scent.id).interactions.sensing > compiled(ordinary.id).interactions.sensing,
            message = "Specialized senses compile distinct hunting and reproductive benefits: expected `compiled(scent.id).interactions.sensing > compiled(ordinary.id).interactions.sensing` to be true"
        )
        assertEquals(
            compiled(ordinary.id).interactions.pursuitSpeed,
            compiled(scent.id).interactions.pursuitSpeed,
            message = "Specialized senses compile distinct hunting and reproductive benefits: expected `compiled(scent.id).interactions.pursuitSpeed` to match `compiled(ordinary.id).interactions.pursuitSpeed`"
        )
        assertTrue(
            compiled(scent.id).lifeHistory.seasonalReproduction > compiled(ordinary.id).lifeHistory.seasonalReproduction,
            message = "Specialized senses compile distinct hunting and reproductive benefits: expected `compiled(scent.id).lifeHistory.seasonalReproduction > compiled(ordinary.id).lifeHistory.seasonalReproduction` to be true"
        )
        assertTrue(
            compiled(sight.id).interactions.captureAbility > compiled(ordinary.id).interactions.captureAbility,
            message = "Specialized senses compile distinct hunting and reproductive benefits: expected `compiled(sight.id).interactions.captureAbility > compiled(ordinary.id).interactions.captureAbility` to be true"
        )
        assertTrue(predationRate(scent) > predationRate(ordinary), message = "Specialized senses compile distinct hunting and reproductive benefits: expected `predationRate(scent) > predationRate(ordinary)` to be true")
        assertTrue(predationRate(sight) > predationRate(ordinary), message = "Specialized senses compile distinct hunting and reproductive benefits: expected `predationRate(sight) > predationRate(ordinary)` to be true")
    }

    @Test
    fun `keen and poor sensory traits are mutually exclusive`() {
        listOf(
            CommonTrait.KEEN_EYESIGHT to CommonTrait.POOR_VISION,
            CommonTrait.KEEN_HEARING to CommonTrait.POOR_HEARING,
            CommonTrait.KEEN_SCENT_SENSE to CommonTrait.POOR_SCENT_SENSE,
        ).forEach { (keen, poor) ->
            val conflicting = predator("${keen.name}-${poor.name}").copy(
                traits = predator("${keen.name}-${poor.name}").traits + listOf(keen, poor),
            )

            assertFailsWith<IllegalArgumentException> {
                EcologyCompiler.compile(listOf(conflicting))
            }
        }
    }

    @Test
    fun `sensing protects prey against ambush predation`() {
        val ambusher = predator("ambusher", SizeClass.LARGE)
        val ordinaryPrey = terrestrialPrey("ordinary-prey", SizeClass.SMALL)
        val alertPrey = ordinaryPrey.copy(
            id = "alert-prey",
            displayName = "alert-prey",
            traits = ordinaryPrey.traits + CommonTrait.KEEN_HEARING,
        )
        val ecology = EcologyCompiler.compile(listOf(ambusher, ordinaryPrey, alertPrey))

        fun predationRate(prey: SpeciesDefinition) =
            ecology.interactions.get(ecology.speciesIndex(ambusher.id), ecology.speciesIndex(prey.id)).targetLossRate

        assertTrue(
            ecology.species[ecology.speciesIndex(alertPrey.id)].interactions.sensing > 0.0,
            message = "Sensing protects prey against ambush predation: expected `ecology.species[ecology.speciesIndex(alertPrey.id)].interactions.sensing > 0.0` to be true"
        )
        assertTrue(predationRate(alertPrey) < predationRate(ordinaryPrey), message = "Sensing protects prey against ambush predation: expected `predationRate(alertPrey) < predationRate(ordinaryPrey)` to be true")
    }

    @Test
    fun `fast metabolism raises activity reproduction and food demand`() {
        val ordinary = predator("ordinary-metabolism")
        val fast = predator("fast-metabolism").copy(
            traits = predator("fast-metabolism").traits + CommonTrait.FAST_METABOLISM,
        )
        val ecology = EcologyCompiler.compile(listOf(ordinary, fast))

        fun compiled(id: String) = ecology.species[ecology.speciesIndex(id)]

        assertTrue(
            compiled(fast.id).physiology.maintenanceDemand > compiled(ordinary.id).physiology.maintenanceDemand,
            message = "Fast metabolism raises activity reproduction and food demand: expected `compiled(fast.id).physiology.maintenanceDemand > compiled(ordinary.id).physiology.maintenanceDemand` to be true"
        )
        assertTrue(
            compiled(fast.id).lifeHistory.seasonalReproduction > compiled(ordinary.id).lifeHistory.seasonalReproduction,
            message = "Fast metabolism raises activity reproduction and food demand: expected `compiled(fast.id).lifeHistory.seasonalReproduction > compiled(ordinary.id).lifeHistory.seasonalReproduction` to be true"
        )
        assertTrue(
            compiled(fast.id).interactions.pursuitSpeed > compiled(ordinary.id).interactions.pursuitSpeed,
            message = "Fast metabolism raises activity reproduction and food demand: expected `compiled(fast.id).interactions.pursuitSpeed > compiled(ordinary.id).interactions.pursuitSpeed` to be true"
        )
    }

    @Test
    fun `fast and slow metabolisms are mutually exclusive`() {
        val invalid = predator("conflicting-metabolism").copy(
            traits = predator("conflicting-metabolism").traits +
                listOf(CommonTrait.FAST_METABOLISM, CommonTrait.SLOW_METABOLISM),
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("METABOLIC_PACE"), message = "Fast and slow metabolisms are mutually exclusive: expected `failure.message.orEmpty().contains(\"METABOLIC_PACE\")` to be true")
    }

    @Test
    fun `motile species default to neighbor dispersal while sessile species do not`() {
        val motile = predator("default-motile-dispersal")
        val sessile = producer(id = "default-sessile-dispersal")
        val ecology = EcologyCompiler.compile(listOf(motile, sessile))

        assertEquals(
            DispersalKind.NEIGHBOR,
            ecology.species[ecology.speciesIndex(motile.id)].lifeHistory.dispersalKind,
            message = "Motile species default to neighbor dispersal while sessile species do not: expected `ecology.species[ecology.speciesIndex(motile.id)].lifeHistory.dispersalKind` to match `DispersalKind.NEIGHBOR`",
        )
        assertEquals(
            DispersalKind.NONE,
            ecology.species[ecology.speciesIndex(sessile.id)].lifeHistory.dispersalKind,
            message = "Motile species default to neighbor dispersal while sessile species do not: expected `ecology.species[ecology.speciesIndex(sessile.id)].lifeHistory.dispersalKind` to match `DispersalKind.NONE`",
        )
    }

    @Test
    fun `slow growth and infrequent reproduction trade population growth for lower maintenance`() {
        val ordinary = predator("ordinary-life-history")
        val slowGrowing = predator("slow-growing").copy(
            traits = predator("slow-growing").traits + CommonTrait.SLOW_GROWTH,
        )
        val infrequent = predator("infrequent-reproducer").copy(
            traits = predator("infrequent-reproducer").traits + CommonTrait.INFREQUENT_REPRODUCTION,
        )
        val ecology = EcologyCompiler.compile(listOf(ordinary, slowGrowing, infrequent))

        fun compiled(id: String) = ecology.species[ecology.speciesIndex(id)]

        assertTrue(
            compiled(slowGrowing.id).physiology.maintenanceDemand < compiled(ordinary.id).physiology.maintenanceDemand,
            message = "Slow growth and infrequent reproduction trade population growth for lower maintenance: expected `compiled(slowGrowing.id).physiology.maintenanceDemand < compiled(ordinary.id).physiology.maintenanceDemand` to be true"
        )
        assertTrue(
            compiled(slowGrowing.id).lifeHistory.seasonalReproduction < compiled(ordinary.id).lifeHistory.seasonalReproduction,
            message = "Slow growth and infrequent reproduction trade population growth for lower maintenance: expected `compiled(slowGrowing.id).lifeHistory.seasonalReproduction < compiled(ordinary.id).lifeHistory.seasonalReproduction` to be true"
        )
        assertTrue(
            compiled(infrequent.id).physiology.maintenanceDemand < compiled(ordinary.id).physiology.maintenanceDemand,
            message = "Slow growth and infrequent reproduction trade population growth for lower maintenance: expected `compiled(infrequent.id).physiology.maintenanceDemand < compiled(ordinary.id).physiology.maintenanceDemand` to be true"
        )
        assertTrue(
            compiled(infrequent.id).lifeHistory.seasonalReproduction < compiled(slowGrowing.id).lifeHistory.seasonalReproduction,
            message = "Slow growth and infrequent reproduction trade population growth for lower maintenance: expected `compiled(infrequent.id).lifeHistory.seasonalReproduction < compiled(slowGrowing.id).lifeHistory.seasonalReproduction` to be true"
        )
    }

    @Test
    fun `motile species require exactly one thermal strategy`() {
        val invalid = SpeciesDefinition(
            id = "invalid",
            displayName = "Invalid swimmer",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.AQUATIC_OVOSPORE,
                CommonTrait.BUOYANCY_BLADDER,
                CommonTrait.SUSPENSION_FEEDING_TENTACLES,
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

        assertTrue(failure.message.orEmpty().contains("DOMINANT_BODY_COVERING"), message = "Traits in the same biological group are mutually exclusive: expected `failure.message.orEmpty().contains(\"DOMINANT_BODY_COVERING\")` to be true")
        assertTrue(failure.message.orEmpty().contains(CommonTrait.FUR.displayName), message = "Traits in the same biological group are mutually exclusive: expected `failure.message.orEmpty().contains(CommonTrait.FUR.displayName)` to be true")
        assertTrue(
            failure.message.orEmpty().contains(CommonTrait.FEATHERS.displayName),
            message = "Traits in the same biological group are mutually exclusive: expected `failure.message.orEmpty().contains(CommonTrait.FEATHERS.displayName)` to be true"
        )
    }

    @Test
    fun `every declared trait group has multiple authored alternatives`() {
        val groupedTraits =
            (CommonTrait.entries + ColorTrait.entries)
                .filter { it.group != null }
                .groupBy { it.group }

        assertEquals(
            TraitGroup.entries.toSet(),
            groupedTraits.keys.filterNotNull().toSet(),
            message = "Every declared trait group has multiple authored alternatives: expected `groupedTraits.keys.filterNotNull().toSet()` to match `TraitGroup.entries.toSet()`"
        )
        groupedTraits.forEach { (group, traits) ->
            assertTrue(traits.size >= 2, "$group has fewer than two alternatives")
        }
    }

    @Test
    fun `biological color is compiled from mutually exclusive traits`() {
        val brownPredator = predator("brown-predator")
        val compiled = EcologyCompiler.compile(listOf(brownPredator)).species.single()
        assertEquals(BiologicalColor.BROWN, compiled.niche.camouflageColor, message = "Biological color is compiled from mutually exclusive traits: expected `compiled.niche.camouflageColor` to match `BiologicalColor.BROWN`")

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
            message = "Biological color is compiled from mutually exclusive traits: expected `adaptiveCompiled.physiology.maintenanceDemand > compiled.physiology.maintenanceDemand` to be true",
        )

        val conflicting = brownPredator.copy(
            traits = brownPredator.traits + ColorTrait.WHITE_CAMOUFLAGE,
        )
        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(conflicting))
        }
        assertTrue(failure.message.orEmpty().contains("BIOLOGICAL_COLOR"), message = "Biological color is compiled from mutually exclusive traits: expected `failure.message.orEmpty().contains(\"BIOLOGICAL_COLOR\")` to be true")
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
    fun `birdsong requires both feathers and a chirping call`() {
        val base = predator("singer")
        val featheredSinger = base.copy(
            traits = base.traits + CommonTrait.FEATHERS + CommonTrait.COMPLEX_VOCALIZATIONS,
        )
        val chirpingSinger = featheredSinger.copy(
            traits = featheredSinger.traits + CommonTrait.CHIRPING_CALL,
        )

        assertTrue(TraitDependencies.unmetRequirements(featheredSinger).isNotEmpty(), message = "Birdsong requires both feathers and a chirping call: expected `TraitDependencies.unmetRequirements(featheredSinger).isNotEmpty()` to be true")
        assertTrue(TraitDependencies.unmetRequirements(chirpingSinger).isEmpty(), message = "Birdsong requires both feathers and a chirping call: expected `TraitDependencies.unmetRequirements(chirpingSinger).isEmpty()` to be true")
    }

    @Test
    fun `every compiled species requires at least one reproductive strategy`() {
        val invalid = SpeciesDefinition(
            id = "reproduction-missing",
            displayName = "Reproduction missing",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.LIMBED_BODY,
                CommonTrait.WALKING_LIMBS,
            ),
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("reproductive strategy"), message = "Every compiled species requires at least one reproductive strategy: expected `failure.message.orEmpty().contains(\"reproductive strategy\")` to be true")
    }

    @Test
    fun `reproductive strategies compose and aerial dispersal requires ovospores`() {
        val mixedStrategy = predator("mixed-reproduction").copy(
            traits = predator("mixed-reproduction").traits + CommonTrait.CLONAL_PROPAGATION,
        )
        EcologyCompiler.compile(listOf(mixedStrategy))

        listOf(CommonTrait.TERRESTRIAL_OVOSPORE, CommonTrait.AQUATIC_OVOSPORE).forEach { ovospore ->
            assertTrue(TraitCapability.REPRODUCTION in ovospore.capabilities, message = "Reproductive strategies compose and aerial dispersal requires ovospores: expected `TraitCapability.REPRODUCTION in ovospore.capabilities` to be true")
            assertTrue(
                TraitCapability.OVOSPORE_REPRODUCTION in ovospore.capabilities,
                message = "Reproductive strategies compose and aerial dispersal requires ovospores: expected `TraitCapability.OVOSPORE_REPRODUCTION in ovospore.capabilities` to be true"
            )
            assertTrue(TraitCapability.OVOSPORE_BROODING in ovospore.capabilities, message = "Reproductive strategies compose and aerial dispersal requires ovospores: expected `TraitCapability.OVOSPORE_BROODING in ovospore.capabilities` to be true")
        }
        assertTrue(
            TraitCapability.REPRODUCTION in CommonTrait.VIVIPARITY.capabilities,
            message = "Reproductive strategies compose and aerial dispersal requires ovospores: expected `TraitCapability.REPRODUCTION in CommonTrait.VIVIPARITY.capabilities` to be true"
        )
        assertTrue(
            TraitCapability.REPRODUCTION in CommonTrait.CLONAL_PROPAGATION.capabilities,
            message = "Reproductive strategies compose and aerial dispersal requires ovospores: expected `TraitCapability.REPRODUCTION in CommonTrait.CLONAL_PROPAGATION.capabilities` to be true"
        )

        val invalidAerialDisperser = predator("invalid-aerial-disperser").copy(
            traits = predator("invalid-aerial-disperser").traits
                .filterNot { TraitCapability.REPRODUCTION in it.capabilities } +
                CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
        )
        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalidAerialDisperser))
        }
        assertTrue(failure.message.orEmpty().contains("OVOSPORE_REPRODUCTION"), message = "Reproductive strategies compose and aerial dispersal requires ovospores: expected `failure.message.orEmpty().contains(\"OVOSPORE_REPRODUCTION\")` to be true")
    }

    @Test
    fun `thermal foundation compiles to an explicit runtime strategy`() {
        val compiled = EcologyCompiler.compile(
            listOf(predator("explicit-thermal-strategy")),
        ).species.single()

        assertEquals(ThermalStrategy.ENDOTHERMY, compiled.physiology.thermal.regulation, message = "Thermal foundation compiles to an explicit runtime strategy: expected `compiled.physiology.thermal.regulation` to match `ThermalStrategy.ENDOTHERMY`")
    }

    @Test
    fun `floating body provides aerial habitat independently of photosynthesis`() {
        val floating = SpeciesDefinition(
            id = "floating-body",
            displayName = "Floating body",
            sizeClass = SizeClass.MINUSCULE,
            motile = true,
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.SOLITARY,
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

        assertTrue(floatingProfile.supportFor(Habitat.AERIAL) > 0.0, message = "Floating body provides aerial habitat independently of photosynthesis: expected `floatingProfile.supportFor(Habitat.AERIAL) > 0.0` to be true")
        assertEquals(0.0, floatingProfile.supportFor(EcoStrategy.PHOTOSYNTHESIS), message = "Floating body provides aerial habitat independently of photosynthesis: expected `floatingProfile.supportFor(EcoStrategy.PHOTOSYNTHESIS)` to match `0.0`")
        assertTrue(photosyntheticProfile.supportFor(Habitat.AERIAL) > 0.0, message = "Floating body provides aerial habitat independently of photosynthesis: expected `photosyntheticProfile.supportFor(Habitat.AERIAL) > 0.0` to be true")
        assertTrue(
            photosyntheticProfile.supportFor(EcoStrategy.PHOTOSYNTHESIS) > 0.0,
            message = "Floating body provides aerial habitat independently of photosynthesis: expected `photosyntheticProfile.supportFor(EcoStrategy.PHOTOSYNTHESIS) > 0.0` to be true"
        )
    }

    @Test
    fun `floating body requires minuscule size`() {
        val invalid = SpeciesDefinition(
            id = "oversized-floater",
            displayName = "Oversized floater",
            sizeClass = SizeClass.TINY,
            motile = true,
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.CLONAL_PROPAGATION,
                CommonTrait.FLOATING_BODY,
            ),
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("requires MINUSCULE size"), message = "Floating body requires minuscule size: expected `failure.message.orEmpty().contains(\"requires MINUSCULE size\")` to be true")
    }

    @Test
    fun `limb regrowth requires a limbed body`() {
        val invalid = SpeciesDefinition(
            id = "limbless-regenerator",
            displayName = "Limbless regenerator",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.UNDULATING_BODY,
                CommonTrait.LIMB_REGROWTH,
            ),
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(
            failure.message.orEmpty().contains("requires one of LIMBED_BODY, TENTACLES"),
            message = "Limb regrowth requires limbs or tentacles: expected `failure.message.orEmpty().contains(\"requires one of LIMBED_BODY, TENTACLES\")` to be true",
        )
    }

    @Test
    fun `deep diving accepts either underwater respiration or breath holding`() {
        val base = SpeciesDefinition(
            id = "deep-diver",
            displayName = "Deep diver",
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.AQUATIC_OVOSPORE,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.LIMBED_BODY,
                CommonTrait.AQUATIC_LIMBS,
                CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            ),
        )

        val missingRespiration = TraitDependencies.unmetRequirements(base)
        assertEquals(1, missingRespiration.size, message = "Deep diving accepts either underwater respiration or breath holding: expected `missingRespiration.size` to match `1`")
        assertTrue(
            missingRespiration.single().requirement is TraitRequirement.AnyOf,
            message = "Deep diving accepts either underwater respiration or breath holding: expected `missingRespiration.single().requirement is TraitRequirement.AnyOf` to be true"
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.GILLS),
            ).isEmpty(),
            message = "Deep diving accepts either underwater respiration or breath holding: expected `TraitDependencies.unmetRequirements( base.copy(traits = base.traits + CommonTrait.GILLS), ).isEmpty()` to be true",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.PROLONGED_BREATH_HOLDING),
            ).isEmpty(),
            message = "Deep diving accepts either underwater respiration or breath holding: expected `TraitDependencies.unmetRequirements( base.copy(traits = base.traits + CommonTrait.PROLONGED_BREATH_HOLDING), ).isEmpty()` to be true",
        )
    }

    @Test
    fun `pelagic soaring wings require a flight structure`() {
        val invalid = predator("flightless-soarer").copy(
            traits = predator("flightless-soarer").traits + CommonTrait.PELAGIC_SOARING,
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("requires WINGS"), message = "Pelagic soaring wings require a flight structure: expected `failure.message.orEmpty().contains(\"requires WINGS\")` to be true")
    }

    @Test
    fun `specialized anatomy requires its underlying structure`() {
        val base = predator("anatomy")

        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.DENSE_UNDERCOAT),
            ).single().requirement is TraitRequirement.AllOf,
            message = "Specialized anatomy requires its underlying structure: expected `TraitDependencies.unmetRequirements( base.copy(traits = base.traits + CommonTrait.DENSE_UNDERCOAT), ).single().requirement is TraitRequirement.AllOf` to be true",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.SWIFT_LEGS),
            ).isEmpty(),
            message = "Specialized anatomy requires its underlying structure: expected `TraitDependencies.unmetRequirements( base.copy(traits = base.traits + CommonTrait.SWIFT_LEGS), ).isEmpty()` to be true",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(
                    traits = base.traits
                        .filterNot { it == CommonTrait.WALKING_LIMBS } +
                        CommonTrait.SWIFT_LEGS,
                ),
            ).isNotEmpty(),
            message = "Assertion failed",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.STICKY_FEET),
            ).isNotEmpty(),
            message = "Specialized anatomy requires its underlying structure: expected `TraitDependencies.unmetRequirements( base.copy(traits = base.traits + CommonTrait.STICKY_FEET), ).isNotEmpty()` to be true",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(
                    traits = base.traits
                        .filterNot { it == CommonTrait.WALKING_LIMBS } +
                        listOf(CommonTrait.CLIMBING_LIMBS, CommonTrait.STICKY_FEET),
                ),
            ).isEmpty(),
            message = "Assertion failed",
        )
    }

    @Test
    fun `expanded archetype traits require their underlying physiology`() {
        val terrestrial = predator("archetype-dependencies")
        fun unmet(vararg traits: SpeciesTrait): List<UnmetTraitRequirement> =
            TraitDependencies.unmetRequirements(
                terrestrial.copy(traits = terrestrial.traits + traits),
            )

        assertTrue(unmet(CommonTrait.BEHAVIORAL_THERMOREGULATION).isNotEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.BEHAVIORAL_THERMOREGULATION).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.SCHOOLING).isNotEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.SCHOOLING).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.HOST_PENETRATING_FILAMENTS).isNotEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.HOST_PENETRATING_FILAMENTS).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.REEF_BUILDING).isNotEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.REEF_BUILDING).isNotEmpty()` to be true")

        val ectotherm = terrestrial.copy(
            traits = terrestrial.traits
                .filterNot { it == CommonTrait.ENDOTHERMY } +
                CommonTrait.ECTOTHERMY +
                CommonTrait.BEHAVIORAL_THERMOREGULATION,
        )
        assertTrue(TraitDependencies.unmetRequirements(ectotherm).isEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `TraitDependencies.unmetRequirements(ectotherm).isEmpty()` to be true")
        assertTrue(
            unmet(CommonTrait.ABSORPTIVE_FILAMENTS, CommonTrait.HOST_PENETRATING_FILAMENTS).isEmpty(),
            message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.ABSORPTIVE_FILAMENTS, CommonTrait.HOST_PENETRATING_FILAMENTS).isEmpty()` to be true"
        )
        assertTrue(
            unmet(CommonTrait.RIGID_COLONY_FRAMEWORK, CommonTrait.REEF_BUILDING).isEmpty(),
            message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.RIGID_COLONY_FRAMEWORK, CommonTrait.REEF_BUILDING).isEmpty()` to be true"
        )

        val aquatic = terrestrial.copy(
            traits = terrestrial.traits +
                CommonTrait.AQUATIC_LIMBS +
                CommonTrait.COLLECTIVE_LIVING +
                CommonTrait.SCHOOLING,
        )
        assertTrue(TraitDependencies.unmetRequirements(aquatic).isEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `TraitDependencies.unmetRequirements(aquatic).isEmpty()` to be true")
    }

    @Test
    fun `hearing cognition and tool dependencies are explicit`() {
        val base = predator("dependency-base")
        fun unmet(vararg traits: SpeciesTrait) =
            TraitDependencies.unmetRequirements(base.copy(traits = base.traits + traits))

        assertTrue(unmet(CommonTrait.ECHOLOCATION).isNotEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.ECHOLOCATION).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.KEEN_HEARING, CommonTrait.ECHOLOCATION).isEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.KEEN_HEARING, CommonTrait.ECHOLOCATION).isEmpty()` to be true")
        assertTrue(unmet(CommonTrait.INTELLIGENT).isEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.INTELLIGENT).isEmpty()` to be true")
        assertTrue(unmet(CommonTrait.SAPIENT).isNotEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.SAPIENT).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.SLOW_GROWTH, CommonTrait.SAPIENT).isEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.SLOW_GROWTH, CommonTrait.SAPIENT).isEmpty()` to be true")
        assertTrue(unmet(CommonTrait.TOOL_MANIPULATION).isNotEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.TOOL_MANIPULATION).isNotEmpty()` to be true")
        assertTrue(
            unmet(
                CommonTrait.INTELLIGENT,
                CommonTrait.TOOL_MANIPULATION,
            ).isEmpty(),
            message = "Hearing cognition and tool dependencies are explicit: expected `unmet( CommonTrait.INTELLIGENT, CommonTrait.TOOL_MANIPULATION, ).isEmpty()` to be true",
        )
    }

    @Test
    fun `group huddling requires a non-solitary social organization`() {
        val base = predator("huddling-dependencies")
        fun unmet(organization: CommonTrait) =
            TraitDependencies.unmetRequirements(
                base.copy(
                    traits = base.traits.filter { it.group != TraitGroup.SOCIAL_ORGANIZATION } +
                        organization + CommonTrait.GROUP_HUDDLING,
                ),
            )

        assertTrue(unmet(CommonTrait.SOLITARY).isNotEmpty(), message = "Group huddling requires a non-solitary social organization: expected `unmet(CommonTrait.SOLITARY).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.GROUP_LIVING).isEmpty(), message = "Group huddling requires a non-solitary social organization: expected `unmet(CommonTrait.GROUP_LIVING).isEmpty()` to be true")
        assertTrue(unmet(CommonTrait.COLLECTIVE_LIVING).isEmpty(), message = "Group huddling requires a non-solitary social organization: expected `unmet(CommonTrait.COLLECTIVE_LIVING).isEmpty()` to be true")
        assertTrue(unmet(CommonTrait.EUSOCIAL_COLONY).isEmpty(), message = "Group huddling requires a non-solitary social organization: expected `unmet(CommonTrait.EUSOCIAL_COLONY).isEmpty()` to be true")
    }

    @Test
    fun `activity overlap modifies only terrestrial predation matchups`() {
        fun activePredator(id: String, pattern: CommonTrait) =
            predator(id).copy(traits = predator(id).traits + pattern)
        val matched = activePredator("matched", CommonTrait.DIURNAL)
        val neutral = activePredator("neutral", CommonTrait.CATHEMERAL)
        val mismatched = activePredator("mismatched", CommonTrait.NOCTURNAL)
        val prey = predator("day-prey", SizeClass.SMALL).copy(
            traits = predator("day-prey", SizeClass.SMALL).traits
                .filterNot { it == CommonTrait.AMBUSH_MUSCULATURE } +
                CommonTrait.GRAZING_MOUTHPARTS +
                CommonTrait.DIURNAL,
        )
        val ecology = EcologyCompiler.compile(listOf(matched, neutral, mismatched, prey))

        fun predationRate(predator: SpeciesDefinition) =
            ecology.interactions.get(
                ecology.speciesIndex(predator.id),
                ecology.speciesIndex(prey.id),
            ).targetLossRate

        assertTrue(predationRate(matched) > predationRate(neutral), message = "Activity overlap modifies only terrestrial predation matchups: expected `predationRate(matched) > predationRate(neutral)` to be true")
        assertTrue(predationRate(neutral) > predationRate(mismatched), message = "Activity overlap modifies only terrestrial predation matchups: expected `predationRate(neutral) > predationRate(mismatched)` to be true")
        assertEquals(
            ActivityPattern.DIURNAL,
            ecology.species[ecology.speciesIndex(matched.id)].interactions.activityPattern,
            message = "Activity overlap modifies only terrestrial predation matchups: expected `ecology.species[ecology.speciesIndex(matched.id)].interactions.activityPattern` to match `ActivityPattern.DIURNAL`",
        )
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
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.PHOTOSYNTHETIC_SURFACE,
                CommonTrait.SURFACE_HOLDFAST,
                CommonTrait.INTERWOVEN_MAT,
            ),
        )

        val compiled = EcologyCompiler.compile(listOf(surfaceAttached)).species.single()

        assertTrue(CommonTrait.ROOTED_BODY !in surfaceAttached.traits, message = "Surface-attached sessile life does not require roots: expected `CommonTrait.ROOTED_BODY !in surfaceAttached.traits` to be true")
        assertTrue(compiled.niche.supportFor(Habitat.LAND_SURFACE) > 0.0, message = "Surface-attached sessile life does not require roots: expected `compiled.niche.supportFor(Habitat.LAND_SURFACE) > 0.0` to be true")
        assertTrue(TraitDependencies.unmetRequirements(surfaceAttached).isEmpty(), message = "Surface-attached sessile life does not require roots: expected `TraitDependencies.unmetRequirements(surfaceAttached).isEmpty()` to be true")
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
            assertEquals(TraitGroup.PHOTOSYNTHETIC_STRUCTURE, structure.group, message = "Leaf structures independently provide photosynthetic tissue: expected `structure.group` to match `TraitGroup.PHOTOSYNTHETIC_STRUCTURE`")

            val definition = producer(
                "${structure.name.lowercase()}-producer",
                traits = listOf(
                    CommonTrait.LUNGS,
                    CommonTrait.TEMPERATE_BIOCHEMISTRY,
                    structure,
                    CommonTrait.ROOTED_BODY,
                ),
            )
            val compiled = EcologyCompiler.compile(listOf(definition)).species.single()

            assertTrue(TraitDependencies.unmetRequirements(definition).isEmpty(), message = "Leaf structures independently provide photosynthetic tissue: expected `TraitDependencies.unmetRequirements(definition).isEmpty()` to be true")
            assertTrue(compiled.niche.supportFor(EcoStrategy.PHOTOSYNTHESIS) > 0.0, message = "Leaf structures independently provide photosynthetic tissue: expected `compiled.niche.supportFor(EcoStrategy.PHOTOSYNTHESIS) > 0.0` to be true")
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
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.LIMBED_BODY,
                CommonTrait.WINGS,
                CommonTrait.SUSPENSION_FEEDING_TENTACLES,
            ),
        )
        val ecology = EcologyCompiler.compile(listOf(species))
        val compiled = ecology.species.single()
        val strongest = ecology.niches[compiled.niche.bestNicheIndex()]

        assertEquals(Habitat.AERIAL, strongest.habitat, message = "Habitat and strategy jointly derive the strongest niche: expected `strongest.habitat` to match `Habitat.AERIAL`")
        assertEquals(EcoStrategy.FILTER_FEEDING, strongest.strategy, message = "Habitat and strategy jointly derive the strongest niche: expected `strongest.strategy` to match `EcoStrategy.FILTER_FEEDING`")
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
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.VIVIPARITY,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.LIMBED_BODY,
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

        assertEquals(Habitat.LAND_SURFACE, strongest.habitat, message = "Plant and animal feeding adaptations derive a generalist niche: expected `strongest.habitat` to match `Habitat.LAND_SURFACE`")
        assertEquals(EcoStrategy.GENERALIST_FORAGING, strongest.strategy, message = "Plant and animal feeding adaptations derive a generalist niche: expected `strongest.strategy` to match `EcoStrategy.GENERALIST_FORAGING`")
        assertEquals(
            InteractionKind.GRAZING,
            ecology.interactions.get(compiled.index, ecology.speciesIndex("plant")).kind,
            message = "Plant and animal feeding adaptations derive a generalist niche: expected `ecology.interactions.get(compiled.index, ecology.speciesIndex(\"plant\")).kind` to match `InteractionKind.GRAZING`",
        )
        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(compiled.index, ecology.speciesIndex("herbivore")).kind,
            message = "Plant and animal feeding adaptations derive a generalist niche: expected `ecology.interactions.get(compiled.index, ecology.speciesIndex(\"herbivore\")).kind` to match `InteractionKind.PREDATION`",
        )
        assertEquals(
            0.0,
            ecology.species[ecology.speciesIndex("herbivore")]
                .niche.supportFor(EcoStrategy.GENERALIST_FORAGING),
            message = "Plant and animal feeding adaptations derive a generalist niche: expected `ecology.species[ecology.speciesIndex(\"herbivore\")] .niche.supportFor(EcoStrategy.GENERALIST_FORAGING)` to match `0.0`",
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
            message = "Assertion failed",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                namedOvospore.copy(
                    traits = namedOvospore.traits +
                        CommonTrait.OVOSPORE_NEST
                ),
            ).isEmpty(),
            message = "Assertion failed",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                namedOvospore.copy(
                    traits = namedOvospore.traits + CommonTrait.BODY_CARRIED_OVOSPORES,
                ),
            ).isEmpty(),
            message = "Assertion failed",
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
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.LIMBED_BODY,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.BROOD_PARASITISM,
            ),
        )

        assertTrue(TraitDependencies.unmetRequirements(parasiteBase).isNotEmpty(), message = "Brood parasitism requires its authored host relationship: expected `TraitDependencies.unmetRequirements(parasiteBase).isNotEmpty()` to be true")

        val parasite = parasiteBase.copy(
            traits = parasiteBase.traits + broodParasitismOf(host.id, host.displayName),
        )
        val ecology = EcologyCompiler.compile(listOf(host, parasite))
        val parasiteIndex = ecology.speciesIndex(parasite.id)
        val hostIndex = ecology.speciesIndex(host.id)

        assertTrue(TraitDependencies.unmetRequirements(parasite).isEmpty(), message = "Brood parasitism requires its authored host relationship: expected `TraitDependencies.unmetRequirements(parasite).isEmpty()` to be true")
        assertTrue(ecology.interactions.get(parasiteIndex, hostIndex).targetRequired, message = "Brood parasitism requires its authored host relationship: expected `ecology.interactions.get(parasiteIndex, hostIndex).targetRequired` to be true")
        assertEquals(
            listOf(parasite, host).map { it.id }.toSet(),
            EcologyAssembly.completeRequiredTargets(
                ecology = ecology,
                selected = listOf(ecology.species[parasiteIndex]),
                availableTargets = ecology.species,
            ).map { it.id }.toSet(),
            message = "Assertion failed",
        )
        assertTrue(
            EcologyAssembly.completeRequiredTargets(
                ecology = ecology,
                selected = listOf(ecology.species[parasiteIndex]),
                availableTargets = emptyList(),
            ).isEmpty(),
            message = "Assertion failed",
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

        assertEquals(InteractionKind.SUPPLEMENTAL_FEEDING, cucumberEdge.kind, message = "Specific food creates only the requested directed edge: expected `cucumberEdge.kind` to match `InteractionKind.SUPPLEMENTAL_FEEDING`")
        assertEquals(InteractionKind.NONE, otherEdge.kind, message = "Specific food creates only the requested directed edge: expected `otherEdge.kind` to match `InteractionKind.NONE`")
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

        assertEquals(InteractionKind.SUPPLEMENTAL_FEEDING, interaction.kind, message = "Authored relationship effects compose on one interaction edge: expected `interaction.kind` to match `InteractionKind.SUPPLEMENTAL_FEEDING`")
        assertEquals(0.07, interaction.targetBenefitRate, message = "Authored relationship effects compose on one interaction edge: expected `interaction.targetBenefitRate` to match `0.07`")
        assertTrue(interaction.targetRequired, message = "Authored relationship effects compose on one interaction edge: expected `interaction.targetRequired` to be true")
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
            message = "Assertion failed",
        )
        assertEquals(
            InteractionKind.NONE,
            ecology.interactions.get(ecology.speciesIndex(mediumFilter.id), tiny).kind,
            message = "Filter feeders target minuscule motile life and huge or colossal feeders also target tiny life: expected `ecology.interactions.get(ecology.speciesIndex(mediumFilter.id), tiny).kind` to match `InteractionKind.NONE`",
        )
        assertEquals(
            InteractionKind.FILTER_FEEDING,
            ecology.interactions.get(ecology.speciesIndex(hugeFilter.id), minuscule).kind,
            message = "Assertion failed",
        )
        assertEquals(
            InteractionKind.FILTER_FEEDING,
            ecology.interactions.get(ecology.speciesIndex(hugeFilter.id), tiny).kind,
            message = "Filter feeders target minuscule motile life and huge or colossal feeders also target tiny life: expected `ecology.interactions.get(ecology.speciesIndex(hugeFilter.id), tiny).kind` to match `InteractionKind.FILTER_FEEDING`",
        )
        assertEquals(
            InteractionKind.FILTER_FEEDING,
            ecology.interactions.get(ecology.speciesIndex(colossalFilter.id), tiny).kind,
            message = "Filter feeders target minuscule motile life and huge or colossal feeders also target tiny life: expected `ecology.interactions.get(ecology.speciesIndex(colossalFilter.id), tiny).kind` to match `InteractionKind.FILTER_FEEDING`",
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

            assertEquals(0.0, species.niche.supportFor(Habitat.FRESHWATER), message = "Huge and colossal aquatic species cannot occupy river habitat: expected `species.niche.supportFor(Habitat.FRESHWATER)` to match `0.0`")
            assertEquals(-1, NicheSelection.choose(species, ecology, river), message = "Huge and colossal aquatic species cannot occupy river habitat: expected `NicheSelection.choose(species, ecology, river)` to match `-1`")
        }
    }

    @Test
    fun `medium predators do not use tiny aggregate insects as prey`() {
        val mediumPredator = predator("medium-predator", SizeClass.MEDIUM)
        val smallPrey = predator("small-prey", SizeClass.SMALL).copy(
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.VIVIPARITY,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.LIMBED_BODY,
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
            message = "Medium predators do not use tiny aggregate insects as prey: expected `ecology.interactions.get(consumer, ecology.speciesIndex(InvariantSpecies.BUGS.id)).kind` to match `InteractionKind.NONE`",
        )
        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(consumer, ecology.speciesIndex(smallPrey.id)).kind,
            message = "Medium predators do not use tiny aggregate insects as prey: expected `ecology.interactions.get(consumer, ecology.speciesIndex(smallPrey.id)).kind` to match `InteractionKind.PREDATION`",
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
            message = "Medium aquatic predators can use tiny aquatic life without opening that prey to large hunters: expected `ecology.interactions.get(ecology.speciesIndex(mediumPredator.id), prey).kind` to match `InteractionKind.PREDATION`",
        )
        assertEquals(
            InteractionKind.NONE,
            ecology.interactions.get(ecology.speciesIndex(largePredator.id), prey).kind,
            message = "Medium aquatic predators can use tiny aquatic life without opening that prey to large hunters: expected `ecology.interactions.get(ecology.speciesIndex(largePredator.id), prey).kind` to match `InteractionKind.NONE`",
        )
    }

    @Test
    fun `cooperative hunters can attack prey one size class larger`() {
        val ordinaryMedium = predator("ordinary-medium", SizeClass.MEDIUM)
        val cooperativeMedium = predator("cooperative-medium", SizeClass.MEDIUM).copy(
            traits = predator("cooperative-medium", SizeClass.MEDIUM).traits
                .filter { it.group != TraitGroup.SOCIAL_ORGANIZATION } +
                CommonTrait.GROUP_LIVING +
                CommonTrait.COOPERATIVE_HUNTING,
        )
        val cooperativeLarge = predator("cooperative-large", SizeClass.LARGE).copy(
            traits = predator("cooperative-large", SizeClass.LARGE).traits
                .filter { it.group != TraitGroup.SOCIAL_ORGANIZATION } +
                CommonTrait.GROUP_LIVING +
                CommonTrait.COOPERATIVE_HUNTING,
        )
        val largePrey = terrestrialPrey("large-prey", SizeClass.LARGE)
        val hugePrey = terrestrialPrey("huge-prey", SizeClass.HUGE)
        val colossalPrey = terrestrialPrey("colossal-prey", SizeClass.COLOSSAL)
        val ecology = EcologyCompiler.compile(
            listOf(
                ordinaryMedium,
                cooperativeMedium,
                cooperativeLarge,
                largePrey,
                hugePrey,
                colossalPrey,
            ),
        )

        fun interaction(consumerId: String, targetId: String): InteractionKind =
            ecology.interactions.get(
                ecology.speciesIndex(consumerId),
                ecology.speciesIndex(targetId),
            ).kind

        val compiledCooperativeMedium =
            ecology.species[ecology.speciesIndex(cooperativeMedium.id)]
        assertEquals(1, compiledCooperativeMedium.interactions.largerPreySizeClasses, message = "Cooperative hunters can attack prey one size class larger: expected `compiledCooperativeMedium.interactions.largerPreySizeClasses` to match `1`")
        assertTrue(
            compiledCooperativeMedium.niche.supportFor(Habitat.LAND_SURFACE) > 0.0,
            message = "Cooperative hunters can attack prey one size class larger: expected `compiledCooperativeMedium.niche.supportFor(Habitat.LAND_SURFACE) > 0.0` to be true"
        )
        assertTrue(
            compiledCooperativeMedium.niche.supportFor(EcoStrategy.AMBUSH_PREDATION) > 0.0,
            message = "Cooperative hunters can attack prey one size class larger: expected `compiledCooperativeMedium.niche.supportFor(EcoStrategy.AMBUSH_PREDATION) > 0.0` to be true",
        )
        assertEquals(InteractionKind.NONE, interaction(ordinaryMedium.id, largePrey.id), message = "Cooperative hunters can attack prey one size class larger: expected `interaction(ordinaryMedium.id, largePrey.id)` to match `InteractionKind.NONE`")
        assertEquals(
            InteractionKind.PREDATION,
            interaction(cooperativeMedium.id, largePrey.id),
            message = "Cooperative hunters can attack prey one size class larger: expected `interaction(cooperativeMedium.id, largePrey.id)` to match `InteractionKind.PREDATION`"
        )
        assertEquals(
            InteractionKind.PREDATION,
            interaction(cooperativeLarge.id, hugePrey.id),
            message = "Cooperative hunters can attack prey one size class larger: expected `interaction(cooperativeLarge.id, hugePrey.id)` to match `InteractionKind.PREDATION`"
        )
        assertEquals(
            InteractionKind.NONE,
            interaction(cooperativeMedium.id, colossalPrey.id),
            message = "Cooperative hunters can attack prey one size class larger: expected `interaction(cooperativeMedium.id, colossalPrey.id)` to match `InteractionKind.NONE`"
        )
    }

    @Test
    fun `terrestrial grazers consume the modeled carpet plant population`() {
        val grazer = predator("grazer", SizeClass.SMALL).copy(
            traits = listOf(
                CommonTrait.LUNGS,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.VIVIPARITY,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.LIMBED_BODY,
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
            message = "Assertion failed",
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
            val capture = consumer.interactions.captureAbility +
                if (pursuit) {
                    consumer.interactions.pursuitSpeed + consumer.interactions.sensing
                } else {
                    0.0
                }
            val defense = target.interactions.defense + if (pursuit) target.interactions.pursuitSpeed else 0.0
            return (0.07 * support * capture / maxOf(0.25, defense)).coerceIn(0.0, 0.25)
        }

        listOf(orca to seal, seal to silverfish).forEach { (consumer, target) ->
            val interaction = ecology.interactions.get(consumer.index, target.index)
            val attack = undiscountedAttack(consumer, target)
            assertEquals(InteractionKind.PREDATION, interaction.kind, message = "Separated marine predator tiers are not treated as intraguild competitors: expected `interaction.kind` to match `InteractionKind.PREDATION`")
            assertEquals(attack, interaction.targetLossRate, 1.0e-12, message = "Separated marine predator tiers are not treated as intraguild competitors: expected `interaction.targetLossRate` to match `attack`")
            assertEquals(attack * 1.30, interaction.consumerGainRate, 1.0e-12, message = "Separated marine predator tiers are not treated as intraguild competitors: expected `interaction.consumerGainRate` to match `attack * 1.30`")
        }
    }

    @Test
    fun `high pouncing increases predation only against fossorial prey`() {
        val pouncer = predator("pouncer", SizeClass.SMALL).copy(
            traits = predator("pouncer", SizeClass.SMALL).traits + CommonTrait.HIGH_POUNCING,
        )
        val surfacePrey = predator("surface-prey", SizeClass.SMALL).copy(
            traits = predator("surface-prey", SizeClass.SMALL).traits
                .filterNot { it == CommonTrait.AMBUSH_MUSCULATURE } +
                CommonTrait.GRAZING_MOUTHPARTS,
        )
        val burrowingPrey = surfacePrey.copy(
            id = "burrowing-prey",
            displayName = "burrowing-prey",
            traits = surfacePrey.traits +
                CommonTrait.DIGGING_LIMBS +
                CommonTrait.FOSSORIAL_LIVING,
        )
        val ecology = EcologyCompiler.compile(listOf(pouncer, surfacePrey, burrowingPrey))
        val pouncerIndex = ecology.speciesIndex(pouncer.id)
        val surfaceAttack = ecology.interactions
            .get(pouncerIndex, ecology.speciesIndex(surfacePrey.id))
            .targetLossRate
        val burrowAttack = ecology.interactions
            .get(pouncerIndex, ecology.speciesIndex(burrowingPrey.id))
            .targetLossRate

        assertTrue(burrowAttack > surfaceAttack, message = "High pouncing increases predation only against fossorial prey: expected `burrowAttack > surfaceAttack` to be true")
    }

    @Test
    fun `burrow borrowers require another local burrow builder`() {
        val builder = predator("burrow-builder", SizeClass.SMALL).copy(
            traits = predator("burrow-builder", SizeClass.SMALL).traits +
                CommonTrait.DIGGING_LIMBS +
                CommonTrait.BURROW_BUILDER,
        )
        val borrower = predator("burrow-borrower", SizeClass.SMALL).copy(
            traits = predator("burrow-borrower", SizeClass.SMALL).traits
                .filterNot { it == CommonTrait.AMBUSH_MUSCULATURE } +
                CommonTrait.GRAZING_MOUTHPARTS +
                CommonTrait.BURROW_BORROWER,
        )
        val ecology = EcologyCompiler.compile(listOf(builder, borrower))

        assertTrue(
            ecology.interactions.get(
                ecology.speciesIndex(borrower.id),
                ecology.speciesIndex(builder.id),
            ).targetRequired,
            message = "Burrow borrowers require another local burrow builder: expected `ecology.interactions.get( ecology.speciesIndex(borrower.id), ecology.speciesIndex(builder.id), ).targetRequired` to be true",
        )
    }

    @Test
    fun `sound lures increase capture only against prey sharing a call`() {
        val luringPredator = predator("sound-lurer", SizeClass.SMALL).copy(
            traits = predator("sound-lurer", SizeClass.SMALL).traits +
                listOf(CommonTrait.CHIRPING_CALL, CommonTrait.SOUND_LURES),
        )
        val preyBase = predator("prey-base", SizeClass.SMALL).copy(
            traits = predator("prey-base", SizeClass.SMALL).traits
                .filterNot { it == CommonTrait.AMBUSH_MUSCULATURE } +
                CommonTrait.GRAZING_MOUTHPARTS,
        )
        val chirpingPrey = preyBase.copy(
            id = "chirping-prey",
            displayName = "chirping prey",
            traits = preyBase.traits + CommonTrait.CHIRPING_CALL,
        )
        val barkingPrey = preyBase.copy(
            id = "barking-prey",
            displayName = "barking prey",
            traits = preyBase.traits + CommonTrait.BARKING_CALL,
        )
        val ecology = EcologyCompiler.compile(listOf(luringPredator, chirpingPrey, barkingPrey))
        val predatorIndex = ecology.speciesIndex(luringPredator.id)
        val sharedCallAttack = ecology.interactions
            .get(predatorIndex, ecology.speciesIndex(chirpingPrey.id))
            .targetLossRate
        val differentCallAttack = ecology.interactions
            .get(predatorIndex, ecology.speciesIndex(barkingPrey.id))
            .targetLossRate

        assertTrue(sharedCallAttack > differentCallAttack, message = "Sound lures increase capture only against prey sharing a call: expected `sharedCallAttack > differentCallAttack` to be true")
    }

    @Test
    fun `cosmetic traits compile without changing phenotype parameters`() {
        val ordinary = predator("ordinary")
        val vocal = predator("vocal").copy(
            traits = predator("vocal").traits + CommonTrait.ROARING_CALL,
        )
        val ordinaryCompiled = EcologyCompiler.compile(listOf(ordinary)).species.single()
        val vocalCompiled = EcologyCompiler.compile(listOf(vocal)).species.single()

        assertEquals(ordinaryCompiled.physiology, vocalCompiled.physiology, message = "Cosmetic traits compile without changing phenotype parameters: expected `vocalCompiled.physiology` to match `ordinaryCompiled.physiology`")
        assertEquals(ordinaryCompiled.environment, vocalCompiled.environment, message = "Cosmetic traits compile without changing phenotype parameters: expected `vocalCompiled.environment` to match `ordinaryCompiled.environment`")
        assertEquals(ordinaryCompiled.lifeHistory, vocalCompiled.lifeHistory, message = "Cosmetic traits compile without changing phenotype parameters: expected `vocalCompiled.lifeHistory` to match `ordinaryCompiled.lifeHistory`")
        assertEquals(
            ordinaryCompiled.interactions,
            vocalCompiled.interactions.copy(
                acousticSignalMask = ordinaryCompiled.interactions.acousticSignalMask,
            ),
            message = "Cosmetic traits compile without changing phenotype parameters: expected `vocalCompiled.interactions.copy( acousticSignalMask = ordinaryCompiled.interactions.acousticSignalMask, )` to match `ordinaryCompiled.interactions`",
        )
    }

    @Test
    fun `attached and suspended photosynthesizers compile to separate competition layers`() {
        val waterLily = EarthSpeciesCatalog.ALL.single { it.id == "white-water-lily" }
        val ecology = EcologyCompiler.compile(listOf(waterLily, InvariantSpecies.PLANKTON))

        assertEquals(
            ProducerCompetitionLayer.ATTACHED,
            ecology.species.single { it.id == waterLily.id }.niche.producerCompetitionLayer,
            message = "Attached and suspended photosynthesizers compile to separate competition layers: expected `ecology.species.single { it.id == waterLily.id }.niche.producerCompetitionLayer` to match `ProducerCompetitionLayer.ATTACHED`",
        )
        assertEquals(
            ProducerCompetitionLayer.SUSPENDED,
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }
                .niche.producerCompetitionLayer,
            message = "Assertion failed",
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

        assertEquals(5, ecology.species.size, message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species.size` to match `5`")
        assertTrue(ecology.species.all { it.kind == SpeciesKind.INVARIANT }, message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species.all { it.kind == SpeciesKind.INVARIANT }` to be true")
        assertTrue(
            ecology.species.all {
                it.lifeHistory.dormancyKind == DormancyKind.PROPAGULE
            },
            message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species.all { it.lifeHistory.dormancyKind == DormancyKind.PROPAGULE }` to be true"
        )
        assertTrue(
            ecology.species.all {
                it.lifeHistory.nicheCompetitionSensitivity < 0.20
            },
            message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species.all { it.lifeHistory.nicheCompetitionSensitivity < 0.20 }` to be true"
        )
        assertEquals(
            0.10,
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }
                .lifeHistory.dormantEntryBiomassRetention,
            message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species.single { it.id == InvariantSpecies.PLANKTON.id } .lifeHistory.dormantEntryBiomassRetention` to match `0.10`",
        )
        assertEquals(
            10.0,
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }
                .lifeHistory.dormantReactivationMultiplier,
            message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species.single { it.id == InvariantSpecies.PLANKTON.id } .lifeHistory.dormantReactivationMultiplier` to match `10.0`",
        )
        assertTrue(
            ecology.species
                .filterNot { it.id == InvariantSpecies.PLANKTON.id }
                .all { it.lifeHistory.dormantEntryBiomassRetention == 1.0 },
            message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species .filterNot { it.id == InvariantSpecies.PLANKTON.id } .all { it.lifeHistory.dormantEntryBiomassRetention == 1.0 }` to be true",
        )
    }

    @Test
    fun `all authored traits declare an effect or relationship`() {
        val authoredTraits: List<SpeciesTrait> = CommonTrait.entries + ColorTrait.entries
        authoredTraits.forEach { trait ->
            assertTrue(
                trait.description.isNotBlank(),
                "${trait.displayName} has no player-facing description",
            )
        }
        authoredTraits.forEach { trait ->
            if (trait.isCosmetic) {
                assertTrue(trait.effects.isEmpty(), "${trait.displayName} has cosmetic effects")
                return@forEach
            }
            assertTrue(
                trait.effects.isNotEmpty() || trait.relationships.isNotEmpty(),
                "${trait.displayName} has no effect or relationship",
            )
        }
    }

    private fun producer(
        id: String = "producer",
        traits: List<SpeciesTrait> = listOf(
            CommonTrait.PASSIVE_RESPIRATION,
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
            CommonTrait.LUNGS,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.TERRESTRIAL_OVOSPORE,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.LIMBED_BODY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SOLITARY,
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
            CommonTrait.LUNGS,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.LIMBED_BODY,
            CommonTrait.AQUATIC_LIMBS,
            CommonTrait.SOLITARY,
            CommonTrait.JAW,
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
            CommonTrait.LUNGS,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.SOLITARY,
        ),
    )

    private fun terrestrialPrey(
        id: String,
        sizeClass: SizeClass,
    ) = SpeciesDefinition(
        id = id,
        displayName = id,
        sizeClass = sizeClass,
        motile = true,
        traits = listOf(
            CommonTrait.LUNGS,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.VIVIPARITY,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.LIMBED_BODY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SOLITARY,
            CommonTrait.GRAZING_MOUTHPARTS,
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
            CommonTrait.LUNGS,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.LIMBED_BODY,
            CommonTrait.AQUATIC_LIMBS,
            CommonTrait.SOLITARY,
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
