package io.github.caduviegas.carslist.infrastructure.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import io.github.caduviegas.carslist.R

object NotificationUtils {
    fun showSilentNotification(context: Context, title: String, message: String) {
        val channelId = "sync_lead_channel"
        val channel = NotificationChannel(
            channelId,
            "Sync Lead",
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setSound(null, null)
            enableVibration(false)
        }
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_car)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setAutoCancel(true)
            .build()
        manager.notify(1001, notification)
    }
}