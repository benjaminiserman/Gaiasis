package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val angiosperm = EarthSpeciesCatalog.sessile(
    "angiosperm",
    SizeClass.SMALL,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
    CommonTrait.PHOTOSYNTHETIC_SURFACE,
    CommonTrait.ROOTED_BODY,
    CommonTrait.FLOWERS,
)
