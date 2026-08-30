package dev.biserman.planet.planet.ecology

/** The complete phenotype contributed by a scalable trait at one level. */
data class TraitLevelDefinition(
    val displayName: String? = null,
    val description: String? = null,
    val effects: List<TraitEffect>,
    val conditionalEffects: List<ConditionalTraitEffect> = emptyList(),
    val interactionEffects: List<ConditionalInteractionEffect> = emptyList(),
    val capabilities: Set<TraitCapability> = emptySet(),
)

/**
 * Ordered phenotypes for a trait whose evolutionary state can improve or
 * regress one adjacent level at a time. Level zero is represented by absence.
 */
class TraitScale(levels: List<TraitLevelDefinition>) {
    private val levels = levels.toList()

    init {
        require(this.levels.size >= 2) { "A scaled trait must have at least two present levels" }
        require(this.levels.all { it.effects.isNotEmpty() || it.conditionalEffects.isNotEmpty() || it.interactionEffects.isNotEmpty() })
    }

    val maximumLevel: Int
        get() = levels.size

    fun definitionAt(level: Int): TraitLevelDefinition {
        require(level in 1..maximumLevel) { "Trait level $level must be between 1 and $maximumLevel" }
        return levels[level - 1]
    }

    /** Legal one-step evolutionary changes, including loss back to level zero. */
    fun adjacentLevelsFrom(level: Int): List<Int> {
        require(level in 0..maximumLevel)
        return buildList {
            if (level > 0) add(level - 1)
            if (level < maximumLevel) add(level + 1)
        }
    }
}

/** An authored level selection. Ordinary traits implicitly select level one. */
data class LeveledTrait(
    val trait: SpeciesTrait,
    val level: Int,
) : SpeciesTrait by trait {
    init {
        require(trait !is LeveledTrait) { "Nested trait levels are not supported" }
        require(level in 1..trait.maxLevel) {
            "${trait.displayName} level $level must be between 1 and ${trait.maxLevel}"
        }
    }

    override val displayName: String
        get() = trait.displayNameAt(level)

    override val description: String
        get() = trait.descriptionAt(level)
}

fun SpeciesTrait.atLevel(level: Int): SpeciesTrait = LeveledTrait(baseTrait, level)

val SpeciesTrait.baseTrait: SpeciesTrait
    get() = if (this is LeveledTrait) trait else this

val SpeciesTrait.authoredLevel: Int
    get() = if (this is LeveledTrait) level else 1

fun defaultLeveledDisplayNameAdjective(trait: SpeciesTrait, level: Int): String =
    listOf(
        "rudimentary" to 2,
        "limited" to 4,
        "developed" to 1,
        "advanced" to 3,
        "exceptional" to 5
    ).filter {
        it.second <= trait.maxLevel
    }[level - 1].first

fun SpeciesTrait.displayNameAt(level: Int): String =
    if (scale != null) {
        scale?.definitionAt(level)?.displayName ?: "${defaultLeveledDisplayNameAdjective(this, level)} $displayName"
    } else {
        displayName
    }

fun SpeciesTrait.descriptionAt(level: Int): String =
    scale?.definitionAt(level)?.description ?: description

fun SpeciesTrait.adjacentLevelsFrom(level: Int): List<Int> =
    scale?.adjacentLevelsFrom(level) ?: when (level) {
        0 -> listOf(1)
        1 -> listOf(0)
        else -> throw IllegalArgumentException("$displayName has only absent and present states")
    }

data class TraitProfileEntry(
    val trait: SpeciesTrait,
    val level: Int,
)

