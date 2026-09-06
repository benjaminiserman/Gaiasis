package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.SpeciesDefinition
import dev.biserman.planet.planet.ecology.SpeciesTrait
import dev.biserman.planet.planet.ecology.baseTrait

val animal = EarthSpeciesCatalog.animal(
    "animal",
    SizeClass.SMALL,
    CommonTrait.ECTOTHERMY,
    CommonTrait.PASSIVE_RESPIRATION,
    CommonTrait.CLONAL_PROPAGATION,
    CommonTrait.CATHEMERAL,
    CommonTrait.PRIMITIVE_BODY,
    CommonTrait.SOLITARY,
    CommonTrait.VASCULAR_SYSTEM,
    CommonTrait.SALTWATER_OSMOREGULATION,
    ColorTrait.PALE_COLORATION
)

fun SpeciesDefinition.extend(
    name: String,
    sizeClass: SizeClass,
    vararg adaptations: SpeciesTrait,
    minus: List<SpeciesTrait> = listOf(),
) = copy(
    id = EarthSpeciesCatalog.idFromName(name),
    displayName = name,
    sizeClass = sizeClass,
    traits = mergeInheritedTraits(traits, adaptations, minus),
    descendants = mutableListOf(),
)

fun SpeciesDefinition.descend(
    name: String,
    sizeClass: SizeClass,
    vararg adaptations: SpeciesTrait,
    minus: List<SpeciesTrait> = listOf(),
): SpeciesDefinition {
    val descendant = copy(
        id = EarthSpeciesCatalog.idFromName(name),
        displayName = name,
        sizeClass = sizeClass,
        traits = mergeInheritedTraits(traits, adaptations, minus),
        ancestorSpeciesId = id,
        descendants = mutableListOf(),
    )
    descendants.add(descendant)
    return descendant
}

private fun mergeInheritedTraits(
    inheritedTraits: List<SpeciesTrait>,
    adaptations: Array<out SpeciesTrait>,
    minus: List<SpeciesTrait>,
): List<SpeciesTrait> {
    val adaptationTraits = adaptations.map { it.baseTrait }.toSet()
    val removedTraits = minus.map { it.baseTrait }.toSet()
    val replacedGroups = inheritedTraits.mapNotNull { it.baseTrait.group }
        .toSet()
        .intersect(adaptations.mapNotNull { it.baseTrait.group }.toSet())

    return inheritedTraits.filter { inherited ->
        inherited.baseTrait.group !in replacedGroups &&
            inherited.baseTrait !in removedTraits &&
            inherited.baseTrait !in adaptationTraits
    } + adaptations
}
