package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.atLevel

val reptile = tetrapoda.descend(
    "reptile",
    SizeClass.SMALL,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.BEHAVIORAL_THERMOREGULATION,
    CommonTrait.DIURNAL,
    ColorTrait.GREEN_COLORATION,
    CommonTrait.CLAWS
)
val crocodile = reptile.descend(
    "crocodile",
    SizeClass.LARGE,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.AMPHIBIOUS_LIMBS.atLevel(2),
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.STRONG_JAWS,
    CommonTrait.OVOSPORE_NEST,
    CommonTrait.ARMORED_HIDE.atLevel(2),
    CommonTrait.SCENT.atLevel(3),
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.BELLOWING_CALL,
    CommonTrait.AGGRESSIVE,
    CommonTrait.NOCTURNAL
)
val tuatara = reptile.descend(
    "rhynchocephalian",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.SLOW_GROWTH.atLevel(2),
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.AUTOTOMY,
    CommonTrait.LIMB_REGROWTH,
    CommonTrait.SLOW_METABOLISM.atLevel(1),
    CommonTrait.HEARING.atLevel(1),
    CommonTrait.BURROW_BUILDER,
    CommonTrait.CALM,
    CommonTrait.DIGGING_LIMBS.atLevel(2)
)

// lizards
val lizard = reptile.descend(
    "lizard",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.SLENDER_PHYSIQUE,
)
val gecko = lizard.descend(
    "gecko",
    SizeClass.TINY,
    CommonTrait.STICKY_FEET,
    CommonTrait.CHIRPING_CALL,
    CommonTrait.NOCTURNAL,
    CommonTrait.AUTOTOMY,
    CommonTrait.SKITTISH,
    minus = listOf(CommonTrait.CLAWS)
)
val monitorLizard = lizard.descend(
    "monitor lizard",
    SizeClass.MEDIUM,
    CommonTrait.ARMORED_HIDE.atLevel(2),
    CommonTrait.VENOM_DELIVERY.atLevel(2),
    CommonTrait.SCENT.atLevel(3),
    CommonTrait.INTELLIGENCE.atLevel(1),
    CommonTrait.CALM,
    ColorTrait.BROWN_COLORATION
)
val wormLizard = lizard.descend(
    "worm lizard",
    SizeClass.TINY,
    CommonTrait.FOSSORIAL_LIVING,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.BODY_UNDULATION,
    CommonTrait.EYES.atLevel(1),
    ColorTrait.PALE_COLORATION,
    minus = listOf(CommonTrait.CLIMBING_LIMBS)
)
val chameleon = lizard.descend(
    "chameleon",
    SizeClass.SMALL,
    CommonTrait.PROJECTILE_TONGUE,
    CommonTrait.TERRESTRIAL_CAMOUFLAGE,
    CommonTrait.SLOW_METABOLISM.atLevel(2),
    CommonTrait.EYES.atLevel(3),
    ColorTrait.ADAPTIVE_COLORATION,
    minus = listOf(CommonTrait.AMBUSH_MUSCULATURE, CommonTrait.SLENDER_PHYSIQUE)
)
val iguana = lizard.descend(
    "iguana",
    SizeClass.SMALL,
    CommonTrait.FRUIT_EATING_MOUTHPARTS,
    CommonTrait.BROWSING_MOUTHPARTS,
    CommonTrait.AUTOTOMY,
    minus = listOf(CommonTrait.AMBUSH_MUSCULATURE, CommonTrait.SLENDER_PHYSIQUE, CommonTrait.MEAT_EATING_MOUTHPARTS)
)

// snakes
val serpent = reptile.descend(
    "serpent",
    SizeClass.SMALL,
    CommonTrait.MEAT_EATING_MOUTHPARTS,
    CommonTrait.SCENT.atLevel(3),
    CommonTrait.BODY_UNDULATION,
    CommonTrait.SLENDER_PHYSIQUE,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.NOCTURNAL,
    ColorTrait.BROWN_COLORATION
)
val snake = serpent.descend(
    "snake",
    SizeClass.SMALL,
    CommonTrait.FANGS.atLevel(1),
)
val python = serpent.descend(
    "python",
    SizeClass.MEDIUM,
    CommonTrait.CONSTRICTING_BODY,
    CommonTrait.INFRARED_SENSING,
    ColorTrait.BROWN_COLORATION
)
val boa = serpent.descend(
    "boa",
    SizeClass.MEDIUM,
    CommonTrait.CONSTRICTING_BODY,
    CommonTrait.INFRARED_SENSING,
    CommonTrait.VIVIPARITY,
    CommonTrait.CALM,
    ColorTrait.BROWN_COLORATION
)
val viper = serpent.descend(
    "viper",
    SizeClass.SMALL,
    CommonTrait.VENOM_DELIVERY.atLevel(2),
    CommonTrait.FANGS.atLevel(2),
)
val cobra = serpent.descend(
    "cobra",
    SizeClass.SMALL,
    CommonTrait.VENOM_DELIVERY.atLevel(2),
    CommonTrait.FANGS.atLevel(2),
    CommonTrait.HISSING_WARNING,
    CommonTrait.VESPERTINE,
    CommonTrait.AGGRESSIVE
)

val testudines = reptile.descend(
    "testudines",
    SizeClass.SMALL,
    CommonTrait.PROTECTIVE_SHELL,
    CommonTrait.SLOW_GROWTH.atLevel(1),
    CommonTrait.HEARING.atLevel(1)
)
val turtle = testudines.descend(
    "turtle",
    SizeClass.SMALL,
    CommonTrait.AMPHIBIOUS_LIMBS.atLevel(2),
    CommonTrait.FRESHWATER_OSMOREGULATION,
)
val seaTurtle = testudines.descend(
    "sea turtle",
    SizeClass.MEDIUM,
    CommonTrait.AQUATIC_LIMBS.atLevel(2),
    CommonTrait.PROLONGED_BREATH_HOLDING,
    CommonTrait.SALTWATER_OSMOREGULATION,
    CommonTrait.LONG_MIGRATION,
    minus = listOf(CommonTrait.WALKING_LIMBS.atLevel(2))
)
val tortoise = testudines.descend(
    "tortoise",
    SizeClass.MEDIUM,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.SLOW_METABOLISM.atLevel(2),
    CommonTrait.SLOW_GROWTH.atLevel(2),
    CommonTrait.INTELLIGENCE.atLevel(1),
    CommonTrait.CALM
)
