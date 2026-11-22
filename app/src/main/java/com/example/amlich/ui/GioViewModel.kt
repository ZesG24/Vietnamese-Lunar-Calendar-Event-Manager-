package com.example.amlich.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.amlich.data.GioItemEntity
import com.example.amlich.data.GioRepository
import com.example.amlich.data.UpcomingGioItem
import com.example.amlich.reminder.GioReminderWorker
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GioUiState(
    val gioItems: List<GioItemEntity> = emptyList(),
    val upcomingItems: List<UpcomingGioItem> = emptyList()
)

class GioViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GioRepository(application)

    val uiState: StateFlow<GioUiState> = repository.allGioItems.map { items ->
        val upcoming = repository.calculateUpcomingGioItems(items)
        GioUiState(gioItems = items, upcomingItems = upcoming)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GioUiState()
    )

    fun addGioItem(title: String, relationship: String, lunarDay: Int, lunarMonth: Int, note: String, reminderDaysBefore: Int) {
        viewModelScope.launch {
            val item = GioItemEntity(
                title = title,
                relationship = relationship,
                lunarDay = lunarDay,
                lunarMonth = lunarMonth,
                note = note,
                reminderDaysBefore = reminderDaysBefore
            )
            repository.insertGioItem(item)
            // Re-schedule daily reminder worker
            GioReminderWorker.scheduleDailyWorker(getApplication())
        }
    }

    fun deleteGioItem(item: GioItemEntity) {
        viewModelScope.launch {
            repository.deleteGioItem(item)
        }
    }
}
