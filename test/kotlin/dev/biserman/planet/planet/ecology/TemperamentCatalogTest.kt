package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertTrue

class TemperamentCatalogTest {
    @Test
    fun `catalog animals do not duplicate inherited temperaments`() {
        val animals = EarthSpeciesCatalog.MAMMALS + EarthSpeciesCatalog.BIRDS +
            EarthSpeciesCatalog.REPTILES_AND_AMPHIBIANS + EarthSpeciesCatalog.FISH +
            EarthSpeciesCatalog.INVERTEBRATES + EarthSpeciesCatalog.EXTINCT_SPECIES

        assertTrue(
            animals.all { definition ->
                definition.traits.count { it.group == TraitGroup.TEMPERAMENT } <= 1
            },
        )
        assertTrue(animals.any { CommonTrait.SKITTISH in it.traits })
        assertTrue(animals.any { CommonTrait.CALM in it.traits })
        assertTrue(animals.any { CommonTrait.AGGRESSIVE in it.traits })
    }
}
