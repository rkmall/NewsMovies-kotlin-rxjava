package com.rupesh.kotlinrxjavaex.presentation.util

import com.google.common.truth.Truth.assertThat

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.*
import java.util.concurrent.TimeUnit

class SharedPreferenceHelperTest {

    private lateinit var context: Context
    private lateinit var sharedPrefFirstRun: SharedPreferences
    private lateinit var sharedPrefTime: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private lateinit var sharedPreferenceHelper: SharedPreferenceHelper

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        sharedPrefFirstRun = mock(SharedPreferences::class.java)
        sharedPrefTime = mock(SharedPreferences::class.java)
        prefEditor = mock(SharedPreferences.Editor::class.java)
        sharedPreferenceHelper = SharedPreferenceHelper(sharedPrefFirstRun, sharedPrefTime)
    }


    @Test
    fun `call to storeFirstRun sets FIRST_RUN_DONE as true and returns true in subsequent calls`() {
        `when`(prefEditor.putBoolean(AppConstPresentation.IS_FIRST_RUN, false))
            .thenReturn(prefEditor)

        `when`(sharedPrefFirstRun.edit()).thenReturn(prefEditor)

        `when`(context.getSharedPreferences(AppConstPresentation.FIRST_RUN, Context.MODE_PRIVATE))
            .thenReturn(sharedPrefFirstRun)

        sharedPreferenceHelper.storeFirstRun()
        verify(prefEditor, times(1)).apply()

        `when`(sharedPrefFirstRun.getBoolean(AppConstPresentation.IS_FIRST_RUN, false))
            .thenReturn(true)

        sharedPreferenceHelper.getIsFirstRunDone()
        verify(sharedPrefFirstRun, times( 1)).getBoolean(AppConstPresentation.IS_FIRST_RUN, false)
        assertThat(sharedPreferenceHelper.getIsFirstRunDone()).isTrue()
    }

    @Test
    fun `call to storeCurrentTime will sets CURRENT_TIME and returns stored CurrentTime in subsequent calls`() {

        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())

        `when`(prefEditor.putLong(AppConstPresentation.CURRENT_TIME, currentTimeInMin))
            .thenReturn(prefEditor)

        `when`(sharedPrefTime.edit()).thenReturn(prefEditor)

        `when`(context.getSharedPreferences(AppConstPresentation.TIME_PERIOD, Context.MODE_PRIVATE))
            .thenReturn(sharedPrefTime)

        `when`(sharedPrefTime.getLong(AppConstPresentation.CURRENT_TIME, 180L))
            .thenReturn(180L)

        sharedPreferenceHelper.storeCurrentTime(currentTimeInMin)
        verify(prefEditor, times(1)).apply()


        sharedPreferenceHelper.getStoredTime()
        verify(sharedPrefTime, times(1)).getLong(AppConstPresentation.CURRENT_TIME, 180L)
        assertThat(sharedPreferenceHelper.getStoredTime()).isEqualTo(180L)
    }

}