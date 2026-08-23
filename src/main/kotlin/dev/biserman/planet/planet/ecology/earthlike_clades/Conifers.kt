package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val conifer = EarthSpeciesCatalog.sessile(
    "conifer",
    SizeClass.LARGE,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
    CommonTrait.NEEDLE_LEAVES,
    CommonTrait.ROOTED_BODY,
    CommonTrait.CANOPY_GROWTH,
    CommonTrait.FROST_HARDENED_TISSUES,
    CommonTrait.SEASONAL_LEAF_DORMANCY,
)
