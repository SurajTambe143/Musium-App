package com.example.musicapp.utility

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat

class NotificationHelper(var context: Context) {

    private val notificationManager by lazy {
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }


    private fun getNotification(title: String, body: String): Notification {
        val notificationBuilder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setSound(null)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
        }
        return notificationBuilder.build()
    }

    fun sendNotification(title: String, body: String) {
        notificationManager.notify(NOTIFICATION_ID, getNotification(title, body))
    }


    private fun setNotificationWithProgressBar(progress: Int): Notification {
        val notificationBuilder =
            NotificationCompat.Builder(context, CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setTicker("")
                .setAutoCancel(false)
        if (progress >= 100) {
            notificationBuilder.setContentTitle("Download complete")
                .setContentText("Image has been saved")
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setProgress(0, 0, false)
        } else {
            notificationBuilder.setContentTitle("Download in progress")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setProgress(100, progress, false)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
        }
        return notificationBuilder.build()
    }

    fun sendProgressNotification(progress: Int) {
        notificationManager.notify(
            NOTIFICATION_DOWNLOAD_ID,
            setNotificationWithProgressBar(progress)
        )
    }

    fun cancelNotification(notificationID: Int) {
        notificationManager.cancel(notificationID)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createChannel() =
        NotificationChannel(
            CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = CHANNEL_DESCRIPTION
            setSound(null, null)
        }

    companion object {
        private const val CHANNEL_ID = "Prerna Union Bank"
        private const val CHANNEL_NAME = "Prerna Union Bank"
        private const val CHANNEL_DESCRIPTION = "Messages send by the chat app."

        private const val NOTIFICATION_ID = 1
        private const val NOTIFICATION_DOWNLOAD_ID = 2
    }
}