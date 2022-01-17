package com.rupesh.kotlinrxjavaex.presentation.util

import java.util.concurrent.TimeUnit

object TimeChecker {

    /**
     * Get current time and calculate the time-interval with reference to passed time in minutes
     * @return long: time interval in minutes
     */
    fun checkTimePeriod(period: Long): Long {
        val currentTime: Long = System.currentTimeMillis()
        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(currentTime)

        return currentTimeInMin - period
    }
}