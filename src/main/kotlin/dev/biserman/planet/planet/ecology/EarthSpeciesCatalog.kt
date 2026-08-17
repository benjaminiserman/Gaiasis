package dev.biserman.planet.planet.ecology

import java.text.Normalizer
import java.util.Locale

/**
 * Broad authoring and stress-test catalog of recognizable Earth organisms.
 *
 * These are ecological prototypes, not taxonomic rules or calibrated claims
 * about exact adult mass. Size classes intentionally use the nearest useful
 * simulation foundation, and every species remains an ordinary combination of
 * descriptive traits.
 */
object EarthSpeciesCatalog {
    private val naturallyHairlessMammalIds = setOf(
        "african-elephant",
        "hippopotamus",
        "white-rhinoceros",
        "blue-whale",
        "humpback-whale",
        "orca",
        "bottlenose-dolphin",
        "west-indian-manatee",
    )

    val MAMMALS: List<SpeciesDefinition> = listOf(
        animal(
            "african elephant",
            SizeClass.HUGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.PREHENSILE_TRUNK,
            CommonTrait.MASSIVE_EARS,
            CommonTrait.BULKY_BODY,
            CommonTrait.LONG_TUSKS,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.TRUMPETING_CALL,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.EXTENDED_PARENTAL_CARE
        ),
        animal(
            "giraffe",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.LONG_NECK,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "plains zebra",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.SWEAT_GLANDS,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.REGIONAL_MIGRATION,
            CommonTrait.CAMOUFLAGE_PATTERN,
            CommonTrait.BRAYING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "blue wildebeest",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.LARGE_HORN,
            CommonTrait.BELLOWING_CALL,
            CommonTrait.REGIONAL_MIGRATION,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "thomson's gazelle",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.LARGE_HORN,
            CommonTrait.BLEATING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "african lion",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.STRONG_JAWS,
            CommonTrait.RETRACTABLE_CLAWS,
            CommonTrait.FLEXIBLE_SPINE,
            CommonTrait.ROARING_CALL,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "cheetah",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.SLENDER_BODY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.RETRACTABLE_CLAWS,
            CommonTrait.FLEXIBLE_SPINE,
            CommonTrait.CHIRPING_CALL,
            CommonTrait.MEOWING_CALL,
            CommonTrait.PURRING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "spotted hyena",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.STRONG_JAWS,
            CommonTrait.WHOOPING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "hippopotamus",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.BULKY_BODY,
            CommonTrait.LONG_TUSKS,
            CommonTrait.STRONG_JAWS,
            CommonTrait.GRUNTING_CALL
        ),
        animal(
            "white rhinoceros",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.BULKY_BODY,
            CommonTrait.LARGE_HORN,
        ),
        animal(
            "western gorilla",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.BULKY_BODY,
            CommonTrait.HOOTING_CALL,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "chimpanzee",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.TOOL_MANIPULATION,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.HOOTING_CALL,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "bornean orangutan",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.TOOL_MANIPULATION,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.HOOTING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "polar bear",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SEA_ICE_LOCOMOTION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.BLUBBER,
            CommonTrait.FAT_RESERVES,
            CommonTrait.BULKY_BODY,
            CommonTrait.GROWLING_CALL,
            ColorTrait.WHITE_CAMOUFLAGE
        ),
        animal(
            "brown bear",
            SizeClass.LARGE,
            CommonTrait.HETEROTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.FAT_RESERVES,
            CommonTrait.SEASONAL_TORPOR,
            CommonTrait.BULKY_BODY,
            CommonTrait.GROWLING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "gray wolf",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.FAT_RESERVES,
            CommonTrait.HOWLING_CALL,
            CommonTrait.BARKING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "red fox",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.CACHED_FOOD,
            CommonTrait.SLENDER_BODY,
            CommonTrait.HIGH_POUNCING,
            CommonTrait.BARKING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "snowshoe hare",
            SizeClass.SMALL,
            CommonTrait.HETEROTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.LEAPING_LEGS,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            ColorTrait.WHITE_CAMOUFLAGE
        ),
        animal(
            "north American beaver",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.CACHED_FOOD,
            CommonTrait.DAM_BUILDING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "red squirrel",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.CACHED_FOOD,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "house mouse",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.DIGGING_CLAWS,
            CommonTrait.CACHED_FOOD,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "norway rat",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DIGGING_CLAWS,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "red kangaroo",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.LEAPING_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "koala",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            obligateBrowser(
                foodSpeciesId = "eucalyptus-tree",
                displayName = "eucalyptus leaf specialization",
            ),
            CommonTrait.BELLOWING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "giant panda",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.BULKY_BODY,
            obligateBrowser(
                foodSpeciesId = "giant-bamboo",
                displayName = "bamboo feeding specialization",
            ),
            CommonTrait.BLEATING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "bengal tiger",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.CAMOUFLAGE_PATTERN,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.STRONG_JAWS,
            CommonTrait.RETRACTABLE_CLAWS,
            CommonTrait.FLEXIBLE_SPINE,
            CommonTrait.ROARING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "snow leopard",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.WOOLLY_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.HYPOXIA_RESPONSIVE_METABOLISM,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.CAMOUFLAGE_PATTERN,
            CommonTrait.FAT_RESERVES,
            CommonTrait.RETRACTABLE_CLAWS,
            CommonTrait.FLEXIBLE_SPINE,
            CommonTrait.MEOWING_CALL,
            CommonTrait.PURRING_CALL,
            ColorTrait.WHITE_CAMOUFLAGE
        ),
        animal(
            "margay",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.CAMOUFLAGE_PATTERN,
            CommonTrait.RETRACTABLE_CLAWS,
            CommonTrait.FLEXIBLE_SPINE,
            CommonTrait.SLENDER_BODY,
            CommonTrait.CHIRPING_CALL,
            CommonTrait.MEOWING_CALL,
            CommonTrait.PURRING_CALL,
            CommonTrait.SOUND_LURES,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "european wildcat",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.RETRACTABLE_CLAWS,
            CommonTrait.FLEXIBLE_SPINE,
            CommonTrait.SLENDER_BODY,
            CommonTrait.CHIRPING_CALL,
            CommonTrait.MEOWING_CALL,
            CommonTrait.PURRING_CALL,
            CommonTrait.SOUND_LURES,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "white-tailed deer",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.ANTLERS,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "american bison",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.LARGE_HORN,
            CommonTrait.BELLOWING_CALL,
            CommonTrait.REGIONAL_MIGRATION,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "dromedary camel",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.FAT_RESERVES,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.GRUNTING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "wild boar",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DIGGING_CLAWS,
            CommonTrait.BULKY_BODY,
            CommonTrait.LONG_TUSKS,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.GRUNTING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "blue whale",
            SizeClass.COLOSSAL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.BALEEN,
            CommonTrait.BLUBBER,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.FAT_RESERVES,
            CommonTrait.WHALESONG,
            ColorTrait.BLUE_CAMOUFLAGE
        ),
        animal(
            "humpback whale",
            SizeClass.COLOSSAL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.BALEEN,
            CommonTrait.BLUBBER,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.FAT_RESERVES,
            CommonTrait.WHALESONG,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "orca",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.LONG_INTERBIRTH_INTERVAL,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.BLUBBER,
            CommonTrait.ECHOLOCATION,
            CommonTrait.CLICK_WHISTLE_REPERTOIRE,
            CommonTrait.STRONG_JAWS,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "bottlenose dolphin",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.ECHOLOCATION,
            CommonTrait.CLICK_WHISTLE_REPERTOIRE,
            CommonTrait.BLUBBER,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "harbor seal",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.STROKE_AND_GLIDE_SWIMMING,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BLUBBER,
            CommonTrait.BARKING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "weddell seal",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.STROKE_AND_GLIDE_SWIMMING,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.BLUBBER,
            CommonTrait.TRILLING_CALL,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "crabeater seal",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.STROKE_AND_GLIDE_SWIMMING,
            CommonTrait.SIEVING_TEETH,
            CommonTrait.BLUBBER,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "sea otter",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.TOOL_MANIPULATION,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SLENDER_BODY,
            CommonTrait.CHIRPING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "walrus",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.BENTHIC_SUCTION_FEEDING,
            CommonTrait.BLUBBER,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.BULKY_BODY,
            CommonTrait.LONG_TUSKS,
            CommonTrait.BELLOWING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "west Indian manatee",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.CHIRPING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "little brown bat",
            SizeClass.TINY,
            CommonTrait.HETEROTHERMY,
            CommonTrait.MEMBRANOUS_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.ECHOLOCATION,
            CommonTrait.SEASONAL_TORPOR,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "large flying fox",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.MEMBRANOUS_WINGS,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.SCREECHING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "duck-billed platypus",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.TERRESTRIAL_OVOSPORE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "three-toed sloth",
            SizeClass.SMALL,
            CommonTrait.HETEROTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.SLOW_METABOLISM,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "giant anteater",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.COLONY_PROBING_TONGUE,
            CommonTrait.DIGGING_CLAWS,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "sugar glider",
            SizeClass.SMALL,
            CommonTrait.HETEROTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.GLIDING_MEMBRANE,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.NECTAR_SIPPING_TONGUE,
            CommonTrait.SLENDER_BODY,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        // Procyonids
        animal(
            "common raccoon",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.TOOL_MANIPULATION,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "white-nosed coati",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DIGGING_CLAWS,
            CommonTrait.SLENDER_BODY,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "kinkajou",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.NECTAR_SIPPING_TONGUE,
            CommonTrait.SLENDER_BODY,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        // Mustelids
        animal(
            "wolverine",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.STRONG_JAWS,
            CommonTrait.BULKY_BODY,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.FAT_RESERVES,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "european badger",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DIGGING_CLAWS,
            CommonTrait.BULKY_BODY,
            CommonTrait.DENSE_UNDERCOAT,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "honey badger",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DIGGING_CLAWS,
            CommonTrait.REINFORCED_HIDE,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "stoat",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SLENDER_BODY,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            ColorTrait.WHITE_CAMOUFLAGE
        ),
        animal(
            "north American river otter",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SLENDER_BODY,
            CommonTrait.CHIRPING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        // Siberian boreal forest and taiga
        animal(
            "siberian musk deer",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "sable",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.CACHED_FOOD,
            CommonTrait.SLENDER_BODY,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        // Himalayan and Tibetan alpine plateau
        animal(
            "wild yak",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.ENLARGED_CARDIOPULMONARY_SYSTEM,
            CommonTrait.SNOW_AND_ICE_LICKING,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.LARGE_HORN,
            CommonTrait.BELLOWING_CALL,
            CommonTrait.FAT_RESERVES,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "himalayan pika",
            SizeClass.SMALL,
            CommonTrait.HETEROTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.HYPOXIA_RESPONSIVE_METABOLISM,
            CommonTrait.INSULATED_BURROW_REFUGE,
            CommonTrait.CACHED_FOOD,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        // Rocky Mountains
        animal(
            "rocky Mountain elk",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.ANTLERS,
            CommonTrait.BUGLING_CALL,
            CommonTrait.SHORT_MIGRATION,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "mountain goat",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.LEAPING_LEGS,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.HIGH_AFFINITY_BLOOD,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.LARGE_HORN,
            CommonTrait.BLEATING_CALL,
            ColorTrait.WHITE_CAMOUFLAGE
        ),
        // High Andes
        animal(
            "vicuña",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.HIGH_AFFINITY_BLOOD,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        // Sahara
        animal(
            "addax",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.HEAT_STABLE_ENZYMES,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.LARGE_HORN,
            CommonTrait.BLEATING_CALL,
            CommonTrait.REGIONAL_MIGRATION,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "fennec fox",
            SizeClass.SMALL,
            CommonTrait.HETEROTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.MASSIVE_EARS,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.DRY_BURROW_NEST,
            CommonTrait.SLENDER_BODY,
            CommonTrait.HIGH_POUNCING,
            CommonTrait.BARKING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "jerboa",
            SizeClass.TINY,
            CommonTrait.HETEROTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.DRY_BURROW_NEST,
            CommonTrait.CACHED_FOOD,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        // Canadian Shield boreal forest
        animal(
            "woodland caribou",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.ANTLERS,
            CommonTrait.REGIONAL_MIGRATION,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "canada lynx",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.CAMOUFLAGE_PATTERN,
            CommonTrait.RETRACTABLE_CLAWS,
            CommonTrait.FLEXIBLE_SPINE,
            CommonTrait.MEOWING_CALL,
            CommonTrait.PURRING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
    ).map { definition ->
        val mammalTraits =
            if (definition.id == "duck-billed-platypus") {
                definition.traits + CommonTrait.MAMMARY_GLANDS
            } else {
                definition.traits
                    .filterNot(::isCoreOvosporeTrait) +
                    listOf(CommonTrait.VIVIPARITY, CommonTrait.MAMMARY_GLANDS)
            }
        val hasAuthoredBodyCovering = mammalTraits.any {
            it.group == TraitGroup.DOMINANT_BODY_COVERING
        }
        if (definition.id in naturallyHairlessMammalIds || hasAuthoredBodyCovering) {
            definition.copy(traits = mammalTraits)
        } else {
            definition.copy(traits = mammalTraits + CommonTrait.FUR)
        }
    }

    val EXTINCT_SPECIES: List<SpeciesDefinition> = listOf(
        animal(
            "tyrannosaurus rex",
            SizeClass.HUGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.STRONG_JAWS,
            CommonTrait.FAT_RESERVES,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "velociraptor",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.FEATHERS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.HOOKED_TALONS,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.COOPERATIVE_HUNTING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "triceratops",
            SizeClass.HUGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.LARGE_HORN,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "stegosaurus",
            SizeClass.HUGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "ankylosaurus",
            SizeClass.HUGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.ARMORED_HIDE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "brachiosaurus",
            SizeClass.HUGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.LONG_NECK,
            CommonTrait.FERMENTING_HINDGUT,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "titanosaurus",
            SizeClass.COLOSSAL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.LONG_NECK,
            CommonTrait.FERMENTING_HINDGUT,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "pteranodon",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.MEMBRANOUS_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "woolly mammoth",
            SizeClass.HUGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.FUR,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.PREHENSILE_TRUNK,
            CommonTrait.BULKY_BODY,
            CommonTrait.LONG_TUSKS,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.TRUMPETING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "saber-toothed cat",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.FUR,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.RETRACTABLE_CLAWS,
            CommonTrait.FLEXIBLE_SPINE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "dodo",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.FEATHERS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.FAT_RESERVES,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "megalodon",
            SizeClass.HUGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GILLS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.STRONG_JAWS,
            CommonTrait.FAT_RESERVES,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "trilobite",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.GILLS,
            CommonTrait.MARINE_SNOW_COLLECTORS,
            CommonTrait.ARMORED_HIDE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "ammonite",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.GILLS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.PROTECTIVE_SHELL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
    ).map { definition ->
        if (definition.id in setOf("woolly-mammoth", "saber-toothed-cat")) {
            definition.copy(
                traits = definition.traits.filterNot(::isCoreOvosporeTrait) +
                    listOf(CommonTrait.VIVIPARITY, CommonTrait.MAMMARY_GLANDS),
            )
        } else {
            definition
        }
    }

    val BIRDS: List<SpeciesDefinition> = listOf(
        animal(
            "bald eagle",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.HOOKED_TALONS,
            CommonTrait.INSULATING_PLUMAGE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "great horned owl",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.HOOKED_TALONS,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.HOOTING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "peregrine falcon",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.HOOKED_TALONS,
            CommonTrait.INSULATING_PLUMAGE,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "ruby-throated hummingbird",
            SizeClass.TINY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.NECTAR_SIPPING_TONGUE,
            CommonTrait.POLLEN_CARRYING_SURFACES,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.CHIRPING_CALL,
            CommonTrait.BIRDSONG,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "scarlet macaw",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.SCREECHING_CALL,
            CommonTrait.IMITATIVE_VOCALIZATION,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "common raven",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.TOOL_MANIPULATION,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.CROAKING_CALL,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "emperor penguin",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.SEA_ICE_ROOKERY,
            CommonTrait.BODY_CARRIED_OVOSPORES,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.WATERPROOF_PLUMAGE,
            CommonTrait.COLD_ACTIVE_ENZYMES,
            CommonTrait.FAT_RESERVES,
            CommonTrait.TRUMPETING_CALL,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "common ostrich",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.LEAPING_LEGS,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.BOOMING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "greater flamingo",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.EURYHALINE_OSMOREGULATION,
            CommonTrait.SIEVING_TEETH,
            CommonTrait.WATERPROOF_PLUMAGE,
            CommonTrait.HONKING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "brown pelican",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.EURYHALINE_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.WATERPROOF_PLUMAGE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "mallard duck",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.WATERPROOF_PLUMAGE,
            CommonTrait.REGIONAL_MIGRATION,
            CommonTrait.QUACKING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "canada goose",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.WATERPROOF_PLUMAGE,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.HONKING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "mute swan",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.WATERPROOF_PLUMAGE,
            CommonTrait.TRUMPETING_CALL,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "red junglefowl",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.CROWING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "indian peafowl",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.SCREECHING_CALL,
            ColorTrait.BLUE_CAMOUFLAGE
        ),
        animal(
            "pileated woodpecker",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.DRUMMING_DISPLAY,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "wandering albatross",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.EURYHALINE_OSMOREGULATION,
            CommonTrait.PELAGIC_SOARING_WINGS,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.WATERPROOF_PLUMAGE,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "turkey vulture",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.EXPANDABLE_CROP,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "african grey parrot",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.IMITATIVE_VOCALIZATION,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "common kingfisher",
            SizeClass.TINY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SPEAR_BILL,
            CommonTrait.WATERPROOF_PLUMAGE,
            ColorTrait.BLUE_CAMOUFLAGE
        ),
        animal(
            "andean condor",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.EXPANDABLE_CROP,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "snowy owl",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.HOOKED_TALONS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.FAT_RESERVES,
            CommonTrait.HOOTING_CALL,
            ColorTrait.WHITE_CAMOUFLAGE
        ),
        animal(
            "great blue heron",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.WADING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SPEAR_BILL,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.WATERPROOF_PLUMAGE,
            ColorTrait.BLUE_CAMOUFLAGE
        ),
        animal(
            "red-crowned crane",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.WADING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.SPEAR_BILL,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.TRUMPETING_CALL,
            CommonTrait.WATERPROOF_PLUMAGE,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "sandhill crane",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.WADING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.SPEAR_BILL,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.REGIONAL_MIGRATION,
            CommonTrait.TRUMPETING_CALL,
            CommonTrait.WATERPROOF_PLUMAGE,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "atlantic puffin",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.COASTAL_BREEDING_SITE,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.WATERPROOF_PLUMAGE,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "kakapo",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.FAT_RESERVES,
            CommonTrait.BOOMING_CALL,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "willow ptarmigan",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.INSULATING_PLUMAGE,
            CommonTrait.FAT_RESERVES,
            CommonTrait.CROAKING_CALL,
            ColorTrait.ADAPTIVE_CAMOUFLAGE
        ),
        animal(
            "keel-billed toucan",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.CROAKING_CALL,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "secretary bird",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.HOOKED_TALONS,
            CommonTrait.MOTION_TRACKING_SENSES,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "song sparrow",
            SizeClass.TINY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.CHIRPING_CALL,
            CommonTrait.BIRDSONG,
            CommonTrait.REGIONAL_MIGRATION,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "northern shrike",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.CACHED_FOOD,
            CommonTrait.CHIRPING_CALL,
            CommonTrait.BIRDSONG,
            CommonTrait.SOUND_LURES,
            ColorTrait.PALE_CAMOUFLAGE
        ),
    ).map { definition ->
        definition.copy(traits = definition.traits + CommonTrait.FEATHERS)
    }

    val REPTILES_AND_AMPHIBIANS: List<SpeciesDefinition> = listOf(
        animal(
            "nile crocodile",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.STRONG_JAWS,
            CommonTrait.OVOSPORE_NEST,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.SEASONAL_TORPOR,
            CommonTrait.BELLOWING_CALL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "american alligator",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.STRONG_JAWS,
            CommonTrait.OVOSPORE_NEST,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.SEASONAL_TORPOR,
            CommonTrait.BELLOWING_CALL,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "komodo dragon",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.FAT_RESERVES,
            CommonTrait.HISSING_WARNING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "green iguana",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "veiled chameleon",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.CAMOUFLAGE_PATTERN,
            CommonTrait.PROJECTILE_TONGUE,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "reticulated python",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.UNDULATING_BODY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.CONSTRICTING_BODY,
            CommonTrait.HISSING_WARNING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "king cobra",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.UNDULATING_BODY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.HISSING_WARNING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "western diamondback rattlesnake",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.UNDULATING_BODY,
            CommonTrait.SUBTERRANEAN_BURROWING,
            CommonTrait.HEAT_STABLE_ENZYMES,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.RATTLING_WARNING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "green sea turtle",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.PROTECTIVE_SHELL,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "galapagos tortoise",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.PROTECTIVE_SHELL,
            CommonTrait.FAT_RESERVES,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "red-eyed tree frog",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.PROJECTILE_TONGUE,
            CommonTrait.LEAPING_LEGS,
            CommonTrait.CROAKING_CALL,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "poison dart frog",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.PROJECTILE_TONGUE,
            CommonTrait.LEAPING_LEGS,
            CommonTrait.CROAKING_CALL,
            CommonTrait.TOXIC_SKIN,
            CommonTrait.APOSEMATIC_COLORATION,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "axolotl",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GILLS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "common mudpuppy",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GILLS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "tuatara",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SUBTERRANEAN_BURROWING,
            CommonTrait.SEASONAL_TORPOR,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "desert horned lizard",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.BEHAVIORAL_THERMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SUBTERRANEAN_BURROWING,
            CommonTrait.DRY_BURROW_NEST,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.WATER_RETENTIVE_SCALES,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "common snapping turtle",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.BEHAVIORAL_THERMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.PROTECTIVE_SHELL,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "leatherback sea turtle",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.STREAMLINED_BODY,
            CommonTrait.COASTAL_BREEDING_SITE,
            CommonTrait.FAT_RESERVES,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "marine iguana",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.BEHAVIORAL_THERMOREGULATION,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.WATER_RETENTIVE_SCALES,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "japanese giant salamander",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "ringed caecilian",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.UNDULATING_BODY,
            CommonTrait.SUBTERRANEAN_BURROWING,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
    )

    val FISH: List<SpeciesDefinition> = listOf(
        animal(
            "great white shark",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.STRONG_JAWS,
            CommonTrait.FAT_RESERVES,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "whale shark",
            SizeClass.HUGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.GILL_PADS,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "giant oceanic manta ray",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.GILL_PADS,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "atlantic bluefin tuna",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.STREAMLINED_BODY,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "atlantic salmon",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.EURYHALINE_OSMOREGULATION,
            CommonTrait.COLD_ACTIVE_ENZYMES,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "ocellaris clownfish",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GILL_PADS,
            CommonTrait.REEF_NESTING,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "lined seahorse",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BODY_CARRIED_OVOSPORES,
            CommonTrait.REEF_CAMOUFLAGE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "electric eel",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.ELECTRIC_ORGAN,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "deep-sea anglerfish",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.COLD_ACTIVE_ENZYMES,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BIOLUMINESCENT_LURE,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "porcupinefish",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GILLS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.QUILLS,
            CommonTrait.REEF_NESTING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "atlantic swordfish",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.STREAMLINED_BODY,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "channel catfish",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "common carp",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.BENTHIC_SUCTION_FEEDING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "peruvian anchoveta",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GILL_PADS,
            CommonTrait.SCHOOLING,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "coral grouper",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.REEF_CAMOUFLAGE,
            CommonTrait.REEF_SHELTER_DEPENDENCE,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "bumphead parrotfish",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.REEF_BORING,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "arapaima",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "antarctic silverfish",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.ANTIFREEZE_PROTEINS,
            CommonTrait.COLD_ACTIVE_ENZYMES,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "antarctic toothfish",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.ANTIFREEZE_PROTEINS,
            CommonTrait.COLD_ACTIVE_ENZYMES,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "mediterranean moray",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.REEF_CAMOUFLAGE,
            CommonTrait.REEF_SHELTER_DEPENDENCE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "european plaice",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.BENTHIC_BODY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.CAMOUFLAGE_PATTERN,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "pacific herring",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GILL_PADS,
            CommonTrait.SCHOOLING,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "alligator gar",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.ARMORED_HIDE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "atlantic flyingfish",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GILL_PADS,
            CommonTrait.STREAMLINED_BODY,
            CommonTrait.GLIDING_MEMBRANE,
            CommonTrait.SCHOOLING,
            ColorTrait.COUNTERSHADE_CAMOUFLAGE
        ),
        animal(
            "glacier lanternfish",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.BIOLUMINESCENT_LURE,
            CommonTrait.SCHOOLING,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "reef stonefish",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.BENTHIC_BODY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.REEF_CAMOUFLAGE,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
    ).map { definition ->
        val aquaticDefinition = definition.copy(
            traits = definition.traits.filterNot(::isCoreOvosporeTrait) +
                CommonTrait.AQUATIC_OVOSPORE,
        )
        if (CommonTrait.GILLS in aquaticDefinition.traits) {
            aquaticDefinition
        } else {
            aquaticDefinition.copy(traits = aquaticDefinition.traits + CommonTrait.GILLS)
        }
    }

    val INVERTEBRATES: List<SpeciesDefinition> = listOf(
        animal(
            "common octopus",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.GILLS,
            CommonTrait.JET_PROPULSION,
            CommonTrait.GRASPING_TENTACLES,
            CommonTrait.SUCTION_CUPS,
            CommonTrait.INK_CLOUD,
            ColorTrait.ADAPTIVE_CAMOUFLAGE
        ),
        animal(
            "giant squid",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.GILLS,
            CommonTrait.JET_PROPULSION,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.GRASPING_TENTACLES,
            CommonTrait.SUCTION_CUPS,
            CommonTrait.INK_CLOUD,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "moon jellyfish",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.PULSING_BELL,
            CommonTrait.GELATINOUS_BODY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "crown-of-thorns starfish",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.TOXIC_SKIN,
            CommonTrait.REEF_BORING,
            CommonTrait.APOSEMATIC_COLORATION,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "blue crab",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.GILLS,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.CRUSHING_CLAWS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.BLUE_CAMOUFLAGE
        ),
        animal(
            "american lobster",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.GILLS,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.CRUSHING_CLAWS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "cleaner shrimp",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.GILLS,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.REEF_NESTING,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.RED_CAMOUFLAGE
        ),
        sessile(
            "staghorn coral",
            SizeClass.MEDIUM,
            ColorTrait.BROWN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.POLYP_BODY,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.RIGID_COLONY_FRAMEWORK,
            CommonTrait.REEF_BUILDING,
            CommonTrait.CLONAL_PROPAGATION,
            CommonTrait.SHALLOW_WATER_PHOTOSYMBIOSIS,
            CommonTrait.WARM_WATER_ENZYMES
        ),
        sessile(
            "eastern oyster",
            SizeClass.TINY,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.GILL_PADS,
            CommonTrait.WARM_WATER_ENZYMES
        ),
        sessile(
            "blue mussel",
            SizeClass.TINY,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.GILL_PADS,
            CommonTrait.PROTECTIVE_SHELL
        ),
        animal(
            "garden snail",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.MUSCULAR_FOOT,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.PROTECTIVE_SHELL,
            CommonTrait.SLOW_METABOLISM,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "banana slug",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.MUSCULAR_FOOT,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.SLOW_METABOLISM,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "common earthworm",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.UNDULATING_BODY,
            CommonTrait.DECOMPOSING_ENZYMES,
            CommonTrait.SUBTERRANEAN_BURROWING,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "monarch butterfly",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.NECTAR_SIPPING_TONGUE,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.POLLEN_CARRYING_SURFACES,
            CommonTrait.TOXIC_SKIN,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.APOSEMATIC_COLORATION,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "western honey bee",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.NECTAR_SIPPING_TONGUE,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.POLLEN_CARRYING_SURFACES,
            CommonTrait.COLONY_LIVING,
            CommonTrait.COLONY_THERMOREGULATION,
            CommonTrait.OVOSPORE_NEST,
            CommonTrait.BROOD_PROVISIONING,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.HONEY_STORES,
            CommonTrait.APOSEMATIC_COLORATION,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "leafcutter ant",
            SizeClass.MINUSCULE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CRAWLING_APPENDAGES,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.COLONY_LIVING,
            CommonTrait.CLIMBING_LIMBS,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "termite",
            SizeClass.MINUSCULE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CRAWLING_APPENDAGES,
            CommonTrait.DECOMPOSING_ENZYMES,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.COLONY_LIVING,
            CommonTrait.SUBTERRANEAN_BURROWING,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "desert locust",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.HEAT_STABLE_ENZYMES,
            CommonTrait.REGIONAL_MIGRATION,
            CommonTrait.DRY_BURROW_NEST,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "seven-spot ladybird",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "orb-weaver spider",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.WEB_SILK,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "emperor scorpion",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CRAWLING_APPENDAGES,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.HEAT_STABLE_ENZYMES,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "common green darner dragonfly",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.REGIONAL_MIGRATION,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "common mosquito",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.SUCKING_PROBOSCIS,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.BURROWING_EGGS,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "giant centipede",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CRAWLING_APPENDAGES,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        sessile(
            "brain coral",
            SizeClass.MEDIUM,
            ColorTrait.BROWN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.POLYP_BODY,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.RIGID_COLONY_FRAMEWORK,
            CommonTrait.REEF_BUILDING,
            CommonTrait.CLONAL_PROPAGATION,
            CommonTrait.SHALLOW_WATER_PHOTOSYMBIOSIS,
            CommonTrait.WARM_WATER_ENZYMES
        ),
        sessile(
            "common sea fan",
            SizeClass.SMALL,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.POLYP_BODY,
            CommonTrait.SUSPENSION_FEEDING_TENTACLES,
            CommonTrait.CLONAL_PROPAGATION,
            ColorTrait.RED_CAMOUFLAGE
        ),
        sessile(
            "giant green anemone",
            SizeClass.SMALL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.POLYP_BODY,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.SHALLOW_WATER_PHOTOSYMBIOSIS
        ),
        sessile(
            "slender sea pen",
            SizeClass.SMALL,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.POLYP_BODY,
            CommonTrait.SUSPENSION_FEEDING_TENTACLES,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "sea wasp",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.PULSING_BELL,
            CommonTrait.GELATINOUS_BODY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "european mantis",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.CAMOUFLAGE_PATTERN,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "scarab dung beetle",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CRAWLING_APPENDAGES,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.WASTE_FEEDING_MOUTHPARTS,
            CommonTrait.DIGGING_CLAWS,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "pea aphid",
            SizeClass.MINUSCULE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.SUCKING_PROBOSCIS,
            CommonTrait.VIVIPARITY,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "spotted antlion",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CRAWLING_APPENDAGES,
            CommonTrait.SUBTERRANEAN_BURROWING,
            CommonTrait.DRY_BURROW_NEST,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "common walkingstick",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.CAMOUFLAGE_PATTERN,
            CommonTrait.MOLTING_EXOSKELETON,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "periodical cicada",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.SUCKING_PROBOSCIS,
            CommonTrait.PROLONGED_JUVENILE_DORMANCY,
            CommonTrait.MOLTING_EXOSKELETON,
            CommonTrait.CICADA_CHORUS,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
    )

    val PRODUCERS_AND_FUNGI: List<SpeciesDefinition> = listOf(
        sessile(
            "coast redwood",
            SizeClass.COLOSSAL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.NEEDLE_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.DEEP_ROOT_SYSTEM,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        sessile(
            "english oak",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FRUIT_BEARING,
            CommonTrait.FLOWERS,
            CommonTrait.SEASONAL_LEAF_DORMANCY,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.DEEP_ROOT_SYSTEM
        ),
        sessile(
            "scots pine",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.NEEDLE_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_LEAF_DORMANCY
        ),
        sessile(
            "african baobab",
            SizeClass.HUGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FRUIT_BEARING,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.SUCCULENT_STEM,
            CommonTrait.DROUGHT_DECIDUOUS_LEAVES,
            CommonTrait.WATER_STORAGE_TISSUE
        ),
        sessile(
            "umbrella thorn acacia",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FRUIT_BEARING,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.DEEP_ROOT_SYSTEM,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.DROUGHT_DECIDUOUS_LEAVES
        ),
        sessile(
            "red mangrove",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.LARGE_EVERGREEN_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.SALT_EXCLUDING_ROOTS,
            CommonTrait.WAXY_CUTICLE
        ),
        sessile(
            "saguaro cactus",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.SUCCULENT_STEM,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.FROST_SENSITIVE_SUCCULENT_TISSUES,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        sessile(
            "common sunflower",
            SizeClass.MEDIUM,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.DEEP_ROOT_SYSTEM,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        sessile(
            "perennial ryegrass",
            SizeClass.MEDIUM,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FLOWERS,
            CommonTrait.SEASONAL_LEAF_DORMANCY
        ),
        sessile(
            "common wheat",
            SizeClass.MEDIUM,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FLOWERS,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        sessile(
            "eucalyptus tree",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.LARGE_EVERGREEN_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.DEEP_ROOT_SYSTEM
        ),
        sessile(
            "giant bamboo",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FLOWERS,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.RAPID_GROWTH,
        ),
        sessile(
            "strangler fig",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.LARGE_EVERGREEN_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.SHADE_FRONDS,
            CommonTrait.FRUIT_BEARING,
            CommonTrait.FLOWERS,
            CommonTrait.DEEP_ROOT_SYSTEM
        ),
        sessile(
            "bracken fern",
            SizeClass.SMALL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.SHADE_FRONDS,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
            CommonTrait.SEASONAL_LEAF_DORMANCY
        ),
        sessile(
            "sphagnum moss",
            SizeClass.TINY,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.SURFACE_HOLDFAST,
            CommonTrait.INTERWOVEN_MAT,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SHADE_FRONDS
        ),
        sessile(
            "white water lily",
            SizeClass.SMALL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.FLOATING_FRONDS,
            CommonTrait.FLOWERS,
            CommonTrait.FRESHWATER_OSMOREGULATION
        ),
        sessile(
            "eelgrass",
            SizeClass.SMALL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.FLOWERS,
            CommonTrait.SALT_EXCLUDING_ROOTS
        ),
        sessile(
            "giant kelp",
            SizeClass.LARGE,
            ColorTrait.BROWN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.FLOATING_FRONDS,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        sessile(
            "field mushroom",
            SizeClass.SMALL,
            CommonTrait.ROOTED_BODY,
            CommonTrait.ABSORPTIVE_FILAMENTS,
            CommonTrait.DECOMPOSING_ENZYMES,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        sessile(
            "bread mold",
            SizeClass.TINY,
            CommonTrait.ROOTED_BODY,
            CommonTrait.ABSORPTIVE_FILAMENTS,
            CommonTrait.DECOMPOSING_ENZYMES,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        sessile(
            "reindeer lichen",
            SizeClass.TINY,
            // Its thallus is pale, but its photobiont still captures light with
            // chlorophyll rather than a pale photosynthetic pigment.
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.SURFACE_HOLDFAST,
            CommonTrait.INTERWOVEN_MAT,
            CommonTrait.ABSORPTIVE_FILAMENTS,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.WHOLE_BODY_ANHYDROBIOSIS
        ),
        sessile(
            "venus flytrap",
            SizeClass.SMALL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.WATER_STORAGE_TISSUE
        ),
        sessile(
            "siberian larch",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.NEEDLE_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_LEAF_DORMANCY,
            CommonTrait.DEEP_ROOT_SYSTEM
        ),
        sessile(
            "himalayan juniper",
            SizeClass.MEDIUM,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.NEEDLE_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.DEEP_ROOT_SYSTEM
        ),
        sessile(
            "lodgepole pine",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.NEEDLE_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_LEAF_DORMANCY,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        sessile(
            "ichu grass",
            SizeClass.TINY,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FLOWERS,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_LEAF_DORMANCY,
            CommonTrait.WAXY_CUTICLE
        ),
        sessile(
            "saharan cypress",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.NEEDLE_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.DEEP_ROOT_SYSTEM,
            CommonTrait.WAXY_CUTICLE
        ),
        sessile(
            "black spruce",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.NEEDLE_LEAVES,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_LEAF_DORMANCY,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        sessile(
            "common duckweed",
            SizeClass.TINY,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.FLOATING_FRONDS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.RAPID_GROWTH
        ),
        sessile(
            "common reed",
            SizeClass.MEDIUM,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.FLOWERS,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        sessile(
            "kudzu vine",
            SizeClass.MEDIUM,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.SHADE_FRONDS,
            CommonTrait.FLOWERS,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.RAPID_GROWTH
        ),
        sessile(
            "epiphytic orchid",
            SizeClass.SMALL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.EPIPHYTIC_ROOTS,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL
        ),
        sessile(
            "moss campion",
            SizeClass.TINY,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CUSHION_GROWTH,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        sessile(
            "prickly pear cactus",
            SizeClass.MEDIUM,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.SUCCULENT_STEM,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.FROST_SENSITIVE_SUCCULENT_TISSUES,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        sessile(
            "common bladderwort",
            SizeClass.TINY,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.FLOWERS
        ),
        sessile(
            "field horsetail",
            SizeClass.SMALL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL
        ),
        sessile(
            "honey fungus",
            SizeClass.SMALL,
            CommonTrait.ROOTED_BODY,
            CommonTrait.ABSORPTIVE_FILAMENTS,
            CommonTrait.DECOMPOSING_ENZYMES,
            CommonTrait.HOST_PENETRATING_FILAMENTS,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        sessile(
            "shelf fungus",
            SizeClass.SMALL,
            CommonTrait.ROOTED_BODY,
            CommonTrait.ABSORPTIVE_FILAMENTS,
            CommonTrait.DECOMPOSING_ENZYMES,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL
        ),
    ).map { definition ->
        if (definition.id in setOf("white-water-lily", "eelgrass", "giant-kelp")) {
            definition.copy(
                traits = definition.traits.filterNot(::isCoreOvosporeTrait) +
                    CommonTrait.AQUATIC_OVOSPORE,
            )
        } else {
            definition
        }
    }

    val ALL: List<SpeciesDefinition> =
        MAMMALS /*+ EXTINCT_SPECIES*/ + BIRDS + REPTILES_AND_AMPHIBIANS + FISH +
            INVERTEBRATES + PRODUCERS_AND_FUNGI

    fun animal(
        name: String,
        sizeClass: SizeClass,
        thermalStrategy: CommonTrait,
        vararg adaptations: SpeciesTrait,
        biochemistry: CommonTrait = CommonTrait.TEMPERATE_BIOCHEMISTRY,
    ): SpeciesDefinition {
        val reproduction =
            if (adaptations.any { TraitCapability.REPRODUCTION in it.capabilities }) {
                emptyList()
            } else {
                listOf(defaultOvosporeTrait(adaptations))
            }
        return SpeciesDefinition(
            id = idFromName(name),
            displayName = name,
            sizeClass = sizeClass,
            motile = true,
            traits = listOf(biochemistry, thermalStrategy) + reproduction + adaptations,
        )
    }

    fun sessile(
        name: String,
        sizeClass: SizeClass,
        vararg adaptations: SpeciesTrait,
    ): SpeciesDefinition {
        val reproduction =
            if (adaptations.any { TraitCapability.REPRODUCTION in it.capabilities }) {
                emptyList()
            } else {
                listOf(defaultOvosporeTrait(adaptations))
            }
        return SpeciesDefinition(
            id = idFromName(name),
            displayName = name,
            sizeClass = sizeClass,
            motile = false,
            traits = listOf(CommonTrait.TEMPERATE_BIOCHEMISTRY) + reproduction + adaptations,
        )
    }

    private fun defaultOvosporeTrait(adaptations: Array<out SpeciesTrait>): CommonTrait {
        val supportedHabitats = adaptations
            .flatMap { it.effects }
            .filterIsInstance<TraitEffect.HabitatSupport>()
            .filter { it.amount > 0.0 }
            .mapTo(mutableSetOf()) { it.habitat }
        val aquatic = supportedHabitats.any {
            it == Habitat.COASTAL ||
                it == Habitat.FRESHWATER ||
                it == Habitat.SUNLIT_WATER ||
                it == Habitat.DARK_WATER
        }
        val terrestrial = supportedHabitats.any {
            it == Habitat.LAND_SURFACE || it == Habitat.CANOPY || it == Habitat.SEA_ICE
        }
        return if (aquatic && !terrestrial) {
            CommonTrait.AQUATIC_OVOSPORE
        } else {
            CommonTrait.TERRESTRIAL_OVOSPORE
        }
    }

    private fun isCoreOvosporeTrait(trait: SpeciesTrait): Boolean =
        trait == CommonTrait.TERRESTRIAL_OVOSPORE || trait == CommonTrait.AQUATIC_OVOSPORE

    fun idFromName(name: String): String =
        Normalizer
            .normalize(name, Normalizer.Form.NFD)
            .replace(Regex("\\p{M}+"), "")
            .lowercase(Locale.ROOT)
            .replace(Regex("['’]"), "")
            .replace(Regex("[^a-z0-9]+"), "-")
            .trim('-')

    fun obligateBrowser(
        foodSpeciesId: String,
        displayName: String,
    ) = TargetedRelationshipTrait(
        displayName = displayName,
        description =
        "Digestive and feeding anatomy is specialized around one locally available producer lineage.",
        relationships = listOf(
            RelationshipEffect.ObligateFood(
                target = SpeciesSelector.ExactSpecies(foodSpeciesId),
                attackRate = 0.0015,
                assimilationEfficiency = 0.65,
            ),
        ),
        maintenanceCost = 0.04,
    )
}