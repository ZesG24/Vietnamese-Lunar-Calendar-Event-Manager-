package com.example.amlich

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EventNote
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.example.amlich.reminder.GioReminderWorker
import com.example.amlich.ui.CalendarViewModel
import com.example.amlich.ui.GioViewModel
import com.example.amlich.ui.screens.GioScreen
import com.example.amlich.ui.screens.HomeScreen

class MainActivity : ComponentActivity() {

    private val calendarViewModel: CalendarViewModel by viewModels()
    private val gioViewModel: GioViewModel by viewModels()

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schedule daily background reminder worker
        GioReminderWorker.scheduleDailyWorker(this)

        // Request POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }

        setContent {
            MainAppScreen(
                calendarViewModel = calendarViewModel,
                gioViewModel = gioViewModel
            )
        }
    }
}

@Composable
fun MainAppScreen(
    calendarViewModel: CalendarViewModel,
    gioViewModel: GioViewModel
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color(0xFF8B0000)) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.CalendarMonth, contentDescription = "Lịch Âm", tint = Color.White) },
                    label = { Text("Lịch Âm", color = Color.White) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFA52A2A)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.EventNote, contentDescription = "Ngày Giỗ", tint = Color.White) },
                    label = { Text("Ngày Giỗ", color = Color.White) },
                    colors = NavigationBarItemDefaults.colors(
                        indicatorColor = Color(0xFFA52A2A)
                    )
                )
            }
        }
    ) { innerPadding ->
        Modifier.padding(innerPadding)
        when (selectedTab) {
            0 -> HomeScreen(viewModel = calendarViewModel)
            1 -> GioScreen(viewModel = gioViewModel)
        }
    }
}
