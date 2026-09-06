package dev.biserman.planet.planet.ecology

import dev.biserman.planet.planet.Planet
import dev.biserman.planet.planet.ecology.earthlike_clades.animal
import dev.biserman.planet.planet.ecology.earthlike_clades.fungus
import dev.biserman.planet.planet.ecology.earthlike_clades.plant

enum class TreeOfLifeInclusion {
    INCLUDED,
    EXCLUDED,
    MIXED,
}

data class GlobalPopulation(
    val individuals: Double,
    val biomassKg: Double,
    val occupiedTiles: Int,
)

enum class TraitDifferenceKind {
    ADDED,
    REMOVED,
}

data class TraitDifference(
    val trait: SpeciesTrait,
    val kind: TraitDifferenceKind,
)

/** The playable Earth catalog, presented as extant leaves and their ancestor chain. */
object EarthTreeOfLife {
    private val authoredRoots by lazy {
        // Enter through the catalog so its clade source files finish initializing
        // before their root values are read back here.
        EarthSpeciesCatalog.ALL.size
        // Fish is the vertebrate root because tetrapods descend from bony fish in this model.
        listOf(animal, fungus, plant)
    }

    val revision: Long
        get() = EvolvingSpeciesCatalog.revision

    val roots: List<SpeciesDefinition>
        get() = authoredRoots.filter(::hasExtantDescendant)

    val nodesById: Map<String, SpeciesDefinition>
        get() = buildMap {
            fun addSubtree(node: SpeciesDefinition) {
                if (!hasExtantDescendant(node)) return
                // A handful of catalog leaves refine a prototype without renaming it
                // (for example termite -> termite). They share one visible node.
                putIfAbsent(node.id, node)
                visibleChildren(node).forEach(::addSubtree)
            }
            roots.forEach(::addSubtree)
        }

    fun visibleChildren(node: SpeciesDefinition): List<SpeciesDefinition> =
        (node.descendants + EvolvingSpeciesCatalog.mutations.filter { it.ancestorSpeciesId == node.id })
            .flatMap { child ->
                when {
                    !hasExtantDescendant(child) -> emptyList()
                    child.id == node.id -> visibleChildren(child)
                    else -> listOf(child)
                }
            }
            .distinctBy { it.id }
            .sortedBy { it.displayName }

    fun isExtant(node: SpeciesDefinition): Boolean =
        EvolvingSpeciesCatalog.extantSpecies.any { it.id == node.id }

    /** Uses the final playable phenotype when an ancestor prototype shares its id. */
    fun inspectionDefinition(node: SpeciesDefinition): SpeciesDefinition =
        EvolvingSpeciesCatalog.extantSpecies.firstOrNull { it.id == node.id } ?: node

    /** The phenotype from which this visible node directly descends. */
    fun directAncestor(node: SpeciesDefinition): SpeciesDefinition? {
        val inspected = inspectionDefinition(node)
        val ancestorId = inspected.ancestorSpeciesId ?: return null
        // A few authored leaves refine a prototype without changing its id. In
        // that case the visible prototype passed in is the actual ancestor.
        if (ancestorId == node.id && inspected !== node) return node
        return nodesById[ancestorId]?.let(::inspectionDefinition)
    }

    /** Added and removed trait levels relative to the node's direct ancestor. */
    fun traitDifferencesFromAncestor(node: SpeciesDefinition): List<TraitDifference> {
        val inspected = inspectionDefinition(node)
        val ancestor = directAncestor(node) ?: return emptyList()
        val currentKeys = inspected.traits.mapTo(hashSetOf(), ::traitKey)
        val ancestorKeys = ancestor.traits.mapTo(hashSetOf(), ::traitKey)
        return buildList {
            inspected.traits
                .filter { traitKey(it) !in ancestorKeys }
                .sortedBy { it.displayName }
                .forEach { add(TraitDifference(it, TraitDifferenceKind.ADDED)) }
            ancestor.traits
                .filter { traitKey(it) !in currentKeys }
                .sortedBy { it.displayName }
                .forEach { add(TraitDifference(it, TraitDifferenceKind.REMOVED)) }
        }
    }

