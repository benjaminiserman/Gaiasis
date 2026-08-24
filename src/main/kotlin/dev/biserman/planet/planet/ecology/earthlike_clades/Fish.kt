package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val fish = EarthSpeciesCatalog.animal(
    "fish",
    SizeClass.SMALL,
    CommonTrait.ECTOTHERMY,
    CommonTrait.GILLS,
    CommonTrait.EYES,
    CommonTrait.TEETH,
    CommonTrait.SCALES,
    CommonTrait.JAW,
    CommonTrait.TEETH,
    CommonTrait.TAIL,
    CommonTrait.AQUATIC_LIMBS,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.CATHEMERAL,
    CommonTrait.LIMBED_BODY,
    CommonTrait.VASCULAR_SYSTEM,
    ColorTrait.COUNTERSHADE_CAMOUFLAGE
)

// cartilaginous fish
val cartilaginousFish = fish.descend(
    "cartilaginous fish",
    SizeClass.MEDIUM,
    CommonTrait.CARTILAGINOUS_SKELETON,
    CommonTrait.PLACOID_SCALES,
    CommonTrait.GROUP_LIVING
)
val shark = cartilaginousFish.descend(
    "shark",
    SizeClass.MEDIUM,
    CommonTrait.STREAMLINED_PHYSIQUE,
    CommonTrait.TEETH_REGROWTH,
    CommonTrait.STRONG_JAWS
)
val skate = cartilaginousFish.descend(
    "skate",
    SizeClass.MEDIUM,
    CommonTrait.FLATTENED_PHYSIQUE,
    CommonTrait.SOLITARY
)
val stingray = cartilaginousFish.descend(
    "stingray",
    SizeClass.MEDIUM,
    CommonTrait.FLATTENED_PHYSIQUE,
    CommonTrait.STINGER,
    CommonTrait.VENOM_DELIVERY,
    CommonTrait.VIVIPARITY
)

// bony fish
val bonyFish = fish.descend(
    "bony fish",
    SizeClass.SMALL,
    CommonTrait.BONY_SKELETON,
)
val gar = bonyFish.descend(
    "gar",
    SizeClass.MEDIUM,
    CommonTrait.BONY_SCALES,
    CommonTrait.SLENDER_PHYSIQUE,
    CommonTrait.SOLITARY,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.FRESHWATER_OSMOREGULATION
)
val sturgeon = bonyFish.descend(
    "sturgeon",
    SizeClass.LARGE,
    CommonTrait.BONY_SCALES,
    CommonTrait.GROUP_LIVING,
    CommonTrait.ELECTRORECEPTION,
    CommonTrait.EURYHALINE_OSMOREGULATION,
    CommonTrait.SLOW_GROWTH,
    CommonTrait.REGIONAL_MIGRATION,
    CommonTrait.COLD_ACTIVE_ENZYMES,
    CommonTrait.BULKY_PHYSIQUE
)

// teleosts
val teleostei = bonyFish.descend(
    "teleostei",
    SizeClass.SMALL,
    CommonTrait.PROTRUSIBLE_JAW,
    minus = listOf(CommonTrait.STREAMLINED_PHYSIQUE)
)
val eel = teleostei.descend(
    "eel",
    SizeClass.SMALL,
    CommonTrait.UNDULATING_BODY,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.SOLITARY,
    ColorTrait.BROWN_CAMOUFLAGE,
    minus = listOf(CommonTrait.AQUATIC_LIMBS)
)
val herring = teleostei.descend(
    "herring",
    SizeClass.SMALL,
    CommonTrait.SCHOOLING,
    CommonTrait.GILL_RAKERS,
    CommonTrait.STREAMLINED_PHYSIQUE
)
val swordfish = teleostei.descend(
    "swordfish",
    SizeClass.LARGE,
    CommonTrait.STREAMLINED_PHYSIQUE,
    CommonTrait.SPEAR_BILL,
    CommonTrait.LONG_MIGRATION,
    CommonTrait.ECTOTHERMY,
    CommonTrait.GROUP_LIVING
)

