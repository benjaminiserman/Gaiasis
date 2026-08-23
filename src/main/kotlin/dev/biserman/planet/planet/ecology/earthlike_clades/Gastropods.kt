package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val gastropod = EarthSpeciesCatalog.animal(
    "gastropod",
    SizeClass.TINY,
    CommonTrait.ECTOTHERMY,
    CommonTrait.MUSCULAR_FOOT,
    CommonTrait.SLOW_METABOLISM,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.SOLITARY,
    ColorTrait.BROWN_CAMOUFLAGE
)
