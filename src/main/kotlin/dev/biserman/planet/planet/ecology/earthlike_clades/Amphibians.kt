package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.atLevel

val amphibian = EarthSpeciesCatalog.animal(
    "amphibian",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.ECTOTHERMY,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.NOCTURNAL,
    CommonTrait.SLIMY_SKIN,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.PASSIVE_RESPIRATION,
    ColorTrait.GREEN_COLORATION
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
    SizeClass.TINY,
)
val treeFrog = anura.descend(
    "tree frog",
    SizeClass.TINY,
    CommonTrait.CLIMBING_LIMBS,
    ColorTrait.GREEN_COLORATION,
    minus = listOf(CommonTrait.AMPHIBIOUS_LIMBS)
)
val bullfrog = anura.descend(
    "bullfrog",
    SizeClass.SMALL,
    CommonTrait.BULKY_PHYSIQUE,
)
val toad = anura.descend(
    "toad",
    SizeClass.TINY,
    CommonTrait.TOXIC_SKIN,
    ColorTrait.BROWN_COLORATION,
    minus = listOf(CommonTrait.SLIMY_SKIN)
)

// salamanders
val caudata = amphibian.descend(
    "caudata",
    SizeClass.SMALL,
    CommonTrait.LIMB_REGROWTH,
    CommonTrait.AMBUSH_MUSCULATURE
)
val moleSalamander = caudata.descend(
    "mole salamander",
    SizeClass.TINY,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.GILLS
)
val newt = caudata.descend(
    "newt",
    SizeClass.TINY,
    CommonTrait.TOXIC_SKIN,
    CommonTrait.APOSEMATIC_COLORATION,
    CommonTrait.DIURNAL,
    ColorTrait.RED_COLORATION,
    minus = listOf(CommonTrait.SLIMY_SKIN)
)
val giantSalamander = caudata.descend(
    "giant salamander",
    SizeClass.MEDIUM,
    CommonTrait.BULKY_PHYSIQUE,
    CommonTrait.EYES.atLevel(1),
    CommonTrait.JAW,
    CommonTrait.SUCTION_FEEDING,
    CommonTrait.SLOW_METABOLISM
)

// caecilians
val caecilian = amphibian.descend(
    "caecilian",
    SizeClass.TINY,
    CommonTrait.BODY_UNDULATION,
    CommonTrait.FOSSORIAL_LIVING,
    CommonTrait.SEGMENTED_PHYSIQUE,
    CommonTrait.LACTATION_GLANDS,
    CommonTrait.VIVIPARITY,
    CommonTrait.EYES.atLevel(1),
    ColorTrait.BLACK_COLORATION,
    minus = listOf(CommonTrait.AMPHIBIOUS_LIMBS)
)
