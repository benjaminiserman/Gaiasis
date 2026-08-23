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

val reptileMinorClades = listOf(
    reptile.extend(
        "gecko",
        SizeClass.TINY,
        CommonTrait.AMBUSH_MUSCULATURE,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.STICKY_FEET,
        CommonTrait.SLENDER_BODY,
        CommonTrait.CHIRPING_CALL,
        CommonTrait.NOCTURNAL
    ),
    reptile.extend(
        "monitor lizard",
        SizeClass.MEDIUM,
        CommonTrait.AMBUSH_MUSCULATURE,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.ARMORED_HIDE,
        CommonTrait.VENOM_DELIVERY,
        CommonTrait.KEEN_SCENT_SENSE,
        ColorTrait.BROWN_CAMOUFLAGE
    ),
    reptile.extend(
        "worm lizard",
        SizeClass.TINY,
        CommonTrait.FOSSORIAL_LIVING,
        CommonTrait.BURROW_BUILDER,
        CommonTrait.SLENDER_BODY,
        CommonTrait.UNDULATING_BODY,
        ColorTrait.PALE_CAMOUFLAGE
    ),
    reptile.extend(
        "snake",
        SizeClass.SMALL,
        CommonTrait.UNDULATING_BODY,
        CommonTrait.AMBUSH_MUSCULATURE,
        CommonTrait.HISSING_WARNING,
        CommonTrait.VENOM_DELIVERY,
        CommonTrait.KEEN_SCENT_SENSE,
        CommonTrait.NOCTURNAL,
        ColorTrait.BROWN_CAMOUFLAGE
    ),
    reptile.extend(
        "chameleon",
        SizeClass.SMALL,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.PROJECTILE_TONGUE,
        CommonTrait.CAMOUFLAGE_PATTERN,
        CommonTrait.SLOW_METABOLISM,
        ColorTrait.ADAPTIVE_CAMOUFLAGE
    ),
    reptile.extend(
        "iguana",
        SizeClass.SMALL,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.FRUIT_EATING_MOUTHPARTS,
        CommonTrait.BROWSING_MOUTHPARTS
    ),
    reptile.extend(
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
    ),
    reptile.extend(
        "turtle",
        SizeClass.SMALL,
        CommonTrait.AMPHIBIOUS_LIMBS,
        CommonTrait.PROTECTIVE_SHELL
    ),
    reptile.extend(
        "tortoise",
        SizeClass.MEDIUM,
        CommonTrait.GRAZING_MOUTHPARTS,
        CommonTrait.SLOW_METABOLISM,
        CommonTrait.PROTECTIVE_SHELL
    ),
)
