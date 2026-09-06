package dev.biserman.planet.planet.ecology

interface HabitatSelection {
    val habitats: List<Pair<Habitat, Double>>

    val accessCapabilities: Set<TraitCapability>
        get() = habitats.mapTo(linkedSetOf()) { it.first.accessCapability }
}

private val AERIAL_RESPIRATION = setOf(TraitCapability.AERIAL_RESPIRATION)
private val FRESHWATER_TOLERANCE = setOf(
    TraitCapability.FRESHWATER_OSMOREGULATION,
    TraitCapability.EURYHALINE_OSMOREGULATION,
)
private val SALTWATER_TOLERANCE = setOf(
    TraitCapability.SALTWATER_OSMOREGULATION,
    TraitCapability.EURYHALINE_OSMOREGULATION,
)
private val AQUATIC_OR_AERIAL_RESPIRATION = setOf(
    TraitCapability.UNDERWATER_RESPIRATION,
    TraitCapability.AERIAL_RESPIRATION,
)
private val AQUATIC_OR_BREATH_HOLDING = setOf(
    TraitCapability.UNDERWATER_RESPIRATION,
    TraitCapability.PROLONGED_BREATH_HOLDING,
)

enum class HabitatGroup(override val habitats: List<Pair<Habitat, Double>>) : HabitatSelection {
    LAND(
        listOf(
            Habitat.LAND_SURFACE to 1.0,

            Habitat.SEA_ICE to 0.5,
            Habitat.COASTAL to 0.5
        )
    ),
    AQUATIC(
        listOf(
            Habitat.FRESHWATER to 1.0,
            Habitat.SHALLOW_OCEAN to 1.0,
            Habitat.OPEN_OCEAN to 1.0,
            Habitat.DARK_WATER to 1.0,

            Habitat.COASTAL to 0.5,
            Habitat.CAVE to 0.5
        )
    ),
    FRESHWATER(
        listOf(
            Habitat.FRESHWATER to 1.0,

            Habitat.CAVE to 0.5
        )
    ),
    SALTWATER(
        listOf(
            Habitat.SHALLOW_OCEAN to 1.0,
            Habitat.OPEN_OCEAN to 1.0,
            Habitat.DARK_WATER to 1.0,

            Habitat.COASTAL to 0.5
        )
    ),
    BRIGHT(
        listOf(
            Habitat.SHALLOW_OCEAN to 1.0,
            Habitat.OPEN_OCEAN to 1.0,
            Habitat.LAND_SURFACE to 1.0,
            Habitat.COASTAL to 1.0,
            Habitat.FRESHWATER to 1.0,
            Habitat.CANOPY to 1.0,
        )
    ),
    DARK(
        listOf(
            Habitat.DARK_WATER to 1.0,
            Habitat.CAVE to 1.0,
        )
    ),
    CLIMBING(
        listOf(
            Habitat.CANOPY to 1.0,
        )
    ),
    WALKING(
        listOf(
            Habitat.LAND_SURFACE to 1.0,

            Habitat.CAVE to 0.5,

            Habitat.COASTAL to 0.1,
            Habitat.FRESHWATER to 0.1,
            Habitat.SHALLOW_OCEAN to 0.1,
            Habitat.DARK_WATER to 0.1
        )
    ),
    FLYING(
        listOf(
            Habitat.LAND_SURFACE to 1.0,
            Habitat.SEA_ICE to 1.0,
            Habitat.CANOPY to 1.0,

            Habitat.AERIAL to 0.5
        )
    ),
    AERIAL(
        listOf(
            Habitat.AERIAL to 1.0,

            Habitat.CANOPY to 0.5
        )
    )
}

/**
 * Author-facing ecology vocabulary. Runtime code compiles these values into
 * primitive arrays and never walks trait objects during a seasonal turn.
 */
