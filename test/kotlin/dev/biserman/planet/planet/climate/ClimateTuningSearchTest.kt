package dev.biserman.planet.planet.climate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ClimateTuningSearchTest {
    @Test
    fun `coordinate search improves and shrinks its step`() {
        val evaluations = mutableListOf<ClimateTuningEvaluation>()
        val result = ClimateTuningSearch(
            parameters = listOf(
                ClimateTuningParameter("temperature", min = 0.0, max = 10.0, step = 2.0, minStep = 0.25),
            ),
            initialValues = mapOf("temperature" to 0.0),
            maxEvaluations = 9,
            evaluate = { values -> (values.getValue("temperature") - 3.0).let { it * it } },
            afterEvaluation = evaluations::add,
        ).run()

        assertEquals(9.0, result.initialLoss, "Coordinate search improves and shrinks its step: expected `result.initialLoss` to match `9.0`")
        assertEquals(0.0, result.bestLoss, "Coordinate search improves and shrinks its step: expected `result.bestLoss` to match `0.0`")
        assertEquals(3.0, result.bestValues.getValue("temperature"), "Coordinate search improves and shrinks its step: expected `result.bestValues.getValue(\"temperature\")` to match `3.0`")
        assertEquals(result.evaluations, evaluations.size, "Coordinate search improves and shrinks its step: expected `evaluations.size` to match `result.evaluations`")
    }

    @Test
    fun `coordinate search respects its evaluation budget and bounds`() {
        val seen = mutableListOf<Double>()
        val result = ClimateTuningSearch(
            parameters = listOf(
                ClimateTuningParameter("moisture", min = 0.0, max = 1.0, step = 0.75),
            ),
            initialValues = mapOf("moisture" to 0.5),
            maxEvaluations = 2,
            evaluate = { values ->
                values.getValue("moisture").also(seen::add)
            },
        ).run()

        assertEquals(2, result.evaluations, "Coordinate search respects its evaluation budget and bounds: expected `result.evaluations` to match `2`")
        assertEquals(2, seen.size, "Coordinate search respects its evaluation budget and bounds: expected `seen.size` to match `2`")
        assertTrue(seen.all { it in 0.0..1.0 }, "Coordinate search respects its evaluation budget and bounds: expected `seen.all { it in 0.0..1.0 }` to be true")
    }

    @Test
    fun `interaction trials find improvements hidden from coordinate moves`() {
        val result = ClimateTuningSearch(
            parameters = listOf(
                ClimateTuningParameter("temperature", min = -1.0, max = 1.0, step = 1.0),
                ClimateTuningParameter("moisture", min = -1.0, max = 1.0, step = 1.0),
            ),
            initialValues = mapOf("temperature" to 0.0, "moisture" to 0.0),
            maxEvaluations = 9,
            interactionPairs = listOf("temperature" to "moisture"),
            evaluate = { values ->
                if (values.getValue("temperature") == 1.0 && values.getValue("moisture") == 1.0) 0.0 else 1.0
            },
        ).run()

        assertEquals(0.0, result.bestLoss, "Interaction trials find improvements hidden from coordinate moves: expected `result.bestLoss` to match `0.0`")
        assertEquals(1.0, result.bestValues.getValue("temperature"), "Interaction trials find improvements hidden from coordinate moves: expected `result.bestValues.getValue(\"temperature\")` to match `1.0`")
        assertEquals(1.0, result.bestValues.getValue("moisture"), "Interaction trials find improvements hidden from coordinate moves: expected `result.bestValues.getValue(\"moisture\")` to match `1.0`")
        assertTrue(result.evaluations <= 9, "Interaction trials find improvements hidden from coordinate moves: expected `result.evaluations <= 9` to be true")
    }
}