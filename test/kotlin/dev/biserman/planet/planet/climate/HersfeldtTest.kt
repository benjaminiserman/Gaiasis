package dev.biserman.planet.planet.climate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HersfeldtTest {
    @Test
    fun `AET cannot evaporate more water than precipitation and stored soil moisture`() {
        val datum = ClimateDatum(
            tileId = 0,
            months = List(12) { month ->
                ClimateDatumSample(
                    averageTemperature = 20.0,
                    insolation = 200.0,
                    precipitation = if (month == 0) 300.0 else 0.0,
                )
            },
        )
        val pet = List(12) { month -> if (month == 1) 600.0 else 0.0 }

        val aet = Hersfeldt.estimateAet(datum, pet)

        assertEquals(300.0, aet[1])
        assertEquals(300.0, aet.sum())
    }

    @Test
    fun `barren land takes priority over aridity`() {
        val diagnostics = diagnostics(
            winterTemperature = 20.0,
            summerTemperature = 30.0,
            aridityFactor = 0.01,
            totalGddz = 0.0,
        )

        assertEquals("TG", Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id)
    }

    @Test
    fun `low tropical growth aridity takes priority over annual aridity`() {
        val diagnostics = diagnostics(
            winterTemperature = 20.0,
            summerTemperature = 30.0,
            aridityFactor = 0.95,
            growthAridityFactor = 0.4,
            totalGint = 0.0,
        )

        assertEquals("TUA", Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id)
    }

    @Test
    fun `torrid moist climate is swelter rather than supertropical`() {
        val diagnostics = diagnostics(
            winterTemperature = 20.0,
            summerTemperature = 65.0,
            aridityFactor = 0.8,
            growthAridityFactor = 0.6,
            totalGint = 0.0,
        )

        assertEquals("HDb", Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id)
    }

    @Test
    fun `ocean above sixty degrees is torrid despite a cool winter`() {
        val diagnostics = diagnostics(
            winterTemperature = 10.0,
            summerTemperature = 65.0,
        )

        assertEquals("Or", Hersfeldt.classifyOcean(diagnostics, minIce = 0.0, maxIce = 0.0).id)
    }

    @Test
    fun `high GDD continental climate is temperate even with low GInt`() {
        val diagnostics = diagnostics(
            winterTemperature = -10.0,
            summerTemperature = 30.0,
            totalGdd = 2_000.0,
            totalGint = 0.0,
        )

        assertEquals("CDb", Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id)
    }

    @Test
    fun `uninterrupted cyclic growth and interruption accumulate indefinitely`() {
        val datum = ClimateDatum(
            tileId = 0,
            months = List(12) {
                ClimateDatumSample(
                    averageTemperature = 10.0,
                    insolation = 220.0,
                    precipitation = 50.0,
                )
            },
        )

        val results = Hersfeldt.gdd(datum)

        assertTrue(results.totalGdd.isInfinite())
        assertTrue(results.totalGddl.isInfinite())
        assertTrue(results.totalGddz.isInfinite())
        assertTrue(results.totalGint.isInfinite())
    }

    private fun diagnostics(
        winterTemperature: Double,
        summerTemperature: Double,
        aridityFactor: Double = 0.8,
        growthAridityFactor: Double = 0.6,
        growthSupply: Double = 1.0,
        evaporationRatio: Double = 0.8,
        totalGdd: Double = 2_000.0,
        totalGddz: Double = 2_000.0,
        totalGint: Double = 2_000.0,
        totalGddl: Double = 2_000.0,
    ): Hersfeldt.Diagnostics {
        val monthlyValues = List(12) { 1.0 }
        val gddResults = Hersfeldt.GddResults(
            monthlyGdd = monthlyValues,
            monthlyGddl = monthlyValues,
            monthlyGddz = monthlyValues,
            monthlyGint = monthlyValues,
            totalGdd = totalGdd,
            totalGddl = totalGddl,
            totalGddz = totalGddz,
            totalGint = totalGint,
        )
        return Hersfeldt.Diagnostics(
            winterTemperature = winterTemperature,
            summerTemperature = summerTemperature,
            averageTemperature = (winterTemperature + summerTemperature) / 2.0,
            annualPrecipitation = 600.0,
            temperatureRange = summerTemperature - winterTemperature,
            precipitationRange = 0.0,
            potentialEvapotranspiration = 600.0,
            actualEvapotranspiration = 480.0,
            aridityFactor = aridityFactor,
            growthAridityFactor = growthAridityFactor,
            growthSupply = growthSupply,
            evaporationRatio = evaporationRatio,
            totalGdd = totalGdd,
            totalGddz = totalGddz,
            totalGint = totalGint,
            winterType = Hersfeldt.winterType(winterTemperature),
            summerType = Hersfeldt.summerType(summerTemperature),
            pet = monthlyValues,
            aet = monthlyValues,
            gddResults = gddResults,
        )
    }
}