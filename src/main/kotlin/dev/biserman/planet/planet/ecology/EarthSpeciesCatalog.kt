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
            CommonTrait.OPEN_COUNTRY_HERDING,
            CommonTrait.REGIONAL_MIGRATION,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "cheetah",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
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
            CommonTrait.ARMORED_HIDE
        ),
        animal(
            "white rhinoceros",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            obligateBrowser(
                foodSpeciesId = "giant-bamboo",
                displayName = "bamboo feeding specialization",
            ),
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
            ColorTrait.WHITE_CAMOUFLAGE
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
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "american bison",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.SWIFT_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.RUMINANT_STOMACH,
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            ColorTrait.PALE_CAMOUFLAGE
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
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "west Indian manatee",
            SizeClass.LARGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.AQUATIC_FLIPPERS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
        if (definition.id in naturallyHairlessMammalIds) {
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
            CommonTrait.DENSE_UNDERCOAT,
            CommonTrait.SEASONAL_WINTER_COAT,
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
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "scarlet macaw",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.EXTENDED_PARENTAL_CARE,
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
            CommonTrait.OPEN_COUNTRY_HERDING,
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
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "red junglefowl",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "indian peafowl",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
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
            ColorTrait.PALE_CAMOUFLAGE
        ),
        animal(
            "common kingfisher",
            SizeClass.TINY,
            CommonTrait.ENDOTHERMY,
            CommonTrait.FEATHERED_WINGS,
            CommonTrait.AMBUSH_MUSCULATURE,
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
            CommonTrait.OVOSPORE_NEST,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.SEASONAL_TORPOR,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "american alligator",
            SizeClass.LARGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.AMPHIBIOUS_LIMBS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.OVOSPORE_NEST,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.SEASONAL_TORPOR,
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
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "king cobra",
            SizeClass.MEDIUM,
            CommonTrait.ECTOTHERMY,
            CommonTrait.UNDULATING_BODY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
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
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "poison dart frog",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.PROJECTILE_TONGUE,
            CommonTrait.LEAPING_LEGS,
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
            CommonTrait.TOOL_MANIPULATION,
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
            CommonTrait.BUOYANCY_BLADDER,
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
            CommonTrait.ARMORED_HIDE,
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
            CommonTrait.ARMORED_HIDE,
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
            ColorTrait.RED_CAMOUFLAGE
        ),
        sessile(
            "staghorn coral",
            SizeClass.MEDIUM,
            ColorTrait.BROWN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.DIFFUSIVE_AQUATIC_GAS_EXCHANGE,
            CommonTrait.SUBSTRATE_HOLDFAST,
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
            ColorTrait.RED_CAMOUFLAGE
        ),
        animal(
            "orb-weaver spider",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.WEB_SILK,
            CommonTrait.VENOM_DELIVERY,
            ColorTrait.BROWN_CAMOUFLAGE
        ),
        animal(
            "emperor scorpion",
            SizeClass.SMALL,
            CommonTrait.ECTOTHERMY,
            CommonTrait.CRAWLING_APPENDAGES,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY,
            CommonTrait.ARMORED_HIDE,
            CommonTrait.HEAT_STABLE_ENZYMES,
            ColorTrait.BLACK_CAMOUFLAGE
        ),
        animal(
            "common green darner dragonfly",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.REGIONAL_MIGRATION,
            ColorTrait.GREEN_CAMOUFLAGE
        ),
        animal(
            "common mosquito",
            SizeClass.TINY,
            CommonTrait.ECTOTHERMY,
            CommonTrait.INSECTOID_WINGS,
            CommonTrait.SUCKING_PROBOSCIS,
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
            ColorTrait.BROWN_CAMOUFLAGE
        ),
    )

    val PRODUCERS_AND_FUNGI: List<SpeciesDefinition> = listOf(
        sessile(
            "coast redwood",
            SizeClass.COLOSSAL,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_LEAF_DORMANCY
        ),
        sessile(
            "african baobab",
            SizeClass.HUGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.ROOTED_BODY,
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
            CommonTrait.ROOTED_BODY,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
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
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.DEEP_ROOT_SYSTEM,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.DROUGHT_DECIDUOUS_LEAVES
        ),
        sessile(
            "black spruce",
            SizeClass.LARGE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_LEAF_DORMANCY,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
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
    ) = SpeciesDefinition(
        id = idFromName(name),
        displayName = name,
        sizeClass = sizeClass,
        motile = false,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            defaultOvosporeTrait(adaptations),
        ) + adaptations,
    )

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
