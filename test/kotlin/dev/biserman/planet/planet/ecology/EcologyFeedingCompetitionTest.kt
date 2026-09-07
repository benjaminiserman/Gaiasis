package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class EcologyFeedingCompetitionTest {
    private val lion = EarthSpeciesCatalog.ALL.single { it.id == "african-lion" }
    private val prey = EarthSpeciesCatalog.ALL.single { it.id == "plains-zebra" }
    private val ecology = EcologyCompiler.compile(listOf(prey, lion, lion.copy(id = "second-lion")))
    private val environment = SeasonalCellEnvironment.create(
        areaKm2 = 40_000.0,
        temperatureC = 25.0,
        insolation = 0.8,
        precipitationMm = 90.0,
        isLand = true,
    )
    private val predatorNiche = ecology.niches.indices.filter {
        ecology.niches[it].habitat == Habitat.LAND_SURFACE &&
            ecology.niches[it].strategy in listOf(EcoStrategy.AMBUSH_PREDATION, EcoStrategy.PURSUIT_PREDATION)
    }.maxBy { ecology.species[1].niche.fitFor(it) }
    private val capacity = EcologyBiomass.carryingCapacityKg(ecology.species[1], ecology.niches[predatorNiche], environment)

    @Test
    fun `an isolated predator has no interspecific feeding penalty`() {
        val disabled = advance(0.0, competitor = false)
        val enabled = advance(1.0, competitor = false)
        assertSame(disabled.first, enabled.first)
        assertEquals(disabled.second.wasteBiomass, enabled.second.wasteBiomass, 1e-8)
    }

    @Test
    fun `overlapping predators capture less prey and receive less food even with abundant prey`() {
        val disabled = advance(0.0)
        val enabled = advance(1.0)
        val preyWithoutInterference = disabled.first.activeBiomass[disabled.first.find(0)]
        val preyWithInterference = enabled.first.activeBiomass[enabled.first.find(0)]
        assertTrue(preyWithInterference > preyWithoutInterference, "Uncaptured prey must remain alive")
        assertTrue(enabled.second.wasteBiomass < disabled.second.wasteBiomass, "Consumer assimilation must decrease with capture")
        assertTrue(preyWithoutInterference > capacity * 90.0, "Prey supply must not be the limiting factor")
        assertSame(enabled.first, advance(1.0, reversed = true).first)
    }

    @Test
    fun `different niches do not interfere and background strength does not change capture`() {
        assertSame(advance(0.0, separateNiche = true).first, advance(1.0, separateNiche = true).first)
        assertSame(advance(1.0, backgroundStrength = 0.0).first, advance(1.0, backgroundStrength = 1.0).first)
    }

    @Test
    fun `feeding interference strength must be finite and nonnegative`() {
        for (invalid in listOf(-1.0, Double.NaN, Double.POSITIVE_INFINITY)) {
            assertFailsWith<IllegalArgumentException> { EcologyRuntimeConfig(feedingInterferenceCompetition = invalid) }
        }
    }

    private fun advance(
        strength: Double,
        competitor: Boolean = true,
        separateNiche: Boolean = false,
        reversed: Boolean = false,
        backgroundStrength: Double = 0.15,
    ): Pair<TileCommunity, CellTurnFluxes> {
        val community = TileCommunity()
        val indices = if (competitor) listOf(0, 1, 2) else listOf(0, 1)
        for (speciesIndex in if (reversed) indices.reversed() else indices) {
            val niche = when {
                speciesIndex == 0 -> NicheSelection.choose(ecology.species[0], ecology, environment)
                speciesIndex == 2 && separateNiche -> ecology.niches.indexOf(NicheDefinition(Habitat.LAND_SURFACE, EcoStrategy.SCAVENGING))
                else -> predatorNiche
            }
            assertTrue(ecology.species[speciesIndex].niche.fitFor(niche) > 0.0)
            community.add(speciesIndex, niche, if (speciesIndex == 0) capacity * 100.0 else capacity * 0.25)
        }
        val fluxes = CellTurnFluxes()
        EcologyRuntime(
            ecology,
            EcologyRuntimeConfig(
                feedingInterferenceCompetition = strength,
                interspecificNicheCompetition = backgroundStrength,
                backgroundMortality = 0.0,
                stressMortality = 0.0,
                maximumStarvationMortality = 0.0,
                maximumHabitatDiversityMortality = 0.0,
            )
        ).advanceSeason(community, environment, fluxes, finalizeExtinctions = false)
        return community to fluxes
    }

    private fun assertSame(expected: TileCommunity, actual: TileCommunity) {
        assertEquals(expected.size, actual.size)
        for (index in 0 until expected.size) {
            val other = actual.find(expected.speciesIndices[index])
            assertTrue(other >= 0)
            assertEquals(expected.activeBiomass[index], actual.activeBiomass[other], 1e-8)
            assertEquals(expected.reserves[index], actual.reserves[other], 1e-8)
        }
    }
}
