package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val annelida = EarthSpeciesCatalog.animal(
    "annelida",
    SizeClass.TINY,
    CommonTrait.ECTOTHERMY,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.SOLITARY,
    CommonTrait.VASCULAR_SYSTEM,
    CommonTrait.PRIMITIVE_BODY,
    CommonTrait.PASSIVE_RESPIRATION,
    CommonTrait.CATHEMERAL,
    CommonTrait.POOR_HEARING,
    CommonTrait.POOR_SCENT_SENSE,
    CommonTrait.SEGMENTED_PHYSIQUE,
    CommonTrait.BODY_UNDULATION,
    CommonTrait.SALTWATER_OSMOREGULATION,
    ColorTrait.PALE_COLORATION
)

val earthworm = annelida.descend(
    "earthworm",
    SizeClass.TINY,
    CommonTrait.FOSSORIAL_LIVING,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.DECOMPOSING_ENZYMES,
    minus = listOf(CommonTrait.SALTWATER_OSMOREGULATION)
)

val bristleWorm = annelida.descend(
    "bristle worm",
    SizeClass.TINY,
    CommonTrait.PASSIVE_RESPIRATION,
    CommonTrait.ANTENNAE,
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
