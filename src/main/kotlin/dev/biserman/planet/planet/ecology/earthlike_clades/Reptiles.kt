package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass

val reptile = EarthSpeciesCatalog.animal(
    "reptile",
    SizeClass.SMALL,
    CommonTrait.ECTOTHERMY,
    CommonTrait.WALKING_LIMBS,
    CommonTrait.TERRESTRIAL_OVOSPORE,
    CommonTrait.BEHAVIORAL_THERMOREGULATION,
    CommonTrait.DIURNAL,
    CommonTrait.SOLITARY,
    ColorTrait.GREEN_CAMOUFLAGE
)
val crocodile = reptile.extend(
    "crocodile",
    SizeClass.LARGE,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.STRONG_JAWS,
    CommonTrait.OVOSPORE_NEST,
    CommonTrait.ARMORED_HIDE,
    CommonTrait.KEEN_SCENT_SENSE,
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.BELLOWING_CALL,
    CommonTrait.NOCTURNAL
)
val tuatara = reptile.extend(
    "tuatara",
    SizeClass.SMALL,
    CommonTrait.SLOW_GROWTH,
    CommonTrait.SEASONAL_TORPOR,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.AUTOTOMY,
    CommonTrait.LIMB_REGROWTH,
    CommonTrait.SLOW_METABOLISM,
    CommonTrait.POOR_HEARING,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.DIGGING_LIMBS
)

// lizards
val lizard = reptile.descend(
    "lizard",
    SizeClass.SMALL,
    CommonTrait.CLIMBING_LIMBS,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.SLENDER_BODY,
    CommonTrait.CLAWS
)
val gecko = lizard.descend(
    "gecko",
    SizeClass.TINY,
    CommonTrait.STICKY_FEET,
    CommonTrait.CHIRPING_CALL,
    CommonTrait.NOCTURNAL,
    minus = listOf(CommonTrait.CLAWS)
)
val monitorLizard = lizard.descend(
    "monitor lizard",
    SizeClass.MEDIUM,
    CommonTrait.ARMORED_HIDE,
    CommonTrait.VENOM_DELIVERY,
    CommonTrait.KEEN_SCENT_SENSE,
    ColorTrait.BROWN_CAMOUFLAGE
)
val wormLizard = lizard.descend(
    "worm lizard",
    SizeClass.TINY,
    CommonTrait.FOSSORIAL_LIVING,
    CommonTrait.BURROW_BUILDER,
    CommonTrait.UNDULATING_BODY,
    ColorTrait.PALE_CAMOUFLAGE,
    minus = listOf(CommonTrait.CLIMBING_LIMBS)
)
val chameleon = lizard.extend(
    "chameleon",
    SizeClass.SMALL,
    CommonTrait.PROJECTILE_TONGUE,
    CommonTrait.CAMOUFLAGE_PATTERN,
    CommonTrait.SLOW_METABOLISM,
    ColorTrait.ADAPTIVE_CAMOUFLAGE,
    minus = listOf(CommonTrait.AMBUSH_MUSCULATURE, CommonTrait.SLENDER_BODY)
)
val iguana = lizard.extend(
    "iguana",
    SizeClass.SMALL,
    CommonTrait.FRUIT_EATING_MOUTHPARTS,
    CommonTrait.BROWSING_MOUTHPARTS,
    minus = listOf(CommonTrait.AMBUSH_MUSCULATURE, CommonTrait.SLENDER_BODY)
)

// snakes
val serpent = reptile.descend(
    "serpent",
    SizeClass.SMALL,
    CommonTrait.KEEN_SCENT_SENSE,
    CommonTrait.UNDULATING_BODY,
    CommonTrait.SLENDER_BODY,
    CommonTrait.AMBUSH_MUSCULATURE,
    CommonTrait.NOCTURNAL,
    ColorTrait.BROWN_CAMOUFLAGE
)
val snake = serpent.descend(
    "snake",
    SizeClass.SMALL
)
val python = serpent.descend(
    "python",
    SizeClass.MEDIUM,
    CommonTrait.CONSTRICTING_BODY,
    ColorTrait.BROWN_CAMOUFLAGE
)
val boa = serpent.descend(
    "boa",
    SizeClass.MEDIUM,
    CommonTrait.CONSTRICTING_BODY,
    CommonTrait.VIVIPARITY,
    ColorTrait.BROWN_CAMOUFLAGE
)
val viper = serpent.descend(
    "viper",
    SizeClass.SMALL,
    CommonTrait.VENOM_DELIVERY,
    CommonTrait.FANGS,
)
val cobra = serpent.descend(
    "cobra",
    SizeClass.SMALL,
    CommonTrait.VENOM_DELIVERY,
    CommonTrait.FANGS,
    CommonTrait.HISSING_WARNING,
    CommonTrait.VESPERTINE
)

val testudines = reptile.extend(
    "testudines",
    SizeClass.SMALL,
    CommonTrait.PROTECTIVE_SHELL,
    CommonTrait.POOR_HEARING
)
val turtle = testudines.extend(
    "turtle",
    SizeClass.SMALL,
    CommonTrait.AMPHIBIOUS_LIMBS,
    CommonTrait.FRESHWATER_OSMOREGULATION,
    CommonTrait.SLOW_GROWTH
)
val seaTurtle = testudines.extend(
    "sea turtle",
    SizeClass.MEDIUM,
    CommonTrait.AQUATIC_FLIPPERS,
    CommonTrait.PROLONGED_BREATH_HOLDING,
    CommonTrait.LONG_MIGRATION,
    minus = listOf(CommonTrait.WALKING_LIMBS)
)
val tortoise = testudines.extend(
    "tortoise",
    SizeClass.MEDIUM,
    CommonTrait.GRAZING_MOUTHPARTS,
    CommonTrait.SLOW_METABOLISM,
)
