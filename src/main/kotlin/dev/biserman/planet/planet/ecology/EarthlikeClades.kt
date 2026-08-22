package dev.biserman.planet.planet.ecology

import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog.animal
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog.sessile
import kotlin.collections.listOf

/**
 * Similar to EarthSpeciesCatalog, but a bit less specific to Earth
 */
object EarthlikeClades {
    val mammal = animal(
        "mammal",
        SizeClass.MEDIUM,
        CommonTrait.ENDOTHERMY,
        CommonTrait.WALKING_LIMBS,
        CommonTrait.FUR,
        CommonTrait.MAMMARY_GLANDS,
        CommonTrait.VIVIPARITY,
        ColorTrait.BROWN_CAMOUFLAGE,
    )

    val reptile = animal(
        "reptile",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.WALKING_LIMBS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.BEHAVIORAL_THERMOREGULATION,
        CommonTrait.DIURNAL,
        ColorTrait.GREEN_CAMOUFLAGE,
    )

    val amphibian = animal(
        "amphibian",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.AMPHIBIOUS_LIMBS,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.FRESHWATER_OSMOREGULATION,
        ColorTrait.GREEN_CAMOUFLAGE,
    )

    val fish = animal(
        "fish",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.GILLS,
        CommonTrait.AQUATIC_FLIPPERS,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.CATHEMERAL,
        ColorTrait.COUNTERSHADE_CAMOUFLAGE,
    )

    val bird = animal(
        "bird",
        SizeClass.SMALL,
        CommonTrait.ENDOTHERMY,
        CommonTrait.FEATHERS,
        CommonTrait.WINGS,
        CommonTrait.BEAK,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.OVOSPORE_NEST,
        CommonTrait.DIURNAL,
        ColorTrait.BROWN_CAMOUFLAGE,
    )

