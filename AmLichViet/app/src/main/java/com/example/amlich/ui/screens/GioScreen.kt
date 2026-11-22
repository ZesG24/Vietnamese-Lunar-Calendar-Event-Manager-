package com.example.amlich.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amlich.data.GioItemEntity
import com.example.amlich.ui.GioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GioScreen(viewModel: GioViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Quản Lý Ngày Giỗ",
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF8B0000))
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = Color(0xFF8B0000),
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Thêm Ngày Giỗ")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFFDFBF7))
        ) {
            if (uiState.upcomingItems.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "Chưa có ngày giỗ nào được tạo.",
                            color = Color.Gray,
                            fontSize = 16.sp
                        )
                        Text(
                            text = "Bấm nút (+) bên dưới để thêm ngày giỗ người thân.",
                            color = Color.Gray,
                            fontSize = 13.sp
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp)
                ) {
                    items(uiState.upcomingItems) { upcoming ->
                        val item = upcoming.item
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = item.title,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color(0xFF8B0000)
                                        )
                                        if (item.relationship.isNotBlank()) {
                                            Text(
                                                text = " (${item.relationship})",
                                                fontSize = 14.sp,
                                                color = Color.Gray
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = "Âm Lịch: Mùng ${item.lunarDay} tháng ${item.lunarMonth}",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Black
                                    )

                                    Text(
                                        text = "Dương Lịch năm nay: ${upcoming.solarDate.dayOfMonth}/${upcoming.solarDate.monthValue}/${upcoming.solarDate.year}",
                                        fontSize = 12.sp,
                                        color = Color(0xFF555555)
                                    )

                                    if (item.note.isNotBlank()) {
                                        Text(
                                            text = "Ghi chú: ${item.note}",
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(top = 2.dp)
                                        )
                                    }
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    // Badge đếm ngược
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                color = if (upcoming.daysRemaining == 0L) Color.Red else Color(0xFFFF8C00),
                                                shape = RoundedCornerShape(16.dp)
                                            )
                                            .padding(horizontal = 10.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = if (upcoming.daysRemaining == 0L) "Hôm nay!" else "Còn ${upcoming.daysRemaining} ngày",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }

                                    IconButton(
                                        onClick = { viewModel.deleteGioItem(item) },
                                        modifier = Modifier.padding(top = 4.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Gray)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddGioDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { title, relationship, lunarDay, lunarMonth, note, reminderDays ->
                viewModel.addGioItem(title, relationship, lunarDay, lunarMonth, note, reminderDays)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun AddGioDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, relationship: String, lunarDay: Int, lunarMonth: Int, note: String, reminderDays: Int) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var relationship by remember { mutableStateOf("") }
    var lunarDayText by remember { mutableStateOf("") }
    var lunarMonthText by remember { mutableStateOf("") }
    var note by remember { mutableStateOf("") }
    var reminderDaysText by remember { mutableStateOf("1") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Thêm Ngày Giỗ Mới", fontWeight = FontWeight.Bold) },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Tên người được thờ (VD: Ông Nội)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = relationship,
                    onValueChange = { relationship = it },
                    label = { Text("Quan hệ gia đình (VD: Bố, Mẹ, Ông, Cụ...)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = lunarDayText,
                        onValueChange = { lunarDayText = it },
                        label = { Text("Ngày Âm (1-30)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    OutlinedTextField(
                        value = lunarMonthText,
                        onValueChange = { lunarMonthText = it },
                        label = { Text("Tháng Âm (1-12)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = reminderDaysText,
                    onValueChange = { reminderDaysText = it },
                    label = { Text("Nhắc trước mấy ngày (VD: 1 hoặc 3)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Ghi chú (Địa điểm, đồ cúng...)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val day = lunarDayText.toIntOrNull() ?: 1
                    val month = lunarMonthText.toIntOrNull() ?: 1
                    val reminderDays = reminderDaysText.toIntOrNull() ?: 1
                    if (title.isNotBlank()) {
                        onConfirm(title, relationship, day, month, note, reminderDays)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF8B0000))
            ) {
                Text("Lưu Ngày Giỗ", color = Color.White)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
