package com.example.musicapp.utility

import android.content.Context
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics


fun logEvent(context: Context?, eventName: String?, bundle: Bundle?) {
    // Initialise FirebaseAnalytics
    val firebaseAnalytics = FirebaseAnalytics.getInstance(context!!)
    // Log the event
    firebaseAnalytics.logEvent(eventName!!, bundle)
}
