package dev.biserman.planet.planet.ecology

import com.fasterxml.jackson.databind.ObjectMapper
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EcologyWorldEcosystemNotebookTest {
    @Test
    fun `world ecosystem notebook has stable scenarios collapse controls and focused extinctions`() {
        val notebookPath = Path.of(
            "src/main/kotlin/dev/biserman/planet/notebooks/ecology_world_ecosystems.ipynb",
        )
        val notebook = ObjectMapper().readTree(notebookPath.readText())
        val source = notebook["cells"]
            .flatMap { cell -> cell["source"].map { it.asText() } }
            .joinToString("")
        val scenarios = AuthoredEcosystems.ALL

        assertEquals(22, scenarios.size, message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `scenarios.size` to match `22`")
        assertEquals(1, scenarios.count { !it.intendedStable }, message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `scenarios.count { !it.intendedStable }` to match `1`")
        assertEquals(1, scenarios.count { it.introductions.isNotEmpty() }, message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `scenarios.count { it.introductions.isNotEmpty() }` to match `1`")
        assertEquals(2, scenarios.count { it.climateShifts.isNotEmpty() }, message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `scenarios.count { it.climateShifts.isNotEmpty() }` to match `2`")
        assertEquals(2, scenarios.count { it.habitatShifts.isNotEmpty() }, message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `scenarios.count { it.habitatShifts.isNotEmpty() }` to match `2`")
        assertEquals(
            2,
            scenarios.count {
                it.populationRemovals.isNotEmpty()
            },
            message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `scenarios.count { it.populationRemovals.isNotEmpty() }` to match `2`"
        )
        assertEquals(
            6,
            scenarios.count {
                it.expectedExtinctions.isNotEmpty()
            },
            message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `scenarios.count { it.expectedExtinctions.isNotEmpty() }` to match `6`"
        )
        assertEquals(1, scenarios.count { it.tile.includeAeroplankton }, message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `scenarios.count { it.tile.includeAeroplankton }` to match `1`")
        assertEquals(
            scenarios.size,
            Regex("""runEcosystem\(AuthoredEcosystems\.\w+\)""").findAll(source).count(),
            message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `Regex(\"\"\"runEcosystem\\(AuthoredEcosystems\\.\\w+\\)\"\"\").findAll(source).count()` to match `scenarios.size`",
        )
        assertTrue("FunctionalResourceDynamics.update(" in source, message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `\"FunctionalResourceDynamics.update(\" in source` to be true")
        assertEquals(
            1,
            Regex("""repeat\(4000\)""").findAll(source).count(),
            message = "World ecosystem notebook has stable scenarios collapse controls and focused extinctions: expected `Regex(\"\"\"repeat\\(4000\\)\"\"\").findAll(source).count()` to match `1`"
        )
    }

    @Test
    fun `notebook uses only Earth species catalog entries`() {
        val notebookPath = Path.of(
            "src/main/kotlin/dev/biserman/planet/notebooks/ecology_world_ecosystems.ipynb",
        )
        val source = ObjectMapper().readTree(notebookPath.readText())["cells"]
            .flatMap { cell -> cell["source"].map { it.asText() } }
            .joinToString("")
        val referencedSpecies = AuthoredEcosystems.ALL
            .flatMap { scenario -> scenario.species.map { it.id } }
            .toSet()
        val catalogSpecies = EarthSpeciesCatalog.ALL.map { it.id }.toSet()

        assertTrue(referencedSpecies.isNotEmpty(), message = "Notebook uses only Earth species catalog entries: expected `referencedSpecies.isNotEmpty()` to be true")
        assertEquals(emptySet(), referencedSpecies - catalogSpecies, message = "Notebook uses only Earth species catalog entries: expected `referencedSpecies - catalogSpecies` to match `emptySet()`")
        assertTrue("SpeciesDefinition(" !in source, message = "Notebook uses only Earth species catalog entries: expected `\"SpeciesDefinition(\" !in source` to be true")
        assertTrue("TargetedRelationshipTrait(" !in source, message = "Notebook uses only Earth species catalog entries: expected `\"TargetedRelationshipTrait(\" !in source` to be true")
    }
}
