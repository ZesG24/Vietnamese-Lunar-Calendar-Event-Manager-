package com.example.amlich.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.amlich.MainActivity
import com.example.amlich.core.VietnameseLunarCalendar
import java.time.LocalDate
import java.time.YearMonth

// 1. Widget Sáng (Ngọc Bích & Vàng Hoàng Kim)
class MonthCalendarLightWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MonthCalendarWidgetContent(themeMode = 0)
        }
    }
}

class MonthCalendarLightReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MonthCalendarLightWidget()
}

// 2. Widget Tối (Đêm Huyền Bí)
class MonthCalendarDarkWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MonthCalendarWidgetContent(themeMode = 1)
        }
    }
}

class MonthCalendarDarkReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MonthCalendarDarkWidget()
}

// 3. Widget Trong Suốt (Glass)
class MonthCalendarGlassWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            MonthCalendarWidgetContent(themeMode = 2)
        }
    }
}

class MonthCalendarGlassReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = MonthCalendarGlassWidget()
}

// --- Shared Content Composable ---
@Composable
private fun MonthCalendarWidgetContent(themeMode: Int) {
    val today = LocalDate.now()
    val yearMonth = YearMonth.from(today)

    val firstDayOfMonth = yearMonth.atDay(1)
    val dayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 (Mon) .. 7 (Sun)
    val startDate = firstDayOfMonth.minusDays((dayOfWeek - 1).toLong())

    // Calculate Lunar Month Range
    val lunarFirst = VietnameseLunarCalendar.getLunarDate(firstDayOfMonth)
    val lunarLast = VietnameseLunarCalendar.getLunarDate(yearMonth.atEndOfMonth())
    val lunarRangeText = if (lunarFirst.month == lunarLast.month) {
        "Âm Lịch T${lunarFirst.month}"
    } else {
        "Âm: T${lunarFirst.month} - T${lunarLast.month}"
    }

    // Palette config for 3 modes (Ngọc Bích & Vàng Hoàng Kim Design)
    val (bgColor, headerTextColor, accentColor, weekdayTextColor, todayBgColor, cellTextColor, dimmedTextColor) = when (themeMode) {
        1 -> WidgetPalette(
            bg = Color(0xFF161212),             // Tối - Obsidian
            headerText = Color(0xFFF3ECE1),
            accent = Color(0xFFF5A623),         // Gold
            weekdayText = Color(0xFFF3ECE1),
            todayBg = Color(0x40F5A623),
            cellText = Color(0xFFF3ECE1),
            dimmedText = Color(0x55F3ECE1)
        )
        2 -> WidgetPalette(
            bg = Color(0xB0000000),             // Trong suốt - Glass
            headerText = Color(0xFFFFFFFF),
            accent = Color(0xFFFFD700),         // Bright Gold
            weekdayText = Color(0xFFFFFFFF),
            todayBg = Color(0x60FFD700),
            cellText = Color(0xFFFFFFFF),
            dimmedText = Color(0x44FFFFFF)
        )
        else -> WidgetPalette(
            bg = Color(0xFFF7FAF8),             // Sáng - Trắng ngà xanh nhạt (#F7FAF8)
            headerText = Color(0xFF143527),     // Xanh Ngọc Bích sẫm (#143527)
            accent = Color(0xFFC68B27),         // Vàng Hoàng Kim (#C68B27)
            weekdayText = Color(0xFF143527),   // Xanh Ngọc Bích sẫm
            todayBg = Color(0xFFE8F0EA),        // Nền xanh ngọc nhạt
            cellText = Color(0xFF143527),
            dimmedText = Color(0x44143527)
        )
    }

    Column(
        modifier = GlanceModifier
            .fillMaxSize()
            .background(ColorProvider(day = bgColor, night = bgColor))
            .padding(10.dp)
            .clickable(actionStartActivity<MainActivity>())
    ) {
        // Header: "Tháng MM - YYYY" & Lunar Month Range
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = GlanceModifier.defaultWeight()) {
                Text(
                    text = "Tháng ${yearMonth.monthValue} - ${yearMonth.year}",
                    style = TextStyle(
                        color = ColorProvider(day = headerTextColor, night = headerTextColor),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = lunarRangeText,
                    style = TextStyle(
                        color = ColorProvider(day = accentColor, night = accentColor),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            }
        }

        Spacer(modifier = GlanceModifier.height(8.dp))

        // Weekday Header: T2, T3, T4, T5, T6, T7, CN
        Row(
            modifier = GlanceModifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val weekDays = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
            weekDays.forEach { dayStr ->
                val isSunday = dayStr == "CN"
                val textColor = if (isSunday) accentColor else weekdayTextColor
                Box(
                    modifier = GlanceModifier.defaultWeight(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = dayStr,
                        style = TextStyle(
                            color = ColorProvider(day = textColor, night = textColor),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        Spacer(modifier = GlanceModifier.height(4.dp))

        // 6 Rows x 7 Days Grid
        var currDate = startDate
        for (row in 0 until 6) {
            Row(
                modifier = GlanceModifier.fillMaxWidth().defaultWeight(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (col in 0 until 7) {
                    val cellDate = currDate
                    val isToday = (cellDate == today)
                    val isCurrentMonth = (cellDate.month == yearMonth.month)
                    val lunar = VietnameseLunarCalendar.getLunarDate(cellDate)

                    val daySolarText = "${cellDate.dayOfMonth}"
                    val dayLunarText = if (lunar.day == 1 || lunar.day == 15) "${lunar.day}/${lunar.month}" else "${lunar.day}"

                    val currentCellTextColor = when {
                        isToday -> if (themeMode == 0) headerTextColor else accentColor
                        !isCurrentMonth -> dimmedTextColor
                        cellDate.dayOfWeek.value == 7 -> accentColor
                        else -> cellTextColor
                    }

                    Box(
                        modifier = GlanceModifier
                            .defaultWeight()
                            .fillMaxHeight()
                            .padding(1.dp)
                            .then(
                                if (isToday) GlanceModifier.background(ColorProvider(day = todayBgColor, night = todayBgColor))
                                else GlanceModifier
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = daySolarText,
                                style = TextStyle(
                                    color = ColorProvider(day = currentCellTextColor, night = currentCellTextColor),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                            Text(
                                text = dayLunarText,
                                style = TextStyle(
                                    color = ColorProvider(
                                        day = if (isCurrentMonth) accentColor else dimmedTextColor,
                                        night = if (isCurrentMonth) accentColor else dimmedTextColor
                                    ),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }
                    }
                    currDate = currDate.plusDays(1)
                }
            }
        }
    }
}

private data class WidgetPalette(
    val bg: Color,
    val headerText: Color,
    val accent: Color,
    val weekdayText: Color,
    val todayBg: Color,
    val cellText: Color,
    val dimmedText: Color
)
