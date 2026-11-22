package com.example.amlich.ui

import androidx.lifecycle.ViewModel
import com.example.amlich.core.LunarDate
import com.example.amlich.core.VietnameseLunarCalendar
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.YearMonth

data class DayCalendarItem(
    val solarDate: LocalDate,
    val lunarDate: LunarDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean
)

data class CalendarUiState(
    val selectedDate: LocalDate = LocalDate.now(),
    val currentYearMonth: YearMonth = YearMonth.now(),
    val selectedLunarDate: LunarDate = VietnameseLunarCalendar.getLunarDate(LocalDate.now()),
    val monthDays: List<DayCalendarItem> = emptyList()
)

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    init {
        updateMonthDays(_uiState.value.currentYearMonth, _uiState.value.selectedDate)
    }

    fun selectDate(date: LocalDate) {
        val lunar = VietnameseLunarCalendar.getLunarDate(date)
        val yearMonth = YearMonth.from(date)
        _uiState.value = _uiState.value.copy(
            selectedDate = date,
            currentYearMonth = yearMonth,
            selectedLunarDate = lunar
        )
        updateMonthDays(yearMonth, date)
    }

    fun nextMonth() {
        val next = _uiState.value.currentYearMonth.plusMonths(1)
        val firstDay = next.atDay(1)
        selectDate(firstDay)
    }

    fun previousMonth() {
        val prev = _uiState.value.currentYearMonth.minusMonths(1)
        val firstDay = prev.atDay(1)
        selectDate(firstDay)
    }

    fun goToToday() {
        selectDate(LocalDate.now())
    }

    private fun updateMonthDays(yearMonth: YearMonth, selectedDate: LocalDate) {
        val firstDayOfMonth = yearMonth.atDay(1)
        val dayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 (Mon) .. 7 (Sun)

        // Start from Monday of the first week
        val startDate = firstDayOfMonth.minusDays((dayOfWeek - 1).toLong())
        val days = mutableListOf<DayCalendarItem>()

        val today = LocalDate.now()
        var current = startDate
        // 42 days grid (6 weeks * 7 days)
        repeat(42) {
            val lunar = VietnameseLunarCalendar.getLunarDate(current)
            days.add(
                DayCalendarItem(
                    solarDate = current,
                    lunarDate = lunar,
                    isCurrentMonth = current.month == yearMonth.month,
                    isToday = current == today
                )
            )
            current = current.plusDays(1)
        }

        _uiState.value = _uiState.value.copy(monthDays = days)
    }
}