/** Normalized, level-aware view of a species' authored traits. */
class TraitProfile private constructor(
    val entries: List<TraitProfileEntry>,
) {
    private val levels = entries.associate { it.trait to it.level }

    val traits: Set<SpeciesTrait> = levels.keys

    val capabilities: Set<TraitCapability> = entries
        .flatMapTo(linkedSetOf()) { (trait, level) -> trait.capabilitiesAt(level) }

    fun has(trait: SpeciesTrait): Boolean = trait.baseTrait in levels

    fun levelOf(trait: SpeciesTrait): Int = levels[trait.baseTrait] ?: 0

    companion object {
        fun from(traits: Iterable<SpeciesTrait>): TraitProfile {
            val entries = traits.map { TraitProfileEntry(it.baseTrait, it.authoredLevel) }
            val duplicates = entries.groupBy { it.trait }.filterValues { it.size > 1 }.keys
            require(duplicates.isEmpty()) {
                "A species repeats scaled traits: ${duplicates.joinToString { it.displayName }}"
            }
            entries.forEach { (trait, level) ->
                require(level in 1..trait.maxLevel) {
                    "${trait.displayName} level $level must be between 1 and ${trait.maxLevel}"
                }
            }
            return TraitProfile(entries)
        }
    }
}

fun SpeciesDefinition.traitProfile(): TraitProfile = TraitProfile.from(traits)

fun SpeciesDefinition.hasTrait(trait: SpeciesTrait): Boolean = traitProfile().has(trait)

fun SpeciesDefinition.traitLevel(trait: SpeciesTrait): Int = traitProfile().levelOf(trait)

/** Serializable predicates usable by both same-species and opponent conditions. */
sealed interface TraitCondition {
    fun matches(definition: SpeciesDefinition, profile: TraitProfile = definition.traitProfile()): Boolean

    data class HasTrait(val trait: SpeciesTrait) : TraitCondition {
        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean = profile.has(trait)
    }

    data class TraitLevelAtLeast(val trait: SpeciesTrait, val level: Int) : TraitCondition {
        init {
            require(level >= 1)
        }

        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean =
            profile.levelOf(trait) >= level
    }

    data class HasCapability(val capability: TraitCapability) : TraitCondition {
        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean =
            capability in profile.capabilities
    }

    data class MotilityIs(val motile: Boolean) : TraitCondition {
        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean =
            definition.motile == motile
    }

    data class SizeClassAtLeast(val sizeClass: SizeClass) : TraitCondition {
        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean =
            definition.sizeClass >= sizeClass
    }

    data class SizeClassAtMost(val sizeClass: SizeClass) : TraitCondition {
        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean =
            definition.sizeClass <= sizeClass
    }

    data class AllOf(val conditions: List<TraitCondition>) : TraitCondition {
        init {
            require(conditions.isNotEmpty())
        }

        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean =
            conditions.all { it.matches(definition, profile) }
    }

    data class AnyOf(val conditions: List<TraitCondition>) : TraitCondition {
        init {
            require(conditions.isNotEmpty())
        }

        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean =
            conditions.any { it.matches(definition, profile) }
    }

    data class NoneOf(val conditions: List<TraitCondition>) : TraitCondition {
        override fun matches(definition: SpeciesDefinition, profile: TraitProfile): Boolean =
            conditions.none { it.matches(definition, profile) }
    }
}

/** Effects enabled by the rest of the bearer's compiled trait context. */
data class ConditionalTraitEffect(
    val condition: TraitCondition,
    val effects: List<TraitEffect>,
) {
    init {
        require(effects.isNotEmpty())
    }
}

enum class InteractionEffectSubject {
    BEARER,
    OPPONENT,
}

/** A deliberately small set of pairwise modifiers resolved by the food-web compiler. */
sealed interface InteractionEffect {
    val subject: InteractionEffectSubject
    val multiplier: Double

    data class CaptureBonusMultiplier(
        override val subject: InteractionEffectSubject,
        override val multiplier: Double,
    ) : InteractionEffect {
        init {
            require(multiplier >= 0.0)
        }
    }

    data class DefenseBonusMultiplier(
        override val subject: InteractionEffectSubject,
        override val multiplier: Double,
    ) : InteractionEffect {
        init {
            require(multiplier >= 0.0)
        }
    }
}

/** Pairwise effects enabled when the other creature satisfies a trait condition. */
data class ConditionalInteractionEffect(
    val opponentCondition: TraitCondition,
    val effects: List<InteractionEffect>,
) {
    init {
        require(effects.isNotEmpty())
    }
}
