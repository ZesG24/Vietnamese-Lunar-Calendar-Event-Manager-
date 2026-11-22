package com.example.amlich.core

import java.time.LocalDate
import kotlin.math.floor

/**
 * Data class đại diện cho thông tin Lịch Âm đầy đủ.
 */
data class LunarDate(
    val day: Int,
    val month: Int,
    val year: Int,
    val isLeapMonth: Boolean = false,
    val canYear: String,
    val chiYear: String,
    val canMonth: String,
    val chiMonth: String,
    val canDay: String,
    val chiDay: String,
    val gioHoangDao: String,
    val tietKhi: String
)

/**
 * Thuật toán tính Lịch Âm Việt Nam chuẩn (Múi giờ GMT+7) theo phương pháp Hồ Ngọc Đức.
 */
object VietnameseLunarCalendar {

    private val CAN = arrayOf("Giáp", "Ất", "Bính", "Đinh", "Mậu", "Kỷ", "Canh", "Tân", "Nhâm", "Quý")
    private val CHI = arrayOf("Tý", "Sửu", "Dần", "Mão", "Thìn", "Tỵ", "Ngọ", "Mùi", "Thân", "Dậu", "Tuất", "Hợi")

    private val TIET_KHI = arrayOf(
        "Xuân Phân", "Thanh Minh", "Cốc Vũ", "Lập Hạ", "Tiểu Mãn", "Mang Chủng",
        "Hạ Chí", "Tiểu Thử", "Đại Thử", "Lập Thu", "Xử Thử", "Bạch Lộ",
        "Thu Phân", "Hàn Lộ", "Sương Giáng", "Lập Đông", "Tiểu Tuyết", "Đại Tuyết",
        "Đông Chí", "Tiểu Hàn", "Đại Hàn", "Lập Xuân", "Vũ Thủy", "Kinh Trập"
    )

    private const val TIME_ZONE = 7.0

    /**
     * Quy đổi từ Ngày Dương lịch (LocalDate) sang LunarDate.
     */
    fun getLunarDate(solarDate: LocalDate): LunarDate {
        val dd = solarDate.dayOfMonth
        val mm = solarDate.monthValue
        val yy = solarDate.year

        val jd = jdFromDate(dd, mm, yy)
        val k = floor((jd - 2415021.07699) / 29.530588853).toInt()

        var monthStart = getNewMoonDay(k + 1, TIME_ZONE)
        if (monthStart > jd) {
            monthStart = getNewMoonDay(k, TIME_ZONE)
        }

        var a11 = getLunarMonth11(yy, TIME_ZONE)
        var b11 = a11
        var lunarYear: Int

        if (a11 >= monthStart) {
            lunarYear = yy
            a11 = getLunarMonth11(yy - 1, TIME_ZONE)
        } else {
            lunarYear = yy + 1
            b11 = getLunarMonth11(yy + 1, TIME_ZONE)
        }

        val lunarDay = jd - monthStart + 1
        val kMonth = floor((monthStart - 2415021.07699) / 29.530588853 + 0.5).toInt()
        val kA11 = floor((a11 - 2415021.07699) / 29.530588853 + 0.5).toInt()
        val diff = kMonth - kA11

        var adjustedDiff = diff
        var isLeap = false

        if ((b11 - a11) > 365) {
            val leapMonthDiff = getLeapMonthOffset(a11, TIME_ZONE)
            if (diff >= leapMonthDiff) {
                adjustedDiff = diff - 1
                if (diff == leapMonthDiff) {
                    isLeap = true
                }
            }
        }

        var lunarMonth = adjustedDiff - 1
        if (lunarMonth <= 0) {
            lunarMonth += 12
        }

        if (lunarMonth >= 11 && diff < 4) {
            lunarYear -= 1
        }

        // Calculation of Can Chi
        val canYear = CAN[(lunarYear + 6) % 10]
        val chiYear = CHI[(lunarYear + 8) % 12]

        val canMonth = CAN[(lunarYear * 12 + lunarMonth + 3) % 10]
        val chiMonth = CHI[(lunarMonth + 1) % 12]

        val canDay = CAN[(jd + 9) % 10]
        val chiDay = CHI[(jd + 1) % 12]

        val gioHoangDao = getGioHoangDao(chiDay)
        val tietKhi = getTietKhi(jd)

        return LunarDate(
            day = lunarDay,
            month = lunarMonth,
            year = lunarYear,
            isLeapMonth = isLeap,
            canYear = canYear,
            chiYear = chiYear,
            canMonth = canMonth,
            chiMonth = chiMonth,
            canDay = canDay,
            chiDay = chiDay,
            gioHoangDao = gioHoangDao,
            tietKhi = tietKhi
        )
    }

