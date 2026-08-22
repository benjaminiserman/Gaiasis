package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class EcologyHabitatConstraintTest {
    private val catalogEcology = EcologyCompiler.compile(
        EarthSpeciesCatalog.ALL + InvariantSpecies.ALL,
    )

    @Test
    fun `light-dependent coral loses habitat fit with depth`() {
        val coral = catalogEcology.species.single { it.id == "staghorn-coral" }
        val shallow = ocean(depthM = 20.0)
        val marginal = ocean(depthM = 55.0)
        val deep = ocean(depthM = 100.0)

        assertEquals(
            1.0,
            EcologyFitness.habitat(coral, shallow, Habitat.SUNLIT_WATER),
            message = "Light-dependent coral loses habitat fit with depth: expected `EcologyFitness.habitat(coral, shallow, Habitat.SUNLIT_WATER)` to match `1.0`",
        )
        assertTrue(
            EcologyFitness.habitat(coral, marginal, Habitat.SUNLIT_WATER) in 0.0..1.0,
            message = "Light-dependent coral loses habitat fit with depth: expected `EcologyFitness.habitat(coral, marginal, Habitat.SUNLIT_WATER) in 0.0..1.0` to be true",
        )
        assertEquals(
            0.0,
            EcologyFitness.habitat(coral, deep, Habitat.SUNLIT_WATER),
            message = "Light-dependent coral loses habitat fit with depth: expected `EcologyFitness.habitat(coral, deep, Habitat.SUNLIT_WATER)` to match `0.0`",
        )
        assertTrue(NicheSelection.choose(coral, catalogEcology, shallow) >= 0, message = "Light-dependent coral loses habitat fit with depth: expected `NicheSelection.choose(coral, catalogEcology, shallow) >= 0` to be true")
        assertEquals(-1, NicheSelection.choose(coral, catalogEcology, deep), message = "Light-dependent coral loses habitat fit with depth: expected `NicheSelection.choose(coral, catalogEcology, deep)` to match `-1`")
    }

    @Test
    fun `permanent sea ice is a distinct coastal surface habitat`() {
        val withoutIce = ocean(depthM = 250.0, permanentSeaIce = false, adjacentToLand = 1.0)
        val offshoreIce = ocean(depthM = 250.0, permanentSeaIce = true, adjacentToLand = 0.0)
        val coastalIce = ocean(depthM = 250.0, permanentSeaIce = true, adjacentToLand = 1.0)
        val penguin = catalogEcology.species.single { it.id == "emperor-penguin" }
        val polarBear = catalogEcology.species.single { it.id == "polar-bear" }

        assertEquals(0.0, withoutIce.habitatAvailability(Habitat.SEA_ICE), message = "Permanent sea ice is a distinct coastal surface habitat: expected `withoutIce.habitatAvailability(Habitat.SEA_ICE)` to match `0.0`")
        assertTrue(coastalIce.habitatAvailability(Habitat.SEA_ICE) > 0.0, message = "Permanent sea ice is a distinct coastal surface habitat: expected `coastalIce.habitatAvailability(Habitat.SEA_ICE) > 0.0` to be true")
        assertEquals(-1, NicheSelection.choose(penguin, catalogEcology, withoutIce), message = "Permanent sea ice is a distinct coastal surface habitat: expected `NicheSelection.choose(penguin, catalogEcology, withoutIce)` to match `-1`")
        assertEquals(-1, NicheSelection.choose(penguin, catalogEcology, offshoreIce), message = "Permanent sea ice is a distinct coastal surface habitat: expected `NicheSelection.choose(penguin, catalogEcology, offshoreIce)` to match `-1`")
        assertEquals(
            Habitat.SEA_ICE,
            catalogEcology.niches[NicheSelection.choose(penguin, catalogEcology, coastalIce)].habitat,
            message = "Permanent sea ice is a distinct coastal surface habitat: expected `catalogEcology.niches[NicheSelection.choose(penguin, catalogEcology, coastalIce)].habitat` to match `Habitat.SEA_ICE`",
        )

        assertTrue(polarBear.niche.supportFor(Habitat.SEA_ICE) > 0.0, message = "Permanent sea ice is a distinct coastal surface habitat: expected `polarBear.niche.supportFor(Habitat.SEA_ICE) > 0.0` to be true")
        assertEquals(0.0, polarBear.niche.supportFor(Habitat.SUNLIT_WATER), message = "Permanent sea ice is a distinct coastal surface habitat: expected `polarBear.niche.supportFor(Habitat.SUNLIT_WATER)` to match `0.0`")
        assertEquals(0.0, polarBear.niche.supportFor(Habitat.DARK_WATER), message = "Permanent sea ice is a distinct coastal surface habitat: expected `polarBear.niche.supportFor(Habitat.DARK_WATER)` to match `0.0`")
    }

    @Test
    fun `sea-ice penguins can prey across the ice-water boundary`() {
        val definitions = listOf(
            EarthSpeciesCatalog.BIRDS.single { it.id == "emperor-penguin" },
            InvariantSpecies.SMALL_AQUATIC_LIFE,
        )
        val ecology = EcologyCompiler.compile(definitions)
        val penguin = ecology.speciesIndex("emperor-penguin")
        val aquaticPrey = ecology.speciesIndex(InvariantSpecies.SMALL_AQUATIC_LIFE.id)

        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(penguin, aquaticPrey).kind,
            message = "Sea-ice penguins can prey across the ice-water boundary: expected `ecology.interactions.get(penguin, aquaticPrey).kind` to match `InteractionKind.PREDATION`"
        )
    }

    @Test
    fun `open ocean requires underwater breathing or prolonged breath holding`() {
        val blueWhale = catalogEcology.species.single { it.id == "blue-whale" }
        val greatWhiteShark = catalogEcology.species.single { it.id == "great-white-shark" }
        val manatee = catalogEcology.species.single { it.id == "west-indian-manatee" }
        val seaOtter = catalogEcology.species.single { it.id == "sea-otter" }
        val offshoreOcean = ocean(depthM = 250.0)
        val coastalOcean = ocean(depthM = 45.0, adjacentToLand = 1.0)

        assertTrue(blueWhale.physiology.respiration.prolongedBreathHolding, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `blueWhale.physiology.respiration.prolongedBreathHolding` to be true")
        assertFalse(blueWhale.physiology.respiration.underwaterBreathing, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `blueWhale.physiology.respiration.underwaterBreathing` to be false")
        assertTrue(blueWhale.niche.supportFor(Habitat.SUNLIT_WATER) > 0.0, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `blueWhale.niche.supportFor(Habitat.SUNLIT_WATER) > 0.0` to be true")

        assertTrue(greatWhiteShark.physiology.respiration.underwaterBreathing, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `greatWhiteShark.physiology.respiration.underwaterBreathing` to be true")
        assertFalse(greatWhiteShark.physiology.respiration.prolongedBreathHolding, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `greatWhiteShark.physiology.respiration.prolongedBreathHolding` to be false")
        assertTrue(greatWhiteShark.niche.supportFor(Habitat.SUNLIT_WATER) > 0.0, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `greatWhiteShark.niche.supportFor(Habitat.SUNLIT_WATER) > 0.0` to be true")

        for (coastalDiver in listOf(manatee, seaOtter)) {
            assertFalse(coastalDiver.physiology.respiration.underwaterBreathing, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `coastalDiver.physiology.respiration.underwaterBreathing` to be false")
            assertFalse(coastalDiver.physiology.respiration.prolongedBreathHolding, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `coastalDiver.physiology.respiration.prolongedBreathHolding` to be false")
            assertTrue(coastalDiver.niche.supportFor(Habitat.COASTAL) > 0.0, message = "Open ocean requires underwater breathing or prolonged breath holding: expected `coastalDiver.niche.supportFor(Habitat.COASTAL) > 0.0` to be true")
            assertEquals(0.0, coastalDiver.niche.supportFor(Habitat.SUNLIT_WATER), message = "Open ocean requires underwater breathing or prolonged breath holding: expected `coastalDiver.niche.supportFor(Habitat.SUNLIT_WATER)` to match `0.0`")
            assertEquals(0.0, coastalDiver.niche.supportFor(Habitat.DARK_WATER), message = "Open ocean requires underwater breathing or prolonged breath holding: expected `coastalDiver.niche.supportFor(Habitat.DARK_WATER)` to match `0.0`")
            assertEquals(
                -1,
                NicheSelection.choose(coastalDiver, catalogEcology, offshoreOcean),
                message = "Open ocean requires underwater breathing or prolonged breath holding: expected `NicheSelection.choose(coastalDiver, catalogEcology, offshoreOcean)` to match `-1`"
            )
            assertTrue(
                NicheSelection.choose(coastalDiver, catalogEcology, coastalOcean) >= 0,
                message = "Open ocean requires underwater breathing or prolonged breath holding: expected `NicheSelection.choose(coastalDiver, catalogEcology, coastalOcean) >= 0` to be true"
            )
        }
    }

    @Test
    fun `every species with open-ocean habitat support can respire there`() {
        for (species in catalogEcology.species) {
            val hasOpenOceanSupport =
                species.niche.supportFor(Habitat.SUNLIT_WATER) > 0.0 ||
                    species.niche.supportFor(Habitat.DARK_WATER) > 0.0
            if (hasOpenOceanSupport) {
                assertTrue(
                    species.physiology.respiration.underwaterBreathing || species.physiology.respiration.prolongedBreathHolding,
                    "${species.displayName} has open-ocean support without a qualifying respiration trait",
                )
            }
        }
    }

    @Test
    fun `pandas and koalas consume only their obligate authored plants`() {
        val panda = catalogEcology.speciesIndex("giant-panda")
        val bamboo = catalogEcology.speciesIndex("giant-bamboo")
        val koala = catalogEcology.speciesIndex("koala")
        val eucalyptus = catalogEcology.speciesIndex("eucalyptus-tree")
        val oak = catalogEcology.speciesIndex("english-oak")

        assertEquals(
            InteractionKind.GRAZING,
            catalogEcology.interactions.get(panda, bamboo).kind,
            message = "Pandas and koalas consume only their obligate authored plants: expected `catalogEcology.interactions.get(panda, bamboo).kind` to match `InteractionKind.GRAZING`"
        )
        assertTrue(catalogEcology.interactions.get(panda, bamboo).targetRequired, message = "Pandas and koalas consume only their obligate authored plants: expected `catalogEcology.interactions.get(panda, bamboo).targetRequired` to be true")
        assertEquals(
            InteractionKind.NONE,
            catalogEcology.interactions.get(panda, oak).kind,
            message = "Pandas and koalas consume only their obligate authored plants: expected `catalogEcology.interactions.get(panda, oak).kind` to match `InteractionKind.NONE`"
        )

        assertEquals(
            InteractionKind.GRAZING,
            catalogEcology.interactions.get(koala, eucalyptus).kind,
            message = "Pandas and koalas consume only their obligate authored plants: expected `catalogEcology.interactions.get(koala, eucalyptus).kind` to match `InteractionKind.GRAZING`"
        )
        assertTrue(catalogEcology.interactions.get(koala, eucalyptus).targetRequired, message = "Pandas and koalas consume only their obligate authored plants: expected `catalogEcology.interactions.get(koala, eucalyptus).targetRequired` to be true")
        assertEquals(
            InteractionKind.NONE,
            catalogEcology.interactions.get(koala, oak).kind,
            message = "Pandas and koalas consume only their obligate authored plants: expected `catalogEcology.interactions.get(koala, oak).kind` to match `InteractionKind.NONE`"
        )

        val completed = EcologyAssembly.completeRequiredTargets(
            catalogEcology,
            selected = listOf(catalogEcology.species[panda]),
            availableTargets = catalogEcology.species,
        )
        assertTrue(completed.any { it.id == "giant-bamboo" }, message = "Pandas and koalas consume only their obligate authored plants: expected `completed.any { it.id == \"giant-bamboo\" }` to be true")

        val impossible = EcologyAssembly.completeRequiredTargets(
            catalogEcology,
            selected = listOf(catalogEcology.species[panda]),
            availableTargets = listOf(catalogEcology.species[oak]),
        )
        assertFalse(impossible.any { it.id == "giant-panda" }, message = "Pandas and koalas consume only their obligate authored plants: expected `impossible.any { it.id == \"giant-panda\" }` to be false")
    }

    private fun ocean(
        depthM: Double,
        permanentSeaIce: Boolean = false,
        adjacentToLand: Double = 0.0,
    ) = SeasonalCellEnvironment.create(
        areaKm2 = 40_000.0,
        temperatureC = if (permanentSeaIce) -4.0 else 24.0,
        annualAverageTemperatureC = if (permanentSeaIce) -5.0 else 24.0,
        insolation = 0.8,
        precipitationMm = 800.0,
        isLand = false,
        adjacentToLand = adjacentToLand,
        waterDepthM = depthM,
        usefulSunlightReachesWater = true,
        permanentSeaIce = permanentSeaIce,
    )
}
