package dev.biserman.planet.planet.ecology

import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog.animal
import dev.biserman.planet.planet.ecology.EarthSpeciesCatalog.sessile

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
        ColorTrait.BROWN_CAMOUFLAGE
    )

    val arachnid = animal(
        "arachnid",
        SizeClass.TINY,
        CommonTrait.ECTOTHERMY,
        CommonTrait.CLIMBING_LIMBS,
        CommonTrait.TERRESTRIAL_OVOSPORE,
        CommonTrait.VENOM_DELIVERY,
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
        ColorTrait.ADAPTIVE_CAMOUFLAGE
    )

    fun SpeciesDefinition.extend(name: String, sizeClass: SizeClass? = null, vararg adaptations: SpeciesTrait) = copy(
        id = "...",
        displayName = name,
        sizeClass = sizeClass ?: this.sizeClass,
        traits = this.traits.let { traits ->
            val categorySet = traits.mapNotNull { it.group }
                .toSet()
                .intersect(adaptations.mapNotNull { it.group }.toSet())

            traits.filter { it.group !in categorySet } + adaptations
        },
    )
}