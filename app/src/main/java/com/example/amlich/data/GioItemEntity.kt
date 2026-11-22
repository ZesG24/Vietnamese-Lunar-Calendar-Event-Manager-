package com.example.amlich.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "gio_items")
data class GioItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,                // Tên người được thờ (VD: Ông Ngoại, Cụ Bà,...)
    val relationship: String = "",    // Quan hệ (VD: Ông nội, Mẹ,...)
    val lunarDay: Int,                // Ngày giỗ âm (1-30)
    val lunarMonth: Int,              // Tháng giỗ âm (1-12)
    val isLeapMonth: Boolean = false, // Có phải tháng nhuận không
    val note: String = "",            // Ghi chú (đồ cúng, địa điểm,...)
    val reminderDaysBefore: Int = 1   // Nhắc trước bao nhiêu ngày (VD: 1 ngày, 3 ngày)
)
