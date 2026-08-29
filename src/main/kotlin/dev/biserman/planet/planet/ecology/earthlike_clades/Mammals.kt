package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.SizeClass

val mammal = tetrapoda.descend(
    "mammal",
    SizeClass.MEDIUM,
    CommonTrait.ENDOTHERMY,
    CommonTrait.FUR,
    CommonTrait.MAMMARY_GLANDS,
    CommonTrait.VIVIPARITY,
    CommonTrait.NOCTURNAL,
    CommonTrait.CLAWS,
    CommonTrait.TAIL,
    ColorTrait.BROWN_COLORATION,
)

// glires
val glires = mammal.descend(
    "glires",
    SizeClass.SMALL,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.CACHED_FOOD,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.FANGS
)

val rodent = glires.descend(
    "rodents",
    SizeClass.SMALL,
)
val mouse = rodent.descend(
    "mouse",
    SizeClass.TINY,
    CommonTrait.GROUP_LIVING,
    CommonTrait.CATHEMERAL
)
val rat = rodent.descend(
    "mouse",
    SizeClass.SMALL,
    CommonTrait.GROUP_LIVING,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.NOCTURNAL
)
val porcupine = rodent.descend(
    "porcupine",
    SizeClass.MEDIUM,
    CommonTrait.SPINES
)
val marmot = rodent.descend(
    "marmot",
    SizeClass.SMALL,
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.GROUP_LIVING,
    CommonTrait.BULKY_PHYSIQUE,
    CommonTrait.WHISTLING_CALL
)
val squirrel = rodent.descend(
    "squirrel",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.SEED_CRACKING_MOUTHPARTS,
    CommonTrait.DIURNAL,
    minus = listOf(CommonTrait.BURROW_BUILDER, CommonTrait.DIGGING_LIMBS, CommonTrait.GRAZING_MOUTHPARTS),
)

val lagomorph = glires.descend(
    "lagomorphs",
    SizeClass.SMALL,
    CommonTrait.FERMENTING_HINDGUT,
    CommonTrait.VESPERTINE
)
val leporid = lagomorph.descend(
    "leporid",
    SizeClass.SMALL,
    CommonTrait.LEAPING_LEGS,
    CommonTrait.KEEN_HEARING,
)
val rabbit = leporid.descend(
    "rabbit",
    SizeClass.SMALL,
    CommonTrait.GROUP_LIVING,
)
val hare = leporid.descend(
    "hare",
    SizeClass.SMALL,
    CommonTrait.SWIFT_LIMBS,
    minus = listOf(CommonTrait.BURROW_BUILDER, CommonTrait.DIGGING_LIMBS)
)
val pika = lagomorph.descend(
    "pika",
    SizeClass.SMALL,
    CommonTrait.SEASONAL_WINTER_COAT,
    CommonTrait.DENSE_UNDERCOAT,
    CommonTrait.HIGH_AFFINITY_BLOOD
)

// bats
val bat = mammal.descend(
    "bat",
    SizeClass.TINY,
    CommonTrait.HETEROTHERMY,
    CommonTrait.WINGS,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
    CommonTrait.SCREECHING_CALL,
)
val megabat = bat.descend(
    "megabat",
    SizeClass.SMALL,
    CommonTrait.FRUIT_EATING_MOUTHPARTS,
    CommonTrait.EXTENDED_PARENTAL_CARE,
)
val microbat = bat.descend(
    "microbat",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.WOOLLY_UNDERCOAT,
    CommonTrait.KEEN_HEARING,
    CommonTrait.ECHOLOCATION,
    CommonTrait.SEASONAL_TORPOR,
)

