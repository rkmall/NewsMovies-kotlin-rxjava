package com.rupesh.kotlinrxjavaex.presentation.ui

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rupesh.kotlinrxjavaex.R
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
        ActivityScenario.launch(MainActivity::class.java)
    }

    @Test
    fun testActivity_views_displayed() {
        onView(withId(R.id.main)).check(matches(isDisplayed()))
        onView(withId(R.id.frame_layout_main)).check(matches(isDisplayed()))
        onView(withId(R.id.bnv_main)).check(matches(isDisplayed()))
    }

    @Test
    fun testActivity_frameLayoutMain_displays_newsContainerFragment_onInitialization() {
        onView(withId(R.id.news_container_fragment)).check(matches(isDisplayed()))
    }

    @Test
    fun testActivity_bottomNavView_frameLayoutMain_fragment_replacements() {
        onView(withId(R.id.movies)).perform(click())
        onView(withId(R.id.movie_container_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.news)).perform(click())
        onView(withId(R.id.news_container_fragment)).check(matches(isDisplayed()))
    }
}