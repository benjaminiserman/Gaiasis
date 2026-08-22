package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EcologySeasonalDynamicsTest {
    @Test
    fun `shared resource dynamics omit marine snow without a marine compartment`() {
        val fluxes = CellTurnFluxes().apply {
            carrionBiomass = 4_000.0
            marineSnowBiomass = 4_000.0
        }

        val updated = FunctionalResourceDynamics.update(
            previous = FunctionalResources(marineSnow = 0.8),
            fluxes = fluxes,
            areaKm2 = 40_000.0,
            hasMarineCompartment = false,
        )

        assertTrue(updated.carrion > 0.0, message = "Shared resource dynamics omit marine snow without a marine compartment: expected `updated.carrion > 0.0` to be true")
        assertEquals(0.0, updated.marineSnow, message = "Shared resource dynamics omit marine snow without a marine compartment: expected `updated.marineSnow` to match `0.0`")
    }

    @Test
    fun `ordinary organic fluxes produce graded resource levels`() {
        val fluxes = CellTurnFluxes().apply {
            // Carrion and waste exclude the smallest motile guilds, so their
            // ordinary seasonal fluxes are much smaller than detrital fluxes.
            carrionBiomass = 800_000.0
            detritusBiomass = 400_000_000.0
            wasteBiomass = 800_000.0
            marineSnowBiomass = 400_000_000.0
        }

        val updated = FunctionalResourceDynamics.update(
            previous = FunctionalResources(),
            fluxes = fluxes,
            areaKm2 = 40_000.0,
            hasMarineCompartment = true,
        )

        listOf(updated.carrion, updated.detritus, updated.waste, updated.marineSnow)
            .forEach { level -> assertTrue(level in 0.05..0.30, "resource level was $level") }
    }

    @Test
    fun `climate anomalies are deterministic and remain within authored bounds`() {
        val first = EcologyClimateVariability.anomaly(tileId = 42, year = 17.25)
        val repeated = EcologyClimateVariability.anomaly(tileId = 42, year = 17.25)

        assertEquals(first, repeated, message = "Climate anomalies are deterministic and remain within authored bounds: expected `repeated` to match `first`")
        repeat(1_000) { quarter ->
            val anomaly = EcologyClimateVariability.anomaly(tileId = 42, year = quarter / 4.0)
            assertTrue(anomaly.temperatureC in -2.0..2.0, message = "Climate anomalies are deterministic and remain within authored bounds: expected `anomaly.temperatureC in -2.0..2.0` to be true")
            assertTrue(anomaly.precipitationMultiplier in 0.75..1.25, message = "Climate anomalies are deterministic and remain within authored bounds: expected `anomaly.precipitationMultiplier in 0.75..1.25` to be true")
        }
    }

    @Test
    fun `detritus accessibility prevents complete alternating depletion`() {
        var level = 0.30
        repeat(20) {
            level = OrganicPoolDynamics.update(
                previousLevel = level,
                producedBiomassKg = 8_000.0,
                consumedBiomassKg = 1_000_000.0,
                biomassKgPerLevel = 480_000.0,
                seasonalRetention = 0.68,
                maximumAccessibleFraction = 0.75,
            )
            assertTrue(level > 0.0, message = "Detritus accessibility prevents complete alternating depletion: expected `level > 0.0` to be true")
        }
    }
}
