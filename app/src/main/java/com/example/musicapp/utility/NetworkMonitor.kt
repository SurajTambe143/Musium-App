package com.example.musicapp.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.widget.Toast

class NetworkMonitor(private val context: Context, val online: () -> Unit) {

    private var currentNetworkStatus = NetworkStatus.INITIAL
    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            val networkStatus = NetworkCheck.isOnline(context)
            if (!(currentNetworkStatus == NetworkStatus.INITIAL && networkStatus)) {
                Toast.makeText(context, "Online", Toast.LENGTH_SHORT).show()
                // Handle network available event
                online.invoke()
                currentNetworkStatus=NetworkStatus.ONLINE
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            // Handle network lost event
            Toast.makeText(context, "Network lost", Toast.LENGTH_SHORT).show()
            currentNetworkStatus=NetworkStatus.OFFLINE
        }
    }
    fun resetStatus(){
        currentNetworkStatus=NetworkStatus.INITIAL
    }

    fun startNetworkMonitoring() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val networkRequest = NetworkRequest.Builder()
                .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                .build()

            connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
        } else {
        }
    }

    fun stopNetworkMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}