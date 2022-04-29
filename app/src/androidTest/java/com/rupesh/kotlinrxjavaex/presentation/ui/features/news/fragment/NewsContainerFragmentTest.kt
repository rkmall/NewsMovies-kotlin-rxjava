package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.fragment

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.rupesh.kotlinrxjavaex.CustomTestRunner
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NewsContainerFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun newContainerFragment_correctViews_are_inflated() {
        launchFragmentInHiltContainer<NewsContainerFragment>()
        onView(withId(R.id.news_container_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.tb_news_frag)).check(matches(isDisplayed()))
        onView(withId(R.id.news_tab_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.news_view_pager)).check(matches(isDisplayed()))
    }
}