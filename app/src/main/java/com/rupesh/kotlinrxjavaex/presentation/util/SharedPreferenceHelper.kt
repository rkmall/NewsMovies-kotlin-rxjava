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
    fun storeFirstRun() {
        sharedPrefFirstRun.edit().also {
            it.putBoolean(AppConstPresentation.IS_FIRST_RUN, true)
            it.apply()
        }
    }

    fun storeSubsequentRun() {
        sharedPrefFirstRun.edit().also {
            it.putBoolean(AppConstPresentation.IS_FIRST_RUN, false)
            it.apply()
        }
    }
    /**
     * @return App's first run status
     */
    fun getIsFirstRunDone(): Boolean {
        return sharedPrefFirstRun.getBoolean(AppConstPresentation.IS_FIRST_RUN, false)
    }

    /**
     * Store App's current time in minutes
     */
    fun storeCurrentTime(currentTime: Long) {
        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(currentTime)
        sharedPrefTime.edit().also {
            it.putLong(AppConstPresentation.CURRENT_TIME, currentTimeInMin )
            it.apply()
        }
    }

    /**
     * @return App's stored time status
     */
    fun getStoredTime(): Long {
        return sharedPrefTime.getLong(AppConstPresentation.CURRENT_TIME, 15L)
    }
}