package dev.biserman.planet.planet

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RiverNetworkTest {
    @Test
    fun `upstream segment counts accumulate tributaries downstream`() {
        val firstTributary = "a" to "confluence"
        val secondTributary = "b" to "confluence"
        val joinedRiver = "confluence" to "downstream"
        val lowerRiver = "downstream" to "mouth"

        val counts = upstreamSegmentCounts(
            listOf(firstTributary, secondTributary, joinedRiver, lowerRiver)
        )

        assertEquals(0, counts[firstTributary], "Upstream segment counts accumulate tributaries downstream: expected `counts[firstTributary]` to match `0`")
        assertEquals(0, counts[secondTributary], "Upstream segment counts accumulate tributaries downstream: expected `counts[secondTributary]` to match `0`")
        assertEquals(2, counts[joinedRiver], "Upstream segment counts accumulate tributaries downstream: expected `counts[joinedRiver]` to match `2`")
        assertEquals(3, counts[lowerRiver], "Upstream segment counts accumulate tributaries downstream: expected `counts[lowerRiver]` to match `3`")
    }
}