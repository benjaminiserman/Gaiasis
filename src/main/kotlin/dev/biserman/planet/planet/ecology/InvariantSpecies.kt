package dev.biserman.planet.planet.ecology

/**
 * Globally authored aggregate guilds used where the simulation does not model
 * their many constituent lineages separately. Their invariant status prevents
 * them from evolving; it does not exempt them from ordinary ecology.
 */
object InvariantSpecies {
    val CARPET_PLANTS = SpeciesDefinition(
        id = "invariant-carpet-plants",
        displayName = "carpet plants",
        sizeClass = SizeClass.TINY,
        motile = false,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.INVARIANT_RESISTANCE,
            CommonTrait.PASSIVE_RESPIRATION,
            CommonTrait.CLONAL_PROPAGATION,
            CommonTrait.THAW_DEPENDENT_GROWTH,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            CommonTrait.ROOTED_BODY,
            CommonTrait.FLOWERS,
            CommonTrait.NECTARIES,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        ),
        kind = SpeciesKind.INVARIANT,
    )

    val BUGS = SpeciesDefinition(
        id = "invariant-bugs",
        displayName = "bugs",
        sizeClass = SizeClass.TINY,
        motile = true,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.INVARIANT_RESISTANCE,
            CommonTrait.PASSIVE_RESPIRATION,
            CommonTrait.TERRESTRIAL_OVOSPORE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.COLLECTIVE_LIVING,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.LIMBED_BODY,
            CommonTrait.WALKING_LIMBS,
            CommonTrait.GRAZING_MOUTHPARTS,
        ),
        kind = SpeciesKind.INVARIANT,
    )

    val SMALL_AQUATIC_LIFE = SpeciesDefinition(
        id = "invariant-small-aquatic-life",
        displayName = "small aquatic life",
        sizeClass = SizeClass.TINY,
        motile = true,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.INVARIANT_RESISTANCE,
            CommonTrait.AQUATIC_OVOSPORE,
            CommonTrait.ECTOTHERMY,
            CommonTrait.COLLECTIVE_LIVING,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.VASCULAR_SYSTEM,
            CommonTrait.LIMBED_BODY,
            CommonTrait.AQUATIC_LIMBS,
            CommonTrait.DEEP_DIVING_PHYSIOLOGY,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.GILLS,
            CommonTrait.GILL_RAKERS,
        ),
        kind = SpeciesKind.INVARIANT,
    )

    val PLANKTON = SpeciesDefinition(
        id = "invariant-plankton",
        displayName = "plankton",
        sizeClass = SizeClass.MINUSCULE,
        motile = true,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.INVARIANT_RESISTANCE,
            CommonTrait.CLONAL_PROPAGATION,
            CommonTrait.MICROSCOPIC_RESTING_STAGES,
            CommonTrait.ECTOTHERMY,
            CommonTrait.COLLECTIVE_LIVING,
            CommonTrait.BUOYANCY_BLADDER,
            CommonTrait.FRESHWATER_OSMOREGULATION,
            CommonTrait.PASSIVE_RESPIRATION,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        ),
        kind = SpeciesKind.INVARIANT,
    )

    val AEROPLANKTON = SpeciesDefinition(
        id = "invariant-aeroplankton",
        displayName = "aeroplankton",
        sizeClass = SizeClass.MINUSCULE,
        motile = true,
        traits = listOf(
            CommonTrait.TEMPERATE_BIOCHEMISTRY,
            CommonTrait.INVARIANT_RESISTANCE,
            CommonTrait.PASSIVE_RESPIRATION,
            CommonTrait.CLONAL_PROPAGATION,
            CommonTrait.ECTOTHERMY,
            CommonTrait.COLLECTIVE_LIVING,
            CommonTrait.AERIAL_FLOATING_BODY,
            CommonTrait.PHOTOSYNTHETIC_SURFACE,
            ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
        ),
        kind = SpeciesKind.INVARIANT,
    )

    val ALL: List<SpeciesDefinition> =
        listOf(CARPET_PLANTS, BUGS, SMALL_AQUATIC_LIFE, PLANKTON, AEROPLANKTON)
}
