package com.example.musicapp.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log

object NetworkCheck {
    var networkStatus: NetworkStatus = NetworkStatus.OFFLINE
    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                networkStatus = NetworkStatus.ONLINE
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                networkStatus = NetworkStatus.ONLINE
                return true
            } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                networkStatus = NetworkStatus.ONLINE
                return true
            }
        }
        networkStatus = NetworkStatus.OFFLINE
        return false
    }


}
enum class NetworkStatus {
    ONLINE,
    OFFLINE,
    INITIAL
}