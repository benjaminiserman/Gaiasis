package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val amphibian = EarthSpeciesCatalog.animal(
    "amphibian",
    SizeClass.TINY,
    CommonTrait.ECTOTHERMY,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.NOCTURNAL,
    CommonTrait.SOLITARY,
    CommonTrait.SLIMY_SKIN,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.TAIL,
    ColorTrait.GREEN_CAMOUFLAGE
)

// frogs -- doing a non-taxonomic breakdown of anura for cultural reasons
val anura = amphibian.descend(
    "anura",
    SizeClass.TINY,
    CommonTrait.LEAPING_LEGS,
    CommonTrait.PROJECTILE_TONGUE,
    CommonTrait.CROAKING_CALL,
    minus = listOf(CommonTrait.TAIL)
)
val frog = anura.descend(
    "frog",
    SizeClass.SMALL,
)
val treeFrog = anura.descend(
    "tree frog",
    SizeClass.SMALL,
    CommonTrait.CLIMBING_LIMBS,
    ColorTrait.GREEN_CAMOUFLAGE,
    minus = listOf(CommonTrait.AMPHIBIOUS_LIMBS)
)
val bullFrog = anura.descend(
    "bull frog",
    SizeClass.SMALL,
    CommonTrait.BULKY_BODY,
)
val toad = anura.descend(
    "toad",
    SizeClass.SMALL,
    CommonTrait.TOXIC_SKIN,
    ColorTrait.BROWN_CAMOUFLAGE,
    minus = listOf(CommonTrait.SLIMY_SKIN)
)

// salamanders
val caudata = amphibian.extend(
    "caudata",
    SizeClass.SMALL,
    CommonTrait.LIMB_REGROWTH,
    CommonTrait.AMBUSH_MUSCULATURE
)
val moleSalamander = caudata.extend(
    "mole salamander",
    SizeClass.SMALL,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.SEASONAL_TORPOR
)
val newt = caudata.extend(
    "newt",
    SizeClass.SMALL,
    CommonTrait.TOXIC_SKIN,
    CommonTrait.APOSEMATIC_COLORATION,
    CommonTrait.DIURNAL,
    ColorTrait.RED_CAMOUFLAGE,
    minus = listOf(CommonTrait.SLIMY_SKIN)
)
val giantSalamander = caudata.extend(
    "giant salamander",
    SizeClass.MEDIUM,
    CommonTrait.BULKY_BODY,
    CommonTrait.POOR_VISION,
    CommonTrait.BENTHIC_SUCTION_FEEDING,
    CommonTrait.SLOW_METABOLISM
)

// caecilians
val caecilian = amphibian.extend(
    "caecilian",
    SizeClass.TINY,
    CommonTrait.UNDULATING_BODY,
    CommonTrait.FOSSORIAL_LIVING,
    CommonTrait.MAMMARY_GLANDS,
    CommonTrait.VIVIPARITY,
    CommonTrait.POOR_VISION,
    ColorTrait.BLACK_CAMOUFLAGE,
    minus = listOf(CommonTrait.AMPHIBIOUS_LIMBS)
)

