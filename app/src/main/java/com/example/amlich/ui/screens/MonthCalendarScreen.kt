package com.example.amlich.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amlich.core.VietnameseLunarCalendar
import com.example.amlich.ui.CalendarViewModel
import com.example.amlich.ui.DayCalendarItem
import com.example.amlich.ui.GioViewModel
import com.example.amlich.ui.theme.EventDotAmber
import java.time.LocalDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthCalendarScreen(
    calendarViewModel: CalendarViewModel,
    gioViewModel: GioViewModel
) {
    val uiState by calendarViewModel.uiState.collectAsState()
    val gioUiState by gioViewModel.uiState.collectAsState()
    val allGioItems = gioUiState.gioItems

    val currentYearMonth = uiState.currentYearMonth
    val selectedCalendarDate = uiState.selectedCalendarDate

    var selectedTabItem by remember { mutableIntStateOf(0) } // 0: Ngày lễ trong tháng, 1: Sự kiện của tôi
    var showMonthYearPicker by remember { mutableStateOf(false) }

    // Tính dải tháng Âm lịch nằm trong tháng Dương lịch này
    val lunarFirst = remember(currentYearMonth) { VietnameseLunarCalendar.getLunarDate(currentYearMonth.atDay(1)) }
    val lunarLast = remember(currentYearMonth) { VietnameseLunarCalendar.getLunarDate(currentYearMonth.atEndOfMonth()) }
    val lunarMonthRangeText = remember(lunarFirst, lunarLast) {
        if (lunarFirst.month == lunarLast.month) {
            "Âm lịch: Tháng ${lunarFirst.month}"
        } else {
            "Âm lịch: Tháng ${lunarFirst.month} - Tháng ${lunarLast.month}"
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable { showMonthYearPicker = true }
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Lịch Tháng ${currentYearMonth.monthValue}, ${currentYearMonth.year}",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = "Chọn tháng năm",
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        Text(
                            text = lunarMonthRangeText,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { calendarViewModel.previousMonth() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Tháng trước",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { calendarViewModel.nextMonth() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Tháng sau",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Header hàng T2 .. CN
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                val daysOfWeek = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
                daysOfWeek.forEach { day ->
                    Text(
                        text = day,
                        fontWeight = FontWeight.Bold,
                        color = if (day == "CN") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Lưới ngày Calendar Grid (Chia thành các hàng 7 ngày)
            val monthDays = uiState.monthDays
            val weeks = remember(monthDays) { monthDays.chunked(7) }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                weeks.forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        week.forEach { item ->
                            key(item.solarDate) {
                                val isSelected = selectedCalendarDate != null && item.solarDate == selectedCalendarDate
                                Box(modifier = Modifier.weight(1f)) {
                                    DayCell(
                                        item = item,
                                        isSelected = isSelected,
                                        onClick = { calendarViewModel.selectDate(item.solarDate) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bố cục thông tin chi tiết ngày bấm chọn (Hiển thị tức thì 0ms)
            if (selectedCalendarDate != null) {
                Spacer(modifier = Modifier.height(10.dp))
                SelectedDateDetailCard(solarDate = selectedCalendarDate)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Khối Sự Kiện bên dưới (2 tab: Ngày lễ trong tháng & Sự kiện của tôi)
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                TabRow(
                    selectedTabIndex = selectedTabItem,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.primary,
                    indicator = { tabPositions ->
                        if (selectedTabItem < tabPositions.size) {
                            TabRowDefaults.SecondaryIndicator(
                                modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabItem]),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                ) {
                    Tab(
                        selected = selectedTabItem == 0,
                        onClick = { selectedTabItem = 0 },
                        text = {
                            Text(
                                text = "Ngày lễ trong tháng",
                                fontWeight = if (selectedTabItem == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabItem == 0) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabItem == 1,
                        onClick = { selectedTabItem = 1 },
                        text = {
                            Text(
                                text = "Sự kiện / Ngày Giỗ",
                                fontWeight = if (selectedTabItem == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTabItem == 1) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Danh sách chi tiết sự kiện
                if (selectedTabItem == 0) {
                    val holidays = getMonthHolidays(currentYearMonth.monthValue)
                    if (holidays.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Không có ngày lễ đặc biệt trong tháng này.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            holidays.forEach { (dateStr, title) ->
                                EventCardItem(title = title, subtitle = dateStr, badgeColor = EventDotAmber)
                            }
                        }
                    }
                } else {
                    if (allGioItems.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Chưa có ngày giỗ/sự kiện nào được lưu.",
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            allGioItems.forEach { item ->
                                EventCardItem(
                                    title = item.title,
                                    subtitle = "Ngày ${item.lunarDay}/${item.lunarMonth} Âm lịch ${if (item.note.isNotEmpty()) "• ${item.note}" else ""}",
                                    badgeColor = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    if (showMonthYearPicker) {
        MonthYearPickerDialog(
            initialYear = currentYearMonth.year,
            initialMonth = currentYearMonth.monthValue,
            onDismiss = { showMonthYearPicker = false },
            onConfirm = { year, month ->
                calendarViewModel.selectYearMonth(year, month)
                showMonthYearPicker = false
            }
        )
    }
}

@Composable
private fun SelectedDateDetailCard(
    solarDate: LocalDate
) {
    val lunar = remember(solarDate) { VietnameseLunarCalendar.getLunarDate(solarDate) }
    val monthLength = remember(solarDate) { VietnameseLunarCalendar.getLunarMonthLength(solarDate) }
    val isThangDu = monthLength >= 30

    val JadeGreen = Color(0xFF143527)
    val GoldImperial = Color(0xFFC68B27)
    val GoldBright = Color(0xFFECC66B)
    val GoldBorder = Color(0xFFB8860B)
    val LunarCanChiText = Color(0xFFD1DBD5)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .drawWithContent {
                // 1. Draw card background & content
                drawContent()

                // 2. Viền đôi mạ vàng mờ #C68B27 bên trong
                val goldColor = Color(0x60C68B27)
                val strokeWidth = 1.2.dp.toPx()
                val inset = 6.dp.toPx()

                val left = inset
                val top = inset
                val right = size.width - inset
                val bottom = size.height - inset

                drawRoundRect(
                    color = goldColor,
                    topLeft = Offset(left, top),
                    size = Size(right - left, bottom - top),
                    cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Hàng 1: Thứ mấy, Ngày Dương lịch bên trái, Tháng đủ/thiếu bên phải
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${getDayOfWeekFullName(solarDate.dayOfWeek.value)}, ${solarDate.dayOfMonth}/${solarDate.monthValue}/${solarDate.year}",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    fontFamily = FontFamily.Serif
                )

                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0x26C68B27),
                    border = BorderStroke(1.dp, GoldBorder)
                ) {
                    Text(
                        text = if (isThangDu) "Tháng đủ (30 ngày)" else "Tháng thiếu (29 ngày)",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldImperial,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Banner Âm Lịch (Nền Xanh Ngọc Bích sẫm #143527)
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = JadeGreen,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp)
                ) {
                    Text(
                        text = "Mùng ${lunar.day} tháng ${lunar.month} (Âm lịch)",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = GoldBright
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3 Badge Con Giáp Trang Trí Tròn
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ZodiacBadge(title = "Ngày", name = "${lunar.canDay} ${lunar.chiDay}", emoji = getZodiacEmoji(lunar.chiDay), modifier = Modifier.weight(1f))
                        ZodiacBadge(title = "Tháng", name = "${lunar.canMonth} ${lunar.chiMonth}", emoji = getZodiacEmoji(lunar.chiMonth), modifier = Modifier.weight(1f))
                        ZodiacBadge(title = "Năm", name = "${lunar.canYear} ${lunar.chiYear}", emoji = getZodiacEmoji(lunar.chiYear), modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pill Badge Hoàng Đạo / Hắc Đạo & Tiết Khí
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.Transparent,
                            border = BorderStroke(1.dp, GoldBorder)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(GoldImperial, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = lunar.loaiNgay,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldImperial
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = "Tiết ${lunar.tietKhi}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = LunarCanChiText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Giờ Hoàng Đạo & Giờ Hắc Đạo khi chọn ngày
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Giờ Hoàng Đạo: ${lunar.gioHoangDao}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    lineHeight = 16.sp
                )
                Text(
                    text = "Giờ Hắc Đạo: ${lunar.gioHacDao}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun MonthYearPickerDialog(
    initialYear: Int,
    initialMonth: Int,
    onDismiss: () -> Unit,
    onConfirm: (year: Int, month: Int) -> Unit
) {
    var pickerYear by remember { mutableIntStateOf(initialYear) }
    var pickerMonth by remember { mutableIntStateOf(initialMonth) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Chọn Tháng & Năm",
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Selector chọn Năm: < 2025 >
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = { pickerYear-- }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Năm trước",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Text(
                        text = "Năm $pickerYear",
                        fontFamily = FontFamily.Serif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { pickerYear++ }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Năm sau",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Lưới chọn Tháng (3 cột x 4 hàng)
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    items(12) { index ->
                        val monthNum = index + 1
                        val isSelected = monthNum == pickerMonth
                        Surface(
                            onClick = { pickerMonth = monthNum },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background,
                            border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline)
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 12.dp)
                            ) {
                                Text(
                                    text = "Tháng $monthNum",
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(pickerYear, pickerMonth)
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text("Chọn", color = MaterialTheme.colorScheme.onPrimary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
private fun DayCell(
    item: DayCalendarItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val isSunday = item.solarDate.dayOfWeek.value == 7
    val hasEvent = item.solarDate.dayOfMonth == 1 || item.lunarDate.day == 1 || item.lunarDate.day == 15

    val JadeGreen = Color(0xFF143527)
    val GoldImperial = Color(0xFFC68B27)

    // Nền: Ngày hôm nay tô màu xanh nhạt, ngày được chọn không tô màu (trắng surface)
    val backgroundColor by animateColorAsState(
        targetValue = when {
            item.isToday -> Color(0xFFE8F0EA) // Ngày hôm nay tô màu xanh nhạt
            else -> MaterialTheme.colorScheme.surface // Ngày chọn & ngày thường: không tô màu (trắng)
        },
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "CellBg"
    )

    // Viền: Khi chọn ngày khung màu vàng #C68B27, ngày hôm nay khung xanh #143527
    val borderColor by animateColorAsState(
        targetValue = when {
            isSelected -> GoldImperial // Khi chọn ngày: Khung màu vàng #C68B27
            item.isToday -> JadeGreen  // Ngày hôm nay: Khung màu xanh #143527
            else -> MaterialTheme.colorScheme.outline
        },
        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing),
        label = "CellBorder"
    )

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = backgroundColor,
        border = BorderStroke(
            width = if (isSelected || item.isToday) 2.dp else 0.5.dp,
            color = borderColor
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(2.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(2.dp)
        ) {
            // Số ngày Dương (khi chọn ngày không đổi màu, giữ màu trắng/đen theo theme)
            Text(
                text = "${item.solarDate.dayOfMonth}",
                fontSize = 15.sp,
                fontWeight = if (item.isToday || isSelected) FontWeight.Bold else FontWeight.SemiBold,
                color = when {
                    !item.isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f)
                    item.isToday -> MaterialTheme.colorScheme.primary
                    isSunday -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )

            // Số ngày Âm (giữ màu vàng)
            Text(
                text = if (item.lunarDate.day == 1 || item.lunarDate.day == 15) "${item.lunarDate.day}/${item.lunarDate.month}" else "${item.lunarDate.day}",
                fontSize = 11.sp,
                fontWeight = if (item.lunarDate.day == 1 || item.lunarDate.day == 15) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    !item.isCurrentMonth -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f)
                    else -> MaterialTheme.colorScheme.primary
                }
            )

            // Chấm sự kiện (GoldImperial)
            if (hasEvent && item.isCurrentMonth) {
                Spacer(modifier = Modifier.height(2.dp))
                Box(
                    modifier = Modifier
                        .size(4.dp)
                        .background(GoldImperial, CircleShape)
                )
            }
        }
    }
}

@Composable
private fun EventCardItem(
    title: String,
    subtitle: String,
    badgeColor: androidx.compose.ui.graphics.Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(badgeColor, CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

private fun getDayOfWeekFullName(dayOfWeek: Int): String {
    return when (dayOfWeek) {
        1 -> "Thứ Hai"
        2 -> "Thứ Ba"
        3 -> "Thứ Tư"
        4 -> "Thứ Năm"
        5 -> "Thứ Sáu"
        6 -> "Thứ Bảy"
        7 -> "Chủ Nhật"
        else -> ""
    }
}

private fun getMonthHolidays(month: Int): List<Pair<String, String>> {
    return when (month) {
        1 -> listOf("01/01" to "Tết Dương Lịch")
        2 -> listOf("03/02" to "Ngày thành lập Đảng CCSVN", "14/02" to "Lễ Tình nhân (Valentine)")
        3 -> listOf("08/03" to "Quốc tế Phụ nữ", "26/03" to "Ngày thành lập Đoàn TNCS Hồ Chí Minh")
        4 -> listOf("30/04" to "Ngày Giải phóng Miền Nam")
        5 -> listOf("01/05" to "Quốc tế Lao động", "19/05" to "Sinh nhật Chủ tịch Hồ Chí Minh")
        6 -> listOf("01/06" to "Quốc tế Thiếu nhi", "28/06" to "Ngày Gia đình Việt Nam")
        7 -> listOf("27/07" to "Ngày Thương binh Liệt sĩ")
        8 -> listOf("19/08" to "Ngày Cách mạng Tháng Tám")
        9 -> listOf("02/09" to "Ngày Quốc khánh Việt Nam")
        10 -> listOf("10/10" to "Ngày Giải phóng Thủ đô", "20/10" to "Ngày Phụ nữ Việt Nam")
        11 -> listOf("20/11" to "Ngày Nhà giáo Việt Nam")
        12 -> listOf("22/12" to "Ngày Thành lập QĐND Việt Nam", "25/12" to "Lễ Giáng sinh (Christmas)")
        else -> emptyList()
    }
}

@Composable
private fun ZodiacBadge(
    title: String,
    name: String,
    emoji: String,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0x25FFFFFF),
        border = BorderStroke(1.dp, Color(0x40FFFFFF)),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(24.dp)
                    .background(Color(0xFFC68B27), CircleShape)
            ) {
                Text(
                    text = emoji,
                    fontSize = 12.sp
                )
            }
            Spacer(modifier = Modifier.width(6.dp))
            Column {
                Text(
                    text = title,
                    fontSize = 10.sp,
                    color = Color(0xFFD1DBD5)
                )
                Text(
                    text = name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFFFFFF)
                )
            }
        }
    }
}

private fun getZodiacEmoji(chiName: String): String {
    return when {
        chiName.contains("Tý", ignoreCase = true) -> "🐁"
        chiName.contains("Sửu", ignoreCase = true) -> "🐂"
        chiName.contains("Dần", ignoreCase = true) -> "🐅"
        chiName.contains("Mão", ignoreCase = true) -> "🐈"
        chiName.contains("Thìn", ignoreCase = true) -> "🐉"
        chiName.contains("Tỵ", ignoreCase = true) -> "🐍"
        chiName.contains("Ngọ", ignoreCase = true) -> "🐎"
        chiName.contains("Mùi", ignoreCase = true) -> "🐐"
        chiName.contains("Thân", ignoreCase = true) -> "🐒"
        chiName.contains("Dậu", ignoreCase = true) -> "🐓"
        chiName.contains("Tuất", ignoreCase = true) -> "🐕"
        chiName.contains("Hợi", ignoreCase = true) -> "🐖"
        else -> "🧧"
    }
}
