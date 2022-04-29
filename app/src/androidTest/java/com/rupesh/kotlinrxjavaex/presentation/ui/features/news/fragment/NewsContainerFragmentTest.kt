package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.fragment

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.launchFragmentInHiltContainer
import com.rupesh.kotlinrxjavaex.presentation.ui.CustomMatchersHelper
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
        launchFragmentInHiltContainer<NewsContainerFragment>()
    }

    @Test
    fun newsContainerFragment_correctViews_are_inflated() {
        onView(withId(R.id.news_container_fragment)).check(matches(isDisplayed()))
        onView(withId(R.id.tb_news_frag)).check(matches(isDisplayed()))
        onView(withId(R.id.news_tab_layout)).check(matches(isDisplayed()))
        onView(withId(R.id.news_view_pager)).check(matches(isDisplayed()))

        onView(withId(R.id.sv_news)).check(matches(isDisplayed()))
        onView(withId(R.id.rv_news)).check(matches(isDisplayed()))
    }

    @Test
    fun newsContainerFragment_toolbar_displays_correct_title() {
        onView(withId(R.id.tb_news_frag)).check(matches(hasDescendant(withText("News"))))
    }

    @Test
    fun newsContainerFragment_tabLayout_displays_correct_titles() {
        onView(withText(R.string.newsfrag_tab1_title)).check(matches(isDisplayed()))
        onView(withText(R.string.newsfrag_tab2_title)).check(matches(isDisplayed()))
    }

    @Test
    fun newsContainerFragment_selectTab_displays_correct_fragment() {
        onView(withId(R.id.news_tab_layout))
            .perform(CustomMatchersHelper.selectTabAtPosition(1))
        onView(withId(R.id.news_saved_fragment)).check(matches(isDisplayed()))

        onView(withId(R.id.news_tab_layout))
            .perform(CustomMatchersHelper.selectTabAtPosition(0))
        onView(withId(R.id.news_fragment)).check(matches(isDisplayed()))
    }
}