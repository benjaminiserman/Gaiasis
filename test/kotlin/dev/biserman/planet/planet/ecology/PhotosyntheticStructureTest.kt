package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PhotosyntheticStructureTest {
    @Test
    fun `larger photosynthetic organisms create overhead cover and shade smaller ones`() {
        val ecology = compile("african-baobab", "giant-rosette-lobelia")
        val environment = landEnvironment(canopyPotential = 0.8)
        val community = community(ecology, environment, "african-baobab", "giant-rosette-lobelia")

        val structured = environment.withPhotosyntheticStructure(ecology, community)

        assertTrue(structured.canopyCover in 0.0..environment.canopyPotential)
        assertTrue(structured.canopyCover > 0.0)
        assertEquals(environment.insolation, structured.producerLightAt(Habitat.LAND_SURFACE, SizeClass.HUGE))
        assertTrue(
            structured.producerLightAt(Habitat.LAND_SURFACE, SizeClass.MEDIUM) <
                structured.producerLightAt(Habitat.LAND_SURFACE, SizeClass.HUGE),
        )
    }

    @Test
    fun `overhead cover disappears with the large producer`() {
        val ecology = compile("african-baobab", "giant-rosette-lobelia")
        val environment = landEnvironment(canopyPotential = 0.8)
        val community = community(ecology, environment, "giant-rosette-lobelia")

        val structured = environment.withPhotosyntheticStructure(ecology, community)

        assertEquals(0.0, structured.canopyCover)
        assertEquals(0.0, structured.habitatAvailability(Habitat.CANOPY))
        assertEquals(
            environment.insolation,
            structured.producerLightAt(Habitat.LAND_SURFACE, SizeClass.MEDIUM),
        )
    }

    @Test
    fun `terrestrial producers establish on the land surface`() {
        val ecology = compile("strangler-fig")
        val environment = landEnvironment(canopyPotential = 0.8)
        val species = ecology.species.single()
        val niche = ecology.niches[NicheSelection.choose(species, ecology, environment)]

        assertEquals(EcoStrategy.PHOTOSYNTHESIS, niche.strategy)
        assertEquals(Habitat.LAND_SURFACE, niche.habitat)
    }

    private fun compile(vararg ids: String): CompiledEcology {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        return EcologyCompiler.compile(ids.map(definitions::getValue))
    }

    private fun community(
        ecology: CompiledEcology,
        environment: SeasonalCellEnvironment,
        vararg ids: String,
    ): TileCommunity = TileCommunity().also { community ->
        ids.forEach { id ->
            val species = ecology.species.single { it.id == id }
            val nicheIndex = NicheSelection.choose(species, ecology, environment)
            val referenceBiomass =
                environment.areaKm2 *
                    EcologyBiomass.terrestrialProducerDensityKgKm2.getValue(species.sizeClass)
            community.add(species.index, nicheIndex, referenceBiomass * 0.2)
        }
    }

    private fun landEnvironment(canopyPotential: Double) = SeasonalCellEnvironment.create(
        areaKm2 = 100.0,
        temperatureC = 24.0,
        insolation = 0.8,
        precipitationMm = 100.0,
        isLand = true,
        canopyCover = canopyPotential,
    )
}
