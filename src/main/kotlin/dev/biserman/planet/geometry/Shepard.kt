package dev.biserman.planet.geometry

import godot.core.Vector3
import kotlin.math.pow

object Shepard {
    fun interpolate(
        samples: List<Pair<Vector3, Double>>,
        target: Vector3,
        degree: Double = 2.0,
    ): Double {
        if (samples.isEmpty()) return 0.0
        require(degree.isFinite() && degree >= 0.0) { "Shepard degree must be finite and non-negative" }

        val samplesWithDistance = samples.map { sample -> sample to sample.first.distanceTo(target) }
        samplesWithDistance.firstOrNull { (_, distance) -> distance == 0.0 }
            ?.let { (sample, _) -> return sample.second }

        // Scaling every distance by the nearest distance gives the same normalized
        // weights as 1 / distance^degree without overflowing near a sample.
        val nearestDistance = samplesWithDistance.minOf { (_, distance) -> distance }
        var weightedValue = 0.0
        var totalWeight = 0.0
        for ((sample, distance) in samplesWithDistance) {
            val weight = (nearestDistance / distance).pow(degree)
            weightedValue += sample.second * weight
            totalWeight += weight
        }

        return weightedValue / totalWeight
    }
}
