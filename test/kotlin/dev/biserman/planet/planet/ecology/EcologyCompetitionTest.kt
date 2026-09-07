package dev.biserman.planet.planet.ecology

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class EcologyCompetitionTest {
    @Test
    fun `exponential competition matches linear scaling at one and three species`() {
        val strength = 0.15
        fun totalCrowdingFactor(speciesCount: Int): Double {
            val competitors = (speciesCount - 1).toDouble()
            return 1.0 + competitors *
                EcologyCompetition.interspecificPressurePerCompetitor(strength, competitors)
        }

        assertEquals(1.0, totalCrowdingFactor(1), 1.0e-12)
        assertEquals(1.0 + 2.0 * strength, totalCrowdingFactor(3), 1.0e-12)
        assertTrue(totalCrowdingFactor(5) > 1.0 + 4.0 * strength)
    }
}
