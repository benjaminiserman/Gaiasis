package dev.biserman.planet.planet.ecology

import com.fasterxml.jackson.module.kotlin.readValue
import dev.biserman.planet.utils.Serialization
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class EcologyGlobalsTest {
    @Test
    fun `checked in ecology config loads into the reloadable globals`() {
        val configFile = File("config/ecology_config.json")
        val checkedIn = Serialization.configMapper.readTree(configFile)
        val originalGlobals = Serialization.configMapper.valueToTree<com.fasterxml.jackson.databind.JsonNode>(
            EcologyGlobals,
        )
        try {
            // Exercise the same strict deserialization path as the in-game
            // refresh button, so a JSON property without a matching compiled
            // EcologyGlobals setter fails during tests rather than at runtime.
            Serialization.configMapper.readValue<EcologyGlobals>(configFile)
            EcologyGlobals.validate()
            val loadedGlobals = Serialization.configMapper.valueToTree<com.fasterxml.jackson.databind.JsonNode>(
                EcologyGlobals,
            )

            assertEquals(checkedIn, loadedGlobals)
        } finally {
            Serialization.configMapper.readValue<EcologyGlobals>(originalGlobals.toString())
        }
    }

    @Test
    fun `reloaded globals are captured by newly constructed runtime configs`() {
        val original = EcologyGlobals.backgroundMortality
        val originalFeeding = EcologyGlobals.feedingInterferenceCompetition
        val existingSnapshot = EcologyRuntimeConfig()
        try {
            Serialization.configMapper.readValue<EcologyGlobals>(
                """{"backgroundMortality":0.123,"feedingInterferenceCompetition":0.4}""",
            )
            EcologyGlobals.validate()

            assertEquals(originalFeeding, existingSnapshot.feedingInterferenceCompetition)
            assertEquals(0.4, EcologyRuntimeConfig().feedingInterferenceCompetition)
            assertEquals(original, existingSnapshot.backgroundMortality, message = "Reloaded globals are captured by newly constructed runtime configs: expected `existingSnapshot.backgroundMortality` to match `original`")
            assertEquals(0.123, EcologyRuntimeConfig().backgroundMortality, message = "Reloaded globals are captured by newly constructed runtime configs: expected `EcologyRuntimeConfig().backgroundMortality` to match `0.123`")
        } finally {
            EcologyGlobals.backgroundMortality = original
            EcologyGlobals.feedingInterferenceCompetition = originalFeeding
        }
    }

    @Test
    fun `star light can be loaded from ecology config`() {
        val original = EcologyGlobals.starLight
        try {
            Serialization.configMapper.readValue<EcologyGlobals>(
                """{"starLight":"RED"}""",
            )

            assertEquals(StarLight.RED, EcologyGlobals.starLight, message = "Star light can be loaded from ecology config: expected `EcologyGlobals.starLight` to match `StarLight.RED`")
        } finally {
            EcologyGlobals.starLight = original
        }
    }

    @Test
    fun `refreshing runtime configuration replaces the live snapshot and invalidates caches`() {
        val originalRevision = PlanetEcology.runtimeConfigRevision
        val originalMortality = EcologyGlobals.backgroundMortality
        try {
            EcologyGlobals.backgroundMortality = 0.123

            PlanetEcology.refreshRuntimeConfig()

            assertEquals(0.123, PlanetEcology.currentRuntimeConfig().backgroundMortality)
            assertEquals(originalRevision + 1, PlanetEcology.runtimeConfigRevision)
        } finally {
            EcologyGlobals.backgroundMortality = originalMortality
            PlanetEcology.refreshRuntimeConfig()
        }
    }
}
