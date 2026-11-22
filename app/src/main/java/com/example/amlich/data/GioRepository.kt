package com.example.amlich.data

import android.content.Context
import com.example.amlich.core.VietnameseLunarCalendar
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class UpcomingGioItem(
    val item: GioItemEntity,
    val solarDate: LocalDate,
    val daysRemaining: Long
)

class GioRepository(context: Context) {
    private val gioDao = AppDatabase.getInstance(context).gioDao()

    val allGioItems: Flow<List<GioItemEntity>> = gioDao.getAllGioItems()

    suspend fun getGioItemById(id: Long): GioItemEntity? = gioDao.getGioItemById(id)

    suspend fun insertGioItem(item: GioItemEntity): Long = gioDao.insertGioItem(item)

    suspend fun updateGioItem(item: GioItemEntity) = gioDao.updateGioItem(item)

    suspend fun deleteGioItem(item: GioItemEntity) = gioDao.deleteGioItem(item)

    suspend fun deleteGioItemById(id: Long) = gioDao.deleteGioItemById(id)

    /**
     * Tính danh sách ngày giỗ sắp tới gần nhất (trong vòng 365 ngày).
     */
    fun calculateUpcomingGioItems(items: List<GioItemEntity>, today: LocalDate = LocalDate.now()): List<UpcomingGioItem> {
        val currentYear = today.year
        return items.map { item ->
            var solarDate = VietnameseLunarCalendar.getSolarDateFromLunar(item.lunarDay, item.lunarMonth, currentYear)
            if (solarDate.isBefore(today)) {
                // Nếu ngày giỗ năm nay đã qua, tính cho năm sau
                solarDate = VietnameseLunarCalendar.getSolarDateFromLunar(item.lunarDay, item.lunarMonth, currentYear + 1)
            }
            val daysRemaining = ChronoUnit.DAYS.between(today, solarDate)
            UpcomingGioItem(item = item, solarDate = solarDate, daysRemaining = daysRemaining)
        }.sortedBy { it.daysRemaining }
    }
}
