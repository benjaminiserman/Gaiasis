package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val insect = EarthSpeciesCatalog.animal(
    "insect",
    SizeClass.TINY,
    CommonTrait.ECTOTHERMY, CommonTrait.CLIMBING_LIMBS, CommonTrait.TERRESTRIAL_OVOSPORE, CommonTrait.WINGS, CommonTrait.MOLTING_EXOSKELETON, CommonTrait.SOLITARY, ColorTrait.BROWN_COLORATION
)
