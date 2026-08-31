package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.atLevel

val cnidarian = EarthSpeciesCatalog.animal(
    "cnidarian",
    SizeClass.TINY,
    CommonTrait.ECTOTHERMY,
    CommonTrait.PRIMITIVE_BODY,
    CommonTrait.SLOW_METABOLISM,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.CLONAL_PROPAGATION,
    CommonTrait.PASSIVE_RESPIRATION,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.TENTACLES,
    CommonTrait.BODY_REGENERATION,
    CommonTrait.VENOM_DELIVERY.atLevel(2),
    CommonTrait.SOLITARY,
    CommonTrait.SCENT.atLevel(1),
    CommonTrait.SALTWATER_OSMOREGULATION,
    ColorTrait.PALE_COLORATION,
)
val jellyfish = cnidarian.descend(
    "jellyfish",
    SizeClass.TINY,
    CommonTrait.GELATINOUS_BODY,
    CommonTrait.PULSING_BELL,
)

// Sessile, polyp-shaped cnidarians
val anthozoan = cnidarian.descend(
    "anthozoan",
    SizeClass.SMALL,
    CommonTrait.POLYP_BODY,
    minus = listOf(
        CommonTrait.ECTOTHERMY,
        CommonTrait.SOLITARY,
    ),
    motile = false,
)
val seaAnemone = anthozoan.descend(
    "sea anemone",
    SizeClass.SMALL,
    ColorTrait.PURPLE_COLORATION
)

val stonyCoral = anthozoan.descend(
    "stony coral",
    SizeClass.SMALL,
    ColorTrait.BROWN_PHOTOSYNTHETIC_PIGMENTS,
    CommonTrait.RIGID_COLONY_FRAMEWORK,
    CommonTrait.REEF_BUILDING,
    CommonTrait.INTERNAL_PHOTOSYMBIONTS,
    CommonTrait.WARM_WATER_ENZYMES,
)
val seaFan = anthozoan.descend(
    "sea fan",
    SizeClass.SMALL,
    CommonTrait.SUSPENSION_FEEDING_TENTACLES,
    CommonTrait.SLOW_GROWTH,
    ColorTrait.RED_COLORATION
)
