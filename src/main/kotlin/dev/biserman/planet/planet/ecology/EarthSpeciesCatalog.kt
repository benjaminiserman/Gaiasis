package dev.biserman.planet.planet.ecology

import dev.biserman.planet.planet.ecology.earthlike_clades.*
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
    val MAMMALS: List<SpeciesDefinition> = listOf(
        elephant.descend(
            "african elephant",
            SizeClass.HUGE,
            CommonTrait.CALM,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.SLOW_GROWTH.atLevel(2),
            CommonTrait.INFREQUENT_REPRODUCTION,
            CommonTrait.GROUP_LIVING,
        ),
        giraffe.descend(
            "giraffe",
            SizeClass.LARGE,
            CommonTrait.SKITTISH,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
        ),
        horse.descend(
            "plains zebra",
            SizeClass.LARGE,
            CommonTrait.TERRESTRIAL_CAMOUFLAGE,
            ColorTrait.PALE_COLORATION,
        ),
        antelope.descend(
            "blue wildebeest",
            SizeClass.LARGE,
            CommonTrait.BELLOWING_CALL,
        ),
        antelope.descend(
            "thomson's gazelle",
            SizeClass.MEDIUM,
        ),
        panther.descend(
            "african lion",
            SizeClass.LARGE,
            CommonTrait.CATHEMERAL,
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.GROUP_LIVING,
        ),
        cat.descend(
            "cheetah",
            SizeClass.MEDIUM,
            CommonTrait.DIURNAL,
            CommonTrait.SWIFT_LIMBS.atLevel(2),
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
        ),
        carnivore.descend(
            "spotted hyena",
            SizeClass.MEDIUM,
            CommonTrait.AGGRESSIVE,
            CommonTrait.SWIFT_LIMBS.atLevel(1),
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.SCENT.atLevel(3),
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.STRONG_JAWS,
            CommonTrait.WHOOPING_CALL,
            CommonTrait.GROUP_LIVING,
        ),
        ungulate.descend(
            "hippopotamus",
            SizeClass.LARGE,
            CommonTrait.AGGRESSIVE,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(2),
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE.atLevel(2),
            CommonTrait.BULKY_PHYSIQUE,
            CommonTrait.LONG_TUSKS.atLevel(1),
            CommonTrait.STRONG_JAWS,
            CommonTrait.GRUNTING_CALL,
            CommonTrait.SOLITARY,
        ),
        ungulate.descend(
            "white rhinoceros",
            SizeClass.LARGE,
            CommonTrait.AGGRESSIVE,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE.atLevel(2),
            CommonTrait.BULKY_PHYSIQUE,
            CommonTrait.HORNS.atLevel(2),
            CommonTrait.SLOW_GROWTH.atLevel(1),
            CommonTrait.INFREQUENT_REPRODUCTION,
            CommonTrait.SOLITARY,
        ),
        ape.descend(
            "western gorilla",
            SizeClass.LARGE,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.BULKY_PHYSIQUE,
            ColorTrait.BLACK_COLORATION,
        ),
        ape.descend(
            "chimpanzee",
            SizeClass.MEDIUM,
            ColorTrait.BLACK_COLORATION,
        ),
        ape.descend(
            "bornean orangutan",
            SizeClass.MEDIUM,
            CommonTrait.SOLITARY,
        ),
        monkey.descend(
            "rhesus macaque",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.TOOL_MANIPULATION,
        ),
        lemur.descend(
            "ring-tailed lemur",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.FOOD_DERIVED_WATER,
        ),
        bear.descend(
            "polar bear",
            SizeClass.LARGE,
            CommonTrait.AGGRESSIVE,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(2),
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BLUBBER,
            CommonTrait.DENSE_UNDERCOAT.atLevel(3),
            CommonTrait.INFREQUENT_REPRODUCTION,
            ColorTrait.WHITE_COLORATION,
            CommonTrait.STRONG_JAWS,
        ),
        bear.descend(
            "brown bear",
            SizeClass.LARGE,
            CommonTrait.AGGRESSIVE,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.STRONG_JAWS,
            CommonTrait.INFREQUENT_REPRODUCTION,
        ),
        wolf.descend(
            "gray wolf",
            SizeClass.MEDIUM,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.FAT_RESERVES,
        ),
        fox.descend(
            "red fox",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
        ),
        hare.descend(
            "snowshoe hare",
            SizeClass.SMALL,
            CommonTrait.HETEROTHERMY,
            CommonTrait.DENSE_UNDERCOAT.atLevel(3),
            CommonTrait.SEASONAL_WINTER_COAT,
            ColorTrait.WHITE_COLORATION,
        ),
        rabbit.descend(
            "european rabbit",
            SizeClass.SMALL,
            CommonTrait.DENSE_UNDERCOAT.atLevel(1),
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.FREQUENT_REPRODUCTION,
        ),
        rodent.descend(
            "north American beaver",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(1),
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.DAM_BUILDING,
        ),
        squirrel.descend(
            "red squirrel",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
        ),
        porcupine.descend(
            "north American porcupine",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.SOLITARY,
        ),
        marmot.descend(
            "hoary marmot",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.FAT_RESERVES,
            CommonTrait.GROUP_HUDDLING,
        ),
        mouse.descend(
            "house mouse",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.SOLITARY,
        ),
        rat.descend(
            "norway rat",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SOLITARY,
        ),
        mammal.descend(
            "red kangaroo",
            SizeClass.MEDIUM,
            CommonTrait.SKITTISH,
            CommonTrait.SWIFT_LIMBS.atLevel(1),
            CommonTrait.LEAPING_LEGS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.BROOD_POUCH,
            CommonTrait.COLLECTIVE_LIVING,
        ),
        mammal.descend(
            "koala",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.BROOD_POUCH,
            obligateBrowser(
                foodSpeciesId = "eucalyptus-tree",
                displayName = "eucalyptus leaf specialization",
            ),
            CommonTrait.BELLOWING_CALL,
        ),
        bear.descend(
            "giant panda",
            SizeClass.LARGE,
            CommonTrait.CALM,
            CommonTrait.FERMENTING_HINDGUT,
            obligateBrowser(
                foodSpeciesId = "giant-bamboo",
                displayName = "bamboo feeding specialization",
            ),
            CommonTrait.BLEATING_CALL,
            CommonTrait.SLOW_GROWTH.atLevel(1),
            CommonTrait.INFREQUENT_REPRODUCTION,
            ColorTrait.PALE_COLORATION,
        ),
        panther.descend(
            "bengal tiger",
            SizeClass.LARGE,
        ),
        panther.descend(
            "snow leopard",
            SizeClass.MEDIUM,
            CommonTrait.VESPERTINE,
            CommonTrait.DENSE_UNDERCOAT.atLevel(3),
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.HYPOXIA_RESPONSIVE_METABOLISM,
            CommonTrait.SNOW_AND_ICE_LICKING,
            CommonTrait.FAT_RESERVES,
            CommonTrait.MEOWING_CALL,
            CommonTrait.PURRING_CALL,
            ColorTrait.WHITE_COLORATION,
        ),
        cat.descend(
            "margay",
            SizeClass.SMALL,
            CommonTrait.SOUND_LURES,
        ),
        cat.descend(
            "european wildcat",
            SizeClass.SMALL,
            CommonTrait.SOUND_LURES,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.SEASONAL_WINTER_COAT,
        ),
        deer.descend(
            "white-tailed deer",
            SizeClass.MEDIUM,
            CommonTrait.ANTLERS,
            CommonTrait.SOLITARY,
        ),
        bison.descend(
            "american bison",
            SizeClass.LARGE,
            CommonTrait.CALM,
        ),
        camel.descend(
            "dromedary camel",
            SizeClass.LARGE,
            CommonTrait.HEAT_STABLE_ENZYMES.atLevel(2),
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        pig.descend(
            "wild boar",
            SizeClass.MEDIUM,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.DIGGING_LIMBS.atLevel(1),
            CommonTrait.BULKY_PHYSIQUE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.COLLECTIVE_LIVING,
        ),
        whale.descend(
            "blue whale",
            SizeClass.COLOSSAL,
            CommonTrait.CALM,
            CommonTrait.INFREQUENT_REPRODUCTION,
            ColorTrait.BLUE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        whale.descend(
            "humpback whale",
            SizeClass.COLOSSAL,
            CommonTrait.CALM,
            CommonTrait.SOLITARY,
        ),
        dolphin.descend(
            "orca",
            SizeClass.LARGE,
            CommonTrait.AGGRESSIVE,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.STRONG_JAWS,
        ),
        dolphin.descend(
            "bottlenose dolphin",
            SizeClass.LARGE,
            CommonTrait.CALM,
        ),
        seal.descend(
            "harbor seal",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            ColorTrait.PALE_COLORATION,
        ),
        seal.descend(
            "weddell seal",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.TRILLING_CALL,
        ),
        seal.descend(
            "crabeater seal",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.SIEVING_TEETH.atLevel(1),
            ColorTrait.PALE_COLORATION,
            minus = listOf(CommonTrait.MEAT_EATING_MOUTHPARTS),
        ),
        otter.descend(
            "sea otter",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.SLENDER_PHYSIQUE,
            CommonTrait.SOLITARY,
        ),
        seal.descend(
            "walrus",
            SizeClass.LARGE,
            CommonTrait.AGGRESSIVE,
            CommonTrait.SUCTION_FEEDING,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.BULKY_PHYSIQUE,
            CommonTrait.LONG_TUSKS.atLevel(2),
            CommonTrait.BELLOWING_CALL,
            CommonTrait.INFREQUENT_REPRODUCTION,
            ColorTrait.BROWN_COLORATION,
        ),
        mammal.descend(
            "west Indian manatee",
            SizeClass.LARGE,
            CommonTrait.CALM,
            CommonTrait.AQUATIC_LIMBS.atLevel(2),
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.CHIRPING_CALL,
            CommonTrait.SLOW_GROWTH.atLevel(2),
            CommonTrait.INFREQUENT_REPRODUCTION,
            ColorTrait.PALE_COLORATION,
        ),
        microbat.descend(
            "little brown bat",
            SizeClass.TINY,
            CommonTrait.SKITTISH,
            CommonTrait.SOLITARY,
        ),
        megabat.descend(
            "large flying fox",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.SOLITARY,
        ),
        mammal.descend(
            "duck-billed platypus",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(2),
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.TERRESTRIAL_OVOSPORE,
        ),
        mammal.descend(
            "three-toed sloth",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.HETEROTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.SLOW_METABOLISM.atLevel(2),
        ),
        mammal.descend(
            "giant anteater",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.COLONY_PROBING_TONGUE,
            CommonTrait.DIGGING_LIMBS.atLevel(2),
        ),
        mammal.descend(
            "sugar glider",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.HETEROTHERMY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.GLIDING_MEMBRANE,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.NECTAR_SIPPING_TONGUE,
            CommonTrait.SLENDER_PHYSIQUE,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.BROOD_POUCH,
        ),
        hedgehog.descend(
            "european hedgehog",
            SizeClass.SMALL,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.FAT_RESERVES,
        ),
        mole.descend(
            "european mole",
            SizeClass.SMALL,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
        ),
        shrew.descend(
            "common shrew",
            SizeClass.SMALL,
            CommonTrait.FREQUENT_REPRODUCTION,
        ),
        gymnure.descend(
            "moonrat",
            SizeClass.SMALL,
            ColorTrait.WHITE_COLORATION,
        ),
        // Procyonids
        raccoon.descend(
            "common raccoon",
            SizeClass.SMALL,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SOLITARY,
        ),
        raccoon.descend(
            "white-nosed coati",
            SizeClass.SMALL,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SLENDER_PHYSIQUE,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SOLITARY,
        ),
        raccoon.descend(
            "kinkajou",
            SizeClass.SMALL,
            CommonTrait.FRUIT_EATING_MOUTHPARTS,
            CommonTrait.NECTAR_SIPPING_TONGUE,
            CommonTrait.SLENDER_PHYSIQUE,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SOLITARY,
        ),
        // Mustelids
        mustelid.descend(
            "wolverine",
            SizeClass.MEDIUM,
            CommonTrait.STRONG_JAWS,
            CommonTrait.BULKY_PHYSIQUE,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.FAT_RESERVES,
        ),
        badger.descend(
            "european badger",
            SizeClass.SMALL,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BULKY_PHYSIQUE,
        ),
        badger.descend(
            "honey badger",
            SizeClass.SMALL,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.ARMORED_HIDE.atLevel(1),
            CommonTrait.VENOM_RESISTANCE.atLevel(2),
        ),
        weasel.descend(
            "stoat",
            SizeClass.SMALL,
            CommonTrait.SLENDER_PHYSIQUE,
            CommonTrait.SEASONAL_WINTER_COAT,
            ColorTrait.WHITE_COLORATION,
        ),
        otter.descend(
            "north American river otter",
            SizeClass.SMALL,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.SLENDER_PHYSIQUE,
            CommonTrait.SOLITARY,
        ),
        // Siberian boreal forest and taiga
        deer.descend(
            "siberian musk deer",
            SizeClass.MEDIUM,
            CommonTrait.SOLITARY,
        ),
        mustelid.descend(
            "sable",
            SizeClass.SMALL,
            CommonTrait.CACHED_FOOD,
        ),
        // Himalayan and Tibetan alpine plateau
        bison.descend(
            "wild yak",
            SizeClass.LARGE,
            CommonTrait.SEASONAL_WINTER_COAT,
            CommonTrait.ENLARGED_CARDIOPULMONARY_SYSTEM,
            CommonTrait.SNOW_AND_ICE_LICKING,
            CommonTrait.FAT_RESERVES,
            ColorTrait.BLACK_COLORATION,
        ),
        pika.descend(
            "himalayan pika",
            SizeClass.SMALL,
            CommonTrait.HETEROTHERMY,
            CommonTrait.DIURNAL,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.BURROW_BORROWER,
        ),
        // Rocky Mountains
        deer.descend(
            "rocky Mountain elk",
            SizeClass.LARGE,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.ANTLERS,
            CommonTrait.BUGLING_CALL,
            CommonTrait.SHORT_MIGRATION,
            CommonTrait.COLLECTIVE_LIVING,
        ),
        goat.descend(
            "mountain goat",
            SizeClass.MEDIUM,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.HORNS.atLevel(2),
            ColorTrait.WHITE_COLORATION,
            CommonTrait.COLLECTIVE_LIVING,
        ),
        // High Andes
        camel.descend(
            "vicuña",
            SizeClass.MEDIUM,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.HIGH_AFFINITY_BLOOD,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.COLLECTIVE_LIVING,
        ),
        // Sahara
        antelope.descend(
            "addax",
            SizeClass.LARGE,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.HEAT_STABLE_ENZYMES.atLevel(2),
            ColorTrait.PALE_COLORATION,
        ),
        fox.descend(
            "fennec fox",
            SizeClass.SMALL,
            CommonTrait.HETEROTHERMY,
            CommonTrait.NOCTURNAL,
            CommonTrait.MASSIVE_EARS.atLevel(1),
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.SAND_ADAPTATION,
            ColorTrait.PALE_COLORATION,
        ),
        rodent.descend(
            "jerboa",
            SizeClass.TINY,
            CommonTrait.HETEROTHERMY,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.BURROW_BORROWER,
            CommonTrait.SAND_ADAPTATION,
            ColorTrait.PALE_COLORATION,
        ),
        // Canadian Shield boreal forest
        deer.descend(
            "woodland caribou",
            SizeClass.LARGE,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.HERDING_BEHAVIOR,
            CommonTrait.ANTLERS,
            CommonTrait.REGIONAL_MIGRATION,
            CommonTrait.COLLECTIVE_LIVING,
        ),
        cat.descend(
            "canada lynx",
            SizeClass.MEDIUM,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.SEASONAL_WINTER_COAT,
            ColorTrait.PALE_COLORATION,
        ),
    )

    val EXTINCT_SPECIES: List<SpeciesDefinition> = listOf(
        reptile.descend(
            "tyrannosaurus rex",
            SizeClass.HUGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.STRONG_JAWS,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.FAT_RESERVES,
            ColorTrait.BROWN_COLORATION,
            minus = listOf(CommonTrait.BEHAVIORAL_THERMOREGULATION)
        ),
        reptile.descend(
            "velociraptor",
            SizeClass.SMALL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.SWIFT_LIMBS.atLevel(2),
            CommonTrait.FEATHERS,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.HOOKED_TALONS,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.INSULATING_PLUMAGE.atLevel(1),
            CommonTrait.COOPERATIVE_HUNTING,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.GROUP_LIVING,
            minus = listOf(CommonTrait.BEHAVIORAL_THERMOREGULATION)
        ),
        reptile.descend(
            "triceratops",
            SizeClass.HUGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE.atLevel(2),
            CommonTrait.HORNS.atLevel(2),
            ColorTrait.BROWN_COLORATION,
            minus = listOf(CommonTrait.BEHAVIORAL_THERMOREGULATION)
        ),
        reptile.descend(
            "stegosaurus",
            SizeClass.HUGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FERMENTING_HINDGUT,
            CommonTrait.ARMORED_HIDE.atLevel(2),
            ColorTrait.BROWN_COLORATION,
            minus = listOf(CommonTrait.BEHAVIORAL_THERMOREGULATION)
        ),
        reptile.descend(
            "ankylosaurus",
            SizeClass.HUGE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.ARMORED_HIDE.atLevel(2),
            ColorTrait.BROWN_COLORATION,
            minus = listOf(CommonTrait.BEHAVIORAL_THERMOREGULATION)
        ),
        reptile.descend(
            "brachiosaurus",
            SizeClass.HUGE,
            CommonTrait.ENDOTHERMY,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.LONG_NECK,
            CommonTrait.FERMENTING_HINDGUT,
            ColorTrait.BROWN_COLORATION,
            minus = listOf(CommonTrait.BEHAVIORAL_THERMOREGULATION)
        ),
        reptile.descend(
            "titanosaurus",
            SizeClass.COLOSSAL,
            CommonTrait.ENDOTHERMY,
            CommonTrait.BROWSING_MOUTHPARTS,
            CommonTrait.LONG_NECK,
            CommonTrait.FERMENTING_HINDGUT,
            ColorTrait.BROWN_COLORATION,
            minus = listOf(CommonTrait.BEHAVIORAL_THERMOREGULATION)
        ),
        reptile.descend(
            "pteranodon",
            SizeClass.MEDIUM,
            CommonTrait.ENDOTHERMY,
            CommonTrait.WINGS.atLevel(2),
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.PALE_COLORATION,
            minus = listOf(CommonTrait.BEHAVIORAL_THERMOREGULATION)
        ),
        elephant.descend(
            "woolly mammoth",
            SizeClass.HUGE,
            CommonTrait.DENSE_UNDERCOAT.atLevel(2),
            CommonTrait.SEASONAL_WINTER_COAT,
        ),
        felid.descend(
            "saber-toothed cat",
            SizeClass.LARGE,
            CommonTrait.COOPERATIVE_HUNTING,
            CommonTrait.GROUP_LIVING,
        ),
        bird.descend(
            "dodo",
            SizeClass.SMALL,
            CommonTrait.SEED_CRACKING_MOUTHPARTS,
            CommonTrait.FAT_RESERVES,
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY
        ),
        shark.descend(
            "megalodon",
            SizeClass.HUGE,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.FAT_RESERVES,
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY
        ),
        arthropod.descend(
            "trilobite",
            SizeClass.SMALL,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.MARINE_SNOW_COLLECTORS,
            CommonTrait.ARMORED_HIDE.atLevel(2),
            CommonTrait.SALTWATER_OSMOREGULATION,
            ColorTrait.BROWN_COLORATION,
        ),
        cephalopod.descend(
            "ammonite",
            SizeClass.MEDIUM,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.PROTECTIVE_SHELL,
            ColorTrait.PALE_COLORATION,
        ),
    )

    val BIRDS: List<SpeciesDefinition> = listOf(
        hawk.descend(
            "bald eagle",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.INSULATING_PLUMAGE.atLevel(2),
            CommonTrait.SOLITARY,
        ),
        owl.descend(
            "great horned owl",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.SOLITARY,
        ),
        hawk.descend(
            "peregrine falcon",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.INSULATING_PLUMAGE.atLevel(2),
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        hummingbird.descend(
            "ruby-throated hummingbird",
            SizeClass.TINY,
            CommonTrait.SKITTISH,
            ColorTrait.GREEN_COLORATION,
            CommonTrait.SOLITARY,
        ),
        parrot.descend(
            "scarlet macaw",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.SCREECHING_CALL,
            ColorTrait.RED_COLORATION,
            CommonTrait.SOLITARY,
        ),
        corvid.descend(
            "common raven",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.INSULATING_PLUMAGE.atLevel(2),
            CommonTrait.SOLITARY,
        ),
        penguin.descend(
            "emperor penguin",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.BODY_CARRIED_OVOSPORES,
            CommonTrait.COLD_ACTIVE_ENZYMES.atLevel(2),
            CommonTrait.GROUP_HUDDLING,
            CommonTrait.FLIGHTLESS_WINGS
        ),
        ratite.descend(
            "common ostrich",
            SizeClass.LARGE,
            CommonTrait.SKITTISH,
            CommonTrait.LEAPING_LEGS,
            CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            CommonTrait.BOOMING_CALL,
            CommonTrait.FLIGHTLESS_WINGS
        ),
        bird.descend(
            "greater flamingo",
            SizeClass.MEDIUM,
            CommonTrait.SKITTISH,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(2),
            CommonTrait.EURYHALINE_OSMOREGULATION,
            CommonTrait.SIEVING_TEETH.atLevel(2),
            CommonTrait.WATERPROOF_PLUMAGE,
            CommonTrait.HONKING_CALL,
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        bird.descend(
            "brown pelican",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(1),
            CommonTrait.EURYHALINE_OSMOREGULATION,
            CommonTrait.WATERPROOF_PLUMAGE,
            CommonTrait.SOLITARY,
            CommonTrait.SCOOP_MOUTH,
        ),
        grebe.descend(
            "great crested grebe",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.INSULATING_PLUMAGE.atLevel(1),
            CommonTrait.AQUATIC_CAMOUFLAGE,
        ),
        waterfowl.descend(
            "mallard duck",
            SizeClass.SMALL,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.QUACKING_CALL,
        ),
        waterfowl.descend(
            "canada goose",
            SizeClass.SMALL,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.HONKING_CALL,
            CommonTrait.COLLECTIVE_LIVING
        ),
        waterfowl.descend(
            "mute swan",
            SizeClass.SMALL,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.TRUMPETING_CALL,
            ColorTrait.PALE_COLORATION,
        ),
        fowl.descend(
            "red junglefowl",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.CROWING_CALL,
        ),
        fowl.descend(
            "indian peafowl",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.SCREECHING_CALL,
            ColorTrait.BLUE_COLORATION,
        ),
        woodpecker.descend(
            "pileated woodpecker",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            ColorTrait.BLACK_COLORATION,
            CommonTrait.SOLITARY,
        ),
        gull.descend(
            "wandering albatross",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(1),
            CommonTrait.EURYHALINE_OSMOREGULATION,
            CommonTrait.PELAGIC_SOARING,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.INFREQUENT_REPRODUCTION,
            CommonTrait.SOLITARY,
        ),
        vulture.descend(
            "turkey vulture",
            SizeClass.SMALL,
            CommonTrait.CALM,
            ColorTrait.BLACK_COLORATION,
            CommonTrait.SOLITARY,
        ),
        parrot.descend(
            "african grey parrot",
            SizeClass.SMALL,
            CommonTrait.CALM,
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        bird.descend(
            "common kingfisher",
            SizeClass.TINY,
            CommonTrait.AGGRESSIVE,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SPEAR_BILL,
            CommonTrait.WATERPROOF_PLUMAGE,
            ColorTrait.BLUE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        vulture.descend(
            "andean condor",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.INSULATING_PLUMAGE.atLevel(2),
            CommonTrait.INFREQUENT_REPRODUCTION,
            CommonTrait.HYPOXIA_RESPONSIVE_METABOLISM,
            ColorTrait.BLACK_COLORATION,
            CommonTrait.SOLITARY,
        ),
        owl.descend(
            "snowy owl",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.FAT_RESERVES,
            ColorTrait.WHITE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        bird.descend(
            "great blue heron",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.WADING_LIMBS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SPEAR_BILL,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.WATERPROOF_PLUMAGE,
            ColorTrait.BLUE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        crane.descend(
            "red-crowned crane",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.LONG_MIGRATION,
            ColorTrait.PALE_COLORATION,
        ),
        crane.descend(
            "sandhill crane",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.OPEN_COUNTRY_PREFERENCE,
            ColorTrait.PALE_COLORATION,
        ),
        gull.descend(
            "atlantic puffin",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(2),
            CommonTrait.PROLONGED_BREATH_HOLDING,
            CommonTrait.SALTWATER_OSMOREGULATION,
            CommonTrait.COASTAL_BREEDING_SITE,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.INSULATING_PLUMAGE.atLevel(2),
            ColorTrait.COUNTERSHADE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        parrot.descend(
            "kakapo",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.FAT_RESERVES,
            CommonTrait.BOOMING_CALL,
            CommonTrait.INFREQUENT_REPRODUCTION,
            ColorTrait.GREEN_COLORATION,
            CommonTrait.SOLITARY,
            CommonTrait.FLIGHTLESS_WINGS
        ),
        fowl.descend(
            "willow ptarmigan",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.INSULATING_PLUMAGE.atLevel(2),
            CommonTrait.FAT_RESERVES,
            CommonTrait.CROAKING_CALL,
            ColorTrait.ADAPTIVE_COLORATION,
            CommonTrait.SOLITARY,
            minus = listOf(CommonTrait.HERDING_BEHAVIOR)
        ),
        hornbill.descend(
            "keel-billed toucan",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.EXTENDED_PARENTAL_CARE,
            CommonTrait.SOLITARY,
        ),
        hawk.descend(
            "secretary bird",
            SizeClass.MEDIUM,
            CommonTrait.AGGRESSIVE,
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY,
        ),
        songbird.descend(
            "song sparrow",
            SizeClass.TINY,
            CommonTrait.SOLITARY,
        ),
        songbird.descend(
            "northern shrike",
            SizeClass.SMALL,
            CommonTrait.CACHED_FOOD,
            CommonTrait.SOUND_LURES,
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY,
        ),
    )

    val REPTILES_AND_AMPHIBIANS: List<SpeciesDefinition> = listOf(
        crocodile.descend(
            "nile crocodile",
            SizeClass.LARGE,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            ColorTrait.BROWN_COLORATION,
        ),
        crocodile.descend(
            "american alligator",
            SizeClass.LARGE,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            ColorTrait.BLACK_COLORATION,
        ),
        monitorLizard.descend(
            "komodo dragon",
            SizeClass.MEDIUM,
            CommonTrait.PARTHENOGENESIS,
            CommonTrait.FAT_RESERVES,
            CommonTrait.HISSING_WARNING,
        ),
        gecko.descend(
            "tokay gecko",
            SizeClass.TINY,
            CommonTrait.STRONG_JAWS,
            CommonTrait.TERRESTRIAL_CAMOUFLAGE,
            ColorTrait.PALE_COLORATION,
        ),
        gecko.descend(
            "common house gecko",
            SizeClass.TINY,
            ColorTrait.PALE_COLORATION,
        ),
        wormLizard.descend(
            "mexican mole lizard",
            SizeClass.TINY,
            CommonTrait.SCALES,
            CommonTrait.WATER_RETENTIVE_SCALES,
            CommonTrait.DIGGING_LIMBS.atLevel(2),
            CommonTrait.BURROWING_EGGS,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.SAND_ADAPTATION,
            CommonTrait.HEAT_STABLE_ENZYMES.atLevel(1),
        ),
        iguana.descend(
            "green iguana",
            SizeClass.SMALL,
            CommonTrait.FERMENTING_HINDGUT,
        ),
        chameleon.descend(
            "veiled chameleon",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
        ),
        python.descend(
            "reticulated python",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.HISSING_WARNING,
        ),
        boa.descend(
            "emerald tree boa",
            SizeClass.MEDIUM,
            CommonTrait.UNDULATING_CLIMBING,
            CommonTrait.TERRESTRIAL_CAMOUFLAGE,
            CommonTrait.SILENT_MOVEMENT,
            ColorTrait.GREEN_COLORATION,
        ),
        snake.descend(
            "common garter snake",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.VIVIPARITY,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_TORPOR,
            CommonTrait.TERRESTRIAL_CAMOUFLAGE,
        ),
        cobra.descend(
            "king cobra",
            SizeClass.MEDIUM,
        ),
        viper.descend(
            "western diamondback rattlesnake",
            SizeClass.SMALL,
            CommonTrait.BURROW_BORROWER,
            CommonTrait.HEAT_STABLE_ENZYMES.atLevel(2),
            CommonTrait.INFRARED_SENSING,
            CommonTrait.FOOD_DERIVED_WATER,
            CommonTrait.RATTLING_WARNING,
        ),
        seaTurtle.descend(
            "green sea turtle",
            SizeClass.LARGE,
            CommonTrait.CALM,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.GRAZING_MOUTHPARTS,
        ),
        tortoise.descend(
            "galapagos tortoise",
            SizeClass.MEDIUM,
            CommonTrait.FAT_RESERVES,
            CommonTrait.SLOW_GROWTH.atLevel(2),
            ColorTrait.BROWN_COLORATION,
        ),
        treeFrog.descend(
            "red-eyed tree frog",
            SizeClass.TINY,
            CommonTrait.STICKY_FEET,
            CommonTrait.SOLITARY
        ),
        frog.descend(
            "poison dart frog",
            SizeClass.TINY,
            CommonTrait.WALKING_LIMBS.atLevel(2),
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.TOXIC_SKIN,
            CommonTrait.APOSEMATIC_COLORATION,
            ColorTrait.RED_COLORATION,
            CommonTrait.SOLITARY
        ),
        bullfrog.descend(
            "american bullfrog",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.SEASONAL_TORPOR,
        ),
        toad.descend(
            "common toad",
            SizeClass.TINY,
            CommonTrait.SEASONAL_TORPOR,
        ),
        moleSalamander.descend(
            "axolotl",
            SizeClass.TINY,
            CommonTrait.AQUATIC_LIMBS.atLevel(1),
            ColorTrait.PALE_COLORATION,
            CommonTrait.SOLITARY
        ),
        moleSalamander.descend(
            "common mudpuppy",
            SizeClass.TINY,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(1),
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SOLITARY
        ),
        tuatara.descend(
            "tuatara",
            SizeClass.SMALL,
            CommonTrait.INFREQUENT_REPRODUCTION,
            ColorTrait.BROWN_COLORATION,
        ),
        lizard.descend(
            "desert horned lizard",
            SizeClass.TINY,
            CommonTrait.DIGGING_LIMBS.atLevel(1),
            CommonTrait.BURROW_BUILDER,
            CommonTrait.CONCENTRATED_URINE,
            CommonTrait.WATER_RETENTIVE_SCALES,
            CommonTrait.SAND_ADAPTATION,
            ColorTrait.YELLOW_COLORATION,
            minus = listOf(CommonTrait.CLIMBING_LIMBS, CommonTrait.SLENDER_PHYSIQUE)
        ),
        turtle.descend(
            "common snapping turtle",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_COLORATION,
        ),
        seaTurtle.descend(
            "leatherback sea turtle",
            SizeClass.LARGE,
            CommonTrait.CALM,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.STREAMLINED_PHYSIQUE,
            CommonTrait.COASTAL_BREEDING_SITE,
            CommonTrait.FAT_RESERVES,
            ColorTrait.COUNTERSHADE_COLORATION,
        ),
        iguana.descend(
            "marine iguana",
            SizeClass.SMALL,
            CommonTrait.AMPHIBIOUS_LIMBS.atLevel(2),
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.WATER_RETENTIVE_SCALES,
            ColorTrait.BLACK_COLORATION,
        ),
        giantSalamander.descend(
            "japanese giant salamander",
            SizeClass.MEDIUM,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.LIMBED_BODY,
            CommonTrait.TRACHEA,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SOLITARY
        ),
        newt.descend(
            "eastern newt",
            SizeClass.TINY,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.SEASONAL_TORPOR,
        ),
        caecilian.descend(
            "ringed caecilian",
            SizeClass.TINY,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SOLITARY
        ),
    )

    val FISH: List<SpeciesDefinition> = listOf(
        shark.descend(
            "great white shark",
            SizeClass.LARGE,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.FAT_RESERVES,
            CommonTrait.SLOW_GROWTH.atLevel(2),
            CommonTrait.INFREQUENT_REPRODUCTION,
            CommonTrait.SOLITARY,
        ),
        shark.descend(
            "whale shark",
            SizeClass.HUGE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.GILL_RAKERS,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.SLOW_GROWTH.atLevel(2),
            CommonTrait.SOLITARY,
        ),
        skate.descend(
            "common skate",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.COLD_ACTIVE_ENZYMES.atLevel(1),
            CommonTrait.AQUATIC_CAMOUFLAGE,
            CommonTrait.SLOW_GROWTH.atLevel(2),
            CommonTrait.INFREQUENT_REPRODUCTION,
        ),
        sturgeon.descend(
            "beluga sturgeon",
            SizeClass.LARGE,
            CommonTrait.CALM,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.SUCTION_FEEDING,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.INFREQUENT_REPRODUCTION,
        ),
        stingray.descend(
            "giant oceanic manta ray",
            SizeClass.LARGE,
            CommonTrait.CALM,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.GILL_RAKERS,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.SLOW_GROWTH.atLevel(1),
            CommonTrait.INFREQUENT_REPRODUCTION,
            CommonTrait.SOLITARY,
        ),
        tuna.descend(
            "atlantic bluefin tuna",
            SizeClass.MEDIUM,
            CommonTrait.AGGRESSIVE,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.LONG_MIGRATION,
            CommonTrait.SOLITARY,
        ),
        salmon.descend(
            "atlantic salmon",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.COLD_ACTIVE_ENZYMES.atLevel(2),
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.SOLITARY,
        ),
        pike.descend(
            "northern pike",
            SizeClass.MEDIUM,
            CommonTrait.ANTIFREEZE_PROTEINS,
            CommonTrait.AGGRESSIVE,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.STRONG_JAWS,
        ),
        teleostei.descend(
            "ocellaris clownfish",
            SizeClass.TINY,
            CommonTrait.CALM,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.GILL_RAKERS,
            CommonTrait.REEF_NESTING,
            ColorTrait.RED_COLORATION,
            CommonTrait.SOLITARY,
        ),
        seahorse.descend(
            "lined seahorse",
            SizeClass.TINY,
            CommonTrait.CALM,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.SOLITARY,
        ),
        gobie.descend(
            "round goby",
            SizeClass.TINY,
            CommonTrait.AGGRESSIVE,
            CommonTrait.EURYHALINE_OSMOREGULATION,
            CommonTrait.RESILIENT_DIGESTION,
            CommonTrait.FREQUENT_REPRODUCTION,
            CommonTrait.OVOSPORE_NEST,
        ),
        eel.descend(
            "electric eel",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.ELECTRORECEPTION,
            CommonTrait.ELECTRIC_ORGAN,
        ),
        percomorpha.descend(
            "deep-sea anglerfish",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.DEEP_WATER_ADAPTATION,
            CommonTrait.COLD_ACTIVE_ENZYMES.atLevel(2),
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BIOLUMINESCENCE,
            CommonTrait.BIOLUMINESCENT_LURE,
            ColorTrait.BLACK_COLORATION,
            CommonTrait.SOLITARY,
        ),
        pufferfish.descend(
            "porcupinefish",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.REEF_NESTING,
            ColorTrait.BROWN_COLORATION,
        ),
        swordfish.descend(
            "atlantic swordfish",
            SizeClass.LARGE,
            CommonTrait.AGGRESSIVE,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.SOLITARY,
        ),
        catfish.descend(
            "channel catfish",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.AMBUSH_MUSCULATURE,
        ),
        carp.descend(
            "common carp",
            SizeClass.MEDIUM,
            CommonTrait.ANTIFREEZE_PROTEINS,
            CommonTrait.CALM,
            CommonTrait.BUOYANCY_BLADDER,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SOLITARY,
            minus = listOf(CommonTrait.COLD_ACTIVE_ENZYMES),
        ),
        minnow.descend(
            "fathead minnow",
            SizeClass.TINY,
            CommonTrait.ANTIFREEZE_PROTEINS,
            CommonTrait.RESILIENT_DIGESTION,
            CommonTrait.FREQUENT_REPRODUCTION,
            CommonTrait.OVOSPORE_NEST,
            CommonTrait.GILL_RAKERS,
        ),
        piranha.descend(
            "red-bellied piranha",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.SCAVENGING_SENSES,
            CommonTrait.RESILIENT_DIGESTION,
        ),
        herring.descend(
            "peruvian anchoveta",
            SizeClass.TINY,
        ),
        perch.descend(
            "coral grouper",
            SizeClass.MEDIUM,
            CommonTrait.SALTWATER_OSMOREGULATION,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.REEF_CAMOUFLAGE,
            CommonTrait.REEF_SHELTER_DEPENDENCE,
            ColorTrait.RED_COLORATION,
            CommonTrait.SOLITARY,
            minus = listOf(CommonTrait.FRESHWATER_OSMOREGULATION, CommonTrait.COLD_ACTIVE_ENZYMES),
        ),
        wrasse.descend(
            "bumphead parrotfish",
            SizeClass.MEDIUM,
            CommonTrait.CALM,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.REEF_BORING,
            ColorTrait.GREEN_COLORATION,
            CommonTrait.SOLITARY,
        ),
        teleostei.descend(
            "arapaima",
            SizeClass.MEDIUM,
            CommonTrait.AGGRESSIVE,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SOLITARY,
        ),
        percomorpha.descend(
            "antarctic silverfish",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.ANTIFREEZE_PROTEINS,
            CommonTrait.COLD_ACTIVE_ENZYMES.atLevel(2),
            CommonTrait.SLOW_GROWTH.atLevel(1),
            CommonTrait.SOLITARY,
        ),
        perch.descend(
            "antarctic toothfish",
            SizeClass.MEDIUM,
            CommonTrait.AGGRESSIVE,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.ANTIFREEZE_PROTEINS,
            CommonTrait.SLOW_GROWTH.atLevel(1),
            CommonTrait.SOLITARY,
        ),
        eel.descend(
            "mediterranean moray",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.REEF_CAMOUFLAGE,
            CommonTrait.REEF_SHELTER_DEPENDENCE,
        ),
        flatfish.descend(
            "european plaice",
            SizeClass.SMALL,
            CommonTrait.CALM,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.AQUATIC_CAMOUFLAGE,
        ),
        herring.descend(
            "pacific herring",
            SizeClass.SMALL,
        ),
        gar.descend(
            "alligator gar",
            SizeClass.MEDIUM,
            CommonTrait.AGGRESSIVE,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.ARMORED_HIDE.atLevel(1),
            CommonTrait.SLOW_GROWTH.atLevel(2),
            ColorTrait.BROWN_COLORATION,
        ),
        flyingfish.descend(
            "atlantic flyingfish",
            SizeClass.SMALL,
            CommonTrait.SKITTISH,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.STREAMLINED_PHYSIQUE,
        ),
        teleostei.descend(
            "glacier lanternfish",
            SizeClass.TINY,
            CommonTrait.SKITTISH,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.BIOLUMINESCENCE,
            CommonTrait.BIOLUMINESCENT_LURE,
            CommonTrait.SCHOOLING,
            ColorTrait.BLACK_COLORATION,
        ),
        perch.descend(
            "reef stonefish",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.FLATTENED_PHYSIQUE,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.VENOM_DELIVERY.atLevel(2),
            CommonTrait.REEF_CAMOUFLAGE,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SOLITARY,
        ),
    )

    val INVERTEBRATES: List<SpeciesDefinition> = listOf(
        octopus.descend(
            "common octopus",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.INTELLIGENCE.atLevel(2),
        ),
        squid.descend(
            "giant squid",
            SizeClass.LARGE,
            CommonTrait.AGGRESSIVE,
            ColorTrait.RED_COLORATION,
        ),
        jellyfish.descend(
            "moon jellyfish",
            SizeClass.TINY,
            CommonTrait.CALM,
            CommonTrait.AMBUSH_MUSCULATURE,
        ),
        arthropod.descend(
            "crown-of-thorns starfish",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.BONY_SKELETON,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.PASSIVE_RESPIRATION,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.TOXIC_SKIN,
            CommonTrait.REEF_BORING,
            CommonTrait.APOSEMATIC_COLORATION,
            CommonTrait.LIMB_REGROWTH,
            CommonTrait.AUTOTOMY,
            ColorTrait.RED_COLORATION,
        ),
        crab.descend(
            "blue crab",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.LIMB_REGROWTH,
            CommonTrait.AUTOTOMY,
            ColorTrait.BLUE_COLORATION,
        ),
        lobster.descend(
            "american lobster",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.LIMB_REGROWTH,
            CommonTrait.AUTOTOMY,
        ),
        crayfish.descend(
            "red swamp crayfish",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.DIGGING_LIMBS.atLevel(1),
            CommonTrait.BURROW_BUILDER,
            CommonTrait.RESILIENT_DIGESTION,
            CommonTrait.FREQUENT_REPRODUCTION,
            CommonTrait.LIMB_REGROWTH,
            CommonTrait.AUTOTOMY,
        ),
        isopod.descend(
            "common woodlouse",
            SizeClass.TINY,
            CommonTrait.SKITTISH,
            CommonTrait.TERRESTRIAL_OVOSPORE,
            CommonTrait.SLOW_GROWTH.atLevel(1),
        ),
        shrimp.descend(
            "cleaner shrimp",
            SizeClass.TINY,
            CommonTrait.CALM,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.COASTAL_CLINGING_FEET,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.FOOD_CLEANING_BEHAVIOR,
            CommonTrait.REEF_NESTING,
            CommonTrait.LIMB_REGROWTH,
            CommonTrait.AUTOTOMY,
            ColorTrait.RED_COLORATION,
        ),
        stonyCoral.descend(
            "staghorn coral",
            SizeClass.MEDIUM,
        ),
        clam.descend(
            "eastern oyster",
            SizeClass.TINY,
            CommonTrait.WARM_WATER_ENZYMES
        ),
        clam.descend(
            "blue mussel",
            SizeClass.TINY,
        ),
        landSnail.descend(
            "garden snail",
            SizeClass.TINY,
        ),
        seaSnail.descend(
            "common periwinkle",
            SizeClass.TINY,
            CommonTrait.AQUATIC_CAMOUFLAGE,
            CommonTrait.SLOW_GROWTH.atLevel(2),
        ),
        landSlug.descend(
            "banana slug",
            SizeClass.TINY,
        ),
        seaSlug.descend(
            "blue dragon sea slug",
            SizeClass.TINY,
            CommonTrait.TOXIC_SKIN,
            CommonTrait.AQUATIC_CAMOUFLAGE,
            ColorTrait.BLUE_COLORATION,
        ),
        earthworm.descend(
            "common earthworm",
            SizeClass.TINY,
            ColorTrait.BROWN_COLORATION,
        ),
        bristleWorm.descend(
            "fireworm",
            SizeClass.TINY,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.SPINES,
            CommonTrait.TOXIC_SKIN,
            CommonTrait.APOSEMATIC_COLORATION,
            ColorTrait.RED_COLORATION,
        ),
        leech.descend(
            "medicinal leech",
            SizeClass.TINY,
            CommonTrait.PARASITIC_PROBOSCIS,
            CommonTrait.SLIMY_SKIN,
            CommonTrait.NOCTURNAL,
        ),
        butterfly.descend(
            "monarch butterfly",
            SizeClass.TINY,
            CommonTrait.SKITTISH,
            CommonTrait.TOXIC_SKIN,
            ColorTrait.RED_COLORATION,
        ),
        moth.descend(
            "atlas moth",
            SizeClass.TINY,
            CommonTrait.SKITTISH,
            CommonTrait.RAPID_GROWTH.atLevel(2),
            CommonTrait.TERRESTRIAL_CAMOUFLAGE,
        ),
        bee.descend(
            "western honey bee",
            SizeClass.TINY,
            CommonTrait.AGGRESSIVE,
            ColorTrait.BROWN_COLORATION
        ),
        ant.descend(
            "leafcutter ant",
            SizeClass.MINUSCULE,
        ),
        termite.descend(
            "termite",
            SizeClass.MINUSCULE,
            CommonTrait.FOSSORIAL_LIVING,
        ),
        roach.descend(
            "german cockroach",
            SizeClass.TINY,
            CommonTrait.NOCTURNAL,
            CommonTrait.GROUP_LIVING,
            CommonTrait.FREQUENT_REPRODUCTION,
        ),
        locust.descend(
            "desert locust",
            SizeClass.TINY,
            CommonTrait.HEAT_STABLE_ENZYMES.atLevel(2),
            CommonTrait.BURROW_BORROWER,
            CommonTrait.SAND_ADAPTATION,
        ),
        mayfly.descend(
            "common mayfly",
            SizeClass.TINY,
            CommonTrait.COLD_ACTIVE_ENZYMES.atLevel(1),
        ),
        beetle.descend(
            "seven-spot ladybird",
            SizeClass.TINY,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.WINGS.atLevel(1),
            CommonTrait.AMBUSH_MUSCULATURE,
            ColorTrait.RED_COLORATION,
        ),
        spider.descend(
            "orb-weaver spider",
            SizeClass.TINY,
            CommonTrait.CLIMBING_LIMBS,
        ),
        scorpion.descend(
            "emperor scorpion",
            SizeClass.SMALL,
            CommonTrait.HEAT_STABLE_ENZYMES.atLevel(2),
            CommonTrait.SAND_ADAPTATION,
            ColorTrait.BLACK_COLORATION,
        ),
        tick.descend(
            "deer tick",
            SizeClass.MINUSCULE,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_TORPOR,
            CommonTrait.FREQUENT_REPRODUCTION,
            CommonTrait.TERRESTRIAL_CAMOUFLAGE,
        ),
        dragonfly.descend(
            "common green darner dragonfly",
            SizeClass.TINY,
            CommonTrait.AGGRESSIVE,
            CommonTrait.MOTION_TRACKING_SENSES,
            CommonTrait.REGIONAL_MIGRATION,
            ColorTrait.GREEN_COLORATION,
        ),
        fly.descend(
            "common mosquito",
            SizeClass.TINY,
            CommonTrait.SUCKING_PROBOSCIS,
            CommonTrait.BURROWING_EGGS,
        ),
        centipede.descend(
            "giant centipede",
            SizeClass.SMALL,
            CommonTrait.AGGRESSIVE,
            CommonTrait.AMBUSH_MUSCULATURE,
        ),
        millipede.descend(
            "giant African millipede",
            SizeClass.TINY,
            CommonTrait.CALM,
            CommonTrait.SLOW_METABOLISM.atLevel(1),
            CommonTrait.SLOW_GROWTH.atLevel(1),
            CommonTrait.STINK_DEFENSE,
        ),
        stonyCoral.descend(
            "brain coral",
            SizeClass.MEDIUM,
            CommonTrait.SLOW_GROWTH.atLevel(2)
        ),
        seaFan.descend(
            "common sea fan",
            SizeClass.SMALL,
        ),
        seaAnemone.descend(
            "giant green anemone",
            SizeClass.SMALL,
            ColorTrait.GREEN_COLORATION,
            CommonTrait.INTERNAL_PHOTOSYMBIONTS
        ),
        anthozoan.descend(
            "slender sea pen",
            SizeClass.SMALL,
            CommonTrait.SUSPENSION_FEEDING_TENTACLES,
            CommonTrait.SLOW_GROWTH.atLevel(2),
        ),
        jellyfish.descend(
            "sea wasp",
            SizeClass.TINY,
            CommonTrait.AGGRESSIVE,
            CommonTrait.WARM_WATER_ENZYMES,
            CommonTrait.AMBUSH_MUSCULATURE,
        ),
        mantis.descend(
            "european mantis",
            SizeClass.TINY,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.TERRESTRIAL_CAMOUFLAGE,
        ),
        beetle.descend(
            "scarab dung beetle",
            SizeClass.TINY,
            CommonTrait.CALM,
            CommonTrait.WINGS.atLevel(1),
        ),
        arthropod.descend(
            "pea aphid",
            SizeClass.MINUSCULE,
            CommonTrait.TRACHEA,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.SUCKING_PROBOSCIS,
            CommonTrait.VIVIPARITY,
            ColorTrait.GREEN_COLORATION,
        ),
        arthropod.descend(
            "spotted antlion",
            SizeClass.TINY,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.TRACHEA,
            CommonTrait.DIGGING_LIMBS.atLevel(2),
            CommonTrait.FOSSORIAL_LIVING,
            CommonTrait.BURROW_BUILDER,
            CommonTrait.AMBUSH_MUSCULATURE,
        ),
        arthropod.descend(
            "common walkingstick",
            SizeClass.TINY,
            CommonTrait.SKITTISH,
            CommonTrait.TRACHEA,
            CommonTrait.CLIMBING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
            CommonTrait.TERRESTRIAL_CAMOUFLAGE,
            CommonTrait.LIMB_REGROWTH,
            ColorTrait.BROWN_COLORATION,
        ),
        cicada.descend(
            "periodical cicada",
            SizeClass.TINY,
            CommonTrait.SOLITARY
        ),
    )

    val PRODUCERS_AND_FUNGI: List<SpeciesDefinition> = listOf(
        conifer.descend(
            "coast redwood",
            SizeClass.COLOSSAL,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        broadLeafTree.descend(
            "english oak",
            SizeClass.LARGE,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.SEASONAL_LEAF_DORMANCY,
            CommonTrait.FROST_HARDENED_TISSUES,
        ),
        conifer.descend(
            "scots pine",
            SizeClass.LARGE,
        ),
        broadLeafTree.descend(
            "african baobab",
            SizeClass.HUGE,
            CommonTrait.FRUIT_BEARING,
            CommonTrait.NECTARIES,
            CommonTrait.SUCCULENT_STEM,
            CommonTrait.DROUGHT_DECIDUOUS_LEAVES,
            CommonTrait.WATER_STORAGE_TISSUE
        ),
        broadLeafTree.descend(
            "umbrella thorn acacia",
            SizeClass.LARGE,
            CommonTrait.FRUIT_BEARING,
            CommonTrait.NECTARIES,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.DROUGHT_DECIDUOUS_LEAVES
        ),
        broadLeafTree.descend(
            "red mangrove",
            SizeClass.LARGE,
            CommonTrait.NECTARIES,
            CommonTrait.SALT_EXCLUDING_ROOTS,
            CommonTrait.WAXY_CUTICLE
        ),
        succulent.descend(
            "saguaro cactus",
            SizeClass.LARGE,
            CommonTrait.NECTARIES,
            CommonTrait.WOODY_SUPPORT_TISSUE,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES,
            CommonTrait.SLOW_GROWTH.atLevel(2)
        ),
        forb.descend(
            "common sunflower",
            SizeClass.MEDIUM,
            CommonTrait.DEEP_ROOT_SYSTEM,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        grass.descend(
            "perennial ryegrass",
            SizeClass.MEDIUM,
            CommonTrait.ROOTED_BODY,
            CommonTrait.SEASONAL_LEAF_DORMANCY
        ),
        grass.descend(
            "common wheat",
            SizeClass.MEDIUM,
            CommonTrait.ROOTED_BODY,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        broadLeafTree.descend(
            "eucalyptus tree",
            SizeClass.LARGE,
            CommonTrait.NECTARIES,
        ),
        grass.descend(
            "giant bamboo",
            SizeClass.LARGE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CANOPY_GROWTH,
            CommonTrait.WOODY_SUPPORT_TISSUE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.UNDERGROUND_STORAGE_ORGANS,
            CommonTrait.RAPID_GROWTH.atLevel(2),
        ),
        fruitTree.descend(
            "strangler fig",
            SizeClass.LARGE,
            CommonTrait.LARGE_EVERGREEN_LEAVES.atLevel(1),
            CommonTrait.SHADE_FRONDS,
        ),
        fern.descend(
            "bracken fern",
            SizeClass.SMALL,
            CommonTrait.SEASONAL_LEAF_DORMANCY
        ),
        moss.descend(
            "sphagnum moss",
            SizeClass.TINY,
            CommonTrait.SHADE_FRONDS
        ),
        aquaticAngiosperm.descend(
            "white water lily",
            SizeClass.SMALL,
        ),
        aquaticAngiosperm.descend(
            "eelgrass",
            SizeClass.SMALL,
            CommonTrait.SALT_EXCLUDING_ROOTS,
        ),
        algae.descend(
            "giant kelp",
            SizeClass.MEDIUM,
            CommonTrait.SALTWATER_OSMOREGULATION,
            ColorTrait.BROWN_COLORATION,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.FLOATING_FRONDS,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
        ),
        mushroom.descend(
            "field mushroom",
            SizeClass.SMALL,
            CommonTrait.ROOTED_BODY,
        ),
        mold.descend(
            "bread mold",
            SizeClass.TINY,
            CommonTrait.ROOTED_BODY,
        ),
        fungus.descend(
            "reindeer lichen",
            SizeClass.TINY,
            // Its thallus is pale, but its photobiont still captures light with
            // chlorophyll rather than a pale photosynthetic pigment.
            ColorTrait.GREEN_COLORATION,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.SURFACE_HOLDFAST,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.WHOLE_BODY_ANHYDROBIOSIS,
            CommonTrait.SLOW_GROWTH.atLevel(2)
        ),
        plant.descend(
            "venus flytrap",
            SizeClass.SMALL,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.ROOTED_BODY,
            CommonTrait.AMBUSH_MUSCULATURE,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            CommonTrait.WATER_STORAGE_TISSUE
        ),
        conifer.descend(
            "siberian larch",
            SizeClass.LARGE,
        ),
        conifer.descend(
            "himalayan juniper",
            SizeClass.MEDIUM,
            CommonTrait.WAXY_CUTICLE,
        ),
        conifer.descend(
            "lodgepole pine",
            SizeClass.LARGE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        grass.descend(
            "ichu grass",
            SizeClass.TINY,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FROST_HARDENED_TISSUES,
            CommonTrait.SEASONAL_LEAF_DORMANCY,
            CommonTrait.WAXY_CUTICLE
        ),
        conifer.descend(
            "saharan cypress",
            SizeClass.LARGE,
            CommonTrait.WAXY_CUTICLE,
        ),
        conifer.descend(
            "black spruce",
            SizeClass.LARGE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE
        ),
        aquaticAngiosperm.descend(
            "common duckweed",
            SizeClass.TINY,
            CommonTrait.RAPID_GROWTH.atLevel(2)
        ),
        grass.descend(
            "common reed",
            SizeClass.MEDIUM,
            CommonTrait.SUBSTRATE_HOLDFAST,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.UNDERGROUND_STORAGE_ORGANS,
        ),
        vine.descend(
            "kudzu vine",
            SizeClass.MEDIUM,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.UNDERGROUND_STORAGE_ORGANS,
            CommonTrait.RAPID_GROWTH.atLevel(1)
        ),
        forb.descend(
            "epiphytic orchid",
            SizeClass.SMALL,
            CommonTrait.EPIPHYTIC_ROOTS,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL
        ),
        moss.descend(
            "moss campion",
            SizeClass.TINY,
            CommonTrait.ROOTED_BODY,
            CommonTrait.CUSHION_GROWTH,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.SLOW_GROWTH.atLevel(2)
        ),
        succulent.descend(
            "prickly pear cactus",
            SizeClass.MEDIUM,
            CommonTrait.NECTARIES,
            CommonTrait.DESICCATION_RESISTANT_PROPAGULES
        ),
        aquaticAngiosperm.descend(
            "common bladderwort",
            SizeClass.TINY,
            CommonTrait.MEAT_EATING_MOUTHPARTS,
            CommonTrait.AMBUSH_MUSCULATURE,
        ),
        vascularPlant.descend(
            "field horsetail",
            SizeClass.SMALL,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.WAXY_CUTICLE,
            CommonTrait.PERENNIAL_STORAGE_TISSUE,
            CommonTrait.UNDERGROUND_STORAGE_ORGANS,
            CommonTrait.AERIAL_OVOSPORE_DISPERSAL
        ),
        fruitingFungus.descend(
            "honey fungus",
            SizeClass.SMALL,
            CommonTrait.ROOTED_BODY,
        ),
        shelfFungus.descend(
            "shelf fungus",
            SizeClass.SMALL,
            CommonTrait.ROOTED_BODY,
        ),
    )

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
        val anatomy = buildList {
            if (CommonTrait.HOOKED_TALONS in adaptations && CommonTrait.LIMBED_BODY !in adaptations) {
                add(CommonTrait.LIMBED_BODY)
            }
            if (
                adaptations.any { it == CommonTrait.TEETH || it == CommonTrait.LONG_TUSKS.atLevel(2) || it == CommonTrait.STRONG_JAWS } &&
                CommonTrait.JAW !in adaptations
            ) {
                add(CommonTrait.JAW)
            }
            if (CommonTrait.LONG_TUSKS.atLevel(2) in adaptations && CommonTrait.TEETH !in adaptations) {
                add(CommonTrait.TEETH)
            }
            if (
                adaptations.any { it == CommonTrait.RETRACTABLE_CLAWS || it == CommonTrait.HOOKED_TALONS } &&
                CommonTrait.CLAWS !in adaptations
            ) {
                add(CommonTrait.CLAWS)
            }
        }
        val structuralFoundation =
            if (
                (CommonTrait.LIMBED_BODY in adaptations || CommonTrait.LIMBED_BODY in anatomy) &&
                adaptations.none { it.group == TraitGroup.SKELETON }
            ) {
                listOf(CommonTrait.BONY_SKELETON)
            } else {
                emptyList()
            }
        val reproduction =
            if (adaptations.any {
                    TraitCapability.REPRODUCTION in it.baseTrait.capabilitiesAt(it.authoredLevel)
                }
            ) {
                emptyList()
            } else {
                listOf(defaultOvosporeTrait(adaptations))
            }
        val salinity = defaultSalinityTrait(adaptations)
        return SpeciesDefinition(
            id = idFromName(name),
            displayName = name,
            sizeClass = sizeClass,
            traits = listOf(biochemistry, thermalStrategy) + structuralFoundation + reproduction + salinity + anatomy + adaptations,
        )
    }

    fun sessile(
        name: String,
        sizeClass: SizeClass,
        vararg adaptations: SpeciesTrait,
    ): SpeciesDefinition {
        val reproduction =
            if (adaptations.any {
                    TraitCapability.REPRODUCTION in it.baseTrait.capabilitiesAt(it.authoredLevel)
                }
            ) {
                emptyList()
            } else {
                listOf(defaultOvosporeTrait(adaptations))
            }
        val salinity = defaultSalinityTrait(adaptations)
        return SpeciesDefinition(
            id = idFromName(name),
            displayName = name,
            sizeClass = sizeClass,
            traits = listOf(CommonTrait.TEMPERATE_BIOCHEMISTRY) + reproduction + salinity + adaptations,
        )
    }

    private fun defaultSalinityTrait(adaptations: Array<out SpeciesTrait>): List<CommonTrait> {
        if (adaptations.any { it.group == TraitGroup.SALINITY_STRATEGY }) return emptyList()

        val capabilities = adaptations
            .flatMapTo(mutableSetOf()) { it.baseTrait.capabilitiesAt(it.authoredLevel) }
        val aquatic =
            TraitCapability.UNDERWATER_RESPIRATION in capabilities ||
                TraitCapability.AQUATIC_LOCOMOTION in capabilities
        return if (aquatic) listOf(CommonTrait.SALTWATER_OSMOREGULATION) else emptyList()
    }

    private fun defaultOvosporeTrait(adaptations: Array<out SpeciesTrait>): CommonTrait {
        val supportedHabitats = supportedHabitats(adaptations)
        val aquatic = supportedHabitats.any {
            it == Habitat.COASTAL ||
                it == Habitat.FRESHWATER ||
                it == Habitat.SHALLOW_OCEAN ||
                it == Habitat.OPEN_OCEAN ||
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

    private fun supportedHabitats(adaptations: Array<out SpeciesTrait>): Set<Habitat> {
        val capabilities = adaptations
            .flatMapTo(mutableSetOf()) { it.baseTrait.capabilitiesAt(it.authoredLevel) }
        return Habitat.entries.filterTo(mutableSetOf()) { it.accessCapability in capabilities }
    }

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
