package dev.biserman.planet.planet.ecology

enum class SizeClass(
    val typicalMassKg: Double,
    val maintenancePerKg: Double,
    val seasonalReproduction: Double,
    val densityScale: Double,
) {
    MINUSCULE(0.000_001, 0.34, 1.20, 8.0),
    TINY(0.01, 0.25, 0.90, 5.0),
    SMALL(2.5, 0.18, 0.60, 2.5),
    MEDIUM(50.0, 0.12, 0.34, 1.0),
    LARGE(500.0, 0.08, 0.18, 0.38),
    HUGE(5_000.0, 0.055, 0.10, 0.12),
    COLOSSAL(50_000.0, 0.040, 0.06, 0.04),
}

/**
 * A biological slot for which a species may select at most one implementation.
 * Groups describe genuine alternatives, not every collection of related traits.
 */
enum class TraitGroup : FulfillsTraitRequirement {
    BIOCHEMISTRY,
    THERMOREGULATION,
    METABOLIC_PACE,
    GROWTH_PACE,
    REPRODUCTION_FREQUENCY,
    DORMANCY_MODE,
    DISPERSAL_RANGE,
    SALINITY_STRATEGY,
    FILTERING_APPARATUS,
    GUT_FERMENTATION,
    SPECIALIZED_TONGUE,
    DOMINANT_BODY_COVERING,
    BODY_TYPE,
    BODY_PHYSIQUE,
    PHOTOSYNTHETIC_STRUCTURE,
    TERRESTRIAL_MOVEMENT_STRUCTURE,
    FLIGHT_STRUCTURE,
    ACTIVITY_PATTERN,
    COGNITIVE_COMPLEXITY,
    SOCIAL_ORGANIZATION,
    HEARING_ACUITY,
    SCENT_ACUITY,
    BURROW_REFUGE,
    BIOLOGICAL_COLOR,
    SKELETON,
    SCALE_TYPE,
    OVOSPORE_TYPE
}

/** Anatomical or behavioral capability that can satisfy another trait's prerequisites. */
enum class TraitCapability : FulfillsTraitRequirement {
    LOCOMOTION,
    TERRESTRIAL_LOCOMOTION,
    SUBSTRATE_ANCHORING,
    AQUATIC_LOCOMOTION,
    RESPIRATION,
    AERIAL_RESPIRATION,
    UNDERWATER_RESPIRATION,
    WATER_STORAGE,
    REPRODUCTION,
    SEXUAL_REPRODUCTION,
    OVOSPORE_REPRODUCTION,
    OVOSPORE_BROODING,
    OVOSPORE_BROOD_SITE,
    BROOD_HOST_RELATIONSHIP,
    BURROW_EXCAVATION,
    ADVANCED_COGNITION,
}

/** A recognizable sound pattern that another species may reproduce or exploit. */
enum class AcousticSignal {
    HOWL,
    RATTLE,
    ROAR,
    TRUMPET,
    BELLOW,
    BLEAT,
    GRUNT,
    HONK,
    BUGLE,
    CROAK,
    BRAY,
    HOOT,
    BARK,
    GROWL,
    SCREECH,
    QUACK,
    CROW,
    TRILL,
    WHISTLE,
    CLICK,
    BUZZ,
    CHIRP,
    MEOW,
    PURR,
    HISS,
    BOOM,
    WHOOP,
    SONG
}

interface FulfillsTraitRequirement

sealed interface TraitRequirement {
    fun isSatisfiedBy(
        definition: SpeciesDefinition,
        capabilities: Set<FulfillsTraitRequirement>,
    ): Boolean

    fun describe(): String

    data class AllOf(val requirements: Set<FulfillsTraitRequirement>) : TraitRequirement {
        init {
            require(requirements.isNotEmpty())
        }

        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = this.requirements.all {
            when (it) {
                is SpeciesTrait -> definition.hasTrait(it)
                is TraitCapability -> it in capabilities
                is TraitGroup -> definition.traits.any { trait -> it == trait.baseTrait.group }
                else -> throw IllegalArgumentException("Unexpected requirement type: $it")
            }
        }

        override fun describe(): String =
            "requires ${requirements.joinToString()}"
    }

    data class AnyOf(val requirements: Set<FulfillsTraitRequirement>) : TraitRequirement {
        init {
            require(requirements.isNotEmpty())
        }

        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = this.requirements.any {
            when (it) {
                is SpeciesTrait -> definition.hasTrait(it)
                is TraitCapability -> it in capabilities
                is TraitGroup -> definition.traits.any { trait -> it == trait.baseTrait.group }
                else -> throw IllegalArgumentException("Unexpected requirement type: $it")
            }
        }

        override fun describe(): String =
            "requires one of ${requirements.joinToString()}"
    }

    data class NoneOf(val requirements: Set<FulfillsTraitRequirement>) : TraitRequirement {
        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = this.requirements.none {
            when (it) {
                is SpeciesTrait -> definition.hasTrait(it)
                is TraitCapability -> it in capabilities
                is TraitGroup -> definition.traits.any { trait -> it == trait.baseTrait.group }
                else -> throw IllegalArgumentException("Unexpected requirement type: $it")
            }
        }

        override fun describe(): String =
            "requires none of ${requirements.joinToString()}"
    }

    data class SizeClassIs(val sizeClass: SizeClass) : TraitRequirement {
        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = definition.sizeClass == sizeClass

        override fun describe(): String = "requires $sizeClass size"
    }

    data class SizeClassAtLeast(val sizeClass: SizeClass) : TraitRequirement {
        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = definition.sizeClass >= sizeClass

        override fun describe(): String = "requires at least $sizeClass size"
    }

    data class SizeClassAtMost(val sizeClass: SizeClass) : TraitRequirement {
        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = definition.sizeClass <= sizeClass

        override fun describe(): String = "requires at most $sizeClass size"
    }

    data class MotilityIs(val motile: Boolean) : TraitRequirement {
        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = definition.motile == motile

        override fun describe(): String =
            if (motile) "requires a motile organism" else "requires a sessile organism"
    }

    data object HasAcousticSignal : TraitRequirement {
        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = definition.traits.any { it.baseTrait.acousticSignal != null }

        override fun describe(): String = "requires an acoustic call"
    }

    data class TraitLevelAtLeast(
        val trait: SpeciesTrait,
        val level: Int,
    ) : TraitRequirement {
        init {
            require(level >= 1)
        }

        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<FulfillsTraitRequirement>,
        ): Boolean = definition.traitLevel(trait) >= level

        override fun describe(): String =
            "requires ${trait.displayName} at level $level or higher"
    }

    companion object {
        fun allOf(vararg requirements: FulfillsTraitRequirement) = AllOf(requirements.toSet())
        fun anyOf(vararg requirements: FulfillsTraitRequirement) = AnyOf(requirements.toSet())
        fun noneOf(vararg requirements: FulfillsTraitRequirement) = NoneOf(requirements.toSet())

        fun sizeClassIs(sizeClass: SizeClass) = SizeClassIs(sizeClass)
        fun sizeClassAtLeast(sizeClass: SizeClass) = SizeClassAtLeast(sizeClass)
        fun sizeClassAtMost(sizeClass: SizeClass) = SizeClassAtMost(sizeClass)
        fun motile() = MotilityIs(true)
        fun sessile() = MotilityIs(false)
        fun traitLevelAtLeast(trait: SpeciesTrait, level: Int) = TraitLevelAtLeast(trait, level)
    }
}

sealed interface SpeciesTrait : FulfillsTraitRequirement {
    val displayName: String
    val description: String
    val effects: List<TraitEffect>
    val conditionalEffects: List<ConditionalTraitEffect>
        get() = emptyList()
    val interactionEffects: List<ConditionalInteractionEffect>
        get() = emptyList()
    val scale: TraitScale?
        get() = null
    val maxLevel: Int
        get() = scale?.maximumLevel ?: 1
    val relationships: List<RelationshipEffect>
        get() = emptyList()
    val invariantOnly: Boolean
        get() = false

    /** Cosmetic traits document recognizable biology without changing simulation outcomes. */
    val isCosmetic: Boolean
        get() = false
    val acousticSignal: AcousticSignal?
        get() = null
    val group: TraitGroup?
        get() = null
    val capabilities: Set<TraitCapability>
        get() = emptySet()
    val requirements: List<TraitRequirement>
        get() = emptyList()

    fun effectsAt(level: Int): List<TraitEffect> =
        scale?.definitionAt(level)?.effects ?: effects.also { require(level == 1) }

    fun conditionalEffectsAt(level: Int): List<ConditionalTraitEffect> =
        scale?.definitionAt(level)?.conditionalEffects ?: conditionalEffects.also { require(level == 1) }

    fun interactionEffectsAt(level: Int): List<ConditionalInteractionEffect> =
        scale?.definitionAt(level)?.interactionEffects ?: interactionEffects.also { require(level == 1) }

    fun capabilitiesAt(level: Int): Set<TraitCapability> =
        scale?.definitionAt(level)?.capabilities ?: capabilities.also { require(level == 1) }
}

/**
 * A named trait for species-specific effects that do not belong in the shared
 * [CommonTrait] catalog.
 */
data class EffectTrait(
    override val displayName: String,
    override val description: String,
    override val effects: List<TraitEffect>,
    override val conditionalEffects: List<ConditionalTraitEffect> = emptyList(),
    override val interactionEffects: List<ConditionalInteractionEffect> = emptyList(),
    override val group: TraitGroup? = null,
    override val capabilities: Set<TraitCapability> = emptySet(),
    override val requirements: List<TraitRequirement> = emptyList(),
) : SpeciesTrait {
    init {
        require(displayName.isNotBlank())
        require(description.isNotBlank())
        require(effects.isNotEmpty() || conditionalEffects.isNotEmpty() || interactionEffects.isNotEmpty())
    }
}

data class TargetedRelationshipTrait(
    override val displayName: String,
    override val description: String,
    override val relationships: List<RelationshipEffect>,
    val maintenanceCost: Double,
    override val group: TraitGroup? = null,
    override val capabilities: Set<TraitCapability> = emptySet(),
    override val requirements: List<TraitRequirement> = emptyList(),
) : SpeciesTrait {
    init {
        require(displayName.isNotBlank())
        require(description.isNotBlank())
        require(relationships.isNotEmpty())
        require(maintenanceCost > 0.0)
    }

    override val effects: List<TraitEffect> =
        listOf(TraitEffect.MaintenanceCost(maintenanceCost))
}

/** Authored host dependency paired with [CommonTrait.BROOD_PARASITISM]. */
fun broodParasitismOf(
    hostSpeciesId: String,
    hostDisplayName: String,
): TargetedRelationshipTrait =
    TargetedRelationshipTrait(
        displayName = "$hostDisplayName brood host",
        description =
        "Reproductive timing, ovospore mimicry, or host manipulation is specialized around placing offspring with $hostDisplayName.",
        relationships = listOf(
            RelationshipEffect.RequiresTarget(
                SpeciesSelector.ExactSpecies(hostSpeciesId),
            ),
        ),
        maintenanceCost = 0.06,
        capabilities = setOf(TraitCapability.BROOD_HOST_RELATIONSHIP),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.OVOSPORE_BROODING)),
        ),
    )

enum class ColorTrait(
    override val displayName: String,
    override val description: String,
    colorEffect: TraitEffect,
    maintenanceCost: Double = 0.0,
    traitEffects: List<TraitEffect> = listOf()
) : SpeciesTrait {
    BLACK_COLORATION("black coloration", "Dark pigments conceal the body against very dim backgrounds.", TraitEffect.CamouflageColor(BiologicalColor.BLACK)),
    BROWN_COLORATION("brown coloration", "Earth-toned pigments conceal the body against soil, bark, and dry vegetation.", TraitEffect.CamouflageColor(BiologicalColor.BROWN)),
    GREEN_COLORATION("green coloration", "Green pigments conceal the body among photosynthetic growth.", TraitEffect.CamouflageColor(BiologicalColor.GREEN)),
    BLUE_COLORATION("blue coloration", "Blue pigments conceal the body in blue-lit environments.", TraitEffect.CamouflageColor(BiologicalColor.BLUE)),
    RED_COLORATION("red coloration", "Red pigments conceal or signal where longer wavelengths dominate.", TraitEffect.CamouflageColor(BiologicalColor.RED)),
    PURPLE_COLORATION("purple coloration", "Purple pigments conceal or signal against similarly colored surroundings.", TraitEffect.CamouflageColor(BiologicalColor.PURPLE)),
    YELLOW_COLORATION("yellow coloration", "High-saturation yellow pigments conceal the body in deserts and dry grasslands.", TraitEffect.CamouflageColor(BiologicalColor.YELLOW)),
    PALE_COLORATION("pale coloration", "Low-saturation pigments conceal the body in deserts and dry grasslands.", TraitEffect.CamouflageColor(BiologicalColor.PALE), maintenanceCost = -0.06),
    WHITE_COLORATION("white coloration", "White tissues, hairs, or feathers conceal the body against snow and ice.", TraitEffect.CamouflageColor(BiologicalColor.WHITE)),
    COUNTERSHADE_COLORATION("countershading", "A dark upper surface and light underside reduce contrast in sunlit water.", TraitEffect.CamouflageColor(BiologicalColor.COUNTERSHADE)),
    ADAPTIVE_COLORATION("adaptive coloration", "Pigment cells actively change the body's color and pattern to match its surroundings.", TraitEffect.CamouflageColor(BiologicalColor.ADAPTIVE), maintenanceCost = 0.24),
    RAINBOW_COLORATION(
        "rainbow coloration",
        "Sub-microscopic reflective structures or a variety of different pigments give the organism a rainbow coloration.",
        TraitEffect.CamouflageColor(BiologicalColor.RAINBOW),
        maintenanceCost = 0.12,
        traitEffects = listOf(
            TraitEffect.ReproductionMultiplier(1.3),
        )
    ),

    BLACK_PHOTOSYNTHETIC_PIGMENTS("black photosynthetic pigments", "Broad-spectrum pigments absorb most visible wavelengths.", TraitEffect.PhotosyntheticColor(BiologicalColor.BLACK)),
    BROWN_PHOTOSYNTHETIC_PIGMENTS("brown photosynthetic pigments", "Brown photosynthetic pigments balance absorption across a broad spectrum.", TraitEffect.PhotosyntheticColor(BiologicalColor.BROWN)),
    GREEN_PHOTOSYNTHETIC_PIGMENTS("green photosynthetic pigments", "Green photosynthetic tissues absorb red and blue wavelengths efficiently.", TraitEffect.PhotosyntheticColor(BiologicalColor.GREEN)),
    BLUE_PHOTOSYNTHETIC_PIGMENTS("blue photosynthetic pigments", "Blue photosynthetic pigments favor the wavelengths available in their light environment.", TraitEffect.PhotosyntheticColor(BiologicalColor.BLUE)),
    RED_PHOTOSYNTHETIC_PIGMENTS("red photosynthetic pigments", "Red photosynthetic pigments favor the wavelengths available in their light environment.", TraitEffect.PhotosyntheticColor(BiologicalColor.RED)),
    PURPLE_PHOTOSYNTHETIC_PIGMENTS("purple photosynthetic pigments", "Purple photosynthetic pigments favor the wavelengths available in their light environment.", TraitEffect.PhotosyntheticColor(BiologicalColor.PURPLE)),
    YELLOW_PHOTOSYNTHETIC_PIGMENTS("yellow photosynthetic pigments", "Yellow photosynthetic pigments favor the wavelengths available in their light environment.", TraitEffect.PhotosyntheticColor(BiologicalColor.YELLOW)),
    PALE_PHOTOSYNTHETIC_PIGMENTS("pale photosynthetic pigments", "Sparse photosynthetic pigments trade light capture for lower tissue investment.", TraitEffect.PhotosyntheticColor(BiologicalColor.PALE)),
    WHITE_PHOTOSYNTHETIC_PIGMENTS("white photosynthetic pigments", "Reflective photosynthetic tissues limit excess light absorption.", TraitEffect.PhotosyntheticColor(BiologicalColor.WHITE)),
    ADAPTIVE_PHOTOSYNTHETIC_PIGMENTS("adaptive photosynthetic pigments", "Pigment concentrations shift to match changing light spectra.", TraitEffect.PhotosyntheticColor(BiologicalColor.ADAPTIVE), maintenanceCost = 0.24),
    ;

    override val effects: List<TraitEffect> =
        listOfNotNull(
            colorEffect,
            TraitEffect.MaintenanceCost(maintenanceCost)
        ).plus(traitEffects)
    override val group: TraitGroup = TraitGroup.BIOLOGICAL_COLOR

    companion object {
        fun camouflage(color: BiologicalColor): ColorTrait =
            entries.single { TraitEffect.CamouflageColor(color) in it.effects }

        fun photosynthetic(color: BiologicalColor): ColorTrait =
            entries.single { TraitEffect.PhotosyntheticColor(color) in it.effects }
    }
}

