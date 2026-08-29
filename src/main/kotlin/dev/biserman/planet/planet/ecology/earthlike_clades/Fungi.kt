package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val fungus = EarthSpeciesCatalog.sessile(
    "fungus",
    SizeClass.TINY,
    CommonTrait.PASSIVE_RESPIRATION,
    CommonTrait.MYCELIAL_BODY,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
    CommonTrait.DESICCATION_RESISTANT_PROPAGULES,
    CommonTrait.DECOMPOSING_ENZYMES,
)

val fruitingFungus = fungus.descend(
    "fruiting fungus",
    SizeClass.SMALL,
    CommonTrait.FRUITING_BODY,
    CommonTrait.PERENNIAL_STORAGE_TISSUE,
)
val mushroom = fruitingFungus.descend(
    "mushroom",
    SizeClass.SMALL,
)
val shelfFungus = fruitingFungus.descend(
    "shelf fungus",
    SizeClass.SMALL,
    CommonTrait.SLOW_GROWTH,
)

val mold = fungus.descend(
    "mold",
    SizeClass.TINY,
    CommonTrait.RAPID_GROWTH,
    CommonTrait.INTERWOVEN_MAT,
)
