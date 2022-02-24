package com.rupesh.kotlinrxjavaex.presentation.util

import com.google.common.truth.Truth.assertThat

import android.content.Context
import android.content.SharedPreferences
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.*
import java.util.concurrent.TimeUnit

class AppPreferenceHelperTest {

    private lateinit var context: Context
    private lateinit var sharedPrefFirstRun: SharedPreferences
    private lateinit var sharedPrefTime: SharedPreferences
    private lateinit var prefEditor: SharedPreferences.Editor
    private lateinit var appPreferenceHelper: AppPreferenceHelper

    @Before
    fun setUp() {
        context = mock(Context::class.java)
        sharedPrefFirstRun = mock(SharedPreferences::class.java)
        sharedPrefTime = mock(SharedPreferences::class.java)
        prefEditor = mock(SharedPreferences.Editor::class.java)
        appPreferenceHelper = AppPreferenceHelper(sharedPrefFirstRun, sharedPrefTime)
    }


    @Test
    fun `call to storeFirstRun sets FIRST_RUN_DONE as true and returns true in subsequent calls`() {
        `when`(prefEditor.putBoolean(AppConstantsPresentation.FIRST_RUN_DONE, false))
            .thenReturn(prefEditor)

        `when`(sharedPrefFirstRun.edit()).thenReturn(prefEditor)

        `when`(context.getSharedPreferences(AppConstantsPresentation.FIRST_RUN, Context.MODE_PRIVATE))
            .thenReturn(sharedPrefFirstRun)

        appPreferenceHelper.storeFirstRun()
        verify(prefEditor, times(1)).apply()

        `when`(sharedPrefFirstRun.getBoolean(AppConstantsPresentation.FIRST_RUN_DONE, false))
            .thenReturn(true)

        appPreferenceHelper.getIsFirstRunDone()
        verify(sharedPrefFirstRun, times( 1)).getBoolean(AppConstantsPresentation.FIRST_RUN_DONE, false)
        assertThat(appPreferenceHelper.getIsFirstRunDone()).isTrue()
    }

    @Test
    fun `call to storeCurrentTime will sets CURRENT_TIME and returns stored CurrentTime in subsequent calls`() {

        val currentTimeInMin: Long = TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis())

        `when`(prefEditor.putLong(AppConstantsPresentation.CURRENT_TIME, currentTimeInMin))
            .thenReturn(prefEditor)

        `when`(sharedPrefTime.edit()).thenReturn(prefEditor)

        `when`(context.getSharedPreferences(AppConstantsPresentation.TIME_PERIOD, Context.MODE_PRIVATE))
            .thenReturn(sharedPrefTime)

        `when`(sharedPrefTime.getLong(AppConstantsPresentation.CURRENT_TIME, 180L))
            .thenReturn(180L)

        appPreferenceHelper.storeCurrentTime(currentTimeInMin)
        verify(prefEditor, times(1)).apply()


        appPreferenceHelper.getStoredTime()
        verify(sharedPrefTime, times(1)).getLong(AppConstantsPresentation.CURRENT_TIME, 180L)
        assertThat(appPreferenceHelper.getStoredTime()).isEqualTo(180L)
    }

}