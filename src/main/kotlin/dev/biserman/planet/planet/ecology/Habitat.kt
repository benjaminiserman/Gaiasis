package dev.biserman.planet.planet.ecology

interface HabitatSelection {
    val habitats: List<Pair<Habitat, Double>>
}

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
) : HabitatSelection {
    LAND_SURFACE("land-surface", false),
    CANOPY("canopy", false),
    FRESHWATER("freshwater", true),
    COASTAL("coastal", true),
    SHALLOW_OCEAN("shallow-ocean", true),
    OPEN_OCEAN("open-ocean", true),
    DARK_WATER("dark-water", true),
    SEA_ICE("sea-ice", false),
    AERIAL("aerial", false),
    CAVE("cave", false),
    UNDERGROUND("underground", false),
    ;

    override val habitats: List<Pair<Habitat, Double>> = listOf(this to 1.0)

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
