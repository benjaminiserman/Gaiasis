package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val fish = EarthSpeciesCatalog.animal(
    "fish",
    SizeClass.SMALL,
    CommonTrait.ECTOTHERMY,
    CommonTrait.GILLS,
    CommonTrait.AQUATIC_FLIPPERS,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.CATHEMERAL,
    ColorTrait.COUNTERSHADE_CAMOUFLAGE
)
