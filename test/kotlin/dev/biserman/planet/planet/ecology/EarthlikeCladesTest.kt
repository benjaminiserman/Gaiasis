package dev.biserman.planet.planet.ecology

import dev.biserman.planet.planet.ecology.earthlike_clades.descend
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

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

    @Test
    fun `tree of life contains every extant species and only their ancestor branches`() {
        val extantIds = EarthSpeciesCatalog.ALL.mapTo(hashSetOf()) { it.id }
        val visibleExtantIds = EarthTreeOfLife.nodesById.values
            .filter(EarthTreeOfLife::isExtant)
            .mapTo(hashSetOf()) { it.id }

        assertEquals(extantIds, visibleExtantIds)
        EarthSpeciesCatalog.ALL.forEach { species ->
            var ancestorId = species.ancestorSpeciesId
            val visited = mutableSetOf(species.id)
            while (ancestorId != null && visited.add(ancestorId)) {
                val ancestor = EarthTreeOfLife.nodesById[ancestorId]
                assertTrue(ancestor != null, "Missing ancestor $ancestorId for ${species.id}")
                ancestorId = ancestor.ancestorSpeciesId
            }
        }
        assertTrue(
            EarthTreeOfLife.nodesById.values.none { node ->
                EarthTreeOfLife.visibleChildren(node).any { it === node }
            },
        )
    }

    @Test
    fun `excluding an ancestor excludes all of its extant descendants from randomization`() {
        val mammal = EarthTreeOfLife.nodesById.getValue("mammal")
        val mammalIds = EarthTreeOfLife.extantDescendants(mammal).mapTo(hashSetOf()) { it.id }
        val excluded = mutableSetOf<String>()

        EarthTreeOfLife.setIncluded(
            mammal,
            included = false,
            excludedSpeciesIds = excluded,
        )

        assertEquals(TreeOfLifeInclusion.EXCLUDED, EarthTreeOfLife.inclusion(mammal, excluded))
        assertEquals(mammalIds, excluded)
        assertTrue(EarthTreeOfLife.randomizationCandidates(excluded).none { it.id in mammalIds })
        assertTrue(EarthTreeOfLife.randomizationCandidates(excluded).any { it.id == "giant-kelp" })

        val oneMammal = EarthTreeOfLife.extantDescendants(mammal).first()
        EarthTreeOfLife.setIncluded(
            oneMammal,
            included = true,
            excludedSpeciesIds = excluded,
        )
        assertEquals(TreeOfLifeInclusion.MIXED, EarthTreeOfLife.inclusion(mammal, excluded))

        EarthTreeOfLife.setIncluded(
            mammal,
            included = true,
            excludedSpeciesIds = excluded,
        )
        assertEquals(TreeOfLifeInclusion.INCLUDED, EarthTreeOfLife.inclusion(mammal, excluded))
        assertTrue(excluded.isEmpty())
    }

    @Test
    fun `trait differences include additions and removals from the direct ancestor`() {
        val coconutCrab = EarthTreeOfLife.nodesById.getValue("coconut-crab")
        val differences = EarthTreeOfLife.traitDifferencesFromAncestor(coconutCrab)

        assertTrue(
            differences.any {
                it.kind == TraitDifferenceKind.ADDED && it.trait.baseTrait == CommonTrait.TRACHEA
            },
        )
        assertTrue(
            differences.any {
                it.kind == TraitDifferenceKind.REMOVED && it.trait.baseTrait == CommonTrait.GILLS
            },
        )
    }
}
