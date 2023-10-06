package com.example.musicapp.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkMonitor(private val context: Context,val online: () -> Unit, val offline:()->Unit) {

    private var currentNetworkStatus = NetworkStatus.INITIAL
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            val networkStatus = NetworkCheck.isOnline(context)
            if (!(currentNetworkStatus == NetworkStatus.INITIAL && networkStatus)) {
                // Handle network available event
                online.invoke()
                currentNetworkStatus=NetworkStatus.ONLINE
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            offline.invoke()
            // Handle network lost event
            currentNetworkStatus=NetworkStatus.OFFLINE
        }
    }
    fun resetStatus(){
        currentNetworkStatus=NetworkStatus.INITIAL
    }

    fun startNetworkMonitoring() {
        val networkRequest = NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .build()

        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    fun stopNetworkMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}