enum class Habitat(
    val displayName: String,
    val aquatic: Boolean,
    val accessCapability: TraitCapability,
    /** Every set is an OR-clause; every clause must be fulfilled. */
    val capabilityRequirements: List<Set<TraitCapability>>,
) : HabitatSelection {
    LAND_SURFACE(
        "land-surface",
        false,
        TraitCapability.LAND_SURFACE_ACCESS,
        listOf(setOf(TraitCapability.LAND_SURFACE_ACCESS), AERIAL_RESPIRATION),
    ),
    CANOPY(
        "canopy",
        false,
        TraitCapability.CANOPY_ACCESS,
        listOf(setOf(TraitCapability.CANOPY_ACCESS), AERIAL_RESPIRATION),
    ),
    FRESHWATER(
        "freshwater",
        true,
        TraitCapability.FRESHWATER_ACCESS,
        listOf(
            setOf(TraitCapability.LAND_SURFACE_ACCESS, TraitCapability.FRESHWATER_ACCESS),
            AQUATIC_OR_AERIAL_RESPIRATION,
            AERIAL_RESPIRATION + FRESHWATER_TOLERANCE,
        ),
    ),
    COASTAL(
        "coastal",
        true,
        TraitCapability.COASTAL_ACCESS,
        listOf(
            setOf(
                TraitCapability.LAND_SURFACE_ACCESS,
                TraitCapability.COASTAL_ACCESS,
                TraitCapability.SHALLOW_OCEAN_ACCESS,
                TraitCapability.OPEN_OCEAN_ACCESS,
            ),
            AQUATIC_OR_AERIAL_RESPIRATION,
            AERIAL_RESPIRATION + SALTWATER_TOLERANCE,
        ),
    ),
    SHALLOW_OCEAN(
        "shallow-ocean",
        true,
        TraitCapability.SHALLOW_OCEAN_ACCESS,
        listOf(
            setOf(TraitCapability.SHALLOW_OCEAN_ACCESS),
            AQUATIC_OR_AERIAL_RESPIRATION,
            AQUATIC_OR_BREATH_HOLDING,
            SALTWATER_TOLERANCE,
        ),
    ),
    OPEN_OCEAN(
        "open-ocean",
        true,
        TraitCapability.OPEN_OCEAN_ACCESS,
        listOf(
            setOf(TraitCapability.OPEN_OCEAN_ACCESS),
            AQUATIC_OR_AERIAL_RESPIRATION,
            AQUATIC_OR_BREATH_HOLDING,
            SALTWATER_TOLERANCE,
        ),
    ),
    DARK_WATER(
        "dark-water",
        true,
        TraitCapability.DARK_WATER_ACCESS,
        listOf(
            setOf(TraitCapability.DARK_WATER_ACCESS),
            AQUATIC_OR_AERIAL_RESPIRATION,
            AQUATIC_OR_BREATH_HOLDING,
            SALTWATER_TOLERANCE,
            setOf(TraitCapability.DEEP_WATER_ADAPTATION),
        ),
    ),
    SEA_ICE(
        "sea-ice",
        false,
        TraitCapability.SEA_ICE_ACCESS,
        listOf(
            setOf(
                TraitCapability.LAND_SURFACE_ACCESS,
                TraitCapability.SEA_ICE_ACCESS,
                TraitCapability.COASTAL_ACCESS,
                TraitCapability.SHALLOW_OCEAN_ACCESS,
                TraitCapability.OPEN_OCEAN_ACCESS,
            ),
            AQUATIC_OR_AERIAL_RESPIRATION,
            AERIAL_RESPIRATION + SALTWATER_TOLERANCE,
        ),
    ),
    AERIAL(
        "aerial",
        false,
        TraitCapability.AERIAL_ACCESS,
        listOf(setOf(TraitCapability.AERIAL_ACCESS), AERIAL_RESPIRATION),
    ),
    CAVE(
        "cave",
        false,
        TraitCapability.CAVE_ACCESS,
        listOf(setOf(TraitCapability.CAVE_ACCESS), AERIAL_RESPIRATION),
    ),
    UNDERGROUND(
        "underground",
        false,
        TraitCapability.UNDERGROUND_ACCESS,
        listOf(setOf(TraitCapability.UNDERGROUND_ACCESS), AERIAL_RESPIRATION),
    ),
    ;

    override val habitats: List<Pair<Habitat, Double>> = listOf(this to 1.0)

    fun isAccessibleBy(capabilities: Set<TraitCapability>): Boolean =
        capabilityRequirements.all { clause -> clause.any(capabilities::contains) }

    fun availableLight(insolation: Double, canopyCover: Double): Double = when (this) {
        CANOPY, SEA_ICE, AERIAL -> insolation
        LAND_SURFACE -> insolation * (1.0 - canopyCover * 0.72)
        COASTAL, FRESHWATER, SHALLOW_OCEAN, OPEN_OCEAN -> insolation * (1.0 - canopyCover * 0.15)
        DARK_WATER, CAVE, UNDERGROUND -> 0.0
    }.coerceIn(0.0, 1.0)

    fun camouflageMatch(
        color: BiologicalColor?,
        snowOrIce: Boolean,
        canopyCover: Double,
        reefCover: Double,
    ): Double {
        if (color == null) return 0.0
        if (color == BiologicalColor.ADAPTIVE) return 0.35
        if (color == BiologicalColor.RAINBOW) return 0.0
        if (snowOrIce && color == BiologicalColor.WHITE) return 0.35
        if (aquatic && reefCover > 0.45) {
            return when (color) {
                BiologicalColor.BROWN, BiologicalColor.GREEN, BiologicalColor.PURPLE, BiologicalColor.BLUE, BiologicalColor.RED -> 0.24
                else -> 0.08
            }
        }
        return when (this) {
            CANOPY ->
                when (color) {
                    BiologicalColor.GREEN, BiologicalColor.BROWN -> 0.28
                    else -> 0.05
                }

            LAND_SURFACE -> when (color) {
                BiologicalColor.BROWN -> 0.24
                BiologicalColor.PALE -> if (canopyCover < 0.35) 0.2 else 0.0
                BiologicalColor.YELLOW -> if (canopyCover < 0.35) 0.25 else 0.1
                BiologicalColor.GREEN -> if (canopyCover > 0.2) 0.2 else 0.15
                BiologicalColor.WHITE -> if (canopyCover < 0.35) 0.1 else 0.0
                else -> 0.05
            }

            SEA_ICE -> when {
                color == BiologicalColor.WHITE -> 0.20
                else -> 0.05
            }

            FRESHWATER, COASTAL, SHALLOW_OCEAN, OPEN_OCEAN ->
                when (color) {
                    BiologicalColor.COUNTERSHADE -> 0.30
                    BiologicalColor.BLUE -> 0.20
                    else -> 0.05
                }

            DARK_WATER, CAVE ->
                when (color) {
                    BiologicalColor.BLACK -> 0.1
                    BiologicalColor.BLUE -> 0.075
                    BiologicalColor.BROWN -> 0.075
                    else -> 0.05
                }

            UNDERGROUND ->
                when (color) {
                    BiologicalColor.BROWN -> 0.075
                    else -> 0.05
                }

            AERIAL ->
                when (color) {
                    BiologicalColor.WHITE -> 0.24
                    BiologicalColor.BLUE -> 0.18
                    else -> 0.04
                }
        }
    }
}