    val insect = animal(
        "insect",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.WINGS,
        CommonTrait.MOLTING_EXOSKELETON,
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val arachnid = animal(
        "arachnid",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.VENOM_DELIVERY,
        CommonTrait.MOLTING_EXOSKELETON,
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val gastropod = animal(
        "gastropod",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.MUSCULAR_FOOT,
        CommonTrait.SLOW_METABOLISM,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val crustacean = animal(
        "crustacean",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.AMPHIBIOUS_LIMBS,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.ARMORED_HIDE,
        CommonTrait.CRUSHING_CLAWS,
        ColorTrait.RED_CAMOUFLAGE
    )

    val bivalve = sessile(
        "bivalve",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.PROTECTIVE_SHELL,
        CommonTrait.GILL_PADS,
        CommonTrait.MUSCULAR_FOOT,
        CommonTrait.AQUATIC_OVOSPORE,
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val cephalopod = animal(
        "cephalopod",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.JET_PROPULSION,
        CommonTrait.GRASPING_TENTACLES,
        CommonTrait.AQUATIC_OVOSPORE,
        CommonTrait.INK_CLOUD,
        ColorTrait.ADAPTIVE_CAMOUFLAGE,
    )

    val moss = sessile(
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

    val fern = sessile(
        "fern",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.AERIAL_OVOSPORE_DISPERSAL
    )

    val conifer = sessile(
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

    val angiosperm = sessile(
        "angiosperm",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.FLOWERS,
    )

    val mold = sessile(
        "mold",
        SizeClass.TINY,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.SURFACE_HOLDFAST,
        CommonTrait.ABSORPTIVE_FILAMENTS,
        CommonTrait.DECOMPOSING_ENZYMES,
        CommonTrait.AERIAL_OVOSPORE_DISPERSAL,
        CommonTrait.DESICCATION_RESISTANT_PROPAGULES,
    )

    val mushroom = sessile(
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
        mammal to listOf(
            mammal.extend(
                "mouse",
                SizeClass.SMALL,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.DIGGING_LIMBS,
                CommonTrait.CACHED_FOOD,
            ),
            mammal.extend(
                "squirrel",
                SizeClass.SMALL,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.SEED_CRACKING_MOUTHPARTS,
                CommonTrait.CACHED_FOOD,
            ),
            mammal.extend(
                "rabbit",
                SizeClass.SMALL,
                CommonTrait.SWIFT_LEGS,
                CommonTrait.DIGGING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.LEAPING_LEGS,
                CommonTrait.DENSE_UNDERCOAT,
                CommonTrait.SEASONAL_WINTER_COAT,
            ),
            mammal.extend(
                "megabat",
                SizeClass.SMALL,
                CommonTrait.WINGS,
                CommonTrait.FRUIT_EATING_MOUTHPARTS,
                CommonTrait.SCREECHING_CALL,
                CommonTrait.EXTENDED_PARENTAL_CARE
            ),
            mammal.extend(
                "microbat",
                SizeClass.TINY,
                CommonTrait.HETEROTHERMY,
                CommonTrait.WINGS,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.KEEN_HEARING,
                CommonTrait.ECHOLOCATION,
                CommonTrait.SEASONAL_TORPOR,
            ),
            mammal.extend(
                "mole",
                SizeClass.SMALL,
                CommonTrait.DIGGING_LIMBS,
                CommonTrait.FOSSORIAL_LIVING,
                CommonTrait.VENOM_DELIVERY,
                CommonTrait.BURROW_BUILDER,
            ),
            mammal.extend(
                "monkey",
                SizeClass.SMALL,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.FRUIT_EATING_MOUTHPARTS,
                CommonTrait.EXTENDED_PARENTAL_CARE,
                CommonTrait.HOOTING_CALL
            ),
            mammal.extend(
                "bison",
                SizeClass.LARGE,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.RUMINANT_STOMACH,
                CommonTrait.OPEN_COUNTRY_PREFERENCE,
                CommonTrait.HERDING_BEHAVIOR,
                CommonTrait.REGIONAL_MIGRATION,
                CommonTrait.BELLOWING_CALL
            ),
            mammal.extend(
                "camel",
                SizeClass.LARGE,
                CommonTrait.BROWSING_MOUTHPARTS,
                CommonTrait.RUMINANT_STOMACH,
                CommonTrait.FAT_RESERVES,
                CommonTrait.FOOD_DERIVED_WATER,
                CommonTrait.CONCENTRATED_URINE,
                CommonTrait.GRUNTING_CALL
            ),
            mammal.extend(
                "deer",
                SizeClass.MEDIUM,
                CommonTrait.BROWSING_MOUTHPARTS,
                CommonTrait.RUMINANT_STOMACH,
                CommonTrait.DENSE_UNDERCOAT,
                CommonTrait.SWIFT_LEGS,
                CommonTrait.SEASONAL_WINTER_COAT
            ),
            mammal.extend(
                "antelope",
                SizeClass.MEDIUM,
                CommonTrait.SWIFT_LEGS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.RUMINANT_STOMACH,
                CommonTrait.BARE_HEAT_DISSIPATING_SKIN,
                CommonTrait.OPEN_COUNTRY_PREFERENCE,
                CommonTrait.HERDING_BEHAVIOR,
                CommonTrait.BLEATING_CALL
            ),
            mammal.extend(
                "horse",
                SizeClass.LARGE,
                CommonTrait.SWIFT_LEGS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.FERMENTING_HINDGUT,
                CommonTrait.SWEAT_GLANDS,
                CommonTrait.OPEN_COUNTRY_PREFERENCE,
                CommonTrait.HERDING_BEHAVIOR,
                CommonTrait.REGIONAL_MIGRATION,
                CommonTrait.BRAYING_CALL
            ),
            mammal.extend(
                "giraffe",
                SizeClass.LARGE,
                CommonTrait.BROWSING_MOUTHPARTS,
                CommonTrait.LONG_NECK,
                CommonTrait.RUMINANT_STOMACH,
            ),
            mammal.extend(
                "hippo",
                SizeClass.LARGE,
                CommonTrait.AMPHIBIOUS_LIMBS,
                CommonTrait.FRESHWATER_OSMOREGULATION,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.FERMENTING_HINDGUT,
                CommonTrait.ARMORED_HIDE,
                CommonTrait.GRUNTING_CALL,
                minus = listOf(CommonTrait.FUR)
            ),
            mammal.extend(
                "goat",
                SizeClass.MEDIUM,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.RUMINANT_STOMACH,
                CommonTrait.LEAPING_LEGS,
                CommonTrait.SEASONAL_WINTER_COAT,
                CommonTrait.HIGH_AFFINITY_BLOOD,
                CommonTrait.BLEATING_CALL,
                ColorTrait.WHITE_CAMOUFLAGE
            ),
            mammal.extend(
                "elephant",
                SizeClass.HUGE,
                CommonTrait.BROWSING_MOUTHPARTS,
                CommonTrait.PREHENSILE_TRUNK,
                CommonTrait.MASSIVE_EARS,
                CommonTrait.FERMENTING_HINDGUT,
                CommonTrait.HERDING_BEHAVIOR,
                CommonTrait.REGIONAL_MIGRATION,
                CommonTrait.EXTENDED_PARENTAL_CARE,
                CommonTrait.TRUMPETING_CALL,
                CommonTrait.INTELLIGENT,
            ),
            mammal.extend(
                "panther",
                SizeClass.LARGE,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CAMOUFLAGE_PATTERN,
                CommonTrait.FOOD_DERIVED_WATER,
                CommonTrait.STRONG_JAWS,
                CommonTrait.RETRACTABLE_CLAWS,
                CommonTrait.FLEXIBLE_SPINE,
                CommonTrait.ROARING_CALL
            ),
            mammal.extend(
                "cat",
                SizeClass.SMALL,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CAMOUFLAGE_PATTERN,
                CommonTrait.FOOD_DERIVED_WATER,
                CommonTrait.RETRACTABLE_CLAWS,
                CommonTrait.FLEXIBLE_SPINE,
                CommonTrait.SLENDER_BODY,
                CommonTrait.MEOWING_CALL,
                CommonTrait.CHIRPING_CALL,
                CommonTrait.PURRING_CALL
            ),
            mammal.extend(
                "wolf",
                SizeClass.MEDIUM,
                CommonTrait.SWIFT_LEGS,
                CommonTrait.MOTION_TRACKING_SENSES,
                CommonTrait.COOPERATIVE_HUNTING,
                CommonTrait.DENSE_UNDERCOAT,
                CommonTrait.SEASONAL_WINTER_COAT,
                CommonTrait.HOWLING_CALL,
                CommonTrait.BARKING_CALL,
                CommonTrait.INTELLIGENT,
                ColorTrait.PALE_CAMOUFLAGE,
            ),
            mammal.extend(
                "fox",
                SizeClass.SMALL,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CACHED_FOOD,
                CommonTrait.SLENDER_BODY,
                CommonTrait.HIGH_POUNCING,
                CommonTrait.BARKING_CALL
            ),
            mammal.extend(
                "bear",
                SizeClass.LARGE,
                CommonTrait.HETEROTHERMY,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.BROWSING_MOUTHPARTS,
                CommonTrait.DENSE_UNDERCOAT,
                CommonTrait.SEASONAL_TORPOR,
                CommonTrait.FAT_RESERVES,
                CommonTrait.GROWLING_CALL,
                CommonTrait.BULKY_BODY
            ),
            mammal.extend(
                "badger",
                SizeClass.SMALL,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.DIGGING_LIMBS,
                CommonTrait.DENSE_UNDERCOAT,
                ColorTrait.PALE_CAMOUFLAGE
            ),
            mammal.extend(
                "seal",
                SizeClass.MEDIUM,
                CommonTrait.AQUATIC_FLIPPERS,
                CommonTrait.PROLONGED_BREATH_HOLDING,
                CommonTrait.STROKE_AND_GLIDE_SWIMMING,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.BLUBBER,
                CommonTrait.BARKING_CALL,
                ColorTrait.COUNTERSHADE_CAMOUFLAGE,
            ),
            mammal.extend(
                "whale",
                SizeClass.COLOSSAL,
                CommonTrait.AQUATIC_FLIPPERS,
                CommonTrait.PROLONGED_BREATH_HOLDING,
                CommonTrait.DEEP_DIVING_PHYSIOLOGY,
                CommonTrait.STREAMLINED_BODY,
                CommonTrait.BLUBBER,
                CommonTrait.LONG_MIGRATION,
                CommonTrait.WHALESONG,
                CommonTrait.KEEN_HEARING,
                CommonTrait.ECHOLOCATION,
                CommonTrait.INTELLIGENT,
                ColorTrait.COUNTERSHADE_CAMOUFLAGE,
            ),
            mammal.extend(
                "dolphin",
                SizeClass.MEDIUM,
                CommonTrait.AQUATIC_FLIPPERS,
                CommonTrait.PROLONGED_BREATH_HOLDING,
                CommonTrait.STREAMLINED_BODY,
                CommonTrait.BLUBBER,
                CommonTrait.LONG_MIGRATION,
                CommonTrait.CLICK_WHISTLE_REPERTOIRE,
                CommonTrait.COOPERATIVE_HUNTING,
                CommonTrait.KEEN_HEARING,
                CommonTrait.ECHOLOCATION,
                CommonTrait.INTELLIGENT,
                ColorTrait.COUNTERSHADE_CAMOUFLAGE,
            )
        ),
        reptile to listOf(
            reptile.extend(
                "gecko",
                SizeClass.TINY,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.STICKY_FEET,
                CommonTrait.SLENDER_BODY,
                CommonTrait.CHIRPING_CALL,
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
                CommonTrait.BELLOWING_CALL
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
                minus = listOf(CommonTrait.WINGS)
            ),
            bird.extend(
                "fowl",
                SizeClass.SMALL,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.SEED_CRACKING_MOUTHPARTS,
                CommonTrait.FREQUENT_REPRODUCTION,
                CommonTrait.HERDING_BEHAVIOR
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
                CommonTrait.CLICK_WHISTLE_REPERTOIRE
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
                CommonTrait.HOOTING_CALL
            ),
            bird.extend(
                "hawk",
                SizeClass.SMALL,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.KEEN_EYESIGHT,
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
                CommonTrait.DRUMMING_DISPLAY
            ),
            bird.extend(
                "songbird",
                SizeClass.TINY,
                CommonTrait.SEED_CRACKING_MOUTHPARTS,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CHIRPING_CALL,
                CommonTrait.BIRDSONG,
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
    )

    val forb = sessile(
        "forb",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.FLOWERS,
        CommonTrait.NECTARIES,
    )

    val grass = sessile(
        "grass",
        SizeClass.SMALL,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.PHOTOSYNTHETIC_SURFACE,
        CommonTrait.ROOTED_BODY,
        CommonTrait.FLOWERS,
        CommonTrait.INTERWOVEN_MAT,
    )

    val vine = sessile(
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

    val broadLeafTree = sessile(
        "broad-leaf tree",
        SizeClass.LARGE,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        CommonTrait.LARGE_EVERGREEN_LEAVES,
        CommonTrait.ROOTED_BODY,
        CommonTrait.CANOPY_GROWTH,
        CommonTrait.FLOWERS,
    )

    private fun withInferredSocialOrganization(
        traits: List<SpeciesTrait>,
    ): List<SpeciesTrait> {
        val withoutSocialOrganization =
            traits.filterNot { it.group == TraitGroup.SOCIAL_ORGANIZATION }
        val explicitOrganizations =
            traits.filter { it.group == TraitGroup.SOCIAL_ORGANIZATION }
        require(explicitOrganizations.size <= 1) {
            "Multiple social organizations were authored: " +
                explicitOrganizations.joinToString { it.displayName }
        }
        val organization = when {
            CommonTrait.EUSOCIAL_COLONY in explicitOrganizations ->
                CommonTrait.EUSOCIAL_COLONY
            CommonTrait.COLLECTIVE_LIVING in explicitOrganizations ||
                CommonTrait.HERDING_BEHAVIOR in traits ||
                CommonTrait.SCHOOLING in traits ->
                CommonTrait.COLLECTIVE_LIVING
            CommonTrait.GROUP_LIVING in explicitOrganizations ||
                CommonTrait.COOPERATIVE_HUNTING in traits ||
                CommonTrait.GROUP_HUDDLING in traits ->
                CommonTrait.GROUP_LIVING
            else -> CommonTrait.SOLITARY
        }
        return withoutSocialOrganization + organization
    }

    fun SpeciesDefinition.extend(
        name: String,
        sizeClass: SizeClass,
        vararg adaptations: SpeciesTrait,
        minus: List<SpeciesTrait> = listOf(),
    ) = copy(
        id = "...",
        displayName = name,
        sizeClass = sizeClass,
        traits = this.traits.let { traits ->
            val categorySet = traits.mapNotNull { it.group }
                .toSet()
                .intersect(adaptations.mapNotNull { it.group }.toSet())

            withInferredSocialOrganization(
                traits.filter { it.group !in categorySet && it !in minus } + adaptations,
            )
        },
    )
}