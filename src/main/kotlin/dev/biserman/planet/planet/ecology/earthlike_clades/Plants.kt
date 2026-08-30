package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val landPlant = EarthSpeciesCatalog.sessile(
    "land plant",
    SizeClass.TINY,
    CommonTrait.PASSIVE_RESPIRATION,
    CommonTrait.PRIMITIVE_BODY,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
    CommonTrait.PHOTOSYNTHETIC_SURFACE,
)
val algae = landPlant.descend(
    "algae",
    SizeClass.MINUSCULE,
    CommonTrait.CLONAL_PROPAGATION,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.FRESHWATER_OSMOREGULATION,
)

// Nonvascular land plants
val bryophyte = landPlant.descend(
    "bryophyte",
    SizeClass.TINY,
    CommonTrait.SURFACE_HOLDFAST,
    CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
    CommonTrait.INTERWOVEN_BODY,
)
val moss = bryophyte.descend(
    "moss",
    SizeClass.TINY,
    CommonTrait.FROST_HARDENED_TISSUES,
)

// Vascular land plants
val vascularPlant = landPlant.descend(
    "vascular plant",
    SizeClass.SMALL,
    CommonTrait.VASCULAR_SYSTEM,
    CommonTrait.ROOTED_BODY,
)
val fern = vascularPlant.descend(
    "fern",
    SizeClass.SMALL,
    CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
    CommonTrait.SHADE_FRONDS,
    CommonTrait.PERENNIAL_STORAGE_TISSUE,
)
val conifer = vascularPlant.descend(
    "conifer",
    SizeClass.LARGE,
    CommonTrait.NEEDLE_LEAVES,
    CommonTrait.CANOPY_GROWTH,
    CommonTrait.FROST_HARDENED_TISSUES,
    CommonTrait.SEASONAL_LEAF_DORMANCY,
    CommonTrait.DEEP_ROOT_SYSTEM,
    CommonTrait.SLOW_GROWTH,
)

val angiosperm = vascularPlant.descend(
    "angiosperm",
    SizeClass.SMALL,
    CommonTrait.FLOWERS,
)
val forb = angiosperm.descend(
    "forb",
    SizeClass.SMALL,
    CommonTrait.NECTARIES,
)
val grass = angiosperm.descend(
    "grass",
    SizeClass.SMALL,
    CommonTrait.INTERWOVEN_BODY,
)
val vine = angiosperm.descend(
    "vine",
    SizeClass.MEDIUM,
    CommonTrait.CANOPY_GROWTH,
    CommonTrait.SHADE_FRONDS,
)
val broadLeafTree = angiosperm.descend(
    "broad-leaf tree",
    SizeClass.LARGE,
    CommonTrait.LARGE_EVERGREEN_LEAVES,
    CommonTrait.CANOPY_GROWTH,
    CommonTrait.DEEP_ROOT_SYSTEM,
    CommonTrait.SLOW_GROWTH,
)
val succulent = angiosperm.descend(
    "succulent",
    SizeClass.MEDIUM,
    CommonTrait.SUCCULENT_STEM,
    CommonTrait.FROST_SENSITIVE_SUCCULENT_TISSUES,
    CommonTrait.WAXY_CUTICLE,
    CommonTrait.DEEP_ROOT_SYSTEM,
)
val aquaticAngiosperm = angiosperm.descend(
    "aquatic angiosperm",
    SizeClass.SMALL,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.SUBSTRATE_HOLDFAST,
    CommonTrait.FLOATING_FRONDS,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    minus = listOf(CommonTrait.ROOTED_BODY),
)
