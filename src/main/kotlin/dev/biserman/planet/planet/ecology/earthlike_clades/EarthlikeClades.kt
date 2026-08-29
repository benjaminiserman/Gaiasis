package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.SpeciesDefinition
import dev.biserman.planet.planet.ecology.SpeciesTrait

fun SpeciesDefinition.extend(
    name: String,
    sizeClass: SizeClass,
    vararg adaptations: SpeciesTrait,
    minus: List<SpeciesTrait> = listOf(),
) = copy(
    id = EarthSpeciesCatalog.idFromName(name),
    displayName = name,
    sizeClass = sizeClass,
    traits = traits.let { inheritedTraits ->
        val replacedGroups = inheritedTraits.mapNotNull { it.group }
            .toSet()
            .intersect(adaptations.mapNotNull { it.group }.toSet())

        inheritedTraits.filter { it.group !in replacedGroups && it !in minus } + adaptations
    },
)

fun SpeciesDefinition.descend(
    name: String,
    sizeClass: SizeClass,
    vararg adaptations: SpeciesTrait,
    minus: List<SpeciesTrait> = listOf(),
    motile: Boolean = this.motile,
): SpeciesDefinition {
    val descendant = copy(
        id = EarthSpeciesCatalog.idFromName(name),
        displayName = name,
        sizeClass = sizeClass,
        motile = motile,
        traits = traits.let { inheritedTraits ->
            val replacedGroups = inheritedTraits.mapNotNull { it.group }
                .toSet()
                .intersect(adaptations.mapNotNull { it.group }.toSet())

            inheritedTraits.filter { it.group !in replacedGroups && it !in minus } + adaptations
        },
        ancestorSpeciesId = id,
    )
    descendants.add(descendant)
    return descendant
}
