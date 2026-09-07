package dev.biserman.planet.planet.ecology

import dev.biserman.planet.utils.Serialization
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import java.nio.file.Files
import kotlin.io.path.deleteIfExists
import kotlin.random.Random
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EcologyMutationsTest {
    @AfterEach
    fun resetCatalog() {
        EvolvingSpeciesCatalog.activate(emptyList())
    }

    @Test
    fun `mutation interval occurs at completed century boundaries`() {
        assertFalse(EcologyMutations.isMutationInterval(0))
        assertFalse(EcologyMutations.isMutationInterval(399))
        assertTrue(EcologyMutations.isMutationInterval(400))
        assertTrue(EcologyMutations.isMutationInterval(800))
        assertFalse(EcologyMutations.isMutationInterval(400, intervalYears = 250))
        assertTrue(EcologyMutations.isMutationInterval(1_000, intervalYears = 250))
    }

    @Test
    fun `mutation respects its divergence cap and identifies its lineage`() {
        val parent = EarthSpeciesCatalog.ALL.first()
        val proposal = EcologyMutations.propose(
            parent,
            Random(7),
            maximumDivergences = 1,
        )

        assertEquals(1, proposal.record.changes.size)
        assertEquals(parent.id, proposal.definition.ancestorSpeciesId)
        assertTrue(proposal.definition.displayName.endsWith("⟦M1⟧"))
        assertTrue(proposal.definition.id.startsWith("${parent.id}~m"))
        assertEquals(
            proposal.record.changes.map { it.trait }.toSet().size,
            proposal.record.changes.size,
        )
    }

    @Test
    fun `adding a grouped trait replaces the existing member of that group`() {
        val parent = SpeciesDefinition(
            id = "brown-ancestor",
            displayName = "brown ancestor",
            sizeClass = SizeClass.SMALL,
            traits = listOf(ColorTrait.BROWN_COLORATION),
        )
        val changes = EcologyMutations.changesFor(parent, listOf(ColorTrait.GREEN_COLORATION))
        val record = mutationRecord(parent, changes)
        val descendant = EvolvingSpeciesCatalog.createDefinition(parent, record)

        assertEquals(listOf(ColorTrait.GREEN_COLORATION), descendant.traits)
    }

    @Test
    fun `scaled traits mutate by one adjacent level`() {
        fun parentWithEyes(level: Int?) = SpeciesDefinition(
            id = "eyes-${level ?: 0}",
            displayName = "eyes ${level ?: 0}",
            sizeClass = SizeClass.SMALL,
            traits = level?.let { listOf(CommonTrait.EYES.atLevel(it)) } ?: emptyList(),
        )

        val levelThree = parentWithEyes(3)
        val levelThreeChanges = EcologyMutations.changesFor(levelThree, listOf(CommonTrait.EYES))
        val levelTwoDescendant = EvolvingSpeciesCatalog.createDefinition(
            levelThree,
            mutationRecord(levelThree, levelThreeChanges),
        )
        assertEquals(2, levelThreeChanges.single().level)
        assertEquals(2, levelTwoDescendant.traitLevel(CommonTrait.EYES))

        val eyeless = parentWithEyes(null)
        val eyelessChanges = EcologyMutations.changesFor(eyeless, listOf(CommonTrait.EYES))
        val levelOneDescendant = EvolvingSpeciesCatalog.createDefinition(
            eyeless,
            mutationRecord(eyeless, eyelessChanges),
        )
        assertEquals(1, eyelessChanges.single().level)
        assertEquals(1, levelOneDescendant.traitLevel(CommonTrait.EYES))

        val levelTwo = parentWithEyes(2)
        val adjacentLevels = (0 until 100).mapTo(hashSetOf()) { seed ->
            val changes = EcologyMutations.changesFor(
                levelTwo,
                listOf(CommonTrait.EYES),
                Random(seed),
            )
            EvolvingSpeciesCatalog.createDefinition(
                levelTwo,
                mutationRecord(levelTwo, changes),
            ).traitLevel(CommonTrait.EYES)
        }
        assertEquals(setOf(1, 3), adjacentLevels)
    }

    @Test
    fun `reduced limbs are represented by level one primary limbs`() {
        val seahorse = EarthSpeciesCatalog.ALL.single { it.id == "lined-seahorse" }

        assertTrue(CommonTrait.entries.none { it.name == "REDUCED_LIMBS" })
        assertEquals(1, seahorse.traitLevel(CommonTrait.AQUATIC_LIMBS))
    }

    @Test
    fun `lineage code extends through successive descendants`() {
        val parent = EarthSpeciesCatalog.ALL.first()
        val firstIdentity = EvolvingSpeciesCatalog.nextIdentity(parent)
        val firstRecord = MutatedSpeciesRecord(
            id = firstIdentity.id,
            displayName = firstIdentity.displayName,
            lineageCode = firstIdentity.lineageCode,
            ancestorSpeciesId = parent.id,
            changes = listOf(
                SpeciesMutationChange(
                    MutationTraitReference.from(ColorTrait.RAINBOW_COLORATION)!!,
                    added = true,
                ),
            ),
        )
        val first = EvolvingSpeciesCatalog.createDefinition(parent, firstRecord)
        EvolvingSpeciesCatalog.add(firstRecord, first)

        val grandchild = EvolvingSpeciesCatalog.nextIdentity(first)

        assertEquals("M1.1", grandchild.lineageCode)
        assertTrue(grandchild.displayName.endsWith("⟦M1.1⟧"))
        assertTrue(first in EarthTreeOfLife.visibleChildren(parent))
    }

    @Test
    fun `mutation is rejected when removing required compilation capabilities`() {
        val parent = EarthSpeciesCatalog.ALL.first()
        val withoutReproduction = parent.copy(
            id = "${parent.id}~invalid",
            traits = parent.traits.filterNot { trait ->
                TraitCapability.REPRODUCTION in
                    trait.baseTrait.capabilitiesAt(trait.authoredLevel)
            },
        )

        assertFalse(withoutReproduction.traitProfile().capabilities.contains(TraitCapability.REPRODUCTION))
        assertNull(EcologyMutations.compile(withoutReproduction))
    }

    @Test
    fun `mutation record survives a save round trip`() {
        val original = MutatedSpeciesRecord(
            id = "ancestor~m1",
            displayName = "Ancestor ⟦M1⟧",
            lineageCode = "M1",
            ancestorSpeciesId = "ancestor",
            changes = listOf(
                SpeciesMutationChange(
                    MutationTraitReference.from(CommonTrait.EYES)!!,
                    added = true,
                    level = 2,
                ),
            ),
        )
        val saveFile = Files.createTempFile("species-mutation", ".json.gz")
        try {
            Serialization.save(saveFile.toString(), original)
            val restored = Serialization.load(saveFile.toString(), MutatedSpeciesRecord::class.java)

            assertEquals(original, restored)
        } finally {
            saveFile.deleteIfExists()
        }
    }

    private fun mutationRecord(
        parent: SpeciesDefinition,
        changes: List<SpeciesMutationChange>,
    ) = MutatedSpeciesRecord(
        id = "${parent.id}~m1",
        displayName = "${parent.displayName} ⟦M1⟧",
        lineageCode = "M1",
        ancestorSpeciesId = parent.id,
        changes = changes,
    )
}
