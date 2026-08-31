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
    fun `strategy access gates affinity regardless of trait order`() {
        val base = predator("strategy-foundation").let { species ->
            species.copy(traits = species.traits - CommonTrait.MEAT_EATING_MOUTHPARTS - CommonTrait.AMBUSH_MUSCULATURE)
        }
        EcoStrategy.entries.forEach { strategy ->
            fun effect(name: String, effect: TraitEffect) = EffectTrait(name, name, listOf(effect))
            val access = effect("strategy access", TraitEffect.StrategyAccess(strategy, 0.25))
            val affinity = effect("strategy affinity", TraitEffect.StrategyAffinity(strategy, 0.50))
            val penalty = effect("strategy penalty", TraitEffect.StrategyAffinity(strategy, -2.0))
            val bonus = effect("strategy bonus", TraitEffect.StrategyAffinity(strategy, 2.0))
            fun compiled(vararg traits: SpeciesTrait) = EcologyCompiler.compile(
                listOf(base.copy(traits = base.traits + traits)),
            ).species.single()

            val inaccessible = compiled(affinity)
            assertTrue(!inaccessible.niche.accesses(strategy), "$strategy affinity must not grant access")
            assertEquals(0.0, inaccessible.niche.supportFor(strategy))
            assertTrue(!inaccessible.niche.hasViableNiche())
            assertEquals(compiled().physiology.maintenanceDemand, inaccessible.physiology.maintenanceDemand)
            assertEquals(0.75, compiled(access, affinity).niche.supportFor(strategy))
            assertEquals(0.75, compiled(affinity, access).niche.supportFor(strategy))
            assertTrue(compiled(access, affinity).niche.supports(strategy))
            assertEquals(1.0, compiled(access, bonus).niche.supportFor(strategy))
            val suppressed = compiled(access, penalty)
            assertTrue(suppressed.niche.accesses(strategy), "A penalty does not remove anatomy")
            assertTrue(!suppressed.niche.supports(strategy))
            assertEquals(0.0, suppressed.niche.supportFor(strategy))
        }
    }

    @Test
    fun `hunting and scavenging affinities cannot give herbivores a meat diet`() {
        val hunter = predator("hunter")
        val herbivore = hunter.copy(
            id = "agile-camouflaged-herbivore",
            traits = hunter.traits - CommonTrait.MEAT_EATING_MOUTHPARTS + listOf(
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.SWIFT_LIMBS,
                CommonTrait.MOTION_TRACKING_SENSES,
                CommonTrait.TERRESTRIAL_CAMOUFLAGE,
                CommonTrait.SCAVENGING_SENSES,
                CommonTrait.RESILIENT_DIGESTION,
            ),
        )
        val ecology = EcologyCompiler.compile(listOf(hunter, herbivore))
        val prey = ecology.species[ecology.speciesIndex(herbivore.id)]
        listOf(
            EcoStrategy.AMBUSH_PREDATION,
            EcoStrategy.PURSUIT_PREDATION,
            EcoStrategy.SCAVENGING,
            EcoStrategy.GENERALIST_FORAGING,
        ).forEach { strategy ->
            assertTrue(!prey.niche.accesses(strategy), "Herbivore must not gain $strategy access")
            assertEquals(0.0, prey.niche.supportFor(strategy))
        }
        assertEquals(InteractionKind.NONE, ecology.interactions.get(prey.index, ecology.speciesIndex(hunter.id)).kind)
        assertEquals(InteractionKind.PREDATION, ecology.interactions.get(ecology.speciesIndex(hunter.id), prey.index).kind)
    }

    @Test
    fun `meat eating mouthparts enable predation and scavenging without hunting specializations`() {
        val species = predator("meat-eater").let { it.copy(traits = it.traits - CommonTrait.AMBUSH_MUSCULATURE) }
        val profile = EcologyCompiler.compile(listOf(species)).species.single().niche
        listOf(EcoStrategy.AMBUSH_PREDATION, EcoStrategy.PURSUIT_PREDATION, EcoStrategy.SCAVENGING).forEach { strategy ->
            assertTrue(profile.supports(strategy), "Meat-eating mouthparts enable $strategy")
        }
        assertTrue(!profile.accesses(EcoStrategy.GRAZING))
        assertTrue(!profile.accesses(EcoStrategy.GENERALIST_FORAGING))
    }

    @Test
    fun `derived generalist access respects independently authored affinity`() {
        val omnivore = predator("omnivore").let {
            it.copy(traits = it.traits + CommonTrait.GRAZING_MOUTHPARTS)
        }
        val penalty = EffectTrait(
            "generalist penalty",
            "Reduces performance while combining plant and animal feeding.",
            listOf(TraitEffect.StrategyAffinity(EcoStrategy.GENERALIST_FORAGING, -0.25)),
        )
        val penalized = omnivore.copy(id = "penalized", traits = omnivore.traits + penalty)
        val compiled = EcologyCompiler.compile(listOf(omnivore, penalized)).species
        assertTrue(compiled[1].niche.accesses(EcoStrategy.GENERALIST_FORAGING))
        assertEquals(
            compiled[0].niche.supportFor(EcoStrategy.GENERALIST_FORAGING) - 0.25,
            compiled[1].niche.supportFor(EcoStrategy.GENERALIST_FORAGING),
            1.0e-12,
        )
    }

    @Test
    fun `habitat access gates independently authored affinity`() {
        val canopyAffinity = EffectTrait(
            displayName = "canopy affinity",
            description = "Improves performance in a canopy without providing a way to enter it.",
            effects = listOf(TraitEffect.HabitatAffinity(Habitat.CANOPY, 0.80)),
        )
        val canopyAccess = EffectTrait(
            displayName = "canopy access",
            description = "Provides a physical means of entering and operating in a canopy.",
            effects = listOf(TraitEffect.HabitatAccess(Habitat.CANOPY)),
        )
        val affinityOnly = terrestrialPrey("affinity-only", SizeClass.SMALL).let { species ->
            species.copy(traits = species.traits + canopyAffinity)
        }
        val accessible = terrestrialPrey("accessible", SizeClass.SMALL).let { species ->
            species.copy(traits = species.traits + canopyAffinity + canopyAccess)
        }

        val compiled = EcologyCompiler.compile(listOf(affinityOnly, accessible)).species

        assertEquals(
            0.0,
            compiled.single { it.id == affinityOnly.id }.niche.supportFor(Habitat.CANOPY),
            message = "Canopy affinity alone must not grant physical access to the canopy",
        )
        assertEquals(
            0.80,
            compiled.single { it.id == accessible.id }.niche.supportFor(Habitat.CANOPY),
            absoluteTolerance = 1e-12,
            message = "An accessible habitat should retain its independently compiled affinity",
        )
    }

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
        val strategyAccess = BooleanArray(EcoStrategy.entries.size).also {
            it[EcoStrategy.GRAZING.ordinal] = true
        }
        val camouflage = DoubleArray(Habitat.entries.size).also {
            it[Habitat.LAND_SURFACE.ordinal] = 0.40
        }
        val nicheFit = doubleArrayOf(0.45)
        val habitatAccess = BooleanArray(Habitat.entries.size).also {
            it[Habitat.LAND_SURFACE.ordinal] = true
        }
        val profile = NicheProfile(
            producerCompetitionLayer = ProducerCompetitionLayer.NONE,
            photosyntheticColor = null,
            camouflageColor = BiologicalColor.BROWN,
            habitatAccess = habitatAccess,
            habitatSupport = habitatSupport,
            strategyAccess = strategyAccess,
            strategySupport = strategySupport,
            camouflage = camouflage,
            nicheFit = nicheFit,
        )

        habitatSupport[Habitat.LAND_SURFACE.ordinal] = 0.0
        habitatAccess[Habitat.LAND_SURFACE.ordinal] = false
        strategySupport[EcoStrategy.GRAZING.ordinal] = 0.0
        strategyAccess[EcoStrategy.GRAZING.ordinal] = false
        camouflage[Habitat.LAND_SURFACE.ordinal] = 0.0
        nicheFit[0] = 0.0

        assertEquals(0.75, profile.supportFor(Habitat.LAND_SURFACE), message = "Compiled niche profile owns its optimized arrays: expected `profile.supportFor(Habitat.LAND_SURFACE)` to match `0.75`")
        assertTrue(profile.accesses(Habitat.LAND_SURFACE), message = "Compiled niche profile should own its habitat-access array independently of the compiler's mutable working array")
        assertEquals(0.60, profile.supportFor(EcoStrategy.GRAZING), message = "Compiled niche profile owns its optimized arrays: expected `profile.supportFor(EcoStrategy.GRAZING)` to match `0.60`")
        assertTrue(profile.accesses(EcoStrategy.GRAZING), "Compiled niche profile owns its strategy-access array")
        assertEquals(0.40, profile.camouflageFor(Habitat.LAND_SURFACE), message = "Compiled niche profile owns its optimized arrays: expected `profile.camouflageFor(Habitat.LAND_SURFACE)` to match `0.40`")
        assertEquals(0.45, profile.fitFor(0), message = "Compiled niche profile owns its optimized arrays: expected `profile.fitFor(0)` to match `0.45`")
    }

    @Test
    fun `traits compile into climate and niche parameters`() {
        val producer = producer(
            traits = listOf(
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.PHOTOSYNTHETIC_SURFACE,
                CommonTrait.ROOTED_BODY,
                CommonTrait.FUR,
                CommonTrait.DENSE_UNDERCOAT.atLevel(2),
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

        assertEquals(
            small.physiology.massKg / SizeClass.SMALL.typicalMassKg,
            huge.physiology.massKg / SizeClass.HUGE.typicalMassKg,
        )
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
            traits = predator("slender-build", SizeClass.MEDIUM).traits + CommonTrait.SLENDER_PHYSIQUE,
        )
        val bulky = predator("bulky-build", SizeClass.MEDIUM).copy(
            traits = predator("bulky-build", SizeClass.MEDIUM).traits + CommonTrait.BULKY_PHYSIQUE,
        )
        val ecology = EcologyCompiler.compile(listOf(ordinary, slender, bulky))

        fun compiled(id: String) = ecology.species[ecology.speciesIndex(id)]

        val ordinaryMass = compiled(ordinary.id).physiology.massKg
        assertEquals(
            ordinaryMass * 0.5,
            compiled(slender.id).physiology.massKg,
        )
        assertEquals(
            ordinaryMass * 2.0,
            compiled(bulky.id).physiology.massKg,
        )
        assertEquals(SizeClass.MEDIUM, compiled(slender.id).sizeClass, message = "Body build adjusts mass without changing size class: expected `compiled(slender.id).sizeClass` to match `SizeClass.MEDIUM`")
        assertEquals(SizeClass.MEDIUM, compiled(bulky.id).sizeClass, message = "Body build adjusts mass without changing size class: expected `compiled(bulky.id).sizeClass` to match `SizeClass.MEDIUM`")
    }

    @Test
    fun `specialized senses compile distinct hunting and reproductive benefits`() {
        val ordinary = predator("ordinary-senses").copy(
            traits = predator("ordinary-senses").traits.filterNot { it == CommonTrait.AMBUSH_MUSCULATURE } + CommonTrait.MOTION_TRACKING_SENSES,
        )
        val scent = predator("scent-specialist").copy(
            traits = predator("scent-specialist").traits.filterNot { it == CommonTrait.AMBUSH_MUSCULATURE } + listOf(CommonTrait.MOTION_TRACKING_SENSES, CommonTrait.SCENT.atLevel(5)),
        )
        val sight = predator("sight-specialist").copy(
            traits = predator("sight-specialist").traits.filterNot { it == CommonTrait.AMBUSH_MUSCULATURE } + listOf(CommonTrait.MOTION_TRACKING_SENSES, CommonTrait.EYES.atLevel(5)),
        )
        val prey = predator("sensory-prey", SizeClass.SMALL)
        val ecology = EcologyCompiler.compile(listOf(ordinary, scent, sight, prey))

        fun compiled(id: String) = ecology.species[ecology.speciesIndex(id)]
        fun predationRate(predator: SpeciesDefinition) = ecology.interactions.get(ecology.speciesIndex(predator.id), ecology.speciesIndex(prey.id)).targetLossRate

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
    fun `different levels of the same scaled sense are mutually exclusive`() {
        listOf(
            CommonTrait.HEARING.atLevel(5) to CommonTrait.HEARING.atLevel(1),
            CommonTrait.SCENT.atLevel(5) to CommonTrait.SCENT.atLevel(1),
        ).forEachIndexed { index, (high, low) ->
            assertFailsWith<IllegalArgumentException> {
                val base = predator("conflicting-scaled-sense-$index")
                val conflicting = base.copy(traits = base.traits + listOf(high, low))
                EcologyCompiler.compile(listOf(conflicting))
            }
        }
    }

    @Test
    fun `scaled eyes increase benefits linearly and costs increasingly`() {
        fun withEyes(id: String, level: Int) = predator(id).copy(
            traits = predator(id).traits + CommonTrait.EYES.atLevel(level),
        )

        val definitions = (1..5).map { withEyes("eyes-$it", it) }
        val compiled = EcologyCompiler.compile(definitions).species

        compiled.forEachIndexed { index, species ->
            assertEquals(index + 1, species.traits.levelOf(CommonTrait.EYES))
        }
        assertEquals("rudimentary eyes", CommonTrait.EYES.displayNameAt(1))
        assertEquals("exceptional eyes", CommonTrait.EYES.displayNameAt(5))
        assertEquals(listOf(1), CommonTrait.EYES.adjacentLevelsFrom(0))
        assertEquals(listOf(2, 4), CommonTrait.EYES.adjacentLevelsFrom(3))
        assertEquals(listOf(4), CommonTrait.EYES.adjacentLevelsFrom(5))
        assertEquals(0.02, compiled[1].interactions.sensing - compiled[0].interactions.sensing, 0.000_001)
        assertEquals(0.02, compiled[4].interactions.sensing - compiled[3].interactions.sensing, 0.000_001)
        assertTrue(compiled[4].interactions.captureAbility > compiled[0].interactions.captureAbility)
        assertTrue(compiled[4].physiology.maintenanceDemand > compiled[0].physiology.maintenanceDemand)
        val lowCostStep = compiled[1].physiology.maintenanceDemand - compiled[0].physiology.maintenanceDemand
        val highCostStep = compiled[4].physiology.maintenanceDemand - compiled[3].physiology.maintenanceDemand
        assertTrue(highCostStep > lowCostStep)
    }

    @Test
    fun `scaled trait families expose the intended number of levels and rising costs`() {
        val fiveLevelSenses = listOf(CommonTrait.EYES, CommonTrait.HEARING, CommonTrait.SCENT)
        val threeLevelTraits = listOf(
            CommonTrait.INTELLIGENCE,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.VENOM_RESISTANCE,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.INSULATING_PLUMAGE,
        )

        assertTrue(fiveLevelSenses.all { it.maxLevel == 5 })
        assertTrue(threeLevelTraits.all { it.maxLevel == 3 })
        assertEquals(1, CommonTrait.BLUBBER.maxLevel)

        (fiveLevelSenses + threeLevelTraits).forEach { trait ->
            val costs = (1..trait.maxLevel).map { level ->
                trait.effectsAt(level).filterIsInstance<TraitEffect.MaintenanceCost>().sumOf(TraitEffect.MaintenanceCost::fraction)
            }
            assertEquals(costs.sorted(), costs)
            assertTrue(costs.zipWithNext().all { (lower, higher) -> higher > lower })
        }
    }

    @Test
    fun `trait requirements can require a scaled trait level`() {
        val visualSpecialization = EffectTrait(
            displayName = "visual specialization",
            description = "A specialization that requires developed vision.",
            effects = listOf(TraitEffect.Sensing(0.01)),
            requirements = listOf(TraitRequirement.traitLevelAtLeast(CommonTrait.EYES, 3)),
        )

        fun definition(id: String, eyeLevel: Int) = predator(id).copy(
            traits = predator(id).traits + listOf(CommonTrait.EYES.atLevel(eyeLevel), visualSpecialization),
        )

        assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(definition("rudimentary-vision", 1)))
        }
        EcologyCompiler.compile(listOf(definition("developed-vision", 3)))
    }

    @Test
    fun `contextual effects inspect the bearer's complete trait profile`() {
        val clawCoordination = EffectTrait(
            displayName = "claw coordination",
            description = "Sensory coordination is useful when grasping claws are present.",
            effects = listOf(
                TraitEffect.MaintenanceCost(0.02),
                ConditionalTraitEffect(
                    condition = TraitCondition.HasTrait(CommonTrait.CLAWS),
                    effects = listOf(TraitEffect.Sensing(0.20)),
                ),
            ),
        )
        val withoutClaws = predator("context-without-claws").copy(
            traits = predator("context-without-claws").traits + clawCoordination,
        )
        val withClaws = predator("context-with-claws").copy(
            traits = predator("context-with-claws").traits + listOf(CommonTrait.CLAWS, clawCoordination),
        )
        val compiled = EcologyCompiler.compile(listOf(withoutClaws, withClaws))

        assertEquals(0.0, compiled.species[0].interactions.sensing)
        assertEquals(0.20, compiled.species[1].interactions.sensing)
    }

    @Test
    fun `venom resistance conditionally counters a venomous predator's capture bonus`() {
        fun hunter(id: String, venomous: Boolean) = predator(id).copy(
            traits = predator(id).traits + listOfNotNull(CommonTrait.CLAWS, CommonTrait.VENOM_DELIVERY.atLevel(2).takeIf { venomous }),
        )

        val venomous = hunter("venomous-hunter", venomous = true)
        val ordinary = hunter("ordinary-hunter", venomous = false)
        val prey = predator("ordinary-venom-prey", SizeClass.SMALL)
        val resistantPrey = predator("resistant-venom-prey", SizeClass.SMALL).copy(
            traits = predator("resistant-venom-prey", SizeClass.SMALL).traits + CommonTrait.VENOM_RESISTANCE.atLevel(2),
        )
        val ecology = EcologyCompiler.compile(listOf(venomous, ordinary, prey, resistantPrey))

        fun loss(predator: SpeciesDefinition, target: SpeciesDefinition) = ecology.interactions.get(
            ecology.speciesIndex(predator.id),
            ecology.speciesIndex(target.id),
        ).targetLossRate

        assertTrue(loss(venomous, resistantPrey) < loss(venomous, prey))
        assertEquals(loss(ordinary, prey), loss(ordinary, resistantPrey), 0.000_001)
    }

    @Test
    fun `sensing protects prey against ambush predation`() {
        val ambusher = predator("ambusher", SizeClass.LARGE)
        val ordinaryPrey = terrestrialPrey("ordinary-prey", SizeClass.SMALL)
        val alertPrey = ordinaryPrey.copy(
            id = "alert-prey",
            displayName = "alert-prey",
            traits = ordinaryPrey.traits + CommonTrait.HEARING.atLevel(5),
        )
        val ecology = EcologyCompiler.compile(listOf(ambusher, ordinaryPrey, alertPrey))

        fun predationRate(prey: SpeciesDefinition) = ecology.interactions.get(ecology.speciesIndex(ambusher.id), ecology.speciesIndex(prey.id)).targetLossRate

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
            traits = predator("conflicting-metabolism").traits + listOf(CommonTrait.FAST_METABOLISM, CommonTrait.SLOW_METABOLISM),
        )

        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }

        assertTrue(failure.message.orEmpty().contains("METABOLIC_PACE"), message = "Fast and slow metabolisms are mutually exclusive: expected `failure.message.orEmpty().contains(\"METABOLIC_PACE\")` to be true")
    }

    @Test
    fun `growth pace and reproduction frequency alternatives are mutually exclusive`() {
        val growthConflict = predator("conflicting-growth").let { species ->
            species.copy(traits = species.traits + CommonTrait.SLOW_GROWTH + CommonTrait.RAPID_GROWTH)
        }
        val reproductionConflict = predator("conflicting-reproduction").let { species ->
            species.copy(
                traits = species.traits + CommonTrait.INFREQUENT_REPRODUCTION + CommonTrait.FREQUENT_REPRODUCTION,
            )
        }

        val growthFailure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(growthConflict))
        }
        val reproductionFailure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(reproductionConflict))
        }

        assertTrue(growthFailure.message.orEmpty().contains("GROWTH_PACE"))
        assertTrue(reproductionFailure.message.orEmpty().contains("REPRODUCTION_FREQUENCY"))
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
                CommonTrait.TRACHEA,
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
            traits = predator("conflicting-coverings").traits + listOf(CommonTrait.FUR, CommonTrait.FEATHERS),
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
        val groupedTraits = (CommonTrait.entries + ColorTrait.entries).filter { it.group != null }.groupBy { it.group }

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

        val adaptivePredator = brownPredator.copy(
            id = "adaptive-predator",
            traits = brownPredator.traits - ColorTrait.BROWN_COLORATION + ColorTrait.ADAPTIVE_COLORATION,
        )
        val adaptiveCompiled = EcologyCompiler.compile(listOf(adaptivePredator)).species.single()
        assertTrue(
            adaptiveCompiled.physiology.maintenanceDemand > compiled.physiology.maintenanceDemand,
            message = "Biological color is compiled from mutually exclusive traits: expected `adaptiveCompiled.physiology.maintenanceDemand > compiled.physiology.maintenanceDemand` to be true",
        )

        val conflicting = brownPredator.copy(
            traits = brownPredator.traits + ColorTrait.WHITE_COLORATION,
        )
        val failure = assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(conflicting))
        }
        assertTrue(failure.message.orEmpty().contains("BIOLOGICAL_COLOR"), message = "Biological color is compiled from mutually exclusive traits: expected `failure.message.orEmpty().contains(\"BIOLOGICAL_COLOR\")` to be true")
    }

    @Test
    fun `authored species satisfy every trait dependency`() {
        val failures = (EarthSpeciesCatalog.ALL + EarthSpeciesCatalog.EXTINCT_SPECIES + InvariantSpecies.ALL).flatMap { definition ->
            TraitDependencies.unmetRequirements(definition).map { failure ->
                "${definition.displayName}: ${failure.trait.displayName} " + failure.requirement.describe()
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.BONY_SKELETON,
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
            traits = predator("invalid-aerial-disperser").traits.filterNot { TraitCapability.REPRODUCTION in it.capabilities } + CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.CLONAL_PROPAGATION,
                CommonTrait.AERIAL_FLOATING_BODY,
            ),
        )
        val photosynthetic = floating.copy(
            id = "photosynthetic-floating-body",
            displayName = "Photosynthetic floating body",
            traits = floating.traits + CommonTrait.PHOTOSYNTHETIC_SURFACE + ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.CLONAL_PROPAGATION,
                CommonTrait.AERIAL_FLOATING_BODY,
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.BODY_UNDULATION,
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.AQUATIC_OVOSPORE,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.BONY_SKELETON,
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
                base.copy(traits = base.traits + CommonTrait.DENSE_UNDERCOAT.atLevel(2)),
            ).single().requirement is TraitRequirement.AllOf,
            message = "Dense undercoat should require its underlying fur structure",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(traits = base.traits + CommonTrait.SWIFT_LIMBS),
            ).isEmpty(),
            message = "Specialized anatomy requires its underlying structure: expected `TraitDependencies.unmetRequirements( base.copy(traits = base.traits + CommonTrait.SWIFT_LIMBS), ).isEmpty()` to be true",
        )
        assertTrue(
            TraitDependencies.unmetRequirements(
                base.copy(
                    traits = base.traits.filterNot { it == CommonTrait.WALKING_LIMBS || it == CommonTrait.LIMBED_BODY } + CommonTrait.SWIFT_LIMBS,
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
                    traits = base.traits.filterNot { it == CommonTrait.WALKING_LIMBS } + listOf(CommonTrait.CLIMBING_LIMBS, CommonTrait.STICKY_FEET),
                ),
            ).isEmpty(),
            message = "Assertion failed",
        )
    }

    @Test
    fun `expanded archetype traits require their underlying physiology`() {
        val terrestrial = predator("archetype-dependencies")
        fun unmet(vararg traits: SpeciesTrait): List<UnmetTraitRequirement> = TraitDependencies.unmetRequirements(
            terrestrial.copy(traits = terrestrial.traits + traits),
        )

        assertTrue(unmet(CommonTrait.BEHAVIORAL_THERMOREGULATION).isNotEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.BEHAVIORAL_THERMOREGULATION).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.SCHOOLING).isNotEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.SCHOOLING).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.REEF_BUILDING).isNotEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.REEF_BUILDING).isNotEmpty()` to be true")

        val ectotherm = terrestrial.copy(
            traits = terrestrial.traits.filterNot { it == CommonTrait.ENDOTHERMY } + CommonTrait.ECTOTHERMY + CommonTrait.BEHAVIORAL_THERMOREGULATION,
        )
        assertTrue(TraitDependencies.unmetRequirements(ectotherm).isEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `TraitDependencies.unmetRequirements(ectotherm).isEmpty()` to be true")
        assertTrue(
            unmet(CommonTrait.RIGID_COLONY_FRAMEWORK, CommonTrait.REEF_BUILDING).isEmpty(),
            message = "Expanded archetype traits require their underlying physiology: expected `unmet(CommonTrait.RIGID_COLONY_FRAMEWORK, CommonTrait.REEF_BUILDING).isEmpty()` to be true"
        )

        val aquatic = terrestrial.copy(
            traits = terrestrial.traits + CommonTrait.AQUATIC_LIMBS + CommonTrait.COLLECTIVE_LIVING + CommonTrait.SCHOOLING,
        )
        assertTrue(TraitDependencies.unmetRequirements(aquatic).isEmpty(), message = "Expanded archetype traits require their underlying physiology: expected `TraitDependencies.unmetRequirements(aquatic).isEmpty()` to be true")
    }

    @Test
    fun `hearing cognition and tool dependencies are explicit`() {
        val base = predator("dependency-base")
        fun unmet(vararg traits: SpeciesTrait) = TraitDependencies.unmetRequirements(base.copy(traits = base.traits + traits))

        assertTrue(unmet(CommonTrait.ECHOLOCATION).isNotEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.ECHOLOCATION).isNotEmpty()` to be true")
        assertTrue(
            unmet(CommonTrait.HEARING.atLevel(5), CommonTrait.ECHOLOCATION).isEmpty(),
            message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.HEARING.atLevel(5), CommonTrait.ECHOLOCATION).isEmpty()` to be true"
        )
        assertTrue(unmet(CommonTrait.INTELLIGENCE.atLevel(2)).isEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.INTELLIGENCE.atLevel(2)).isEmpty()` to be true")
        assertTrue(unmet(CommonTrait.INTELLIGENCE.atLevel(3)).isNotEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.INTELLIGENCE.atLevel(3)).isNotEmpty()` to be true")
        assertTrue(
            unmet(CommonTrait.SLOW_GROWTH, CommonTrait.INTELLIGENCE.atLevel(3)).isEmpty(),
            message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.SLOW_GROWTH, CommonTrait.INTELLIGENCE.atLevel(3)).isEmpty()` to be true"
        )
        assertTrue(unmet(CommonTrait.TOOL_MANIPULATION).isNotEmpty(), message = "Hearing cognition and tool dependencies are explicit: expected `unmet(CommonTrait.TOOL_MANIPULATION).isNotEmpty()` to be true")
        assertTrue(
            unmet(
                CommonTrait.INTELLIGENCE.atLevel(2),
                CommonTrait.TOOL_MANIPULATION,
            ).isEmpty(),
            message = "Hearing cognition and tool dependencies are explicit: expected `unmet( CommonTrait.INTELLIGENCE.atLevel(2), CommonTrait.TOOL_MANIPULATION, ).isEmpty()` to be true",
        )
        assertTrue(
            unmet(
                CommonTrait.SLOW_GROWTH,
                CommonTrait.INTELLIGENCE.atLevel(3),
                CommonTrait.TOOL_MANIPULATION,
            ).isEmpty(),
        )
    }

    @Test
    fun `small clade traits require their underlying structures and strategies`() {
        val base = predator("small-clade-dependencies")
        fun unmet(vararg traits: SpeciesTrait) = TraitDependencies.unmetRequirements(base.copy(traits = base.traits + traits))

        assertTrue(unmet(CommonTrait.TOOTH_WHORLS).isNotEmpty())
        assertTrue(
            unmet(CommonTrait.JAW, CommonTrait.TEETH, CommonTrait.TOOTH_WHORLS).isEmpty(),
        )
        assertTrue(unmet(CommonTrait.BIOLUMINESCENT_LURE).isNotEmpty())
        assertTrue(
            unmet(CommonTrait.BIOLUMINESCENCE, CommonTrait.BIOLUMINESCENT_LURE).isEmpty(),
        )
        assertTrue(unmet(CommonTrait.SAW_STRUCTURES).isEmpty())
        assertTrue(unmet(CommonTrait.FOOD_CLEANING_BEHAVIOR).isEmpty())
        assertTrue(unmet(CommonTrait.HERMAPHRODITISM).isEmpty())
        assertTrue(unmet(CommonTrait.PROJECTILE_CHEMICAL_SPRAY).isNotEmpty())
        assertTrue(
            unmet(CommonTrait.STINK_DEFENSE, CommonTrait.PROJECTILE_CHEMICAL_SPRAY).isEmpty(),
        )
        assertTrue(unmet(CommonTrait.INFRARED_SENSING).isEmpty())
        assertTrue(unmet(CommonTrait.PARTHENOGENESIS).isEmpty())

        val sessileCleaner = producer("sessile-cleaner").let { species ->
            species.copy(traits = species.traits + CommonTrait.FOOD_CLEANING_BEHAVIOR)
        }
        assertTrue(TraitDependencies.unmetRequirements(sessileCleaner).isNotEmpty())
    }

    @Test
    fun `cooperative behaviors require compatible social organization`() {
        val base = predator("cooperative-dependencies")
        fun unmet(organization: CommonTrait, behavior: CommonTrait) = TraitDependencies.unmetRequirements(
            base.copy(
                traits = base.traits.filter { it.group != TraitGroup.SOCIAL_ORGANIZATION } + organization + behavior,
            ),
        )

        assertTrue(unmet(CommonTrait.SOLITARY, CommonTrait.COOPERATIVE_HUNTING).isNotEmpty())
        assertTrue(unmet(CommonTrait.GROUP_LIVING, CommonTrait.COOPERATIVE_HUNTING).isEmpty())
        assertTrue(unmet(CommonTrait.COLLECTIVE_LIVING, CommonTrait.HERDING_BEHAVIOR).isEmpty())
        assertTrue(unmet(CommonTrait.SOLITARY, CommonTrait.HERDING_BEHAVIOR).isNotEmpty())
    }

    @Test
    fun `group huddling requires a non-solitary social organization`() {
        val base = predator("huddling-dependencies")
        fun unmet(organization: CommonTrait) = TraitDependencies.unmetRequirements(
            base.copy(
                traits = base.traits.filter { it.group != TraitGroup.SOCIAL_ORGANIZATION } + organization + CommonTrait.GROUP_HUDDLING,
            ),
        )

        assertTrue(unmet(CommonTrait.SOLITARY).isNotEmpty(), message = "Group huddling requires a non-solitary social organization: expected `unmet(CommonTrait.SOLITARY).isNotEmpty()` to be true")
        assertTrue(unmet(CommonTrait.GROUP_LIVING).isEmpty(), message = "Group huddling requires a non-solitary social organization: expected `unmet(CommonTrait.GROUP_LIVING).isEmpty()` to be true")
        assertTrue(unmet(CommonTrait.COLLECTIVE_LIVING).isEmpty(), message = "Group huddling requires a non-solitary social organization: expected `unmet(CommonTrait.COLLECTIVE_LIVING).isEmpty()` to be true")
        assertTrue(unmet(CommonTrait.EUSOCIAL_COLONY).isEmpty(), message = "Group huddling requires a non-solitary social organization: expected `unmet(CommonTrait.EUSOCIAL_COLONY).isEmpty()` to be true")
    }

    @Test
    fun `activity overlap modifies only terrestrial predation matchups`() {
        fun activePredator(id: String, pattern: CommonTrait) = predator(id).copy(traits = predator(id).traits + pattern)

        val matched = activePredator("matched", CommonTrait.DIURNAL)
        val neutral = activePredator("neutral", CommonTrait.CATHEMERAL)
        val mismatched = activePredator("mismatched", CommonTrait.NOCTURNAL)
        val prey = predator("day-prey", SizeClass.SMALL).copy(
            traits = predator("day-prey", SizeClass.SMALL).traits.filterNot { it == CommonTrait.AMBUSH_MUSCULATURE || it == CommonTrait.MEAT_EATING_MOUTHPARTS } + CommonTrait.GRAZING_MOUTHPARTS + CommonTrait.DIURNAL,
        )
        val ecology = EcologyCompiler.compile(listOf(matched, neutral, mismatched, prey))

        fun predationRate(predator: SpeciesDefinition) = ecology.interactions.get(
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
            traits = predator("rooted-predator").traits.filterNot { it == CommonTrait.WALKING_LIMBS } + CommonTrait.ROOTED_BODY,
        )

        assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }
    }

    @Test
    fun `sessile species cannot carry locomotion anatomy`() {
        val invalid = producer("walking-producer").let { species ->
            species.copy(
                traits = species.traits + CommonTrait.BONY_SKELETON + CommonTrait.LIMBED_BODY + CommonTrait.WALKING_LIMBS,
            )
        }

        assertFailsWith<IllegalArgumentException> {
            EcologyCompiler.compile(listOf(invalid))
        }
    }

    @Test
    fun `internal photosymbionts grant shallow water photosynthesis to anchored polyps`() {
        val coral = SpeciesDefinition(
            id = "photosymbiotic-polyp",
            displayName = "photosymbiotic polyp",
            sizeClass = SizeClass.SMALL,
            motile = false,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.PASSIVE_RESPIRATION,
                CommonTrait.AQUATIC_OVOSPORE,
                CommonTrait.SALTWATER_OSMOREGULATION,
                CommonTrait.POLYP_BODY,
                CommonTrait.INTERNAL_PHOTOSYMBIONTS,
            ),
        )
        val compiled = EcologyCompiler.compile(listOf(coral)).species.single()

        assertTrue(compiled.niche.accesses(EcoStrategy.PHOTOSYNTHESIS))
        assertEquals(30.0, compiled.environment.optimalMaximumWaterDepthM)
        assertEquals(80.0, compiled.environment.absoluteMaximumWaterDepthM)
        assertEquals(AquaticSalinityTolerance.SALTWATER_ONLY, compiled.physiology.respiration.salinityTolerance)
    }

    @Test
    fun `combined temperature tolerance effect widens outer and optimal ranges`() {
        val thermalAdaptation = EffectTrait(
            "broad thermal adaptation",
            "Broadens both productive and survivable temperatures.",
            listOf(
                TraitEffect.TemperatureTolerance(
                    colderC = 4.0,
                    hotterC = 3.0,
                    optimalColderC = 2.0,
                    optimalHotterC = 1.0,
                ),
            ),
        )
        val ordinary = EcologyCompiler.compile(listOf(predator("ordinary-thermal-range"))).species.single()
        val compiled = EcologyCompiler.compile(
            listOf(predator("thermal-generalist").let { it.copy(traits = it.traits + thermalAdaptation) }),
        ).species.single()

        assertEquals(ordinary.physiology.thermal.outerLowC - 4.0, compiled.physiology.thermal.outerLowC)
        assertEquals(ordinary.physiology.thermal.optimalLowC - 2.0, compiled.physiology.thermal.optimalLowC)
        assertEquals(ordinary.physiology.thermal.optimalHighC + 1.0, compiled.physiology.thermal.optimalHighC)
        assertEquals(ordinary.physiology.thermal.outerHighC + 3.0, compiled.physiology.thermal.outerHighC)
    }

    @Test
    fun `competition penalties above one are preserved`() {
        val grouped = predator("competition-sensitive").let { species ->
            species.copy(
                traits = species.traits.filter { it.group != TraitGroup.SOCIAL_ORGANIZATION } + CommonTrait.COLLECTIVE_LIVING,
            )
        }
        val compiled = EcologyCompiler.compile(listOf(grouped)).species.single()

        assertEquals(1.1, compiled.lifeHistory.nicheCompetitionSensitivity)
    }

    @Test
    fun `surface-attached sessile life does not require roots`() {
        val surfaceAttached = producer(
            "surface-attached-producer",
            traits = listOf(
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.PHOTOSYNTHETIC_SURFACE,
                CommonTrait.SURFACE_HOLDFAST,
                CommonTrait.INTERWOVEN_BODY,
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
                    CommonTrait.TRACHEA,
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.BONY_SKELETON,
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.VIVIPARITY,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.BONY_SKELETON,
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
        assertTrue(compiled.niche.accesses(EcoStrategy.GENERALIST_FORAGING))
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
            ecology.species[ecology.speciesIndex("herbivore")].niche.supportFor(EcoStrategy.GENERALIST_FORAGING),
            message = "Plant and animal feeding adaptations derive a generalist niche: expected `ecology.species[ecology.speciesIndex(\"herbivore\")] .niche.supportFor(EcoStrategy.GENERALIST_FORAGING)` to match `0.0`",
        )
    }

    @Test
    fun `brooding traits require external ovospores and provisioning requires a brood site`() {
        val ovospore = predator("ovospore-brooder")
        val viviparous = ovospore.copy(
            id = "viviparous-brooder",
            displayName = "viviparous brooder",
            traits = ovospore.traits.filterNot { it == CommonTrait.TERRESTRIAL_OVOSPORE } + CommonTrait.VIVIPARITY,
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
                    traits = namedOvospore.traits + CommonTrait.OVOSPORE_NEST
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.BONY_SKELETON,
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.VIVIPARITY,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.BONY_SKELETON,
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
            traits = predator("cooperative-medium", SizeClass.MEDIUM).traits.filter { it.group != TraitGroup.SOCIAL_ORGANIZATION } + CommonTrait.GROUP_LIVING + CommonTrait.COOPERATIVE_HUNTING,
        )
        val cooperativeLarge = predator("cooperative-large", SizeClass.LARGE).copy(
            traits = predator("cooperative-large", SizeClass.LARGE).traits.filter { it.group != TraitGroup.SOCIAL_ORGANIZATION } + CommonTrait.GROUP_LIVING + CommonTrait.COOPERATIVE_HUNTING,
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

        fun interaction(consumerId: String, targetId: String): InteractionKind = ecology.interactions.get(
            ecology.speciesIndex(consumerId),
            ecology.speciesIndex(targetId),
        ).kind

        val compiledCooperativeMedium = ecology.species[ecology.speciesIndex(cooperativeMedium.id)]
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
                CommonTrait.TRACHEA,
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.VIVIPARITY,
                CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.BONY_SKELETON,
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
            val pursuit = consumer.niche.supportFor(EcoStrategy.PURSUIT_PREDATION) > consumer.niche.supportFor(EcoStrategy.AMBUSH_PREDATION)
            val capture = consumer.interactions.captureAbility + if (pursuit) {
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
            traits = predator("surface-prey", SizeClass.SMALL).traits.filterNot { it == CommonTrait.AMBUSH_MUSCULATURE || it == CommonTrait.MEAT_EATING_MOUTHPARTS } + CommonTrait.GRAZING_MOUTHPARTS,
        )
        val burrowingPrey = surfacePrey.copy(
            id = "burrowing-prey",
            displayName = "burrowing-prey",
            traits = surfacePrey.traits + CommonTrait.DIGGING_LIMBS + CommonTrait.FOSSORIAL_LIVING,
        )
        val ecology = EcologyCompiler.compile(listOf(pouncer, surfacePrey, burrowingPrey))
        val pouncerIndex = ecology.speciesIndex(pouncer.id)
        val surfaceAttack = ecology.interactions.get(pouncerIndex, ecology.speciesIndex(surfacePrey.id)).targetLossRate
        val burrowAttack = ecology.interactions.get(pouncerIndex, ecology.speciesIndex(burrowingPrey.id)).targetLossRate

        assertTrue(burrowAttack > surfaceAttack, message = "High pouncing increases predation only against fossorial prey: expected `burrowAttack > surfaceAttack` to be true")
    }

    @Test
    fun `burrow borrowers require another local burrow builder`() {
        val builder = predator("burrow-builder", SizeClass.SMALL).copy(
            traits = predator("burrow-builder", SizeClass.SMALL).traits + CommonTrait.DIGGING_LIMBS + CommonTrait.BURROW_BUILDER,
        )
        val borrower = predator("burrow-borrower", SizeClass.SMALL).copy(
            traits = predator("burrow-borrower", SizeClass.SMALL).traits.filterNot { it == CommonTrait.AMBUSH_MUSCULATURE || it == CommonTrait.MEAT_EATING_MOUTHPARTS } + CommonTrait.GRAZING_MOUTHPARTS + CommonTrait.BURROW_BORROWER,
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
            traits = predator("sound-lurer", SizeClass.SMALL).traits + listOf(CommonTrait.CHIRPING_CALL, CommonTrait.SOUND_LURES),
        )
        val preyBase = predator("prey-base", SizeClass.SMALL).copy(
            traits = predator("prey-base", SizeClass.SMALL).traits.filterNot { it == CommonTrait.AMBUSH_MUSCULATURE || it == CommonTrait.MEAT_EATING_MOUTHPARTS } + CommonTrait.GRAZING_MOUTHPARTS,
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
        val sharedCallAttack = ecology.interactions.get(predatorIndex, ecology.speciesIndex(chirpingPrey.id)).targetLossRate
        val differentCallAttack = ecology.interactions.get(predatorIndex, ecology.speciesIndex(barkingPrey.id)).targetLossRate

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
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }.niche.producerCompetitionLayer,
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
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }.lifeHistory.dormantEntryBiomassRetention,
            message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species.single { it.id == InvariantSpecies.PLANKTON.id } .lifeHistory.dormantEntryBiomassRetention` to match `0.10`",
        )
        assertEquals(
            10.0,
            ecology.species.single { it.id == InvariantSpecies.PLANKTON.id }.lifeHistory.dormantReactivationMultiplier,
            message = "Invariant guilds compile as ordinary populations with explicit metadata: expected `ecology.species.single { it.id == InvariantSpecies.PLANKTON.id } .lifeHistory.dormantReactivationMultiplier` to match `10.0`",
        )
        assertTrue(
            ecology.species.filterNot { it.id == InvariantSpecies.PLANKTON.id }.all { it.lifeHistory.dormantEntryBiomassRetention == 1.0 },
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
                trait.effects.isNotEmpty() || trait.scale != null || trait.relationships.isNotEmpty(),
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
        traits = withReproduction(traits, CommonTrait.TERRESTRIAL_OVOSPORE) + ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
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
            CommonTrait.TRACHEA,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.TERRESTRIAL_OVOSPORE,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.BONY_SKELETON,
            CommonTrait.LIMBED_BODY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SOLITARY,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_COLORATION,
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
            CommonTrait.TRACHEA,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.BONY_SKELETON,
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
            CommonTrait.TRACHEA,
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
            CommonTrait.TRACHEA,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.VIVIPARITY,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.BONY_SKELETON,
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
            CommonTrait.TRACHEA,
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.BONY_SKELETON,
            CommonTrait.LIMBED_BODY,
            CommonTrait.AQUATIC_LIMBS,
            CommonTrait.SOLITARY,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
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
