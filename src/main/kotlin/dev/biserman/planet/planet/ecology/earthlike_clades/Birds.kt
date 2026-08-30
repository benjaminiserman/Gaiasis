package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.atLevel

val bird = reptile.descend(
    "bird",
    SizeClass.SMALL,
    CommonTrait.FEATHERS,
    CommonTrait.WINGS,
    CommonTrait.BEAK,
    CommonTrait.OVOSPORE_NEST,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.REGIONAL_MIGRATION,
    ColorTrait.BROWN_COLORATION,
)

val ratite = bird.descend(
    "ratite",
    SizeClass.MEDIUM,
    CommonTrait.WALKING_LIMBS,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.LONG_NECK,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.HERDING_BEHAVIOR,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.NEIGHBOR_DISPERSAL,
    minus = listOf(CommonTrait.WINGS),
)
val fowl = bird.descend(
    "fowl",
    SizeClass.SMALL,
    CommonTrait.WALKING_LIMBS,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.FREQUENT_REPRODUCTION,
    CommonTrait.HERDING_BEHAVIOR,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.WEAK_WINGS
)
val waterfowl = bird.descend(
    "waterfowl",
    SizeClass.SMALL,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.GROUP_LIVING,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.WATERPROOF_PLUMAGE,
    CommonTrait.LONG_MIGRATION
)
val grebe = bird.descend(
    "grebe",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
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
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.WADING_LIMBS,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.SPEAR_BILL,
    CommonTrait.REGIONAL_MIGRATION,
    CommonTrait.WATERPROOF_PLUMAGE,
    ColorTrait.WHITE_COLORATION,
)
val gull = bird.descend(
    "gull",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.WADING_LIMBS,
    CommonTrait.SCAVENGING_SENSES,
    CommonTrait.WATERPROOF_PLUMAGE,
    ColorTrait.PALE_COLORATION,
)
val hummingbird = bird.descend(
    "hummingbird",
    SizeClass.TINY,
    CommonTrait.FAST_METABOLISM,
    CommonTrait.NECTAR_SIPPING_TONGUE,
    CommonTrait.POLLEN_CARRYING_SURFACES,
    CommonTrait.LONG_MIGRATION,
    CommonTrait.CHIRPING_CALL,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.COMPLEX_VOCALIZATIONS,
    ColorTrait.RAINBOW_COLORATION
)
val penguin = bird.descend(
    "penguin",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.PROLONGED_BREATH_HOLDING,
    CommonTrait.SEA_ICE_ROOKERY,
    CommonTrait.MOTION_TRACKING_SENSES,
    CommonTrait.BURROW_BORROWER,
    CommonTrait.WATERPROOF_PLUMAGE,
    CommonTrait.FAT_RESERVES,
    CommonTrait.TRUMPETING_CALL,
    CommonTrait.STREAMLINED_PHYSIQUE,
    ColorTrait.COUNTERSHADE_COLORATION,
)
val owl = bird.descend(
    "owl",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.HOOKED_TALONS,
    CommonTrait.MOTION_TRACKING_SENSES,
    CommonTrait.KEEN_HEARING,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.EYES.atLevel(5),
    CommonTrait.SILENT_MOVEMENT,
    CommonTrait.INSULATING_PLUMAGE,
    CommonTrait.HOOTING_CALL,
    CommonTrait.NOCTURNAL,
)
val hawk = bird.descend(
    "hawk",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.EYES.atLevel(5),
    CommonTrait.MOTION_TRACKING_SENSES,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.STREAMLINED_PHYSIQUE,
    CommonTrait.HOOKED_TALONS,
)
val vulture = bird.descend(
    "vulture",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
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
    ColorTrait.BLACK_COLORATION,
)
val woodpecker = bird.descend(
    "woodpecker",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.DRUMMING_DISPLAY,
)
val songbird = bird.descend(
    "songbird",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.FRUIT_EATING_MOUTHPARTS,
    CommonTrait.CHIRPING_CALL,
    CommonTrait.REGIONAL_MIGRATION,
)
val parrot = bird.descend(
    "parrot",
    SizeClass.SMALL,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.FRUIT_EATING_MOUTHPARTS,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.EXTENDED_PARENTAL_CARE,
    CommonTrait.IMITATIVE_VOCALIZATION,
    CommonTrait.INTELLIGENT,
    ColorTrait.RAINBOW_COLORATION
)
val corvid = bird.descend(
    "corvid",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.SCAVENGING_SENSES,
    CommonTrait.TOOL_MANIPULATION,
    CommonTrait.INTELLIGENT,
    CommonTrait.CROAKING_CALL,
    ColorTrait.BLACK_COLORATION,
)
