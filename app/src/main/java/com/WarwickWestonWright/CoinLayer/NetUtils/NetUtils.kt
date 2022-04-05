package com.WarwickWestonWright.CoinLayer.NetUtils

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity

class NetUtils(private val iConnectionStatus: IConnectionStatus, context: AppCompatActivity) {

    interface IConnectionStatus { fun netStatusSet(isConnected: Boolean) }

    private lateinit var netCallback: ConnectivityManager.NetworkCallback
    private val connectivityManager: ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private var isConnected: Boolean = false

    fun setIsNetworkAvailable() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            isConnected = connectivityManager.activeNetworkInfo != null
            iConnectionStatus.netStatusSet(isConnected)
        }
        else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            val requestBuilder: NetworkRequest.Builder = NetworkRequest.Builder()
            requestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            requestBuilder.addCapability(NetworkCapabilities.NET_CAPABILITY_SUPL)
            val request = requestBuilder.build()
            var netAvailable = false
            netCallback = object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isConnected = true
                    iConnectionStatus.netStatusSet(isConnected)
                }
                override fun onLost(network: Network) {
                    isConnected = false
                    iConnectionStatus.netStatusSet(isConnected)
                }
                override fun onLosing(network: Network, maxMsToLive: Int) {
                    isConnected = false
                    iConnectionStatus.netStatusSet(isConnected)
                }
                override fun onUnavailable() {
                    //!netAvailable condition needed as for some reason onUnavailable gets called after onAvailable when network is available
                    if(!netAvailable) {
                        isConnected = false
                        iConnectionStatus.netStatusSet(isConnected)
                    }
                }
            }
            //Requires 'android.permission.CHANGE_NETWORK_STATE'
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                connectivityManager.requestNetwork(request, netCallback, 100)
            }
            connectivityManager.registerDefaultNetworkCallback(netCallback)
        }
    }
}