package com.rupesh.kotlinrxjavaex.presentation.util

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
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
            it.putBoolean(AppConstantsPresentation.IS_FIRST_RUN, true)
            it.apply()
        }
    }

    fun storeSubsequentRun() {
        sharedPrefFirstRun.edit().also {
            it.putBoolean(AppConstantsPresentation.IS_FIRST_RUN, false)
            it.apply()
        }
    }
    /**
     * @return App's first run status
     */
    fun getIsFirstRunDone(): Boolean {
        return sharedPrefFirstRun.getBoolean(AppConstantsPresentation.IS_FIRST_RUN, false)
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
        return sharedPrefTime.getLong(AppConstantsPresentation.CURRENT_TIME, 15L)
    }
}