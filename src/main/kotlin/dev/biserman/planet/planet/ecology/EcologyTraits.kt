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
    LARGE(1_000.0, 0.08, 0.18, 0.38),
    HUGE(10_000.0, 0.055, 0.10, 0.12),
    COLOSSAL(100_000.0, 0.040, 0.06, 0.04),
}

/**
 * A biological slot for which a species may select at most one implementation.
 * Groups describe genuine alternatives, not every collection of related traits.
 */
enum class TraitGroup {
    BIOCHEMISTRY,
    THERMOREGULATION,
    DORMANCY_MODE,
    DISPERSAL_RANGE,
    SALINITY_STRATEGY,
    FILTERING_APPARATUS,
    GUT_FERMENTATION,
    SPECIALIZED_TONGUE,
    DOMINANT_BODY_COVERING,
    BODY_BUILD,
    PHOTOSYNTHETIC_STRUCTURE,
    TERRESTRIAL_ATTACHMENT,
    TERRESTRIAL_MOVEMENT_STRUCTURE,
    FLIGHT_STRUCTURE,
    BIOLOGICAL_COLOR,
}

/** Anatomical or behavioral capability that can satisfy another trait's prerequisites. */
enum class TraitCapability {
    PHOTOSYNTHETIC_TISSUE,
    HAIR_COVERING,
    FEATHER_COVERING,
    GROUND_WALKING,
    ARBOREAL_LOCOMOTION,
    ACTIVE_FLIGHT,
    SOCIAL_COLONY,
    NECTAR_FEEDING,
    SUBSTRATE_ANCHORING,
    AQUATIC_LOCOMOTION,
    UNDERWATER_RESPIRATION,
    BREATH_HOLDING,
    WATER_STORAGE,
    LACTATION,
    REPRODUCTION,
    OVOSPORE_REPRODUCTION,
    OVOSPORE_BROODING,
    OVOSPORE_BROOD_SITE,
    BROOD_HOST_RELATIONSHIP,
    ECTOTHERMIC_REGULATION,
    ABSORPTIVE_FILAMENTS,
    RIGID_REEF_FRAMEWORK,
    CHIRPING_VOCALIZATION,
}

/** A recognizable sound pattern that another species may reproduce or exploit. */
enum class AcousticSignal {
    WHALESONG,
    HOWL,
    BIRDSONG,
    CICADA_CHORUS,
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
    CHIRP,
    MEOW,
    PURR,
    HISS,
    BOOM,
    WHOOP,
    CLICK_WHISTLE,
}

sealed interface TraitRequirement {
    fun isSatisfiedBy(
        definition: SpeciesDefinition,
        capabilities: Set<TraitCapability>,
    ): Boolean

    fun describe(): String

    data class AllOf(val capabilities: Set<TraitCapability>) : TraitRequirement {
        init {
            require(capabilities.isNotEmpty())
        }

        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<TraitCapability>,
        ): Boolean = capabilities.containsAll(this.capabilities)

        override fun describe(): String =
            "requires ${capabilities.joinToString()}"
    }

    data class AnyOf(val capabilities: Set<TraitCapability>) : TraitRequirement {
        init {
            require(capabilities.isNotEmpty())
        }

        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<TraitCapability>,
        ): Boolean = this.capabilities.any { it in capabilities }

        override fun describe(): String =
            "requires one of ${capabilities.joinToString()}"
    }

    data class SizeClassIs(val sizeClass: SizeClass) : TraitRequirement {
        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<TraitCapability>,
        ): Boolean = definition.sizeClass == sizeClass

        override fun describe(): String = "requires $sizeClass size"
    }

    data object HasAcousticSignal : TraitRequirement {
        override fun isSatisfiedBy(
            definition: SpeciesDefinition,
            capabilities: Set<TraitCapability>,
        ): Boolean = definition.traits.any { it.acousticSignal != null }

        override fun describe(): String = "requires an acoustic call"
    }
}

