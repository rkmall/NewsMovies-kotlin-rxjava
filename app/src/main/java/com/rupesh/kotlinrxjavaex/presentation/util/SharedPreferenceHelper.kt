package com.rupesh.kotlinrxjavaex.presentation.util

import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class SharedPreferenceHelper(
    private val sharedPrefFirstRun: SharedPreferences,
    private val sharedPrefTime: SharedPreferences,
) {

    /**
     * Store App's first run status. This is only triggered when getFirstFun() returns false
     */
    fun storeFirstRunDone() {
        sharedPrefFirstRun.edit().also {
            it.putBoolean(AppConstPresentation.PREF_IS_FIRST_RUN, false)
            it.apply()
        }
    }

    /**
     * @return App's first run status
     */
    fun getIsFirstRun(): Boolean {
        return sharedPrefFirstRun.getBoolean(AppConstPresentation.PREF_IS_FIRST_RUN, true)
    }

    /**
     * Store App's current time in minutes
     */
    fun storeCurrentTime(currentTime: Long) {
        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(currentTime)
        sharedPrefTime.edit().also {
            it.putLong(AppConstPresentation.PREF_CURRENT_TIME, currentTimeInMin )
            it.apply()
        }
    }

    /**
     * @return App's stored time status
     */
    fun getStoredTime(): Long {
        return sharedPrefTime.getLong(AppConstPresentation.PREF_CURRENT_TIME, 5L)
    }
}