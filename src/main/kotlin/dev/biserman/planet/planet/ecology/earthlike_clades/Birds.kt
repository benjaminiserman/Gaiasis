package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val bird = EarthSpeciesCatalog.animal(
    "bird",
    SizeClass.SMALL,
    CommonTrait.ENDOTHERMY,
    CommonTrait.FEATHERS,
    CommonTrait.WINGS,
    CommonTrait.BEAK,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.OVOSPORE_NEST,
    CommonTrait.DIURNAL,
    CommonTrait.COLLECTIVE_LIVING,
    ColorTrait.BROWN_CAMOUFLAGE,
)

val ratite = bird.descend(
    "ratite",
    SizeClass.MEDIUM,
    CommonTrait.WALKING_LIMBS,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.LONG_NECK,
    CommonTrait.HERDING_BEHAVIOR,
    CommonTrait.COLLECTIVE_LIVING,
    minus = listOf(CommonTrait.WINGS),
)
val fowl = bird.descend(
    "fowl",
    SizeClass.SMALL,
    CommonTrait.WALKING_LIMBS,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.FREQUENT_REPRODUCTION,
    CommonTrait.HERDING_BEHAVIOR,
    CommonTrait.COLLECTIVE_LIVING,
)
val grebe = bird.descend(
    "grebe",
    SizeClass.SMALL,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.WATERPROOF_PLUMAGE,
    CommonTrait.SPEAR_BILL,
    CommonTrait.LONG_NECK,
)
val crane = bird.descend(
    "crane",
    SizeClass.MEDIUM,
    CommonTrait.WADING_LIMBS,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.SPEAR_BILL,
    CommonTrait.REGIONAL_MIGRATION,
    CommonTrait.WATERPROOF_PLUMAGE,
    ColorTrait.WHITE_CAMOUFLAGE,
)
val gull = bird.descend(
    "gull",
    SizeClass.SMALL,
    CommonTrait.WADING_LIMBS,
    CommonTrait.SCAVENGING_SENSES,
    CommonTrait.WATERPROOF_PLUMAGE,
    ColorTrait.PALE_CAMOUFLAGE,
)
val hummingbird = bird.descend(
    "hummingbird",
    SizeClass.TINY,
    CommonTrait.FAST_METABOLISM,
    CommonTrait.NECTAR_SIPPING_TONGUE,
    CommonTrait.POLLEN_CARRYING_SURFACES,
    CommonTrait.LONG_MIGRATION,
    CommonTrait.CHIRPING_CALL,
    CommonTrait.COMPLEX_VOCALIZATIONS,
)
val penguin = bird.descend(
    "penguin",
    SizeClass.SMALL,
    CommonTrait.AQUATIC_FLIPPERS,
    CommonTrait.PROLONGED_BREATH_HOLDING,
    CommonTrait.SEA_ICE_ROOKERY,
    CommonTrait.MOTION_TRACKING_SENSES,
    CommonTrait.BURROW_BORROWER,
    CommonTrait.WATERPROOF_PLUMAGE,
    CommonTrait.FAT_RESERVES,
    CommonTrait.TRUMPETING_CALL,
    ColorTrait.COUNTERSHADE_CAMOUFLAGE,
)
val owl = bird.descend(
    "owl",
    SizeClass.SMALL,
    CommonTrait.HOOKED_TALONS,
    CommonTrait.MOTION_TRACKING_SENSES,
    CommonTrait.KEEN_HEARING,
    CommonTrait.SILENT_MOVEMENT,
    CommonTrait.INSULATING_PLUMAGE,
    CommonTrait.HOOTING_CALL,
    CommonTrait.NOCTURNAL,
)
val hawk = bird.descend(
    "hawk",
    SizeClass.SMALL,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.KEEN_EYESIGHT,
    CommonTrait.MOTION_TRACKING_SENSES,
    CommonTrait.HOOKED_TALONS,
)
val vulture = bird.descend(
    "vulture",
    SizeClass.SMALL,
    CommonTrait.SCAVENGING_SENSES,
    CommonTrait.RESILIENT_DIGESTION,
    CommonTrait.LONG_MIGRATION,
)
val hornbill = bird.descend(
    "hornbill",
    SizeClass.SMALL,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.FRUIT_EATING_MOUTHPARTS,
    CommonTrait.CROAKING_CALL,
    ColorTrait.BLACK_CAMOUFLAGE,
)
val woodpecker = bird.descend(
    "woodpecker",
    SizeClass.SMALL,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.DRUMMING_DISPLAY,
)
val songbird = bird.descend(
    "songbird",
    SizeClass.TINY,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.CHIRPING_CALL,
    CommonTrait.REGIONAL_MIGRATION,
)
val parrot = bird.descend(
    "parrot",
    SizeClass.SMALL,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.EXTENDED_PARENTAL_CARE,
    CommonTrait.IMITATIVE_VOCALIZATION,
    CommonTrait.INTELLIGENT,
)
val corvid = bird.descend(
    "corvid",
    SizeClass.SMALL,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.SCAVENGING_SENSES,
    CommonTrait.TOOL_MANIPULATION,
    CommonTrait.INTELLIGENT,
    CommonTrait.CROAKING_CALL,
    ColorTrait.BLACK_CAMOUFLAGE,
)
