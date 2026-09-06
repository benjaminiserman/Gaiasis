package dev.biserman.planet.planet.ecology

import dev.biserman.planet.planet.ecology.earthlike_clades.descend
import kotlin.test.Test
import kotlin.test.assertFalse

class EarthlikeCladesTest {
    @Test
    fun `descend minus removes a scaled trait regardless of the selected level`() {
        val levelledAncestor = SpeciesDefinition(
            id = "levelled-ancestor",
            displayName = "levelled ancestor",
            sizeClass = SizeClass.SMALL,
            traits = listOf(CommonTrait.EYES.atLevel(3)),
        )
        val unlevelledAncestor = SpeciesDefinition(
            id = "unlevelled-ancestor",
            displayName = "unlevelled ancestor",
            sizeClass = SizeClass.SMALL,
            traits = listOf(CommonTrait.SCENT),
        )

        val removedByBaseTrait = levelledAncestor.descend(
            "removed by base trait",
            SizeClass.SMALL,
            minus = listOf(CommonTrait.EYES),
        )
        val removedByLevelledTrait = unlevelledAncestor.descend(
            "removed by levelled trait",
            SizeClass.SMALL,
            minus = listOf(CommonTrait.SCENT.atLevel(2)),
        )

        assertFalse(removedByBaseTrait.traits.any { it.baseTrait == CommonTrait.EYES })
        assertFalse(removedByLevelledTrait.traits.any { it.baseTrait == CommonTrait.SCENT })
    }
}
