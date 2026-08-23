package dev.biserman.planet.planet.ecology.earthlike_clades

import dev.biserman.planet.planet.ecology.ColorTrait
import dev.biserman.planet.planet.ecology.CommonTrait
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog
import dev.biserman.planet.planet.ecology.SizeClass
import dev.biserman.planet.planet.ecology.SpeciesDefinition
import dev.biserman.planet.planet.ecology.SpeciesTrait

/**
 * Similar to EarthSpeciesCatalog, but a bit less specific to Earth
 */
object EarthlikeClades {
    val reptile = EarthSpeciesCatalog.animal(
        "reptile",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.WALKING_LIMBS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.BEHAVIORAL_THERMOREGULATION,
        CommonTrait.DIURNAL,
        CommonTrait.SOLITARY,
        ColorTrait.GREEN_CAMOUFLAGE,
    )

    val amphibian = EarthSpeciesCatalog.animal(
        "amphibian",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.AMPHIBIOUS_LIMBS,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.FRESHWATER_OSMOREGULATION,
        CommonTrait.NOCTURNAL,
        CommonTrait.SOLITARY,
        ColorTrait.GREEN_CAMOUFLAGE,
    )

    val fish = EarthSpeciesCatalog.animal(
        "fish",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.GILLS,
        CommonTrait.AQUATIC_FLIPPERS,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.COLLECTIVE_LIVING,
        CommonTrait.CATHEMERAL,
        ColorTrait.COUNTERSHADE_CAMOUFLAGE,
    )

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