    /**
     * Tìm ngày Dương lịch (LocalDate) tương ứng với một Ngày Giỗ Âm lịch trong năm Dương lịch chỉ định.
     */
    fun getSolarDateFromLunar(lunarDay: Int, lunarMonth: Int, solarYear: Int): LocalDate {
        val startDate = LocalDate.of(solarYear, 1, 1)
        val endDate = LocalDate.of(solarYear, 12, 31)

        var curr = startDate
        while (!curr.isAfter(endDate)) {
            val lunar = getLunarDate(curr)
            if (lunar.day == lunarDay && lunar.month == lunarMonth) {
                return curr
            }
            curr = curr.plusDays(1)
        }
        return LocalDate.of(solarYear, 1, 1)
    }

    // --- Astronomical Algorithms (Hồ Ngọc Đức) ---

    private fun jdFromDate(dd: Int, mm: Int, yy: Int): Int {
        val a = (14 - mm) / 12
        val y = yy + 4800 - a
        val m = mm + 12 * a - 3
        var jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - y / 100 + y / 400 - 32045
        if (jd < 2299161) {
            jd = dd + (153 * m + 2) / 5 + 365 * y + y / 4 - 32083
        }
        return jd
    }

    private fun getNewMoonDay(k: Int, timeZone: Double): Int {
        val T = k / 1236.85
        val T2 = T * T
        val T3 = T2 * T
        val dr = Math.PI / 180.0
        var Jd1 = 2415020.75933 + 29.53058868 * k + 0.0001178 * T2 - 0.000000155 * T3
        Jd1 += 0.00033 * Math.sin((166.56 + 132.87 * T - 0.009173 * T2) * dr)

        val M = 359.2242 + 29.10535608 * k - 0.0000333 * T2 - 0.00000347 * T3
        val Mpr = 306.0253 + 385.81691806 * k + 0.0107306 * T2 + 0.00001236 * T3
        val F = 21.2964 + 390.67050646 * k - 0.0016528 * T2 - 0.00000239 * T3

        var C1 = (0.1734 - 0.000393 * T) * Math.sin(M * dr) + 0.0021 * Math.sin(2 * M * dr)
        C1 -= 0.4068 * Math.sin(Mpr * dr)
        C1 += 0.0161 * Math.sin(2 * Mpr * dr)
        C1 -= 0.0004 * Math.sin(3 * Mpr * dr)
        C1 += 0.0104 * Math.sin(2 * F * dr)
        C1 -= 0.0051 * Math.sin((M + Mpr) * dr)
        C1 -= 0.00074 * Math.sin((M - Mpr) * dr)
        C1 += 0.0004 * Math.sin((2 * F + M) * dr)
        C1 -= 0.0004 * Math.sin((2 * F - M) * dr)
        C1 -= 0.0006 * Math.sin((2 * F + Mpr) * dr)
        C1 += 0.0100 * Math.sin((2 * F - Mpr) * dr)
        C1 += 0.0005 * Math.sin((M + 2 * Mpr) * dr)

        val JdNew = Jd1 + C1
        return floor(JdNew + 0.5 + timeZone / 24.0).toInt()
    }

