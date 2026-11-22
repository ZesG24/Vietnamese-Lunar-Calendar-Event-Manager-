package com.example.amlich.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.amlich.core.LunarDate
import com.example.amlich.core.VietnameseLunarCalendar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

data class DayCalendarItem(
    val solarDate: LocalDate,
    val lunarDate: LunarDate,
    val isCurrentMonth: Boolean,
    val isToday: Boolean
)

data class CalendarUiState(
    val selectedCalendarDate: LocalDate? = null,
    val currentYearMonth: YearMonth = YearMonth.now(),
    val selectedLunarDate: LunarDate? = null,
    val monthDays: List<DayCalendarItem> = emptyList()
)

class CalendarViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CalendarUiState())
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    init {
        updateMonthDays(_uiState.value.currentYearMonth)
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.value = enabled
    }

    fun selectDate(date: LocalDate) {
        val lunar = VietnameseLunarCalendar.getLunarDate(date)
        val yearMonth = YearMonth.from(date)
        val currentYearMonth = _uiState.value.currentYearMonth

        _uiState.value = _uiState.value.copy(
            selectedCalendarDate = date,
            currentYearMonth = yearMonth,
            selectedLunarDate = lunar
        )

        if (yearMonth != currentYearMonth || _uiState.value.monthDays.isEmpty()) {
            updateMonthDays(yearMonth)
        }
    }

    fun selectYearMonth(year: Int, month: Int) {
        val targetYearMonth = YearMonth.of(year, month)
        _uiState.value = _uiState.value.copy(
            currentYearMonth = targetYearMonth,
            selectedCalendarDate = null,
            selectedLunarDate = null
        )
        updateMonthDays(targetYearMonth)
    }

    fun nextMonth() {
        val next = _uiState.value.currentYearMonth.plusMonths(1)
        _uiState.value = _uiState.value.copy(
            currentYearMonth = next,
            selectedCalendarDate = null,
            selectedLunarDate = null
        )
        updateMonthDays(next)
    }

    fun previousMonth() {
        val prev = _uiState.value.currentYearMonth.minusMonths(1)
        _uiState.value = _uiState.value.copy(
            currentYearMonth = prev,
            selectedCalendarDate = null,
            selectedLunarDate = null
        )
        updateMonthDays(prev)
    }

    fun goToToday() {
        val today = LocalDate.now()
        selectDate(today)
    }

    private fun updateMonthDays(yearMonth: YearMonth) {
        viewModelScope.launch(Dispatchers.Default) {
            val firstDayOfMonth = yearMonth.atDay(1)
            val dayOfWeek = firstDayOfMonth.dayOfWeek.value // 1 (Mon) .. 7 (Sun)

            val startDate = firstDayOfMonth.minusDays((dayOfWeek - 1).toLong())
            val days = ArrayList<DayCalendarItem>(42)

            val today = LocalDate.now()
            var current = startDate
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
}
