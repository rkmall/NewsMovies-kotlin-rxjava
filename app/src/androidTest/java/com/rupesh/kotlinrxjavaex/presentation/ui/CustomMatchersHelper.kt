package com.rupesh.kotlinrxjavaex.presentation.ui

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import org.hamcrest.CoreMatchers
import org.hamcrest.Matcher


object CustomMatchersHelper {

    fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return CoreMatchers.allOf(
                    ViewMatchers.isDisplayed(),
                    ViewMatchers.isAssignableFrom(TabLayout::class.java)
                )
            }

            override fun getDescription(): String {
                return "tab at index $tabIndex"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("no tab at index $tabIndex"))
                        .build()
                tabAtIndex.select()
            }
        }
    }
}