    private fun getSunLongitudeIndex(jdn: Int, timeZone: Double): Int {
        val T = (jdn - 2451545.0 - timeZone / 24.0) / 36525.0
        val T2 = T * T
        val dr = Math.PI / 180.0
        val L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2
        val M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2
        val C = (1.914602 - 0.004817 * T - 0.000014 * T2) * Math.sin(M * dr) +
                (0.019993 - 0.000101 * T) * Math.sin(2 * M * dr) +
                0.000289 * Math.sin(3 * M * dr)
        var L = L0 + C
        L *= dr
        L -= 2.0 * Math.PI * floor(L / (2.0 * Math.PI))
        return floor(L / (Math.PI / 6.0)).toInt()
    }

    private fun getSunLongitudeDegrees(jdn: Int, timeZone: Double): Double {
        val T = (jdn - 2451545.0 - timeZone / 24.0) / 36525.0
        val T2 = T * T
        val dr = Math.PI / 180.0
        val L0 = 280.46645 + 36000.76983 * T + 0.0003032 * T2
        val M = 357.52910 + 35999.05030 * T - 0.0001559 * T2 - 0.00000048 * T * T2
        val C = (1.914602 - 0.004817 * T - 0.000014 * T2) * Math.sin(M * dr) +
                (0.019993 - 0.000101 * T) * Math.sin(2 * M * dr) +
                0.000289 * Math.sin(3 * M * dr)
        var L = (L0 + C) % 360.0
        if (L < 0) L += 360.0
        return L
    }

    private fun getLunarMonth11(yy: Int, timeZone: Double): Int {
        val off = jdFromDate(31, 12, yy) - 2415021.07699
        val k = floor(off / 29.530588853).toInt()
        var nm = getNewMoonDay(k, timeZone)
        val sunLong = getSunLongitudeIndex(nm, timeZone)
        if (sunLong >= 9) {
            nm = getNewMoonDay(k - 1, timeZone)
        }
        return nm
    }

    private fun getLeapMonthOffset(a11: Int, timeZone: Double): Int {
        var k = floor((a11 - 2415021.07699) / 29.530588853 + 0.5).toInt()
        var last: Int
        var i = 1
        var arc = getSunLongitudeIndex(getNewMoonDay(k, timeZone), timeZone)
        do {
            last = arc
            k++
            arc = getSunLongitudeIndex(getNewMoonDay(k, timeZone), timeZone)
            i++
        } while (arc != last && i < 14)
        return i - 1
    }

    private fun getGioHoangDao(chiDay: String): String {
        return when (chiDay) {
            "Tý", "Ngọ" -> "Tý (23-1), Sửu (1-3), Mão (5-7), Ngọ (11-13), Thân (15-17), Dậu (17-19)"
            "Sửu", "Mùi" -> "Dần (3-5), Mão (5-7), Tỵ (9-11), Thân (15-17), Tuất (19-21), Hợi (21-23)"
            "Dần", "Thân" -> "Tý (23-1), Sửu (1-3), Thìn (7-9), Tỵ (9-11), Mùi (13-15), Tuất (19-21)"
            "Mão", "Dậu" -> "Tý (23-1), Dần (3-5), Mão (5-7), Ngọ (11-13), Mùi (13-15), Dậu (17-19)"
            "Thìn", "Tuất" -> "Dần (3-5), Thìn (7-9), Tỵ (9-11), Thân (15-17), Dậu (17-19), Hợi (21-23)"
            "Tỵ", "Hợi" -> "Sửu (1-3), Thìn (7-9), Ngọ (11-13), Mùi (13-15), Tuất (19-21), Hợi (21-23)"
            else -> "Tý, Sửu, Mão, Ngọ, Thân, Dậu"
        }
    }

    private fun getTietKhi(jd: Int): String {
        val deg = getSunLongitudeDegrees(jd, TIME_ZONE)
        val index = floor(deg / 15.0).toInt() % 24
        return TIET_KHI[index]
    }
}
