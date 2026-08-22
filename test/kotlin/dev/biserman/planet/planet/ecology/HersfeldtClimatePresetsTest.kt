package dev.biserman.planet.planet.ecology

import dev.biserman.planet.planet.climate.ClimateDatum
import dev.biserman.planet.planet.climate.ClimateDatumSample
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class HersfeldtClimatePresetsTest {
    @Test
    fun `catalog contains the notebook climates and five ocean climates`() {
        assertEquals(7, HersfeldtClimatePresets.LAND.size, message = "Catalog contains the notebook climates and five ocean climates: expected `HersfeldtClimatePresets.LAND.size` to match `7`")
        assertEquals(5, HersfeldtClimatePresets.OCEAN.size, message = "Catalog contains the notebook climates and five ocean climates: expected `HersfeldtClimatePresets.OCEAN.size` to match `5`")
        assertEquals(12, HersfeldtClimatePresets.ALL.size, message = "Catalog contains the notebook climates and five ocean climates: expected `HersfeldtClimatePresets.ALL.size` to match `12`")
        assertEquals(
            setOf(
                "oceanic-temperate",
                "desert",
                "savanna",
                "jungle",
                "boreal",
                "tundra",
                "ice-cap",
            ),
            HersfeldtClimatePresets.LAND.mapTo(hashSetOf()) { it.id },
            message = "Assertion failed",
        )
        assertTrue(HersfeldtClimatePresets.LAND.none { it.ocean }, message = "Catalog contains the notebook climates and five ocean climates: expected `HersfeldtClimatePresets.LAND.none { it.ocean }` to be true")
        assertTrue(HersfeldtClimatePresets.OCEAN.all { it.ocean }, message = "Catalog contains the notebook climates and five ocean climates: expected `HersfeldtClimatePresets.OCEAN.all { it.ocean }` to be true")
        assertEquals(
            HersfeldtClimatePresets.ALL.size,
            HersfeldtClimatePresets.ALL.map {
                it.id
            }.distinct().size,
            message = "Catalog contains the notebook climates and five ocean climates: expected `HersfeldtClimatePresets.ALL.map { it.id }.distinct().size` to match `HersfeldtClimatePresets.ALL.size`"
        )
    }

    @Test
    fun `notebook desert profile is preserved month for month`() {
        val desert = HersfeldtClimatePresets.DESERT
        assertFalse(desert.ocean, message = "Notebook desert profile is preserved month for month: expected `desert.ocean` to be false")
        assertEquals(
            listOf(18.0, 20.0, 24.0, 29.0, 33.0, 36.0, 38.0, 37.0, 34.0, 29.0, 23.0, 19.0),
            desert.months.map { it.averageTemperature },
            message = "Notebook desert profile is preserved month for month: expected `desert.months.map { it.averageTemperature }` to match `listOf(18.0, 20.0, 24.0, 29.0, 33.0, 36.0, 38.0, 37.0, 34.0, 29.0, 23.0, 19.0)`",
        )
        assertEquals(
            listOf(220.0, 245.0, 280.0, 310.0, 330.0, 345.0, 340.0, 325.0, 300.0, 270.0, 235.0, 215.0),
            desert.months.map { it.insolation },
            message = "Notebook desert profile is preserved month for month: expected `desert.months.map { it.insolation }` to match `listOf(220.0, 245.0, 280.0, 310.0, 330.0, 345.0, 340.0, 325.0, 300.0, 270.0, 235.0, 215.0)`",
        )
        assertEquals(
            listOf(5.0, 4.0, 4.0, 2.0, 1.0, 0.5, 1.0, 2.0, 3.0, 5.0, 6.0, 6.0),
            desert.months.map { it.precipitation },
            message = "Notebook desert profile is preserved month for month: expected `desert.months.map { it.precipitation }` to match `listOf(5.0, 4.0, 4.0, 2.0, 1.0, 0.5, 1.0, 2.0, 3.0, 5.0, 6.0, 6.0)`",
        )
    }

    @Test
    fun `marine presets cover illuminated and permanently dark water`() {
        assertTrue(
            HersfeldtClimatePresets.TROPICAL_REEF.months.all {
                it.insolation > 0.0
            },
            message = "Marine presets cover illuminated and permanently dark water: expected `HersfeldtClimatePresets.TROPICAL_REEF.months.all { it.insolation > 0.0 }` to be true"
        )
        assertTrue(
            HersfeldtClimatePresets.DEEP_OCEAN.months.all {
                it.insolation == 0.0
            },
            message = "Marine presets cover illuminated and permanently dark water: expected `HersfeldtClimatePresets.DEEP_OCEAN.months.all { it.insolation == 0.0 }` to be true"
        )
        assertTrue(
            HersfeldtClimatePresets.POLAR_SEA.months.any {
                it.averageTemperature < 0.0
            },
            message = "Marine presets cover illuminated and permanently dark water: expected `HersfeldtClimatePresets.POLAR_SEA.months.any { it.averageTemperature < 0.0 }` to be true"
        )
        assertTrue(
            HersfeldtClimatePresets.PERMANENT_SEA_ICE.months.all {
                it.averageTemperature < 0.0
            },
            message = "Marine presets cover illuminated and permanently dark water: expected `HersfeldtClimatePresets.PERMANENT_SEA_ICE.months.all { it.averageTemperature < 0.0 }` to be true"
        )
        assertTrue(
            HersfeldtClimatePresets.TEMPERATE_SHELF.months.any {
                it.averageTemperature >= 18.0
            },
            message = "Marine presets cover illuminated and permanently dark water: expected `HersfeldtClimatePresets.TEMPERATE_SHELF.months.any { it.averageTemperature >= 18.0 }` to be true"
        )
    }

    @Test
    fun `sea ice requires a freezing majority and no warm summer`() {
        assertTrue(
            PlanetEcologyEnvironment.supportsSeaIceHabitat(
                climate(listOf(-8.0, -7.0, -5.0, -2.0, 0.0, 0.0, 0.0, 4.0, 8.0, 10.0, 3.0, 1.0)),
            ),
            message = "Sea ice requires a freezing majority and no warm summer: expected `PlanetEcologyEnvironment.supportsSeaIceHabitat( climate(listOf(-8.0, -7.0, -5.0, -2.0, 0.0, 0.0, 0.0, 4.0, 8.0, 10.0, 3.0, 1.0)), )` to be true",
        )
        assertFalse(
            PlanetEcologyEnvironment.supportsSeaIceHabitat(
                climate(listOf(-8.0, -7.0, -5.0, -2.0, 0.0, 0.0, 1.0, 2.0, 3.0, 4.0, 5.0, 6.0)),
            ),
            "Exactly half the year at or below freezing is insufficient",
        )
        assertFalse(
            PlanetEcologyEnvironment.supportsSeaIceHabitat(
                climate(listOf(-8.0, -7.0, -5.0, -2.0, 0.0, 0.0, 0.0, 2.0, 4.0, 8.0, 10.1, 1.0)),
            ),
            "A month above 10 C excludes the sea-ice habitat",
        )
    }

    private fun climate(temperatures: List<Double>): ClimateDatum =
        ClimateDatum(
            1,
            temperatures.map { temperature ->
                ClimateDatumSample(
                    averageTemperature = temperature,
                    insolation = 100.0,
                    precipitation = 20.0,
                )
            },
        )
}
