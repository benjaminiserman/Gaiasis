package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.SpeciesDefinition
import dev.biserman.planet.planet.ecology.SpeciesTrait

/**
 * Similar to EarthSpeciesCatalog, but a bit less specific to Earth
 */
object EarthlikeClades {
    val majorCreatureGroups = listOf(
        mammal,
        reptile,
        amphibian,
        fish,
        bird,
        insect,
        arachnid,
        gastropod,
        crustacean,
        bivalve,
        cephalopod,
        moss,
        fern,
        conifer,
        angiosperm,
        mold,
        mushroom,
    )

    val forb = EarthSpeciesCatalog.sessile(
        "forb",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.FLOWERS,
        CommonTrait.NECTARIES,
    )

    val grass = EarthSpeciesCatalog.sessile(
        "grass",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.FLOWERS,
        CommonTrait.INTERWOVEN_MAT,
    )

    val vine = EarthSpeciesCatalog.sessile(
        "vine",
        SizeClass.MEDIUM,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.CANOPY_GROWTH,
        CommonTrait.SHADE_FRONDS,
        CommonTrait.FLOWERS,
    )

    val broadLeafTree = EarthSpeciesCatalog.sessile(
        "broad-leaf tree",
        SizeClass.LARGE,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.LARGE_EVERGREEN_LEAVES,
        CommonTrait.ROOTED_BODY,
        CommonTrait.CANOPY_GROWTH,
        CommonTrait.FLOWERS,
    )
}

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
