package com.rupesh.kotlinrxjavaex.presentation.util

import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class SharedPreferenceHelper(
    private val sharedPrefFirstRun: SharedPreferences,
    private val sharedPrefTime: SharedPreferences,
) {

    fun storeFirstRunDone() {
        sharedPrefFirstRun.edit().also {
            it.putBoolean(AppConstPresentation.PREF_IS_FIRST_RUN, false)
            it.apply()
        }
    }

    fun getIsFirstRun(): Boolean {
        return sharedPrefFirstRun.getBoolean(AppConstPresentation.PREF_IS_FIRST_RUN, true)
    }

    fun storeCurrentTime(currentTime: Long) {
        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(currentTime)
        sharedPrefTime.edit().also {
            it.putLong(AppConstPresentation.PREF_CURRENT_TIME, currentTimeInMin )
            it.apply()
        }
    }

    fun getStoredTime(): Long {
        return sharedPrefTime.getLong(AppConstPresentation.PREF_CURRENT_TIME, 5L)
    }
}