sealed interface SpeciesTrait {
    val displayName: String
    val description: String
    val effects: List<TraitEffect>
    val relationships: List<RelationshipEffect>
        get() = emptyList()
    val isFoundation: Boolean
        get() = false
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
        maintenanceCost = 0.02,
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
) : SpeciesTrait {
    BLACK_CAMOUFLAGE("black coloration", "Dark pigments conceal the body against very dim backgrounds.", TraitEffect.CamouflageColor(BiologicalColor.BLACK)),
    BROWN_CAMOUFLAGE("brown coloration", "Earth-toned pigments conceal the body against soil, bark, and dry vegetation.", TraitEffect.CamouflageColor(BiologicalColor.BROWN)),
    GREEN_CAMOUFLAGE("green coloration", "Green pigments conceal the body among photosynthetic growth.", TraitEffect.CamouflageColor(BiologicalColor.GREEN)),
    BLUE_CAMOUFLAGE("blue coloration", "Blue pigments conceal the body in blue-lit environments.", TraitEffect.CamouflageColor(BiologicalColor.BLUE)),
    RED_CAMOUFLAGE("red coloration", "Red pigments conceal or signal where longer wavelengths dominate.", TraitEffect.CamouflageColor(BiologicalColor.RED)),
    PURPLE_CAMOUFLAGE("purple coloration", "Purple pigments conceal or signal against similarly colored surroundings.", TraitEffect.CamouflageColor(BiologicalColor.PURPLE)),
    PALE_CAMOUFLAGE("pale coloration", "Low-saturation pigments conceal the body in deserts and dry grasslands.", TraitEffect.CamouflageColor(BiologicalColor.PALE)),
    WHITE_CAMOUFLAGE("white coloration", "White tissues, hairs, or feathers conceal the body against snow and ice.", TraitEffect.CamouflageColor(BiologicalColor.WHITE)),
    COUNTERSHADE_CAMOUFLAGE("countershading", "A dark upper surface and light underside reduce contrast in sunlit water.", TraitEffect.CamouflageColor(BiologicalColor.COUNTERSHADE)),
    ADAPTIVE_CAMOUFLAGE("adaptive coloration", "Pigment cells actively change the body's color and pattern to match its surroundings.", TraitEffect.CamouflageColor(BiologicalColor.ADAPTIVE), maintenanceCost = 0.08),

    BLACK_PHOTOSYNTHETIC_PIGMENTS("black photosynthetic pigments", "Broad-spectrum pigments absorb most visible wavelengths.", TraitEffect.PhotosyntheticColor(BiologicalColor.BLACK)),
    BROWN_PHOTOSYNTHETIC_PIGMENTS("brown photosynthetic pigments", "Brown photosynthetic pigments balance absorption across a broad spectrum.", TraitEffect.PhotosyntheticColor(BiologicalColor.BROWN)),
    GREEN_PHOTOSYNTHETIC_PIGMENTS("green photosynthetic pigments", "Green photosynthetic tissues absorb red and blue wavelengths efficiently.", TraitEffect.PhotosyntheticColor(BiologicalColor.GREEN)),
    BLUE_PHOTOSYNTHETIC_PIGMENTS("blue photosynthetic pigments", "Blue photosynthetic pigments favor the wavelengths available in their light environment.", TraitEffect.PhotosyntheticColor(BiologicalColor.BLUE)),
    RED_PHOTOSYNTHETIC_PIGMENTS("red photosynthetic pigments", "Red photosynthetic pigments favor the wavelengths available in their light environment.", TraitEffect.PhotosyntheticColor(BiologicalColor.RED)),
    PURPLE_PHOTOSYNTHETIC_PIGMENTS("purple photosynthetic pigments", "Purple photosynthetic pigments favor the wavelengths available in their light environment.", TraitEffect.PhotosyntheticColor(BiologicalColor.PURPLE)),
    PALE_PHOTOSYNTHETIC_PIGMENTS("pale photosynthetic pigments", "Sparse photosynthetic pigments trade light capture for lower tissue investment.", TraitEffect.PhotosyntheticColor(BiologicalColor.PALE)),
    WHITE_PHOTOSYNTHETIC_PIGMENTS("white photosynthetic pigments", "Reflective photosynthetic tissues limit excess light absorption.", TraitEffect.PhotosyntheticColor(BiologicalColor.WHITE)),
    ADAPTIVE_PHOTOSYNTHETIC_PIGMENTS("adaptive photosynthetic pigments", "Pigment concentrations shift to match changing light spectra.", TraitEffect.PhotosyntheticColor(BiologicalColor.ADAPTIVE), maintenanceCost = 0.08),
    ;

    override val effects: List<TraitEffect> =
        listOfNotNull(
            colorEffect,
            TraitEffect.MaintenanceCost(maintenanceCost).takeIf { maintenanceCost > 0.0 },
        )
    override val isFoundation: Boolean = true
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
    override val isFoundation: Boolean = false,
    override val invariantOnly: Boolean = false,
    override val isCosmetic: Boolean = false,
    override val acousticSignal: AcousticSignal? = null,
    override val group: TraitGroup? = null,
    override val capabilities: Set<TraitCapability> = emptySet(),
    override val requirements: List<TraitRequirement> = emptyList(),
) : SpeciesTrait {
    TEMPERATE_BIOCHEMISTRY(
        "temperate biochemistry",
        "Cellular chemistry that functions best at moderate temperatures.",
        listOf(TraitEffect.MaintenanceCost(0.0)),
        isFoundation = true,
        group = TraitGroup.BIOCHEMISTRY,
    ),
    FRIGID_BIOCHEMISTRY(
        "frigid biochemistry",
        "Cellular chemistry built around reactions and structures that remain viable in persistently frigid climates.",
        listOf(
            TraitEffect.TemperatureShift(-25.0),
            TraitEffect.ReproductionMultiplier(0.88),
            TraitEffect.MaintenanceCost(0.04),
        ),
        isFoundation = true,
        group = TraitGroup.BIOCHEMISTRY,
    ),
    HOT_BIOCHEMISTRY(
        "hot biochemistry",
        "Cellular chemistry whose molecules and membranes remain stable in persistently hot climates.",
        listOf(
            TraitEffect.TemperatureShift(28.0),
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.05),
        ),
        isFoundation = true,
        group = TraitGroup.BIOCHEMISTRY,
    ),
    INVARIANT_RESISTANCE(
        "invariant guild resilience",
        "Broad tolerance representing many locally adapted, interchangeable species grouped into one aggregate population.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 32.0, hotterC = 30.0),
            TraitEffect.TemperatureOptimalTolerance(colderC = 30.0, hotterC = 20.0),
            TraitEffect.WaterRequirement(-0.25),
            TraitEffect.ReserveCapacity(0.15),
            TraitEffect.NicheCompetitionSensitivity(0.15),
            TraitEffect.Dormancy(DormancyKind.PROPAGULE, 0.9995),
            TraitEffect.BroadSalinityTolerance,
            TraitEffect.ReproductionMultiplier(0.88),
            TraitEffect.MaintenanceCost(0.08),
        ),
        invariantOnly = true,
        group = TraitGroup.DORMANCY_MODE,
    ),
    MICROSCOPIC_RESTING_STAGES(
        "microscopic resting stages",
        "A small fraction of the active population forms durable cysts, spores, or resting eggs that preserve the lineage through dark or otherwise unproductive seasons.",
        listOf(
            TraitEffect.DormantEntryBiomassRetention(0.10),
            TraitEffect.DormantReactivationMultiplier(10.00),
            TraitEffect.ReproductionMultiplier(1.03),
            TraitEffect.MaintenanceCost(0.001),
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
            TraitEffect.MaintenanceCost(0.02),
        ),
        invariantOnly = true,
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.PHOTOSYNTHETIC_TISSUE)),
        ),
    ),
    ECTOTHERMY(
        "ectothermy",
        "Body activity and temperature depend primarily on heat exchanged with the surrounding environment.",
        listOf(
            TraitEffect.ThermalRegulation(ThermalStrategy.ECTOTHERMY),
            TraitEffect.MaintenanceCost(-0.22),
            TraitEffect.TemperatureTolerance(colderC = -2.0, hotterC = -2.0),
        ),
        isFoundation = true,
        group = TraitGroup.THERMOREGULATION,
        capabilities = setOf(TraitCapability.ECTOTHERMIC_REGULATION),
    ),
    ENDOTHERMY(
        "endothermy",
        "Metabolism produces enough heat to regulate the body substantially independently of ambient temperature.",
        listOf(
            TraitEffect.ThermalRegulation(ThermalStrategy.ENDOTHERMY),
            TraitEffect.TemperatureTolerance(colderC = 8.0, hotterC = 2.0),
            TraitEffect.MaintenanceCost(0.25),
        ),
        isFoundation = true,
        group = TraitGroup.THERMOREGULATION,
    ),
    HETEROTHERMY(
        "heterothermy",
        "Body temperature is actively regulated at some times but allowed to vary during torpor, rest, or unfavorable seasons.",
        listOf(
            TraitEffect.ThermalRegulation(ThermalStrategy.HETEROTHERMY),
            TraitEffect.TemperatureTolerance(colderC = 5.0, hotterC = 1.0),
            TraitEffect.ReserveCapacity(0.20),
            TraitEffect.MaintenanceCost(0.08),
        ),
        isFoundation = true,
        group = TraitGroup.THERMOREGULATION,
    ),
    SLOW_METABOLISM(
        "extremely slow metabolism",
        "Low-throughput digestion and cellular metabolism extract energy from poor food while sharply limiting growth and reproduction.",
        listOf(
            TraitEffect.MetabolicDemandMultiplier(0.55),
            TraitEffect.ReproductionMultiplier(0.65),
            TraitEffect.MaintenanceCost(0.03),
            TraitEffect.PursuitSpeed(-0.5)
        ),
    ),
    BEHAVIORAL_THERMOREGULATION(
        "behavioral thermoregulation",
        "The organism moves between sun, shade, water, shelter, or differently oriented surfaces to keep its body near a useful temperature.",
        listOf(
            TraitEffect.TemperatureOptimalTolerance(colderC = 4.0, hotterC = 4.0),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.04),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.ECTOTHERMIC_REGULATION)),
        ),
    ),
    EXTENDED_PARENTAL_CARE(
        "extended parental care",
        "Parents protect, feed, teach, or transport offspring through a prolonged vulnerable period, improving juvenile survival at a substantial energetic cost.",
        listOf(
            TraitEffect.Defense(0.05),
            TraitEffect.ReproductionMultiplier(1.12),
            TraitEffect.MaintenanceCost(0.11),
        ),
    ),
    MAMMARY_GLANDS(
        "mammary glands",
        "Specialized glands produce nutrient-rich milk, allowing parents to nourish dependent young independently of the food those young can consume directly.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.08),
            TraitEffect.MaintenanceCost(0.06),
        ),
        capabilities = setOf(TraitCapability.LACTATION),
    ),
    LONG_INTERBIRTH_INTERVAL(
        "long interbirth interval",
        "Parents invest for years in each offspring before reproducing again, sharply limiting population growth even when food is abundant.",
        listOf(
            TraitEffect.Defense(0.01),
            TraitEffect.ReproductionMultiplier(0.066),
            TraitEffect.MaintenanceCost(-0.06),
        ),
    ),
    TERRESTRIAL_OVOSPORE(
        "terrestrial ovospore",
        "A seed, spore, or egg develops outside its parent in a terrestrial environment and can be guarded or carried before hatching or germination.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.02),
            TraitEffect.MaintenanceCost(0.03),
        ),
        isFoundation = true,
        capabilities = setOf(
            TraitCapability.REPRODUCTION,
            TraitCapability.OVOSPORE_REPRODUCTION,
            TraitCapability.OVOSPORE_BROODING,
        ),
    ),
    AQUATIC_OVOSPORE(
        "aquatic ovospore",
        "A seed, spore, or egg develops outside its parent while immersed in water and can be guarded or carried before hatching or germination.",
        listOf(
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.0),
        ),
        isFoundation = true,
        capabilities = setOf(
            TraitCapability.REPRODUCTION,
            TraitCapability.OVOSPORE_REPRODUCTION,
            TraitCapability.OVOSPORE_BROODING,
        ),
    ),
    VIVIPARITY(
        "viviparity",
        "Offspring develop within a parent's body until they can survive outside it, protecting early development at a substantial metabolic cost.",
        listOf(
            TraitEffect.Defense(0.10),
            TraitEffect.MaintenanceCost(0.10),
        ),
        isFoundation = true,
        capabilities = setOf(TraitCapability.REPRODUCTION),
    ),
    CLONAL_PROPAGATION(
        "clonal propagation",
        "New organisms separate through budding, fission, fragmentation, runners, or analogous growth without a distinct seed, spore, or egg.",
        listOf(TraitEffect.MaintenanceCost(0.0)),
        isFoundation = true,
        capabilities = setOf(TraitCapability.REPRODUCTION),
    ),
    AERIAL_OVOSPORE_DISPERSAL(
        "aerial ovospore dispersal",
        "Extremely light or aerodynamically shaped ovospores travel long distances through atmospheric currents before settling.",
        listOf(
            TraitEffect.RadiationRange(8),
            TraitEffect.MaintenanceCost(0.08),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.OVOSPORE_REPRODUCTION)),
        ),
    ),
    OVOSPORE_NEST(
        "ovospore nest",
        "A purpose-built or prepared site shelters externally developing eggs, seeds, spores, or analogous propagules from weather and predators.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.15),
            TraitEffect.MaintenanceCost(0.07),
        ),
        capabilities = setOf(TraitCapability.OVOSPORE_BROOD_SITE),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.OVOSPORE_BROODING)),
        ),
    ),
    BODY_CARRIED_OVOSPORES(
        "body-carried ovospores",
        "Externally developing eggs, seeds, spores, or analogous propagules remain attached to, enclosed by, or balanced on a parent's body.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.18),
            TraitEffect.MaintenanceCost(0.08),
        ),
        capabilities = setOf(TraitCapability.OVOSPORE_BROOD_SITE),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.OVOSPORE_BROODING)),
        ),
    ),
    BROOD_PROVISIONING(
        "brood provisioning",
        "Parents stock a brood site or repeatedly deliver food to dependent young that cannot yet forage effectively for themselves.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.22),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.OVOSPORE_BROODING,
                    TraitCapability.OVOSPORE_BROOD_SITE,
                ),
            ),
        ),
    ),
    BROOD_PARASITISM(
        "brood parasitism",
        "Ovospores are placed with a particular host species, transferring incubation or juvenile care to that host while making reproduction dependent on finding it.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.10),
            TraitEffect.MaintenanceCost(-0.06),
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
    PHOTOSYNTHETIC_SURFACE(
        "photosynthetic surface",
        "A broad light-harvesting body surface containing photosynthetic pigments.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PHOTOSYNTHESIS, 1.0),
            TraitEffect.MaintenanceCost(0.08),
        ),
        group = TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
        capabilities = setOf(TraitCapability.PHOTOSYNTHETIC_TISSUE),
    ),
    LARGE_EVERGREEN_LEAVES(
        "large evergreen leaves",
        "Large, long-lived leaves maintain a broad light-harvesting canopy throughout the year rather than being replaced as a seasonal cohort.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PHOTOSYNTHESIS, 1.06),
            TraitEffect.CanopyLightEfficiency(0.10),
            TraitEffect.WaterRequirement(0.12),
            TraitEffect.TemperatureTolerance(colderC = -4.0),
            TraitEffect.MaintenanceCost(0.11),
        ),
        group = TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
        capabilities = setOf(TraitCapability.PHOTOSYNTHETIC_TISSUE),
    ),
    NEEDLE_LEAVES(
        "needle leaves",
        "Narrow, tough leaves expose little surface area to freezing air and water loss, trading peak light capture for persistence in cold or dry climates.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PHOTOSYNTHESIS, 0.90),
            TraitEffect.WaterRequirement(-0.08),
            TraitEffect.TemperatureTolerance(colderC = 4.0),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.07),
        ),
        group = TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
        capabilities = setOf(TraitCapability.PHOTOSYNTHETIC_TISSUE),
    ),
    FLOATING_BODY(
        "floating body",
        "A minuscule, low-density body with drag-producing surfaces or buoyant chambers that remains suspended in atmospheric currents.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.AERIAL, 0.78),
            TraitEffect.PelagicAerialResidency,
            TraitEffect.WaterRequirement(-0.15),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.04),
        ),
        requirements = listOf(TraitRequirement.SizeClassIs(SizeClass.MINUSCULE)),
    ),
    ROOTED_BODY(
        "rooted body",
        "True roots penetrate terrestrial substrate, anchoring the body while gathering water and dissolved nutrients.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.65),
            TraitEffect.StrategySupport(EcoStrategy.ABSORPTION, 0.25),
            TraitEffect.MaintenanceCost(0.03),
        ),
        group = TraitGroup.TERRESTRIAL_ATTACHMENT,
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
    ),
    SURFACE_HOLDFAST(
        "surface holdfast",
        "Fine adhesive filaments or pads fasten the body to soil, stone, bark, or another exposed surface without penetrating it as true roots.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.58),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.20),
            TraitEffect.StrategySupport(EcoStrategy.ABSORPTION, 0.22),
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.025),
        ),
        group = TraitGroup.TERRESTRIAL_ATTACHMENT,
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
    ),
    INTERWOVEN_MAT(
        "interwoven mat",
        "Numerous short shoots, branches, or filaments overlap into a continuous low mat that retains moisture and resists being displaced.",
        listOf(
            TraitEffect.WaterRequirement(-0.04),
            TraitEffect.Defense(0.04),
            TraitEffect.NicheCompetitionSensitivity(0.88),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.035),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.SUBSTRATE_ANCHORING)),
        ),
    ),
    WALKING_LIMBS(
        "walking limbs",
        "Jointed, load-bearing limbs support deliberate walking, running, or hopping across solid ground.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.65),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
        capabilities = setOf(TraitCapability.GROUND_WALKING),
    ),
    UNDULATING_BODY(
        "undulating body",
        "Alternating muscular waves push an elongated body across the ground without weight-bearing limbs.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.65),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.4),
            TraitEffect.CaptureAbility(0.02),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
    ),
    MUSCULAR_FOOT(
        "muscular foot",
        "A broad contractile foot produces slow, stable movement across soil, rock, plants, or other firm surfaces.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.65),
            TraitEffect.Defense(0.02),
            TraitEffect.CaptureAbility(-0.02),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
    ),
    CRAWLING_APPENDAGES(
        "crawling appendages",
        "Several small jointed appendages distribute weight and provide precise movement over irregular solid surfaces.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.65),
            TraitEffect.CaptureAbility(0.01),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
    ),
    ENLARGED_CARDIOPULMONARY_SYSTEM(
        "enlarged heart and lungs",
        "An unusually large heart, lungs, and pulmonary exchange surface sustain oxygen delivery in thin air.",
        listOf(
            TraitEffect.ElevationToleranceShift(2_500.0),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    HIGH_AFFINITY_BLOOD(
        "high-affinity blood",
        "Circulating respiratory pigments bind oxygen effectively at the low partial pressures found at high elevation.",
        listOf(
            TraitEffect.ElevationToleranceShift(2_500.0),
            TraitEffect.CaptureAbility(-0.03),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    HYPOXIA_RESPONSIVE_METABOLISM(
        "hypoxia-responsive metabolism",
        "Oxygen-sensing pathways adjust circulation and cellular energy use during chronic exposure to thin air.",
        listOf(
            TraitEffect.ElevationToleranceShift(3_500.0),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    SEA_ICE_LOCOMOTION(
        "sea-ice locomotion",
        "Broad feet, claws, body posture, or equivalent adaptations support travel and hunting across floating sea ice.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.SEA_ICE, 0.72),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    AQUATIC_FLIPPERS(
        "aquatic flippers",
        "Broad propulsive limbs or fins that support controlled swimming in open water.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.72),
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.52),
            TraitEffect.MaintenanceCost(0.10),
        ),
        capabilities = setOf(TraitCapability.AQUATIC_LOCOMOTION),
    ),
    GILLS(
        "gills",
        "Thin, blood-supplied folds extract dissolved respiratory gases from water as it passes over them.",
        listOf(
            TraitEffect.AquaticRespiration(AquaticRespirationMode.UNDERWATER),
            TraitEffect.MaintenanceCost(0.05),
        ),
        capabilities = setOf(TraitCapability.UNDERWATER_RESPIRATION),
    ),
    DIFFUSIVE_AQUATIC_GAS_EXCHANGE(
        "diffusive aquatic gas exchange",
        "A thin body surface exchanges dissolved respiratory gases directly with surrounding water without dedicated gills.",
        listOf(
            TraitEffect.AquaticRespiration(AquaticRespirationMode.UNDERWATER),
            TraitEffect.Defense(-0.04),
            TraitEffect.MaintenanceCost(0.02),
        ),
        capabilities = setOf(TraitCapability.UNDERWATER_RESPIRATION),
    ),
    PULSING_BELL(
        "pulsing bell",
        "A flexible bell-shaped body rhythmically displaces water, allowing controlled vertical and horizontal swimming without rigid fins.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.62),
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.34),
            TraitEffect.HabitatSupport(Habitat.DARK_WATER, 0.30),
            TraitEffect.PursuitSpeed(0.04),
            TraitEffect.MaintenanceCost(0.08),
        ),
        capabilities = setOf(TraitCapability.AQUATIC_LOCOMOTION),
    ),
    PROLONGED_BREATH_HOLDING(
        "prolonged breath-holding",
        "Large internal oxygen stores and dive responses sustain repeated activity far from an immediately accessible shore.",
        listOf(
            TraitEffect.AquaticRespiration(AquaticRespirationMode.BREATH_HOLDING),
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.06),
        ),
        capabilities = setOf(TraitCapability.BREATH_HOLDING),
    ),
    STROKE_AND_GLIDE_SWIMMING(
        "stroke-and-glide swimming",
        "Alternating propulsive strokes with passive glides reduces the energy and oxygen spent traveling during repeated breath-hold dives.",
        listOf(
            TraitEffect.MetabolicDemandMultiplier(0.75),
            TraitEffect.MaintenanceCost(0.03),
            TraitEffect.Defense(-0.05)
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.AQUATIC_LOCOMOTION,
                ),
            ),
        ),
    ),
    SEA_ICE_ROOKERY(
        "sea-ice rookery",
        "Breeding colonies occupy persistent sea ice close enough to land for repeated access to stable resting and nesting grounds.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.SEA_ICE, 0.82),
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
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.60),
            TraitEffect.ObligateResidentHabitat(Habitat.COASTAL),
            TraitEffect.RequiresAdjacentLand,
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    DEEP_DIVING_PHYSIOLOGY(
        "deep-diving physiology",
        "Pressure-tolerant tissues, collapsible gas spaces, oxygen stores, or equivalent adaptations permit prolonged activity below the sunlit surface layer.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.DARK_WATER, 0.65),
            TraitEffect.DarkWaterAdaptation,
            // Deep water is usually cooler and less seasonally variable than
            // the surface represented by the tile's single temperature.
            TraitEffect.TemperatureOptimalTolerance(hotterC = 4.0, colderC = 4.0),
            TraitEffect.TemperatureTolerance(hotterC = 8.0, colderC = 8.0),
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.MaintenanceCost(0.12),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.AQUATIC_LOCOMOTION)),
            TraitRequirement.AnyOf(
                setOf(
                    TraitCapability.UNDERWATER_RESPIRATION,
                    TraitCapability.BREATH_HOLDING,
                ),
            ),
        ),
    ),
    AMPHIBIOUS_LIMBS(
        "amphibious limbs",
        "Load-bearing limbs and swimming surfaces that permit regular movement between land and shallow water.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.44),
            TraitEffect.HabitatSupport(Habitat.FRESHWATER, 0.58),
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.54),
            TraitEffect.MaintenanceCost(0.11),
        ),
    ),
    WADING_LIMBS(
        "wading limbs",
        "Elongated load-bearing limbs keep the body above shallow water while allowing deliberate movement and prey capture over soft submerged ground.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.34),
            TraitEffect.HabitatSupport(Habitat.FRESHWATER, 0.58),
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.52),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.MaintenanceCost(0.09),
        ),
        group = TraitGroup.TERRESTRIAL_MOVEMENT_STRUCTURE,
        capabilities = setOf(TraitCapability.GROUND_WALKING),
    ),
    CLIMBING_LIMBS(
        "climbing limbs",
        "Grasping limbs, claws, pads, or a prehensile body that supports deliberate movement through a canopy.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.62),
            TraitEffect.CaptureAbility(-0.03),
            TraitEffect.MaintenanceCost(0.08),
        ),
        capabilities = setOf(TraitCapability.ARBOREAL_LOCOMOTION),
    ),
    CANOPY_GROWTH(
        "canopy growth",
        "A tall or climbing growth form that places much of the organism within an elevated living canopy.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.80),
            TraitEffect.CanopyLightEfficiency(0.22),
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    FRUIT_BEARING(
        "fruit-bearing reproductive structures",
        "Energy-rich fruits surround or accompany propagules, recruiting mobile animals to disperse them.",
        listOf(
            TraitEffect.FruitProduction(0.0025),
            TraitEffect.MaintenanceCost(0.08),
        ),
    ),
    FLOWERS(
        "flowers",
        "Specialized reproductive structures expose pollen and ovules while advertising to mobile visitors or releasing pollen into the environment.",
        listOf(
            TraitEffect.Flowering,
            TraitEffect.ReproductionMultiplier(1.05),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    NECTARIES(
        "nectaries",
        "Secretory tissues offer an energy-rich liquid reward that attracts animals to reproductive structures.",
        listOf(
            TraitEffect.NectarProduction(0.025),
            TraitEffect.WaterRequirement(0.02),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    SHADE_FRONDS(
        "broad shade fronds",
        "Wide light-catching surfaces specialized for dim conditions beneath other organisms.",
        listOf(
            TraitEffect.CanopyLightEfficiency(0.38),
            TraitEffect.InsolationOptimum(-0.22),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.PHOTOSYNTHETIC_TISSUE)),
        ),
    ),
    EPIPHYTIC_ROOTS(
        "epiphytic roots",
        "Roots or analogous anchors cling to another organism above the ground and rapidly absorb intermittent rain, mist, and trapped debris without parasitizing the support.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.66),
            TraitEffect.CanopyLightEfficiency(0.10),
            TraitEffect.WaterRequirement(0.12),
            TraitEffect.Defense(-0.04),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.PHOTOSYNTHETIC_TISSUE,
                    TraitCapability.SUBSTRATE_ANCHORING,
                ),
            ),
        ),
    ),
    CUSHION_GROWTH(
        "cushion growth",
        "Many short, tightly packed shoots form a low rounded surface that traps heat and moisture while resisting wind and abrasive particles.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.24),
            TraitEffect.TemperatureOptimalTolerance(colderC = 5.0),
            TraitEffect.WaterRequirement(-0.04),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.Defense(0.05),
            TraitEffect.MaintenanceCost(0.05),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.PHOTOSYNTHETIC_TISSUE,
                    TraitCapability.SUBSTRATE_ANCHORING,
                ),
            ),
        ),
    ),
    BUOYANCY_BLADDER(
        "buoyancy bladder",
        "A gas- or fluid-regulating chamber that controls position in the water column without continuous swimming.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.70),
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.55),
            TraitEffect.Defense(-0.05),
            TraitEffect.MaintenanceCost(0.04),
        ),
        capabilities = setOf(TraitCapability.AQUATIC_LOCOMOTION),
    ),
    FRESHWATER_OSMOREGULATION(
        "freshwater osmoregulation",
        "Membranes and excretory structures that maintain internal chemistry in dilute freshwater.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.FRESHWATER, 0.80),
            TraitEffect.FreshwaterOsmoregulation,
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.04),
        ),
        group = TraitGroup.SALINITY_STRATEGY,
    ),
    EURYHALINE_OSMOREGULATION(
        "euryhaline osmoregulation",
        "Adjustable membranes, kidneys, salt glands, or analogous organs permit repeated transitions between dilute freshwater and salty water.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.FRESHWATER, 0.68),
            TraitEffect.BroadSalinityTolerance,
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.07),
        ),
        group = TraitGroup.SALINITY_STRATEGY,
    ),
    COASTAL_CLINGING_FEET(
        "coastal clinging feet",
        "Gripping limbs or attachment pads that resist waves and currents in shallow coastal habitats.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.78),
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.55),
            TraitEffect.CaptureAbility(-0.05),
            TraitEffect.MaintenanceCost(0.03),
        ),
    ),
    SUBSTRATE_HOLDFAST(
        "aquatic holdfast",
        "A tough anchoring structure that secures a sessile body to rock or reef under waves and currents.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.68),
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.56),
            TraitEffect.MaintenanceCost(0.04),
        ),
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
    ),
    FLOATING_FRONDS(
        "floating fronds",
        "Long buoyant photosynthetic blades rise from an aquatic anchor into well-lit surface water.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.40),
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.28),
            TraitEffect.InsolationOptimum(-0.06),
            TraitEffect.MaintenanceCost(0.07),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.PHOTOSYNTHETIC_TISSUE)),
        ),
    ),
    BENTHIC_BODY(
        "benthic body",
        "A flattened, weighted, or downward-oriented body is specialized for resting and feeding along the bottom of an aquatic habitat.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.50),
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.40),
            TraitEffect.HabitatSupport(Habitat.DARK_WATER, 0.25),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.PursuitSpeed(-0.12),
            TraitEffect.Defense(0.05),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    GELATINOUS_BODY(
        "gelatinous body",
        "A mostly water-filled body achieves large volume and buoyancy with little metabolically expensive tissue, at the cost of poor resistance to attack.",
        listOf(
            TraitEffect.MetabolicDemandMultiplier(0.72),
            TraitEffect.Defense(-0.14),
            TraitEffect.ReproductionMultiplier(1.08),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    POLYP_BODY(
        "anchored polyp body",
        "A mouth surrounded by flexible feeding structures projects from an attached body that can withdraw or contract when disturbed.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.62),
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.54),
            TraitEffect.HabitatSupport(Habitat.DARK_WATER, 0.34),
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.24),
            TraitEffect.CaptureAbility(0.06),
            TraitEffect.MaintenanceCost(0.05),
        ),
        capabilities = setOf(TraitCapability.SUBSTRATE_ANCHORING),
    ),
    FEATHERED_WINGS(
        "feathered wings",
        "Forelimbs bearing asymmetric flight feathers generate lift and thrust through active wingbeats.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.AERIAL, 0.85),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.20),
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.12),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.24),
        ),
        group = TraitGroup.FLIGHT_STRUCTURE,
        capabilities = setOf(TraitCapability.ACTIVE_FLIGHT),
        requirements = listOf(TraitRequirement.AllOf(setOf(TraitCapability.FEATHER_COVERING))),
    ),
    MEMBRANOUS_WINGS(
        "membranous wings",
        "Thin living membranes stretched between elongated supports generate lift and thrust through active wingbeats.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.AERIAL, 0.85),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.20),
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.12),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.24),
        ),
        group = TraitGroup.FLIGHT_STRUCTURE,
        capabilities = setOf(TraitCapability.ACTIVE_FLIGHT),
    ),
    INSECTOID_WINGS(
        "insectoid wings",
        "Thin wings articulated to an external body wall generate lift and thrust without replacing the walking appendages.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.AERIAL, 0.85),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.20),
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.12),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.24),
        ),
        group = TraitGroup.FLIGHT_STRUCTURE,
        capabilities = setOf(TraitCapability.ACTIVE_FLIGHT),
    ),
    PELAGIC_SOARING_WINGS(
        "pelagic soaring wings",
        "Long, efficient wings and wind-harvesting flight allow extended foraging far from land without exhausting energy reserves.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.AERIAL, 0.10),
            TraitEffect.PelagicAerialResidency,
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.MaintenanceCost(0.08),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.ACTIVE_FLIGHT)),
        ),
    ),
    SUBTERRANEAN_BURROWING(
        "subterranean burrowing",
        "Anatomy and behavior for excavating, navigating, and sheltering within soil or soft substrate.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 3.0, hotterC = 3.0),
            TraitEffect.WaterRequirement(-0.06),
            TraitEffect.BurrowRefuge,
            TraitEffect.MaintenanceCost(0.10),
        ),
    ),
    INSULATED_BURROW_REFUGE(
        "insulated burrow refuge",
        "A sheltered burrow or rock-crevice retreat buffers its occupant from the coldest exposed-air temperatures.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 5.0),
            TraitEffect.BurrowRefuge,
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    DRY_BURROW_NEST(
        "dry burrow nest",
        "A nest chamber whose eggs, young, stored food, or respiratory surfaces require a well-drained burrow.",
        listOf(
            TraitEffect.TemperatureTolerance(hotterC = 5.0, colderC = 3.0),
            TraitEffect.WaterRequirement(-0.06),
            TraitEffect.MaximumWaterTolerance(
                optimalMaximumChange = -0.66,
                absoluteMaximumChange = -0.33,
            ),
            TraitEffect.BurrowRefuge,
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    BALEEN(
        "baleen",
        "Dense flexible plates that strain suspended organisms from water passing through the mouth.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.FILTER_FEEDING, 0.88),
            TraitEffect.CaptureAbility(0.06),
            TraitEffect.MaintenanceCost(0.05),
        ),
        group = TraitGroup.FILTERING_APPARATUS,
    ),
    SIEVING_TEETH(
        "sieving teeth",
        "Interlocking teeth form a sieve that retains minuscule swimming prey as water is expelled from the mouth.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.FILTER_FEEDING, 0.82),
            TraitEffect.CaptureAbility(0.0),
            TraitEffect.MaintenanceCost(0.05),
        ),
        group = TraitGroup.FILTERING_APPARATUS,
    ),
    GILL_PADS(
        "gill pads",
        "Broad ciliated or mucus-coated respiratory surfaces that also trap minuscule food from flowing water.",
        listOf(
            TraitEffect.AquaticRespiration(AquaticRespirationMode.UNDERWATER),
            TraitEffect.StrategySupport(EcoStrategy.FILTER_FEEDING, 0.82),
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.30),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.FILTERING_APPARATUS,
        capabilities = setOf(TraitCapability.UNDERWATER_RESPIRATION),
    ),
    SUSPENSION_FEEDING_TENTACLES(
        "suspension-feeding tentacles",
        "Many slender tentacles, pinnules, or comparable appendages intercept minuscule organisms carried past the body by water.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.FILTER_FEEDING, 0.82),
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.MaintenanceCost(0.05),
        ),
        group = TraitGroup.FILTERING_APPARATUS,
    ),
    BENTHIC_SUCTION_FEEDING(
        "benthic suction-feeding mouth",
        "A muscular tongue, sealed lips, and a vaulted mouth expose and suction soft-bodied prey from seafloor sediment.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.52),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    GRAZING_MOUTHPARTS(
        "grazing mouthparts",
        "Scraping, cropping, grinding, or rasping structures for repeatedly harvesting attached or rooted food.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.82),
            TraitEffect.CaptureAbility(-0.06),
            TraitEffect.MaintenanceCost(0.03),
        ),
    ),
    BROWSING_MOUTHPARTS(
        "browsing mouthparts",
        "Lips, teeth, beaks, or cutting jaws specialized for selectively cropping leaves and twigs above ground level.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.74),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.24),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    FRUIT_EATING_MOUTHPARTS(
        "fruit-eating mouthparts and digestion",
        "Grasping, biting, crushing, or swallowing structures and digestion suited to energy-rich fruits.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.FRUGIVORY, 0.78),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.18),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    AMBUSH_MUSCULATURE(
        "burst ambush musculature",
        "Muscles specialized for short, explosive attacks launched from concealment or stillness.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.58),
            TraitEffect.CaptureAbility(0.20),
            TraitEffect.MaintenanceCost(0.13),
        ),
    ),
    SWIFT_LEGS(
        "swift legs",
        "Long, powerful, or rapidly cycling walking limbs increase running speed, helping hunters close distance and prey escape pursuit.",
        listOf(
            TraitEffect.PursuitSpeed(0.18),
            TraitEffect.WaterRequirement(0.03),
            TraitEffect.MaintenanceCost(0.10),
        ),
        requirements = listOf(TraitRequirement.AllOf(setOf(TraitCapability.GROUND_WALKING))),
    ),
    STREAMLINED_BODY(
        "streamlined body",
        "A smooth tapered profile reduces drag during sustained swimming, helping hunters close distance and prey escape pursuit.",
        listOf(
            TraitEffect.PursuitSpeed(0.18),
            TraitEffect.WaterRequirement(0.03),
            TraitEffect.MaintenanceCost(0.10),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.AQUATIC_LOCOMOTION)),
        ),
    ),
    SLENDER_BODY(
        "slender body",
        "A narrow, lightly built torso reduces the mass that must be accelerated and exposes more surface area for heat loss.",
        listOf(
            TraitEffect.BodyMassMultiplier(0.5),
            TraitEffect.PursuitSpeed(0.08),
            TraitEffect.TemperatureTolerance(colderC = -1.0, hotterC = 2.0),
            TraitEffect.Defense(-0.04),
            TraitEffect.MaintenanceCost(0.035),
        ),
        group = TraitGroup.BODY_BUILD,
    ),
    BULKY_BODY(
        "bulky body",
        "A broad, heavily built torso provides thermal mass and resilience but is costly to accelerate and reproduce.",
        listOf(
            TraitEffect.BodyMassMultiplier(2.0),
            TraitEffect.PursuitSpeed(-0.05),
            TraitEffect.TemperatureTolerance(colderC = 2.0, hotterC = -1.0),
            TraitEffect.Defense(0.10),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.045),
        ),
        group = TraitGroup.BODY_BUILD,
    ),
    LONG_TUSKS(
        "long tusks",
        "Elongated exposed teeth serve as weapons, display structures, digging tools, or levers during contests and movement.",
        listOf(
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.Defense(0.14),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    STRONG_JAWS(
        "strong jaws",
        "Deep jaw muscles and reinforced skull structures generate unusually forceful bites for seizing, crushing, or dismembering food.",
        listOf(
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.Defense(0.04),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    ANTLERS(
        "antlers",
        "Branching seasonal head weapons are regrown for display, mate competition, and defense.",
        listOf(
            TraitEffect.Defense(0.11),
            TraitEffect.ReproductionMultiplier(1.06),
            TraitEffect.MaintenanceCost(0.11),
        ),
    ),
    LARGE_HORN(
        "large horn",
        "A large permanent keratinous or bony head weapon deters predators and resolves contests by impact or leverage.",
        listOf(
            TraitEffect.CaptureAbility(0.03),
            TraitEffect.Defense(0.15),
            TraitEffect.ReproductionMultiplier(0.98),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    RETRACTABLE_CLAWS(
        "retractable claws",
        "Claws are protected while traveling and extended for traction, climbing, grappling, and close-range prey capture.",
        listOf(
            TraitEffect.CaptureAbility(0.11),
            TraitEffect.Defense(0.03),
            TraitEffect.MaintenanceCost(0.05),
        ),
        requirements = listOf(
            TraitRequirement.AnyOf(
                setOf(
                    TraitCapability.GROUND_WALKING,
                    TraitCapability.ARBOREAL_LOCOMOTION,
                ),
            ),
        ),
    ),
    FLEXIBLE_SPINE(
        "flexible spine",
        "A highly flexible axial skeleton lengthens the running stride and allows rapid twisting during pursuit, pouncing, and falls.",
        listOf(
            TraitEffect.PursuitSpeed(0.10),
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    HIGH_POUNCING(
        "high pouncing",
        "A high arcing leap uses sound and precise impact to pin prey hidden in shallow burrows, snow, or dense ground cover.",
        listOf(
            TraitEffect.BurrowerCaptureBonus(0.24),
            TraitEffect.MaintenanceCost(0.05),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.GROUND_WALKING)),
        ),
    ),
    SPEAR_BILL(
        "spear bill",
        "A long pointed bill rapidly stabs or seizes small aquatic prey while its bearer wades or waits at the water's edge.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.FRESHWATER, 0.22),
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.14),
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.34),
            TraitEffect.CaptureAbility(0.15),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    MOTION_TRACKING_SENSES(
        "motion-tracking senses",
        "Vision, hearing, scent, vibration detection, or equivalent senses allow a hunter to follow moving prey through a sustained chase.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PURSUIT_PREDATION, 0.70),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.MaintenanceCost(0.13),
        ),
    ),
    CAMOUFLAGE_PATTERN(
        "camouflage pattern",
        "Body colors and markings that break up the organism's outline against common backgrounds.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.36),
            TraitEffect.Camouflage(Habitat.LAND_SURFACE, 0.20),
            TraitEffect.Camouflage(Habitat.CANOPY, 0.18),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    SCAVENGING_SENSES(
        "long-range carrion senses",
        "Sensory organs capable of locating dead organisms across a broad area.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.SCAVENGING, 0.82),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    EXPANDABLE_CROP(
        "expandable food crop",
        "A distensible storage chamber holds a large meal after brief access to an unpredictable carcass.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.SCAVENGING, 0.08),
            TraitEffect.ReserveCapacity(0.42),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    DECOMPOSING_ENZYMES(
        "external decomposing enzymes",
        "Secreted enzymes break down dead sessile tissue outside the body so its nutrients can be absorbed.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.DECOMPOSITION, 0.86),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    DETRITUS_DIGESTIVE_TRACT(
        "detritus-digesting gut",
        "A long digestive tract and microbial community extract energy from fragments of dead sessile organisms.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.DECOMPOSITION, 0.78),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    WASTE_FEEDING_MOUTHPARTS(
        "waste-feeding mouthparts",
        "Mouthparts and chemical senses are specialized for locating and consuming nutrient-rich animal waste.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.COPROPHAGY, 0.84),
            TraitEffect.CaptureAbility(-0.05),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    WASTE_ABSORBING_ROOTS(
        "waste-absorbing roots",
        "Root membranes and associated microbes rapidly capture nutrients released from nearby animal waste.",
        listOf(
            TraitEffect.WasteFertilization(0.48),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.04),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.SUBSTRATE_ANCHORING)),
        ),
    ),
    MARINE_SNOW_COLLECTORS(
        "marine-snow collectors",
        "Fine collecting surfaces or appendages gather sinking organic particles from dark or deep water.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.DEPOSIT_FEEDING, 0.84),
            TraitEffect.HabitatSupport(Habitat.DARK_WATER, 0.30),
            TraitEffect.DarkWaterAdaptation,
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    PARASITIC_PROBOSCIS(
        "parasitic proboscis",
        "A piercing or anchoring feeding organ that extracts resources from a living host.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PARASITISM, 0.82),
            TraitEffect.ReproductionMultiplier(1.10),
            TraitEffect.MaintenanceCost(0.10),
        ),
    ),
    ABSORPTIVE_FILAMENTS(
        "absorptive filaments",
        "A branching external network that digests or absorbs dissolved nutrients across a large surface.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.ABSORPTION, 0.82),
            TraitEffect.MaintenanceCost(0.04),
        ),
        capabilities = setOf(TraitCapability.ABSORPTIVE_FILAMENTS),
    ),
    HOST_PENETRATING_FILAMENTS(
        "host-penetrating filaments",
        "Fine invasive growth enters living tissues and draws resources directly from a host while remaining difficult to remove.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PARASITISM, 0.76),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.30),
            TraitEffect.ReproductionMultiplier(1.08),
            TraitEffect.Defense(-0.05),
            TraitEffect.MaintenanceCost(0.08),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.ABSORPTIVE_FILAMENTS)),
        ),
    ),
    FUR(
        "fur",
        "A coat of hairlike filaments protects the skin and provides light insulation while still releasing excess heat.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 1.0, hotterC = -1.0),
            TraitEffect.MaintenanceCost(0.03),
        ),
        group = TraitGroup.DOMINANT_BODY_COVERING,
        capabilities = setOf(TraitCapability.HAIR_COVERING),
    ),
    DENSE_UNDERCOAT(
        "dense undercoat",
        "A thick layer of fine hair beneath the outer fur traps still air close to the body.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 8.0, hotterC = -2.0),
            TraitEffect.WaterRequirement(-0.04),
            TraitEffect.MaintenanceCost(0.04),
        ),
        requirements = listOf(TraitRequirement.AllOf(setOf(TraitCapability.HAIR_COVERING))),
    ),
    WOOLLY_UNDERCOAT(
        "woolly undercoat",
        "A second layer of fine, densely packed hairs traps additional insulating air beneath the outer coat.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 5.0, hotterC = -2.0),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.HAIR_COVERING)),
        ),
    ),
    FEATHERS(
        "feathers",
        "Branching keratinous filaments form a light protective body covering that can support specialized insulation, display, waterproofing, or flight.",
        listOf(
            TraitEffect.Defense(0.02),
            TraitEffect.MaintenanceCost(0.02),
        ),
        group = TraitGroup.DOMINANT_BODY_COVERING,
        capabilities = setOf(TraitCapability.FEATHER_COVERING),
    ),
    MOLTING_EXOSKELETON(
        "molting exoskeleton",
        "A segmented external skeleton supports the body and resists injury but must periodically be shed and rebuilt as the organism grows.",
        listOf(
            TraitEffect.Defense(0.14),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.05),
        ),
        group = TraitGroup.DOMINANT_BODY_COVERING,
    ),
    WATER_RETENTIVE_SCALES(
        "water-retentive scales",
        "Overlapping low-permeability plates protect the body surface and slow water loss without requiring a continuously moist outer layer.",
        listOf(
            TraitEffect.WaterRequirement(-0.06),
            TraitEffect.TemperatureTolerance(hotterC = 1.0),
            TraitEffect.Defense(0.08),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.04),
        ),
        group = TraitGroup.DOMINANT_BODY_COVERING,
    ),
    INSULATING_PLUMAGE(
        "insulating plumage",
        "Dense overlapping feathers trap air around the body while remaining lighter than an equally thick fur coat.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 8.0, hotterC = -2.0),
            TraitEffect.WaterRequirement(-0.03),
            TraitEffect.MaintenanceCost(0.05),
        ),
        requirements = listOf(TraitRequirement.AllOf(setOf(TraitCapability.FEATHER_COVERING))),
    ),
    BARE_HEAT_DISSIPATING_SKIN(
        "bare heat-dissipating skin",
        "Exposed, well-supplied regions of skin shed heat efficiently but sacrifice insulation and physical protection.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -5.0, hotterC = 6.0),
            TraitEffect.Defense(-0.03),
            TraitEffect.MaintenanceCost(0.03),
        ),
    ),
    CONCENTRATED_URINE(
        "concentrated urine",
        "Highly water-retentive kidneys excrete dissolved wastes in a small volume of concentrated urine.",
        listOf(
            TraitEffect.MaintenanceCost(0.03),
            TraitEffect.WaterRequirement(-0.1),
        ),
    ),
    SWEAT_GLANDS(
        "sweat glands",
        "Skin glands that cool the body by evaporating secreted water.",
        listOf(
            TraitEffect.TemperatureTolerance(hotterC = 9.0),
            TraitEffect.WaterRequirement(0.08),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    MASSIVE_EARS(
        "massive heat-radiating ears",
        "Large thin appendages with rich circulation that exchange heat rapidly with the air.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -3.0, hotterC = 6.0),
            TraitEffect.Defense(-0.04),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    SEASONAL_WINTER_COAT(
        "seasonal winter coat",
        "Insulation grown in response to the low-insolation portion of the year and shed as light returns.",
        listOf(
            TraitEffect.SeasonalColdTolerance(maximumBonusC = 18.0, triggerInsolation = 0.58),
            TraitEffect.TemperatureTolerance(hotterC = -1.5),
            TraitEffect.MaintenanceCost(0.07),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.HAIR_COVERING)),
        ),
    ),
    WATER_STORAGE_TISSUE(
        "water-storage tissue",
        "Specialized tissues that retain a usable water reserve through dry periods.",
        listOf(
            TraitEffect.WaterRequirement(-0.22),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.06),
        ),
        capabilities = setOf(TraitCapability.WATER_STORAGE),
    ),
    SNOW_AND_ICE_LICKING(
        "snow and ice licking",
        "The organism deliberately consumes snow or surface ice when liquid drinking water is unavailable.",
        listOf(
            TraitEffect.SnowHydration,
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    FOOD_DERIVED_WATER(
        "food-derived water",
        "Efficient kidneys and digestion obtain nearly all required water from moist food or metabolically produced water.",
        listOf(
            TraitEffect.WaterRequirement(-0.18),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    DEEP_ROOT_SYSTEM(
        "deep root system",
        "A long or extensively branching anchoring network that reaches water retained below the surface.",
        listOf(
            TraitEffect.WaterRequirement(-0.18),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.SUBSTRATE_ANCHORING)),
        ),
    ),
    SUCCULENT_STEM(
        "succulent stem",
        "A thick photosynthetic or supporting body that stores water through long dry intervals.",
        listOf(
            TraitEffect.WaterRequirement(-0.28),
            TraitEffect.ReproductionMultiplier(0.88),
            TraitEffect.MaintenanceCost(0.07),
        ),
        capabilities = setOf(TraitCapability.WATER_STORAGE),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.SUBSTRATE_ANCHORING)),
        ),
    ),
    WAXY_CUTICLE(
        "waxy cuticle",
        "A reflective, water-resistant outer surface limits evaporation and shields living tissue from intense heat.",
        listOf(
            TraitEffect.TemperatureTolerance(hotterC = 10.0),
            TraitEffect.WaterRequirement(-0.08),
            TraitEffect.CanopyLightEfficiency(-0.04),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    FROST_SENSITIVE_SUCCULENT_TISSUES(
        "frost-sensitive succulent tissues",
        "Large water-filled cells tolerate extreme heat and drought but are readily damaged when their fluids freeze.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -5.0, hotterC = 2.0),
            TraitEffect.MaintenanceCost(0.02),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.WATER_STORAGE)),
        ),
    ),
    DROUGHT_DECIDUOUS_LEAVES(
        "drought-deciduous leaves",
        "Photosynthetic surfaces are shed during dry seasons and regrown when water becomes available.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PHOTOSYNTHESIS, 1.0),
            TraitEffect.WaterRequirement(-0.12),
            TraitEffect.Dormancy(DormancyKind.DROUGHT_DECIDUOUS, 0.999),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.12),
        ),
        group = TraitGroup.PHOTOSYNTHETIC_STRUCTURE,
        capabilities = setOf(TraitCapability.PHOTOSYNTHETIC_TISSUE),
    ),
    SEASONAL_LEAF_DORMANCY(
        "seasonal leaf dormancy",
        "Growth and exposed foliage are withdrawn during the cold or dark season while protected living tissues persist.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.COLD_DARK_LEAF_DORMANCY, 0.999),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.04),
        ),
        group = TraitGroup.DORMANCY_MODE,
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.PHOTOSYNTHETIC_TISSUE)),
        ),
    ),
    FROST_HARDENED_TISSUES(
        "frost-hardened tissues",
        "Seasonal changes in cell fluids and exposed tissues reduce damage from freezing without shifting the organism's entire biochemistry.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 12.0, hotterC = -1.0),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    SALT_EXCLUDING_ROOTS(
        "salt-excluding roots",
        "Root membranes limit the uptake of dissolved salts while drawing water from coastal sediment.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.58),
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, -0.5),
            TraitEffect.ReproductionMultiplier(0.92),
            TraitEffect.MaintenanceCost(0.05),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.SUBSTRATE_ANCHORING)),
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
            TraitEffect.MaintenanceCost(0.08),
        ),
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
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    HEAT_STABLE_ENZYMES(
        "heat-stable enzymes",
        "Proteins and cell membranes remain functional through sustained hot conditions without shifting the organism's entire biochemical regime.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = -2.0, hotterC = 10.0),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    WARM_WATER_ENZYMES(
        "warm-water enzymes",
        "Metabolic enzymes and membranes remain stable and active in persistently warm water, at the cost of poor cold performance.",
        listOf(
            TraitEffect.TemperatureShift(5.0),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    FAT_RESERVES(
        "seasonal fat reserves",
        "Energy-dense tissues accumulated during abundance and consumed when intake later falls.",
        listOf(
            TraitEffect.ReserveCapacity(0.45),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    PERENNIAL_STORAGE_TISSUE(
        "perennial storage tissue",
        "Long-lived stems, roots, or analogous organs store energy across unfavorable seasons and rebuild active tissue later.",
        listOf(
            TraitEffect.ReserveCapacity(0.35),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    CACHED_FOOD(
        "cached food",
        "Surplus food is hidden or otherwise stored during abundance and recovered during later scarcity.",
        listOf(
            TraitEffect.ReserveCapacity(0.32),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.03),
        ),
    ),
    DESICCATION_RESISTANT_PROPAGULES(
        "desiccation-resistant propagules",
        "Seeds, spores, cysts, or other dispersal bodies that remain viable after losing most of their water.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.PROPAGULE, 0.97),
            TraitEffect.ReproductionMultiplier(0.90),
            TraitEffect.MaintenanceCost(0.03),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),
    BURROWING_EGGS(
        "burrowing eggs",
        "A seasonal lifecycle protected by placing resistant eggs or equivalent propagules below the surface.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.BURROWED_EGGS, 0.985),
            TraitEffect.ReproductionMultiplier(0.88),
            TraitEffect.MaintenanceCost(0.03),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),
    PROLONGED_JUVENILE_DORMANCY(
        "prolonged juvenile dormancy",
        "A slow-growing juvenile stage remains protected and minimally active within a substrate for many annual cycles before synchronized emergence.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.PROLONGED_JUVENILE, 0.998),
            TraitEffect.DormantReactivationMultiplier(3.0),
            TraitEffect.MetabolicDemandMultiplier(0.55),
            TraitEffect.ReproductionMultiplier(0.28),
            TraitEffect.MaintenanceCost(0.05),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),
    SEASONAL_TORPOR(
        "seasonal torpor",
        "A reversible low-activity state that sharply reduces ecological activity during an unfavorable season.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.SEASONAL_TORPOR, 0.99),
            TraitEffect.CaptureAbility(-0.05),
            TraitEffect.MaintenanceCost(0.04),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),
    WHOLE_BODY_ANHYDROBIOSIS(
        "whole-body anhydrobiosis",
        "The active organism can dry into a nearly ametabolic state and revive after water returns.",
        listOf(
            TraitEffect.Dormancy(DormancyKind.WHOLE_BODY_DESICCATION, 0.90),
            TraitEffect.ReproductionMultiplier(0.78),
            TraitEffect.MaintenanceCost(0.10),
        ),
        group = TraitGroup.DORMANCY_MODE,
    ),
    REEF_BUILDING(
        "reef-building growth",
        "Successive generations extend and bind a rigid colony framework until it becomes persistent three-dimensional aquatic habitat.",
        listOf(
            TraitEffect.ReefBuilding(0.08),
            TraitEffect.ReproductionMultiplier(0.90),
            TraitEffect.MaintenanceCost(0.09),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.RIGID_REEF_FRAMEWORK)),
        ),
    ),
    RIGID_COLONY_FRAMEWORK(
        "rigid colony framework",
        "The organism secretes or assembles a durable supporting framework that protects soft feeding bodies and persists after portions of the colony die.",
        listOf(
            TraitEffect.Defense(0.18),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.07),
        ),
        capabilities = setOf(TraitCapability.RIGID_REEF_FRAMEWORK),
    ),
    SHALLOW_WATER_PHOTOSYMBIOSIS(
        "shallow-water photosymbiosis",
        "Light-dependent symbionts nourish a sessile host anchored close to the illuminated seafloor.",
        listOf(
            TraitEffect.WaterDepthTolerance(
                optimalMaximumM = 30.0,
                absoluteMaximumM = 80.0,
            ),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    REEF_NESTING(
        "reef nesting",
        "Reproduction or shelter depends on cavities and protected surfaces within an aquatic reef.",
        listOf(
            TraitEffect.ReefUse(0.75),
            TraitEffect.Defense(0.08),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    REEF_CAMOUFLAGE(
        "reef camouflage",
        "Color, texture, and body outline resemble the varied surfaces of an aquatic reef.",
        listOf(
            TraitEffect.ReefUse(0.33),
            TraitEffect.Camouflage(Habitat.COASTAL, 0.18),
            TraitEffect.Camouflage(Habitat.SUNLIT_WATER, 0.18),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    REEF_SHELTER_DEPENDENCE(
        "reef shelter dependence",
        "Feeding, refuge, and daily activity depend on the dense cavities and broken sight-lines of a living reef.",
        listOf(
            TraitEffect.ReefUse(1.0),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    REEF_BORING(
        "reef-boring mouthparts",
        "Hard scraping or drilling structures open cavities and expose food within reef material.",
        listOf(
            TraitEffect.ReefUse(0.75),
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.28),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    GLIDING_MEMBRANE(
        "gliding membrane",
        "A broad skin membrane or flattened body turns height and forward speed into controlled unpowered flight.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.34),
            TraitEffect.HabitatSupport(Habitat.AERIAL, 0.18),
            TraitEffect.PursuitSpeed(0.08),
            TraitEffect.Defense(0.05),
            TraitEffect.WaterRequirement(0.02),
            TraitEffect.MaintenanceCost(0.07),
        ),
        group = TraitGroup.FLIGHT_STRUCTURE,
        requirements = listOf(
            TraitRequirement.AnyOf(
                setOf(
                    TraitCapability.ARBOREAL_LOCOMOTION,
                    TraitCapability.AQUATIC_LOCOMOTION,
                ),
            ),
        ),
    ),
    WEB_SILK(
        "prey-catching silk web",
        "Strong adhesive fibers are arranged into traps that intercept moving prey.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.58),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.18),
            TraitEffect.CaptureAbility(0.24),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    VENOM_DELIVERY(
        "venom delivery",
        "Fangs, stingers, spines, or saliva introduce toxins that rapidly disable prey.",
        listOf(
            TraitEffect.CaptureAbility(0.24),
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(0.07),
            TraitEffect.Defense(0.45)
        ),
    ),
    CONSTRICTING_BODY(
        "constricting body",
        "A long muscular body coils around captured prey and prevents breathing or circulation.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.24),
            TraitEffect.CaptureAbility(0.20),
            TraitEffect.MaintenanceCost(0.08),
        ),
    ),
    HOOKED_TALONS(
        "hooked talons",
        "Curved claws seize, carry, and kill struggling prey.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.12),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    ECHOLOCATION(
        "echolocation",
        "The organism emits sound and reconstructs nearby surfaces and moving prey from returning echoes.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.DARK_WATER, 0.12),
            TraitEffect.CaptureAbility(0.20),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    ELECTRORECEPTION(
        "electroreception",
        "Sensitive organs detect the weak electrical fields produced by hidden or moving organisms in water.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.10),
            TraitEffect.HabitatSupport(Habitat.DARK_WATER, 0.16),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    ELECTRIC_ORGAN(
        "electric organ",
        "Stacks of specialized cells release coordinated electrical discharges that can stun prey, discourage predators, or communicate through opaque water.",
        listOf(
            TraitEffect.CaptureAbility(0.20),
            TraitEffect.Defense(0.20),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.12),
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
            TraitEffect.MaintenanceCost(0.08),
        ),
        group = TraitGroup.DOMINANT_BODY_COVERING
    ),
    REINFORCED_HIDE(
        "reinforced hide",
        "Dense, unusually tough skin beneath a fur coat resists tearing, punctures, and twisting bites without forming rigid armor.",
        listOf(
            TraitEffect.Defense(0.18),
            TraitEffect.CaptureAbility(-0.02),
            TraitEffect.ReproductionMultiplier(0.97),
            TraitEffect.MaintenanceCost(0.05),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.HAIR_COVERING)),
        ),
    ),
    PROTECTIVE_SHELL(
        "protective shell",
        "A rigid external shell encloses vulnerable tissues and can withstand crushing or abrasion.",
        listOf(
            TraitEffect.Defense(0.46),
            TraitEffect.CaptureAbility(-0.14),
            TraitEffect.ReproductionMultiplier(0.86),
            TraitEffect.MaintenanceCost(0.10),
        ),
    ),
    QUILLS(
        "defensive quills",
        "Long rigid hairs or spines make biting and grappling dangerous.",
        listOf(
            TraitEffect.Defense(0.32),
            TraitEffect.CaptureAbility(-0.08),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    TOXIC_SKIN(
        "toxic skin",
        "Skin glands or accumulated compounds make the organism poisonous or intensely distasteful.",
        listOf(
            TraitEffect.Defense(0.28),
            TraitEffect.ReproductionMultiplier(0.93),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    INK_CLOUD(
        "defensive ink cloud",
        "A released cloud obscures vision and confuses chemical senses during escape.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.10),
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.10),
            TraitEffect.Defense(0.22),
            TraitEffect.MaintenanceCost(0.05),
        ),
    ),
    JET_PROPULSION(
        "jet propulsion",
        "Water is forcefully expelled from a muscular chamber for rapid acceleration and maneuvering.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.26),
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.30),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.12),
        ),
        capabilities = setOf(TraitCapability.AQUATIC_LOCOMOTION),
    ),
    GRASPING_TENTACLES(
        "grasping tentacles",
        "Flexible muscular appendages explore crevices and restrain several prey items at once.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.16),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.12),
            TraitEffect.MaintenanceCost(0.08),
        ),
    ),
    BIOLUMINESCENT_LURE(
        "bioluminescent lure",
        "A controlled light organ attracts curious prey in otherwise dark water.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.DARK_WATER, 0.40),
            TraitEffect.DarkWaterAdaptation,
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.24),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    RUMINANT_STOMACH(
        "ruminant stomach",
        "Several fermentation chambers and repeated chewing extract energy from fibrous photosynthetic tissue.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.20),
            TraitEffect.ReserveCapacity(0.10),
            TraitEffect.ReproductionMultiplier(0.95),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.GUT_FERMENTATION,
    ),
    FERMENTING_HINDGUT(
        "fermenting hindgut",
        "A large microbe-rich hindgut digests cellulose after food has passed through the stomach.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.16),
            TraitEffect.ReserveCapacity(0.07),
            TraitEffect.MaintenanceCost(0.05),
        ),
        group = TraitGroup.GUT_FERMENTATION,
    ),
    SEED_CRACKING_MOUTHPARTS(
        "seed-cracking mouthparts",
        "Deep reinforced jaws or a stout beak crush hard seeds and nuts from ground and canopy producers.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.54),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.16),
            TraitEffect.CaptureAbility(0.04),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    NECTAR_SIPPING_TONGUE(
        "nectar-sipping tongue",
        "An elongated tongue or proboscis reaches energy-rich secretions within elevated reproductive structures.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.NECTAR_FEEDING, 0.70),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.24),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.04),
        ),
        group = TraitGroup.SPECIALIZED_TONGUE,
        capabilities = setOf(TraitCapability.NECTAR_FEEDING),
    ),
    POLLEN_CARRYING_SURFACES(
        "pollen-carrying surfaces",
        "Branched hairs, scales, feathers, or other textured body surfaces retain pollen while an animal moves among flowers.",
        listOf(
            TraitEffect.PollinationEfficiency(0.70),
            TraitEffect.CaptureAbility(-0.02),
            TraitEffect.MaintenanceCost(0.03),
        ),
    ),
    APOSEMATIC_COLORATION(
        "aposematic coloration",
        "Conspicuous colors advertise a dangerous or distasteful organism—or mimic another local organism carrying the same warning colors.",
        listOf(
            TraitEffect.AposematicColoration,
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    RAPID_GROWTH(
        "rapid growth",
        "Exceptionally fast production of new shoots and tissues allows an organism to replace losses and spread quickly when conditions are favorable.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.75),
            TraitEffect.MaintenanceCost(0.35),
        ),
    ),
    COLONY_PROBING_TONGUE(
        "colony-probing tongue",
        "An extremely elongated adhesive tongue reaches minuscule colonial prey through narrow passages in opened nests.",
        listOf(
            TraitEffect.CaptureAbility(0.28),
            TraitEffect.MaintenanceCost(0.06),
        ),
        group = TraitGroup.SPECIALIZED_TONGUE,
    ),
    PROJECTILE_TONGUE(
        "projectile tongue",
        "A rapidly projected adhesive tongue lets a stationary hunter seize small moving prey before it can escape.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.62),
            TraitEffect.CaptureAbility(0.22),
            TraitEffect.MaintenanceCost(0.07),
        ),
        group = TraitGroup.SPECIALIZED_TONGUE,
    ),
    SUCKING_PROBOSCIS(
        "sucking proboscis",
        "A narrow piercing mouthpart taps fluids from the living tissues of a host organism.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PARASITISM, 0.58),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.18),
            TraitEffect.CaptureAbility(-0.04),
            TraitEffect.MaintenanceCost(0.04),
        ),
    ),
    LONG_NECK(
        "long browsing neck",
        "An elongated neck reaches foliage beyond the feeding height of most ground animals.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.42),
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.10),
            TraitEffect.WaterRequirement(0.04),
            TraitEffect.MaintenanceCost(0.09),
        ),
    ),
    PREHENSILE_TRUNK(
        "prehensile trunk",
        "A muscular mobile appendage manipulates branches, uproots food, and draws water without lowering the whole body.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.18),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.WaterRequirement(0.03),
            TraitEffect.MaintenanceCost(0.08),
        ),
    ),
    COOPERATIVE_HUNTING(
        "cooperative hunting",
        "Several individuals coordinate pursuit, encirclement, or ambush rather than attacking independently.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.12),
            TraitEffect.StrategySupport(EcoStrategy.PURSUIT_PREDATION, 0.18),
            TraitEffect.CaptureAbility(0.18),
            TraitEffect.LargerPreySizeClasses(1),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.08),
        ),
    ),
    COLONY_LIVING(
        "defended social colony",
        "Many related individuals share shelter, defense, and food information in a persistent colony.",
        listOf(
            TraitEffect.Defense(0.18),
            TraitEffect.ReproductionMultiplier(1.08),
            TraitEffect.MaintenanceCost(0.09),
        ),
        capabilities = setOf(TraitCapability.SOCIAL_COLONY),
    ),
    COLONY_THERMOREGULATION(
        "colony thermoregulation",
        "Workers cluster and generate metabolic heat in winter, then fan and evaporate water to cool the shared nest during hot weather.",
        listOf(
            TraitEffect.SeasonalColdTolerance(maximumBonusC = 24.0, triggerInsolation = 0.62),
            TraitEffect.TemperatureTolerance(hotterC = 5.0),
            TraitEffect.WaterRequirement(0.04),
            TraitEffect.ReproductionMultiplier(0.96),
            TraitEffect.MaintenanceCost(0.14),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.SOCIAL_COLONY)),
        ),
    ),
    HONEY_STORES(
        "communal honey stores",
        "Workers concentrate floral sugars into stable comb stores that feed the colony through winter or other seasonal shortages.",
        listOf(
            TraitEffect.ReserveCapacity(0.90),
            TraitEffect.ReproductionMultiplier(0.93),
            TraitEffect.MaintenanceCost(0.05),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(TraitCapability.SOCIAL_COLONY, TraitCapability.NECTAR_FEEDING),
            ),
        ),
    ),
    OPEN_COUNTRY_PREFERENCE(
        "open-country preference",
        "Foraging, escape, and sensory behavior depend on long sight lines and unobstructed movement through sparse vegetation.",
        listOf(
            TraitEffect.DenseCanopyForagingPenalty(0.82),
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.06),
            TraitEffect.MaintenanceCost(0.02),
        ),
    ),
    HERDING_BEHAVIOR(
        "herding behavior",
        "Individuals maintain cohesive social groups that share vigilance and coordinate travel or defense.",
        listOf(
            TraitEffect.Defense(0.08),
            TraitEffect.ReproductionMultiplier(0.99),
            TraitEffect.MaintenanceCost(0.03),
        ),
    ),
    WHALESONG(
        "whalesong",
        "Long, structured sequences of low-frequency calls carry between distant individuals through open water.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.04),
            TraitEffect.MaintenanceCost(0.05),
        ),
        acousticSignal = AcousticSignal.WHALESONG,
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.AQUATIC_LOCOMOTION))
        )
    ),
    HOWLING_CALL(
        "howling call",
        "Long-range group calls coordinate dispersed pack members and advertise an occupied range.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PURSUIT_PREDATION, 0.05),
            TraitEffect.Defense(0.03),
            TraitEffect.MaintenanceCost(0.05),
        ),
        acousticSignal = AcousticSignal.HOWL,
    ),
    BIRDSONG(
        "birdsong",
        "Learned or innately complex sequences communicate identity, territory, and reproductive quality.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.04),
            TraitEffect.MaintenanceCost(0.04),
        ),
        acousticSignal = AcousticSignal.BIRDSONG,
        requirements = listOf(
            TraitRequirement.AllOf(
                setOf(
                    TraitCapability.FEATHER_COVERING,
                    TraitCapability.CHIRPING_VOCALIZATION,
                ),
            ),
        ),
    ),
    CICADA_CHORUS(
        "cicada chorus",
        "Synchronized, high-intensity mating calls allow widely scattered adults to find one another during a short emergence.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.07),
            TraitEffect.MaintenanceCost(0.05),
        ),
        acousticSignal = AcousticSignal.CICADA_CHORUS,
    ),
    RATTLING_WARNING(
        "rattling warning",
        "A specialized vibrating structure produces a conspicuous warning that discourages accidental encounters with large animals.",
        listOf(
            TraitEffect.Defense(0.10),
            TraitEffect.ReproductionMultiplier(0.98),
            TraitEffect.MaintenanceCost(0.03),
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
    CHIRPING_CALL(
        "chirping call",
        "Short high-pitched calls maintain contact between companions, parents, and young.",
        emptyList(),
        isCosmetic = true,
        acousticSignal = AcousticSignal.CHIRP,
        capabilities = setOf(TraitCapability.CHIRPING_VOCALIZATION)
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
            TraitEffect.MaintenanceCost(0.04),
        ),
        acousticSignal = AcousticSignal.BOOM,
    ),
    DRUMMING_DISPLAY(
        "drumming display",
        "Repeated impacts against a resonant surface create a long-range territorial and courtship signal.",
        listOf(
            TraitEffect.ReproductionMultiplier(1.02),
            TraitEffect.MaintenanceCost(0.02),
        ),
    ),
    WHOOPING_CALL(
        "whooping call",
        "Long-range whoops recruit and coordinate members of a dispersed social hunting group.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.PURSUIT_PREDATION, 0.03),
            TraitEffect.Defense(0.02),
            TraitEffect.MaintenanceCost(0.04),
        ),
        acousticSignal = AcousticSignal.WHOOP,
    ),
    CLICK_WHISTLE_REPERTOIRE(
        "click-and-whistle repertoire",
        "Individually distinctive whistles and patterned clicks coordinate a complex mobile social group.",
        listOf(
            TraitEffect.Defense(0.03),
            TraitEffect.ReproductionMultiplier(1.02),
            TraitEffect.MaintenanceCost(0.04),
        ),
        acousticSignal = AcousticSignal.CLICK_WHISTLE,
    ),
    SOUND_LURES(
        "sound lures",
        "Familiar calls are imitated or repurposed to draw acoustically responsive prey into striking distance.",
        listOf(
            TraitEffect.SoundLureCaptureBonus(0.22),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(TraitRequirement.HasAcousticSignal),
    ),
    DAM_BUILDING(
        "dam-building behavior",
        "Coordinated cutting, digging, and placement of durable material impounds flowing water and maintains a protected freshwater home range.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.FRESHWATER, 0.32),
            TraitEffect.Defense(0.06),
            TraitEffect.ReserveCapacity(0.08),
            TraitEffect.NicheCompetitionSensitivity(0.82),
            TraitEffect.ReproductionMultiplier(0.94),
            TraitEffect.MaintenanceCost(0.11),
        ),
    ),
    SCHOOLING(
        "coordinated schooling",
        "Many mobile aquatic organisms maintain synchronized spacing and direction, confusing attackers while sharing information about threats and food.",
        listOf(
            TraitEffect.Defense(0.15),
            TraitEffect.PursuitSpeed(0.05),
            TraitEffect.NicheCompetitionSensitivity(1.12),
            TraitEffect.MaintenanceCost(0.06),
        ),
        requirements = listOf(
            TraitRequirement.AllOf(setOf(TraitCapability.AQUATIC_LOCOMOTION)),
        ),
    ),
    WATERPROOF_PLUMAGE(
        "waterproof plumage",
        "Overlapping oiled feathers retain insulating air and shed water during swimming and rain.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.34),
            TraitEffect.HabitatSupport(Habitat.FRESHWATER, 0.30),
            TraitEffect.TemperatureTolerance(colderC = 2.0),
            TraitEffect.MaintenanceCost(0.07),
        ),
        requirements = listOf(TraitRequirement.AllOf(setOf(TraitCapability.FEATHER_COVERING))),
    ),
    DIGGING_CLAWS(
        "digging claws",
        "Broad reinforced claws rapidly excavate soil, tear apart nests, and expose concealed food.",
        listOf(
            TraitEffect.TemperatureTolerance(colderC = 2.0, hotterC = 2.0),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.WaterRequirement(-0.04),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    LEAPING_LEGS(
        "powerful leaping legs",
        "Elongated spring-like limbs cross obstacles and produce abrupt escapes or attacks.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.LAND_SURFACE, 0.18),
            TraitEffect.HabitatSupport(Habitat.CANOPY, 0.14),
            TraitEffect.CaptureAbility(0.08),
            TraitEffect.MaintenanceCost(0.08),
        ),
    ),
    TOOL_MANIPULATION(
        "tool manipulation",
        "Dexterous appendages manipulate stones, sticks, containers, or other objects to obtain defended food.",
        listOf(
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.08),
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.08),
            TraitEffect.CaptureAbility(0.14),
            TraitEffect.MaintenanceCost(0.10),
        ),
    ),
    CRUSHING_CLAWS(
        "crushing claws",
        "Opposed hardened claws crack shells, cut plant tissue, and restrain struggling prey.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.18),
            TraitEffect.StrategySupport(EcoStrategy.GRAZING, 0.10),
            TraitEffect.StrategySupport(EcoStrategy.AMBUSH_PREDATION, 0.12),
            TraitEffect.CaptureAbility(0.12),
            TraitEffect.MaintenanceCost(0.07),
        ),
    ),
    SUCTION_CUPS(
        "gripping suction cups",
        "Pressure-sealing discs attach to rock, prey, and other bodies under water.",
        listOf(
            TraitEffect.HabitatSupport(Habitat.COASTAL, 0.24),
            TraitEffect.HabitatSupport(Habitat.SUNLIT_WATER, 0.14),
            TraitEffect.Defense(0.08),
            TraitEffect.CaptureAbility(0.10),
            TraitEffect.MaintenanceCost(0.06),
        ),
    ),
    NEIGHBOR_DISPERSAL(
        "neighboring-range dispersal",
        "Individuals or propagules routinely spread into nearby suitable territory without a fixed seasonal destination.",
        listOf(
            TraitEffect.Dispersal(DispersalKind.NEIGHBOR),
            TraitEffect.MaintenanceCost(0.03),
        ),
        group = TraitGroup.DISPERSAL_RANGE,
    ),
    SHORT_MIGRATION(
        "short seasonal migration",
        "A recurring seasonal movement between nearby ranges with learned or inherited destinations.",
        listOf(
            TraitEffect.Dispersal(DispersalKind.SHORT_MIGRATION),
            TraitEffect.ReserveCapacity(0.10),
            TraitEffect.MaintenanceCost(0.08),
        ),
        group = TraitGroup.DISPERSAL_RANGE,
    ),
    REGIONAL_MIGRATION(
        "regional seasonal migration",
        "A recurring seasonal journey between ranges separated across a substantial region.",
        listOf(
            TraitEffect.Dispersal(DispersalKind.REGIONAL_MIGRATION),
            TraitEffect.ReserveCapacity(0.18),
            TraitEffect.MaintenanceCost(0.13),
        ),
        group = TraitGroup.DISPERSAL_RANGE,
    ),
    LONG_MIGRATION(
        "long-distance seasonal migration",
        "A recurring seasonal journey linking widely separated parts of a planet.",
        listOf(
            TraitEffect.Dispersal(DispersalKind.LONG_MIGRATION),
            TraitEffect.ReserveCapacity(0.28),
            TraitEffect.MaintenanceCost(0.04),
        ),
        group = TraitGroup.DISPERSAL_RANGE,
    ),
}