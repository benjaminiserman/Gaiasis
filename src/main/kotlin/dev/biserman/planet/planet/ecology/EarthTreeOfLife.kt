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

/** The playable Earth catalog, presented as extant leaves and their ancestor chain. */
object EarthTreeOfLife {
    private val extantById by lazy { EarthSpeciesCatalog.ALL.associateBy { it.id } }
    private val authoredRoots by lazy {
        // Enter through the catalog so its clade source files finish initializing
        // before their root values are read back here.
        EarthSpeciesCatalog.ALL.size
        // Fish is the vertebrate root because tetrapods descend from bony fish in this model.
        listOf(animal, fungus, plant)
    }

    val roots: List<SpeciesDefinition> by lazy {
        authoredRoots.filter(::hasExtantDescendant)
    }

    val nodesById: Map<String, SpeciesDefinition> by lazy {
        buildMap {
            fun addSubtree(node: SpeciesDefinition) {
                if (!hasExtantDescendant(node)) return
                // A handful of catalog leaves refine a prototype without renaming it
                // (for example termite -> termite). They share one visible node.
                putIfAbsent(node.id, node)
                node.descendants.forEach(::addSubtree)
            }
            roots.forEach(::addSubtree)
        }
    }

    fun visibleChildren(node: SpeciesDefinition): List<SpeciesDefinition> =
        node.descendants
            .flatMap { child ->
                when {
                    !hasExtantDescendant(child) -> emptyList()
                    child.id == node.id -> visibleChildren(child)
                    else -> listOf(child)
                }
            }
            .distinctBy { it.id }
            .sortedBy { it.displayName }

    fun isExtant(node: SpeciesDefinition): Boolean = node.id in extantById

    /** Uses the final playable phenotype when an ancestor prototype shares its id. */
    fun inspectionDefinition(node: SpeciesDefinition): SpeciesDefinition =
        extantById[node.id] ?: node

    fun extantDescendants(node: SpeciesDefinition): List<SpeciesDefinition> = buildList {
        if (isExtant(node)) add(extantById.getValue(node.id))
        node.descendants.forEach { descendant ->
            if (hasExtantDescendant(descendant)) addAll(extantDescendants(descendant))
        }
    }.distinctBy { it.id }

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
        EarthSpeciesCatalog.ALL.filterNot { it.id in excludedSpeciesIds }

    fun globalPopulation(planet: Planet, node: SpeciesDefinition): GlobalPopulation {
        val descendantIds = extantDescendants(node).mapTo(hashSetOf()) { it.id }
        val massById = PlanetEcology.compiled.species
            .filter { it.id in descendantIds }
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
        isExtant(node) || node.descendants.any(::hasExtantDescendant)
}