    fun extantDescendants(node: SpeciesDefinition): List<SpeciesDefinition> = buildList {
        if (isExtant(node)) add(inspectionDefinition(node))
        visibleChildren(node).forEach { descendant ->
            if (hasExtantDescendant(descendant)) addAll(extantDescendants(descendant))
        }
    }.distinctBy { it.id }

    /** Catalog species that still have a population somewhere on this planet. */
    fun currentlyExtantSpeciesIds(planet: Planet): Set<String> {
        val catalogIds = EvolvingSpeciesCatalog.extantSpecies.mapTo(hashSetOf()) { it.id }
        return planet.planetTiles.values
            .asSequence()
            .flatMap { it.ecosystem.populations.asSequence() }
            .filter { it.speciesId in catalogIds && it.activeBiomassKg + it.dormantBiomassKg > 0.0 }
            .mapTo(hashSetOf()) { it.speciesId }
    }

    fun inclusion(node: SpeciesDefinition, excludedSpeciesIds: Set<String>): TreeOfLifeInclusion {
        val descendants = extantDescendants(node)
        val excludedCount = descendants.count { it.id in excludedSpeciesIds }
        return when (excludedCount) {
            0 -> TreeOfLifeInclusion.INCLUDED
            descendants.size -> TreeOfLifeInclusion.EXCLUDED
            else -> TreeOfLifeInclusion.MIXED
        }
    }

    fun setIncluded(
        node: SpeciesDefinition,
        included: Boolean,
        excludedSpeciesIds: MutableSet<String>,
    ) {
        val ids = extantDescendants(node).mapTo(mutableSetOf()) { it.id }
        if (included) excludedSpeciesIds.removeAll(ids) else excludedSpeciesIds.addAll(ids)
    }

    fun randomizationCandidates(excludedSpeciesIds: Set<String>): List<SpeciesDefinition> =
        EvolvingSpeciesCatalog.extantSpecies.filterNot { it.id in excludedSpeciesIds }

    /** Population of this node's extant species and all of its extant descendants. */
    fun globalPopulation(planet: Planet, node: SpeciesDefinition): GlobalPopulation =
        globalPopulation(planet, extantDescendants(node).mapTo(hashSetOf()) { it.id })

    /** Population belonging only to this species, excluding any descendants. */
    fun ownGlobalPopulation(planet: Planet, node: SpeciesDefinition): GlobalPopulation =
        globalPopulation(planet, if (isExtant(node)) setOf(node.id) else emptySet())

    /** Population of extant descendant species, excluding this species when it is also extant. */
    fun descendantGlobalPopulation(planet: Planet, node: SpeciesDefinition): GlobalPopulation =
        globalPopulation(
            planet,
            extantDescendants(node)
                .asSequence()
                .map { it.id }
                .filter { it != node.id }
                .toSet(),
        )

    private fun globalPopulation(planet: Planet, speciesIds: Set<String>): GlobalPopulation {
        val massById = PlanetEcology.compiled.species
            .filter { it.id in speciesIds }
            .associate { it.id to it.physiology.massKg }
        var individuals = 0.0
        var biomassKg = 0.0
        var occupiedTiles = 0
        planet.planetTiles.values.forEach { tile ->
            var occupied = false
            tile.ecosystem.populations.forEach { population ->
                val massKg = massById[population.speciesId] ?: return@forEach
                val localBiomass = population.activeBiomassKg + population.dormantBiomassKg
                biomassKg += localBiomass
                individuals += localBiomass / massKg
                occupied = true
            }
            if (occupied) occupiedTiles++
        }
        return GlobalPopulation(individuals, biomassKg, occupiedTiles)
    }

    private fun hasExtantDescendant(node: SpeciesDefinition): Boolean =
        isExtant(node) ||
            node.descendants.any(::hasExtantDescendant) ||
            EvolvingSpeciesCatalog.mutations.any {
                it.ancestorSpeciesId == node.id && hasExtantDescendant(it)
            }

    private fun traitKey(trait: SpeciesTrait): Pair<SpeciesTrait, Int> =
        trait.baseTrait to trait.authoredLevel
}
