package dev.biserman.planet.planet.ecology

import kotlin.math.exp
import kotlin.math.ln

/**
 * Derives vertical light interception from the size of active photosynthetic
 * populations. Larger producers filter light before it reaches smaller ones.
 */
object PhotosyntheticStructure {
    private const val CLOSURE_PER_NORMALIZED_BIOMASS = 3.0
    private val minimumOverheadSize = SizeClass.LARGE.ordinal

    fun opticalDepthBySize(
        ecology: CompiledEcology,
        community: TileCommunity,
        environment: SeasonalCellEnvironment,
    ): DoubleArray {
        val opticalDepth = DoubleArray(SizeClass.entries.size)
        if (!environment.isLand) return opticalDepth

        repeat(community.size) { populationIndex ->
            val species = ecology.species[community.speciesIndices[populationIndex]]
            val niche = ecology.niches[community.nicheIndices[populationIndex]]
            if (
                niche.strategy != EcoStrategy.PHOTOSYNTHESIS ||
                niche.habitat !in terrestrialProducerHabitats
            ) {
                return@repeat
            }
            val referenceBiomass =
                environment.areaKm2 *
                    EcologyBiomass.terrestrialProducerDensityKgKm2.getValue(species.sizeClass)
            val normalizedBiomass = community.activeBiomass[populationIndex] / referenceBiomass
            opticalDepth[species.sizeClass.ordinal] +=
                normalizedBiomass.coerceAtLeast(0.0) * CLOSURE_PER_NORMALIZED_BIOMASS
        }

        capOverheadOpticalDepth(opticalDepth, environment.canopyPotential)
        return opticalDepth
    }

    fun overheadCover(opticalDepthBySize: DoubleArray): Double {
        val overheadOpticalDepth =
            (minimumOverheadSize until opticalDepthBySize.size)
                .sumOf { opticalDepthBySize[it] }
        return (1.0 - exp(-overheadOpticalDepth)).coerceIn(0.0, 1.0)
    }

    private fun capOverheadOpticalDepth(
        opticalDepthBySize: DoubleArray,
        climateCanopyPotential: Double,
    ) {
        val current =
            (minimumOverheadSize until opticalDepthBySize.size)
                .sumOf { opticalDepthBySize[it] }
        if (current <= 0.0) return
        val maximum = -ln(1.0 - climateCanopyPotential.coerceIn(0.0, 0.999999))
        if (current <= maximum) return
        val scale = maximum / current
        for (size in minimumOverheadSize until opticalDepthBySize.size) {
            opticalDepthBySize[size] *= scale
        }
    }

    private val terrestrialProducerHabitats = setOf(Habitat.LAND_SURFACE, Habitat.CANOPY)
}
