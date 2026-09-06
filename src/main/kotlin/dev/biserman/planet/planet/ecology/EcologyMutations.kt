package dev.biserman.planet.planet.ecology

import dev.biserman.planet.history.HistoryCalendar
import kotlin.random.Random

enum class MutationTraitKind {
    COMMON,
    COLOR,
}

data class MutationTraitReference(
    val kind: MutationTraitKind,
    val name: String,
) {
    fun resolve(): SpeciesTrait = when (kind) {
        MutationTraitKind.COMMON -> CommonTrait.valueOf(name)
        MutationTraitKind.COLOR -> ColorTrait.valueOf(name)
    }

    companion object {
        fun from(trait: SpeciesTrait): MutationTraitReference? = when (val base = trait.baseTrait) {
            is CommonTrait -> MutationTraitReference(MutationTraitKind.COMMON, base.name)
            is ColorTrait -> MutationTraitReference(MutationTraitKind.COLOR, base.name)
            else -> null
        }
    }
}

data class SpeciesMutationChange(
    val trait: MutationTraitReference,
    val added: Boolean,
)

/** Serializable description of a generated species, relative to its direct ancestor. */
data class MutatedSpeciesRecord(
    val id: String,
    val displayName: String,
    val lineageCode: String,
    val ancestorSpeciesId: String,
    val changes: List<SpeciesMutationChange>,
)

/** Active per-planet additions to the authored Earth catalog. */
object EvolvingSpeciesCatalog {
    private var records: List<MutatedSpeciesRecord> = emptyList()
    private var generatedDefinitions: List<SpeciesDefinition> = emptyList()

    var revision: Long = 0
        private set

    val mutations: List<SpeciesDefinition>
        get() = generatedDefinitions

    val extantSpecies: List<SpeciesDefinition>
        get() = EarthSpeciesCatalog.ALL + generatedDefinitions

    fun activate(savedRecords: List<MutatedSpeciesRecord>) {
        val definitionsById = EarthSpeciesCatalog.ALL.associateByTo(linkedMapOf()) { it.id }
        val restored = savedRecords.map { record ->
            val ancestor = requireNotNull(definitionsById[record.ancestorSpeciesId]) {
                "Unknown mutation ancestor ${record.ancestorSpeciesId} for ${record.id}"
            }
            createDefinition(ancestor, record).also { definitionsById[it.id] = it }
        }
        records = savedRecords.toList()
        generatedDefinitions = restored
        revision++
    }

    fun add(record: MutatedSpeciesRecord, definition: SpeciesDefinition) {
        records = records + record
        generatedDefinitions = generatedDefinitions + definition
        revision++
    }

    fun nextIdentity(parent: SpeciesDefinition): MutationIdentity {
        val parentCode = records.firstOrNull { it.id == parent.id }?.lineageCode
        val siblingNumber = records.count { it.ancestorSpeciesId == parent.id } + 1
        val lineageCode = parentCode?.let { "$it.$siblingNumber" } ?: "M$siblingNumber"
        val baseName = parent.displayName.substringBefore(" ⟦M")
        return MutationIdentity(
            id = "${parent.id}~m$siblingNumber",
            displayName = "$baseName ⟦$lineageCode⟧",
            lineageCode = lineageCode,
        )
    }

    fun createDefinition(
        ancestor: SpeciesDefinition,
        record: MutatedSpeciesRecord,
    ): SpeciesDefinition {
        val traits = ancestor.traits.toMutableList()
        record.changes.forEach { change ->
            val trait = change.trait.resolve()
            traits.removeAll { it.baseTrait == trait.baseTrait }
            if (change.added) traits += trait
        }
        return ancestor.copy(
            id = record.id,
            displayName = record.displayName,
            traits = traits,
            ancestorSpeciesId = ancestor.id,
            descendants = mutableListOf(),
        )
    }
}

data class MutationIdentity(
    val id: String,
    val displayName: String,
    val lineageCode: String,
)

data class ProposedMutation(
    val record: MutatedSpeciesRecord,
    val definition: SpeciesDefinition,
)

object EcologyMutations {
    const val MAXIMUM_TRAIT_CHANGES = 2
    const val FOUNDER_FRACTION = 0.10

    private val mutableTraits: List<SpeciesTrait> =
        (CommonTrait.entries.filterNot { it.invariantOnly } + ColorTrait.entries)
            .sortedBy { it.displayName }

    fun isMutationInterval(
        historyTurn: Long,
        intervalYears: Int = EcologyGlobals.mutationIntervalYears,
    ): Boolean =
        historyTurn > 0L &&
            historyTurn % (intervalYears.toLong() * HistoryCalendar.TURNS_PER_YEAR) == 0L

    fun propose(parent: SpeciesDefinition, random: Random): ProposedMutation {
        val identity = EvolvingSpeciesCatalog.nextIdentity(parent)
        val changeCount = random.nextInt(1, MAXIMUM_TRAIT_CHANGES + 1)
        val selected = mutableTraits.shuffled(random).take(changeCount)
        val presentTraits = parent.traits.mapTo(hashSetOf()) { it.baseTrait }
        val changes = selected.map { trait ->
            SpeciesMutationChange(
                trait = requireNotNull(MutationTraitReference.from(trait)),
                added = trait.baseTrait !in presentTraits,
            )
        }
        val record = MutatedSpeciesRecord(
            id = identity.id,
            displayName = identity.displayName,
            lineageCode = identity.lineageCode,
            ancestorSpeciesId = parent.id,
            changes = changes,
        )
        return ProposedMutation(record, EvolvingSpeciesCatalog.createDefinition(parent, record))
    }

    fun compile(definition: SpeciesDefinition): CompiledSpecies? = try {
        EcologyCompiler.compileSpecies(0, definition, EcologyNiches.defaults)
    } catch (_: IllegalArgumentException) {
        null
    } catch (_: IllegalStateException) {
        null
    }
}
