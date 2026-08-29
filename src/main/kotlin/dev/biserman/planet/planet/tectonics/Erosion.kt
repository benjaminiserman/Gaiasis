package dev.biserman.planet.planet.tectonics

import dev.biserman.planet.geometry.scaleAndCoerceIn
import dev.biserman.planet.planet.Planet
import dev.biserman.planet.planet.tectonics.TectonicGlobals.depositLoss
import dev.biserman.planet.planet.tectonics.TectonicGlobals.depositMultiplier
import dev.biserman.planet.planet.tectonics.TectonicGlobals.depositStrength
import dev.biserman.planet.planet.tectonics.TectonicGlobals.depositionStartHeight
import dev.biserman.planet.planet.tectonics.TectonicGlobals.desiredLandScalePow
import dev.biserman.planet.planet.tectonics.TectonicGlobals.elevationErosion
import dev.biserman.planet.planet.tectonics.TectonicGlobals.maxErosionProportion
import dev.biserman.planet.planet.tectonics.TectonicGlobals.prominenceErosion
import dev.biserman.planet.planet.tectonics.TectonicGlobals.waterErosion
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

object Erosion {
    fun performErosion(planet: Planet) {
        val deposits = planet.planetTiles.values.associateWith { 0.0 }.toMutableMap()
        val waterFlow = planet.planetTiles.values.associateWith { 1.0 }.toMutableMap()
        val currentLandPercent =
            planet.planetTiles.values.count { it.isAboveWater }.toDouble() / planet.planetTiles.size
        val landPercentDepositScale =
            (TectonicRuntimeConfig.desiredLandPercent.coerceIn(0.0, 1.0) / currentLandPercent.coerceIn(0.01, 1.0))
                .pow(desiredLandScalePow)
                .coerceIn(0.25, 4.0)
        val effectiveDepositMultiplier = depositMultiplier * landPercentDepositScale

        for (planetTile in planet.planetTiles.values.sortedByDescending { it.elevation }) {
            val originalElevation = planetTile.elevation
            val prominenceScale = planetTile.prominence.scaleAndCoerceIn(0.0..1000.0, 0.0..1.0)
            val surroundingAverageElevation = planetTile.neighbors.map { it.elevation }.average()
            val deposit = deposits[planetTile]!!
            val water = waterFlow[planetTile]!!
            val depositTaken =
                if (planetTile.elevation <= depositionStartHeight) {
                    deposit * depositStrength * (1 - prominenceScale).pow(3)
                } else {
                    0.0
                }
            planetTile.elevation += (depositTaken * (1 - depositLoss))
                .coerceIn(
                    0.0..max(
                        0.0,
                        (surroundingAverageElevation - planetTile.elevation)
                    )
                )

            val downhillTiles = planetTile.neighbors.mapNotNull { neighbor ->
                val decline = planetTile.elevation - neighbor.elevation
                if (decline.isFinite() && decline > 0.0) neighbor to decline else null
            }
            val sumDecline = downhillTiles.sumOf { (_, decline) -> decline }
            val erosion = max(
                if (planetTile.isContinentalCrust) 0.0 else TectonicGlobals.oceanicErosion,
                min(
                    planetTile.prominence,
                    min(
                        planetTile.elevation * maxErosionProportion,
                        planetTile.prominence.pow(0.5) * prominenceErosion +
                            planetTile.elevation.pow(2) * elevationErosion +
                            water * waterErosion
                    )
                )
            )
            val totalDepositAvailable = max(0.0, (erosion + deposit - depositTaken) * effectiveDepositMultiplier)

            planetTile.elevation -= erosion

            // deposit water
            if (sumDecline > 0.0 && planetTile.isAboveWater && water.isFinite()) {
                downhillTiles.forEach { (depositeeTile, decline) ->
                    val waterSent = water * decline / sumDecline
                    waterFlow[depositeeTile] = (waterFlow[depositeeTile] ?: 0.0) + waterSent
                }
            }

            // deposit sediment
            if (sumDecline > 0.0 && totalDepositAvailable >= 0.1 && totalDepositAvailable.isFinite()) {
                downhillTiles.forEach { (depositeeTile, decline) ->
                    val depositSent = totalDepositAvailable * decline / sumDecline
                    deposits[depositeeTile] = (deposits[depositeeTile] ?: 0.0) + depositSent
                }
            } else {
                planetTile.elevation += max(
                    0.0,
                    min(
                        totalDepositAvailable,
                        surroundingAverageElevation - planetTile.elevation
                    )
                )
            }

            planetTile.erosionDelta = planetTile.elevation - originalElevation
            planetTile.accruedDeposit += planetTile.erosionDelta
            planetTile.depositFlow = deposits[planetTile]!!
            planetTile.waterFlow = waterFlow[planetTile]!!
        }
    }
}