/**
 * A deliberately small starter library. Adding content means adding a readable
 * entry here (or another SpeciesTrait implementation), not changing the turn loop.
 */
enum class CommonTrait(
    override val displayName: String,
    override val description: String,
    override val effects: List<TraitEffect>,
    override val conditionalEffects: List<ConditionalTraitEffect> = emptyList(),
    override val interactionEffects: List<ConditionalInteractionEffect> = emptyList(),
    override val scale: TraitScale? = null,
    override val invariantOnly: Boolean = false,
    override val isCosmetic: Boolean = false,
    override val acousticSignal: AcousticSignal? = null,
    override val group: TraitGroup? = null,
    override val capabilities: Set<TraitCapability> = emptySet(),
    override val requirements: List<TraitRequirement> = emptyList(),
    override val relationships: List<RelationshipEffect> = emptyList(),
) : SpeciesTrait {
    // Biochemistry
    TEMPERATE_BIOCHEMISTRY(
        "temperate biochemistry",
        "Cellular chemistry that functions best at moderate temperatures.",
        listOf(TraitEffect.MaintenanceCost(0.0)),
        group = TraitGroup.BIOCHEMISTRY,
    ),
    FRIGID_BIOCHEMISTRY(
        "frigid biochemistry",
        "Cellular chemistry built around reactions and structures that remain viable in persistently frigid climates.",
        listOf(
            TraitEffect.TemperatureShift(-25.0),
            TraitEffect.ReproductionMultiplier(0.88),
            TraitEffect.MaintenanceCost(0.14),
        ),
        group = TraitGroup.BIOCHEMISTRY,
    ),
    HOT_BIOCHEMISTRY(
        "hot biochemistry",
        "Cellular chemistry whose molecules and membranes remain stable in persistently hot climates.",
        listOf(
            TraitEffect.TemperatureShift(28.0),
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.BIOCHEMISTRY,
    ),
    INVARIANT_RESISTANCE(
        "invariant guild resilience",
        "Broad tolerance representing many locally adapted, interchangeable species grouped into one aggregate population.",
        listOf(
            TraitEffect.TemperatureTolerance(
                colderC = 32.0,
                hotterC = 30.0,
                optimalColderC = 30.0,
                optimalHotterC = 20.0,
            ),
            TraitEffect.WaterRequirement(-0.25),
            TraitEffect.ReserveCapacity(0.15),
            TraitEffect.NicheCompetitionSensitivity(0.15),
            TraitEffect.Dormancy(DormancyKind.PROPAGULE, 0.9995),
            TraitEffect.BroadSalinityTolerance,
            TraitEffect.ReproductionMultiplier(0.88),
            TraitEffect.MaintenanceCost(0.18),
        ),
        invariantOnly = true,
        group = TraitGroup.DORMANCY_MODE,
    ),
    ANTIFREEZE_PROTEINS(
        "antifreeze proteins",
        "Circulating molecules inhibit destructive ice-crystal growth in exposed body fluids.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 12.0, hotterC = -1.0),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    COLD_ACTIVE_ENZYMES(
        "cold-active enzymes",
        "Specialized metabolic enzymes retain useful reaction rates in cold water but become unstable at ordinary warm temperatures.",
        listOf(
            TraitEffect.TemperatureShift(-13.0),
            TraitEffect.ReproductionMultiplier(0.90),
            TraitEffect.MaintenanceCost(0.16),
        ),
    ),
    HEAT_STABLE_ENZYMES(
        "heat-stable enzymes",
        "Proteins and cell membranes remain functional through sustained hot conditions without shifting the organism's entire biochemical regime.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -2.0, hotterC = 10.0),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.14),
        ),
    ),
    WARM_WATER_ENZYMES(
        "warm-water enzymes",
        "Metabolic enzymes and membranes remain stable and active in persistently warm water, at the cost of poor cold performance.",
        listOf(
            TraitEffect.TemperatureShift(5.0),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.08),
        ),
    ),

    VASCULAR_SYSTEM(
        "vascular system",
        "A network of fluid-filled vessels distributes respiratory gases and nutrients between tissues throughout the body.",
        listOf(TraitEffect.MaintenanceCost(0.06)),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.RESPIRATION)),
    ),
    PROLONGED_BREATH_HOLDING(
        "prolonged breath-holding",
        "Large internal oxygen stores and dive responses sustain repeated activity far from an immediately accessible shore.",
        listOf(
            TraitEffect.AquaticRespiration(AquaticRespirationMode.BREATH_HOLDING),
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),

    // Thermoregulation, growth, and metabolism
    MICROSCOPIC_RESTING_STAGES(
        "microscopic resting stages",
        "A small fraction of the active population forms durable cysts, spores, or resting eggs that preserve the lineage through dark or otherwise unproductive seasons.",
        listOf(
            TraitEffect.DormantEntryBiomassRetention(0.10),
            TraitEffect.DormantReactivationMultiplier(10.00),
            TraitEffect.ReproductionMultiplier(1.03),
            TraitEffect.MaintenanceCost(0.003),
        ),
        invariantOnly = true,
    ),
    THAW_DEPENDENT_GROWTH(
        "thaw-dependent growth",
        "Living ground cover can overwinter below freezing, but requires liquid water and a thawed growing season to renew its tissues.",
        listOf(
            TraitEffect.MinimumActiveTemperature(0.0),
            TraitEffect.FrozenDormantSurvival(0.98),
            TraitEffect.ReproductionMultiplier(1.03),
            TraitEffect.MaintenanceCost(0.06),
        ),
        invariantOnly = true,
        requirements = listOf(
            TraitRequirement.allOf(TraitGroup.PHOTOSYNTHETIC_STRUCTURE),
        ),
    ),
    ECTOTHERMY(
        "ectothermy",
        "Body activity and temperature depend primarily on heat exchanged with the surrounding environment.",
        listOf(
            TraitEffect.ThermalRegulation(ThermalStrategy.ECTOTHERMY),
            TraitEffect.MetabolicDemandMultiplier(0.875),
            TraitEffect.MaintenanceCost(-0.66),
            TraitEffect.TemperatureTolerance(colderC = -2.0, hotterC = -2.0),
        ),
        group = TraitGroup.THERMOREGULATION,
    ),
    ENDOTHERMY(
        "endothermy",
        "Metabolism produces enough heat to regulate the body substantially independently of ambient temperature.",
        listOf(
            TraitEffect.ThermalRegulation(ThermalStrategy.ENDOTHERMY),
            TraitEffect.MetabolicDemandMultiplier(1.1),
            TraitEffect.TemperatureTolerance(colderC = 8.0, hotterC = 2.0),
            TraitEffect.MaintenanceCost(0.75),
        ),
        group = TraitGroup.THERMOREGULATION,
    ),
    HETEROTHERMY(
        "heterothermy",
        "Body temperature is actively regulated at some times but allowed to vary during torpor, rest, or unfavorable seasons.",
        listOf(
            TraitEffect.ThermalRegulation(ThermalStrategy.HETEROTHERMY),
            TraitEffect.TemperatureTolerance(colderC = 5.0, hotterC = 1.0),
            TraitEffect.ReserveCapacity(0.20),
            TraitEffect.MaintenanceCost(0.24),
        ),
        group = TraitGroup.THERMOREGULATION,
    ),
    BEHAVIORAL_THERMOREGULATION(
        "behavioral thermoregulation",
        "The organism moves between sun, shade, water, shelter, or differently oriented surfaces to keep its body near a useful temperature.",
        listOf(
            TraitEffect.TemperatureTolerance(
                optimalColderC = 4.0,
                optimalHotterC = 4.0,
            ),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(
            TraitRequirement.allOf(ECTOTHERMY),
        ),
    ),
    SLOW_METABOLISM(
        "extremely slow metabolism",
        "Low-throughput digestion and cellular metabolism extract energy from poor food while sharply limiting growth and reproduction.",
        listOf(
            TraitEffect.MetabolicDemandMultiplier(0.55),
            TraitEffect.ReproductionMultiplier(0.65),
            TraitEffect.MaintenanceCost(0.09),
            TraitEffect.PursuitSpeed(-0.5)
        ),
        group = TraitGroup.METABOLIC_PACE,
    ),
    FAST_METABOLISM(
        "fast metabolism",
        "A high-throughput metabolism rapidly supplies active tissues with energy, but requires a large and reliable food intake.",
        listOf(
            TraitEffect.MetabolicDemandMultiplier(1.35),
            TraitEffect.ReproductionMultiplier(1.12),
            TraitEffect.PursuitSpeed(0.08),
            TraitEffect.MaintenanceCost(0.09),
        ),
        group = TraitGroup.METABOLIC_PACE,
    ),
    SLOW_GROWTH(
        "slow growth",
        "New tissue and mature body mass accumulate gradually, reducing continual construction costs but slowing population biomass recovery.",
        listOf(
            TraitEffect.ReproductionMultiplier(0.72),
            TraitEffect.MaintenanceCost(-0.48),
        ),
        group = TraitGroup.GROWTH_PACE,
    ),
    RAPID_GROWTH(
        "rapid growth",
        "Exceptionally fast production of new shoots and tissues allows an organism to replace losses and spread quickly when conditions are favorable.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.75),
            TraitEffect.MaintenanceCost(1.05),
        ),
        group = TraitGroup.GROWTH_PACE,
    ),
    PROLONGED_JUVENILE_DORMANCY(
        "prolonged juvenile dormancy",
        "A slow-growing juvenile stage remains protected and minimally active within a substrate for many annual cycles before synchronized emergence.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.PROLONGED_JUVENILE, 0.998),
            TraitEffect.DormantReactivationMultiplier(3.0),
            TraitEffect.MetabolicDemandMultiplier(0.55),
            TraitEffect.ReproductionMultiplier(0.28),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),

    // skeleton
    CARTILAGINOUS_SKELETON(
        "cartilaginous skeleton",
        "A skeleton dominated by cartilage provides flexible support with less mineralized tissue, at the cost of reduced rigid protection.",
        listOf(
            TraitEffect.BodyMassMultiplier(0.98),
            TraitEffect.Defense(-0.015),
            TraitEffect.MaintenanceCost(-0.06),
        ),
        group = TraitGroup.SKELETON,
    ),
    BONY_SKELETON(
        "bony skeleton",
        "A rigid mineralized internal skeleton supports powerful movement and protects internal organs at a modest construction cost.",
        listOf(
            TraitEffect.BodyMassMultiplier(1.02),
            TraitEffect.Defense(0.015),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.SKELETON,
    ),
    MOLTING_EXOSKELETON(
        "molting exoskeleton",
        "A segmented external skeleton supports the body and resists injury but must periodically be shed and rebuilt as the organism grows.",
        listOf(
            TraitEffect.Defense(0.14),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.SKELETON,
    ),

    // Body plan
    LIMBED_BODY(
        "limbed body",
        "A body plan with paired or repeated articulated limbs supports controlled movement, handling, and posture.",
        listOf(
            TraitEffect.MaintenanceCost(0.06)
        ),
        group = TraitGroup.BODY_TYPE,
        requirements = listOf(
            TraitRequirement.anyOf(VASCULAR_SYSTEM),
            TraitRequirement.anyOf(TraitGroup.SKELETON)
        )
    ),
    MANTLED_BODY(
        "mantled body",
        "An unsegmented body organized around a muscular mantle and visceral mass permits flexible growth and movement but leaves soft tissues vulnerable without additional protection.",
        listOf(
            TraitEffect.WaterRequirement(0.06),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.BODY_TYPE,
        requirements = listOf(
            TraitRequirement.anyOf(VASCULAR_SYSTEM),
        ),
    ),
    PRIMITIVE_BODY(
        "primitive body",
        "A simple body with little regional specialization or structural reinforcement requires little additional upkeep but provides poor protection and limited leverage for rapid movement.",
        listOf(
            TraitEffect.PursuitSpeed(-0.5),
            TraitEffect.Defense(-0.2),
            TraitEffect.MaintenanceCost(0.01),
        ),
        group = TraitGroup.BODY_TYPE,
        requirements = listOf(
            TraitRequirement.sizeClassAtMost(SizeClass.SMALL)
        )
    ),
    GELATINOUS_BODY(
        "gelatinous body",
        "A mostly water-filled body achieves large volume and buoyancy with little metabolically expensive tissue, at the cost of poor resistance to attack.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.AQUATIC, 0.3),
            TraitEffect.HabitatAffinity(HabitatGroup.LAND, -0.2),
            TraitEffect.MetabolicDemandMultiplier(0.72),
            TraitEffect.Defense(-0.14),
            TraitEffect.ReproductionMultiplier(1.08),
            TraitEffect.MaintenanceCost(0.02),
        ),
        group = TraitGroup.BODY_TYPE,
        requirements = listOf(
            TraitRequirement.noneOf(VASCULAR_SYSTEM),
        )
    ),
    POLYP_BODY(
        "anchored polyp body",
        "A mouth surrounded by flexible feeding structures projects from an attached body that can withdraw or contract when disturbed.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.AQUATIC, 0.3),
            TraitEffect.HabitatAffinity(HabitatGroup.LAND, -0.2),
            TraitEffect.StrategyAffinity(EcoStrategy.FILTER_FEEDING, 0.05),
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.05),
            TraitEffect.CaptureAbility(0.06),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.BODY_TYPE,
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
        requirements = listOf(
            TraitRequirement.noneOf(VASCULAR_SYSTEM),
            TraitRequirement.noneOf(TraitCapability.LOCOMOTION),
            TraitRequirement.sessile(),
        )
    ),
    ROOTED_BODY(
        "rooted body",
        "True roots penetrate terrestrial substrate, anchoring the body while gathering water and dissolved nutrients.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.LAND_SURFACE, 0.65),
            TraitEffect.MaintenanceCost(0.02),
        ),
        group = TraitGroup.BODY_TYPE,
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
        requirements = listOf(
            TraitRequirement.noneOf(TraitCapability.LOCOMOTION),
            TraitRequirement.sessile(),
        )
    ),
    INTERWOVEN_BODY(
        "interwoven body",
        "Numerous short shoots, branches, or filaments overlap into a continuous low mat that retains moisture and resists being displaced.",
        listOf(
            TraitEffect.WaterRequirement(-0.04),
            TraitEffect.Defense(0.04),
            TraitEffect.NicheCompetitionSensitivity(0.88),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.105),
        ),
        group = TraitGroup.BODY_TYPE,
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
    ),
    AERIAL_FLOATING_BODY(
        "aerial floating body",
        "A minuscule, low-density body with drag-producing surfaces or buoyant chambers that remains suspended in atmospheric currents.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.AERIAL, 0.3),
            TraitEffect.PelagicAerialResidency,
            TraitEffect.WaterRequirement(-0.15),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.02),
        ),
        group = TraitGroup.BODY_TYPE,
        requirements = listOf(TraitRequirement.SizeClassIs(SizeClass.MINUSCULE)),
    ),

    // osmoregulation
    SALTWATER_OSMOREGULATION(
        "saltwater osmoregulation",
        "Membranes and excretory structures maintain internal chemistry against the concentrated salts of marine water.",
        listOf(
            TraitEffect.HabitatAffinity(HabitatGroup.SALTWATER, 0.10),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.SALINITY_STRATEGY,
    ),
    FRESHWATER_OSMOREGULATION(
        "freshwater osmoregulation",
        "Membranes and excretory structures that maintain internal chemistry in dilute freshwater.",
        listOf(
            TraitEffect.HabitatAffinity(HabitatGroup.FRESHWATER, 0.15),
            TraitEffect.FreshwaterOsmoregulation,
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.SALINITY_STRATEGY,
    ),
    EURYHALINE_OSMOREGULATION(
        "euryhaline osmoregulation",
        "Adjustable membranes, kidneys, salt glands, or analogous organs permit repeated transitions between dilute freshwater and salty water.",
        listOf(
            TraitEffect.HabitatAffinity(HabitatGroup.FRESHWATER, 0.1),
            TraitEffect.HabitatAffinity(HabitatGroup.SALTWATER, 0.1),
            TraitEffect.BroadSalinityTolerance,
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.21),
        ),
        group = TraitGroup.SALINITY_STRATEGY,
    ),

    // climate adaptations
    CONCENTRATED_URINE(
        "concentrated urine",
        "Highly water-retentive kidneys excrete dissolved wastes in a small volume of concentrated urine.",
        listOf(
            TraitEffect.WaterRequirement(-0.07),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    SAND_ADAPTATION(
        "sand adaptation",
        "Anatomy and behavior suited to persist in sandy, freely draining terrain with scarce surface water.",
        listOf(
            TraitEffect.WaterRequirement(-0.07),
            TraitEffect.MaintenanceCost(0.03)
        ),
    ),
    SWEAT_GLANDS(
        "sweat glands",
        "Skin glands that cool the body by evaporating secreted water.",
        listOf(
            TraitEffect.TemperatureTolerance(hotterC = 9.0),
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.21),
        ),
    ),
    MASSIVE_EARS(
        "massive heat-radiating ears",
        "Large thin appendages with rich circulation that exchange heat rapidly with the air.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -3.0, hotterC = 6.0),
            TraitEffect.Defense(-0.04),
            TraitEffect.Sensing(0.01),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    WATER_STORAGE_TISSUE(
        "water-storage tissue",
        "Specialized tissues that retain a usable water reserve through dry periods.",
        listOf(
            TraitEffect.WaterRequirement(-0.22),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.18),
        ),
        capabilities = setOf(TraitCapability.WATER_STORAGE),
    ),
    SNOW_AND_ICE_LICKING(
        "snow and ice licking",
        "The organism deliberately consumes snow or surface ice when liquid drinking water is unavailable.",
        listOf(
            TraitEffect.SnowHydration,
            TraitEffect.MetabolicDemandMultiplier(1.1),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    FOOD_DERIVED_WATER(
        "food-derived water",
        "Efficient kidneys and digestion obtain nearly all required water from moist food or metabolically produced water.",
        listOf(
            TraitEffect.WaterRequirement(-0.14),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    FROST_SENSITIVE_SUCCULENT_TISSUES(
        "frost-sensitive succulent tissues",
        "Large water-filled cells tolerate extreme heat and drought but are readily damaged when their fluids freeze.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -5.0, hotterC = 2.0),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.WATER_STORAGE),
        ),
    ),
    DROUGHT_DECIDUOUS_LEAVES(
        "drought-deciduous leaves",
        "Photosynthetic surfaces are shed during dry seasons and regrown when water becomes available.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.PHOTOSYNTHESIS, 1.0),
            TraitEffect.WaterRequirement(-0.12),
            TraitEffect.Dormancy(DormancyKind.DROUGHT_DECIDUOUS, 0.999),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.36),
        ),
        group = TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
    ),
    SEASONAL_LEAF_DORMANCY(
        "seasonal leaf dormancy",
        "Growth and exposed foliage are withdrawn during the cold or dark season while protected living tissues persist.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.COLD_DARK_LEAF_DORMANCY, 0.999),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.DORMANCY_MODE,
        requirements = listOf(
            TraitRequirement.allOf(TraitGroup.PHOTOSYNTHETIC_STRUCTURE),
        ),
    ),
    FROST_HARDENED_TISSUES(
        "frost-hardened tissues",
        "Seasonal changes in cell fluids and exposed tissues reduce damage from freezing without shifting the organism's entire biochemistry.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 12.0, hotterC = -2.0),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    SALT_EXCLUDING_ROOTS(
        "salt-excluding roots",
        "Root membranes limit the uptake of dissolved salts while drawing water from coastal sediment.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.COASTAL, 0.65),
            TraitEffect.HabitatAffinity(Habitat.LAND_SURFACE, -0.5),
            TraitEffect.HabitatAffinity(Habitat.FRESHWATER, -0.33),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(ROOTED_BODY, TraitCapability.SUBSTRATE_ANCHORING),
        ),
    ),
    BLUBBER(
        "blubber",
        "A thick subcutaneous fat layer that insulates the body in water and doubles as an energy reserve.",
        listOf(
            TraitEffect.TemperatureShift(-10.0),
            TraitEffect.TemperatureTolerance(colderC = 6.0),
            TraitEffect.ReserveCapacity(0.28),
            TraitEffect.CaptureAbility(-0.03),
            TraitEffect.MaintenanceCost(0.24),
        ),
    ),
    FAT_RESERVES(
        "seasonal fat reserves",
        "Energy-dense tissues accumulated during abundance and consumed when intake later falls.",
        listOf(
            TraitEffect.ReserveCapacity(0.45),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    PERENNIAL_STORAGE_TISSUE(
        "perennial storage tissue",
        "Long-lived stems, roots, or analogous organs store energy across unfavorable seasons and rebuild active tissue later.",
        listOf(
            TraitEffect.ReserveCapacity(0.35),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    CACHED_FOOD(
        "cached food",
        "Surplus food is hidden or otherwise stored during abundance and recovered during later scarcity.",
        listOf(
            TraitEffect.ReserveCapacity(0.32),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    DESICCATION_RESISTANT_PROPAGULES(
        "desiccation-resistant propagules",
        "Seeds, spores, cysts, or other dispersal bodies that remain viable after losing most of their water.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.PROPAGULE, 0.97),
            TraitEffect.ReproductionMultiplier(0.90),
            TraitEffect.MaintenanceCost(0.09),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),
    SEASONAL_TORPOR(
        "seasonal torpor",
        "A reversible low-activity state that sharply reduces ecological activity during an unfavorable season.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.SEASONAL_TORPOR, 0.99),
            TraitEffect.Defense(-0.05),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),
    WHOLE_BODY_ANHYDROBIOSIS(
        "whole-body anhydrobiosis",
        "The active organism can dry into a nearly ametabolic state and revive after water returns.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.WHOLE_BODY_DESICCATION, 0.90),
            TraitEffect.ReproductionMultiplier(0.78),
            TraitEffect.MaintenanceCost(0.3),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),

    // Respiration
    TRACHEA(
        "trachea",
        "Internal air-filled exchange surfaces extract respiratory gases from the atmosphere and deliver them to the body.",
        listOf(
            TraitEffect.MaintenanceCost(0.09),
        ),
        capabilities = setOf(
            TraitCapability.RESPIRATION,
            TraitCapability.AERIAL_RESPIRATION,
        ),
    ),
    PASSIVE_RESPIRATION(
        "passive respiration",
        "Respiratory gases diffuse across the body surface without dedicated pumping or specialized internal exchange organs.",
        listOf(
            TraitEffect.AquaticRespiration(AquaticRespirationMode.UNDERWATER),
            TraitEffect.MaintenanceCost(0.03),
            TraitEffect.Defense(-0.1)
        ),
        capabilities = setOf(
            TraitCapability.RESPIRATION,
            TraitCapability.UNDERWATER_RESPIRATION
        ),
    ),
    GILLS(
        "gills",
        "Thin, blood-supplied folds extract dissolved respiratory gases from water as it passes over them.",
        listOf(
            TraitEffect.AquaticRespiration(AquaticRespirationMode.UNDERWATER),
            TraitEffect.MaintenanceCost(0.06),
        ),
        capabilities = setOf(
            TraitCapability.RESPIRATION,
            TraitCapability.UNDERWATER_RESPIRATION,
        ),
    ),

    // Reproduction
    EXTENDED_PARENTAL_CARE(
        "extended parental care",
        "Parents protect, feed, teach, or transport offspring through a prolonged vulnerable period, improving juvenile survival at a substantial energetic cost.",
        listOf(
            TraitEffect.Defense(0.05),
            TraitEffect.ReproductionMultiplier(1.12),
            TraitEffect.MaintenanceCost(0.33),
        ),
    ),
    LACTATION_GLANDS(
        "lactation glands",
        "Specialized glands produce nutrient-rich milk or an analogous secretion, allowing parents to nourish dependent young independently of the food those young can consume directly.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.08),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    SEQUENTIAL_HERMAPHRODITISM(
        "sequential hermaphroditism",
        "An individual changes reproductive role during its lifetime in response to age, size, or social conditions, improving mating opportunities at the cost of reorganizing reproductive tissues.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.15),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.SEXUAL_REPRODUCTION)),
    ),
    HERMAPHRODITISM(
        "hermaphroditism",
        "Each reproductive individual can perform both mating roles, improving the chance that an encounter produces offspring at the cost of maintaining both reproductive functions.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.3),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.SEXUAL_REPRODUCTION)),
    ),
    PARTHENOGENESIS(
        "parthenogenesis",
        "Unfertilized reproductive cells can develop into offspring, allowing reproduction without a mate while reducing the genetic variety produced by each reproductive event.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.14),
            TraitEffect.NicheCompetitionSensitivity(1.08),
            TraitEffect.MaintenanceCost(0.09),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.REPRODUCTION)),
    ),
    VIVIPARITY(
        "viviparity",
        "Offspring develop within a parent's body until they can survive outside it, protecting early development at a substantial metabolic cost.",
        listOf(
            TraitEffect.Defense(0.07),
            TraitEffect.MaintenanceCost(0.21),
        ),
        group = TraitGroup.OVOSPORE_TYPE,
        capabilities = setOf(
            TraitCapability.REPRODUCTION,
            TraitCapability.SEXUAL_REPRODUCTION,
        ),
    ),
    BROOD_POUCH(
        "brood pouch",
        "A protected external body chamber encloses highly underdeveloped live-born young while they complete early development.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.12),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.allOf(VIVIPARITY),
        ),
    ),
    INFREQUENT_REPRODUCTION(
        "infrequent reproduction",
        "Reproductive events occur only at long intervals, conserving routine reproductive investment while sharply limiting population growth.",
        listOf(
            TraitEffect.ReproductionMultiplier(0.35),
            TraitEffect.MaintenanceCost(-1.0),
            TraitEffect.Defense(0.01)
        ),
        group = TraitGroup.REPRODUCTION_FREQUENCY,
    ),
    FREQUENT_REPRODUCTION(
        "frequent reproduction",
        "Reproductive events occur at short intervals, increasing potential population growth at the cost of continual reproductive investment.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.35),
            TraitEffect.MaintenanceCost(0.45)
        ),
        group = TraitGroup.REPRODUCTION_FREQUENCY,
    ),
    TERRESTRIAL_OVOSPORE(
        "terrestrial ovospore",
        "A seed, spore, or egg develops outside its parent in a terrestrial environment and can be guarded or carried before hatching or germination.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.02),
            TraitEffect.MaintenanceCost(0.09),
        ),
        group = TraitGroup.OVOSPORE_TYPE,
        capabilities = setOf(
            TraitCapability.REPRODUCTION,
            TraitCapability.SEXUAL_REPRODUCTION,
            TraitCapability.OVOSPORE_REPRODUCTION,
            TraitCapability.OVOSPORE_BROODING,
        ),
    ),
    AQUATIC_OVOSPORE(
        "aquatic ovospore",
        "A seed, spore, or egg develops outside its parent while immersed in water and can be guarded or carried before hatching or germination.",
        listOf(
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.04),
        ),
        group = TraitGroup.OVOSPORE_TYPE,
        capabilities = setOf(
            TraitCapability.REPRODUCTION,
            TraitCapability.SEXUAL_REPRODUCTION,
            TraitCapability.OVOSPORE_REPRODUCTION,
            TraitCapability.OVOSPORE_BROODING,
        ),
    ),
    CLONAL_PROPAGATION(
        "clonal propagation",
        "New organisms separate through budding, fission, fragmentation, runners, or analogous growth without a distinct seed, spore, or egg.",
        listOf(TraitEffect.MaintenanceCost(0.09)),
        capabilities = setOf(TraitCapability.REPRODUCTION),
    ),
    AERIAL_OVOSPORE_DISPERSAL(
        "aerial ovospore dispersal",
        "Extremely light or aerodynamically shaped ovospores travel long distances through atmospheric currents before settling.",
        listOf(
            TraitEffect.RadiationRange(8),
            TraitEffect.MaintenanceCost(0.24),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.OVOSPORE_REPRODUCTION),
        ),
    ),
    RADIATIVE_FRUITS(
        "radiative fruits",
        "A temporary reproductive structure raises spore-producing tissue above the substrate, improving aerial dispersal at a substantial construction cost.",
        listOf(
            TraitEffect.RadiationRange(3),
            TraitEffect.ReproductionMultiplier(1.12),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.OVOSPORE_REPRODUCTION),
        ),
    ),
    OVOSPORE_NEST(
        "ovospore nest",
        "A purpose-built or prepared site shelters externally developing eggs, seeds, spores, or analogous propagules from weather and predators.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.15),
            TraitEffect.MaintenanceCost(0.21),
        ),
        capabilities = setOf(TraitCapability.OVOSPORE_BROOD_SITE),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.OVOSPORE_BROODING),
        ),
    ),
    BODY_CARRIED_OVOSPORES(
        "body-carried ovospores",
        "Externally developing eggs, seeds, spores, or analogous propagules remain attached to, enclosed by, or balanced on a parent's body.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.18),
            TraitEffect.MaintenanceCost(0.24),
        ),
        capabilities = setOf(TraitCapability.OVOSPORE_BROOD_SITE),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.OVOSPORE_BROODING),
        ),
    ),
    OVIPOSITOR(
        "ovipositor",
        "A specialized egg-laying structure places eggs or equivalent propagules into soil, plant tissue, or another protected substrate.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.02),
            TraitEffect.MaintenanceCost(0.09),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.OVOSPORE_REPRODUCTION),
        ),
    ),
    BROOD_PARASITISM(
        "brood parasitism",
        "Ovospores are placed with a particular host species, transferring incubation or juvenile care to that host while making reproduction dependent on finding it.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.10),
            TraitEffect.MaintenanceCost(-0.18),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.OVOSPORE_BROODING,
                    TraitCapability.BROOD_HOST_RELATIONSHIP,
                ),
            ),
        ),
    ),
    SEA_ICE_ROOKERY(
        "sea-ice rookery",
        "Breeding colonies occupy persistent sea ice close enough to land for repeated access to stable resting and nesting grounds.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.SEA_ICE, 0.5),
            TraitEffect.ObligateResidentHabitat(Habitat.SEA_ICE),
            TraitEffect.RequiresAdjacentLand,
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    COASTAL_BREEDING_SITE(
        "coastal breeding site",
        "Reproduction depends on returning from the water to stable shoreline ground, cliffs, or exposed structures where offspring can develop.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.COASTAL, 0.5),
            TraitEffect.ObligateResidentHabitat(Habitat.COASTAL),
            TraitEffect.RequiresAdjacentLand,
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    BURROW_BUILDER(
        "burrow builder",
        "A self-excavated burrow provides shelter, a buffered microclimate, and a dry chamber for nesting or resting.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 8.0, hotterC = 5.0),
            TraitEffect.WaterRequirement(-0.06),
            TraitEffect.MaximumWaterTolerance(
                optimalMaximumChange = -0.25,
                absoluteMaximumChange = -0.125,
            ),
            TraitEffect.Defense(0.05),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.BURROW_REFUGE,
        requirements = listOf(TraitRequirement.anyOf(TraitCapability.BURROW_EXCAVATION)),
    ),
    BURROW_BORROWER(
        "burrow borrower",
        "An occupant uses burrows excavated by another species for shelter, a buffered microclimate, and a dry chamber for nesting or resting.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 8.0, hotterC = 5.0),
            TraitEffect.WaterRequirement(-0.06),
            TraitEffect.MaximumWaterTolerance(
                optimalMaximumChange = -0.66,
                absoluteMaximumChange = -0.33,
            ),
            TraitEffect.Defense(0.05),
            TraitEffect.MaintenanceCost(0.03),
        ),
        group = TraitGroup.BURROW_REFUGE,
        relationships = listOf(
            RelationshipEffect.RequiresTarget(SpeciesSelector.HasTrait(BURROW_BUILDER)),
        ),
    ),
    BURROWING_EGGS(
        "burrowing eggs",
        "A seasonal lifecycle protected by placing resistant eggs or equivalent propagules below the surface.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.BURROWED_EGGS, 0.985),
            TraitEffect.ReproductionMultiplier(0.88),
            TraitEffect.MaintenanceCost(0.09),
        ),
        group = TraitGroup.DORMANCY_MODE,
        requirements = listOf(
            TraitRequirement.anyOf(
                TraitCapability.BURROW_EXCAVATION,
                BURROW_BUILDER,
                BURROW_BORROWER,
                OVIPOSITOR,
            ),
        ),
    ),
    FRUIT_BEARING(
        "fruit-bearing reproductive structures",
        "Energy-rich fruits surround or accompany propagules, recruiting mobile animals to disperse them.",
        listOf(
            TraitEffect.FruitProduction(0.0025),
            TraitEffect.MaintenanceCost(0.24),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.OVOSPORE_REPRODUCTION,
                    TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
                ),
            ),
        ),
    ),
    FLOWERS(
        "flowers",
        "Specialized reproductive structures expose pollen and ovules while advertising to mobile visitors or releasing pollen into the environment.",
        listOf(
            TraitEffect.Flowering,
            TraitEffect.ReproductionMultiplier(1.05),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.OVOSPORE_REPRODUCTION,
                    TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
                ),
            ),
        ),
    ),
    NECTARIES(
        "nectaries",
        "Secretory tissues offer an energy-rich liquid reward that attracts animals to reproductive structures.",
        listOf(
            TraitEffect.NectarProduction(0.025),
            TraitEffect.WaterRequirement(0.02),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(TraitRequirement.anyOf(ROOTED_BODY)),
    ),
    POLLEN_CARRYING_SURFACES(
        "pollen-carrying surfaces",
        "Branched hairs, scales, feathers, or other textured body surfaces retain pollen while an animal moves among flowers.",
        listOf(
            TraitEffect.PollinationEfficiency(0.70),
            TraitEffect.CaptureAbility(-0.02),
            TraitEffect.MaintenanceCost(0.09),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.LOCOMOTION)),
    ),

    // Basic limbs and locomotion
    WALKING_LIMBS(
        "walking limbs",
        "Jointed, load-bearing limbs support deliberate walking, running, or hopping across solid ground.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.WALKING, 0.35),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION,
        ),
        requirements = listOf(TraitRequirement.anyOf(LIMBED_BODY))
    ),
    BODY_UNDULATION(
        "body undulation",
        "Alternating muscular waves push an elongated body across the ground without weight-bearing limbs.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.LAND, 0.2),
            TraitEffect.MaintenanceCost(-0.12),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION,
        ),
    ),
    MUSCULAR_FOOT(
        "muscular foot",
        "A broad contractile foot produces slow, stable movement across soil, rock, plants, or other firm surfaces.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.WALKING, 0.15),
            TraitEffect.Defense(0.02),
            TraitEffect.CaptureAbility(-0.02),
            TraitEffect.MaintenanceCost(0.04),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION
        ),
        requirements = listOf(
            TraitRequirement.anyOf(MANTLED_BODY, PRIMITIVE_BODY)
        )
    ),
    CRAWLING_APPENDAGES(
        "crawling appendages",
        "Several small jointed appendages distribute weight and provide precise movement over irregular solid surfaces.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.WALKING, 0.25),
            TraitEffect.HabitatAccess(HabitatGroup.CLIMBING, 0.25),
            TraitEffect.HabitatAccess(Habitat.COASTAL, 0.15),
            TraitEffect.HabitatAccess(Habitat.SHALLOW_OCEAN, 0.15),
            TraitEffect.HabitatAccess(Habitat.FRESHWATER, 0.15),
            TraitEffect.CaptureAbility(0.03),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY),
            TraitRequirement.sizeClassAtMost(SizeClass.MEDIUM)
        )
    ),
    HYDRAULIC_APPENDAGES(
        "hydraulic appendages",
        "Fluid pressure extends, stiffens, or repositions flexible appendages used for walking, climbing, attachment, feeding, or manipulation.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.WALKING, 0.1),
            TraitEffect.HabitatAccess(HabitatGroup.CLIMBING, 0.1),
            TraitEffect.HabitatAccess(Habitat.COASTAL, 0.075),
            TraitEffect.HabitatAccess(Habitat.SHALLOW_OCEAN, 0.075),
            TraitEffect.HabitatAccess(Habitat.FRESHWATER, 0.075),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.sizeClassAtMost(SizeClass.SMALL)
        )
    ),
    LEAPING_LEGS(
        "powerful leaping legs",
        "Elongated spring-like limbs cross obstacles and produce abrupt escapes or attacks.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.CANOPY, 0.1),
            TraitEffect.Defense(0.05),
            TraitEffect.CaptureAbility(0.08),
            TraitEffect.MaintenanceCost(0.20),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY),
            TraitRequirement.sizeClassAtLeast(SizeClass.TINY)
        )
    ),
    DIGGING_LIMBS(
        "digging limbs",
        "Reinforced limbs rapidly excavate soil, tear apart nests, and expose concealed food.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.UNDERGROUND, 0.15),
            TraitEffect.StrategyAccess(EcoStrategy.COLONY_RAIDING, 0.15),
            TraitEffect.CaptureAbility(0.05),
            TraitEffect.MaintenanceCost(0.15),
        ),
        capabilities = setOf(TraitCapability.BURROW_EXCAVATION),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY)
        )
    ),
    AQUATIC_LIMBS(
        "aquatic limbs",
        "Broad propulsive flippers or fins that support controlled swimming in open water.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.AQUATIC, 0.5),
            TraitEffect.MaintenanceCost(0.12),
        ),
        capabilities = setOf(
            TraitCapability.AQUATIC_LOCOMOTION,
            TraitCapability.LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY, MANTLED_BODY)
        )
    ),
    AMPHIBIOUS_LIMBS(
        "amphibious limbs",
        "Load-bearing limbs and swimming surfaces that permit regular movement between land and shallow water.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.LAND, 0.3),
            TraitEffect.HabitatAccess(HabitatGroup.AQUATIC, 0.2),
            TraitEffect.MaintenanceCost(0.18),
        ),
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.AQUATIC_LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY)
        )
    ),
    WADING_LIMBS(
        "wading limbs",
        "Elongated load-bearing limbs keep the body above shallow water while allowing deliberate movement and prey capture over soft submerged ground.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.WALKING, 0.3),
            TraitEffect.HabitatAccess(Habitat.FRESHWATER, 0.25),
            TraitEffect.HabitatAccess(Habitat.COASTAL, 0.25),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY)
        )
    ),
    CLIMBING_LIMBS(
        "climbing limbs",
        "Grasping limbs, claws, pads, or a prehensile body that supports deliberate movement through a canopy.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.CLIMBING, 0.3),
            TraitEffect.CaptureAbility(-0.03),
            TraitEffect.MaintenanceCost(0.2),
        ),
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY)
        )
    ),
    PULSING_BELL(
        "pulsing bell",
        "A flexible bell-shaped body rhythmically displaces water, allowing controlled vertical and horizontal swimming without rigid fins.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.AQUATIC, 0.2),
            TraitEffect.PursuitSpeed(-0.2),
            TraitEffect.Defense(-0.1),
            TraitEffect.MaintenanceCost(0.03),
        ),
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.AQUATIC_LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(GELATINOUS_BODY)
        )
    ),
    DEEP_DIVING_PHYSIOLOGY(
        "deep-diving physiology",
        "Pressure-tolerant tissues, collapsible gas spaces, oxygen stores, or equivalent adaptations permit prolonged activity below the sunlit surface layer.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.DARK_WATER, 0.25),
            TraitEffect.DarkWaterAdaptation,
            // Deep water is usually cooler and less seasonally variable than
            // the surface represented by the tile's single temperature.
            TraitEffect.TemperatureTolerance(
                colderC = 8.0,
                hotterC = 8.0,
                optimalColderC = 4.0,
                optimalHotterC = 4.0,
            ),
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.MaintenanceCost(0.36),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.AQUATIC_LOCOMOTION),
            TraitRequirement.anyOf(
                TraitCapability.UNDERWATER_RESPIRATION,
                PROLONGED_BREATH_HOLDING,
            ),
        ),
    ),
    STICKY_FEET(
        "sticky feet",
        "Specialized toe pads use microscopic dry-adhesive structures, soft wet-contact surfaces, or analogous mechanisms to grip smooth and steep surfaces.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.CLIMBING, 0.2),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(CLIMBING_LIMBS, MUSCULAR_FOOT),
        ),
    ),
    COASTAL_CLINGING_FEET(
        "coastal clinging feet",
        "Gripping limbs or attachment pads that resist waves and currents in shallow coastal habitats.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.COASTAL, 0.25),
            TraitEffect.CaptureAbility(-0.05),
            TraitEffect.MaintenanceCost(0.09),
        ),
        capabilities = setOf(TraitCapability.LOCOMOTION),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY)
        )
    ),
    WINGS(
        "wings",
        "Paired aerodynamic surfaces generate lift and thrust through active wingbeats.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.FLYING, 0.4),
            TraitEffect.PursuitSpeed(0.5),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.72),
        ),
        group = TraitGroup.FLIGHT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY)
        )
    ),
    WEAK_WINGS(
        "weak wings",
        "Paired aerodynamic surfaces weakly generate lift and thrust through active wingbeats.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.FLYING, 0.2),
            TraitEffect.PursuitSpeed(0.15),
            TraitEffect.CaptureAbility(0.08),
            TraitEffect.MaintenanceCost(0.3),
        ),
        group = TraitGroup.FLIGHT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY)
        )
    ),
    FLIGHTLESS_WINGS(
        "flightless wings",
        "Feathered forelimbs retained for balance, display, insulation, swimming, or defense after the loss of powered flight.",
        listOf(
            TraitEffect.Defense(0.02),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.FLIGHT_STRUCTURE,
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.TERRESTRIAL_LOCOMOTION,
        ),
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY)
        )
    ),
    PELAGIC_SOARING(
        "pelagic soaring",
        "Long, efficient wings and wind-harvesting flight allow extended foraging far from land without exhausting energy reserves.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.AERIAL, 0.25),
            TraitEffect.PelagicAerialResidency,
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.MaintenanceCost(0.24),
        ),
        requirements = listOf(
            TraitRequirement.allOf(WINGS),
        ),
    ),
    GLIDING_MEMBRANE(
        "gliding membrane",
        "A broad skin membrane or flattened body turns height and forward speed into controlled unpowered flight.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.CANOPY, 0.15),
            TraitEffect.HabitatAffinity(HabitatGroup.WALKING, -0.05),
            TraitEffect.PursuitSpeed(0.08),
            TraitEffect.Defense(0.05),
            TraitEffect.WaterRequirement(0.02),
            TraitEffect.MaintenanceCost(0.21),
        ),
        group = TraitGroup.FLIGHT_STRUCTURE,
        requirements = listOf(
            TraitRequirement.anyOf(
                CLIMBING_LIMBS,
                TraitCapability.AQUATIC_LOCOMOTION,
            ),
        ),
    ),
    JET_PROPULSION(
        "jet propulsion",
        "Water is forcefully expelled from a muscular chamber for rapid acceleration and maneuvering.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.AQUATIC, 0.2),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.36),
        ),
        capabilities = setOf(
            TraitCapability.LOCOMOTION,
            TraitCapability.AQUATIC_LOCOMOTION,
        ),
    ),

    // Body structure,
    SLENDER_PHYSIQUE(
        "slender physique",
        "A narrow, lightly built torso reduces the mass that must be accelerated and exposes more surface area for heat loss.",
        listOf(
            TraitEffect.BodyMassMultiplier(0.5),
            TraitEffect.PursuitSpeed(0.08),
            TraitEffect.TemperatureTolerance(colderC = -1.0, hotterC = 2.0),
            TraitEffect.Defense(-0.04),
            TraitEffect.MaintenanceCost(0.105),
        ),
        group = TraitGroup.BODY_PHYSIQUE,
    ),
    BULKY_PHYSIQUE(
        "bulky physique",
        "A broad, heavily built torso provides thermal mass and resilience but is costly to accelerate and reproduce.",
        listOf(
            TraitEffect.BodyMassMultiplier(2.0),
            TraitEffect.PursuitSpeed(-0.05),
            TraitEffect.TemperatureTolerance(colderC = 2.0, hotterC = -1.0),
            TraitEffect.Defense(0.10),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.2),
        ),
        group = TraitGroup.BODY_PHYSIQUE,
    ),
    STREAMLINED_PHYSIQUE(
        "streamlined physique",
        "A smooth tapered profile reduces drag during sustained swimming or flying, helping hunters close distance and prey escape pursuit.",
        listOf(
            TraitEffect.PursuitSpeed(0.18),
            TraitEffect.MaintenanceCost(0.09),
            TraitEffect.StrategyAffinity(EcoStrategy.PURSUIT_PREDATION, 0.2),
            TraitEffect.HabitatAffinity(Habitat.AERIAL, 0.2),
            TraitEffect.HabitatAffinity(Habitat.OPEN_OCEAN, 0.2),
            TraitEffect.HabitatAffinity(HabitatGroup.WALKING, -0.2)
        ),
        requirements = listOf(
            TraitRequirement.anyOf(TraitCapability.AQUATIC_LOCOMOTION, WINGS),
            TraitRequirement.sizeClassAtLeast(SizeClass.SMALL)
        ),
        group = TraitGroup.BODY_PHYSIQUE,
    ),
    FLATTENED_PHYSIQUE(
        "flattened physique",
        "A flattened body is specialized for resting and feeding along the bottom of an aquatic habitat.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.COASTAL, 0.15),
            TraitEffect.HabitatAffinity(Habitat.SHALLOW_OCEAN, 0.3),
            TraitEffect.HabitatAffinity(Habitat.DARK_WATER, 0.3),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.PursuitSpeed(-0.12),
            TraitEffect.Defense(0.05),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.BODY_PHYSIQUE,
    ),
    SEGMENTED_PHYSIQUE(
        "segmented physique",
        "A body with a distinctive, narrow, or thin segmented structure can easily adapt its length and aid in digging.",
        listOf(
            TraitEffect.BodyMassMultiplier(0.5),
            TraitEffect.PursuitSpeed(-0.1),
            TraitEffect.HabitatAccess(Habitat.UNDERGROUND, 0.25),
            TraitEffect.MaintenanceCost(0.075),
        ),
        capabilities = setOf(TraitCapability.BURROW_EXCAVATION),
        group = TraitGroup.BODY_PHYSIQUE,
        requirements = listOf(
            TraitRequirement.anyOf(LIMBED_BODY, PRIMITIVE_BODY, GELATINOUS_BODY)
        )
    ),

    // Basic structural traits
    SURFACE_HOLDFAST(
        "surface holdfast",
        "Fine adhesive filaments or pads fasten the body to soil, stone, bark, or another exposed surface without penetrating it as true roots.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.LAND_SURFACE, 0.6),
            TraitEffect.HabitatAccess(Habitat.CANOPY, 0.2),
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.075),
        ),
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
    ),
    BUOYANCY_BLADDER(
        "buoyancy bladder",
        "A gas- or fluid-regulating chamber that controls position in the water column without continuous swimming.",
        listOf(
            TraitEffect.HabitatAccess(HabitatGroup.AQUATIC, 0.1),
            TraitEffect.Defense(-0.05),
            TraitEffect.MaintenanceCost(0.12),
        )
    ),
    SUBSTRATE_HOLDFAST(
        "aquatic holdfast",
        "A tough anchoring structure that secures a sessile body to rock or reef under waves and currents.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.COASTAL, 0.5),
            TraitEffect.HabitatAccess(Habitat.FRESHWATER, 0.5),
            TraitEffect.HabitatAccess(Habitat.SHALLOW_OCEAN, 0.5),
            TraitEffect.MaintenanceCost(0.12),
        ),
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
    ),
    FLEXIBLE_SPINE(
        "flexible spine",
        "A highly flexible axial skeleton lengthens the running stride and allows rapid twisting during pursuit, pouncing, and falls.",
        listOf(
            TraitEffect.PursuitSpeed(0.10),
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    EYES(
        "eyes",
        "Light-sensitive visual organs range from rudimentary direction-and-motion detectors to high-resolution systems that distinguish distant targets.",
        emptyList(),
        scale = TraitScale(
            (1..5).map { level ->
                TraitLevelDefinition(
                    description = listOf(
                        "Simple visual organs detect light direction and nearby movement.",
                        "Low-resolution visual organs distinguish broad shapes and movement at short range.",
                        "Developed visual organs recognize shapes, movement, brightness, and color across useful distances.",
                        "Acute visual organs resolve small or partially concealed targets at long range.",
                        "Exceptionally high-resolution visual organs distinguish distant targets and fine detail against cluttered backgrounds.",
                    )[level - 1],
                    effects = listOf(
                        TraitEffect.Sensing(0.02 * level),
                        TraitEffect.CaptureAbility(0.01 * level),
                        TraitEffect.MaintenanceCost(
                            listOf(0.01, 0.028, 0.060, 0.118, 0.224)[level - 1],
                        ),
                    ),
                )
            },
        ),
    ),
    ANTENNAE(
        "antennae",
        "Large, specialized, or highly specialized antennae provide a wide range of information about the environment and the animal's surroundings.",
        listOf(
            TraitEffect.Sensing(0.02),
            TraitEffect.MaintenanceCost(0.05)
        ),
    ),
    BEAK(
        "beak",
        "A hard projecting mouth structure cuts, crushes, probes, or tears food without teeth.",
        listOf(
            TraitEffect.CaptureAbility(0.02),
            TraitEffect.MaintenanceCost(0.03)
        ),
    ),
    JAW(
        "jaw",
        "A hinged mouth frame closes around food and provides an attachment point for muscles and specialized feeding structures.",
        listOf(
            TraitEffect.CaptureAbility(0.02),
            TraitEffect.MaintenanceCost(0.03)
        ),
    ),
    TEETH(
        "teeth",
        "Hard structures along the mouth grasp, cut, crush, or process food.",
        listOf(
            TraitEffect.CaptureAbility(0.02),
            TraitEffect.MaintenanceCost(0.03)
        ),
        requirements = listOf(TraitRequirement.allOf(JAW)),
    ),
    TOOTH_WHORLS(
        "tooth whorls",
        "Successive teeth form a curved or spiral cutting surface that grips and slices food as the mouth closes.",
        listOf(
            TraitEffect.CaptureAbility(0.16),
            TraitEffect.LargerPreySizeClasses(1),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.21),
        ),
        requirements = listOf(TraitRequirement.allOf(TEETH)),
    ),
    FANGS(
        "fangs",
        "Elongated pointed teeth pierce and retain prey or deliver a disabling bite.",
        listOf(
            TraitEffect.CaptureAbility(0.08),
            TraitEffect.MaintenanceCost(0.06)
        ),
    ),
    SERRATED_TEETH(
        "serrated teeth",
        "Saw-edged teeth slice flesh efficiently as the jaws close or the head moves through prey.",
        listOf(
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.12)
        ),
        requirements = listOf(TraitRequirement.allOf(TEETH)),
    ),
    TEETH_REGROWTH(
        "continuous tooth replacement",
        "New teeth repeatedly replace worn or lost teeth throughout life, preserving feeding performance at an energetic cost.",
        listOf(
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.ReproductionMultiplier(1.02),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(TraitRequirement.allOf(TEETH)),
    ),
    PROTRUSIBLE_JAW(
        "protrusible jaw",
        "The jaws project rapidly away from the skull, extending reach and drawing nearby prey into the mouth.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.18),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(TraitRequirement.allOf(JAW)),
    ),
    TENTACLES(
        "tentacles",
        "Flexible elongated appendages sense, grasp, or move food through water without rigid joints.",
        listOf(
            TraitEffect.CaptureAbility(0.05),
            TraitEffect.MaintenanceCost(0.18)
        ),
    ),
    NAILS(
        "nails",
        "Flattened keratin plates protect the tips of grasping digits while preserving fine touch and manipulation, rather than serving as hooked weapons.",
        listOf(
            TraitEffect.CaptureAbility(-0.03),
            TraitEffect.MaintenanceCost(0.06)
        ),
    ),
    CLAWS(
        "claws",
        "Curved hardened tips on the digits provide traction, defense, and a grip on food or prey.",
        listOf(
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.Defense(0.03),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.LOCOMOTION)),
    ),
    PINCERS(
        "pincers",
        "Opposed grasping appendages hold, manipulate, and cut food or other objects.",
        listOf(
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.Defense(0.02),
            TraitEffect.MaintenanceCost(0.09),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.LOCOMOTION)),
    ),
    CRUSHING_PINCERS(
        "crushing pincers",
        "Opposed hardened pincers crack shells, cut plant tissue, and restrain struggling prey.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.GRAZING, 0.10),
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.12),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.21),
        ),
        requirements = listOf(TraitRequirement.allOf(PINCERS))
    ),
    ANTLERS(
        "antlers",
        "Branching seasonal head weapons are regrown for display, mate competition, and defense.",
        listOf(
            TraitEffect.Defense(0.11),
            TraitEffect.ReproductionMultiplier(1.06),
            TraitEffect.MaintenanceCost(0.33),
        ),
    ),
    HORNS(
        "horns",
        "One or more large permanent keratinous or bony head weapons deter predators and resolve contests by impact or leverage.",
        listOf(
            TraitEffect.CaptureAbility(0.03),
            TraitEffect.Defense(0.15),
            TraitEffect.ReproductionMultiplier(0.98),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    SUCTION_CUPS(
        "gripping suction cups",
        "Pressure-sealing discs attach to rock, prey, and other bodies under water.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.COASTAL, 0.25),
            TraitEffect.HabitatAccess(Habitat.SHALLOW_OCEAN, 0.15),
            TraitEffect.Defense(0.08),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    TAIL(
        "tail",
        "A posterior extension balances the body, signals to others, steers movement, or distracts attackers.",
        listOf(TraitEffect.MaintenanceCost(0.03), TraitEffect.ReproductionMultiplier(1.01)),
    ),
    PREHENSILE_TAIL(
        "prehensile tail",
        "A muscular, flexible tail grasps branches, reef structures, vegetation, or other supports.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.CANOPY, 0.2),
            TraitEffect.ReefUse(0.22),
            TraitEffect.MaintenanceCost(0.2),
        ),
        requirements = listOf(TraitRequirement.allOf(TAIL)),
    ),
    REDUCED_LIMBS(
        "reduced limbs",
        "One or more ancestral limb pairs are shortened or lost, lowering tissue costs while reducing limb-powered speed and dexterity.",
        listOf(
            TraitEffect.PursuitSpeed(-0.08),
            TraitEffect.CaptureAbility(-0.02),
            TraitEffect.MaintenanceCost(-0.12),
        ),
    ),
    LIMB_REGROWTH(
        "limb regrowth",
        "Specialized wound responses rebuild a lost limb or equivalent motile appendage, restoring function over time at a substantial energetic cost.",
        listOf(
            TraitEffect.Defense(0.08),
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(0.25),
        ),
        requirements = listOf(TraitRequirement.anyOf(LIMBED_BODY, TENTACLES)),
    ),
    BODY_REGENERATION(
        "body regeneration",
        "Distributed regenerative tissues rebuild extensive damaged regions, while viable fragments can sometimes restore a complete body or develop into new individuals at a substantial energetic cost.",
        listOf(
            TraitEffect.Defense(0.1),
            TraitEffect.ReproductionMultiplier(1.1),
            TraitEffect.MaintenanceCost(0.3)
        )
    ),
    DEEP_ROOT_SYSTEM(
        "deep root system",
        "A long or extensively branching anchoring network that reaches water retained below the surface.",
        listOf(
            TraitEffect.WaterRequirement(-0.18),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.SUBSTRATE_ANCHORING),
        ),
    ),
    SUCCULENT_STEM(
        "succulent stem",
        "A thick photosynthetic or supporting body that stores water through long dry intervals.",
        listOf(
            TraitEffect.WaterRequirement(-0.28),
            TraitEffect.ReproductionMultiplier(0.88),
            TraitEffect.MaintenanceCost(0.21),
        ),
        capabilities = setOf(TraitCapability.WATER_STORAGE),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.SUBSTRATE_ANCHORING),
        ),
    ),
    WAXY_CUTICLE(
        "waxy cuticle",
        "A reflective, water-resistant outer surface limits evaporation and shields living tissue from intense heat.",
        listOf(
            TraitEffect.TemperatureTolerance(hotterC = 10.0),
            TraitEffect.WaterRequirement(-0.08),
            TraitEffect.CanopyLightEfficiency(-0.04),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),

    // Basic body coverings
    FUR(
        "fur",
        "A coat of hairlike filaments protects the skin and provides light insulation while still releasing excess heat.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 1.0, hotterC = -1.0),
            TraitEffect.MaintenanceCost(0.09),
        ),
        group = TraitGroup.DOMINANT_BODY_COVERING,
    ),
    SCALES(
        "scales",
        "Overlapping or adjoining plates cover and reinforce the body surface.",
        listOf(TraitEffect.Defense(0.02), TraitEffect.MaintenanceCost(0.03)),
        group = TraitGroup.DOMINANT_BODY_COVERING,
    ),
    FEATHERS(
        "feathers",
        "Branching keratinous filaments form a light protective body covering that can support specialized insulation, display, waterproofing, or flight.",
        listOf(
            TraitEffect.MaintenanceCost(0.12),
            TraitEffect.TemperatureTolerance(colderC = 2.0, hotterC = 1.0),
        ),
        group = TraitGroup.DOMINANT_BODY_COVERING,
    ),
    DENSE_UNDERCOAT(
        "dense undercoat",
        "A thick layer of fine hair beneath the outer fur traps still air close to the body.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 8.0, hotterC = -3.0),
            TraitEffect.MetabolicDemandMultiplier(0.9),
            TraitEffect.MaintenanceCost(0.12),
            TraitEffect.BodyMassMultiplier(1.03)
        ),
        requirements = listOf(TraitRequirement.allOf(FUR)),
    ),
    WOOLLY_UNDERCOAT(
        "woolly undercoat",
        "A second layer of fine, densely packed hairs traps additional insulating air beneath the outer coat.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 5.0, hotterC = -2.0),
            TraitEffect.MaintenanceCost(0.18),
            TraitEffect.BodyMassMultiplier(1.02)
        ),
        requirements = listOf(
            TraitRequirement.allOf(FUR),
        ),
    ),
    SEASONAL_WINTER_COAT(
        "seasonal winter coat",
        "Insulation grown in response to the low-insolation portion of the year and shed as light returns.",
        listOf(
            TraitEffect.SeasonalColdTolerance(maximumBonusC = 12.0, triggerInsolation = 0.58),
            TraitEffect.TemperatureTolerance(hotterC = -0.5),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(
            TraitRequirement.allOf(FUR),
        ),
    ),
    WATER_RETENTIVE_SCALES(
        "water-retentive scales",
        "Overlapping low-permeability plates protect the body surface and slow water loss without requiring a continuously moist outer layer.",
        listOf(
            TraitEffect.WaterRequirement(-0.06),
            TraitEffect.TemperatureTolerance(hotterC = 1.0),
            TraitEffect.Defense(0.08),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(TraitRequirement.allOf(SCALES)),
    ),
    BONY_SCALES(
        "bony scales",
        "Mineralized scales form a durable protective layer that resists bites and abrasion but adds weight and growth costs.",
        listOf(
            TraitEffect.Defense(0.18),
            TraitEffect.PursuitSpeed(-0.04),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.18),
            TraitEffect.BodyMassMultiplier(1.05)
        ),
        group = TraitGroup.SCALE_TYPE,
        requirements = listOf(TraitRequirement.allOf(SCALES)),
    ),
    PLACOID_SCALES(
        "placoid scales",
        "Small tooth-like scales protect the skin and channel water along the body, reducing drag during swimming.",
        listOf(
            TraitEffect.Defense(0.10),
            TraitEffect.PursuitSpeed(0.06),
            TraitEffect.MaintenanceCost(0.18),
            TraitEffect.BodyMassMultiplier(1.02)
        ),
        group = TraitGroup.SCALE_TYPE,
        requirements = listOf(TraitRequirement.allOf(SCALES)),
    ),
    INSULATING_PLUMAGE(
        "insulating plumage",
        "Dense overlapping feathers trap air around the body while remaining lighter than an equally thick fur coat.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 10.0, hotterC = -3.0),
            TraitEffect.WaterRequirement(-0.03),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(TraitRequirement.allOf(FEATHERS)),
    ),
    WATERPROOF_PLUMAGE(
        "waterproof plumage",
        "Overlapping oiled feathers retain insulating air and shed water during swimming and rain.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.COASTAL, 0.15),
            TraitEffect.HabitatAffinity(Habitat.FRESHWATER, 0.15),
            TraitEffect.TemperatureTolerance(colderC = 0.5),
            TraitEffect.MaintenanceCost(0.21),
        ),
        requirements = listOf(TraitRequirement.allOf(FEATHERS)),
    ),
    BARE_HEAT_DISSIPATING_SKIN(
        "bare heat-dissipating skin",
        "Exposed, well-supplied regions of skin shed heat efficiently but sacrifice insulation and physical protection.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -5.0, hotterC = 6.0),
            TraitEffect.Defense(-0.03),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),

    // altitude adaptations
    ENLARGED_CARDIOPULMONARY_SYSTEM(
        "enlarged heart and lungs",
        "An unusually large heart, lungs, and pulmonary exchange surface sustain oxygen delivery in thin air.",
        listOf(
            TraitEffect.ElevationToleranceShift(2_500.0),
            TraitEffect.MaintenanceCost(0.27),
        ),
        requirements = listOf(TraitRequirement.anyOf(TRACHEA))
    ),
    HIGH_AFFINITY_BLOOD(
        "high-affinity blood",
        "Circulating respiratory pigments bind oxygen effectively at the low partial pressures found at high elevation.",
        listOf(
            TraitEffect.ElevationToleranceShift(2_500.0),
            TraitEffect.CaptureAbility(-0.03),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    HYPOXIA_RESPONSIVE_METABOLISM(
        "hypoxia-responsive metabolism",
        "Oxygen-sensing pathways adjust circulation and cellular energy use during chronic exposure to thin air.",
        listOf(
            TraitEffect.ElevationToleranceShift(3_500.0),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),

    // Sense traits
    MOTION_TRACKING_SENSES(
        "motion-tracking senses",
        "Vision, hearing, scent, vibration detection, or equivalent senses allow a hunter to follow moving prey through a sustained chase.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.PURSUIT_PREDATION, 0.2),
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.1),
            TraitEffect.Sensing(0.06),
            TraitEffect.CaptureAbility(0.06),
            TraitEffect.MaintenanceCost(0.25),
        ),
    ),
    KEEN_SCENT_SENSE(
        "keen sense of smell",
        "A large, sensitive chemical-sensing system follows faint trails, detects concealed threats, and helps individuals locate distant mates.",
        listOf(
            TraitEffect.Sensing(0.08),
            TraitEffect.ReproductionMultiplier(1.05),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.SCENT_ACUITY,
    ),
    POOR_SCENT_SENSE(
        "poor sense of smell",
        "Reduced chemical sensitivity makes it harder to follow faint trails, detect concealed threats, and locate distant mates.",
        listOf(
            TraitEffect.Sensing(-0.12),
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(-0.15),
        ),
        group = TraitGroup.SCENT_ACUITY,
    ),
    KEEN_HEARING(
        "keen hearing",
        "Sensitive auditory organs and neural processing locate faint or distant sounds, including concealed threats, and distinguish them from background noise.",
        listOf(
            TraitEffect.Sensing(0.08),
            TraitEffect.CaptureAbility(0.03),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.HEARING_ACUITY,
    ),
    POOR_HEARING(
        "poor hearing",
        "Reduced auditory sensitivity makes it harder to detect faint sounds and to distinguish useful signals from background noise.",
        listOf(
            TraitEffect.Sensing(-0.08),
            TraitEffect.CaptureAbility(-0.03),
            TraitEffect.MaintenanceCost(-0.12),
        ),
        group = TraitGroup.HEARING_ACUITY,
    ),
    ECHOLOCATION(
        "echolocation",
        "The organism emits sound and reconstructs nearby surfaces and moving prey from returning echoes.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.DARK_WATER, 0.15),
            TraitEffect.CaptureAbility(0.20),
            TraitEffect.MaintenanceCost(0.27),
        ),
        requirements = listOf(
            TraitRequirement.allOf(KEEN_HEARING),
        ),
    ),
    ELECTRORECEPTION(
        "electroreception",
        "Sensitive organs detect the weak electrical fields produced by hidden or moving organisms in water.",
        listOf(
            TraitEffect.HabitatAffinity(HabitatGroup.AQUATIC, 0.1),
            TraitEffect.HabitatAffinity(HabitatGroup.DARK, 0.2),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.MaintenanceCost(0.21),
            TraitEffect.Sensing(0.1)
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.AQUATIC_LOCOMOTION)
        )
    ),

    // Sonic traits
    HOWLING_CALL(
        "howling call",
        "Long-range group calls coordinate dispersed pack members and advertise an occupied range.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.PURSUIT_PREDATION, 0.05),
            TraitEffect.Defense(0.03),
            TraitEffect.MaintenanceCost(0.15),
        ),
        acousticSignal = AcousticSignal.HOWL,
    ),
    SONG_CALL(
        "song call",
        "Long, patterned vocalizations carry identity, contact, and reproductive information through open water.",
        emptyList(),
        isCosmetic = true,
        acousticSignal = AcousticSignal.SONG,
    ),
    RATTLING_WARNING(
        "rattling warning",
        "A specialized vibrating structure produces a conspicuous warning that discourages accidental encounters with large animals.",
        listOf(
            TraitEffect.Defense(0.10),
            TraitEffect.ReproductionMultiplier(0.98),
            TraitEffect.MaintenanceCost(0.09),
        ),
        acousticSignal = AcousticSignal.RATTLE,
    ),
    ROARING_CALL("roaring call", "A resonant roar advertises the caller across its home range.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.ROAR),
    TRUMPETING_CALL("trumpeting call", "A loud trumpet communicates alarm, excitement, and identity between social group members.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.TRUMPET),
    BELLOWING_CALL("bellowing call", "A deep bellow carries social, territorial, or reproductive information between large animals.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.BELLOW),
    BLEATING_CALL("bleating call", "A nasal bleat maintains contact between companions, parents, and young.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.BLEAT),
    GRUNTING_CALL("grunting call", "Short grunts communicate contact, agitation, and feeding context at close range.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.GRUNT),
    HONKING_CALL("honking call", "A loud honk maintains contact within a flock and during coordinated flight.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.HONK),
    BUGLING_CALL("bugling call", "A far-carrying bugle advertises a breeding individual and challenges rivals.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.BUGLE),
    CROAKING_CALL("croaking call", "Repeated croaks advertise identity and reproductive readiness near breeding sites.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.CROAK),
    BRAYING_CALL("braying call", "A harsh, carrying bray maintains contact and expresses alarm or social arousal.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.BRAY),
    HOOTING_CALL("hooting call", "Resonant hoots carry identity and location through forests or darkness.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.HOOT),
    BARKING_CALL("barking call", "Short abrupt calls communicate alarm, contact, or territorial intent.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.BARK),
    GROWLING_CALL("growling call", "A low rough vocal warning signals agitation and readiness for close-range defense.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.GROWL),
    SCREECHING_CALL("screeching call", "A loud harsh call carries alarm, contact, or territorial information over long distances.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.SCREECH),
    QUACKING_CALL("quacking call", "Repeated nasal calls maintain contact among water-foraging companions and their young.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.QUACK),
    CROWING_CALL("crowing call", "A loud repeated crow advertises an individual's presence and breeding territory.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.CROW),
    TRILLING_CALL("trilling call", "Rapidly modulated calls transmit identity and contact information as a sustained trill.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.TRILL),
    WHISTLING_CALL("whistling call", "A clear tonal whistle communicates contact, alarm, or location across an open or cluttered habitat.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.WHISTLE),
    CLICKING_CALL("clicking call", "Short percussive clicks communicate contact, alarm, or location, especially where tonal calls carry poorly.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.CLICK),
    BUZZING_CALL("buzzing call", "A sustained buzzing signal communicates presence, contact, or reproductive readiness at close range.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.BUZZ),
    CHIRPING_CALL(
        "chirping call",
        "Short high-pitched calls maintain contact between companions, parents, and young.",
        emptyList(),
        isCosmetic = true,
        acousticSignal = AcousticSignal.CHIRP,
    ),
    MEOWING_CALL("meowing call", "A modulated tonal call communicates contact, solicitation, agitation, or reproductive intent.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.MEOW),
    PURRING_CALL("purring call", "A quiet rhythmic vibration communicates close-range social state and contentment.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.PURR),
    HISSING_WARNING("hissing warning", "Forcefully expelled air produces a conspicuous warning before close-range defense.", emptyList(), isCosmetic = true, acousticSignal = AcousticSignal.HISS),
    IMITATIVE_VOCALIZATION("imitative vocalization", "Flexible vocal control reproduces learned calls and unfamiliar environmental sounds.", emptyList(), isCosmetic = true),
    BOOMING_CALL(
        "booming call",
        "A resonant low-frequency display call carries between widely separated potential mates.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.04),
            TraitEffect.MaintenanceCost(0.12),
        ),
        acousticSignal = AcousticSignal.BOOM,
    ),
    DRUMMING_DISPLAY(
        "drumming display",
        "Repeated impacts against a resonant surface create a long-range territorial and courtship signal.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.02),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    INFRARED_SENSING(
        "infrared sensing",
        "Heat-sensitive organs resolve nearby warm surfaces or organisms independently of visible illumination.",
        listOf(
            TraitEffect.HabitatAffinity(HabitatGroup.DARK, 0.08),
            TraitEffect.Sensing(0.12),
            TraitEffect.CaptureAbility(0.08),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    WHOOPING_CALL(
        "whooping call",
        "Long-range whoops recruit and coordinate members of a dispersed social hunting group.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.PURSUIT_PREDATION, 0.03),
            TraitEffect.Defense(0.02),
            TraitEffect.MaintenanceCost(0.12),
        ),
        acousticSignal = AcousticSignal.WHOOP,
    ),
    COMPLEX_VOCALIZATIONS(
        "complex vocalizations",
        "A diverse repertoire of individually distinctive calls coordinates a complex mobile social group.",
        listOf(
            TraitEffect.Defense(0.03),
            TraitEffect.ReproductionMultiplier(1.02),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(TraitRequirement.HasAcousticSignal),
    ),
    SOUND_LURES(
        "sound lures",
        "Familiar calls are imitated or repurposed to draw acoustically responsive prey into striking distance.",
        listOf(
            TraitEffect.SoundLureCaptureBonus(0.22),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(TraitRequirement.HasAcousticSignal),
    ),

    // Defensive traits
    SILENT_MOVEMENT(
        "silent movement",
        "Silent movement allows creatures to avoid being noticed, and also improves the ability to ambush and capture prey.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.5),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.Defense(0.04),
            TraitEffect.MaintenanceCost(0.24),
        ),
        requirements = listOf(TraitRequirement.SizeClassAtLeast(SizeClass.SMALL))
    ),
    TERRESTRIAL_CAMOUFLAGE(
        "terrestrial camouflage",
        "Body colors and markings break up the organism's outline against soil, vegetation, bark, or other terrestrial backgrounds.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.36),
            TraitEffect.Camouflage(Habitat.LAND_SURFACE, 0.20),
            TraitEffect.Camouflage(Habitat.CANOPY, 0.18),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    AQUATIC_CAMOUFLAGE(
        "aquatic camouflage",
        "Body colors and markings obscure the organism against open water, reefs, vegetation, or the dim seafloor.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.36),
            TraitEffect.Camouflage(Habitat.COASTAL, 0.20),
            TraitEffect.Camouflage(Habitat.SHALLOW_OCEAN, 0.18),
            TraitEffect.Camouflage(Habitat.OPEN_OCEAN, 0.18),
            TraitEffect.Camouflage(Habitat.DARK_WATER, 0.16),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    APOSEMATIC_COLORATION(
        "aposematic coloration",
        "Conspicuous colors advertise a dangerous or distasteful organism—or mimic another local organism carrying the same warning colors.",
        listOf(
            TraitEffect.AposematicColoration,
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    AUTOTOMY(
        "autotomy",
        "The organism deliberately sheds a trapped or attacked appendage, escaping at the cost of lost tissue and function.",
        listOf(
            TraitEffect.Defense(0.12),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.LOCOMOTION))
    ),
    INFLATABLE_BODY(
        "inflatable body",
        "The body rapidly expands with water or air, making the organism harder to swallow while temporarily limiting movement.",
        listOf(
            TraitEffect.Defense(0.24),
            TraitEffect.PursuitSpeed(-0.10),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    ARMORED_HIDE(
        "armored hide",
        "Thick skin reinforced with plates or embedded bone resists bites, claws, and impacts.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -5.0, hotterC = 6.0),
            TraitEffect.Defense(0.34),
            TraitEffect.CaptureAbility(-0.07),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.24),
            TraitEffect.BodyMassMultiplier(1.2)
        ),
    ),
    REINFORCED_HIDE(
        "reinforced hide",
        "Dense, unusually tough skin beneath a fur coat resists tearing, punctures, and twisting bites without forming rigid armor.",
        listOf(
            TraitEffect.Defense(0.18),
            TraitEffect.CaptureAbility(-0.02),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.15),
            TraitEffect.BodyMassMultiplier(1.1)
        ),
        requirements = listOf(
            TraitRequirement.allOf(FUR),
        ),
    ),
    PROTECTIVE_SHELL(
        "protective shell",
        "A rigid external shell encloses vulnerable tissues and can withstand crushing or abrasion.",
        listOf(
            TraitEffect.Defense(0.46),
            TraitEffect.CaptureAbility(-0.14),
            TraitEffect.ReproductionMultiplier(0.86),
            TraitEffect.MaintenanceCost(0.3),
        ),
    ),
    SPINES(
        "defensive quills",
        "Long rigid hairs or spines make biting and grappling dangerous.",
        listOf(
            TraitEffect.Defense(0.32),
            TraitEffect.CaptureAbility(-0.08),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    TOXIC_SKIN(
        "toxic skin",
        "Skin glands or accumulated compounds make the organism poisonous or intensely distasteful.",
        listOf(
            TraitEffect.Defense(0.28),
            TraitEffect.ReproductionMultiplier(0.93),
            TraitEffect.MaintenanceCost(0.21),
        ),
    ),
    SLIMY_SKIN(
        "slimy skin",
        "Skin glands or accumulated compounds make the organism moderately distasteful or difficult to grasp.",
        listOf(
            TraitEffect.Defense(0.06),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    INK_CLOUD(
        "defensive ink cloud",
        "A released cloud obscures vision and confuses chemical senses during escape.",
        listOf(
            TraitEffect.HabitatAffinity(HabitatGroup.AQUATIC, 0.20),
            TraitEffect.HabitatAffinity(HabitatGroup.DARK, -0.20),
            TraitEffect.HabitatAffinity(HabitatGroup.LAND, -0.20),
            TraitEffect.Defense(0.22),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    STINK_DEFENSE(
        "stink defense",
        "Specialized glands release a persistent, repellent odor that discourages attackers but costs resources to produce and can reveal the defender's presence.",
        listOf(
            TraitEffect.Defense(0.26),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),

    // Offensive traits
    MEAT_EATING_MOUTHPARTS(
        "meat-eating mouthparts",
        "Biting, piercing, tearing, or engulfing feeding structures process animal tissue from captured prey or carrion.",
        listOf(
            // Feeding anatomy permits basic hunting; specialized traits supply most of its affinity.
            TraitEffect.StrategyAccess(EcoStrategy.AMBUSH_PREDATION, 0.10),
            TraitEffect.StrategyAccess(EcoStrategy.PURSUIT_PREDATION, 0.10),
            TraitEffect.StrategyAccess(EcoStrategy.SCAVENGING, 0.10),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    BALEEN(
        "baleen",
        "Dense flexible plates that strain suspended organisms from water passing through the mouth.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.FILTER_FEEDING, 0.88),
            TraitEffect.CaptureAbility(0.06),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.FILTERING_APPARATUS,
        requirements = listOf(TraitRequirement.allOf(JAW))
    ),
    SIEVING_TEETH(
        "sieving teeth",
        "Interlocking teeth or baleen form a sieve that retains minuscule swimming prey as water is expelled from the mouth.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.FILTER_FEEDING, 0.82),
            TraitEffect.CaptureAbility(0.1),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.FILTERING_APPARATUS,
        requirements = listOf(TraitRequirement.allOf(TEETH))
    ),
    GILL_RAKERS(
        "gill rakers",
        "Stiff projections along the gill arches retain small suspended food while water passes over the gills.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.FILTER_FEEDING, 0.82),
            TraitEffect.MaintenanceCost(0.18),
        ),
        group = TraitGroup.FILTERING_APPARATUS,
        requirements = listOf(TraitRequirement.allOf(GILLS)),
    ),
    SUSPENSION_FEEDING_TENTACLES(
        "suspension-feeding tentacles",
        "Many slender tentacles, pinnules, or comparable appendages intercept minuscule organisms carried past the body by water.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.FILTER_FEEDING, 0.82),
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.FILTERING_APPARATUS,
    ),
    SUCTION_FEEDING(
        "suction-feeding mouth",
        "A muscular tongue, sealed lips, and a vaulted mouth expose and suction soft-bodied prey.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.25),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(TraitRequirement.allOf(JAW)),
    ),
    AMBUSH_MUSCULATURE(
        "burst ambush musculature",
        "Muscles specialized for short, explosive attacks launched from concealment or stillness.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.58),
            TraitEffect.CaptureAbility(0.20),
            TraitEffect.MaintenanceCost(0.39),
        ),
    ),
    SWIFT_LIMBS(
        "swift limbs",
        "Long, powerful, or rapidly cycling limbs increase speed, helping hunters close distance and prey escape pursuit.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.PURSUIT_PREDATION, 0.2),
            TraitEffect.PursuitSpeed(0.18),
            TraitEffect.WaterRequirement(0.03),
            TraitEffect.MaintenanceCost(0.24),
        ),
        requirements = listOf(
            TraitRequirement.allOf(
                TraitCapability.LOCOMOTION,
                LIMBED_BODY
            )
        ),
    ),
    LONG_TUSKS(
        "long tusks",
        "Elongated exposed teeth serve as weapons, display structures, digging tools, or levers during contests and movement.",
        listOf(
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.Defense(0.14),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(TraitRequirement.allOf(TEETH)),
    ),
    STRONG_JAWS(
        "strong jaws",
        "Deep jaw muscles and reinforced skull structures generate unusually forceful bites for seizing, crushing, or dismembering food.",
        listOf(
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.Defense(0.04),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(TraitRequirement.allOf(JAW)),
    ),
    RETRACTABLE_CLAWS(
        "retractable claws",
        "Claws are protected while traveling and extended for traction, climbing, grappling, and close-range prey capture.",
        listOf(
            TraitEffect.CaptureAbility(0.11),
            TraitEffect.Defense(0.03),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.allOf(CLAWS),
            TraitRequirement.anyOf(
                TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
                CLIMBING_LIMBS,
            ),
        ),
    ),
    HIGH_POUNCING(
        "high pouncing",
        "A high arcing leap uses sound and precise impact to pin prey hidden in shallow burrows, snow, or dense ground cover.",
        listOf(
            TraitEffect.BurrowerCaptureBonus(0.24),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE),
        ),
    ),
    SPEAR_BILL(
        "spear bill",
        "A long pointed bill rapidly stabs or seizes small aquatic prey",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.FRESHWATER, 0.2),
            TraitEffect.HabitatAffinity(Habitat.COASTAL, 0.15),
            TraitEffect.HabitatAffinity(Habitat.OPEN_OCEAN, 0.15),
            TraitEffect.HabitatAffinity(Habitat.LAND_SURFACE, -0.2),
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.34),
            TraitEffect.CaptureAbility(0.15),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    SCOOP_MOUTH(
        "expandable scoop pouch",
        "A long bill and distensible throat pouch rapidly engulf small fish and drain excess water before swallowing.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.COASTAL, 0.2),
            TraitEffect.HabitatAffinity(Habitat.FRESHWATER, 0.15),
            TraitEffect.HabitatAffinity(Habitat.OPEN_OCEAN, 0.15),
            TraitEffect.HabitatAffinity(Habitat.LAND_SURFACE, -0.2),
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.14),
            TraitEffect.CaptureAbility(0.14),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    WEB_SILK(
        "prey-catching silk web",
        "Strong adhesive fibers are arranged into traps that intercept moving prey.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.58),
            TraitEffect.HabitatAffinity(Habitat.CANOPY, 0.2),
            TraitEffect.CaptureAbility(0.24),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.21),
        ),
    ),
    VENOM_DELIVERY(
        "venom delivery",
        "Fangs, stingers, spines, or saliva introduce toxins that rapidly disable prey.",
        listOf(
            TraitEffect.CaptureAbility(0.24),
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(0.21),
            TraitEffect.Defense(0.45)
        ),
    ),
    VENOM_RESISTANCE(
        "venom resistance",
        "Altered molecular targets, detoxification pathways, or protective blood chemistry reduce the disabling effects of venom delivered by predators.",
        listOf(
            TraitEffect.ReproductionMultiplier(0.98),
            TraitEffect.MaintenanceCost(0.15),
        ),
        interactionEffects = listOf(
            ConditionalInteractionEffect(
                opponentCondition = TraitCondition.HasTrait(VENOM_DELIVERY),
                effects = listOf(
                    InteractionEffect.CaptureBonusMultiplier(
                        subject = InteractionEffectSubject.OPPONENT,
                        multiplier = 0.5,
                    ),
                ),
            ),
        ),
    ),
    SAW_STRUCTURES(
        "saw structures",
        "An elongated edge bearing repeated hard teeth or blades wounds food or attackers through sweeping, scraping, or vibrating motions.",
        listOf(
            TraitEffect.CaptureAbility(0.16),
            TraitEffect.Defense(0.08),
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(0.21),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.LOCOMOTION)),
    ),
    PROJECTILE_CHEMICAL_SPRAY(
        "projectile chemical spray",
        "A pressurized or forcefully expelled chemical secretion is aimed at threats or prey from beyond immediate contact range.",
        listOf(
            TraitEffect.Defense(0.20),
            TraitEffect.CaptureAbility(0.05),
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(0.24),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(STINK_DEFENSE, TOXIC_SKIN, VENOM_DELIVERY),
        ),
    ),
    CONSTRICTING_BODY(
        "constricting body",
        "A long muscular body coils around captured prey and prevents breathing or circulation.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.24),
            TraitEffect.CaptureAbility(0.20),
            TraitEffect.MaintenanceCost(0.24),
        ),
    ),
    HOOKED_TALONS(
        "hooked talons",
        "Curved claws seize, carry, and kill struggling prey.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.12),
            TraitEffect.StrategyAffinity(EcoStrategy.PURSUIT_PREDATION, 0.12),
            TraitEffect.HabitatAffinity(HabitatGroup.AERIAL, 0.1),
            TraitEffect.HabitatAffinity(HabitatGroup.WALKING, -0.1),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(CLAWS, LIMBED_BODY)),
        ),
    ),
    STINGER(
        "stinger",
        "A sharp specialized appendage punctures another organism during attack or defense.",
        listOf(
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.Defense(0.06),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(TraitRequirement.allOf(TraitCapability.LOCOMOTION)),
    ),
    ELECTRIC_ORGAN(
        "electric organ",
        "Stacks of specialized cells release coordinated electrical discharges that can stun prey, discourage predators, or communicate through opaque water.",
        listOf(
            TraitEffect.CaptureAbility(0.20),
            TraitEffect.Defense(0.20),
            TraitEffect.MaintenanceCost(0.36),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.AQUATIC_LOCOMOTION)
        )
    ),
    GRASPING_TENTACLES(
        "grasping tentacles",
        "Flexible muscular appendages explore crevices and restrain several prey items at once.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.16),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.HabitatAffinity(Habitat.COASTAL, 0.15),
            TraitEffect.HabitatAffinity(Habitat.SHALLOW_OCEAN, 0.15),
            TraitEffect.MaintenanceCost(0.24),
        ),
        requirements = listOf(TraitRequirement.allOf(TENTACLES)),
    ),
    BIOLUMINESCENCE(
        "bioluminescence",
        "Chemical light produced by living tissues supports signaling, illumination, concealment, warning displays, or distraction in dim surroundings.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.DARK_WATER, 0.12),
            TraitEffect.Sensing(0.04),
            TraitEffect.Defense(0.04),
            TraitEffect.ReproductionMultiplier(1.04),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    BIOLUMINESCENT_LURE(
        "bioluminescent lure",
        "A controlled light organ attracts curious prey in otherwise dark water.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.DARK_WATER, 0.2),
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.24),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.MaintenanceCost(0.27),
        ),
        requirements = listOf(TraitRequirement.allOf(BIOLUMINESCENCE)),
    ),
    PARASITIC_PROBOSCIS(
        "parasitic proboscis",
        "A piercing or anchoring feeding organ that extracts resources from a living host.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.PARASITISM, 0.82),
            TraitEffect.ReproductionMultiplier(1.10),
            TraitEffect.MaintenanceCost(0.3),
        ),
    ),
    COLONY_PROBING_TONGUE(
        "colony-probing tongue",
        "An extremely elongated adhesive tongue reaches minuscule colonial prey through narrow passages in opened nests.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.COLONY_RAIDING, 0.7),
            TraitEffect.CaptureAbility(0.28),
            TraitEffect.MaintenanceCost(0.18),
        ),
        group = TraitGroup.SPECIALIZED_TONGUE,
    ),
    PROJECTILE_TONGUE(
        "projectile tongue",
        "A rapidly projected adhesive tongue lets a stationary hunter seize small moving prey before it can escape.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.35),
            TraitEffect.CaptureAbility(0.22),
            TraitEffect.MaintenanceCost(0.21),
        ),
        group = TraitGroup.SPECIALIZED_TONGUE,
    ),
    SUCKING_PROBOSCIS(
        "sucking proboscis",
        "A narrow piercing mouthpart taps fluids from the living tissues of a host organism.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.PARASITISM, 0.58),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),

    // Herbivory traits
    GRAZING_MOUTHPARTS(
        "grazing mouthparts",
        "Scraping, cropping, grinding, or rasping structures for repeatedly harvesting attached or rooted food.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.GRAZING, 0.82),
            TraitEffect.CaptureAbility(-0.06),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    BROWSING_MOUTHPARTS(
        "browsing mouthparts",
        "Lips, teeth, beaks, or cutting jaws specialized for selectively cropping leaves and twigs above ground level.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.GRAZING, 0.74),
            TraitEffect.StrategyAccess(EcoStrategy.FRUGIVORY, 0.25),
            TraitEffect.HabitatAffinity(Habitat.CANOPY, 0.2),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    FRUIT_EATING_MOUTHPARTS(
        "fruit-eating mouthparts and digestion",
        "Grasping, biting, crushing, or swallowing structures and digestion suited to energy-rich fruits.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.FRUGIVORY, 0.78),
            TraitEffect.HabitatAffinity(Habitat.CANOPY, 0.15),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    RUMINANT_STOMACH(
        "ruminant stomach",
        "Several fermentation chambers and repeated chewing extract energy from fibrous photosynthetic tissue.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.GRAZING, 0.20),
            TraitEffect.ReserveCapacity(0.10),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.GUT_FERMENTATION,
    ),
    FERMENTING_HINDGUT(
        "fermenting hindgut",
        "A large microbe-rich hindgut digests cellulose after food has passed through the stomach.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.GRAZING, 0.16),
            TraitEffect.ReserveCapacity(0.07),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.GUT_FERMENTATION,
    ),
    SEED_CRACKING_MOUTHPARTS(
        "seed-cracking mouthparts",
        "Deep reinforced jaws or a stout beak crush hard seeds and nuts from ground and canopy producers.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.GRAZING, 0.54),
            TraitEffect.StrategyAccess(EcoStrategy.FRUGIVORY, 0.33),
            TraitEffect.HabitatAffinity(Habitat.CANOPY, 0.15),
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    LONG_NECK(
        "long browsing neck",
        "An elongated neck reaches foliage beyond the feeding height of most ground animals.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.CANOPY, 0.3),
            TraitEffect.StrategyAffinity(EcoStrategy.GRAZING, 0.10),
            TraitEffect.WaterRequirement(0.04),
            TraitEffect.MaintenanceCost(0.27),
        ),
    ),
    PREHENSILE_TRUNK(
        "prehensile trunk",
        "A muscular mobile appendage manipulates branches, uproots food, and draws water without lowering the whole body.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.GRAZING, 0.18),
            TraitEffect.HabitatAccess(Habitat.CANOPY, 0.2),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.WaterRequirement(0.03),
            TraitEffect.MaintenanceCost(0.24),
        ),
    ),
    NECTAR_SIPPING_TONGUE(
        "nectar-sipping tongue",
        "An elongated tongue or proboscis reaches energy-rich secretions within elevated reproductive structures.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.NECTAR_FEEDING, 1.0),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(-0.09),
        ),
        group = TraitGroup.SPECIALIZED_TONGUE,
    ),

    // Intelligence and tool use
    INTELLIGENT(
        "advanced intelligence",
        "A large, flexible nervous system supports learning, memory, planning, social inference, and novel solutions across many situations.",
        listOf(
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.Sensing(0.08),
            TraitEffect.Defense(0.06),
            TraitEffect.ReserveCapacity(0.05),
            TraitEffect.NicheCompetitionSensitivity(0.94),
            TraitEffect.ReproductionMultiplier(1.08),
            TraitEffect.MaintenanceCost(1.05),
        ),
        group = TraitGroup.COGNITIVE_COMPLEXITY,
        capabilities = setOf(TraitCapability.ADVANCED_COGNITION),
    ),
    SAPIENT(
        "sapient intelligence",
        "Exceptional abstraction, cumulative culture, symbolic communication, and deliberate long-term planning reshape how the organism solves ecological problems.",
        listOf(
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.Sensing(0.08),
            TraitEffect.Defense(0.08),
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.NicheCompetitionSensitivity(0.90),
            TraitEffect.ReproductionMultiplier(1.12),
            TraitEffect.MaintenanceCost(1.65),
        ),
        group = TraitGroup.COGNITIVE_COMPLEXITY,
        capabilities = setOf(TraitCapability.ADVANCED_COGNITION),
        requirements = listOf(
            TraitRequirement.allOf(SLOW_GROWTH),
        ),
    ),
    TOOL_MANIPULATION(
        "tool manipulation",
        "Dexterous appendages manipulate stones, sticks, containers, or other objects to obtain defended food.",
        listOf(
            TraitEffect.CaptureAbility(0.14),
            TraitEffect.Defense(0.03),
            TraitEffect.MaintenanceCost(0.3),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitCapability.ADVANCED_COGNITION),
        ),
    ),

    // Scavenger and decomposer traits
    FOOD_CLEANING_BEHAVIOR(
        "food-cleaning behavior",
        "The organism deliberately removes parasites, damaged tissue, fouling growth, or trapped debris from another organism and consumes the material it removes.",
        listOf(
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.NicheCompetitionSensitivity(0.86),
            TraitEffect.ReproductionMultiplier(1.04),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.LOCOMOTION)),
            TraitRequirement.anyOf(GRAZING_MOUTHPARTS, MEAT_EATING_MOUTHPARTS),
        ),
    ),
    SCAVENGING_SENSES(
        "long-range carrion senses",
        "Sensory organs capable of locating dead organisms across a broad area.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.SCAVENGING, 0.6),
            TraitEffect.MaintenanceCost(0.27),
        ),
    ),
    RESILIENT_DIGESTION(
        "resilient digestion",
        "A robust digestive system handles large, irregular meals and extracts nutrition from food of inconsistent quality.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.SCAVENGING, 0.4),
            TraitEffect.MetabolicDemandMultiplier(0.8),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.18),
        ),
    ),
    DECOMPOSING_ENZYMES(
        "external decomposing enzymes",
        "Secreted enzymes break down dead sessile tissue outside the body so its nutrients can be absorbed.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.DECOMPOSITION, 0.86),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    WASTE_FEEDING_MOUTHPARTS(
        "waste-feeding mouthparts",
        "Mouthparts and chemical senses are specialized for locating and consuming nutrient-rich animal waste.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.COPROPHAGY, 0.84),
            TraitEffect.CaptureAbility(-0.05),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    MARINE_SNOW_COLLECTORS(
        "marine-snow collectors",
        "Fine collecting surfaces or appendages gather sinking organic particles from dark or deep water.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.DEPOSIT_FEEDING, 0.84),
            TraitEffect.HabitatAffinity(Habitat.DARK_WATER, 0.30),
            TraitEffect.DarkWaterAdaptation,
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),

    // Reef-related traits
    RIGID_COLONY_FRAMEWORK(
        "rigid colony framework",
        "The organism secretes or assembles a durable supporting framework that protects soft feeding bodies and persists after portions of the colony die.",
        listOf(
            TraitEffect.Defense(0.18),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.21),
        ),
    ),
    REEF_BUILDING(
        "reef-building growth",
        "Successive generations extend and bind a rigid colony framework until it becomes persistent three-dimensional aquatic habitat.",
        listOf(
            TraitEffect.ReefBuilding(0.08),
            TraitEffect.ReproductionMultiplier(0.90),
            TraitEffect.MaintenanceCost(0.27),
        ),
        requirements = listOf(
            TraitRequirement.allOf(RIGID_COLONY_FRAMEWORK),
        ),
    ),
    INTERNAL_PHOTOSYMBIONTS(
        "internal photosymbionts",
        "Light-dependent symbionts living within an anchored aquatic host provide part of its energy, restricting productive growth to illuminated shallow water.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.PHOTOSYNTHESIS, 0.55),
            TraitEffect.WaterDepthTolerance(
                optimalMaximumM = 30.0,
                absoluteMaximumM = 80.0,
            ),
            TraitEffect.ReproductionMultiplier(1.10),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.allOf(POLYP_BODY),
        ),
    ),
    REEF_NESTING(
        "reef nesting",
        "Reproduction or shelter depends on cavities and protected surfaces within an aquatic reef.",
        listOf(
            TraitEffect.ReefUse(0.75),
            TraitEffect.Defense(0.08),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    REEF_CAMOUFLAGE(
        "reef camouflage",
        "Color, texture, and body outline resemble the varied surfaces of an aquatic reef.",
        listOf(
            TraitEffect.ReefUse(0.33),
            TraitEffect.Camouflage(Habitat.COASTAL, 0.18),
            TraitEffect.Camouflage(Habitat.SHALLOW_OCEAN, 0.18),
            TraitEffect.MaintenanceCost(0.12),
        ),
    ),
    REEF_SHELTER_DEPENDENCE(
        "reef shelter dependence",
        "Feeding, refuge, and daily activity depend on the dense cavities and broken sight-lines of a living reef.",
        listOf(
            TraitEffect.ReefUse(1.0),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),
    REEF_BORING(
        "reef-boring mouthparts",
        "Hard scraping or drilling structures open cavities and expose food within reef material.",
        listOf(
            TraitEffect.ReefUse(0.75),
            TraitEffect.StrategyAffinity(EcoStrategy.GRAZING, 0.28),
            TraitEffect.MaintenanceCost(0.15),
        ),
    ),

    // Photosynthetic traits
    PHOTOSYNTHETIC_SURFACE(
        "photosynthetic surface",
        "A broad light-harvesting body surface containing photosynthetic pigments.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.PHOTOSYNTHESIS, 1.0),
            TraitEffect.HabitatAffinity(HabitatGroup.DARK, -0.3),
            TraitEffect.MaintenanceCost(0.24),
        ),
        group = TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
    ),
    LARGE_EVERGREEN_LEAVES(
        "large evergreen leaves",
        "Large, long-lived leaves maintain a broad light-harvesting canopy throughout the year rather than being replaced as a seasonal cohort.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.PHOTOSYNTHESIS, 1.06),
            TraitEffect.CanopyLightEfficiency(0.10),
            TraitEffect.WaterRequirement(0.12),
            TraitEffect.TemperatureTolerance(colderC = -4.0),
            TraitEffect.MaintenanceCost(0.33),
        ),
        group = TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
    ),
    NEEDLE_LEAVES(
        "needle leaves",
        "Narrow, tough leaves expose little surface area to freezing air and water loss, trading peak light capture for persistence in cold or dry climates.",
        listOf(
            TraitEffect.StrategyAccess(EcoStrategy.PHOTOSYNTHESIS, 0.90),
            TraitEffect.WaterRequirement(-0.08),
            TraitEffect.TemperatureTolerance(colderC = 4.0),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.21),
        ),
        group = TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
    ),
    CANOPY_GROWTH(
        "canopy growth",
        "A tall or climbing growth form that places much of the organism within an elevated living canopy.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.CANOPY, 0.8),
            TraitEffect.CanopyLightEfficiency(0.22),
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.21),
        ),
    ),
    SHADE_FRONDS(
        "broad shade fronds",
        "Wide light-catching surfaces specialized for dim conditions beneath other organisms.",
        listOf(
            TraitEffect.CanopyLightEfficiency(0.38),
            TraitEffect.InsolationOptimum(-0.22),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitGroup.PHOTOSYNTHETIC_STRUCTURE),
        ),
    ),
    EPIPHYTIC_ROOTS(
        "epiphytic roots",
        "Roots or analogous anchors cling to another organism above the ground and rapidly absorb intermittent rain, mist, and trapped debris without parasitizing the support.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.CANOPY, 0.66),
            TraitEffect.CanopyLightEfficiency(0.10),
            TraitEffect.WaterRequirement(0.12),
            TraitEffect.Defense(-0.04),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
                    TraitCapability.SUBSTRATE_ANCHORING,
                ),
            ),
        ),
    ),
    CUSHION_GROWTH(
        "cushion growth",
        "Many short, tightly packed shoots form a low rounded surface that traps heat and moisture while resisting wind and abrasive particles.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.LAND_SURFACE, 0.5),
            TraitEffect.TemperatureTolerance(optimalColderC = 5.0),
            TraitEffect.WaterRequirement(-0.04),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.Defense(0.05),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
                    TraitCapability.SUBSTRATE_ANCHORING,
                ),
            ),
        ),
    ),
    FLOATING_FRONDS(
        "floating fronds",
        "Long buoyant photosynthetic blades rise from an aquatic anchor into well-lit surface water.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.SHALLOW_OCEAN, 0.4),
            TraitEffect.HabitatAccess(Habitat.FRESHWATER, 0.4),
            TraitEffect.HabitatAffinity(Habitat.COASTAL, 0.2),
            TraitEffect.InsolationOptimum(-0.06),
            TraitEffect.MaintenanceCost(0.21),
        ),
        requirements = listOf(
            TraitRequirement.allOf(TraitGroup.PHOTOSYNTHETIC_STRUCTURE),
        ),
    ),

    // Behavioral traits
    FOSSORIAL_LIVING(
        "fossorial living",
        "Anatomy and behavior for excavating, navigating, and sheltering within soil or soft substrate.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 3.0, hotterC = 3.0),
            TraitEffect.HabitatAffinity(Habitat.UNDERGROUND, 0.15),
            TraitEffect.WaterRequirement(-0.06),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(
                TraitCapability.BURROW_EXCAVATION,
                BURROW_BUILDER,
                BURROW_BORROWER,
            ),
        ),
    ),
    BALL_ROLLING(
        "ball rolling",
        "The organism can roll up into a ball to protect vital organs.",
        listOf(
            TraitEffect.Defense(0.1),
            TraitEffect.PursuitSpeed(-0.1),
            TraitEffect.MaintenanceCost(0.1),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(
                ARMORED_HIDE,
                BONY_SCALES,
                REINFORCED_HIDE,
                SPINES
            )
        )
    ),
    UNDULATING_CLIMBING(
        "undulating climbing",
        "Lasso or concertina movement allows creatures to climb without needing limbs.",
        listOf(
            TraitEffect.HabitatAccess(Habitat.CANOPY, 0.25),
            TraitEffect.MaintenanceCost(0.15)
        ),
        requirements = listOf(
            TraitRequirement.anyOf(BODY_UNDULATION)
        )
    ),
    OPEN_COUNTRY_PREFERENCE(
        "open-country preference",
        "Foraging, escape, and sensory behavior depend on long sight lines and unobstructed movement through sparse vegetation.",
        listOf(
            TraitEffect.DenseCanopyForagingPenalty(0.82),
            TraitEffect.HabitatAffinity(Habitat.LAND_SURFACE, 0.05),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    DAM_BUILDING(
        "dam-building behavior",
        "Coordinated cutting, digging, and placement of durable material impounds flowing water and maintains a protected freshwater home range.",
        listOf(
            TraitEffect.HabitatAffinity(Habitat.FRESHWATER, 0.5),
            TraitEffect.HabitatAffinity(Habitat.LAND_SURFACE, -0.25),
            TraitEffect.Defense(0.06),
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.NicheCompetitionSensitivity(0.82),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.33),
        ),
    ),

    // Social traits
    TERRITORIAL(
        "territorial behavior",
        "Individuals or small groups defend an exclusive local area, limiting overlap with rivals at an energetic cost.",
        listOf(
            TraitEffect.Defense(-0.05),
            TraitEffect.SelfCrowdingSensitivity(1.35),
            TraitEffect.ReproductionMultiplier(0.66),
            TraitEffect.MaintenanceCost(0.21),
        ),
    ),
    SOLITARY(
        "solitary living",
        "Adults normally forage and maintain their living space independently outside courtship and parental care.",
        listOf(
            TraitEffect.MaintenanceCost(-0.15),
            TraitEffect.ReproductionMultiplier(0.75)
        ),
        group = TraitGroup.SOCIAL_ORGANIZATION,
    ),
    GROUP_LIVING(
        "group living",
        "Individuals maintain recurring social relationships in a small or moderately sized group.",
        listOf(
            TraitEffect.Defense(0.04),
            TraitEffect.NicheCompetitionSensitivity(1.05),
            TraitEffect.MaintenanceCost(0.075),
        ),
        group = TraitGroup.SOCIAL_ORGANIZATION,
    ),
    COLLECTIVE_LIVING(
        "collective living",
        "Large herds, flocks, shoals, or swarms coordinate movement and vigilance without forming a reproductive caste system.",
        listOf(
            TraitEffect.Defense(0.08),
            TraitEffect.NicheCompetitionSensitivity(1.1),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.SOCIAL_ORGANIZATION,
    ),
    EUSOCIAL_COLONY(
        "eusocial colony",
        "Overlapping generations cooperate in a persistent colony with reproductive division of labor and communal brood care.",
        listOf(
            TraitEffect.Defense(0.18),
            TraitEffect.ReproductionMultiplier(1.1),
            TraitEffect.MaintenanceCost(0.27),
        ),
        group = TraitGroup.SOCIAL_ORGANIZATION,
    ),
    COOPERATIVE_HUNTING(
        "cooperative hunting",
        "Several individuals coordinate pursuit, encirclement, or ambush rather than attacking independently.",
        listOf(
            TraitEffect.StrategyAffinity(EcoStrategy.AMBUSH_PREDATION, 0.12),
            TraitEffect.StrategyAffinity(EcoStrategy.PURSUIT_PREDATION, 0.18),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.LargerPreySizeClasses(1),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.24),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(GROUP_LIVING, COLLECTIVE_LIVING, EUSOCIAL_COLONY),
        ),
    ),
    GROUP_HUDDLING(
        "group huddling",
        "Individuals press together in dense groups, reducing exposed surface area and sharing metabolic heat during severe cold.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 5.0),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(GROUP_LIVING, COLLECTIVE_LIVING, EUSOCIAL_COLONY)
        ),
    ),
    COLONY_THERMOREGULATION(
        "colony thermoregulation",
        "Workers cluster and generate metabolic heat in winter, then fan and evaporate water to cool the shared nest during hot weather.",
        listOf(
            TraitEffect.SeasonalColdTolerance(maximumBonusC = 24.0, triggerInsolation = 0.62),
            TraitEffect.TemperatureTolerance(hotterC = 5.0),
            TraitEffect.WaterRequirement(0.04),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.42),
        ),
        requirements = listOf(
            TraitRequirement.allOf(EUSOCIAL_COLONY),
        ),
    ),
    HERDING_BEHAVIOR(
        "herding behavior",
        "Individuals maintain cohesive social groups that share vigilance and coordinate travel or defense.",
        listOf(
            TraitEffect.Defense(0.08),
            TraitEffect.ReproductionMultiplier(0.99),
            TraitEffect.MaintenanceCost(0.09),
        ),
        requirements = listOf(
            TraitRequirement.anyOf(GROUP_LIVING, COLLECTIVE_LIVING),
        ),
    ),
    SCHOOLING(
        "coordinated schooling",
        "Many mobile aquatic organisms maintain synchronized spacing and direction, confusing attackers while sharing information about threats and food.",
        listOf(
            TraitEffect.Defense(0.15),
            TraitEffect.PursuitSpeed(0.05),
            TraitEffect.NicheCompetitionSensitivity(1.12),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.AQUATIC_LOCOMOTION,
                    TraitGroup.SOCIAL_ORGANIZATION,
                ),
            ),
        ),
    ),
    HONEY_STORES(
        "communal honey stores",
        "Workers concentrate floral sugars into stable comb stores that feed the colony through winter or other seasonal shortages.",
        listOf(
            TraitEffect.ReserveCapacity(0.90),
            TraitEffect.ReproductionMultiplier(0.93),
            TraitEffect.MaintenanceCost(0.15),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(EUSOCIAL_COLONY, NECTAR_SIPPING_TONGUE),
            ),
        ),
    ),

    // Daily activity patterns
    DIURNAL(
        "diurnal activity",
        "Daily activity is concentrated in the bright portion of the local light cycle.",
        listOf(
            TraitEffect.ActivityPatternEffect(ActivityPattern.DIURNAL),
            TraitEffect.MaintenanceCost(0.03),
            TraitEffect.TemperatureShift(-2.0)
        ),
        group = TraitGroup.ACTIVITY_PATTERN,
    ),
    NOCTURNAL(
        "nocturnal activity",
        "Daily activity is concentrated in the dark portion of the local light cycle.",
        listOf(
            TraitEffect.ActivityPatternEffect(ActivityPattern.NOCTURNAL),
            TraitEffect.MaintenanceCost(0.06),
            TraitEffect.TemperatureShift(2.0)
        ),
        group = TraitGroup.ACTIVITY_PATTERN,
    ),
    VESPERTINE(
        "vespertine activity",
        "Activity peaks around dawn and dusk, partially overlapping both day-active and night-active communities.",
        listOf(
            TraitEffect.ActivityPatternEffect(ActivityPattern.VESPERTINE),
            TraitEffect.MaintenanceCost(0.045),
        ),
        group = TraitGroup.ACTIVITY_PATTERN,
    ),
    CATHEMERAL(
        "cathemeral activity",
        "Activity is distributed flexibly across day and night instead of specializing around one part of the light cycle.",
        listOf(
            TraitEffect.ActivityPatternEffect(ActivityPattern.CATHEMERAL),
            TraitEffect.MaintenanceCost(0.03),
            TraitEffect.TemperatureTolerance(hotterC = 1.0, colderC = 1.0)
        ),
        group = TraitGroup.ACTIVITY_PATTERN,
    ),

    // Dispersal and migration patterns
    NEIGHBOR_DISPERSAL(
        "neighboring-range dispersal",
        "Individuals or propagules routinely spread into nearby suitable territory without a fixed seasonal destination.",
        listOf(
            TraitEffect.Dispersal(DispersalKind.NEIGHBOR),
            TraitEffect.MaintenanceCost(0.09),
        ),
        group = TraitGroup.DISPERSAL_RANGE,
    ),
    SHORT_MIGRATION(
        "short seasonal migration",
        "A recurring seasonal movement between nearby ranges with learned or inherited destinations.",
        listOf(
            TraitEffect.Dispersal(DispersalKind.SHORT_MIGRATION),
            TraitEffect.ReserveCapacity(0.10),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.DISPERSAL_RANGE,
    ),
    REGIONAL_MIGRATION(
        "regional seasonal migration",
        "A recurring seasonal journey between ranges separated across a substantial region.",
        listOf(
            TraitEffect.Dispersal(DispersalKind.REGIONAL_MIGRATION),
            TraitEffect.ReserveCapacity(0.18),
            TraitEffect.MaintenanceCost(0.15),
        ),
        group = TraitGroup.DISPERSAL_RANGE,
    ),
    LONG_MIGRATION(
        "long-distance seasonal migration",
        "A recurring seasonal journey linking widely separated parts of a planet.",
        listOf(
            TraitEffect.Dispersal(DispersalKind.LONG_MIGRATION),
            TraitEffect.ReserveCapacity(0.28),
            TraitEffect.MaintenanceCost(0.21),
        ),
        group = TraitGroup.DISPERSAL_RANGE,
    ),
    NATAL_HOMING(
        "natal homing",
        "Adults navigate back to the region or habitat where they developed in order to reproduce.",
        listOf(
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.ReproductionMultiplier(1.08),
            TraitEffect.MaintenanceCost(0.18),
        ),
        requirements = listOf(TraitRequirement.anyOf(REGIONAL_MIGRATION, LONG_MIGRATION))
    ),
}
