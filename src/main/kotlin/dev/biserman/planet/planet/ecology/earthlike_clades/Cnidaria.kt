package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.atLevel

val cnidarian = animal.descend(
    "cnidarian",
    SizeClass.TINY,
    CommonTrait.SLOW_METABOLISM.atLevel(2),
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.TENTACLES,
    CommonTrait.PULSING_BELL,
    CommonTrait.BODY_REGENERATION,
    CommonTrait.VENOM_DELIVERY.atLevel(2),
    CommonTrait.SCENT.atLevel(1),
    ColorTrait.PALE_COLORATION,
    minus = listOf(CommonTrait.VASCULAR_SYSTEM)
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
        CommonTrait.PULSING_BELL,
    ),
)
val seaAnemone = anthozoan.descend(
    "sea anemone",
    SizeClass.SMALL,
    ColorTrait.PURPLE_COLORATION
)

val stonyCoral = anthozoan.descend(
    "stony coral",
    SizeClass.SMALL,
    ColorTrait.BROWN_COLORATION,
    CommonTrait.RIGID_COLONY_FRAMEWORK,
    CommonTrait.REEF_BUILDING,
    CommonTrait.INTERNAL_PHOTOSYMBIONTS,
    CommonTrait.WARM_WATER_ENZYMES,
)
val seaFan = anthozoan.descend(
    "sea fan",
    SizeClass.SMALL,
    CommonTrait.SUSPENSION_FEEDING_TENTACLES,
    ColorTrait.RED_COLORATION
)