val trueInsectivore = mammal.descend(
    "true insectivores",
    SizeClass.SMALL,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.KEEN_SCENT_SENSE,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.TERRITORIAL
)
val hedgehog = trueInsectivore.descend(
    "hedgehog",
    SizeClass.SMALL,
    CommonTrait.SPINES,
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.GRAZING_MOUTHPARTS,
    minus = listOf(CommonTrait.TERRITORIAL)
)
val mole = trueInsectivore.descend(
    "mole",
    SizeClass.SMALL,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.FOSSORIAL_LIVING,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.POOR_VISION,
    CommonTrait.FAST_METABOLISM,
    CommonTrait.CATHEMERAL
)
val shrew = trueInsectivore.descend(
    "shrew",
    SizeClass.SMALL,
    CommonTrait.KEEN_HEARING,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.POOR_VISION,
    CommonTrait.FAST_METABOLISM,
    CommonTrait.CATHEMERAL
)
val gymnure = trueInsectivore.descend(
    "gymnure",
    SizeClass.SMALL,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.FRUIT_EATING_MOUTHPARTS
)

// primates
val primate = mammal.descend(
    "primates",
    SizeClass.SMALL,
    CommonTrait.INTELLIGENT,
    CommonTrait.POOR_SCENT_SENSE,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.COMPLEX_VOCALIZATIONS,
    CommonTrait.GROUP_LIVING,
    CommonTrait.EXTENDED_PARENTAL_CARE,
    CommonTrait.HOOTING_CALL,
    CommonTrait.DIURNAL,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.FRUIT_EATING_MOUTHPARTS,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.NAILS,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    minus = listOf(CommonTrait.CLAWS)
)
val monkey = primate.descend(
    "monkey",
    SizeClass.SMALL,
)
val lemur = primate.descend(
    "lemur",
    SizeClass.SMALL,
    CommonTrait.LEAPING_LEGS,
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.KEEN_SCENT_SENSE,
    minus = listOf(CommonTrait.INTELLIGENT)
)
val ape = primate.descend(
    "ape",
    SizeClass.MEDIUM,
    CommonTrait.TOOL_MANIPULATION,
    CommonTrait.SLOW_GROWTH,
    minus = listOf(
        CommonTrait.GRAZING_MOUTHPARTS,
        CommonTrait.TAIL,
    )
)

// ungulates
val ungulate = mammal.descend(
    "ungulates",
    SizeClass.MEDIUM,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.OPEN_COUNTRY_PREFERENCE,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.REGIONAL_MIGRATION,
    CommonTrait.DIURNAL,
    minus = listOf(CommonTrait.CLAWS)
)
val horse = ungulate.descend(
    "horse",
    SizeClass.LARGE,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.FERMENTING_HINDGUT,
    CommonTrait.SWEAT_GLANDS,
    CommonTrait.OPEN_COUNTRY_PREFERENCE,
    CommonTrait.HERDING_BEHAVIOR,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.REGIONAL_MIGRATION,
    CommonTrait.CATHEMERAL,
    CommonTrait.BRAYING_CALL,
)
val camel = ungulate.descend(
    "camel",
    SizeClass.LARGE,
    CommonTrait.BROWSING_MOUTHPARTS,
    CommonTrait.RUMINANT_STOMACH,
    CommonTrait.FAT_RESERVES,
    CommonTrait.FOOD_DERIVED_WATER,
    CommonTrait.CONCENTRATED_URINE,
    CommonTrait.GROUP_LIVING,
    CommonTrait.GRUNTING_CALL,
)
val pig = ungulate.descend(
    "pig",
    SizeClass.MEDIUM,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.FERMENTING_HINDGUT,
    CommonTrait.GRUNTING_CALL,
    CommonTrait.KEEN_HEARING,
    CommonTrait.KEEN_SCENT_SENSE,
    CommonTrait.INTELLIGENT,
    CommonTrait.LONG_TUSKS,
    CommonTrait.SOLITARY,
    minus = listOf(CommonTrait.OPEN_COUNTRY_PREFERENCE, CommonTrait.REGIONAL_MIGRATION)
)

