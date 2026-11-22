package com.example.amlich.reminder

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.amlich.MainActivity
import com.example.amlich.data.GioRepository
import kotlinx.coroutines.flow.firstOrNull
import java.time.LocalDate
import java.util.concurrent.TimeUnit

class GioReminderWorker(
    private val context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repository = GioRepository(context)
        val allItems = repository.allGioItems.firstOrNull() ?: emptyList()
        val today = LocalDate.now()

        val upcomingItems = repository.calculateUpcomingGioItems(allItems, today)

        for (item in upcomingItems) {
            val reminderDaysBefore = item.item.reminderDaysBefore
            // Nếu hôm nay đúng là ngày nhắc nhở (VD: trước 1 ngày hoặc 3 ngày)
            if (item.daysRemaining == reminderDaysBefore.toLong()) {
                sendNotification(
                    id = item.item.id.toInt(),
                    title = "Nhắc nhở Ngày Giỗ Sắp Tới",
                    message = "Sắp đến ngày giỗ ${item.item.title} vào ngày ${item.item.lunarDay}/${item.item.lunarMonth} Âm lịch (còn $reminderDaysBefore ngày nữa)."
                )
            } else if (item.daysRemaining == 0L) {
                sendNotification(
                    id = item.item.id.toInt() + 1000,
                    title = "Hôm nay là Ngày Giỗ!",
                    message = "Hôm nay là ngày giỗ ${item.item.title} (${item.item.lunarDay}/${item.item.lunarMonth} Âm lịch)."
                )
            }
        }

        return Result.success()
    }

    private fun sendNotification(id: Int, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "gio_reminder_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Nhắc nhở Ngày Giỗ",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Kênh thông báo nhắc nhở ngày giỗ gia tộc"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            id,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        notificationManager.notify(id, notification)
    }

    companion object {
        private const val WORK_NAME = "GioDailyReminderWorker"

        fun scheduleDailyWorker(context: Context) {
            val workRequest = PeriodicWorkRequestBuilder<GioReminderWorker>(1, TimeUnit.DAYS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
        }
    }
}
