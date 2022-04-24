package com.rupesh.kotlinrxjavaex.presentation.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import java.util.concurrent.TimeUnit

object NetworkChecker {

    fun isNetWorkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capabilities = connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork) ?: return false

        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

    fun checkTimePeriod(period: Long): Long {
        val currentTime: Long = System.currentTimeMillis()
        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(currentTime)
        return currentTimeInMin - period
    }
}