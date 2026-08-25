package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val bivalve = EarthSpeciesCatalog.sessile(
    "bivalve",
    SizeClass.SMALL,
    CommonTrait.ECTOTHERMY,
    CommonTrait.PROTECTIVE_SHELL,
    CommonTrait.GILLS,
    CommonTrait.GILL_RAKERS,
    CommonTrait.MUSCULAR_FOOT,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.CATHEMERAL,
    CommonTrait.SOLITARY,
    ColorTrait.BROWN_COLORATION
)
