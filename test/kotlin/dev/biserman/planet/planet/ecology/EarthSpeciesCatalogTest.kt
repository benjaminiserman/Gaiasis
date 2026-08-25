package dev.biserman.planet.planet.ecology

import org.junit.jupiter.api.BeforeAll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EarthSpeciesCatalogTest {
    companion object {
        private lateinit var compiledCatalog: CompiledEcology

        /**
         * Gates this test class on catalog validity so an invalid catalog produces
         * one actionable compilation failure instead of many cascading failures.
         */
        @JvmStatic
        @BeforeAll
        fun compileCatalogBeforeCatalogTests() {
            compiledCatalog = EcologyCompiler.compile(EarthSpeciesCatalog.ALL + InvariantSpecies.ALL)
        }
    }

    @Test
    fun `earth species catalog compiled successfully`() {
        assertEquals(
            EarthSpeciesCatalog.ALL.size + InvariantSpecies.ALL.size,
            compiledCatalog.species.size,
        )
    }

    @Test
    fun `catalog IDs are inferred from common names`() {
        val catalog = EarthSpeciesCatalog.ALL + EarthSpeciesCatalog.EXTINCT_SPECIES
        assertTrue(catalog.all { it.id == EarthSpeciesCatalog.idFromName(it.displayName) }, message = "Catalog IDs are inferred from common names: expected `catalog.all { it.id == EarthSpeciesCatalog.idFromName(it.displayName) }` to be true")
        assertTrue(
            (catalog + InvariantSpecies.ALL).all {
                it.displayName.first().isLowerCase()
            },
            message = "Catalog IDs are inferred from common names: expected `(catalog + InvariantSpecies.ALL).all { it.displayName.first().isLowerCase() }` to be true"
        )
        assertEquals("thomsons-gazelle", EarthSpeciesCatalog.idFromName("thomson's gazelle"), message = "Catalog IDs are inferred from common names: expected `EarthSpeciesCatalog.idFromName(\"thomson's gazelle\")` to match `\"thomsons-gazelle\"`")
        assertEquals("vicuna", EarthSpeciesCatalog.idFromName("vicuña"), message = "Catalog IDs are inferred from common names: expected `EarthSpeciesCatalog.idFromName(\"vicuña\")` to match `\"vicuna\"`")
        assertEquals("saguaro-cactus", EarthSpeciesCatalog.idFromName("saguaro cactus"), message = "Catalog IDs are inferred from common names: expected `EarthSpeciesCatalog.idFromName(\"saguaro cactus\")` to match `\"saguaro-cactus\"`")
    }

    @Test
    fun `catalog assigns structural anatomy rather than generic locomotion outcomes`() {
        val species = (EarthSpeciesCatalog.ALL + EarthSpeciesCatalog.EXTINCT_SPECIES).associateBy { it.id }

        assertTrue(
            EarthSpeciesCatalog.MAMMALS.all {
                CommonTrait.MAMMARY_GLANDS in it.traits
            },
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `EarthSpeciesCatalog.MAMMALS.all { CommonTrait.MAMMARY_GLANDS in it.traits }` to be true"
        )
        assertTrue(
            EarthSpeciesCatalog.BIRDS.all {
                CommonTrait.FEATHERS in it.traits
            },
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `EarthSpeciesCatalog.BIRDS.all { CommonTrait.FEATHERS in it.traits }` to be true"
        )
        assertTrue(CommonTrait.WINGS in species.getValue("bald-eagle").traits, message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.WINGS in species.getValue(\"bald-eagle\").traits` to be true")
        assertTrue(
            CommonTrait.WINGS in species.getValue("little-brown-bat").traits,
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.WINGS in species.getValue(\"little-brown-bat\").traits` to be true"
        )
        assertTrue(
            CommonTrait.WINGS in species.getValue("western-honey-bee").traits,
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.WINGS in species.getValue(\"western-honey-bee\").traits` to be true"
        )
        assertTrue(
            CommonTrait.UNDULATING_BODY in species.getValue("king-cobra").traits,
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.UNDULATING_BODY in species.getValue(\"king-cobra\").traits` to be true"
        )
        assertTrue(
            CommonTrait.MUSCULAR_FOOT in species.getValue("garden-snail").traits,
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.MUSCULAR_FOOT in species.getValue(\"garden-snail\").traits` to be true"
        )
        assertTrue(
            CommonTrait.CRAWLING_APPENDAGES in species.getValue("leafcutter-ant").traits,
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.CRAWLING_APPENDAGES in species.getValue(\"leafcutter-ant\").traits` to be true"
        )
        assertTrue(
            CommonTrait.WALKING_LIMBS in species.getValue("cheetah").traits,
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.WALKING_LIMBS in species.getValue(\"cheetah\").traits` to be true"
        )
        assertTrue(
            CommonTrait.SWIFT_LEGS in species.getValue("cheetah").traits,
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.SWIFT_LEGS in species.getValue(\"cheetah\").traits` to be true"
        )
        assertTrue(
            CommonTrait.STREAMLINED_PHYSIQUE in species.getValue("atlantic-bluefin-tuna").traits,
            message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.STREAMLINED_BODY in species.getValue(\"atlantic-bluefin-tuna\").traits` to be true"
        )
        listOf("tokay-gecko", "common-house-gecko", "red-eyed-tree-frog").forEach { speciesId ->
            val traits = species.getValue(speciesId).traits
            assertTrue(CommonTrait.CLIMBING_LIMBS in traits, "$speciesId should have climbing limbs")
            assertTrue(CommonTrait.STICKY_FEET in traits, "$speciesId should have sticky feet")
        }
        assertTrue(CommonTrait.FUR in species.getValue("cheetah").traits, message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.FUR in species.getValue(\"cheetah\").traits` to be true")
        assertTrue(CommonTrait.FUR !in species.getValue("blue-whale").traits, message = "Catalog assigns structural anatomy rather than generic locomotion outcomes: expected `CommonTrait.FUR !in species.getValue(\"blue-whale\").traits` to be true")
    }

    @Test
    fun `body engineering and feeding traits cover their representative mammals`() {
        val species = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val beaver = species.getValue("north-american-beaver")
        val withoutDam = beaver.copy(
            id = "beaver-without-dam",
            traits = beaver.traits - CommonTrait.DAM_BUILDING,
        )
        val compiled = EcologyCompiler.compile(listOf(beaver, withoutDam))

        assertTrue(CommonTrait.DAM_BUILDING in beaver.traits, message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.DAM_BUILDING in beaver.traits` to be true")
        assertTrue(
            compiled.species.single { it.id == beaver.id }.lifeHistory.reserveCapacity >
                compiled.species.single { it.id == withoutDam.id }.lifeHistory.reserveCapacity,
            message = "Body engineering and feeding traits cover their representative mammals: expected " +
                "`compiled.species.single { it.id == beaver.id }.lifeHistory.reserveCapacity > " +
                "compiled.species.single { it.id == withoutDam.id }.lifeHistory.reserve...` to be true",
        )
        assertTrue(
            CommonTrait.GLIDING_MEMBRANE in species.getValue("sugar-glider").traits,
            message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.GLIDING_MEMBRANE in species.getValue(\"sugar-glider\").traits` to be true"
        )
        assertTrue(
            CommonTrait.SLENDER_PHYSIQUE in species.getValue("red-fox").traits,
            message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.SLENDER_BODY in species.getValue(\"red-fox\").traits` to be true"
        )
        assertTrue(
            CommonTrait.BULKY_PHYSIQUE in species.getValue("brown-bear").traits,
            message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.BULKY_BODY in species.getValue(\"brown-bear\").traits` to be true"
        )
        listOf("african-elephant", "walrus", "hippopotamus").forEach { id ->
            assertTrue(CommonTrait.LONG_TUSKS in species.getValue(id).traits, message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.LONG_TUSKS in species.getValue(id).traits` to be true")
        }
        listOf(
            "orca",
            "nile-crocodile",
            "american-alligator",
            "great-white-shark",
            "hippopotamus",
            "spotted-hyena",
        ).forEach { id ->
            assertTrue(CommonTrait.STRONG_JAWS in species.getValue(id).traits, message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.STRONG_JAWS in species.getValue(id).traits` to be true")
        }

        listOf("common-raccoon", "white-nosed-coati", "kinkajou").forEach(species::getValue)
        listOf(
            "sable",
            "sea-otter",
            "wolverine",
            "european-badger",
            "honey-badger",
            "stoat",
            "north-american-river-otter",
        ).forEach(species::getValue)
        val honeyBadger = species.getValue("honey-badger")
        assertTrue(CommonTrait.REINFORCED_HIDE in honeyBadger.traits, message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.REINFORCED_HIDE in honeyBadger.traits` to be true")
        assertTrue(CommonTrait.FUR in honeyBadger.traits, message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.FUR in honeyBadger.traits` to be true")
        assertTrue(CommonTrait.BULKY_PHYSIQUE !in honeyBadger.traits, message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.BULKY_BODY !in honeyBadger.traits` to be true")
        assertTrue(CommonTrait.STRONG_JAWS !in honeyBadger.traits, message = "Body engineering and feeding traits cover their representative mammals: expected `CommonTrait.STRONG_JAWS !in honeyBadger.traits` to be true")
    }

    @Test
    fun `cnidarian body plans compile into distinct ecological roles`() {
        val definitions = EarthSpeciesCatalog.INVERTEBRATES.associateBy { it.id }
        val ecology = EcologyCompiler.compile(EarthSpeciesCatalog.ALL + InvariantSpecies.ALL)
        val brainCoral = ecology.species.single { it.id == "brain-coral" }
        val seaFan = ecology.species.single { it.id == "common-sea-fan" }
        val anemone = ecology.species.single { it.id == "giant-green-anemone" }
        val seaWasp = ecology.species.single { it.id == "sea-wasp" }

        assertTrue(brainCoral.interactions.reefBuilding > 0.0, message = "Cnidarian body plans compile into distinct ecological roles: expected `brainCoral.interactions.reefBuilding > 0.0` to be true")
        assertTrue(seaFan.niche.supportFor(EcoStrategy.FILTER_FEEDING) > 0.0, message = "Cnidarian body plans compile into distinct ecological roles: expected `seaFan.niche.supportFor(EcoStrategy.FILTER_FEEDING) > 0.0` to be true")
        assertEquals(0.0, seaFan.interactions.reefBuilding, message = "Cnidarian body plans compile into distinct ecological roles: expected `seaFan.interactions.reefBuilding` to match `0.0`")
        assertTrue(anemone.niche.supportFor(EcoStrategy.AMBUSH_PREDATION) > 0.0, message = "Cnidarian body plans compile into distinct ecological roles: expected `anemone.niche.supportFor(EcoStrategy.AMBUSH_PREDATION) > 0.0` to be true")
        assertTrue(seaWasp.niche.supportFor(Habitat.SHALLOW_OCEAN) > 0.0, message = "Cnidarian body plans compile into distinct ecological roles: expected `seaWasp.niche.supportFor(Habitat.SHALLOW_OCEAN) > 0.0` to be true")
        assertTrue(
            CommonTrait.PULSING_BELL in definitions.getValue("sea-wasp").traits,
            message = "Cnidarian body plans compile into distinct ecological roles: expected `CommonTrait.PULSING_BELL in definitions.getValue(\"sea-wasp\").traits` to be true"
        )
        assertTrue(
            CommonTrait.GELATINOUS_BODY in definitions.getValue("moon-jellyfish").traits,
            message = "Cnidarian body plans compile into distinct ecological roles: expected `CommonTrait.GELATINOUS_BODY in definitions.getValue(\"moon-jellyfish\").traits` to be true"
        )
        assertTrue(
            CommonTrait.BUOYANCY_BLADDER !in definitions.getValue("moon-jellyfish").traits,
            message = "Cnidarian body plans compile into distinct ecological roles: expected `CommonTrait.BUOYANCY_BLADDER !in definitions.getValue(\"moon-jellyfish\").traits` to be true"
        )
    }

    @Test
    fun `catalog includes distinct underrepresented functional archetypes`() {
        val species = EarthSpeciesCatalog.ALL.associateBy { it.id }

        assertTrue(
            CommonTrait.WADING_LIMBS in species.getValue("great-blue-heron").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.WADING_LIMBS in species.getValue(\"great-blue-heron\").traits` to be true"
        )
        assertTrue(
            CommonTrait.COASTAL_BREEDING_SITE in species.getValue("atlantic-puffin").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.COASTAL_BREEDING_SITE in species.getValue(\"atlantic-puffin\").traits` to be true"
        )
        assertTrue(
            CommonTrait.BEHAVIORAL_THERMOREGULATION in species.getValue("desert-horned-lizard").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.BEHAVIORAL_THERMOREGULATION in species.getValue(\"desert-horned-lizard\").traits` to be true"
        )
        assertTrue(
            CommonTrait.FLATTENED_PHYSIQUE in species.getValue("european-plaice").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.BENTHIC_BODY in species.getValue(\"european-plaice\").traits` to be true"
        )
        assertTrue(
            CommonTrait.SCHOOLING in species.getValue("pacific-herring").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.SCHOOLING in species.getValue(\"pacific-herring\").traits` to be true"
        )
        assertTrue(
            CommonTrait.MOLTING_EXOSKELETON in species.getValue("pea-aphid").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.MOLTING_EXOSKELETON in species.getValue(\"pea-aphid\").traits` to be true"
        )
        assertTrue(
            CommonTrait.EPIPHYTIC_ROOTS in species.getValue("epiphytic-orchid").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.EPIPHYTIC_ROOTS in species.getValue(\"epiphytic-orchid\").traits` to be true"
        )
        assertTrue(
            CommonTrait.CUSHION_GROWTH in species.getValue("moss-campion").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.CUSHION_GROWTH in species.getValue(\"moss-campion\").traits` to be true"
        )
        listOf("sphagnum-moss", "reindeer-lichen").forEach { id ->
            assertTrue(CommonTrait.SURFACE_HOLDFAST in species.getValue(id).traits, message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.SURFACE_HOLDFAST in species.getValue(id).traits` to be true")
            assertTrue(CommonTrait.INTERWOVEN_MAT in species.getValue(id).traits, message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.INTERWOVEN_MAT in species.getValue(id).traits` to be true")
            assertTrue(CommonTrait.ROOTED_BODY !in species.getValue(id).traits, message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.ROOTED_BODY !in species.getValue(id).traits` to be true")
        }
        assertTrue(
            CommonTrait.ROOTED_BODY in species.getValue("moss-campion").traits,
            message = "Catalog includes distinct underrepresented functional archetypes: expected `CommonTrait.ROOTED_BODY in species.getValue(\"moss-campion\").traits` to be true"
        )
    }

    @Test
    fun `catalog assigns authored slow life histories`() {
        val species = EarthSpeciesCatalog.ALL.associateBy { it.id }

        listOf(
            "african-elephant",
            "white-rhinoceros",
            "western-gorilla",
            "chimpanzee",
            "bornean-orangutan",
            "giant-panda",
            "west-indian-manatee",
            "galapagos-tortoise",
            "tuatara",
            "great-white-shark",
            "whale-shark",
            "giant-oceanic-manta-ray",
            "antarctic-silverfish",
            "antarctic-toothfish",
            "alligator-gar",
            "american-lobster",
            "brain-coral",
            "common-sea-fan",
            "slender-sea-pen",
            "english-oak",
            "saguaro-cactus",
            "reindeer-lichen",
            "himalayan-juniper",
            "saharan-cypress",
            "moss-campion",
        ).forEach { id ->
            assertTrue(CommonTrait.SLOW_GROWTH in species.getValue(id).traits, "$id should have slow growth")
        }
        listOf(
            "african-elephant",
            "white-rhinoceros",
            "western-gorilla",
            "chimpanzee",
            "bornean-orangutan",
            "polar-bear",
            "brown-bear",
            "giant-panda",
            "blue-whale",
            "orca",
            "bottlenose-dolphin",
            "walrus",
            "west-indian-manatee",
            "wandering-albatross",
            "andean-condor",
            "kakapo",
            "tuatara",
            "great-white-shark",
            "giant-oceanic-manta-ray",
        ).forEach { id ->
            assertTrue(
                CommonTrait.INFREQUENT_REPRODUCTION in species.getValue(id).traits,
                "$id should reproduce infrequently",
            )
        }
        assertTrue(
            CommonTrait.INFREQUENT_REPRODUCTION !in species.getValue("humpback-whale").traits,
            message = "Catalog assigns authored slow life histories: expected `CommonTrait.INFREQUENT_REPRODUCTION !in species.getValue(\"humpback-whale\").traits` to be true"
        )
        assertTrue(
            CommonTrait.INFREQUENT_REPRODUCTION !in species.getValue("giant-bamboo").traits,
            message = "Catalog assigns authored slow life histories: expected `CommonTrait.INFREQUENT_REPRODUCTION !in species.getValue(\"giant-bamboo\").traits` to be true"
        )
    }

    @Test
    fun `catalog assigns limb regrowth to regenerative animals`() {
        val species = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val regenerativeSpecies =
            listOf(
                "axolotl",
                "common-mudpuppy",
                "japanese-giant-salamander",
                "common-octopus",
                "giant-squid",
                "crown-of-thorns-starfish",
                "blue-crab",
                "american-lobster",
                "cleaner-shrimp",
                "common-walkingstick",
            )

        regenerativeSpecies.forEach { id ->
            assertTrue(CommonTrait.LIMB_REGROWTH in species.getValue(id).traits, "$id should regrow limbs")
        }

        val axolotl = species.getValue("axolotl")
        val withoutRegrowth = axolotl.copy(
            id = "axolotl-without-limb-regrowth",
            traits = axolotl.traits - CommonTrait.LIMB_REGROWTH,
        )
        val compiled = EcologyCompiler.compile(listOf(axolotl, withoutRegrowth)).species

        assertTrue(compiled[0].interactions.defense > compiled[1].interactions.defense, message = "Catalog assigns limb regrowth to regenerative animals: expected `compiled[0].interactions.defense > compiled[1].interactions.defense` to be true")
        assertTrue(
            compiled[0].lifeHistory.seasonalReproduction < compiled[1].lifeHistory.seasonalReproduction,
            message = "Catalog assigns limb regrowth to regenerative animals: expected `compiled[0].lifeHistory.seasonalReproduction < compiled[1].lifeHistory.seasonalReproduction` to be true"
        )
        assertTrue(
            compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand,
            message = "Catalog assigns limb regrowth to regenerative animals: expected `compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand` to be true"
        )
    }

    @Test
    fun `trees use authored photosynthetic structures`() {
        val species = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val conifers = listOf(
            "coast-redwood",
            "scots-pine",
            "siberian-larch",
            "himalayan-juniper",
            "lodgepole-pine",
            "saharan-cypress",
            "black-spruce",
        )
        val broadLeafEvergreens = listOf("red-mangrove", "eucalyptus-tree", "strangler-fig")
        val droughtDeciduous = listOf("african-baobab", "umbrella-thorn-acacia")

        conifers.forEach { id ->
            assertTrue(CommonTrait.NEEDLE_LEAVES in species.getValue(id).traits, message = "Trees use authored photosynthetic structures: expected `CommonTrait.NEEDLE_LEAVES in species.getValue(id).traits` to be true")
            assertTrue(CommonTrait.PHOTOSYNTHETIC_SURFACE !in species.getValue(id).traits, message = "Trees use authored photosynthetic structures: expected `CommonTrait.PHOTOSYNTHETIC_SURFACE !in species.getValue(id).traits` to be true")
        }
        broadLeafEvergreens.forEach { id ->
            assertTrue(CommonTrait.LARGE_EVERGREEN_LEAVES in species.getValue(id).traits, message = "Trees use authored photosynthetic structures: expected `CommonTrait.LARGE_EVERGREEN_LEAVES in species.getValue(id).traits` to be true")
            assertTrue(CommonTrait.PHOTOSYNTHETIC_SURFACE !in species.getValue(id).traits, message = "Trees use authored photosynthetic structures: expected `CommonTrait.PHOTOSYNTHETIC_SURFACE !in species.getValue(id).traits` to be true")
        }
        droughtDeciduous.forEach { id ->
            assertTrue(CommonTrait.DROUGHT_DECIDUOUS_LEAVES in species.getValue(id).traits, message = "Trees use authored photosynthetic structures: expected `CommonTrait.DROUGHT_DECIDUOUS_LEAVES in species.getValue(id).traits` to be true")
            assertTrue(CommonTrait.PHOTOSYNTHETIC_SURFACE !in species.getValue(id).traits, message = "Trees use authored photosynthetic structures: expected `CommonTrait.PHOTOSYNTHETIC_SURFACE !in species.getValue(id).traits` to be true")
        }
        assertTrue(
            CommonTrait.DROUGHT_DECIDUOUS_LEAVES !in species.getValue("saharan-cypress").traits,
            message = "Trees use authored photosynthetic structures: expected `CommonTrait.DROUGHT_DECIDUOUS_LEAVES !in species.getValue(\"saharan-cypress\").traits` to be true"
        )
    }

    @Test
    fun `periodical cicadas spend most of their lifecycle as dormant juveniles`() {
        val cicada = EarthSpeciesCatalog.INVERTEBRATES.single { it.id == "periodical-cicada" }
        val ordinaryBurrowedEggLifecycle = cicada.copy(
            id = "seasonal-cicada",
            traits = cicada.traits
                .filterNot { it == CommonTrait.PROLONGED_JUVENILE_DORMANCY } +
                CommonTrait.BURROWING_EGGS,
        )
        val ecology = EcologyCompiler.compile(listOf(cicada, ordinaryBurrowedEggLifecycle))
        val periodical = ecology.species[ecology.speciesIndex(cicada.id)].lifeHistory
        val seasonal = ecology.species[ecology.speciesIndex(ordinaryBurrowedEggLifecycle.id)].lifeHistory

        assertEquals(
            TraitGroup.DORMANCY_MODE,
            CommonTrait.PROLONGED_JUVENILE_DORMANCY.group,
            message = "Periodical cicadas spend most of their lifecycle as dormant juveniles: expected `CommonTrait.PROLONGED_JUVENILE_DORMANCY.group` to match `TraitGroup.DORMANCY_MODE`"
        )
        assertEquals(DormancyKind.PROLONGED_JUVENILE, periodical.dormancyKind, message = "Periodical cicadas spend most of their lifecycle as dormant juveniles: expected `periodical.dormancyKind` to match `DormancyKind.PROLONGED_JUVENILE`")
        assertTrue(periodical.dormantSurvival > seasonal.dormantSurvival, message = "Periodical cicadas spend most of their lifecycle as dormant juveniles: expected `periodical.dormantSurvival > seasonal.dormantSurvival` to be true")
        assertTrue(
            periodical.seasonalReproduction < seasonal.seasonalReproduction,
            message = "Periodical cicadas spend most of their lifecycle as dormant juveniles: expected `periodical.seasonalReproduction < seasonal.seasonalReproduction` to be true"
        )
        assertTrue(
            periodical.dormantReactivationMultiplier > seasonal.dormantReactivationMultiplier,
            message = "Periodical cicadas spend most of their lifecycle as dormant juveniles: expected `periodical.dormantReactivationMultiplier > seasonal.dormantReactivationMultiplier` to be true"
        )
    }

    @Test
    fun `catalog assigns explicit reproductive strategies`() {
        val catalog = EarthSpeciesCatalog.ALL + EarthSpeciesCatalog.EXTINCT_SPECIES + InvariantSpecies.ALL
        assertTrue(
            catalog.all { definition ->
                definition.traits.any { TraitCapability.REPRODUCTION in it.capabilities }
            },
            message = "Catalog assigns explicit reproductive strategies: expected `catalog.all { definition -> definition.traits.any { TraitCapability.REPRODUCTION in it.capabilities } }` to be true",
        )
        assertTrue(
            EarthSpeciesCatalog.MAMMALS
                .filterNot { it.id == "duck-billed-platypus" }
                .all { CommonTrait.VIVIPARITY in it.traits },
            message = "Catalog assigns explicit reproductive strategies: expected `EarthSpeciesCatalog.MAMMALS .filterNot { it.id == \"duck-billed-platypus\" } .all { CommonTrait.VIVIPARITY in it.traits }` to be true",
        )
        assertTrue(
            EarthSpeciesCatalog.MAMMALS
                .filterNot { it.id == "duck-billed-platypus" }
                .none { definition ->
                    definition.traits.any { TraitCapability.OVOSPORE_REPRODUCTION in it.capabilities }
                },
            message = "Catalog assigns explicit reproductive strategies: expected `EarthSpeciesCatalog.MAMMALS .filterNot { it.id == \"duck-billed-platypus\" } .none { definition -> definition.traits.any { TraitCapability.OVOSPORE_R...` to be true",
        )
        val platypus = EarthSpeciesCatalog.MAMMALS.single { it.id == "duck-billed-platypus" }
        assertTrue(CommonTrait.TERRESTRIAL_OVOSPORE in platypus.traits, message = "Catalog assigns explicit reproductive strategies: expected `CommonTrait.TERRESTRIAL_OVOSPORE in platypus.traits` to be true")
        assertTrue(CommonTrait.VIVIPARITY !in platypus.traits, message = "Catalog assigns explicit reproductive strategies: expected `CommonTrait.VIVIPARITY !in platypus.traits` to be true")
        assertTrue(
            EarthSpeciesCatalog.BIRDS.all {
                CommonTrait.TERRESTRIAL_OVOSPORE in it.traits
            },
            message = "Catalog assigns explicit reproductive strategies: expected `EarthSpeciesCatalog.BIRDS.all { CommonTrait.TERRESTRIAL_OVOSPORE in it.traits }` to be true"
        )
        assertTrue(
            EarthSpeciesCatalog.FISH.all {
                CommonTrait.AQUATIC_OVOSPORE in it.traits
            },
            message = "Catalog assigns explicit reproductive strategies: expected `EarthSpeciesCatalog.FISH.all { CommonTrait.AQUATIC_OVOSPORE in it.traits }` to be true"
        )
        assertTrue(
            CommonTrait.CLONAL_PROPAGATION in catalog.single {
                it.id == "staghorn-coral"
            }.traits,
            message = "Catalog assigns explicit reproductive strategies: expected `CommonTrait.CLONAL_PROPAGATION in catalog.single { it.id == \"staghorn-coral\" }.traits` to be true"
        )
        listOf("bracken-fern", "field-mushroom", "bread-mold").forEach { speciesId ->
            assertTrue(
                CommonTrait.AERIAL_OVOSPORE_DISPERSAL in catalog.single { it.id == speciesId }.traits,
                message = "Catalog assigns explicit reproductive strategies: expected `CommonTrait.AERIAL_OVOSPORE_DISPERSAL in catalog.single { it.id == speciesId }.traits` to be true",
            )
        }
    }

    @Test
    fun `mammary glands trade metabolic upkeep for effective offspring recruitment`() {
        val mammal = EarthSpeciesCatalog.MAMMALS.first()
        val withoutMammaryGlands = mammal.copy(
            id = "${mammal.id}-without-mammary-glands",
            traits = mammal.traits - CommonTrait.MAMMARY_GLANDS,
        )
        val compiled = EcologyCompiler.compile(listOf(mammal, withoutMammaryGlands)).species

        assertTrue(
            compiled[0].lifeHistory.seasonalReproduction > compiled[1].lifeHistory.seasonalReproduction,
            message = "Mammary glands trade metabolic upkeep for effective offspring recruitment: expected `compiled[0].lifeHistory.seasonalReproduction > compiled[1].lifeHistory.seasonalReproduction` to be true"
        )
        assertTrue(
            compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand,
            message = "Mammary glands trade metabolic upkeep for effective offspring recruitment: expected `compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand` to be true"
        )
    }

    @Test
    fun `marsupials protect developing young in a brood pouch`() {
        val definitions = EarthSpeciesCatalog.MAMMALS.associateBy { it.id }
        val marsupialIds = listOf("red-kangaroo", "koala", "sugar-glider")

        marsupialIds.forEach { speciesId ->
            assertTrue(
                CommonTrait.BROOD_POUCH in definitions.getValue(speciesId).traits,
                "$speciesId should have a brood pouch",
            )
        }

        val kangaroo = definitions.getValue("red-kangaroo")
        val withoutPouch = kangaroo.copy(
            id = "red-kangaroo-without-pouch",
            traits = kangaroo.traits - CommonTrait.BROOD_POUCH,
        )
        val compiled = EcologyCompiler.compile(listOf(kangaroo, withoutPouch)).species

        assertTrue(
            compiled[0].lifeHistory.seasonalReproduction > compiled[1].lifeHistory.seasonalReproduction,
            message = "Marsupials protect developing young in a brood pouch: expected `compiled[0].lifeHistory.seasonalReproduction > compiled[1].lifeHistory.seasonalReproduction` to be true"
        )
        assertTrue(
            compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand,
            message = "Marsupials protect developing young in a brood pouch: expected `compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand` to be true"
        )
        val requirements = CommonTrait.BROOD_POUCH.requirements.single() as TraitRequirement.AllOf
        assertEquals(
            setOf(CommonTrait.VIVIPARITY),
            requirements.requirements,
            message = "Marsupials protect developing young in a brood pouch: expected `requirements.requirements` to match `setOf(CommonTrait.VIVIPARITY)`",
        )
        val nonLactatingBrooder = kangaroo.copy(
            id = "non-lactating-brood-pouch",
            traits = kangaroo.traits - CommonTrait.MAMMARY_GLANDS,
        )
        EcologyCompiler.compile(listOf(nonLactatingBrooder))
    }

    @Test
    fun `infrequent reproduction reduces reproduction and its average maintenance`() {
        val orca = EarthSpeciesCatalog.MAMMALS.single { it.id == "orca" }
        val frequentReproducer = orca.copy(
            id = "orca-with-frequent-reproduction",
            traits = orca.traits - CommonTrait.INFREQUENT_REPRODUCTION,
        )
        val compiled = EcologyCompiler.compile(listOf(orca, frequentReproducer)).species

        assertTrue(
            compiled[0].lifeHistory.seasonalReproduction < compiled[1].lifeHistory.seasonalReproduction,
            message = "Infrequent reproduction reduces reproduction and its average maintenance: expected `compiled[0].lifeHistory.seasonalReproduction < compiled[1].lifeHistory.seasonalReproduction` to be true"
        )
        assertTrue(
            compiled[0].physiology.maintenanceDemand < compiled[1].physiology.maintenanceDemand,
            message = "Infrequent reproduction reduces reproduction and its average maintenance: expected `compiled[0].physiology.maintenanceDemand < compiled[1].physiology.maintenanceDemand` to be true"
        )
    }

    @Test
    fun `coral grouper depends on living reef cover`() {
        val grouperDefinition = EarthSpeciesCatalog.ALL.single { it.id == "coral-grouper" }
        val grouper = EcologyCompiler.compile(listOf(grouperDefinition)).species.single()
        fun environment(reefCover: Double) = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 27.0,
            insolation = 0.8,
            precipitationMm = 0.0,
            isLand = false,
            waterDepthM = 40.0,
            reefCover = reefCover,
        )

        assertEquals(1.0, grouper.interactions.reefUse, message = "Coral grouper depends on living reef cover: expected `grouper.interactions.reefUse` to match `1.0`")
        assertEquals(0.0, EcologyFitness.reefAssociationMultiplier(grouper, environment(0.0)), message = "Coral grouper depends on living reef cover: expected `EcologyFitness.reefAssociationMultiplier(grouper, environment(0.0))` to match `0.0`")
        assertTrue(EcologyFitness.reefAssociationMultiplier(grouper, environment(0.75)) > 1.0, message = "Coral grouper depends on living reef cover: expected `EcologyFitness.reefAssociationMultiplier(grouper, environment(0.75)) > 1.0` to be true")
    }

    @Test
    fun `bees use nectar feeding and return pollination to flowering producers`() {
        val ecology = EcologyCompiler.compile(EarthSpeciesCatalog.ALL + InvariantSpecies.ALL)
        val bee = ecology.species.single { it.id == "western-honey-bee" }
        val sunflower = ecology.species.single { it.id == "common-sunflower" }

        assertTrue(bee.niche.supportFor(EcoStrategy.NECTAR_FEEDING) > 0.0, message = "Bees use nectar feeding and return pollination to flowering producers: expected `bee.niche.supportFor(EcoStrategy.NECTAR_FEEDING) > 0.0` to be true")
        assertEquals(0.0, bee.niche.supportFor(EcoStrategy.GRAZING), message = "Bees use nectar feeding and return pollination to flowering producers: expected `bee.niche.supportFor(EcoStrategy.GRAZING)` to match `0.0`")
        assertTrue(bee.interactions.pollinationEfficiency > 0.0, message = "Bees use nectar feeding and return pollination to flowering producers: expected `bee.interactions.pollinationEfficiency > 0.0` to be true")
        assertTrue(sunflower.interactions.flowering, message = "Bees use nectar feeding and return pollination to flowering producers: expected `sunflower.interactions.flowering` to be true")
        assertTrue(sunflower.interactions.nectarProduction > 0.0, message = "Bees use nectar feeding and return pollination to flowering producers: expected `sunflower.interactions.nectarProduction > 0.0` to be true")
    }

    @Test
    fun `southern ocean specialists compile into the intended food web`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val silverfish = definitions.getValue("antarctic-silverfish")
        val weddellSeal = definitions.getValue("weddell-seal")
        val crabeaterSeal = definitions.getValue("crabeater-seal")
        val orca = definitions.getValue("orca")
        val ecology = EcologyCompiler.compile(
            listOf(InvariantSpecies.PLANKTON, silverfish, weddellSeal, crabeaterSeal, orca),
        )

        assertEquals(
            InteractionKind.FILTER_FEEDING,
            ecology.interactions.get(
                ecology.speciesIndex(crabeaterSeal.id),
                ecology.speciesIndex(InvariantSpecies.PLANKTON.id),
            ).kind,
            message = "Southern ocean specialists compile into the intended food web: expected " +
                "`ecology.interactions.get(ecology.speciesIndex(crabeaterSeal.id), " +
                "ecology.speciesIndex(InvariantSpecies.PLANKTON.id)).kind` to match " +
                "`InteractionKind.FILTER_FEEDING`",
        )
        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(
                ecology.speciesIndex(weddellSeal.id),
                ecology.speciesIndex(silverfish.id),
            ).kind,
            message = "Southern ocean specialists compile into the intended food web: expected `ecology.interactions.get( ecology.speciesIndex(weddellSeal.id), ecology.speciesIndex(silverfish.id), ).kind` to match `InteractionKind.PREDATION`",
        )
        listOf(weddellSeal, crabeaterSeal).forEach { seal ->
            assertEquals(
                InteractionKind.PREDATION,
                ecology.interactions.get(
                    ecology.speciesIndex(orca.id),
                    ecology.speciesIndex(seal.id),
                ).kind,
                message = "Southern ocean specialists compile into the intended food web: expected `ecology.interactions.get( ecology.speciesIndex(orca.id), ecology.speciesIndex(seal.id), ).kind` to match `InteractionKind.PREDATION`",
            )
        }

        val compiledOrca = ecology.species[ecology.speciesIndex(orca.id)]
        val ordinaryOrcaTraits =
            orca.traits -
                setOf(
                    CommonTrait.EXTENDED_PARENTAL_CARE,
                    CommonTrait.INFREQUENT_REPRODUCTION,
                )
        val ordinaryOrca = EcologyCompiler.compile(
            listOf(
                orca.copy(
                    id = "orca-without-extended-parental-care",
                    traits = ordinaryOrcaTraits,
                ),
            ),
        ).species.single()
        assertTrue(CommonTrait.DEEP_DIVING_PHYSIOLOGY in orca.traits, message = "Southern ocean specialists compile into the intended food web: expected `CommonTrait.DEEP_DIVING_PHYSIOLOGY in orca.traits` to be true")
        assertTrue(compiledOrca.niche.supportFor(Habitat.DARK_WATER) > 0.0, message = "Southern ocean specialists compile into the intended food web: expected `compiledOrca.niche.supportFor(Habitat.DARK_WATER) > 0.0` to be true")
        assertTrue(
            compiledOrca.lifeHistory.seasonalReproduction < ordinaryOrca.lifeHistory.seasonalReproduction,
            message = "Southern ocean specialists compile into the intended food web: expected `compiledOrca.lifeHistory.seasonalReproduction < ordinaryOrca.lifeHistory.seasonalReproduction` to be true"
        )
    }

    @Test
    fun `documented arid mammals conserve water with concentrated urine`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val adaptedIds = listOf(
            "dromedary-camel",
            "fennec-fox",
            "jerboa",
            "red-kangaroo",
        )

        adaptedIds.forEach { speciesId ->
            val adapted = definitions.getValue(speciesId)
            assertTrue(
                CommonTrait.CONCENTRATED_URINE in adapted.traits,
                "$speciesId should have concentrated urine",
            )
            // Several of these mammals also have other water-saving traits, which can
            // legitimately clamp both complete phenotypes to zero required free water.
            // Isolate the kidney trait so the comparison retains its observable range.
            val hydrationControl = adapted.copy(
                id = "$speciesId-hydration-control",
                traits = adapted.traits.filterNot {
                    it != CommonTrait.CONCENTRATED_URINE &&
                        it.effects.any { effect -> effect is TraitEffect.WaterRequirement }
                },
            )
            val baseline = hydrationControl.copy(
                id = "$speciesId-without-concentrated-urine",
                traits = hydrationControl.traits - CommonTrait.CONCENTRATED_URINE,
            )
            val compiled = EcologyCompiler.compile(listOf(hydrationControl, baseline)).species
            assertTrue(
                compiled[0].physiology.hydration.minimumWater < compiled[1].physiology.hydration.minimumWater,
                message = "$speciesId: expected concentrated urine to lower minimum water; " +
                    "adapted=${compiled[0].physiology.hydration.minimumWater}, " +
                    "baseline=${compiled[1].physiology.hydration.minimumWater}",
            )
            assertTrue(
                compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand,
                message = "Documented arid mammals conserve water with concentrated urine: expected `compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand` to be true"
            )
        }
    }

    @Test
    fun `giant bamboo rapid growth trades maintenance for reproduction`() {
        val bamboo = EarthSpeciesCatalog.ALL.single { it.id == "giant-bamboo" }
        assertTrue(CommonTrait.RAPID_GROWTH in bamboo.traits, message = "Giant bamboo rapid growth trades maintenance for reproduction: expected `CommonTrait.RAPID_GROWTH in bamboo.traits` to be true")
        val ordinaryGrowth = bamboo.copy(
            id = "ordinary-growth-bamboo",
            traits = bamboo.traits - CommonTrait.RAPID_GROWTH,
        )
        val compiled = EcologyCompiler.compile(listOf(bamboo, ordinaryGrowth)).species

        assertTrue(
            compiled[0].lifeHistory.seasonalReproduction > compiled[1].lifeHistory.seasonalReproduction,
            message = "Giant bamboo rapid growth trades maintenance for reproduction: expected `compiled[0].lifeHistory.seasonalReproduction > compiled[1].lifeHistory.seasonalReproduction` to be true"
        )
        assertTrue(
            compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand,
            message = "Giant bamboo rapid growth trades maintenance for reproduction: expected `compiled[0].physiology.maintenanceDemand > compiled[1].physiology.maintenanceDemand` to be true"
        )
    }

    @Test
    fun `anteater raids minuscule colonies while bee stingers and honey provide defense and reserves`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val anteater = definitions.getValue("giant-anteater")
        val ant = definitions.getValue("leafcutter-ant")
        val termite = definitions.getValue("termite")
        val bee = definitions.getValue("western-honey-bee")
        val chameleon = definitions.getValue("veiled-chameleon")
        val solitaryInsect = ant.copy(
            id = "solitary-insect",
            displayName = "Solitary insect",
            traits = ant.traits - CommonTrait.EUSOCIAL_COLONY + CommonTrait.SOLITARY,
        )

        assertTrue(
            CommonTrait.COLONY_PROBING_TONGUE in anteater.traits,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.COLONY_PROBING_TONGUE in anteater.traits` to be true"
        )
        assertTrue(CommonTrait.PROJECTILE_TONGUE !in anteater.traits, message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.PROJECTILE_TONGUE !in anteater.traits` to be true")
        assertTrue(CommonTrait.PROJECTILE_TONGUE in chameleon.traits, message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.PROJECTILE_TONGUE in chameleon.traits` to be true")
        assertTrue(CommonTrait.AMBUSH_MUSCULATURE !in anteater.traits, message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.AMBUSH_MUSCULATURE !in anteater.traits` to be true")
        assertTrue(CommonTrait.VENOM_DELIVERY in bee.traits, message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.VENOM_DELIVERY in bee.traits` to be true")
        assertTrue(CommonTrait.HONEY_STORES in bee.traits, message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.HONEY_STORES in bee.traits` to be true")
        assertTrue(CommonTrait.COLONY_THERMOREGULATION in bee.traits, message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.COLONY_THERMOREGULATION in bee.traits` to be true")
        assertTrue(CommonTrait.APOSEMATIC_COLORATION in bee.traits, message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.APOSEMATIC_COLORATION in bee.traits` to be true")
        assertTrue(
            CommonTrait.APOSEMATIC_COLORATION in
                definitions.getValue("poison-dart-frog").traits,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `CommonTrait.APOSEMATIC_COLORATION in definitions.getValue(\"poison-dart-frog\").traits` to be true",
        )

        val ecology = EcologyCompiler.compile(
            listOf(anteater, ant, termite, solitaryInsect, bee, chameleon),
        )
        val anteaterIndex = ecology.speciesIndex(anteater.id)
        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(anteaterIndex, ecology.speciesIndex(ant.id)).kind,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `ecology.interactions.get(anteaterIndex, ecology.speciesIndex(ant.id)).kind` to match `InteractionKind.PREDATION`",
        )
        assertEquals(
            InteractionKind.PREDATION,
            ecology.interactions.get(anteaterIndex, ecology.speciesIndex(termite.id)).kind,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `ecology.interactions.get(anteaterIndex, ecology.speciesIndex(termite.id)).kind` to match `InteractionKind.PREDATION`",
        )
        assertEquals(
            InteractionKind.NONE,
            ecology.interactions.get(anteaterIndex, ecology.speciesIndex(solitaryInsect.id)).kind,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `ecology.interactions.get(anteaterIndex, ecology.speciesIndex(solitaryInsect.id)).kind` to match `InteractionKind.NONE`",
        )
        val compiledAnteater = ecology.species[anteaterIndex]
        assertTrue(
            compiledAnteater.niche.supportFor(EcoStrategy.COLONY_RAIDING) > 0.0,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `compiledAnteater.niche.supportFor(EcoStrategy.COLONY_RAIDING) > 0.0` to be true"
        )
        assertEquals(
            0.0,
            compiledAnteater.niche.supportFor(EcoStrategy.AMBUSH_PREDATION),
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `compiledAnteater.niche.supportFor(EcoStrategy.AMBUSH_PREDATION)` to match `0.0`"
        )
        assertEquals(
            0.0,
            ecology.species[ecology.speciesIndex(chameleon.id)]
                .niche.supportFor(EcoStrategy.COLONY_RAIDING),
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `ecology.species[ecology.speciesIndex(chameleon.id)] .niche.supportFor(EcoStrategy.COLONY_RAIDING)` to match `0.0`",
        )

        val undefendedBee = bee.copy(
            id = "undefended-bee",
            traits = bee.traits - CommonTrait.VENOM_DELIVERY - CommonTrait.HONEY_STORES,
        )
        val beeComparison = EcologyCompiler.compile(listOf(bee, undefendedBee)).species
        assertTrue(
            beeComparison[0].interactions.defense > beeComparison[1].interactions.defense,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `beeComparison[0].interactions.defense > beeComparison[1].interactions.defense` to be true"
        )
        assertTrue(
            beeComparison[0].lifeHistory.reserveCapacity > beeComparison[1].lifeHistory.reserveCapacity,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `beeComparison[0].lifeHistory.reserveCapacity > beeComparison[1].lifeHistory.reserveCapacity` to be true"
        )
        assertTrue(
            beeComparison[0].lifeHistory.seasonalReproduction < beeComparison[1].lifeHistory.seasonalReproduction,
            message = "Anteater raids minuscule colonies while bee stingers and honey provide defense and reserves: expected `beeComparison[0].lifeHistory.seasonalReproduction < beeComparison[1].lifeHistory.seasonalReproduction` to be true"
        )
    }

    @Test
    fun `sloth slow metabolism trades reproductive speed for lower energy demand`() {
        val sloth = EarthSpeciesCatalog.ALL.single { it.id == "three-toed-sloth" }
        assertTrue(CommonTrait.SLOW_METABOLISM in sloth.traits, message = "Sloth slow metabolism trades reproductive speed for lower energy demand: expected `CommonTrait.SLOW_METABOLISM in sloth.traits` to be true")
        val ordinaryMetabolism = sloth.copy(
            id = "ordinary-metabolism-sloth",
            traits = sloth.traits - CommonTrait.SLOW_METABOLISM,
        )
        val compiled = EcologyCompiler.compile(listOf(sloth, ordinaryMetabolism)).species

        assertTrue(
            compiled[0].physiology.maintenanceDemand < compiled[1].physiology.maintenanceDemand,
            message = "Sloth slow metabolism trades reproductive speed for lower energy demand: expected `compiled[0].physiology.maintenanceDemand < compiled[1].physiology.maintenanceDemand` to be true"
        )
        assertTrue(
            compiled[0].lifeHistory.seasonalReproduction < compiled[1].lifeHistory.seasonalReproduction,
            message = "Sloth slow metabolism trades reproductive speed for lower energy demand: expected `compiled[0].lifeHistory.seasonalReproduction < compiled[1].lifeHistory.seasonalReproduction` to be true"
        )
    }

    @Test
    fun `fruit specialists and fruit-bearing producers use the frugivory system`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        listOf("bornean-orangutan", "large-flying-fox").forEach { speciesId ->
            val species = definitions.getValue(speciesId)
            assertTrue(CommonTrait.FRUIT_EATING_MOUTHPARTS in species.traits, message = "Fruit specialists and fruit-bearing producers use the frugivory system: expected `CommonTrait.FRUIT_EATING_MOUTHPARTS in species.traits` to be true")
            assertTrue(CommonTrait.BROWSING_MOUTHPARTS !in species.traits, message = "Fruit specialists and fruit-bearing producers use the frugivory system: expected `CommonTrait.BROWSING_MOUTHPARTS !in species.traits` to be true")
        }
        listOf("strangler-fig", "african-baobab").forEach { speciesId ->
            assertTrue(
                CommonTrait.FRUIT_BEARING in definitions.getValue(speciesId).traits,
                message = "Fruit specialists and fruit-bearing producers use the frugivory system: expected `CommonTrait.FRUIT_BEARING in definitions.getValue(speciesId).traits` to be true"
            )
        }

        val compiled = EcologyCompiler.compile(
            listOf(definitions.getValue("bornean-orangutan")),
        ).species.single()
        assertTrue(compiled.niche.supportFor(EcoStrategy.FRUGIVORY) > 0.0, message = "Fruit specialists and fruit-bearing producers use the frugivory system: expected `compiled.niche.supportFor(EcoStrategy.FRUGIVORY) > 0.0` to be true")
        assertTrue(
            compiled.niche.supportFor(EcoStrategy.FRUGIVORY) >
                compiled.niche.supportFor(EcoStrategy.GRAZING),
            message = "Fruit specialists and fruit-bearing producers use the frugivory system: expected `compiled.niche.supportFor(EcoStrategy.FRUGIVORY) > compiled.niche.supportFor(EcoStrategy.GRAZING)` to be true",
        )
    }

    @Test
    fun `pelican scoop mouth favors coastal fish hunting over land`() {
        val pelican = EarthSpeciesCatalog.ALL.single { it.id == "brown-pelican" }
        assertTrue(
            CommonTrait.SCOOP_MOUTH in pelican.traits,
            message = "Brown pelicans have an expandable scoop pouch for fish hunting.",
        )
        val withoutScoopMouth = pelican.copy(
            id = "brown-pelican-without-scoop-mouth",
            traits = pelican.traits - CommonTrait.SCOOP_MOUTH,
        )
        val compiled = EcologyCompiler.compile(listOf(pelican, withoutScoopMouth)).species

        assertTrue(
            compiled[0].niche.supportFor(Habitat.COASTAL) >
                compiled[1].niche.supportFor(Habitat.COASTAL),
            message = "A scoop mouth improves a pelican's coastal habitat fit.",
        )
        assertTrue(
            compiled[0].niche.supportFor(Habitat.LAND_SURFACE) <
                compiled[1].niche.supportFor(Habitat.LAND_SURFACE),
            message = "A scoop mouth reduces a pelican's land-surface habitat fit.",
        )
        assertTrue(
            compiled[0].niche.supportFor(Habitat.LAND_SURFACE) > 0.0,
            message = "A scoop mouth does not prevent pelicans from resting and nesting on land.",
        )
    }

    @Test
    fun `swift legs improve pursuit capture and pursuit evasion without making prey predatory`() {
        val cheetah = EarthSpeciesCatalog.ALL.single { it.id == "cheetah" }
        val gazelle = EarthSpeciesCatalog.ALL.single { it.id == "thomsons-gazelle" }

        assertTrue(CommonTrait.SWIFT_LEGS in cheetah.traits, message = "Swift legs improve pursuit capture and pursuit evasion without making prey predatory: expected `CommonTrait.SWIFT_LEGS in cheetah.traits` to be true")
        assertTrue(CommonTrait.MOTION_TRACKING_SENSES in cheetah.traits, message = "Swift legs improve pursuit capture and pursuit evasion without making prey predatory: expected `CommonTrait.MOTION_TRACKING_SENSES in cheetah.traits` to be true")
        assertTrue(CommonTrait.SWIFT_LEGS in gazelle.traits, message = "Swift legs improve pursuit capture and pursuit evasion without making prey predatory: expected `CommonTrait.SWIFT_LEGS in gazelle.traits` to be true")
        assertTrue(CommonTrait.MOTION_TRACKING_SENSES !in gazelle.traits, message = "Swift legs improve pursuit capture and pursuit evasion without making prey predatory: expected `CommonTrait.MOTION_TRACKING_SENSES !in gazelle.traits` to be true")

        val compiledGazelle = EcologyCompiler.compile(listOf(gazelle)).species.single()
        assertEquals(
            0.0,
            compiledGazelle.niche.supportFor(EcoStrategy.PURSUIT_PREDATION),
            message = "Swift legs improve pursuit capture and pursuit evasion without making prey predatory: expected `compiledGazelle.niche.supportFor(EcoStrategy.PURSUIT_PREDATION)` to match `0.0`",
        )

        val slowCheetah = cheetah.copy(
            id = "slow-cheetah",
            traits = cheetah.traits - CommonTrait.SWIFT_LEGS,
        )
        val slowGazelle = gazelle.copy(
            id = "slow-gazelle",
            traits = gazelle.traits - CommonTrait.SWIFT_LEGS,
        )
        val swiftHunterAgainstSlowPrey = predationRate(cheetah, slowGazelle)
        val slowHunterAgainstSlowPrey = predationRate(slowCheetah, slowGazelle)
        val swiftHunterAgainstSwiftPrey = predationRate(cheetah, gazelle)

        assertTrue(swiftHunterAgainstSlowPrey > slowHunterAgainstSlowPrey, message = "Swift legs improve pursuit capture and pursuit evasion without making prey predatory: expected `swiftHunterAgainstSlowPrey > slowHunterAgainstSlowPrey` to be true")
        assertTrue(
            swiftHunterAgainstSwiftPrey < swiftHunterAgainstSlowPrey,
            message = "Swift legs improve pursuit capture and pursuit evasion without making prey predatory: expected `swiftHunterAgainstSwiftPrey < swiftHunterAgainstSlowPrey` to be true"
        )
    }

    @Test
    fun `regional biodiversity additions cover six underrepresented environments`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val regionalSpecies = mapOf(
            "Siberia" to listOf("siberian-larch", "siberian-musk-deer", "sable"),
            "Himalayan plateau" to listOf("himalayan-juniper", "wild-yak", "himalayan-pika"),
            "Rockies" to listOf("lodgepole-pine", "rocky-mountain-elk", "mountain-goat"),
            "Andes" to listOf("ichu-grass", "vicuna", "andean-condor"),
            "Sahara" to listOf("saharan-cypress", "addax", "fennec-fox"),
            "Canadian Shield" to listOf("black-spruce", "woodland-caribou", "canada-lynx"),
        )

        regionalSpecies.forEach { (region, speciesIds) ->
            val species = speciesIds.map { requireNotNull(definitions[it]) { "$region is missing $it" } }
            assertEquals(1, species.count { !it.motile }, "$region plant coverage")
            assertEquals(2, species.count { it.motile }, "$region animal coverage")
        }

        val coldRegionIds = regionalSpecies
            .filterKeys { it != "Sahara" }
            .values
            .flatten()
        coldRegionIds.forEach { speciesId ->
            val traits = definitions.getValue(speciesId).traits
            assertTrue(
                traits.any {
                    it == CommonTrait.DENSE_UNDERCOAT ||
                        it == CommonTrait.INSULATING_PLUMAGE ||
                        it == CommonTrait.FROST_HARDENED_TISSUES ||
                        it == CommonTrait.SEASONAL_WINTER_COAT
                },
                "$speciesId lacks an explicit cold-climate adaptation",
            )
        }
    }

    @Test
    fun `Himalayan specialists are physically viable in a cold semidesert at 4500 meters`() {
        val ecology = EcologyCompiler.compile(EarthSpeciesCatalog.ALL)
        val temperatures = listOf(
            -16.1, -11.5, -3.8, 2.7, 6.1, 7.0,
            6.9, 6.1, 3.3, -2.7, -10.1, -15.6,
        )
        val insolations = listOf(
            0.49, 0.55, 0.65, 0.75, 0.81, 0.83,
            0.82, 0.78, 0.70, 0.60, 0.52, 0.47,
        )
        // About 327 mm annually: a dry, high-elevation semidesert with
        // winter snow and a modest summer monsoon rather than hyperarid desert.
        val precipitation = listOf(6.0, 8.0, 14.0, 24.0, 34.0, 48.0, 58.0, 56.0, 42.0, 22.0, 10.0, 5.0)
        val annualEnvironments = temperatures.indices.map { month ->
            SeasonalCellEnvironment.create(
                areaKm2 = 40_000.0,
                temperatureC = temperatures[month],
                annualAverageTemperatureC = -2.31,
                insolation = insolations[month],
                precipitationMm = precipitation[month],
                isLand = true,
                elevationM = 4_500.0,
            )
        }

        listOf("wild-yak", "himalayan-pika", "snow-leopard").forEach { speciesId ->
            val species = ecology.species.single { it.id == speciesId }
            val suitability = EcologySuitability.evaluate(species, ecology, annualEnvironments)
            assertTrue(
                suitability.suitable,
                "$speciesId: score=${suitability.score}, mean=${suitability.meanAnnualFitness}, " +
                    "best=${suitability.bestSeasonFitness}, viable=${suitability.viableSeasonFraction}, " +
                    "issues=${suitability.issues}",
            )
        }
    }

    @Test
    fun `catalog corrections keep feeding and insulation anatomy explicit`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val walrus = requireNotNull(definitions["walrus"])
        val manatee = requireNotNull(definitions["west-indian-manatee"])

        assertTrue(CommonTrait.SUCTION_FEEDING in walrus.traits, message = "Catalog corrections keep feeding and insulation anatomy explicit: expected `CommonTrait.BENTHIC_SUCTION_FEEDING in walrus.traits` to be true")
        assertTrue(CommonTrait.SIEVING_TEETH !in walrus.traits, message = "Catalog corrections keep feeding and insulation anatomy explicit: expected `CommonTrait.SIEVING_TEETH !in walrus.traits` to be true")
        assertTrue(CommonTrait.BLUBBER !in manatee.traits, message = "Catalog corrections keep feeding and insulation anatomy explicit: expected `CommonTrait.BLUBBER !in manatee.traits` to be true")
        assertTrue("tardigrade" !in definitions, message = "Catalog corrections keep feeding and insulation anatomy explicit: expected `\"tardigrade\" !in definitions` to be true")

        val ecology = EcologyCompiler.compile(EarthSpeciesCatalog.ALL)
        val compiledWalrus = ecology.species.single { it.id == "walrus" }
        assertEquals(
            0.0,
            compiledWalrus.niche.supportFor(EcoStrategy.FILTER_FEEDING),
            message = "Catalog corrections keep feeding and insulation anatomy explicit: expected `compiledWalrus.niche.supportFor(EcoStrategy.FILTER_FEEDING)` to match `0.0`",
        )
        assertTrue(
            compiledWalrus.niche.supportFor(EcoStrategy.AMBUSH_PREDATION) > 0.0,
            message = "Catalog corrections keep feeding and insulation anatomy explicit: expected `compiledWalrus.niche.supportFor(EcoStrategy.AMBUSH_PREDATION) > 0.0` to be true",
        )
    }

    @Test
    fun `open country preference herding and benthic suction feeding cover matching animals`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        val openCountryHerders = setOf(
            "plains-zebra",
            "blue-wildebeest",
            "thomsons-gazelle",
            "red-kangaroo",
            "american-bison",
            "common-ostrich",
        )
        val benthicSuctionFeeders = setOf("walrus", "common-carp")

        openCountryHerders.forEach { speciesId ->
            assertTrue(
                CommonTrait.OPEN_COUNTRY_PREFERENCE in requireNotNull(definitions[speciesId]).traits &&
                    CommonTrait.HERDING_BEHAVIOR in requireNotNull(definitions[speciesId]).traits,
                "$speciesId should combine open-country preference with herding behavior",
            )
        }
        benthicSuctionFeeders.forEach { speciesId ->
            assertTrue(
                CommonTrait.SUCTION_FEEDING in
                    requireNotNull(definitions[speciesId]).traits,
                "$speciesId should have benthic suction feeding",
            )
        }

        val ecology = EcologyCompiler.compile(EarthSpeciesCatalog.ALL)
        val carp = ecology.species.single { it.id == "common-carp" }
        assertTrue(
            carp.niche.supportFor(EcoStrategy.GENERALIST_FORAGING) > 0.0,
            message = "Open country preference herding and benthic suction feeding cover matching animals: expected `carp.niche.supportFor(EcoStrategy.GENERALIST_FORAGING) > 0.0` to be true"
        )
        assertEquals(0.0, carp.niche.supportFor(EcoStrategy.FILTER_FEEDING), message = "Open country preference herding and benthic suction feeding cover matching animals: expected `carp.niche.supportFor(EcoStrategy.FILTER_FEEDING)` to match `0.0`")
    }

    @Test
    fun `new defensive movement social and signaling traits cover representative species`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }

        fun traitsOf(id: String) = requireNotNull(definitions[id]).traits

        assertTrue(CommonTrait.REINFORCED_HIDE in traitsOf("honey-badger"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.REINFORCED_HIDE in traitsOf(\"honey-badger\")` to be true")
        assertTrue(CommonTrait.FUR in traitsOf("honey-badger"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.FUR in traitsOf(\"honey-badger\")` to be true")
        assertTrue(CommonTrait.ANTLERS in traitsOf("white-tailed-deer"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.ANTLERS in traitsOf(\"white-tailed-deer\")` to be true")
        assertTrue(CommonTrait.HORNS in traitsOf("blue-wildebeest"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.LARGE_HORN in traitsOf(\"blue-wildebeest\")` to be true")
        assertTrue(
            CommonTrait.RETRACTABLE_CLAWS in traitsOf("african-lion"),
            message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.RETRACTABLE_CLAWS in traitsOf(\"african-lion\")` to be true"
        )
        assertTrue(CommonTrait.FLEXIBLE_SPINE in traitsOf("cheetah"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.FLEXIBLE_SPINE in traitsOf(\"cheetah\")` to be true")
        assertTrue(CommonTrait.HIGH_POUNCING in traitsOf("red-fox"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.HIGH_POUNCING in traitsOf(\"red-fox\")` to be true")
        assertTrue(
            CommonTrait.OPEN_COUNTRY_PREFERENCE !in traitsOf("woodland-caribou"),
            message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.OPEN_COUNTRY_PREFERENCE !in traitsOf(\"woodland-caribou\")` to be true"
        )
        assertTrue(
            CommonTrait.HERDING_BEHAVIOR in traitsOf("woodland-caribou"),
            message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.HERDING_BEHAVIOR in traitsOf(\"woodland-caribou\")` to be true"
        )
        assertTrue(CommonTrait.HOWLING_CALL in traitsOf("gray-wolf"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.HOWLING_CALL in traitsOf(\"gray-wolf\")` to be true")
        assertTrue(
            CommonTrait.RATTLING_WARNING in traitsOf("western-diamondback-rattlesnake"),
            message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.RATTLING_WARNING in traitsOf(\"western-diamondback-rattlesnake\")` to be true"
        )
        assertTrue(CommonTrait.SPEAR_BILL in traitsOf("red-crowned-crane"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.SPEAR_BILL in traitsOf(\"red-crowned-crane\")` to be true")
        assertTrue(CommonTrait.SPEAR_BILL in traitsOf("great-blue-heron"), message = "New defensive movement social and signaling traits cover representative species: expected `CommonTrait.SPEAR_BILL in traitsOf(\"great-blue-heron\")` to be true")
    }

    @Test
    fun `cognition activity and structural traits cover representative species`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        fun traitsOf(id: String) = requireNotNull(definitions[id]).traits

        listOf(
            "african-elephant",
            "western-gorilla",
            "chimpanzee",
            "bornean-orangutan",
            "gray-wolf",
            "blue-whale",
            "humpback-whale",
            "orca",
            "bottlenose-dolphin",
            "common-raven",
            "african-grey-parrot",
            "common-octopus",
        ).forEach { speciesId ->
            assertTrue(CommonTrait.INTELLIGENT in traitsOf(speciesId), "$speciesId should have intelligence")
        }
        assertTrue(CommonTrait.GROUP_HUDDLING in traitsOf("emperor-penguin"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.GROUP_HUDDLING in traitsOf(\"emperor-penguin\")` to be true")
        assertTrue(CommonTrait.FLIGHTLESS_WINGS in traitsOf("emperor-penguin"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.FLIGHTLESS_WINGS in traitsOf(\"emperor-penguin\")` to be true")
        assertTrue(CommonTrait.BEAK in traitsOf("common-raven"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.BEAK in traitsOf(\"common-raven\")` to be true")
        assertTrue(CommonTrait.STINGER in traitsOf("western-honey-bee"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.STINGER in traitsOf(\"western-honey-bee\")` to be true")
        assertTrue(CommonTrait.TENTACLES in traitsOf("common-octopus"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.TENTACLES in traitsOf(\"common-octopus\")` to be true")
        assertTrue(CommonTrait.AUTOTOMY in traitsOf("tokay-gecko"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.AUTOTOMY in traitsOf(\"tokay-gecko\")` to be true")
        assertTrue(CommonTrait.DIURNAL in traitsOf("cheetah"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.DIURNAL in traitsOf(\"cheetah\")` to be true")
        assertTrue(CommonTrait.NOCTURNAL in traitsOf("bengal-tiger"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.NOCTURNAL in traitsOf(\"bengal-tiger\")` to be true")
        assertTrue(CommonTrait.VESPERTINE in traitsOf("snow-leopard"), message = "Cognition activity and structural traits cover representative species: expected `CommonTrait.VESPERTINE in traitsOf(\"snow-leopard\")` to be true")
    }

    @Test
    fun `social organization is explicitly authored`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }
        fun traitsOf(id: String) = requireNotNull(definitions[id]).traits

        assertTrue(CommonTrait.SOLITARY in traitsOf("bengal-tiger"), message = "Social organization is explicitly authored: expected `CommonTrait.SOLITARY in traitsOf(\"bengal-tiger\")` to be true")
        assertTrue(CommonTrait.GROUP_LIVING in traitsOf("gray-wolf"), message = "Social organization is explicitly authored: expected `CommonTrait.GROUP_LIVING in traitsOf(\"gray-wolf\")` to be true")
        assertTrue(CommonTrait.COLLECTIVE_LIVING in traitsOf("emperor-penguin"), message = "Social organization is explicitly authored: expected `CommonTrait.COLLECTIVE_LIVING in traitsOf(\"emperor-penguin\")` to be true")
        assertTrue(CommonTrait.COLLECTIVE_LIVING in traitsOf("plains-zebra"), message = "Social organization is explicitly authored: expected `CommonTrait.COLLECTIVE_LIVING in traitsOf(\"plains-zebra\")` to be true")
        assertTrue(CommonTrait.COLLECTIVE_LIVING in traitsOf("pacific-herring"), message = "Social organization is explicitly authored: expected `CommonTrait.COLLECTIVE_LIVING in traitsOf(\"pacific-herring\")` to be true")
        assertTrue(CommonTrait.EUSOCIAL_COLONY in traitsOf("western-honey-bee"), message = "Social organization is explicitly authored: expected `CommonTrait.EUSOCIAL_COLONY in traitsOf(\"western-honey-bee\")` to be true")

        EarthSpeciesCatalog.ALL.filter { it.motile }.forEach { definition ->
            assertEquals(
                1,
                definition.traits.count { it.group == TraitGroup.SOCIAL_ORGANIZATION },
                definition.displayName,
            )
        }
    }

    @Test
    fun `catalog activity patterns are non-conflicting and structurally valid`() {
        val conflicting = EarthSpeciesCatalog.ALL.filter { definition ->
            definition.traits.count { it.group == TraitGroup.ACTIVITY_PATTERN } > 1
        }

        assertTrue(conflicting.isEmpty(), "Conflicting activity patterns: ${conflicting.joinToString { it.id }}")
        EarthSpeciesCatalog.ALL.forEach { definition ->
            assertTrue(
                TraitDependencies.unmetRequirements(definition).none {
                    it.trait.group == TraitGroup.ACTIVITY_PATTERN
                },
                "${definition.displayName} has an invalid activity pattern",
            )
        }
    }

    @Test
    fun `major animal acoustic repertoires are represented without blanket sound traits`() {
        val definitions = EarthSpeciesCatalog.ALL.associateBy { it.id }

        fun traitsOf(id: String) = requireNotNull(definitions[id]).traits

        mapOf(
            "plains-zebra" to CommonTrait.BRAYING_CALL,
            "spotted-hyena" to CommonTrait.WHOOPING_CALL,
            "chimpanzee" to CommonTrait.HOOTING_CALL,
            "cheetah" to CommonTrait.CHIRPING_CALL,
            "orca" to CommonTrait.COMPLEX_VOCALIZATIONS,
            "weddell-seal" to CommonTrait.TRILLING_CALL,
            "great-horned-owl" to CommonTrait.HOOTING_CALL,
            "scarlet-macaw" to CommonTrait.IMITATIVE_VOCALIZATION,
            "common-ostrich" to CommonTrait.BOOMING_CALL,
            "mallard-duck" to CommonTrait.QUACKING_CALL,
            "pileated-woodpecker" to CommonTrait.DRUMMING_DISPLAY,
            "nile-crocodile" to CommonTrait.BELLOWING_CALL,
            "king-cobra" to CommonTrait.HISSING_WARNING,
        ).forEach { (speciesId, acousticTrait) ->
            assertTrue(acousticTrait in traitsOf(speciesId), "$speciesId should have ${acousticTrait.displayName}")
        }

        listOf("cheetah", "snow-leopard", "canada-lynx", "margay", "european-wildcat").forEach { speciesId ->
            assertTrue(CommonTrait.MEOWING_CALL in traitsOf(speciesId), "$speciesId should have a meowing call")
            assertTrue(CommonTrait.PURRING_CALL in traitsOf(speciesId), "$speciesId should have a purring call")
        }
        listOf("margay", "european-wildcat", "northern-shrike").forEach { speciesId ->
            assertTrue(CommonTrait.CHIRPING_CALL in traitsOf(speciesId), "$speciesId should have a chirping call")
            assertTrue(CommonTrait.SOUND_LURES in traitsOf(speciesId), "$speciesId should use sound lures")
        }

        val explicitlyAcoustic = setOf(
            CommonTrait.HOWLING_CALL,
            CommonTrait.RATTLING_WARNING,
            CommonTrait.ROARING_CALL,
            CommonTrait.TRUMPETING_CALL,
            CommonTrait.BELLOWING_CALL,
            CommonTrait.BLEATING_CALL,
            CommonTrait.GRUNTING_CALL,
            CommonTrait.HONKING_CALL,
            CommonTrait.BUGLING_CALL,
            CommonTrait.CROAKING_CALL,
            CommonTrait.BRAYING_CALL,
            CommonTrait.HOOTING_CALL,
            CommonTrait.BARKING_CALL,
            CommonTrait.GROWLING_CALL,
            CommonTrait.SCREECHING_CALL,
            CommonTrait.QUACKING_CALL,
            CommonTrait.CROWING_CALL,
            CommonTrait.TRILLING_CALL,
            CommonTrait.CHIRPING_CALL,
            CommonTrait.MEOWING_CALL,
            CommonTrait.PURRING_CALL,
            CommonTrait.HISSING_WARNING,
            CommonTrait.IMITATIVE_VOCALIZATION,
            CommonTrait.BOOMING_CALL,
            CommonTrait.DRUMMING_DISPLAY,
            CommonTrait.WHOOPING_CALL,
            CommonTrait.COMPLEX_VOCALIZATIONS,
        )
        listOf("three-toed-sloth", "galapagos-tortoise", "turkey-vulture").forEach { speciesId ->
            assertTrue(
                traitsOf(speciesId).none(explicitlyAcoustic::contains),
                "$speciesId should not have an explicitly acoustic trait",
            )
        }
    }

    @Test
    fun `marine freshwater and euryhaline species compile to distinct water chemistry`() {
        val definitions = listOf(
            EarthSpeciesCatalog.MAMMALS.single { it.id == "blue-whale" },
            EarthSpeciesCatalog.FISH.single { it.id == "common-carp" },
            EarthSpeciesCatalog.FISH.single { it.id == "atlantic-salmon" },
        )
        val ecology = EcologyCompiler.compile(definitions)
        val blueWhale = ecology.species.single { it.id == "blue-whale" }
        val carp = ecology.species.single { it.id == "common-carp" }
        val salmon = ecology.species.single { it.id == "atlantic-salmon" }

        assertEquals(
            AquaticSalinityTolerance.SALTWATER_ONLY,
            blueWhale.physiology.respiration.salinityTolerance,
            message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `blueWhale.physiology.respiration.salinityTolerance` to match `AquaticSalinityTolerance.SALTWATER_ONLY`"
        )
        assertEquals(0.0, blueWhale.niche.supportFor(Habitat.FRESHWATER), message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `blueWhale.niche.supportFor(Habitat.FRESHWATER)` to match `0.0`")
        assertTrue(blueWhale.niche.supportFor(Habitat.SHALLOW_OCEAN) > 0.0, message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `blueWhale.niche.supportFor(Habitat.SHALLOW_OCEAN) > 0.0` to be true")

        assertEquals(
            AquaticSalinityTolerance.FRESHWATER_ONLY,
            carp.physiology.respiration.salinityTolerance,
            message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `carp.physiology.respiration.salinityTolerance` to match `AquaticSalinityTolerance.FRESHWATER_ONLY`"
        )
        assertTrue(carp.niche.supportFor(Habitat.FRESHWATER) > 0.0, message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `carp.niche.supportFor(Habitat.FRESHWATER) > 0.0` to be true")
        assertEquals(0.0, carp.niche.supportFor(Habitat.SHALLOW_OCEAN), message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `carp.niche.supportFor(Habitat.SHALLOW_OCEAN)` to match `0.0`")

        assertEquals(
            AquaticSalinityTolerance.BROAD,
            salmon.physiology.respiration.salinityTolerance,
            message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `salmon.physiology.respiration.salinityTolerance` to match `AquaticSalinityTolerance.BROAD`"
        )
        assertTrue(salmon.niche.supportFor(Habitat.FRESHWATER) > 0.0, message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `salmon.niche.supportFor(Habitat.FRESHWATER) > 0.0` to be true")
        assertTrue(salmon.niche.supportFor(Habitat.SHALLOW_OCEAN) > 0.0, message = "Marine freshwater and euryhaline species compile to distinct water chemistry: expected `salmon.niche.supportFor(Habitat.SHALLOW_OCEAN) > 0.0` to be true")
    }

    @Test
    fun `orca echolocation does not create an aerial land niche`() {
        val orca = EcologyCompiler.compile(
            listOf(EarthSpeciesCatalog.MAMMALS.single { it.id == "orca" }),
        ).species.single()
        val land = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 18.0,
            insolation = 0.8,
            precipitationMm = 900.0,
            isLand = true,
        )

        assertEquals(0.0, orca.niche.supportFor(Habitat.AERIAL), message = "Orca echolocation does not create an aerial land niche: expected `orca.niche.supportFor(Habitat.AERIAL)` to match `0.0`")
        assertEquals(
            -1,
            NicheSelection.choose(
                orca,
                EcologyCompiler.compile(
                    listOf(EarthSpeciesCatalog.MAMMALS.single { it.id == "orca" }),
                ),
                land
            ),
            message = "Orca echolocation does not create an aerial land niche: expected `NicheSelection.choose( orca, EcologyCompiler.compile( listOf(EarthSpeciesCatalog.MAMMALS.single { it.id == \"orca\" }), ), land )` to match `-1`",
        )
    }

    @Test
    fun `saguaro is frost sensitive and emperor penguin is heat limited`() {
        val ecology = EcologyCompiler.compile(
            listOf(
                EarthSpeciesCatalog.PRODUCERS_AND_FUNGI.single { it.id == "saguaro-cactus" },
                EarthSpeciesCatalog.BIRDS.single { it.id == "emperor-penguin" },
            ),
        )
        val saguaro = ecology.species.single { it.id == "saguaro-cactus" }
        val penguin = ecology.species.single { it.id == "emperor-penguin" }
        val tropicalReef = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 29.0,
            annualAverageTemperatureC = 28.0,
            insolation = 0.82,
            precipitationMm = 1_800.0,
            isLand = false,
            waterDepthM = 50.0,
        )

        assertTrue(saguaro.physiology.thermal.outerLowC > 0.0, message = "Saguaro is frost sensitive and emperor penguin is heat limited: expected `saguaro.physiology.thermal.outerLowC > 0.0` to be true")
        assertTrue(EcologyFitness.thermal(penguin, tropicalReef) < 0.35, message = "Saguaro is frost sensitive and emperor penguin is heat limited: expected `EcologyFitness.thermal(penguin, tropicalReef) < 0.35` to be true")
    }

    @Test
    fun `emperor penguin cannot persist on tropical reef prey`() {
        val ecology = EcologyCompiler.compile(
            listOf(
                EarthSpeciesCatalog.BIRDS.single { it.id == "emperor-penguin" },
                InvariantSpecies.SMALL_AQUATIC_LIFE,
                InvariantSpecies.PLANKTON,
            ),
        )
        val tropicalReef = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 29.0,
            annualAverageTemperatureC = 28.0,
            insolation = 0.82,
            precipitationMm = 1_800.0,
            surfaceFertilityModifier = 0.35,
            isLand = false,
            waterDepthM = 50.0,
            reefCover = 0.75,
        )
        val community = TileCommunity()
        ecology.species.forEach { species ->
            val nicheIndex = NicheSelection.choose(species, ecology, tropicalReef)
            if (nicheIndex < 0) return@forEach
            val capacity = EcologyBiomass.carryingCapacityKg(
                species,
                ecology.niches[nicheIndex],
                tropicalReef,
            )
            community.add(species.index, nicheIndex, capacity * 0.50)
        }

        val runtime = EcologyRuntime(ecology)
        repeat(4_000) {
            runtime.advanceSeason(community, tropicalReef)
        }

        assertEquals(-1, community.find(ecology.speciesIndex("emperor-penguin")), message = "Emperor penguin cannot persist on tropical reef prey: expected `community.find(ecology.speciesIndex(\"emperor-penguin\"))` to match `-1`")
    }

    @Test
    fun `catalog is broad unique and compiler-valid`() {
        val definitions = EarthSpeciesCatalog.ALL
        println(
            "EARTH_SPECIES_CATALOG total=${definitions.size} traits=${CommonTrait.entries.size} " +
                "mammals=${EarthSpeciesCatalog.MAMMALS.size} extinct=${EarthSpeciesCatalog.EXTINCT_SPECIES.size} " +
                "birds=${EarthSpeciesCatalog.BIRDS.size} reptiles_amphibians=${EarthSpeciesCatalog.REPTILES_AND_AMPHIBIANS.size} " +
                "fish=${EarthSpeciesCatalog.FISH.size} invertebrates=${EarthSpeciesCatalog.INVERTEBRATES.size} " +
                "producers_fungi=${EarthSpeciesCatalog.PRODUCERS_AND_FUNGI.size}",
        )

        assertTrue(definitions.size >= 140, message = "Catalog is broad unique and compiler-valid: expected `definitions.size >= 140` to be true")
        assertEquals(definitions.size, definitions.map { it.id }.distinct().size, message = "Catalog is broad unique and compiler-valid: expected `definitions.map { it.id }.distinct().size` to match `definitions.size`")
        assertEquals(definitions.size, definitions.map { it.displayName }.distinct().size, message = "Catalog is broad unique and compiler-valid: expected `definitions.map { it.displayName }.distinct().size` to match `definitions.size`")
        assertTrue(EarthSpeciesCatalog.MAMMALS.size >= 40, message = "Catalog is broad unique and compiler-valid: expected `EarthSpeciesCatalog.MAMMALS.size >= 40` to be true")
        assertTrue(EarthSpeciesCatalog.EXTINCT_SPECIES.size >= 12, message = "Catalog is broad unique and compiler-valid: expected `EarthSpeciesCatalog.EXTINCT_SPECIES.size >= 12` to be true")
        assertTrue(EarthSpeciesCatalog.BIRDS.size >= 20, message = "Catalog is broad unique and compiler-valid: expected `EarthSpeciesCatalog.BIRDS.size >= 20` to be true")
        assertTrue(EarthSpeciesCatalog.REPTILES_AND_AMPHIBIANS.size >= 15, message = "Catalog is broad unique and compiler-valid: expected `EarthSpeciesCatalog.REPTILES_AND_AMPHIBIANS.size >= 15` to be true")
        assertTrue(EarthSpeciesCatalog.FISH.size >= 18, message = "Catalog is broad unique and compiler-valid: expected `EarthSpeciesCatalog.FISH.size >= 18` to be true")
        assertTrue(EarthSpeciesCatalog.INVERTEBRATES.size >= 24, message = "Catalog is broad unique and compiler-valid: expected `EarthSpeciesCatalog.INVERTEBRATES.size >= 24` to be true")
        assertTrue(EarthSpeciesCatalog.PRODUCERS_AND_FUNGI.size >= 20, message = "Catalog is broad unique and compiler-valid: expected `EarthSpeciesCatalog.PRODUCERS_AND_FUNGI.size >= 20` to be true")

        val ecology = EcologyCompiler.compile(definitions)
        ecology.species.forEach { species ->
            assertTrue(
                species.niche.hasViableNiche(),
                "${species.displayName} has no supported niche",
            )
        }
    }

    @Test
    fun `catalog covers recognizable organism archetypes`() {
        val ids = (EarthSpeciesCatalog.ALL + EarthSpeciesCatalog.EXTINCT_SPECIES)
            .mapTo(hashSetOf()) { it.id }
        val expected = setOf(
            "african-elephant",
            "african-lion",
            "blue-whale",
            "bald-eagle",
            "emperor-penguin",
            "nile-crocodile",
            "king-cobra",
            "poison-dart-frog",
            "great-white-shark",
            "ocellaris-clownfish",
            "common-octopus",
            "western-honey-bee",
            "orb-weaver-spider",
            "staghorn-coral",
            "english-oak",
            "giant-kelp",
            "field-mushroom",
            "tyrannosaurus-rex",
            "woolly-mammoth",
            "trilobite",
        )

        assertTrue(ids.containsAll(expected), "Missing ${expected - ids}")
    }

    @Test
    fun `photosynthetic method does not make land plants aquatic or kelp terrestrial`() {
        val ecology = EcologyCompiler.compile(EarthSpeciesCatalog.ALL)
        val baobab = ecology.species.single { it.id == "african-baobab" }
        val kelp = ecology.species.single { it.id == "giant-kelp" }

        assertEquals(0.0, baobab.niche.supportFor(Habitat.SHALLOW_OCEAN), message = "Photosynthetic method does not make land plants aquatic or kelp terrestrial: expected `baobab.niche.supportFor(Habitat.SHALLOW_OCEAN)` to match `0.0`")
        assertEquals(0.0, baobab.niche.supportFor(Habitat.DARK_WATER), message = "Photosynthetic method does not make land plants aquatic or kelp terrestrial: expected `baobab.niche.supportFor(Habitat.DARK_WATER)` to match `0.0`")
        assertEquals(0.0, kelp.niche.supportFor(Habitat.LAND_SURFACE), message = "Photosynthetic method does not make land plants aquatic or kelp terrestrial: expected `kelp.niche.supportFor(Habitat.LAND_SURFACE)` to match `0.0`")
        assertEquals(0.0, kelp.niche.supportFor(Habitat.CANOPY), message = "Photosynthetic method does not make land plants aquatic or kelp terrestrial: expected `kelp.niche.supportFor(Habitat.CANOPY)` to match `0.0`")
        assertTrue(kelp.niche.supportFor(Habitat.SHALLOW_OCEAN) > 0.0, message = "Photosynthetic method does not make land plants aquatic or kelp terrestrial: expected `kelp.niche.supportFor(Habitat.SHALLOW_OCEAN) > 0.0` to be true")
    }

    private fun predationRate(
        predator: SpeciesDefinition,
        prey: SpeciesDefinition,
    ): Double {
        val ecology = EcologyCompiler.compile(listOf(predator, prey))
        val predatorIndex = ecology.speciesIndex(predator.id)
        val preyIndex = ecology.speciesIndex(prey.id)
        val offset = predatorIndex * ecology.species.size + preyIndex
        assertEquals(
            InteractionKind.PREDATION.ordinal,
            ecology.interactions.kindAt(offset),
            message = "Photosynthetic method does not make land plants aquatic or kelp terrestrial: expected `ecology.interactions.kindAt(offset)` to match `InteractionKind.PREDATION.ordinal`"
        )
        return ecology.interactions.targetLossAt(offset)
    }
}
