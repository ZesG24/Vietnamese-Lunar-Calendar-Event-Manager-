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
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.color.ColorProvider
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.height
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import com.example.amlich.MainActivity
import com.example.amlich.core.VietnameseLunarCalendar
import com.example.amlich.data.GioRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate

class AmLichGlanceWidget : GlanceAppWidget() {

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val today = LocalDate.now()
        val lunarDate = VietnameseLunarCalendar.getLunarDate(today)

        val repository = GioRepository(context)
        val allItems = repository.allGioItems.firstOrNull() ?: emptyList()
        val upcomingItems = repository.calculateUpcomingGioItems(allItems, today)
        val nearestGio = upcomingItems.firstOrNull()

        provideContent {
            WidgetContent(
                today = today,
                lunarDate = lunarDate,
                nearestGioTitle = nearestGio?.item?.title,
                nearestGioDays = nearestGio?.daysRemaining
            )
        }
    }

    @Composable
    private fun WidgetContent(
        today: LocalDate,
        lunarDate: com.example.amlich.core.LunarDate,
        nearestGioTitle: String?,
        nearestGioDays: Long?
    ) {
        Column(
            modifier = GlanceModifier
                .fillMaxSize()
                .background(ColorProvider(day = Color(0xFF8B0000), night = Color(0xFF5A0000)))
                .padding(12.dp)
                .clickable(actionStartActivity<MainActivity>()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Header: Ngày Dương
            Text(
                text = "Thứ ${getDayOfWeekName(today.dayOfWeek.value)}, ${today.dayOfMonth}/${today.monthValue}/${today.year}",
                style = TextStyle(
                    color = ColorProvider(day = Color.White, night = Color.White),
                    fontSize = 13.sp
                )
            )

            Spacer(modifier = GlanceModifier.height(4.dp))

            // Lịch Âm nổi bật
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${lunarDate.day}",
                    style = TextStyle(
                        color = ColorProvider(day = Color(0xFFFFD700), night = Color(0xFFFFD700)),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Column(modifier = GlanceModifier.padding(start = 8.dp)) {
                    Text(
                        text = "Tháng ${lunarDate.month} (Âm Lịch)",
                        style = TextStyle(
                            color = ColorProvider(day = Color.White, night = Color.White),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "${lunarDate.canDay} ${lunarDate.chiDay} • ${lunarDate.canYear} ${lunarDate.chiYear}",
                        style = TextStyle(
                            color = ColorProvider(day = Color(0xFFFFE4B5), night = Color(0xFFFFE4B5)),
                            fontSize = 11.sp
                        )
                    )
                }
            }

            Spacer(modifier = GlanceModifier.height(6.dp))

            // Ngày Giỗ sắp tới
            if (nearestGioTitle != null && nearestGioDays != null) {
                val gioText = if (nearestGioDays == 0L) {
                    "🚩 Hôm nay: Ngày giỗ $nearestGioTitle"
                } else {
                    "📌 Sắp giỗ $nearestGioTitle: còn $nearestGioDays ngày"
                }
                Text(
                    text = gioText,
                    style = TextStyle(
                        color = ColorProvider(day = Color(0xFFFFE4C4), night = Color(0xFFFFE4C4)),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                )
            } else {
                Text(
                    text = "Giờ H.Đạo: ${lunarDate.gioHoangDao.take(28)}...",
                    style = TextStyle(
                        color = ColorProvider(day = Color.LightGray, night = Color.LightGray),
                        fontSize = 10.sp
                    )
                )
            }
        }
    }

    private fun getDayOfWeekName(dayOfWeek: Int): String {
        return when (dayOfWeek) {
            1 -> "Hai"
            2 -> "Ba"
            3 -> "Tư"
            4 -> "Năm"
            5 -> "Sáu"
            6 -> "Bảy"
            7 -> "Chủ Nhật"
            else -> ""
        }
    }
}
