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
        ColorTrait.GREEN_CAMOUFLAGE,
    )

    val amphibian = animal(
        "amphibian",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.AMPHIBIOUS_LIMBS,
        CommonTrait.AQUATIC_OVOSPORE,
        ColorTrait.GREEN_CAMOUFLAGE,
    )

    val fish = animal(
        "fish",
        SizeClass.SMALL,
        CommonTrait.ECTOTHERMY,
        CommonTrait.GILLS,
        CommonTrait.AQUATIC_FLIPPERS,
        CommonTrait.AQUATIC_OVOSPORE,
        ColorTrait.COUNTERSHADE_CAMOUFLAGE,
    )

    val bird = animal(
        "bird",
        SizeClass.SMALL,
        CommonTrait.ENDOTHERMY,
        CommonTrait.FEATHERED_WINGS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.OVOSPORE_NEST,
        ColorTrait.BROWN_CAMOUFLAGE,
    )

    val insect = animal(
        "insect",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.INSECTOID_WINGS,
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
                CommonTrait.DIGGING_CLAWS,
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
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.LEAPING_LEGS,
                CommonTrait.DENSE_UNDERCOAT,
                CommonTrait.SEASONAL_WINTER_COAT,
            ),
            mammal.extend(
                "megabat",
                SizeClass.SMALL,
                CommonTrait.MEMBRANOUS_WINGS,
                CommonTrait.FRUIT_EATING_MOUTHPARTS,
                CommonTrait.EXTENDED_PARENTAL_CARE
            ),
            mammal.extend(
                "microbat",
                SizeClass.TINY,
                CommonTrait.HETEROTHERMY,
                CommonTrait.MEMBRANOUS_WINGS,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.ECHOLOCATION,
                CommonTrait.SEASONAL_TORPOR,
            ),
            mammal.extend(
                "mole",
                SizeClass.SMALL,
                CommonTrait.DIGGING_CLAWS,
                CommonTrait.SUBTERRANEAN_BURROWING,
                CommonTrait.VENOM_DELIVERY,
                CommonTrait.INSULATED_BURROW_REFUGE,
            ),
            mammal.extend(
                "monkey",
                SizeClass.SMALL,
                CommonTrait.CLIMBING_LIMBS,
                CommonTrait.FRUIT_EATING_MOUTHPARTS,
                CommonTrait.EXTENDED_PARENTAL_CARE
            ),
            mammal.extend(
                "bison",
                SizeClass.LARGE,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.RUMINANT_STOMACH,
                CommonTrait.OPEN_COUNTRY_HERDING,
                CommonTrait.REGIONAL_MIGRATION,
            ),
            mammal.extend(
                "camel",
                SizeClass.LARGE,
                CommonTrait.BROWSING_MOUTHPARTS,
                CommonTrait.RUMINANT_STOMACH,
                CommonTrait.FAT_RESERVES,
                CommonTrait.FOOD_DERIVED_WATER,
                CommonTrait.CONCENTRATED_URINE,
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
                CommonTrait.OPEN_COUNTRY_HERDING,
            ),
            mammal.extend(
                "horse",
                SizeClass.LARGE,
                CommonTrait.SWIFT_LEGS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.FERMENTING_HINDGUT,
                CommonTrait.SWEAT_GLANDS,
                CommonTrait.OPEN_COUNTRY_HERDING,
                CommonTrait.REGIONAL_MIGRATION,
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
            ),
            mammal.extend(
                "elephant",
                SizeClass.HUGE,
                CommonTrait.BROWSING_MOUTHPARTS,
                CommonTrait.PREHENSILE_TRUNK,
                CommonTrait.MASSIVE_EARS,
                CommonTrait.FERMENTING_HINDGUT,
                CommonTrait.OPEN_COUNTRY_HERDING,
                CommonTrait.REGIONAL_MIGRATION,
            ),
            mammal.extend(
                "anteater",
                SizeClass.SMALL,
                CommonTrait.COLONY_PROBING_TONGUE,
                CommonTrait.DIGGING_CLAWS
            ),
            mammal.extend(
                "panther",
                SizeClass.LARGE,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CAMOUFLAGE_PATTERN,
                CommonTrait.FOOD_DERIVED_WATER
            ),
            mammal.extend(
                "wolf",
                SizeClass.MEDIUM,
                CommonTrait.SWIFT_LEGS,
                CommonTrait.MOTION_TRACKING_SENSES,
                CommonTrait.COOPERATIVE_HUNTING,
                CommonTrait.DENSE_UNDERCOAT,
                CommonTrait.SEASONAL_WINTER_COAT
            ),
            mammal.extend(
                "fox",
                SizeClass.SMALL,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.CACHED_FOOD
            ),
            mammal.extend(
                "bear",
                SizeClass.LARGE,
                CommonTrait.HETEROTHERMY,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.BROWSING_MOUTHPARTS,
                CommonTrait.DENSE_UNDERCOAT,
                CommonTrait.SEASONAL_TORPOR,
                CommonTrait.FAT_RESERVES
            ),
            mammal.extend(
                "seal",
                SizeClass.MEDIUM,
                CommonTrait.AQUATIC_FLIPPERS,
                CommonTrait.PROLONGED_BREATH_HOLDING,
                CommonTrait.STROKE_AND_GLIDE_SWIMMING,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.BLUBBER
            ),
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

            traits.filter { it.group !in categorySet && it !in minus } + adaptations
        },
    )
}
