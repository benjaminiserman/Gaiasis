package dev.biserman.planet.planet.ecology

enum class StarLight {
    BLUE_WHITE,
    WHITE,
    YELLOW,
    ORANGE,
    RED,
}

enum class BiologicalColor {
    BLACK,
    BROWN,
    GREEN,
    BLUE,
    RED,
    PURPLE,
    PALE,
    WHITE,
    YELLOW,
    COUNTERSHADE,
    ADAPTIVE,
    RAINBOW
}

enum class ThermalStrategy {
    ECTOTHERMY,
    ENDOTHERMY,
    HETEROTHERMY,
}

enum class ActivityPattern {
    DIURNAL,
    NOCTURNAL,
    VESPERTINE,
    CATHEMERAL,
}

enum class AquaticSalinityTolerance {
    SALTWATER_ONLY,
    FRESHWATER_ONLY,
    BROAD,
}

enum class AquaticRespirationMode {
    UNDERWATER,
    BREATH_HOLDING,
}

enum class DormancyKind {
    NONE,
    PROPAGULE,
    BURROWED_EGGS,
    PROLONGED_JUVENILE,
    SEASONAL_TORPOR,
    COLD_DARK_LEAF_DORMANCY,
    DROUGHT_DECIDUOUS,
    WHOLE_BODY_DESICCATION,
}

enum class DispersalKind(val rangeClass: Int) {
    NONE(0),
    NEIGHBOR(1),
    SHORT_MIGRATION(2),
    REGIONAL_MIGRATION(3),
    LONG_MIGRATION(4),
}

/**
 * Invariant species are authored aggregate guilds rather than evolutionary
 * lineages. They still use the ordinary population, niche, interaction, and
 * extinction systems.
 */
enum class SpeciesKind {
    EVOLVING,
    INVARIANT,
}

data class SpeciesDefinition(
    val id: String,
    val displayName: String,
    val sizeClass: SizeClass,
    val motile: Boolean,
    val traits: List<SpeciesTrait>,
    val ancestorSpeciesId: String? = null,
    val kind: SpeciesKind = SpeciesKind.EVOLVING,
    val descendants: MutableList<SpeciesDefinition> = mutableListOf()
) {
    init {
        require(id.isNotBlank())
        require(displayName.isNotBlank())
        TraitProfile.from(traits)
    }
}

/** An unconditional effect applied while compiling one species' phenotype. */
sealed interface DirectTraitEffect : TraitEffect {
    fun applyTo(context: SpeciesCompilationContext)
}

sealed interface TraitEffect {