    val insect = EarthSpeciesCatalog.animal(
        "insect",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.WINGS,
        CommonTrait.MOLTING_EXOSKELETON,
        CommonTrait.SOLITARY,
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val arachnid = EarthSpeciesCatalog.animal(
        "arachnid",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.VENOM_DELIVERY,
        CommonTrait.MOLTING_EXOSKELETON,
        CommonTrait.NOCTURNAL,
        CommonTrait.SOLITARY,
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val gastropod = EarthSpeciesCatalog.animal(
        "gastropod",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.MUSCULAR_FOOT,
        CommonTrait.SLOW_METABOLISM,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.SOLITARY,
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val crustacean = EarthSpeciesCatalog.animal(
        "crustacean",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.AMPHIBIOUS_LIMBS,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.ARMORED_HIDE,
        CommonTrait.CRUSHING_CLAWS,
        CommonTrait.NOCTURNAL,
        CommonTrait.SOLITARY,
        ColorTrait.RED_CAMOUFLAGE
    )

    val bivalve = EarthSpeciesCatalog.sessile(
        "bivalve",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.PROTECTIVE_SHELL,
        CommonTrait.GILL_PADS,
        CommonTrait.MUSCULAR_FOOT,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.CATHEMERAL,
        CommonTrait.SOLITARY,
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val cephalopod = EarthSpeciesCatalog.animal(
        "cephalopod",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.JET_PROPULSION,
        CommonTrait.GRASPING_TENTACLES,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.INK_CLOUD,
        CommonTrait.SOLITARY,
        CommonTrait.NOCTURNAL,
        ColorTrait.ADAPTIVE_CAMOUFLAGE,
    )

    val moss = EarthSpeciesCatalog.sessile(
        "moss",
        SizeClass.TINY,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.FROST_HARDENED_TISSUES,
        CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
        CommonTrait.INTERWOVEN_MAT,
        CommonTrait.SURFACE_HOLDFAST
    )

    val fern = EarthSpeciesCatalog.sessile(
        "fern",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.AERIAL_OVOSPORE_DISPERSAL
    )

    val conifer = EarthSpeciesCatalog.sessile(
        "conifer",
        SizeClass.LARGE,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.NEEDLE_LEAVES,
        CommonTrait.ROOTED_BODY,
        CommonTrait.CANOPY_GROWTH,
        CommonTrait.FROST_HARDENED_TISSUES,
        CommonTrait.SEASONAL_LEAF_DORMANCY,
    )

    val angiosperm = EarthSpeciesCatalog.sessile(
        "angiosperm",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.FLOWERS,
    )

    val mold = EarthSpeciesCatalog.sessile(
        "mold",
        SizeClass.TINY,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.SURFACE_HOLDFAST,
        CommonTrait.ABSORPTIVE_FILAMENTS,
        CommonTrait.DECOMPOSING_ENZYMES,
        CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
        CommonTrait.DESICCATION_RESISTANT_PROPAGULES,
    )

    val mushroom = EarthSpeciesCatalog.sessile(
        "mushroom",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.SURFACE_HOLDFAST,
        CommonTrait.ABSORPTIVE_FILAMENTS,
        CommonTrait.DECOMPOSING_ENZYMES,
        CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
        CommonTrait.PERENNIAL_STORAGE_TISSUE,
    )

    val majorCreatureGroups = listOf(
        mammal,
        reptile,
        amphibian,
        fish,
        bird,
        insect,
        arachnid,
        gastropod,
        crustacean,
        bivalve,
        cephalopod,
        moss,
        fern,
        conifer,
        angiosperm,
        mold,
        mushroom
    )

    val minorCreatureGroups = mapOf(
        reptile to listOf(
            reptile.extend(
                "gecko",
                SizeClass.TINY,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.STICKY_FEET,
                CommonTrait.SLENDER_BODY,
                CommonTrait.CHIRPING_CALL,
                CommonTrait.NOCTURNAL,
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
                CommonTrait.BROWSING_MOUTHPARTS,
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
                CommonTrait.NOCTURNAL,
            ),
            reptile.extend(
                "turtle",
                SizeClass.SMALL,
                CommonTrait.AMPHIBIOUS_LIMBS,
                CommonTrait.PROTECTIVE_SHELL,
            ),
            reptile.extend(
                "tortoise",
                SizeClass.MEDIUM,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.SLOW_METABOLISM,
                CommonTrait.PROTECTIVE_SHELL
            )
        ),
        amphibian to listOf(
            amphibian.extend(
                "caecillian",
                SizeClass.TINY,
                CommonTrait.UNDULATING_BODY,
                CommonTrait.FOSSORIAL_LIVING,
                CommonTrait.BURROW_BUILDER,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.MAMMARY_GLANDS,
                CommonTrait.VIVIPARITY,
                CommonTrait.SLIMY_SKIN,
                ColorTrait.PALE_CAMOUFLAGE
            ),
            amphibian.extend(
                "frog",
                SizeClass.TINY,
                CommonTrait.LEAPING_LEGS,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.PROJECTILE_TONGUE,
                CommonTrait.CROAKING_CALL,
                CommonTrait.SLIMY_SKIN
            ),
            amphibian.extend(
                "salamander",
                SizeClass.SMALL,
                CommonTrait.SLIMY_SKIN,
                CommonTrait.LIMB_REGROWTH,
                CommonTrait.AMBUSH_MUSCULATURE
            )
        ),
        bird to listOf(
            bird.extend(
                "ratite",
                SizeClass.MEDIUM,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.LONG_NECK,
                CommonTrait.HERDING_BEHAVIOR,
                CommonTrait.COLLECTIVE_LIVING,
                minus = listOf(CommonTrait.WINGS)
            ),
            bird.extend(
                "fowl",
                SizeClass.SMALL,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.SEED_CRACKING_MOUTHPARTS,
                CommonTrait.FREQUENT_REPRODUCTION,
                CommonTrait.HERDING_BEHAVIOR,
                CommonTrait.COLLECTIVE_LIVING,
            ),
            bird.extend(
                "crane",
                SizeClass.MEDIUM,
                CommonTrait.WADING_LIMBS,
                CommonTrait.SEED_CRACKING_MOUTHPARTS,
                CommonTrait.SPEAR_BILL,
                CommonTrait.REGIONAL_MIGRATION,
                CommonTrait.WATERPROOF_PLUMAGE,
                ColorTrait.WHITE_CAMOUFLAGE
            ),
            bird.extend(
                "gull",
                SizeClass.SMALL,
                CommonTrait.WADING_LIMBS,
                CommonTrait.SCAVENGING_SENSES,
                CommonTrait.WATERPROOF_PLUMAGE,
                ColorTrait.PALE_CAMOUFLAGE
            ),
            bird.extend(
                "hummingbird",
                SizeClass.TINY,
                CommonTrait.FAST_METABOLISM,
                CommonTrait.NECTAR_SIPPING_TONGUE,
                CommonTrait.POLLEN_CARRYING_SURFACES,
                CommonTrait.LONG_MIGRATION,
                CommonTrait.CHIRPING_CALL,
                CommonTrait.COMPLEX_VOCALIZATIONS
            ),
            bird.extend(
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
            ),
            bird.extend(
                "owl",
                SizeClass.SMALL,
                CommonTrait.HOOKED_TALONS,
                CommonTrait.MOTION_TRACKING_SENSES,
                CommonTrait.KEEN_HEARING,
                CommonTrait.SILENT_MOVEMENT,
                CommonTrait.INSULATING_PLUMAGE,
                CommonTrait.HOOTING_CALL,
                CommonTrait.NOCTURNAL,
            ),
            bird.extend(
                "hawk",
                SizeClass.SMALL,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.KEEN_EYESIGHT,
                CommonTrait.MOTION_TRACKING_SENSES,
                CommonTrait.HOOKED_TALONS
            ),
            bird.extend(
                "vulture",
                SizeClass.SMALL,
                CommonTrait.SCAVENGING_SENSES,
                CommonTrait.RESILIENT_DIGESTION,
                CommonTrait.LONG_MIGRATION
            ),
            bird.extend(
                "hornbill",
                SizeClass.SMALL,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.FRUIT_EATING_MOUTHPARTS,
                CommonTrait.CROAKING_CALL,
                ColorTrait.BLACK_CAMOUFLAGE
            ),
            bird.extend(
                "woodpecker",
                SizeClass.SMALL,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.DIGGING_LIMBS,
                CommonTrait.BURROW_BUILDER,
                CommonTrait.DRUMMING_DISPLAY
            ),
            bird.extend(
                "songbird",
                SizeClass.TINY,
                CommonTrait.SEED_CRACKING_MOUTHPARTS,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CHIRPING_CALL,
                CommonTrait.REGIONAL_MIGRATION
            ),
            bird.extend(
                "parrot",
                SizeClass.SMALL,
                CommonTrait.SEED_CRACKING_MOUTHPARTS,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.EXTENDED_PARENTAL_CARE,
                CommonTrait.IMITATIVE_VOCALIZATION,
                CommonTrait.INTELLIGENT,
            ),
            bird.extend(
                "corvid",
                SizeClass.SMALL,
                CommonTrait.SEED_CRACKING_MOUTHPARTS,
                CommonTrait.SCAVENGING_SENSES,
                CommonTrait.TOOL_MANIPULATION,
                CommonTrait.INTELLIGENT,
                CommonTrait.CROAKING_CALL,
                ColorTrait.BLACK_CAMOUFLAGE
            )
        )
    ).mapValues { (_, clades) -> clades.associateBy { it.id } }

    val forb = EarthSpeciesCatalog.sessile(
        "forb",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.FLOWERS,
        CommonTrait.NECTARIES,
    )

    val grass = EarthSpeciesCatalog.sessile(
        "grass",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.FLOWERS,
        CommonTrait.INTERWOVEN_MAT,
    )

    val vine = EarthSpeciesCatalog.sessile(
        "vine",
        SizeClass.MEDIUM,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.CANOPY_GROWTH,
        CommonTrait.SHADE_FRONDS,
        CommonTrait.FLOWERS,
    )

    val broadLeafTree = EarthSpeciesCatalog.sessile(
        "broad-leaf tree",
        SizeClass.LARGE,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.LARGE_EVERGREEN_LEAVES,
        CommonTrait.ROOTED_BODY,
        CommonTrait.CANOPY_GROWTH,
        CommonTrait.FLOWERS,
    )

    fun SpeciesDefinition.extend(
        name: String,
        sizeClass: SizeClass,
        vararg adaptations: SpeciesTrait,
        minus: List<SpeciesTrait> = listOf(),
    ) = copy(
        id = EarthSpeciesCatalog.idFromName(name),
        displayName = name,
        sizeClass = sizeClass,
        traits = this.traits.let { traits ->
            val categorySet = traits.mapNotNull { it.group }
                .toSet()
                .intersect(adaptations.mapNotNull { it.group }.toSet())

            traits.filter { it.group !in categorySet && it !in minus } + adaptations
        },
    )
}
