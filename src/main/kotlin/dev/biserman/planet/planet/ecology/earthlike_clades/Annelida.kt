package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.atLevel

val annelida = animal.descend(
    "annelida",
    SizeClass.TINY,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.HEARING.atLevel(1),
    CommonTrait.SCENT.atLevel(1),
    CommonTrait.SEGMENTED_PHYSIQUE,
    CommonTrait.BODY_UNDULATION,
)

val earthworm = annelida.descend(
    "earthworm",
    SizeClass.TINY,
    CommonTrait.FOSSORIAL_LIVING,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.HERMAPHRODITISM,
    CommonTrait.DECOMPOSING_ENZYMES,
    CommonTrait.BODY_REGENERATION,
    minus = listOf(CommonTrait.SALTWATER_OSMOREGULATION)
)

val bristleWorm = annelida.descend(
    "bristle worm",
    SizeClass.TINY,
    CommonTrait.PASSIVE_RESPIRATION,
    CommonTrait.ANTENNAE.atLevel(2),
    CommonTrait.MARINE_SNOW_COLLECTORS,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
)

val leech = annelida.descend(
    "leech",
    SizeClass.TINY,
    CommonTrait.SUCKING_PROBOSCIS,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.FAT_RESERVES,
    ColorTrait.BLACK_COLORATION
)
