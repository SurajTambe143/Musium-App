package com.example.musicapp.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.util.Log

class SongPlayingServices : Service() {

    var mp: MediaPlayer? = null
    private var myBinder = MyBinder()
    val TAG:String="Service"

    companion object {

        var mediaPlayer: MediaPlayer? = null

        var uri: Uri? = null
    }

    private var songList: com.example.musicapp.model_data.song_details.Hit? = null

    fun setMusicList(data : com.example.musicapp.model_data.song_details.Hit?) {
        Log.e("Service list :setMusicList method", data.toString() )
        songList = data
    }

    override fun onBind(p0: Intent?): IBinder {
        Log.e(TAG, "onBind: is called of service", )
        return myBinder
    }

    inner class MyBinder : Binder() {
        fun currentService(): SongPlayingServices {
            return this@SongPlayingServices
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG, "onStartCommand: is called of Service", )
        return START_NOT_STICKY
    }

    fun notificationChannelInit() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        }
    }

    fun showNotification() {
        Log.e(TAG, "showNotification: is called of Service", )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("songlist notification", songList.toString())
            val CHANNEL_ID = "1"
            val CHANNEL_NAME = "Musium"
            val DESCRIPTION_TEXT = "Music is playing"

            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            mChannel.description = DESCRIPTION_TEXT

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            val nt = Notification.Builder(this, CHANNEL_ID)
                .setContentTitle(songList?.result?.title)
                .setContentText(songList?.result?.artist_names)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .build()
            notificationManager.notify(122,nt)
            startForeground(122, nt)
        } else {
            val nt = Notification.Builder(this)
                .setContentTitle(songList?.result?.title)
                .setContentText(songList?.result?.artist_names)
                .build()
            startForeground(122, nt)
        }
    }

    override fun onDestroy() {
        Log.e(TAG, "onDestroy: is called of Service", )
        stopSelf()
//        Log.e("S", "onDestroy:")
//        mp?.stop()
    }
}