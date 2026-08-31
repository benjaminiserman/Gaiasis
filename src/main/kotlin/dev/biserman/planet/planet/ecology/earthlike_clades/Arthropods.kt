package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.atLevel

val arthropod = EarthSpeciesCatalog.animal(
    "arthropod",
    SizeClass.TINY,
    CommonTrait.ECTOTHERMY,
    CommonTrait.CRAWLING_APPENDAGES,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.CATHEMERAL,
    CommonTrait.SOLITARY,
    CommonTrait.MOLTING_EXOSKELETON,
    CommonTrait.LIMBED_BODY,
    CommonTrait.VASCULAR_SYSTEM,
    CommonTrait.EYES.atLevel(3),
    CommonTrait.SCENT.atLevel(3),
    CommonTrait.GILLS,
    CommonTrait.HEARING.atLevel(1),
    ColorTrait.PALE_COLORATION,
)

// arachnids
val arachnid = arthropod.descend(
    "arachnid",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.NOCTURNAL,
    CommonTrait.TRACHEA,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.WAXY_CUTICLE,
    ColorTrait.BROWN_COLORATION,
    minus = listOf(CommonTrait.GILLS)
)
val spider = arachnid.descend(
    "spider",
    SizeClass.TINY,
    CommonTrait.VENOM_DELIVERY.atLevel(2),
    CommonTrait.FANGS,
    CommonTrait.WEB_SILK
)
val scorpion = arachnid.descend(
    "scorpion",
    SizeClass.TINY,
    CommonTrait.VENOM_DELIVERY.atLevel(2),
    CommonTrait.PINCERS,
    CommonTrait.STINGER,
)
val tick = arachnid.descend(
    "tick",
    SizeClass.MINUSCULE,
    CommonTrait.PARASITIC_PROBOSCIS,
    minus = listOf(CommonTrait.AMBUSH_MUSCULATURE, CommonTrait.MEAT_EATING_MOUTHPARTS)
)

// myriapoda
val myriapoda = arthropod.descend(
    "myriapoda",
    SizeClass.TINY,
    CommonTrait.NOCTURNAL,
    CommonTrait.TRACHEA,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.SEGMENTED_PHYSIQUE,
    ColorTrait.BROWN_COLORATION,
    minus = listOf(CommonTrait.GILLS)
)
val centipede = myriapoda.descend(
    "centipede",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.FANGS,
    CommonTrait.VENOM_DELIVERY.atLevel(2)
)
val millipede = myriapoda.descend(
    "millipede",
    SizeClass.TINY,
    CommonTrait.DECOMPOSING_ENZYMES,
    CommonTrait.FOSSORIAL_LIVING,
    CommonTrait.ARMORED_HIDE.atLevel(2),
    CommonTrait.EYES.atLevel(1),
)

// crustaceans
val crustacean = arthropod.descend(
    "crustacean",
    SizeClass.TINY,
    CommonTrait.BODY_CARRIED_OVOSPORES,
    CommonTrait.WAXY_CUTICLE,
    CommonTrait.NOCTURNAL,
)
val isopod = crustacean.descend(
    "isopod",
    SizeClass.TINY,
    CommonTrait.DECOMPOSING_ENZYMES,
    CommonTrait.ARMORED_HIDE.atLevel(2),
    CommonTrait.BALL_ROLLING
)
val shrimp = crustacean.descend(
    "shrimp",
    SizeClass.TINY,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.EURYHALINE_OSMOREGULATION,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    minus = listOf(CommonTrait.CRAWLING_APPENDAGES)
)

val reptantia = crustacean.descend(
    "reptantia",
    SizeClass.SMALL,
    CommonTrait.PINCERS,
    CommonTrait.CRUSHING_PINCERS,
    CommonTrait.ARMORED_HIDE.atLevel(2),
    CommonTrait.COASTAL_CLINGING_FEET,
    CommonTrait.BROWSING_MOUTHPARTS,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    ColorTrait.RED_COLORATION
)

val astacidea = reptantia.descend(
    "astacidea",
    SizeClass.SMALL,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.TAIL
)
val lobster = astacidea.descend(
    "lobster",
    SizeClass.SMALL,
    CommonTrait.SLOW_GROWTH,
)
val crayfish = astacidea.descend(
    "crayfish",
    SizeClass.SMALL,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    ColorTrait.BROWN_COLORATION
)

val crab = reptantia.descend(
    "crab",
    SizeClass.SMALL,
)

