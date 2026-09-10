package dev.biserman.planet.gui

import godot.core.Vector2
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class StatsGraphPlotTest {
    @Test
    fun `downsampling caps output and preserves extrema`() {
        val points = (0..<1_000).map { index ->
            val value = when (index) {
                500 -> 100.0
                501 -> -100.0
                else -> index % 10.toDouble()
            }
            Vector2(index.toDouble(), value)
        }

        val sampled = downsampleMinMax(points, 0.0, 999.0, 40)

        assertTrue(sampled.size <= 40)
        assertTrue(sampled.any { it.y == 100.0 })
        assertTrue(sampled.any { it.y == -100.0 })
        assertEquals(points.first(), sampled.first())
        assertEquals(points.last(), sampled.last())
        assertTrue(sampled.zipWithNext().all { (first, second) -> first.x < second.x })
    }

    @Test
    fun `downsampling excludes points outside visible range`() {
        val points = (0..100).map { Vector2(it.toDouble(), it.toDouble()) }

        val sampled = downsampleMinMax(points, 25.0, 75.0, 20)

        assertTrue(sampled.size <= 20)
        assertEquals(25.0, sampled.first().x)
        assertEquals(75.0, sampled.last().x)
    }
}
