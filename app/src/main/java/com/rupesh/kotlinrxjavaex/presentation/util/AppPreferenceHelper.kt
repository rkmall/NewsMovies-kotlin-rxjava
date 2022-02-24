package com.rupesh.kotlinrxjavaex.presentation.util

import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class AppPreferenceHelper(
    private val sharedPrefFirstRun: SharedPreferences,
    private val sharedPrefTime: SharedPreferences
) {

    /**
     * Store App's first run status. This is only triggered when getFirstFun() returns false
     */
    fun storeFirstRun() {
        sharedPrefFirstRun.edit().also {
            it.putBoolean(AppConstantsPresentation.FIRST_RUN_DONE, true)
            it.apply()
        }
    }

    /**
     * @return App's first run status
     */
    fun getIsFirstRunDone(): Boolean {
        return sharedPrefFirstRun.getBoolean(AppConstantsPresentation.FIRST_RUN_DONE, false)
    }

    /**
     * Store App's current time in minutes
     */
    fun storeCurrentTime(currentTime: Long) {
        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(currentTime)
        sharedPrefTime.edit().also {
            it.putLong(AppConstantsPresentation.CURRENT_TIME, currentTimeInMin )
            it.apply()
        }
    }

    /**
     * @return App's stored time status
     */
    fun getStoredTime(): Long {
        return sharedPrefTime.getLong(AppConstantsPresentation.CURRENT_TIME, 180L)
    }

}