    data class HabitatAccess(val habitatSelection: HabitatSelection, val amount: Double = 0.0) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) {
            habitatSelection.habitats.forEach { (habitat, factor) ->
                context.accessHabitat(habitat)
                context.adjustHabitatAffinity(habitat, amount * factor)
            }
        }
    }
    data class HabitatAffinity(val habitatSelection: HabitatSelection, val amount: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) {
            habitatSelection.habitats.forEach { (habitat, factor) ->
                context.adjustHabitatAffinity(habitat, amount * factor)
            }
        }
    }

    /** Enables a feeding strategy and optionally contributes its baseline affinity. */
    data class StrategyAccess(val strategy: EcoStrategy, val amount: Double = 0.0) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) {
            context.accessStrategy(strategy)
            context.adjustStrategyAffinity(strategy, amount)
        }
    }

    /** Changes aptitude without granting the anatomy needed to use a strategy. */
    data class StrategyAffinity(val strategy: EcoStrategy, val amount: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.adjustStrategyAffinity(strategy, amount)
    }
    data class TemperatureShift(val degreesC: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.shiftTemperature(degreesC)
    }
    data class TemperatureTolerance(
        val colderC: Double = 0.0,
        val hotterC: Double = 0.0,
        val optimalColderC: Double = 0.0,
        val optimalHotterC: Double = 0.0,
    ) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) =
            context.widenTemperatureTolerance(
                colderC,
                hotterC,
                optimalColderC,
                optimalHotterC,
            )
    }
    data class MinimumActiveTemperature(val temperatureC: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) =
            context.requireMinimumActiveTemperature(temperatureC)
    }
    data class FrozenDormantSurvival(val fractionPerSeason: Double) : DirectTraitEffect {
        init {
            require(fractionPerSeason in 0.0..1.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.multiplyFrozenDormantSurvival(fractionPerSeason)
    }
    data class ThermalRegulation(val strategy: ThermalStrategy) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.regulateTemperatureWith(strategy)
    }
    data class SeasonalColdTolerance(
        val maximumBonusC: Double,
        val triggerInsolation: Double,
    ) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) =
            context.tolerateSeasonalCold(maximumBonusC, triggerInsolation)
    }

    data class WaterRequirement(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeWaterRequirement(change)
    }
    data class MaximumWaterTolerance(
        val optimalMaximumChange: Double,
        val absoluteMaximumChange: Double,
    ) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) =
            context.changeMaximumWaterTolerance(optimalMaximumChange, absoluteMaximumChange)
    }
    data class WaterDepthTolerance(
        val optimalMaximumM: Double,
        val absoluteMaximumM: Double,
    ) : DirectTraitEffect {
        init {
            require(optimalMaximumM >= 0.0)
            require(absoluteMaximumM > optimalMaximumM)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.limitWaterDepth(optimalMaximumM, absoluteMaximumM)
    }
    data class ElevationToleranceShift(val meters: Double) : DirectTraitEffect {
        init {
            require(meters >= 0.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) = context.shiftElevationTolerance(meters)
    }
    data object SnowHydration : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.hydrateFromSnow()
    }

    data class InsolationOptimum(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.shiftInsolationOptimum(change)
    }
    data class CanopyLightEfficiency(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeCanopyLightEfficiency(change)
    }
    data class DenseCanopyForagingPenalty(val maximumPenalty: Double) : DirectTraitEffect {
        init {
            require(maximumPenalty in 0.0..1.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.addDenseCanopyForagingPenalty(maximumPenalty)
    }
    data class CaptureAbility(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeCaptureAbility(change)
    }
    data class BodyMassMultiplier(val multiplier: Double) : DirectTraitEffect {
        init {
            require(multiplier > 0.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.multiplyBodyMass(multiplier)
    }
    data class LargerPreySizeClasses(val additionalClasses: Int) : DirectTraitEffect {
        init {
            require(additionalClasses > 0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.expandLargerPreySizeClasses(additionalClasses)
    }
    data class BurrowerCaptureBonus(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeBurrowerCaptureBonus(change)
    }
    data class PursuitSpeed(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changePursuitSpeed(change)
    }
    data class Sensing(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeSensing(change)
    }
    data class ActivityPatternEffect(val pattern: ActivityPattern) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.setActivityPattern(pattern)
    }
    data class Defense(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeDefense(change)
    }
    data class Camouflage(val habitat: Habitat, val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.addCamouflage(habitat, change)
    }
    data class CamouflageColor(val color: BiologicalColor) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.setCamouflageColor(color)
    }
    data class PhotosyntheticColor(val color: BiologicalColor) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.setPhotosyntheticColor(color)
    }
    data object AposematicColoration : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.enableAposematicColoration()
    }
    data class ReefUse(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeReefUse(change)
    }
    data class ReefBuilding(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeReefBuilding(change)
    }
    data class FruitProduction(val activeBiomassFractionPerSeason: Double) : DirectTraitEffect {
        init {
            require(activeBiomassFractionPerSeason in 0.0..1.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.produceFruit(activeBiomassFractionPerSeason)
    }
    data object Flowering : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.enableFlowering()
    }
    data class NectarProduction(val activeBiomassFractionPerSeason: Double) : DirectTraitEffect {
        init {
            require(activeBiomassFractionPerSeason in 0.0..1.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.produceNectar(activeBiomassFractionPerSeason)
    }
    data class PollinationEfficiency(val change: Double) : DirectTraitEffect {
        init {
            require(change >= 0.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.changePollinationEfficiency(change)
    }
    data class WasteFertilization(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeWasteFertilization(change)
    }
    data class ReserveCapacity(val change: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.changeReserveCapacity(change)
    }
    data class NicheCompetitionSensitivity(val multiplier: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) =
            context.multiplyNicheCompetitionSensitivity(multiplier)
    }

    /** Increases the resource crowding caused by this species' own biomass. */
    data class SelfCrowdingSensitivity(val multiplier: Double) : DirectTraitEffect {
        init {
            require(multiplier >= 1.0) {
                "Self-crowding sensitivity must not reduce self-crowding"
            }
        }

        override fun applyTo(context: SpeciesCompilationContext) =
            context.multiplySelfCrowdingSensitivity(multiplier)
    }
    data class Dormancy(val kind: DormancyKind, val survivalPerSeason: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.enterDormancy(kind, survivalPerSeason)
    }
    data class DormantEntryBiomassRetention(val fraction: Double) : DirectTraitEffect {
        init {
            require(fraction in 0.0..1.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.multiplyDormantEntryRetention(fraction)
    }
    data class DormantReactivationMultiplier(val multiplier: Double) : DirectTraitEffect {
        init {
            require(multiplier >= 1.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) =
            context.multiplyDormantReactivation(multiplier)
    }
    data class Dispersal(val kind: DispersalKind) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.enableDispersal(kind)
    }
    data class RadiationRange(val tileSteps: Int) : DirectTraitEffect {
        init {
            require(tileSteps >= 1)
        }
        override fun applyTo(context: SpeciesCompilationContext) = context.expandRadiationRange(tileSteps)
    }
    data class ReproductionMultiplier(val multiplier: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.multiplyReproduction(multiplier)
    }
    data class MetabolicDemandMultiplier(val multiplier: Double) : DirectTraitEffect {
        init {
            require(multiplier > 0.0)
        }
        override fun applyTo(context: SpeciesCompilationContext) = context.multiplyMetabolicDemand(multiplier)
    }
    data object FreshwaterOsmoregulation : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.enableFreshwaterOsmoregulation()
    }
    data object BroadSalinityTolerance : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.enableBroadSalinityTolerance()
    }
    data class AquaticRespiration(val mode: AquaticRespirationMode) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.enableAquaticRespiration(mode)
    }
    data object PelagicAerialResidency : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.enablePelagicAerialResidency()
    }
    data object DarkWaterAdaptation : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.adaptToDarkWater()
    }
    data class ObligateResidentHabitat(val habitat: Habitat) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.requireResidentHabitat(habitat)
    }
    data object RequiresAdjacentLand : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.requireAdjacentLand()
    }
    data class MaintenanceCost(val fraction: Double) : DirectTraitEffect {
        override fun applyTo(context: SpeciesCompilationContext) = context.addMaintenanceCost(fraction * 0.1)
    }
}

sealed interface SpeciesSelector {
    data class ExactSpecies(val speciesId: String) : SpeciesSelector
    data class DescendantsOf(val ancestorSpeciesId: String) : SpeciesSelector
    data class HasTrait(val trait: CommonTrait) : SpeciesSelector
}

sealed interface RelationshipEffect {
    fun compile(context: RelationshipCompilationContext)

    /**
     * All ordinary feeding edges are replaced by the selected food taxa. At
     * least one selected target must be locally present for the consumer to
     * remain active.
     */
    data class ObligateFood(
        val target: SpeciesSelector,
        val attackRate: Double,
        val assimilationEfficiency: Double,
    ) : RelationshipEffect {
        override fun compile(context: RelationshipCompilationContext) {
            context.forEachTarget(target) { targetIndex ->
                context.requireProducerTarget(targetIndex)
                context.setInteraction(
                    targetIndex,
                    InteractionKind.GRAZING,
                    attackRate,
                    attackRate * assimilationEfficiency,
                    required = true,
                )
            }
        }
    }

    data class SupplementalFood(
        val target: SpeciesSelector,
        val attackRate: Double,
        val assimilationEfficiency: Double,
    ) : RelationshipEffect {
        override fun compile(context: RelationshipCompilationContext) {
            context.forEachTarget(target) { targetIndex ->
                context.setInteraction(
                    targetIndex,
                    InteractionKind.SUPPLEMENTAL_FEEDING,
                    attackRate,
                    attackRate * assimilationEfficiency,
                )
            }
        }
    }

    data class ParasiteOf(
        val target: SpeciesSelector,
        val drainRate: Double,
    ) : RelationshipEffect {
        override fun compile(context: RelationshipCompilationContext) {
            context.forEachTarget(target) { targetIndex ->
                context.setInteraction(
                    targetIndex,
                    InteractionKind.PARASITISM,
                    drainRate,
                    drainRate * 0.35,
                )
            }
        }
    }

    data class BenefitsTargetWhenFeeding(
        val target: SpeciesSelector,
        val benefitRate: Double,
    ) : RelationshipEffect {
        override fun compile(context: RelationshipCompilationContext) {
            context.forEachTarget(target) { targetIndex ->
                context.addTargetBenefit(targetIndex, benefitRate)
            }
        }
    }

    /**
     * The consumer can remain active only while at least one selected target is
     * locally present. This represents obligate hosts or symbionts without
     * prescribing what the relationship extracts from the target.
     */
    data class RequiresTarget(
        val target: SpeciesSelector,
    ) : RelationshipEffect {
        override fun compile(context: RelationshipCompilationContext) {
            context.forEachTarget(target, context::requireTarget)
        }
    }
}

data class NicheDefinition(
    val habitat: Habitat,
    val strategy: EcoStrategy,
) {
    val displayName: String =
        "${habitat.displayName} ${strategy.displayName}"
}

object EcologyNiches {
    /**
     * Strategies own the whitelist of habitats in which they make ecological
     * sense, so adding a strategy does not require a second registry.
     */
    val defaults: List<NicheDefinition> =
        EcoStrategy.entries.flatMap { strategy ->
            strategy.supportedHabitats.map { habitat ->
                NicheDefinition(habitat, strategy)
            }
        }
}