// insects
val insect = arthropod.descend(
    "insect",
    SizeClass.TINY,
    CommonTrait.WINGS,
    CommonTrait.WAXY_CUTICLE,
    CommonTrait.TRACHEA,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.OVIPOSITOR,
    ColorTrait.BROWN_COLORATION,
    minus = listOf(CommonTrait.GILLS)
)
val dragonfly = insect.descend(
    "dragonfly",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.SWIFT_LIMBS,
    CommonTrait.EYES.atLevel(5),
    CommonTrait.DIURNAL,
    ColorTrait.RAINBOW_COLORATION
)
val mayfly = insect.descend(
    "mayfly",
    SizeClass.TINY,
    CommonTrait.AQUATIC_OVOSPORE,
    CommonTrait.GILLS,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.GRAZING_MOUTHPARTS
)
val mantis = insect.descend(
    "mantis",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.EYES.atLevel(5),
    CommonTrait.MOTION_TRACKING_SENSES,
    CommonTrait.DIURNAL,
    ColorTrait.GREEN_COLORATION,
)
val roach = insect.descend(
    "roach",
    SizeClass.TINY,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.BODY_CARRIED_OVOSPORES,
    CommonTrait.SCAVENGING_SENSES,
    CommonTrait.LIMB_REGROWTH,
    CommonTrait.WEAK_WINGS
)
val termite = insect.descend(
    "termite",
    SizeClass.TINY,
    CommonTrait.DECOMPOSING_ENZYMES,
    CommonTrait.EUSOCIAL_COLONY,
    CommonTrait.DIGGING_LIMBS,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.OVOSPORE_NEST,
    ColorTrait.PALE_COLORATION,
)
val locust = insect.descend(
    "locust",
    SizeClass.TINY,
    CommonTrait.REGIONAL_MIGRATION,
    CommonTrait.BURROWING_EGGS,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.LEAPING_LEGS
)
val cicada = insect.descend(
    "cicada",
    SizeClass.TINY,
    CommonTrait.BURROWING_EGGS,
    CommonTrait.SUCKING_PROBOSCIS,
    CommonTrait.SCREECHING_CALL,
    CommonTrait.PROLONGED_JUVENILE_DORMANCY,
    CommonTrait.TERRESTRIAL_CAMOUFLAGE,
    CommonTrait.COLLECTIVE_LIVING
)
val bee = insect.descend(
    "bee",
    SizeClass.TINY,
    CommonTrait.NECTAR_SIPPING_TONGUE,
    CommonTrait.POLLEN_CARRYING_SURFACES,
    CommonTrait.EUSOCIAL_COLONY,
    CommonTrait.COLONY_THERMOREGULATION,
    CommonTrait.OVOSPORE_NEST,
    CommonTrait.VENOM_DELIVERY.atLevel(2),
    CommonTrait.STINGER,
    CommonTrait.HONEY_STORES,
    CommonTrait.APOSEMATIC_COLORATION,
    CommonTrait.BUZZING_CALL,
    CommonTrait.COMPLEX_VOCALIZATIONS,
    CommonTrait.DIURNAL,
    ColorTrait.YELLOW_COLORATION,
)
val ant = insect.descend(
    "ant",
    SizeClass.MINUSCULE,
    CommonTrait.EUSOCIAL_COLONY,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.OVOSPORE_NEST,
)
val beetle = insect.descend(
    "beetle",
    SizeClass.TINY,
    CommonTrait.WASTE_FEEDING_MOUTHPARTS,
    CommonTrait.ARMORED_HIDE.atLevel(2),
    CommonTrait.DIGGING_LIMBS,
    ColorTrait.BLACK_COLORATION,
    CommonTrait.WEAK_WINGS
)
val butterfly = insect.descend(
    "butterfly",
    SizeClass.TINY,
    CommonTrait.NECTAR_SIPPING_TONGUE,
    CommonTrait.POLLEN_CARRYING_SURFACES,
    CommonTrait.LONG_MIGRATION,
    CommonTrait.APOSEMATIC_COLORATION,
    CommonTrait.DIURNAL,
    ColorTrait.RAINBOW_COLORATION
)
val moth = insect.descend(
    "moth",
    SizeClass.TINY,
    CommonTrait.NECTAR_SIPPING_TONGUE,
    CommonTrait.POLLEN_CARRYING_SURFACES,
    CommonTrait.LONG_MIGRATION,
    CommonTrait.APOSEMATIC_COLORATION,
    CommonTrait.NOCTURNAL,
    ColorTrait.BROWN_COLORATION
)
val fly = insect.descend(
    "fly",
    SizeClass.MINUSCULE,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.WASTE_FEEDING_MOUTHPARTS,
    CommonTrait.FRUIT_EATING_MOUTHPARTS,
    CommonTrait.SCAVENGING_SENSES,
    CommonTrait.BUZZING_CALL,
)
