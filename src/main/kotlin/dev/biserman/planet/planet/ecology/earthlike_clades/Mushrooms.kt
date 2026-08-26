package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val mushroom = EarthSpeciesCatalog.sessile(
    "mushroom",
    SizeClass.SMALL,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.SURFACE_HOLDFAST,
    CommonTrait.DECOMPOSING_ENZYMES,
    CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
    CommonTrait.PERENNIAL_STORAGE_TISSUE,
)
