package com.example.musicapp.utility

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate

object DayNightModeHelper {
    private const val TAG = "DayNightModeHelper"

    fun setDayMode(context: Context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        Log.e(TAG, "setDayMode: true ", )
    }

    fun setNightMode(context: Context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        Log.e(TAG, "setNightMode: true ", )
    }

    fun setAutoMode(context: Context) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}
