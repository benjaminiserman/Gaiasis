package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EcologyWorldEcosystemNotebookTest {
    @Test
    fun `world ecosystem notebook has stable scenarios collapse controls and focused extinctions`() {
        val scenarios = AuthoredEcosystems.ALL

        assertTrue(scenarios.any { !it.intendedStable }, "Expected at least one collapse control.")
        assertTrue(scenarios.any { it.introductions.isNotEmpty() }, "Expected at least one species introduction.")
        assertTrue(scenarios.any { it.climateShifts.isNotEmpty() }, "Expected at least one climate shift.")
        assertTrue(scenarios.any { it.habitatShifts.isNotEmpty() }, "Expected at least one habitat shift.")
        assertTrue(scenarios.any { it.populationRemovals.isNotEmpty() }, "Expected at least one population removal.")
        assertEquals(
            6,
            scenarios.count {
                it.expectedExtinctions.isNotEmpty() &&
                    (
                        !it.intendedStable ||
                            it.introductions.isNotEmpty() ||
                            it.climateShifts.isNotEmpty() ||
                            it.habitatShifts.isNotEmpty() ||
                            it.populationRemovals.isNotEmpty()
                        )
            },
            message = "The notebook must retain its six explicit collapse or intervention scenarios",
        )
    }

    @Test
    fun `notebook uses only Earth species catalog entries`() {
        val referencedSpecies = AuthoredEcosystems.ALL
            .flatMap { scenario -> scenario.species.map { it.id } }
            .toSet()
        val catalogSpecies = EarthSpeciesCatalog.ALL.map { it.id }.toSet()

        assertTrue(referencedSpecies.isNotEmpty(), message = "Notebook uses only Earth species catalog entries: expected `referencedSpecies.isNotEmpty()` to be true")
        assertEquals(emptySet(), referencedSpecies - catalogSpecies, message = "Notebook uses only Earth species catalog entries: expected `referencedSpecies - catalogSpecies` to match `emptySet()`")
    }
}