val otophysa = teleostei.descend(
    "otophysa",
    SizeClass.SMALL,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.SCHOOLING,
)
val minnow = otophysa.descend(
    "minnow",
    SizeClass.TINY,
)
val carp = otophysa.descend(
    "carp",
    SizeClass.SMALL,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.SUCTION_FEEDING,
    CommonTrait.COLD_ACTIVE_ENZYMES,
)
val piranha = otophysa.descend(
    "piranha",
    SizeClass.SMALL,
    CommonTrait.COOPERATIVE_HUNTING,
    CommonTrait.SERRATED_TEETH,
    CommonTrait.STRONG_JAWS,
    ColorTrait.RED_CAMOUFLAGE
)
val catfish = otophysa.descend(
    "catfish",
    SizeClass.MEDIUM,
    CommonTrait.SOLITARY,
    CommonTrait.SUCTION_FEEDING,
    CommonTrait.KEEN_SCENT_SENSE,
    CommonTrait.SPINES,
    CommonTrait.VENOM_DELIVERY,
    ColorTrait.BROWN_CAMOUFLAGE,
)

val protacanthopterygii = teleostei.descend(
    "protacanthopterygii",
    SizeClass.SMALL,
    CommonTrait.REGIONAL_MIGRATION,
    CommonTrait.NATAL_HOMING,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.AQUATIC_CAMOUFLAGE,
    CommonTrait.STREAMLINED_PHYSIQUE,
)
val pike = protacanthopterygii.descend(
    "pike",
    SizeClass.MEDIUM,
    CommonTrait.SOLITARY,
    ColorTrait.GREEN_CAMOUFLAGE
)
val salmon = protacanthopterygii.descend(
    "salmon",
    SizeClass.SMALL,
    CommonTrait.LONG_MIGRATION,
    CommonTrait.EURYHALINE_OSMOREGULATION
)

val percomorpha = teleostei.descend(
    "percomorpha",
    SizeClass.SMALL,
    CommonTrait.GROUP_LIVING
)
val gobie = percomorpha.descend(
    "gobie",
    SizeClass.SMALL,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.SOLITARY,
    CommonTrait.TERRITORIAL,
    ColorTrait.BROWN_CAMOUFLAGE
)
val seahorse = percomorpha.descend(
    "seahorse",
    SizeClass.TINY,
    CommonTrait.REDUCED_LIMBS,
    CommonTrait.PREHENSILE_TAIL,
    CommonTrait.VIVIPARITY,
    CommonTrait.REEF_CAMOUFLAGE,
    CommonTrait.BUOYANCY_BLADDER,
    ColorTrait.BROWN_CAMOUFLAGE
)
val tuna = percomorpha.descend(
    "tuna",
    SizeClass.MEDIUM,
    CommonTrait.STREAMLINED_PHYSIQUE,
    CommonTrait.ECTOTHERMY,
    CommonTrait.SCHOOLING,
    CommonTrait.GILL_RAKERS,
    CommonTrait.SUCTION_FEEDING
)
val flatfish = percomorpha.descend(
    "flatfish",
    SizeClass.SMALL,
    CommonTrait.FLATTENED_PHYSIQUE,
    CommonTrait.SOLITARY,
    CommonTrait.EURYHALINE_OSMOREGULATION,
    ColorTrait.BROWN_CAMOUFLAGE,
)
val flyingfish = percomorpha.descend(
    "flying fish",
    SizeClass.TINY,
    CommonTrait.GLIDING_MEMBRANE,
    CommonTrait.GILL_RAKERS,
    CommonTrait.SCHOOLING
)
val perch = percomorpha.descend(
    "perch",
    SizeClass.SMALL,
    CommonTrait.SUCTION_FEEDING,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.COLD_ACTIVE_ENZYMES,
    CommonTrait.GILL_RAKERS
)
val pufferfish = percomorpha.descend(
    "pufferfish",
    SizeClass.SMALL,
    CommonTrait.INFLATABLE_BODY,
    CommonTrait.TOXIC_SKIN,
    CommonTrait.SPINES,
    CommonTrait.SOLITARY,
    CommonTrait.TERRITORIAL
)
val wrasse = percomorpha.descend(
    "wrasse",
    SizeClass.MEDIUM,
    CommonTrait.REEF_CAMOUFLAGE,
    CommonTrait.REEF_NESTING,
    CommonTrait.TERRITORIAL,
)

// tetrapoda (descended elsewhere)
val tetrapoda = bonyFish.descend(
    "tetrapoda",
    SizeClass.SMALL,
    CommonTrait.WALKING_LIMBS,
    CommonTrait.LUNGS,
    CommonTrait.SOLITARY,
    minus = listOf(CommonTrait.AQUATIC_LIMBS, CommonTrait.GILLS, ColorTrait.COUNTERSHADE_CAMOUFLAGE)
)
