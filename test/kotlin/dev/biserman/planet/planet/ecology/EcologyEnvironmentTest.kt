package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EcologyEnvironmentTest {
    @Test
    fun `planet adapter preserves signed elevation`() {
        assertEquals(
            -750.0,
            PlanetEcologyEnvironment.signedElevationM(-750.0),
            "Below-sea-level elevations must remain negative when adapted for ecology",
        )
        assertEquals(
            4_500.0,
            PlanetEcologyEnvironment.signedElevationM(4_500.0),
            "Above-sea-level elevations must retain their height when adapted for ecology",
        )
    }

    @Test
    fun `major rivers expose freshwater and increase land water availability`() {
        val dry = land(adjacentToMajorRiver = 0.0)
        val partialRiver = land(adjacentToMajorRiver = 0.25)
        val river = land(adjacentToMajorRiver = 1.0)

        assertEquals(
            0.0,
            dry.habitatAvailability(Habitat.FRESHWATER),
            "Land without a major-river edge must not expose freshwater habitat",
        )
        assertEquals(
            0.105,
            partialRiver.habitatAvailability(Habitat.FRESHWATER),
            1e-12,
            "A 25% major-river edge should expose 25% of the full freshwater habitat",
        )
        assertEquals(
            0.42,
            river.habitatAvailability(Habitat.FRESHWATER),
            1e-12,
            "A full major-river edge should expose the configured freshwater habitat amount",
        )
        assertTrue(
            river.waterAvailability > dry.waterAvailability,
            "A major river should increase water availability: dry=${dry.waterAvailability}, river=${river.waterAvailability}",
        )
        assertEquals(
            (river.waterAvailability - dry.waterAvailability) * 0.25,
            partialRiver.waterAvailability - dry.waterAvailability,
            1e-12,
            "The river moisture bonus should scale linearly with shared-edge fraction",
        )
    }

    @Test
    fun `shared coastline fraction scales coastal habitat`() {
        val land = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 20.0,
            insolation = 0.8,
            precipitationMm = 35.0,
            isLand = true,
            adjacentToOcean = 0.25,
        )
        val ocean = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 20.0,
            insolation = 0.8,
            precipitationMm = 35.0,
            isLand = false,
            adjacentToLand = 0.25,
        )

        assertEquals(
            0.12,
            land.habitatAvailability(Habitat.COASTAL),
            1e-12,
            "Land coastal habitat should scale with the 25% ocean-edge fraction",
        )
        assertEquals(
            0.25,
            ocean.habitatAvailability(Habitat.COASTAL),
            1e-12,
            "Ocean coastal habitat should scale with the 25% land-edge fraction",
        )
        assertEquals(0.25, land.adjacentToOcean, "Land should preserve its ocean-edge fraction")
        assertEquals(0.25, ocean.adjacentToLand, "Ocean should preserve its land-edge fraction")
    }

    @Test
    fun `water depth and useful light select aquatic compartments`() {
        val shallow = ocean(waterDepthM = 40.0, usefulSunlightReachesWater = true)
        val deep = ocean(waterDepthM = 900.0, usefulSunlightReachesWater = true)
        val darkSurface = ocean(waterDepthM = 40.0, usefulSunlightReachesWater = false)

        assertTrue(
            shallow.habitatAvailability(Habitat.SUNLIT_WATER) > 0.0,
            "A shallow illuminated ocean should expose sunlit-water habitat",
        )
        assertEquals(
            0.0,
            shallow.habitatAvailability(Habitat.DARK_WATER),
            "A shallow ocean should not expose dark-water habitat",
        )
        assertTrue(
            deep.habitatAvailability(Habitat.DARK_WATER) > 0.0,
            "A 900 m ocean should expose dark-water habitat",
        )
        assertEquals(
            0.0,
            darkSurface.habitatAvailability(Habitat.SUNLIT_WATER),
            "Water receiving no useful starlight should not expose sunlit-water habitat",
        )
        assertTrue(
            darkSurface.habitatAvailability(Habitat.DARK_WATER) > 0.0,
            "Water receiving no useful starlight should expose dark-water habitat even when shallow",
        )
    }

    @Test
    fun `hard-coded pigments favor different star colors`() {
        val greenAtYellow = LightColorModel.photosyntheticMatch(StarLight.YELLOW, BiologicalColor.GREEN)
        val redAtYellow = LightColorModel.photosyntheticMatch(StarLight.YELLOW, BiologicalColor.RED)
        val greenAtRed = LightColorModel.photosyntheticMatch(StarLight.RED, BiologicalColor.GREEN)
        val redAtRed = LightColorModel.photosyntheticMatch(StarLight.RED, BiologicalColor.RED)

        assertTrue(
            greenAtYellow > redAtYellow,
            "Yellow starlight should favor green over red pigments: green=$greenAtYellow, red=$redAtYellow",
        )
        assertTrue(
            redAtRed > greenAtRed,
            "Red starlight should favor red over green pigments: red=$redAtRed, green=$greenAtRed",
        )
    }

    @Test
    fun `authored light table covers every strongly typed star`() {
        assertEquals(
            StarLight.entries.toSet(),
            LightColorModel.authoredCompatibility.keys,
            "Every StarLight needs an authored photosynthetic compatibility table; " +
                "expected=${StarLight.entries.toSet()}, actual=${LightColorModel.authoredCompatibility.keys}",
        )
    }

    @Test
    fun `adding a biological color requires an explicit light compatibility`() {
        val expectedColors = BiologicalColor.entries.toSet()
        LightColorModel.authoredCompatibility.forEach { (starLight, compatibility) ->
            assertEquals(
                expectedColors,
                compatibility.byPigment.keys,
                "$starLight must explicitly cover every BiologicalColor; " +
                    "expected=$expectedColors, actual=${compatibility.byPigment.keys}",
            )
        }
    }

    @Test
    fun `pale matches open ground while white matches snow`() {
        val paleOpen = Habitat.LAND_SURFACE.camouflageMatch(
            BiologicalColor.PALE,
            snowOrIce = false,
            canopyCover = 0.1,
            reefCover = 0.0,
        )
        val paleForest = Habitat.LAND_SURFACE.camouflageMatch(
            BiologicalColor.PALE,
            snowOrIce = false,
            canopyCover = 0.8,
            reefCover = 0.0,
        )
        val paleSnow = Habitat.LAND_SURFACE.camouflageMatch(
            BiologicalColor.PALE,
            snowOrIce = true,
            canopyCover = 0.1,
            reefCover = 0.0,
        )
        val whiteSnow = Habitat.LAND_SURFACE.camouflageMatch(
            BiologicalColor.WHITE,
            snowOrIce = true,
            canopyCover = 0.1,
            reefCover = 0.0,
        )
        val whiteOpen = Habitat.LAND_SURFACE.camouflageMatch(
            BiologicalColor.WHITE,
            snowOrIce = false,
            canopyCover = 0.1,
            reefCover = 0.0,
        )

        assertTrue(
            paleOpen > paleForest,
            "Pale camouflage should match open ground better than forest: open=$paleOpen, forest=$paleForest",
        )
        assertTrue(
            whiteSnow > paleSnow,
            "White camouflage should match snow better than pale camouflage: white=$whiteSnow, pale=$paleSnow",
        )
        assertTrue(
            whiteSnow > whiteOpen,
            "White camouflage should match snow better than snow-free ground: snow=$whiteSnow, open=$whiteOpen",
        )
    }

    @Test
    fun `countershading matches sunlit water but not dark water`() {
        val countershadedSunlit = Habitat.SUNLIT_WATER.camouflageMatch(
            BiologicalColor.COUNTERSHADE,
            snowOrIce = false,
            canopyCover = 0.0,
            reefCover = 0.0,
        )
        val blueGreenSunlit = Habitat.SUNLIT_WATER.camouflageMatch(
            BiologicalColor.BLUE,
            snowOrIce = false,
            canopyCover = 0.0,
            reefCover = 0.0,
        )
        val countershadedDark = Habitat.DARK_WATER.camouflageMatch(
            BiologicalColor.COUNTERSHADE,
            snowOrIce = false,
            canopyCover = 0.0,
            reefCover = 0.0,
        )

        assertTrue(
            countershadedSunlit > blueGreenSunlit,
            "Countershading should outperform solid blue in sunlit water: " +
                "countershade=$countershadedSunlit, blue=$blueGreenSunlit",
        )
        assertTrue(
            countershadedSunlit > countershadedDark,
            "Countershading should work better in sunlit than dark water: " +
                "sunlit=$countershadedSunlit, dark=$countershadedDark",
        )
    }

    @Test
    fun `grazing obtains food only from modeled producer populations`() {
        assertEquals(
            0.0,
            EcoStrategy.GRAZING.resourceSupport(
                land(),
                Habitat.LAND_SURFACE,
                SizeClass.MEDIUM,
            ),
            "Grazing should receive no food from an environment without modeled producers",
        )
    }

    @Test
    fun `functional organic resources start empty`() {
        assertEquals(
            FunctionalResources(),
            land().resources,
            "A newly created land environment should not begin with carrion, detritus, waste, or marine snow",
        )
    }

    @Test
    fun `decomposition and coprophagy use their dynamic organic resources`() {
        val environment = land().withResources(
            FunctionalResources(detritus = 0.62, waste = 0.37),
        )

        assertEquals(
            0.62,
            EcoStrategy.DECOMPOSITION.resourceSupport(
                environment,
                Habitat.LAND_SURFACE,
                SizeClass.SMALL,
            ),
            "Decomposition support should equal the environment's detritus level",
        )
        assertEquals(
            0.37,
            EcoStrategy.COPROPHAGY.resourceSupport(
                environment,
                Habitat.LAND_SURFACE,
                SizeClass.SMALL,
            ),
            "Coprophagy support should equal the environment's waste level",
        )
    }

    @Test
    fun `burrow builders have an upper water limit but fossorial living does not`() {
        fun burrower(id: String, buildsBurrow: Boolean) = SpeciesDefinition(
            id = id,
            displayName = id,
            sizeClass = SizeClass.SMALL,
            motile = true,
            traits = listOfNotNull(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ECTOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.DIGGING_LIMBS,
                CommonTrait.FOSSORIAL_LIVING,
                CommonTrait.AMBUSH_MUSCULATURE,
                CommonTrait.BURROW_BUILDER.takeIf { buildsBurrow },
            ),
        )
        val ecology = EcologyCompiler.compile(
            listOf(burrower("fossorial", false), burrower("burrow-builder", true)),
        )
        val saturated = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 20.0,
            insolation = 0.7,
            precipitationMm = 10_000.0,
            isLand = true,
        )
        val fossorialFit = EcologyFitness.water(ecology.species[0], saturated, Habitat.LAND_SURFACE)
        val builderFit = EcologyFitness.water(ecology.species[1], saturated, Habitat.LAND_SURFACE)

        assertEquals(
            1.0,
            fossorialFit,
            "Fossorial living alone should not penalize an organism in saturated soil",
        )
        assertTrue(
            builderFit < fossorialFit,
            "A burrow builder should be less fit than a non-building fossorial organism in saturated soil: " +
                "builder=$builderFit, fossorial=$fossorialFit",
        )
    }

    @Test
    fun `thermal strategies affect activity fitness`() {
        fun grazer(id: String, thermalTrait: CommonTrait) = SpeciesDefinition(
            id = id,
            displayName = id,
            sizeClass = SizeClass.MEDIUM,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                thermalTrait,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
            ),
        )
        val ecology = EcologyCompiler.compile(
            listOf(
                grazer("ectotherm", CommonTrait.ECTOTHERMY),
                grazer("endotherm", CommonTrait.ENDOTHERMY),
            ),
        )
        val cold = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 4.0,
            insolation = 0.5,
            precipitationMm = 60.0,
            isLand = true,
        )

        assertTrue(
            EcologyFitness.thermal(ecology.species[1], cold) >
                EcologyFitness.thermal(ecology.species[0], cold),
            "At 4°C, the endotherm should retain more thermal activity than the ectotherm: " +
                "endotherm=${EcologyFitness.thermal(ecology.species[1], cold)}, " +
                "ectotherm=${EcologyFitness.thermal(ecology.species[0], cold)}",
        )
    }

    @Test
    fun `seasonal coat responds to low insolation rather than cold alone`() {
        val coated = SpeciesDefinition(
            id = "coated",
            displayName = "Coated grazer",
            sizeClass = SizeClass.MEDIUM,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.VIVIPARITY,
                CommonTrait.WALKING_LIMBS,
                CommonTrait.GRAZING_MOUTHPARTS,
                CommonTrait.FUR,
                CommonTrait.SEASONAL_WINTER_COAT,
            ),
        )
        val species = EcologyCompiler.compile(listOf(coated)).species.single()
        val winterFitness = EcologyFitness.seasonalTemperature(species, -5.0, insolation = 0.18)
        val brightColdFitness = EcologyFitness.seasonalTemperature(species, -5.0, insolation = 0.85)

        assertTrue(
            winterFitness > brightColdFitness,
            "A seasonal coat should provide more protection during dim winter conditions than equally cold bright conditions: " +
                "dim=$winterFitness, bright=$brightColdFitness",
        )
    }

    @Test
    fun `colony thermoregulation buffers dim winters and hot summers`() {
        val bee = EarthSpeciesCatalog.ALL.single { it.id == "western-honey-bee" }
        val unregulated = bee.copy(
            id = "unregulated-honey-bee",
            traits = bee.traits - CommonTrait.COLONY_THERMOREGULATION,
        )
        val compiled = EcologyCompiler.compile(listOf(bee, unregulated)).species
        val regulated = compiled[0]
        val ordinary = compiled[1]
        val hotTemperatureC =
            (regulated.physiology.thermal.outerHighC + ordinary.physiology.thermal.outerHighC) / 2.0

        assertTrue(
            EcologyFitness.seasonalTemperature(regulated, 0.0, insolation = 0.15) >
                EcologyFitness.seasonalTemperature(ordinary, 0.0, insolation = 0.15),
            "Colony thermoregulation should improve fitness during a dim 0°C season",
        )
        assertTrue(
            EcologyFitness.temperature(regulated, hotTemperatureC) >
                EcologyFitness.temperature(ordinary, hotTemperatureC),
            "Colony thermoregulation should improve fitness at a temperature between the colonies' upper lethal limits: " +
                "temperature=$hotTemperatureC, regulated=${EcologyFitness.temperature(regulated, hotTemperatureC)}, " +
                "ordinary=${EcologyFitness.temperature(ordinary, hotTemperatureC)}",
        )
        assertTrue(
            regulated.physiology.maintenanceDemand > ordinary.physiology.maintenanceDemand,
            "Colony thermoregulation should cost maintenance: regulated=${regulated.physiology.maintenanceDemand}, " +
                "ordinary=${ordinary.physiology.maintenanceDemand}",
        )
        assertTrue(
            regulated.physiology.hydration.minimumWater > ordinary.physiology.hydration.minimumWater,
            "Colony thermoregulation should increase minimum water demand: " +
                "regulated=${regulated.physiology.hydration.minimumWater}, " +
                "ordinary=${ordinary.physiology.hydration.minimumWater}",
        )
    }

    @Test
    fun `terrestrial motile elevation fitness declines outside a shifted optimal band`() {
        fun organism(
            id: String,
            motile: Boolean,
            locomotion: CommonTrait,
            altitudeTrait: CommonTrait? = null,
        ) = SpeciesDefinition(
            id = id,
            displayName = id,
            sizeClass = SizeClass.MEDIUM,
            motile = motile,
            traits = listOfNotNull(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.ENDOTHERMY.takeIf { motile },
                CommonTrait.SOLITARY.takeIf { motile },
                locomotion,
                CommonTrait.GRAZING_MOUTHPARTS.takeIf { motile },
                CommonTrait.PHOTOSYNTHETIC_SURFACE.takeIf { !motile },
                ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS.takeIf { !motile },
                altitudeTrait,
            ),
        )
        val ecology = EcologyCompiler.compile(
            listOf(
                organism("lowland-grazer", true, CommonTrait.WALKING_LIMBS),
                organism(
                    "highland-grazer",
                    true,
                    CommonTrait.WALKING_LIMBS,
                    CommonTrait.HIGH_AFFINITY_BLOOD,
                ),
                organism("flying-grazer", true, CommonTrait.WINGS),
                organism("rooted-producer", false, CommonTrait.ROOTED_BODY),
            ),
        )
        val lowland = ecology.species[0]
        val highland = ecology.species[1]
        val flying = ecology.species[2]
        val rooted = ecology.species[3]

        fun assertElevation(
            expected: Double,
            species: CompiledSpecies,
            elevationM: Double,
            habitat: Habitat,
        ) {
            val actual = EcologyFitness.elevation(species, land(elevationM = elevationM), habitat)
            assertEquals(
                expected,
                actual,
                "${species.displayName} should have elevation fitness $expected at ${elevationM}m in $habitat, but had $actual",
            )
        }

        assertElevation(0.0, lowland, -1_000.0, Habitat.LAND_SURFACE)
        assertElevation(0.5, lowland, -500.0, Habitat.LAND_SURFACE)
        assertElevation(1.0, lowland, 0.0, Habitat.LAND_SURFACE)
        assertElevation(1.0, lowland, 2_000.0, Habitat.LAND_SURFACE)
        assertElevation(0.5, lowland, 2_500.0, Habitat.LAND_SURFACE)
        assertElevation(0.0, lowland, 3_000.0, Habitat.LAND_SURFACE)
        assertElevation(0.0, highland, 1_500.0, Habitat.LAND_SURFACE)
        assertElevation(0.5, highland, 2_000.0, Habitat.LAND_SURFACE)
        assertElevation(1.0, highland, 2_500.0, Habitat.LAND_SURFACE)
        assertElevation(1.0, highland, 4_500.0, Habitat.LAND_SURFACE)
        assertElevation(0.5, highland, 5_000.0, Habitat.LAND_SURFACE)
        assertElevation(0.0, highland, 5_500.0, Habitat.LAND_SURFACE)
        assertElevation(1.0, flying, 6_000.0, Habitat.AERIAL)
        assertElevation(1.0, rooted, 6_000.0, Habitat.LAND_SURFACE)
    }

    @Test
    fun `snow licking supplies water only when snow or ice is present`() {
        val yak = EcologyCompiler.compile(
            listOf(EarthSpeciesCatalog.MAMMALS.single { it.id == "wild-yak" }),
        ).species.single()
        val frozen = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = -8.0,
            annualAverageTemperatureC = -2.0,
            insolation = 0.5,
            precipitationMm = 2.0,
            isLand = true,
        )
        val thawed = SeasonalCellEnvironment.create(
            areaKm2 = 40_000.0,
            temperatureC = 8.0,
            annualAverageTemperatureC = 8.0,
            insolation = 0.7,
            precipitationMm = 2.0,
            isLand = true,
        )

        assertEquals(
            1.0,
            EcologyFitness.water(yak, frozen, Habitat.LAND_SURFACE),
            "Snow licking should completely meet the wild yak's water needs while snow or ice is present",
        )
        assertTrue(
            EcologyFitness.water(yak, thawed, Habitat.LAND_SURFACE) < 1.0,
            "Snow licking should not meet the wild yak's water needs after the environment thaws",
        )
    }

    @Test
    fun `competition can make a locally secondary niche the best establishment choice`() {
        val definition = SpeciesDefinition(
            id = "branch-mat",
            displayName = "Branch mat",
            sizeClass = SizeClass.SMALL,
            motile = false,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.PHOTOSYNTHETIC_SURFACE,
                CommonTrait.ROOTED_BODY,
                CommonTrait.CANOPY_GROWTH,
                ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            ),
        )
        val ecology = EcologyCompiler.compile(listOf(definition))
        val species = ecology.species.single()
        val environment = land(canopyCover = 0.9)
        val unopposed = NicheSelection.choose(species, ecology, environment)
        val competition = DoubleArray(ecology.niches.size)
        competition[unopposed] = 1_000.0
        val diverted = NicheSelection.choose(species, ecology, environment, competition)

        assertTrue(
            unopposed >= 0,
            "The branch mat should have at least one viable unopposed niche, but selection returned $unopposed",
        )
        assertTrue(
            diverted >= 0,
            "The branch mat should retain a viable niche after its preferred niche is occupied, but selection returned $diverted",
        )
        assertTrue(
            diverted != unopposed,
            "Strong competition in niche $unopposed should divert establishment to another niche, but both choices were $diverted",
        )
    }

    @Test
    fun `radiation selects an intrinsic niche instead of escaping into an empty fallback`() {
        val definition = SpeciesDefinition(
            id = "branch-mat",
            displayName = "Branch mat",
            sizeClass = SizeClass.SMALL,
            motile = false,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.PHOTOSYNTHETIC_SURFACE,
                CommonTrait.ROOTED_BODY,
                CommonTrait.CANOPY_GROWTH,
                ColorTrait.GREEN_PHOTOSYNTHETIC_PIGMENTS,
            ),
        )
        val ecology = EcologyCompiler.compile(listOf(definition))
        val species = ecology.species.single()
        val environment = land(canopyCover = 0.9)
        val intrinsicBest = NicheSelection.choose(species, ecology, environment)
        val competition = DoubleArray(ecology.niches.size)
        competition[intrinsicBest] = 1_000_000.0

        val radiationChoice = NicheSelection.choose(
            species = species,
            ecology = ecology,
            environment = environment,
            competitionByNiche = competition,
            minimumRelativeIntrinsicFit = 0.80,
            competitionAffectsSelection = false,
        )

        assertEquals(
            intrinsicBest,
            radiationChoice,
            "Radiation should ignore local competition and retain the intrinsically best niche; " +
                "intrinsic=$intrinsicBest, selected=$radiationChoice",
        )
    }

    @Test
    fun `a valid scavenging niche can establish before the first carrion flux`() {
        val definition = SpeciesDefinition(
            id = "obligate-scavenger",
            displayName = "Obligate scavenger",
            sizeClass = SizeClass.MEDIUM,
            motile = true,
            traits = listOf(
                CommonTrait.TEMPERATE_BIOCHEMISTRY,
                CommonTrait.ENDOTHERMY,
                CommonTrait.SOLITARY,
                CommonTrait.TERRESTRIAL_OVOSPORE,
                CommonTrait.WINGS,
                CommonTrait.SCAVENGING_SENSES,
            ),
        )
        val ecology = EcologyCompiler.compile(listOf(definition))
        val environment = land().withResources(
            FunctionalResources(carrion = 0.0),
        )

        val nicheIndex = NicheSelection.choose(ecology.species.single(), ecology, environment)

        assertTrue(
            nicheIndex >= 0,
            "An obligate scavenger should be able to establish before carrion is first produced, but selection returned $nicheIndex",
        )
        assertEquals(
            EcoStrategy.SCAVENGING,
            ecology.niches[nicheIndex].strategy,
            "The obligate scavenger should establish as a scavenger, not ${ecology.niches[nicheIndex].strategy}",
        )
        assertEquals(
            Habitat.AERIAL,
            ecology.niches[nicheIndex].habitat,
            "The winged scavenger should establish in the aerial habitat, not ${ecology.niches[nicheIndex].habitat}",
        )
    }

    @Test
    fun `ordinary flight does not establish over open ocean`() {
        val ecology = EcologyCompiler.compile(
            listOf(
                EarthSpeciesCatalog.ALL.first { it.id == "bald-eagle" },
                EarthSpeciesCatalog.ALL.first { it.id == "brown-pelican" },
                EarthSpeciesCatalog.ALL.first { it.id == "wandering-albatross" },
            ),
        )
        val openOcean = ocean(waterDepthM = 80.0, usefulSunlightReachesWater = true)

        assertEquals(
            -1,
            NicheSelection.choose(ecology.species[0], ecology, openOcean),
            "A bald eagle's ordinary flight should not permit residence over open ocean",
        )
        assertEquals(
            -1,
            NicheSelection.choose(ecology.species[1], ecology, openOcean),
            "A brown pelican's ordinary flight and coastal habits should not permit residence over open ocean",
        )
        val albatrossNiche = NicheSelection.choose(ecology.species[2], ecology, openOcean)
        assertTrue(
            albatrossNiche >= 0,
            "A wandering albatross should find a viable niche over open ocean, but selection returned $albatrossNiche",
        )
        assertEquals(
            Habitat.AERIAL,
            ecology.niches[albatrossNiche].habitat,
            "A wandering albatross over open ocean should occupy the aerial habitat",
        )
        assertTrue(
            ecology.species[2].environment.pelagicAerialResident,
            "The compiled wandering albatross should be marked as a pelagic aerial resident",
        )
    }

    @Test
    fun `dark water requires an explicit depth adaptation`() {
        val ecology = EcologyCompiler.compile(
            listOf(
                EarthSpeciesCatalog.ALL.first { it.id == "ocellaris-clownfish" },
                EarthSpeciesCatalog.ALL.first { it.id == "deep-sea-anglerfish" },
            ),
        )
        val darkOcean = ocean(waterDepthM = 900.0, usefulSunlightReachesWater = false)

        assertEquals(
            -1,
            NicheSelection.choose(ecology.species[0], ecology, darkOcean),
            "A clownfish without a depth adaptation should not establish in a dark 900 m ocean",
        )
        val anglerfishNiche = NicheSelection.choose(ecology.species[1], ecology, darkOcean)
        assertTrue(
            anglerfishNiche >= 0,
            "A deep-sea anglerfish should find a viable niche in a dark 900 m ocean, but selection returned $anglerfishNiche",
        )
        assertEquals(
            Habitat.DARK_WATER,
            ecology.niches[anglerfishNiche].habitat,
            "A deep-sea anglerfish in an unlit 900 m ocean should occupy dark water",
        )
        assertTrue(
            ecology.species[1].environment.darkWaterAdapted,
            "The compiled deep-sea anglerfish should retain its authored dark-water adaptation",
        )
    }

    private fun land(
        adjacentToMajorRiver: Double = 0.0,
        canopyCover: Double = 0.0,
        elevationM: Double = 0.0,
    ) = SeasonalCellEnvironment.create(
        areaKm2 = 40_000.0,
        temperatureC = 22.0,
        insolation = 0.8,
        precipitationMm = 35.0,
        isLand = true,
        adjacentToMajorRiver = adjacentToMajorRiver,
        canopyCover = canopyCover,
        elevationM = elevationM,
        resources = FunctionalResources(),
    )

    private fun ocean(
        waterDepthM: Double,
        usefulSunlightReachesWater: Boolean,
    ) = SeasonalCellEnvironment.create(
        areaKm2 = 40_000.0,
        temperatureC = 18.0,
        insolation = 0.8,
        precipitationMm = 60.0,
        isLand = false,
        waterDepthM = waterDepthM,
        usefulSunlightReachesWater = usefulSunlightReachesWater,
    )
}
