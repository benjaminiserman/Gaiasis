package dev.biserman.planet.planet.climate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HersfeldtTest {
    @Test
    fun `Hargreaves PET converts mean irradiance to monthly water depth`() {
        val temperature = 20.0
        val irradiance = 200.0
        val dailySolarRadiation = irradiance * 0.0864
        val latentHeatOfVaporization = (595.5 - 0.55 * temperature) / 238.8
        val expectedDailyPet = 0.0135 * (temperature + 17.8) * dailySolarRadiation / latentHeatOfVaporization

        assertEquals(
            expectedDailyPet * Hersfeldt.monthLength,
            Hersfeldt.hargreavesPet(temperature, irradiance),
            1e-9,
            "Hargreaves PET converts mean irradiance to monthly water depth: expected `Hersfeldt.hargreavesPet(temperature, irradiance)` to match `expectedDailyPet * Hersfeldt.monthLength`"
        )
    }

    @Test
    fun `Hargreaves PET cannot be negative`() {
        assertEquals(0.0, Hersfeldt.hargreavesPet(20.0, -100.0), "Hargreaves PET cannot be negative: expected `Hersfeldt.hargreavesPet(20.0, -100.0)` to match `0.0`")
        assertEquals(0.0, Hersfeldt.hargreavesPet(-20.0, 200.0), "Hargreaves PET cannot be negative: expected `Hersfeldt.hargreavesPet(-20.0, 200.0)` to match `0.0`")
    }

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

        assertEquals(300.0, aet[1], "AET cannot evaporate more water than precipitation and stored soil moisture: expected `aet[1]` to match `300.0`")
        assertEquals(300.0, aet.sum(), "AET cannot evaporate more water than precipitation and stored soil moisture: expected `aet.sum()` to match `300.0`")
    }

    @Test
    fun `barren land takes priority over aridity`() {
        val diagnostics = diagnostics(
            winterTemperature = 20.0,
            summerTemperature = 30.0,
            aridityFactor = 0.01,
            totalGddz = 0.0,
        )

        assertEquals("TG", Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id, "Barren land takes priority over aridity: expected `Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id` to match `\"TG\"`")
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

        assertEquals("TUA", Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id, "Low tropical growth aridity takes priority over annual aridity: expected `Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id` to match `\"TUA\"`")
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

        assertEquals("HDb", Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id, "Torrid moist climate is swelter rather than supertropical: expected `Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id` to match `\"HDb\"`")
    }

    @Test
    fun `ocean above sixty degrees is torrid despite a cool winter`() {
        val diagnostics = diagnostics(
            winterTemperature = 10.0,
            summerTemperature = 65.0,
        )

        assertEquals(
            "Or",
            Hersfeldt.classifyOcean(diagnostics, minIce = 0.0, maxIce = 0.0).id,
            "Ocean above sixty degrees is torrid despite a cool winter: expected `Hersfeldt.classifyOcean(diagnostics, minIce = 0.0, maxIce = 0.0).id` to match `\"Or\"`"
        )
    }

    @Test
    fun `high GDD continental climate is temperate even with low GInt`() {
        val diagnostics = diagnostics(
            winterTemperature = -10.0,
            summerTemperature = 30.0,
            totalGdd = 2_000.0,
            totalGint = 0.0,
        )

        assertEquals("CDb", Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id, "High GDD continental climate is temperate even with low GInt: expected `Hersfeldt.classifyLand(diagnostics, minIce = 0.0).id` to match `\"CDb\"`")
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

        assertTrue(results.totalGdd.isInfinite(), "Uninterrupted cyclic growth and interruption accumulate indefinitely: expected `results.totalGdd.isInfinite()` to be true")
        assertTrue(results.totalGddl.isInfinite(), "Uninterrupted cyclic growth and interruption accumulate indefinitely: expected `results.totalGddl.isInfinite()` to be true")
        assertTrue(results.totalGddz.isInfinite(), "Uninterrupted cyclic growth and interruption accumulate indefinitely: expected `results.totalGddz.isInfinite()` to be true")
        assertTrue(results.totalGint.isInfinite(), "Uninterrupted cyclic growth and interruption accumulate indefinitely: expected `results.totalGint.isInfinite()` to be true")
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
