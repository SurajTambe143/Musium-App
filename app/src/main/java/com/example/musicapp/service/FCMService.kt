package com.example.musicapp.service

import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.musicapp.R
import com.example.musicapp.home.MusiumApplication
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService: FirebaseMessagingService() {

    private val tAG = "tagged"
    val myMessage = "myMessage"

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)

        Log.e(tAG, "onMessageReceived Called")
        Log.e(tAG, "onMessageReceived: Message is received from: " + message.from)

        if (message.notification !=null){
            val title = message.notification!!.title
            val body = message.notification!!.body

            val notificationBuilder =NotificationCompat.Builder(this,MusiumApplication().channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .build()

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(1002,notificationBuilder)
        }
        if (message.data.isNotEmpty()) {
            Log.e(tAG, "onMessageReceived: Data: " + message.data.toString())
        }
    }
    override fun onNewToken(token: String) {
        super.onNewToken(token)

        Log.e(tAG, "onNewToken Called")
    }
}