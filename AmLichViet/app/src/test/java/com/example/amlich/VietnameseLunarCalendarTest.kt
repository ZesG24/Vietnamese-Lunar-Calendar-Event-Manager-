package com.example.amlich

import com.example.amlich.core.VietnameseLunarCalendar
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class VietnameseLunarCalendarTest {

    @Test
    fun testLunarDateConversion() {
        // Lunar New Year 2024 (Mùng 1 Tết Giáp Thìn): 10/02/2024 Solar
        val date = LocalDate.of(2024, 2, 10)
        val lunar = VietnameseLunarCalendar.getLunarDate(date)

        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2024, lunar.year)
        assertEquals("Giáp", lunar.canYear)
        assertEquals("Thìn", lunar.chiYear)
    }

    @Test
    fun testSolarFromLunarConversion() {
        // Find Solar date for Lunar 1/1 in 2024
        val solarDate = VietnameseLunarCalendar.getSolarDateFromLunar(1, 1, 2024)
        assertEquals(LocalDate.of(2024, 2, 10), solarDate)
    }
}
