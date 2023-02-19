package com.myjokes.app.wearable.composestarter.presentation.retrofit

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.wifi.WifiManager
import android.os.Build
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class ConnectivityInterceptor(val ctx: Context) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        if (!ConnectionCheckUtils.checkOnline(ctx)) {
            throw NoConnectivityException(ctx, url)
        }
        return chain.proceed(request)
    }


    object ConnectionCheckUtils {


        fun checkOnline(context: Context): Boolean {
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw      = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    //for check internet over Bluetooth
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                return connectivityManager.activeNetworkInfo?.isConnected ?: false
            }
        }

        fun checkWifiOnAndConnected(context: Context): Boolean {
            val wifiMgr = context.getSystemService(Context.WIFI_SERVICE) as WifiManager?
            return if (wifiMgr!!.isWifiEnabled) { // Wi-Fi adapter is ON
                val wifiInfo = wifiMgr.connectionInfo

                wifiInfo.networkId != -1
                // Connected to an access point
            } else {
                false // Wi-Fi adapter is OFF
            }
        }
    }
    class NoConnectivityException(val ctx: Context, val url: String) : IOException() {

        override val message: String?
            get() = "No interent connection" + url
    }


}
