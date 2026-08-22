package dev.biserman.planet.planet.ecology

import com.fasterxml.jackson.module.kotlin.readValue
import dev.biserman.planet.utils.Serialization
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals

class EcologyGlobalsTest {
    @Test
    fun `checked in ecology config matches the reloadable globals`() {
        val configFile = File("config/ecology_config.json")
        val checkedIn = Serialization.configMapper.readTree(configFile)
        val currentGlobals = Serialization.configMapper.valueToTree<com.fasterxml.jackson.databind.JsonNode>(
            EcologyGlobals,
        )

        assertEquals(currentGlobals, checkedIn, message = "Checked in ecology config matches the reloadable globals: expected `checkedIn` to match `currentGlobals`")

        // Exercise the same strict deserialization path as the in-game
        // refresh button, so a JSON property without a matching compiled
        // EcologyGlobals setter fails during tests rather than at runtime.
        Serialization.configMapper.readValue<EcologyGlobals>(configFile)
        EcologyGlobals.validate()
    }

    @Test
    fun `reloaded globals are captured by newly constructed runtime configs`() {
        val original = EcologyGlobals.backgroundMortality
        val existingSnapshot = EcologyRuntimeConfig()
        try {
            Serialization.configMapper.readValue<EcologyGlobals>(
                """{"backgroundMortality":0.123}""",
            )
            EcologyGlobals.validate()

            assertEquals(original, existingSnapshot.backgroundMortality, message = "Reloaded globals are captured by newly constructed runtime configs: expected `existingSnapshot.backgroundMortality` to match `original`")
            assertEquals(0.123, EcologyRuntimeConfig().backgroundMortality, message = "Reloaded globals are captured by newly constructed runtime configs: expected `EcologyRuntimeConfig().backgroundMortality` to match `0.123`")
        } finally {
            EcologyGlobals.backgroundMortality = original
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
    fun `refreshing runtime configuration advances the cache revision`() {
        val originalRevision = PlanetEcology.runtimeConfigRevision

        PlanetEcology.refreshRuntimeConfig()

        assertEquals(originalRevision + 1, PlanetEcology.runtimeConfigRevision, message = "Refreshing runtime configuration advances the cache revision: expected `PlanetEcology.runtimeConfigRevision` to match `originalRevision + 1`")
    }
}
