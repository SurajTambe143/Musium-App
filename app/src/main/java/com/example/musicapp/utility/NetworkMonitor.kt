package com.example.musicapp.utility

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.view.View
import com.example.musicapp.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class NetworkMonitor(private val context: Context, binding: ActivityMainBinding, val online: () -> Unit) {

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
                Snackbar.make(binding.clMainActivityRoot,"Network available",Snackbar.LENGTH_SHORT).show()
                currentNetworkStatus=NetworkStatus.ONLINE
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            Snackbar.make(binding.clMainActivityRoot,"You are Offline",Snackbar.LENGTH_SHORT).show()
            binding.shimmerViewContainer.visibility= View.GONE
            // Handle network lost event
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
        }
    }

    fun stopNetworkMonitoring() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

}