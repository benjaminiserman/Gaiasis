package dev.biserman.planet.planet.climate

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class HersfeldtClassificationGraphTest {
    @Test
    fun `adjacency matrix is complete symmetric and has no isolated classifications`() {
        val graph = HersfeldtClassificationGraph
        val matrix = graph.adjacencyMatrix

        assertEquals(graph.classificationIds.sorted(), matrix.ids, "Adjacency matrix is complete symmetric and has no isolated classifications: expected `matrix.ids` to match `graph.classificationIds.sorted()`")
        assertEquals(matrix.ids.size, matrix.rows.size, "Adjacency matrix is complete symmetric and has no isolated classifications: expected `matrix.rows.size` to match `matrix.ids.size`")
        matrix.rows.forEach { assertEquals(matrix.ids.size, it.size, "Adjacency matrix is complete symmetric and has no isolated classifications: expected `it.size` to match `matrix.ids.size`") }

        matrix.ids.indices.forEach { row ->
            assertFalse(matrix.rows[row][row], "Adjacency matrix is complete symmetric and has no isolated classifications: expected `matrix.rows[row][row]` to be false")
            assertTrue(matrix.rows[row].any { it }, "Adjacency matrix is complete symmetric and has no isolated classifications: expected `matrix.rows[row].any { it }` to be true")
            matrix.ids.indices.forEach { column ->
                assertEquals(matrix.rows[row][column], matrix.rows[column][row], "Adjacency matrix is complete symmetric and has no isolated classifications: expected `matrix.rows[column][row]` to match `matrix.rows[row][column]`")
            }
        }
    }

    @Test
    fun `common classifier boundary transitions are one graph hop`() {
        val graph = HersfeldtClassificationGraph

        assertEquals(1, graph.distance("Aha", "Ada"), "Common classifier boundary transitions are one graph hop: expected `graph.distance(\"Aha\", \"Ada\")` to match `1`") // desert -> semidesert
        assertEquals(1, graph.distance("CAMa", "CAa"), "Common classifier boundary transitions are one graph hop: expected `graph.distance(\"CAMa\", \"CAa\")` to match `1`") // growth supply
        assertEquals(1, graph.distance("CEb", "CDb"), "Common classifier boundary transitions are one graph hop: expected `graph.distance(\"CEb\", \"CDb\")` to match `1`") // boreal -> temperate GDD
        assertEquals(1, graph.distance("CEb", "CFb"), "Common classifier boundary transitions are one graph hop: expected `graph.distance(\"CEb\", \"CFb\")` to match `1`") // boreal -> tundra GDD
        assertEquals(1, graph.distance("TUf", "TUfp"), "Common classifier boundary transitions are one graph hop: expected `graph.distance(\"TUf\", \"TUfp\")` to match `1`") // evaporation/monsoon
    }

    @Test
    fun `shortest path counts successive classifier conditions and is capped`() {
        val graph = HersfeldtClassificationGraph

        assertEquals(0, graph.distance("TUr", "TUr"), "Shortest path counts successive classifier conditions and is capped: expected `graph.distance(\"TUr\", \"TUr\")` to match `0`")
        assertEquals(2, graph.distance("TUr", "TUs"), "Shortest path counts successive classifier conditions and is capped: expected `graph.distance(\"TUr\", \"TUs\")` to match `2`")
        assertEquals(graph.distance("CAMa", "TUr"), graph.distance("TUr", "CAMa"), "Shortest path counts successive classifier conditions and is capped: expected `graph.distance(\"TUr\", \"CAMa\")` to match `graph.distance(\"CAMa\", \"TUr\")`")
        assertEquals(graph.MAX_SCORED_DISTANCE, graph.distance("UNKNOWN", "TUr"), "Shortest path counts successive classifier conditions and is capped: expected `graph.distance(\"UNKNOWN\", \"TUr\")` to match `graph.MAX_SCORED_DISTANCE`")
    }
}
