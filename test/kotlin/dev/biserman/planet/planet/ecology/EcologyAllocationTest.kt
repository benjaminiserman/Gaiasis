package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EcologyAllocationTest {
    @Test
    fun `shared prey gains match a food web with explicitly budgeted intake`() {
        val definitions = (0..2).map { index ->
            SpeciesDefinition(
                "species-$index",
                "Species $index",
                SizeClass.SMALL,
                listOf(
                    CommonTrait.TRACHEA,
                    CommonTrait.TEMPERATE_BIOCHEMISTRY,
                    CommonTrait.TERRESTRIAL_OVOSPORE,
                    CommonTrait.PHOTOSYNTHETIC_SURFACE,
                    CommonTrait.ROOTED_BODY,
                    ColorTrait.GREEN_COLORATION,
                )
            )
        }
        val compiled = EcologyCompiler.compile(definitions)

        // Two consumers each request the entire host. Parasitism avoids prey
        // camouflage/accessibility modifiers, isolating the shared food budget.
        fun ecologyWithRate(rate: Double): CompiledEcology {
            val kinds = ByteArray(9)
            val losses = DoubleArray(9)
            val gains = DoubleArray(9)
            for (consumer in 1..2) {
                kinds[consumer * 3] = InteractionKind.PARASITISM.ordinal.toByte()
                losses[consumer * 3] = rate
                gains[consumer * 3] = rate * 0.35
            }
            return compiled.copy(interactions = InteractionMatrix(3, kinds, gains, losses, DoubleArray(9), ByteArray(9)))
        }
        val environment = land(areaKm2 = 40_000.0)
        fun advance(rate: Double, order: List<Int>): TileCommunity {
            val ecology = ecologyWithRate(rate)
            val community = TileCommunity().apply {
                for (speciesIndex in order) {
                    add(
                        speciesIndex,
                        NicheSelection.choose(ecology.species[speciesIndex], ecology, environment),
                        if (speciesIndex == 0) 100.0 else 1_000.0
                    )
                }
            }
            EcologyRuntime(ecology, EcologyRuntimeConfig(feedingInterferenceCompetition = 0.0)).advanceSeason(community, environment, finalizeExtinctions = false)
            return community
        }
        val budgeted = advance(0.5, listOf(0, 1, 2))
        val hungry = advance(0.0, listOf(0, 1, 2))
        assertTrue(budgeted.activeBiomass[1] > hungry.activeBiomass[1] || budgeted.reserves[1] > hungry.reserves[1])
        for (order in listOf(listOf(0, 1, 2), listOf(2, 0, 1))) {
            assertSamePopulations(budgeted, advance(1.0, order))
        }
    }

    @Test
    fun `scavengers share accessible carrion without eating new production`() {
        val definition = SpeciesDefinition(
            "scavenger",
            "Scavenger",
            SizeClass.MEDIUM,
            listOf(
                CommonTrait.TRACHEA, CommonTrait.TEMPERATE_BIOCHEMISTRY, CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY, CommonTrait.VIVIPARITY, CommonTrait.VASCULAR_SYSTEM,
                CommonTrait.BONY_SKELETON, CommonTrait.LIMBED_BODY, CommonTrait.WALKING_LIMBS,
                CommonTrait.MEAT_EATING_MOUTHPARTS, CommonTrait.SCAVENGING_SENSES, ColorTrait.BLACK_COLORATION,
            )
        )
        val ecology = EcologyCompiler.compile(listOf(definition, definition.copy(id = "second-scavenger")))
        val environment = land().withResources(FunctionalResources(carrion = 0.000001))
        val niche = ecology.niches.indexOf(NicheDefinition(Habitat.LAND_SURFACE, EcoStrategy.SCAVENGING))
        fun advance(order: List<Int>, env: SeasonalCellEnvironment, fluxes: CellTurnFluxes?): TileCommunity {
            val community = TileCommunity().apply { order.forEach { add(it, niche, 1_000.0) } }
            EcologyRuntime(ecology).advanceSeason(community, env, fluxes, finalizeExtinctions = false)
            return community
        }
        val fluxes = CellTurnFluxes()
        val forward = advance(listOf(0, 1), environment, fluxes)
        // Availability is after retention and the inaccessible carcass fraction.
        val budget = 0.000001 * 100.0 * 0.55 * 0.90
        assertEquals(budget, fluxes.carrionConsumedBiomass, 1e-12)
        assertTrue(fluxes.carrionBiomass > budget, "Deaths produce next season's food")
        assertSamePopulations(forward, advance(listOf(1, 0), environment, CellTurnFluxes()))
        assertSamePopulations(forward, advance(listOf(0, 1), environment, null))
        val emptyFluxes = CellTurnFluxes()
        advance(listOf(0, 1), environment.withResources(FunctionalResources()), emptyFluxes)
        assertEquals(0.0, emptyFluxes.carrionConsumedBiomass)
        assertTrue(emptyFluxes.carrionBiomass > 0.0)
    }

    @Test
    fun `organic food consumption stays within all five pool budgets`() {
        val definitions = EarthSpeciesCatalog.ALL
        val compiled = EcologyCompiler.compile(definitions)
        val resources = FunctionalResources(0.000001, 0.000001, 0.000001, 0.000001, 0.000001)
        val environments = listOf(
            land(),
            SeasonalCellEnvironment.create(
                areaKm2 = 1.0,
                temperatureC = 21.0,
                insolation = 0.8,
                precipitationMm = 90.0,
                isLand = false,
                adjacentToLand = 1.0,
                waterDepthM = 10.0,
            )
        ).map { it.withResources(resources) }
        for (pool in OrganicResourcePool.entries) {
            val candidate = compiled.species.asSequence().flatMap { species ->
                environments.asSequence().flatMap { environment ->
                    compiled.niches.indices.asSequence().filter { niche ->
                        compiled.niches[niche].strategy == pool.strategy && species.niche.fitFor(niche) > 0.0 && environment.habitatAvailability(compiled.niches[niche].habitat) > 0.0
                    }.map { niche -> Triple(species, environment, niche) }
                }
            }.maxBy { (species, environment, niche) -> EcologyFitness.combined(species, environment, compiled.niches[niche]) }
            val (species, environment, niche) = candidate
            val ecology = EcologyCompiler.compile(listOf(definitions[species.index]))
            val community = TileCommunity().apply { add(0, niche, 1_000.0) }
            val fluxes = CellTurnFluxes()
            EcologyRuntime(ecology).advanceSeason(community, environment, fluxes)
            val consumed = when (pool) {
                OrganicResourcePool.CARRION -> fluxes.carrionConsumedBiomass
                OrganicResourcePool.DETRITUS -> fluxes.detritusConsumedBiomass
                OrganicResourcePool.WASTE -> fluxes.wasteConsumedBiomass
                OrganicResourcePool.MARINE_SNOW -> fluxes.marineSnowConsumedBiomass
                OrganicResourcePool.FRUIT -> fluxes.fruitConsumedBiomass
            }
            assertTrue(consumed > 0.0, "$pool fixture ${species.id} in ${compiled.niches[niche]} should feed")
            assertTrue(consumed <= pool.accessibleBiomassKg(resources, environment.areaKm2) + 1e-12, "$pool exceeded its budget")
        }
    }

    private fun assertSamePopulations(expected: TileCommunity, actual: TileCommunity) {
        assertEquals(expected.size, actual.size)
        for (index in 0 until expected.size) {
            val other = actual.find(expected.speciesIndices[index])
            assertTrue(other >= 0)
            assertEquals(expected.activeBiomass[index], actual.activeBiomass[other], 1e-8)
            assertEquals(expected.reserves[index], actual.reserves[other], 1e-8)
            assertEquals(expected.dormantBiomass[index], actual.dormantBiomass[other], 1e-8)
        }
    }

    private fun land(areaKm2: Double = 1.0) = SeasonalCellEnvironment.create(
        areaKm2 = areaKm2,
        temperatureC = 21.0,
        annualAverageTemperatureC = 18.0,
        insolation = 0.8,
        precipitationMm = 90.0,
        surfaceFertilityModifier = 0.7,
        isLand = true,
    )
}
