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
    CommonTrait.GILLS,
    CommonTrait.CATHEMERAL,
    CommonTrait.POOR_HEARING,
    ColorTrait.BROWN_COLORATION
)
val clam = mollusc.extend(
    "clam",
    SizeClass.TINY,
    CommonTrait.GILL_RAKERS,
    CommonTrait.PROTECTIVE_SHELL,
    CommonTrait.SUBSTRATE_HOLDFAST
)

val gastropod = mollusc.extend(
    "gastropod",
    SizeClass.TINY,
    CommonTrait.SLIMY_SKIN,
    CommonTrait.MUSCULAR_FOOT,
    CommonTrait.STICKY_FEET,
    CommonTrait.GRAZING_MOUTHPARTS,
)
val snail = gastropod.extend(
    "snail",
    SizeClass.TINY,
    CommonTrait.PROTECTIVE_SHELL,
)
val landSnail = snail.extend(
    "land snail",
    SizeClass.TINY,
    CommonTrait.TRACHEA,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.DECOMPOSING_ENZYMES,
    CommonTrait.NOCTURNAL
)
val seaSnail = snail.extend(
    "sea snail",
    SizeClass.TINY,
)
val slug = gastropod.extend(
    "slug",
    SizeClass.TINY,
)
val landSlug = slug.extend(
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
val seaSlug = slug.extend(
    "sea slug",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.APOSEMATIC_COLORATION,
    ColorTrait.RAINBOW_COLORATION,
    minus = listOf(CommonTrait.GRAZING_MOUTHPARTS)
)

val cephalopod = mollusc.extend(
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
val octopus = cephalopod.extend(
    "octopus",
    SizeClass.SMALL,
    CommonTrait.INTELLIGENT,
    ColorTrait.ADAPTIVE_COLORATION,
)
val squid = cephalopod.extend(
    "squid",
    SizeClass.SMALL,
    CommonTrait.INTELLIGENT,
    CommonTrait.DEEP_DIVING_PHYSIOLOGY,
    CommonTrait.BUOYANCY_BLADDER,
    ColorTrait.PALE_COLORATION,
)
