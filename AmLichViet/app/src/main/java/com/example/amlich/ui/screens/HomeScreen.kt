package com.example.amlich.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Today
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amlich.ui.CalendarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: CalendarViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val lunar = uiState.selectedLunarDate
    val selectedDate = uiState.selectedDate

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tháng ${uiState.currentYearMonth.monthValue}/${uiState.currentYearMonth.year}",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.previousMonth() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Tháng trước", tint = Color.White)
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.nextMonth() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Tháng sau", tint = Color.White)
                    }
                    IconButton(onClick = { viewModel.goToToday() }) {
                        Icon(Icons.Default.Today, contentDescription = "Hôm nay", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8B0000))
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFBF7))
        ) {
            // Header Lịch Âm nổi bật của Ngày được chọn
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF8B0000)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Thứ ${getDayOfWeekName(selectedDate.dayOfWeek.value)}, Ngày ${selectedDate.dayOfMonth}/${selectedDate.monthValue}/${selectedDate.year}",
                        color = Color.White,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${lunar.day}",
                            fontSize = 48.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFFD700)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Tháng ${lunar.month} (Âm Lịch)",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = "Năm ${lunar.canYear} ${lunar.chiYear}",
                                fontSize = 14.sp,
                                color = Color(0xFFFFE4B5)
                            )
                            Text(
                                text = "Ngày ${lunar.canDay} ${lunar.chiDay} • Tháng ${lunar.canMonth} ${lunar.chiMonth}",
                                fontSize = 12.sp,
                                color = Color(0xFFFFE4B5)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tiết khí: ${lunar.tietKhi}",
                            fontSize = 12.sp,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Giờ H.Đạo: ${lunar.gioHoangDao}",
                        fontSize = 11.sp,
                        color = Color(0xFFFFE4C4),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }

            // Hàng thứ trong tuần (T2 .. CN)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val daysOfWeek = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        fontWeight = FontWeight.Bold,
                        color = if (day == "CN") Color.Red else Color(0xFF555555),
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Grid Màn hình Lịch Tháng (6x7)
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            ) {
                items(uiState.monthDays) { item ->
                    val isSelected = item.solarDate == selectedDate
                    val isSunday = item.solarDate.dayOfWeek.value == 7

                    Box(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .padding(2.dp)
                            .background(
                                color = when {
                                    isSelected -> Color(0xFF8B0000)
                                    item.isToday -> Color(0xFFFFE4E1)
                                    else -> Color.White
                                },
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = if (item.isToday) 1.5.dp else 0.5.dp,
                                color = if (item.isToday) Color(0xFF8B0000) else Color(0xFFEEEEEE),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.selectDate(item.solarDate) },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Ngày Dương
                            Text(
                                text = "${item.solarDate.dayOfMonth}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = when {
                                    isSelected -> Color.White
                                    !item.isCurrentMonth -> Color.LightGray
                                    isSunday -> Color.Red
                                    else -> Color.Black
                                }
                            )
                            // Ngày Âm
                            Text(
                                text = if (item.lunarDate.day == 1) "${item.lunarDate.day}/${item.lunarDate.month}" else "${item.lunarDate.day}",
                                fontSize = 10.sp,
                                color = when {
                                    isSelected -> Color(0xFFFFD700)
                                    !item.isCurrentMonth -> Color.LightGray
                                    else -> Color(0xFF888888)
                                }
                            )
                        }
                    }
                }
            }
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
