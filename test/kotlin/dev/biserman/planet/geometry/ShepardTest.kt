package dev.biserman.planet.geometry

import godot.core.Vector3
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ShepardTest {
    @Test
    fun `interpolation preserves an exact sample`() {
        val target = Vector3(1.0, 0.0, 0.0)
        val samples = listOf(
            Vector3(0.0, 0.0, 0.0) to 0.0,
            target to 750.0,
            Vector3(2.0, 0.0, 0.0) to 1000.0,
        )

        assertEquals(750.0, Shepard.interpolate(samples, target, degree = 2.0))
    }

    @Test
    fun `degree controls inverse distance weighting`() {
        val samples = listOf(
            Vector3(0.0, 0.0, 0.0) to 0.0,
            Vector3(3.0, 0.0, 0.0) to 100.0,
        )

        assertEquals(
            20.0,
            Shepard.interpolate(samples, Vector3(1.0, 0.0, 0.0), degree = 2.0),
            1e-12,
        )
    }
}
