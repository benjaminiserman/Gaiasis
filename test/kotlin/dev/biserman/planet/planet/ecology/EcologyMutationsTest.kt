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
    fun `mutation changes at most two shared traits and identifies its lineage`() {
        val parent = EarthSpeciesCatalog.ALL.first()
        val proposal = EcologyMutations.propose(parent, Random(7))

        assertTrue(proposal.record.changes.size in 1..2)
        assertEquals(parent.id, proposal.definition.ancestorSpeciesId)
        assertTrue(proposal.definition.displayName.endsWith("⟦M1⟧"))
        assertTrue(proposal.definition.id.startsWith("${parent.id}~m"))
        assertEquals(
            proposal.record.changes.map { it.trait }.toSet().size,
            proposal.record.changes.size,
        )
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
}
