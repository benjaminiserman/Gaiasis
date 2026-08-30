package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val mollusc = EarthSpeciesCatalog.animal(
    "mollusc",
    SizeClass.TINY,
    CommonTrait.ECTOTHERMY,
    CommonTrait.SLOW_METABOLISM,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.SOLITARY,
    CommonTrait.VASCULAR_SYSTEM,
    CommonTrait.MANTLED_BODY,
    CommonTrait.GILLS,
    CommonTrait.CATHEMERAL,
    CommonTrait.POOR_HEARING,
    CommonTrait.EYES,
    ColorTrait.BROWN_COLORATION,
    CommonTrait.SALTWATER_OSMOREGULATION,
)
val clam = mollusc.descend(
    "clam",
    SizeClass.TINY,
    CommonTrait.GILL_RAKERS,
    CommonTrait.PROTECTIVE_SHELL,
    CommonTrait.SUBSTRATE_HOLDFAST
)

val gastropod = mollusc.descend(
    "gastropod",
    SizeClass.TINY,
    CommonTrait.SLIMY_SKIN,
    CommonTrait.MUSCULAR_FOOT,
    CommonTrait.STICKY_FEET,
    CommonTrait.GRAZING_MOUTHPARTS,
)
val snail = gastropod.descend(
    "snail",
    SizeClass.TINY,
    CommonTrait.PROTECTIVE_SHELL,
)
val landSnail = snail.descend(
    "land snail",
    SizeClass.TINY,
    CommonTrait.TRACHEA,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.DECOMPOSING_ENZYMES,
    CommonTrait.NOCTURNAL
)
val seaSnail = snail.descend(
    "sea snail",
    SizeClass.TINY,
)
val slug = gastropod.descend(
    "slug",
    SizeClass.TINY,
)
val landSlug = slug.descend(
    "land slug",
    SizeClass.TINY,
    CommonTrait.TRACHEA,
    CommonTrait.NOCTURNAL,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.DECOMPOSING_ENZYMES,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.FRUIT_EATING_MOUTHPARTS
)
val seaSlug = slug.descend(
    "sea slug",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.APOSEMATIC_COLORATION,
    ColorTrait.RAINBOW_COLORATION,
    minus = listOf(CommonTrait.GRAZING_MOUTHPARTS)
)

val cephalopod = mollusc.descend(
    "cephalopod",
    SizeClass.SMALL,
    CommonTrait.INK_CLOUD,
    CommonTrait.TENTACLES,
    CommonTrait.JET_PROPULSION,
    CommonTrait.GRASPING_TENTACLES,
    CommonTrait.SUCTION_CUPS,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.LIMB_REGROWTH,
    CommonTrait.RAPID_GROWTH,
    minus = listOf(CommonTrait.SLOW_METABOLISM)
)
val octopus = cephalopod.descend(
    "octopus",
    SizeClass.SMALL,
    CommonTrait.INTELLIGENT,
    ColorTrait.ADAPTIVE_COLORATION,
)
val squid = cephalopod.descend(
    "squid",
    SizeClass.SMALL,
    CommonTrait.INTELLIGENT,
    CommonTrait.DEEP_DIVING_PHYSIOLOGY,
    CommonTrait.BUOYANCY_BLADDER,
    ColorTrait.PALE_COLORATION,
)
