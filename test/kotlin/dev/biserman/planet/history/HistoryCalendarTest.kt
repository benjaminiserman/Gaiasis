package dev.biserman.planet.history

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class HistoryCalendarTest {
    @Test
    fun `four seasonal turns advance one year`() {
        assertEquals(0L, HistoryCalendar.year(0), "Four seasonal turns advance one year: expected `HistoryCalendar.year(0)` to match `0L`")
        assertEquals(0L, HistoryCalendar.year(3), "Four seasonal turns advance one year: expected `HistoryCalendar.year(3)` to match `0L`")
        assertEquals(1L, HistoryCalendar.year(4), "Four seasonal turns advance one year: expected `HistoryCalendar.year(4)` to match `1L`")
        assertEquals(2L, HistoryCalendar.year(8), "Four seasonal turns advance one year: expected `HistoryCalendar.year(8)` to match `2L`")
    }

    @Test
    fun `northern seasons begin with spring and repeat each year`() {
        assertEquals(Season.SPRING, HistoryCalendar.season(0, Hemisphere.NORTHERN), "Northern seasons begin with spring and repeat each year: expected `HistoryCalendar.season(0, Hemisphere.NORTHERN)` to match `Season.SPRING`")
        assertEquals(Season.SUMMER, HistoryCalendar.season(1, Hemisphere.NORTHERN), "Northern seasons begin with spring and repeat each year: expected `HistoryCalendar.season(1, Hemisphere.NORTHERN)` to match `Season.SUMMER`")
        assertEquals(Season.AUTUMN, HistoryCalendar.season(2, Hemisphere.NORTHERN), "Northern seasons begin with spring and repeat each year: expected `HistoryCalendar.season(2, Hemisphere.NORTHERN)` to match `Season.AUTUMN`")
        assertEquals(Season.WINTER, HistoryCalendar.season(3, Hemisphere.NORTHERN), "Northern seasons begin with spring and repeat each year: expected `HistoryCalendar.season(3, Hemisphere.NORTHERN)` to match `Season.WINTER`")
        assertEquals(Season.SPRING, HistoryCalendar.season(4, Hemisphere.NORTHERN), "Northern seasons begin with spring and repeat each year: expected `HistoryCalendar.season(4, Hemisphere.NORTHERN)` to match `Season.SPRING`")
    }

    @Test
    fun `southern seasons are opposite northern seasons`() {
        assertEquals(Season.AUTUMN, HistoryCalendar.season(0, Hemisphere.SOUTHERN), "Southern seasons are opposite northern seasons: expected `HistoryCalendar.season(0, Hemisphere.SOUTHERN)` to match `Season.AUTUMN`")
        assertEquals(Season.WINTER, HistoryCalendar.season(1, Hemisphere.SOUTHERN), "Southern seasons are opposite northern seasons: expected `HistoryCalendar.season(1, Hemisphere.SOUTHERN)` to match `Season.WINTER`")
        assertEquals(Season.SPRING, HistoryCalendar.season(2, Hemisphere.SOUTHERN), "Southern seasons are opposite northern seasons: expected `HistoryCalendar.season(2, Hemisphere.SOUTHERN)` to match `Season.SPRING`")
        assertEquals(Season.SUMMER, HistoryCalendar.season(3, Hemisphere.SOUTHERN), "Southern seasons are opposite northern seasons: expected `HistoryCalendar.season(3, Hemisphere.SOUTHERN)` to match `Season.SUMMER`")
    }
}