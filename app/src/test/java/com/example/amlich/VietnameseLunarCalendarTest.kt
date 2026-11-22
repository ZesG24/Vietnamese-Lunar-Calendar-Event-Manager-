package com.example.amlich

import com.example.amlich.core.VietnameseLunarCalendar
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class VietnameseLunarCalendarTest {

    // -------------------------------------------------------------
    // 1. KIỂM TRA CÁC NGÀY TẾT NGUYÊN ĐÁN TRONG QUÁ KHỨ (1945 - 2020)
    // -------------------------------------------------------------

    @Test
    fun testTet1945_AtDau() {
        // Mùng 1 Tết Ất Dậu: 13/02/1945 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(1945, 2, 13))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(1945, lunar.year)
        assertEquals("Ất", lunar.canYear)
        assertEquals("Dậu", lunar.chiYear)
    }

    @Test
    fun testDienBienPhu1954() {
        // Ngày Chiến thắng Điện Biên Phủ: 07/05/1954 Solar -> 05/04/1954 Âm (Giáp Ngọ)
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(1954, 5, 7))
        assertEquals(5, lunar.day)
        assertEquals(4, lunar.month)
        assertEquals(1954, lunar.year)
        assertEquals("Giáp", lunar.canYear)
        assertEquals("Ngọ", lunar.chiYear)
    }

    @Test
    fun testTet1975_AtMao() {
        // Mùng 1 Tết Ất Mão: 11/02/1975 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(1975, 2, 11))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(1975, lunar.year)
        assertEquals("Ất", lunar.canYear)
        assertEquals("Mão", lunar.chiYear)
    }

    @Test
    fun testGiaiPhong1975() {
        // Ngày Giải phóng Miền Nam: 30/04/1975 Solar -> 20/03/1975 Âm (Ất Mão)
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(1975, 4, 30))
        assertEquals(20, lunar.day)
        assertEquals(3, lunar.month)
        assertEquals(1975, lunar.year)
    }

    @Test
    fun testTet2000_CanhThin() {
        // Mùng 1 Tết Canh Thìn: 05/02/2000 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2000, 2, 5))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2000, lunar.year)
        assertEquals("Canh", lunar.canYear)
        assertEquals("Thìn", lunar.chiYear)
    }

    @Test
    fun testTet2020_CanhTy() {
        // Mùng 1 Tết Canh Tý: 25/01/2020 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2020, 1, 25))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2020, lunar.year)
        assertEquals("Canh", lunar.canYear)
        assertEquals("Tý", lunar.chiYear)
    }

    // -------------------------------------------------------------
    // 2. KIỂM TRA CÁC NGÀY TẾT HIỆN TẠI (2023 - 2026)
    // -------------------------------------------------------------

    @Test
    fun testTet2023_QuyMao() {
        // Mùng 1 Tết Quý Mão: 22/01/2023 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2023, 1, 22))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2023, lunar.year)
        assertEquals("Quý", lunar.canYear)
        assertEquals("Mão", lunar.chiYear)
    }

    @Test
    fun testTet2024_GiapThin() {
        // Mùng 1 Tết Giáp Thìn: 10/02/2024 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2024, 2, 10))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2024, lunar.year)
        assertEquals("Giáp", lunar.canYear)
        assertEquals("Thìn", lunar.chiYear)
    }

    @Test
    fun testTet2025_AtTy() {
        // Mùng 1 Tết Ất Tỵ: 29/01/2025 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2025, 1, 29))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2025, lunar.year)
        assertEquals("Ất", lunar.canYear)
        assertEquals("Tỵ", lunar.chiYear)
    }

    @Test
    fun testTet2026_BinhNgo() {
        // Mùng 1 Tết Bính Ngọ: 17/02/2026 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2026, 2, 17))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2026, lunar.year)
        assertEquals("Bính", lunar.canYear)
        assertEquals("Ngọ", lunar.chiYear)
    }

    // -------------------------------------------------------------
    // 3. KIỂM TRA CÁC NGÀY TẾT TRONG TƯƠNG LAI (2030 - 2060)
    // -------------------------------------------------------------

    @Test
    fun testTet2030_CanhTuat() {
        // Mùng 1 Tết Canh Tuất: 02/02/2030 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2030, 2, 2))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2030, lunar.year)
        assertEquals("Canh", lunar.canYear)
        assertEquals("Tuất", lunar.chiYear)
    }

    @Test
    fun testTet2035_AtMao() {
        // Mùng 1 Tết Ất Mão: 08/02/2035 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2035, 2, 8))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2035, lunar.year)
        assertEquals("Ất", lunar.canYear)
        assertEquals("Mão", lunar.chiYear)
    }

    @Test
    fun testTet2040_CanhThan() {
        // Mùng 1 Tết Canh Thân: 12/02/2040 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2040, 2, 12))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2040, lunar.year)
        assertEquals("Canh", lunar.canYear)
        assertEquals("Thân", lunar.chiYear)
    }

    @Test
    fun testTet2050_CanhNgo() {
        // Mùng 1 Tết Canh Ngọ: 23/01/2050 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2050, 1, 23))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2050, lunar.year)
        assertEquals("Canh", lunar.canYear)
        assertEquals("Ngọ", lunar.chiYear)
    }

    @Test
    fun testTet2060_CanhThin() {
        // Mùng 1 Tết Canh Thìn: 02/02/2060 Solar
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2060, 2, 2))
        assertEquals(1, lunar.day)
        assertEquals(1, lunar.month)
        assertEquals(2060, lunar.year)
        assertEquals("Canh", lunar.canYear)
        assertEquals("Thìn", lunar.chiYear)
    }

    // -------------------------------------------------------------
    // 4. KIỂM TRA CHUYỂN ĐỔI NGÀY ĐẶC BIỆT & THÁNG ÂM
    // -------------------------------------------------------------

    @Test
    fun testLeapMonth2023() {
        // Năm 2023 Quý Mão
        val lunarMain = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2023, 2, 20))
        assertEquals(1, lunarMain.day)

        val lunarLeap = VietnameseLunarCalendar.getLunarDate(LocalDate.of(2023, 3, 22))
        assertEquals(1, lunarLeap.day)
    }

    @Test
    fun testHistoricDate_QuocKhanh1945() {
        // Ngày Bác Hồ đọc Tuyên ngôn Độc lập: 02/09/1945 Solar -> 26/07/1945 Âm (Ất Dậu)
        val lunar = VietnameseLunarCalendar.getLunarDate(LocalDate.of(1945, 9, 2))
        assertEquals(26, lunar.day)
        assertEquals(7, lunar.month)
        assertEquals(1945, lunar.year)
    }

    @Test
    fun testSolarFromLunarConversion() {
        // Tìm ngày Dương lịch của Mùng 1 Tết Giáp Thìn 2024
        val solarDate = VietnameseLunarCalendar.getSolarDateFromLunar(1, 1, 2024)
        assertEquals(LocalDate.of(2024, 2, 10), solarDate)
    }

    @Test
    fun testMonthLengthAndHoangDao() {
        val date = LocalDate.of(2025, 5, 1)
        val length = VietnameseLunarCalendar.getLunarMonthLength(date)
        assertTrue("Số ngày trong tháng Âm phải là 29 hoặc 30", length in 29..30)

        val lunar = VietnameseLunarCalendar.getLunarDate(date)
        assertNotNull(lunar.gioHoangDao)
        assertNotNull(lunar.gioHacDao)
        assertTrue(lunar.loaiNgay == "Ngày Hoàng Đạo" || lunar.loaiNgay == "Ngày Hắc Đạo")
    }
}