val ruminant = ungulate.descend(
    "ruminant",
    SizeClass.LARGE,
    CommonTrait.RUMINANT_STOMACH,
    CommonTrait.HERDING_BEHAVIOR,
)
val bison = ruminant.descend(
    "bison",
    SizeClass.LARGE,
    CommonTrait.BELLOWING_CALL,
)
val goat = ruminant.descend(
    "goat",
    SizeClass.MEDIUM,
    CommonTrait.LEAPING_LEGS,
    CommonTrait.SEASONAL_WINTER_COAT,
    CommonTrait.HIGH_AFFINITY_BLOOD,
    CommonTrait.BLEATING_CALL,
    CommonTrait.GROUP_LIVING,
    ColorTrait.PALE_COLORATION,
    minus = listOf(CommonTrait.HERDING_BEHAVIOR)
)
val deer = ruminant.descend(
    "deer",
    SizeClass.MEDIUM,
    CommonTrait.BROWSING_MOUTHPARTS,
    CommonTrait.DENSE_UNDERCOAT,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.SEASONAL_WINTER_COAT,
    CommonTrait.GROUP_LIVING,
    CommonTrait.VESPERTINE,
    minus = listOf(
        CommonTrait.OPEN_COUNTRY_PREFERENCE,
        CommonTrait.REGIONAL_MIGRATION,
        CommonTrait.HERDING_BEHAVIOR,
        CommonTrait.GRAZING_MOUTHPARTS
    )
)
val antelope = ruminant.descend(
    "antelope",
    SizeClass.MEDIUM,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
    CommonTrait.BLEATING_CALL,
)
val giraffe = ruminant.descend(
    "giraffe",
    SizeClass.LARGE,
    CommonTrait.BROWSING_MOUTHPARTS,
    CommonTrait.LONG_NECK,
    CommonTrait.GROUP_LIVING,
    minus = listOf(CommonTrait.REGIONAL_MIGRATION, CommonTrait.GRAZING_MOUTHPARTS)
)

val cetacean = ungulate.descend(
    "cetaceans",
    SizeClass.LARGE,
    CommonTrait.AQUATIC_LIMBS,
    CommonTrait.PROLONGED_BREATH_HOLDING,
    CommonTrait.STREAMLINED_PHYSIQUE,
    CommonTrait.BLUBBER,
    CommonTrait.LONG_MIGRATION,
    CommonTrait.KEEN_HEARING,
    CommonTrait.ECHOLOCATION,
    CommonTrait.COMPLEX_VOCALIZATIONS,
    CommonTrait.INTELLIGENT,
    CommonTrait.GROUP_LIVING,
    ColorTrait.COUNTERSHADE_COLORATION,
    minus = listOf(
        CommonTrait.GRAZING_MOUTHPARTS,
        CommonTrait.OPEN_COUNTRY_PREFERENCE,
    )
)
val whale = cetacean.descend(
    "whale",
    SizeClass.COLOSSAL,
    CommonTrait.BALEEN,
    CommonTrait.DEEP_DIVING_PHYSIOLOGY,
    CommonTrait.SONG_CALL
)
val dolphin = cetacean.descend(
    "dolphin",
    SizeClass.MEDIUM,
    CommonTrait.COOPERATIVE_HUNTING,
    CommonTrait.CLICKING_CALL
)

// carnivores
val carnivore = mammal.descend(
    "carnivores",
    SizeClass.MEDIUM,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.TEETH,
    CommonTrait.FANGS
)
val bear = carnivore.descend(
    "bear",
    SizeClass.LARGE,
    CommonTrait.HETEROTHERMY,
    CommonTrait.BROWSING_MOUTHPARTS,
    CommonTrait.DENSE_UNDERCOAT,
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.FAT_RESERVES,
    CommonTrait.GROWLING_CALL,
    CommonTrait.BULKY_PHYSIQUE,
    CommonTrait.DIURNAL,
    minus = listOf(CommonTrait.AMBUSH_MUSCULATURE)
)
val raccoon = carnivore.descend(
    "raccoon",
    SizeClass.SMALL,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.GROUP_LIVING,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.KEEN_SCENT_SENSE,
    CommonTrait.SCAVENGING_SENSES,
    CommonTrait.INTELLIGENT,
    CommonTrait.TOOL_MANIPULATION,
    CommonTrait.TERRESTRIAL_CAMOUFLAGE,
    CommonTrait.CATHEMERAL,
    ColorTrait.PALE_COLORATION,
    minus = listOf(CommonTrait.AMBUSH_MUSCULATURE)
)

