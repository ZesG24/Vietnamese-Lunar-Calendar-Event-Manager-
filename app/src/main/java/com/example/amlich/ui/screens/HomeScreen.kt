package com.example.amlich.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.amlich.R
import com.example.amlich.core.VietnameseLunarCalendar
import com.example.amlich.ui.CalendarViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun HomeScreen(
    viewModel: CalendarViewModel
) {
    val today = remember { LocalDate.now() }
    val lunar = remember(today) { VietnameseLunarCalendar.getLunarDate(today) }

    // Đồng hồ thời gian thực (Giờ hiện tại)
    var currentTimeString by remember {
        mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")))
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTimeString = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))
            delay(1000)
        }
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // TOP HEADER: Logo & Tên thương hiệu "Lịch Âm & Sự Kiện"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_logo),
                    contentDescription = "Logo Lịch Âm & Sự Kiện",
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(10.dp))
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Lịch Âm & Sự Kiện",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // 1. THẺ TỜ LỊCH HÔM NAY (Today Card với viền đôi mạ vàng)
            TodayCalendarCard(
                solarDate = today,
                currentTimeString = currentTimeString,
                lunarDay = lunar.day,
                lunarMonth = lunar.month,
                lunarYear = lunar.year,
                canDay = lunar.canDay,
                chiDay = lunar.chiDay,
                canMonth = lunar.canMonth,
                chiMonth = lunar.chiMonth,
                canYear = lunar.canYear,
                chiYear = lunar.chiYear,
                loaiNgay = lunar.loaiNgay
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 2. MỤC GIỜ HOÀNG ĐẠO & GIỜ HẮC ĐẠO
            LuckyHoursSection(
                gioHoangDaoText = lunar.gioHoangDao,
                gioHacDaoText = lunar.gioHacDao
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 3. MỤC THÀNH NGỮ - TỤC NGỮ (Quote Section)
            QuoteSection(solarDate = today)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TodayCalendarCard(
    solarDate: LocalDate,
    currentTimeString: String,
    lunarDay: Int,
    lunarMonth: Int,
    lunarYear: Int,
    canDay: String,
    chiDay: String,
    canMonth: String,
    chiMonth: String,
    canYear: String,
    chiYear: String,
    loaiNgay: String
) {
    val goldColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.35f)

    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .drawWithContent {
                drawContent()

                // Khung viền vàng mờ bên trong
                val strokeWidth = 1.2.dp.toPx()
                val inset = 8.dp.toPx()

                val left = inset
                val top = inset
                val right = size.width - inset
                val bottom = size.height - inset

                drawRoundRect(
                    color = goldColor,
                    topLeft = Offset(left, top),
                    size = Size(right - left, bottom - top),
                    cornerRadius = CornerRadius(18.dp.toPx(), 18.dp.toPx()),
                    style = Stroke(width = strokeWidth)
                )
            }
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Hàng 1: Bên trái "● CHỦ NHẬT", Bên phải "11:30"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "● ${getDayOfWeekFullName(solarDate.dayOfWeek.value).uppercase()}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = currentTimeString,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Hàng 2: Số ngày dương to nổi bật + Vạch dọc + Tháng M / NĂM YYYY
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "${solarDate.dayOfMonth}",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 52.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(end = 16.dp)
                )

                // Vạch phân cách dọc mảnh
                Box(
                    modifier = Modifier
                        .height(40.dp)
                        .width(1.dp)
                        .background(MaterialTheme.colorScheme.outline)
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = "Tháng ${solarDate.monthValue}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "NĂM ${solarDate.year}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Khối Âm Lịch (Lunar Banner - Nền surfaceVariant)
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "$lunarDay tháng $lunarMonth (Âm lịch)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // 3 Badge Con Giáp Dễ Thương
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        ZodiacBadge(title = "Ngày", name = "$canDay $chiDay", emoji = getZodiacEmoji(chiDay), modifier = Modifier.weight(1f))
                        ZodiacBadge(title = "Tháng", name = "$canMonth $chiMonth", emoji = getZodiacEmoji(chiMonth), modifier = Modifier.weight(1f))
                        ZodiacBadge(title = "Năm", name = "$canYear $chiYear", emoji = getZodiacEmoji(chiYear), modifier = Modifier.weight(1f))
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Pill Badge Ngày Hoàng Đạo / Hắc Đạo
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Text(
                            text = "● $loaiNgay",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun LuckyHoursSection(
    gioHoangDaoText: String,
    gioHacDaoText: String
) {
    val rawHoangDaoList = remember(gioHoangDaoText) {
        gioHoangDaoText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }
    val rawHacDaoList = remember(gioHacDaoText) {
        gioHacDaoText.split(",").map { it.trim() }.filter { it.isNotEmpty() }
    }
    val currentHour = LocalTime.now().hour

    Column(modifier = Modifier.fillMaxWidth()) {
        // 1. GIỜ HOÀNG ĐẠO
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Giờ Hoàng Đạo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            rawHoangDaoList.forEach { gio ->
                val isCurrentGio = checkIsCurrentGio(gio, currentHour)

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCurrentGio) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background,
                    border = BorderStroke(
                        width = 1.dp,
                        color = if (isCurrentGio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                ) {
                    Text(
                        text = if (isCurrentGio) "● $gio" else gio,
                        fontSize = 13.sp,
                        fontWeight = if (isCurrentGio) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrentGio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // 2. GIỜ HẮC ĐẠO
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Giờ Hắc Đạo",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            rawHacDaoList.forEach { gio ->
                val isCurrentGio = checkIsCurrentGio(gio, currentHour)

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = if (isCurrentGio) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.background.copy(alpha = 0.5f),
                    border = BorderStroke(
                        width = if (isCurrentGio) 1.dp else 0.5.dp,
                        color = if (isCurrentGio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                    )
                ) {
                    Text(
                        text = if (isCurrentGio) "● $gio" else gio,
                        fontSize = 13.sp,
                        fontWeight = if (isCurrentGio) FontWeight.Bold else FontWeight.Normal,
                        color = if (isCurrentGio) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuoteSection(solarDate: LocalDate) {
    val (quoteText, explanation) = remember(solarDate) {
        val quotes = listOf(
            Pair("“Lời nói chẳng mất tiền mua / Lựa lời mà nói cho vừa lòng nhau”", "Lời khuyên răn về cách ứng xử, lời ăn tiếng nói chuẩn mực trong cuộc sống hàng ngày."),
            Pair("“Ăn quả nhớ kẻ trồng cây / Có công mài sắt có ngày nên kim”", "Tôn vinh lòng biết ơn nguồn cội và sự kiên trì nhẫn nại hướng tới thành công."),
            Pair("“Nhiễu điều phủ lấy giá gương / Người trong một nước phải thương nhau cùng”", "Bài học sâu sắc về tinh thần đoàn kết, đùm bọc lẫn nhau trong cộng đồng."),
            Pair("“Công cha như núi Thái Sơn / Nghĩa mẹ như nước trong nguồn chảy ra”", "Nhắc nhở công ơn sinh thành dưỡng dục trời biển của cha mẹ đối với con cái."),
            Pair("“Lá lành đùm lá rách / Lành làm gáo vỡ làm môi”", "Tinh thần tương thân tương ái, giúp đỡ nhau lúc khó khăn hoạn nạn."),
            Pair("“Bầu ơi thương lấy bí cùng / Tuy rằng khác giống nhưng chung một giàn”", "Khuyên con người phải biết yêu thương, giúp đỡ lẫn nhau trong cuộc sống."),
            Pair("“Gần mực thì đen, gần đèn thì sáng”", "Bài học về môi trường sống và ảnh hưởng của bạn bè đối với nhân cách."),
            Pair("“Một cây làm chẳng nên non / Ba cây chụm lại nên hòn núi cao”", "Khẳng định sức mạnh vĩ đại của sự đoàn kết và hợp lực."),
            Pair("“Dù ai đi ngược về xuôi / Nhớ ngày Giỗ Tổ mùng mười tháng ba”", "Nhắc nhở đạo lý uống nước nhớ nguồn, luôn hướng về cội nguồn dân tộc."),
            Pair("“Anh em như thể tay chân / Rách lành bọc lót, dở hay đỡ đần”", "Tình anh em ruột thịt gắn bó khăng khít, luôn sẻ chia hoạn nạn."),
            Pair("“Thuận vợ thuận chồng, tát biển Đông cũng cạn”", "Khẳng định sự hòa thuận gia đình là nền tảng vượt qua mọi khó khăn."),
            Pair("“Muốn sang thì bắc cầu Kiều / Muốn con hay chữ thì yêu lấy thầy”", "Tôn sư trọng đạo, kính trọng thầy cô giáo dạy dỗ thành tài."),
            Pair("“Tốt gỗ hơn tốt nước sơn / Sống đàng hoàng hơn sống danh xưng”", "Giá trị thực chất bên trong luôn quan trọng hơn vẻ bề ngoài."),
            Pair("“Chớ thấy sóng cả mà ngã tay chèo”", "Dặn dò sự kiên định, không nản lòng trước sóng gió thử thách."),
            Pair("“Không ai giàu ba họ, không ai khó ba đời”", "Khuyên con người không tự mãn lúc thịnh, không nản chí lúc suy."),
            Pair("“Cây ngay không sợ chết đứng”", "Người sống trung thực, ngay thẳng không bao giờ sợ gièm pha."),
            Pair("“Đi một ngày đàng, học một học xá”", "Khuyên nhủ mở rộng tầm mắt, học hỏi kiến thức thực tế cuộc sống."),
            Pair("“Trời nào phụ kẻ có lòng / Đất nào nỡ phụ người công chí bền”", "Nỗ lực chân thành và sự kiên trì luôn thu hoạch quả ngọt."),
            Pair("“Giấy rách phải giữ lấy lề”", "Dù trong hoàn cảnh khó khăn vẫn giữ vững phẩm giá, đạo đức thanh cao."),
            Pair("“Uống nước nhớ nguồn / Ăn khoai nhớ kẻ cho mầm mà trồng”", "Đạo lý tri ân gia đình, tổ tiên và những người giúp đỡ mình."),
            Pair("“Kiên trì nhẫn nại thành công / Bền gan vững chí nối dòng hiển vinh”", "Lòng kiên trì là chìa khóa mở ra mọi đỉnh cao."),
            Pair("“Thấy người hoạn nạn thì thương / Thấy người tàn tật lại càng thương hơn”", "Lòng nhân ái, bao dung thương yêu con người."),
            Pair("“Học ăn, học nói, học gói, học mở”", "Nét văn hóa giao tiếp và ứng xử toàn diện của người Việt."),
            Pair("“Thất bại là mẹ thành công”", "Rút ra bài học từ vấp ngã để vững bước tiến xa hơn."),
            Pair("“Giàu đâu đến kẻ ngủ trưa / Sang đâu đến kẻ say sưa tối ngày”", "Nhắc nhở tinh thần siêng năng, cần cù lao động."),
            Pair("“Kính trên nhường dưới”", "Truyền thống lễ nghĩa, tôn trọng người lớn tuổi và yêu thương người nhỏ."),
            Pair("“Khéo ăn thì no, khéo co thì ấm”", "Nghệ thuật quản lý tài chính và thích nghi linh hoạt trong cuộc sống."),
            Pair("“Một điều lành bằng ngàn điều ước”", "Gieo mầm việc thiện luôn mang lại bình an lâu dài."),
            Pair("“Muốn ăn quả chín phải trồng cây / Muốn thành công lớn phải dày gian nan”", "Trân trọng thành quả lao động từ sự hy sinh kiên trì.")
        )
        val index = (solarDate.dayOfYear + solarDate.year) % quotes.size
        quotes[index]
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        // Header Mục
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(18.dp)
                    .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Thành ngữ - Tục ngữ",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                fontFamily = FontFamily.Serif
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Câu trích dẫn
                Text(
                    text = quoteText,
                    fontSize = 15.sp,
                    fontFamily = FontFamily.Serif,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Diễn nghĩa
                Text(
                    text = explanation,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Đường phân cách mờ
                HorizontalDivider(
                    thickness = 0.5.dp,
                    color = MaterialTheme.colorScheme.outline
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Nguồn trích dẫn
                Text(
                    text = "Ca dao & Tục ngữ Việt Nam",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
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

private fun checkIsCurrentGio(gioStr: String, currentHour: Int): Boolean {
    return when {
        gioStr.contains("Tý") && (currentHour >= 23 || currentHour < 1) -> true
        gioStr.contains("Sửu") && currentHour in 1..2 -> true
        gioStr.contains("Dần") && currentHour in 3..4 -> true
        gioStr.contains("Mão") && currentHour in 5..6 -> true
        gioStr.contains("Thìn") && currentHour in 7..8 -> true
        gioStr.contains("Tỵ") && currentHour in 9..10 -> true
        gioStr.contains("Ngọ") && currentHour in 11..12 -> true
        gioStr.contains("Mùi") && currentHour in 13..14 -> true
        gioStr.contains("Thân") && currentHour in 15..16 -> true
        gioStr.contains("Dậu") && currentHour in 17..18 -> true
        gioStr.contains("Tuất") && currentHour in 19..20 -> true
        gioStr.contains("Hợi") && currentHour in 21..22 -> true
        else -> false
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
