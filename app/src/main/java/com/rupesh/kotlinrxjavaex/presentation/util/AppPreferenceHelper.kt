package com.rupesh.kotlinrxjavaex.presentation.util

import android.content.Context
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit

class AppPreferenceHelper(
    val context: Context
) {

    companion object{
        const val FIRST_RUN = "FirstRun"
        const val FIRST_RUN_DONE = "FirstRunDone"

        const val TIME_PERIOD = "TimePeriod"
        const val CURRENT_TIME = "CurrentTime"
    }

    lateinit var sharedPref: SharedPreferences

    /**
     * Store App's first run status. This is only triggered when getFirstFun() returns false
     */
    fun storeFirstRun() {
        sharedPref = context.getSharedPreferences(FIRST_RUN, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(FIRST_RUN_DONE, true)
        editor.apply()
    }

    /**
     * @return App's first run status
     */
    fun getIsFirstRunDone(): Boolean{
        sharedPref = context.getSharedPreferences(FIRST_RUN, Context.MODE_PRIVATE)
        return sharedPref.getBoolean(FIRST_RUN_DONE, false)
    }

    /**
     * Store App's current time in minutes
     */
    fun storeCurrentTime(currentTime: Long){
        sharedPref = context.getSharedPreferences(TIME_PERIOD, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()

        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(currentTime)

        editor.putLong(CURRENT_TIME, currentTimeInMin )
        editor.apply()
    }

    /**
     * @return App's stored time status
     */
    fun getStoredTime(): Long{
        sharedPref = context.getSharedPreferences(TIME_PERIOD, Context.MODE_PRIVATE)
        return sharedPref.getLong(CURRENT_TIME, 180L)
    }

}