val seal = carnivore.descend(
    "seal",
    SizeClass.MEDIUM,
    CommonTrait.AQUATIC_LIMBS,
    CommonTrait.PROLONGED_BREATH_HOLDING,
    CommonTrait.STREAMLINED_PHYSIQUE,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.BLUBBER,
    CommonTrait.BARKING_CALL,
    ColorTrait.COUNTERSHADE_COLORATION,
)

val felid = carnivore.descend(
    "felids",
    SizeClass.MEDIUM,
    CommonTrait.TERRESTRIAL_CAMOUFLAGE,
    CommonTrait.FOOD_DERIVED_WATER,
    CommonTrait.RETRACTABLE_CLAWS,
    CommonTrait.FLEXIBLE_SPINE,
    CommonTrait.SILENT_MOVEMENT,
)
val panther = felid.descend(
    "panther",
    SizeClass.LARGE,
    CommonTrait.STRONG_JAWS,
    CommonTrait.ROARING_CALL,
)
val cat = felid.descend(
    "cat",
    SizeClass.SMALL,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.SLENDER_PHYSIQUE,
    CommonTrait.MEOWING_CALL,
    CommonTrait.CHIRPING_CALL,
    CommonTrait.PURRING_CALL,
)

val canid = carnivore.descend(
    "canids",
    SizeClass.MEDIUM,
    CommonTrait.BARKING_CALL,
    CommonTrait.KEEN_SCENT_SENSE,
    CommonTrait.VESPERTINE
)
val wolf = canid.descend(
    "wolf",
    SizeClass.MEDIUM,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.MOTION_TRACKING_SENSES,
    CommonTrait.COOPERATIVE_HUNTING,
    CommonTrait.GROUP_LIVING,
    CommonTrait.DENSE_UNDERCOAT,
    CommonTrait.SEASONAL_WINTER_COAT,
    CommonTrait.HOWLING_CALL,
    CommonTrait.INTELLIGENT,
    ColorTrait.PALE_COLORATION,
    minus = listOf(CommonTrait.AMBUSH_MUSCULATURE)
)
val fox = canid.descend(
    "fox",
    SizeClass.SMALL,
    CommonTrait.CACHED_FOOD,
    CommonTrait.SLENDER_PHYSIQUE,
    CommonTrait.HIGH_POUNCING,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
)

val mustelid = carnivore.descend(
    "mustelid",
    SizeClass.SMALL,
    CommonTrait.DENSE_UNDERCOAT,
    CommonTrait.SLENDER_PHYSIQUE,
)
val badger = mustelid.descend(
    "badger",
    SizeClass.SMALL,
    CommonTrait.GRAZING_MOUTHPARTS,
    ColorTrait.PALE_COLORATION,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.KEEN_SCENT_SENSE,
    minus = listOf(CommonTrait.SLENDER_PHYSIQUE, CommonTrait.AMBUSH_MUSCULATURE)
)
val otter = mustelid.descend(
    "otter",
    SizeClass.SMALL,
    CommonTrait.GROUP_LIVING,
    CommonTrait.AQUATIC_LIMBS,
    CommonTrait.TOOL_MANIPULATION,
    CommonTrait.STREAMLINED_PHYSIQUE,
    CommonTrait.INTELLIGENT,
    CommonTrait.CHIRPING_CALL,
)
val weasel = mustelid.descend(
    "weasel",
    SizeClass.SMALL,
    CommonTrait.BURROW_BORROWER,
    minus = listOf(CommonTrait.SLENDER_PHYSIQUE)
)

// other

val elephant = mammal.descend(
    "elephant",
    SizeClass.HUGE,
    CommonTrait.BROWSING_MOUTHPARTS,
    CommonTrait.PREHENSILE_TRUNK,
    CommonTrait.MASSIVE_EARS,
    CommonTrait.FERMENTING_HINDGUT,
    CommonTrait.HERDING_BEHAVIOR,
    CommonTrait.COLLECTIVE_LIVING,
    CommonTrait.REGIONAL_MIGRATION,
    CommonTrait.EXTENDED_PARENTAL_CARE,
    CommonTrait.TRUMPETING_CALL,
    CommonTrait.INTELLIGENT,
)
