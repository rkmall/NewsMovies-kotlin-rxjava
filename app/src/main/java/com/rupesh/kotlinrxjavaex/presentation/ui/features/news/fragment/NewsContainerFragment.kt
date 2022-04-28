package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsContainerBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.features.news.adapter.NewsViewPagerAdapter
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.presentation.util.*
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [NewsContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NewsContainerFragment : BaseFragment<FragmentNewsContainerBinding>() {

    private val newsViewModel: NewsViewModel by viewModels()

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var newsViewPagerAdapter: NewsViewPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator
    private lateinit var sharedPreference: SharedPreferenceHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPrefFirstRun = activity?.getSharedPreferences(AppConstPresentation.PREF_FIRST_RUN, Context.MODE_PRIVATE)
        val sharedPrefTimePeriod = activity?.getSharedPreferences(AppConstPresentation.PREF_TIME_PERIOD, Context.MODE_PRIVATE)
        sharedPreference = SharedPreferenceHelper(sharedPrefFirstRun!!, sharedPrefTimePeriod!!)

        setToolbar()
        setViewPager()
        setTabLayout()
        checkNetwork()
        setRefreshButton()
    }

    private fun setRefreshButton() {
        binding.newsRefreshBtn.setOnClickListener {
            checkNetwork()
        }
    }

    private fun checkNetwork() {
        val networkAvailable = NetworkChecker.isNetWorkAvailable(requireContext())
        val isFirstRun = sharedPreference.getIsFirstRun()

        when {
            networkAvailable -> {
                initiateCall()
                with(binding) { newsRefreshBtn.setVisibleGone() }
            }

            !networkAvailable && !isFirstRun -> {
                getCachedPlusSavedData()
                with(binding) { newsRefreshBtn.setVisible() }
                requireView().snackBar(AppConstPresentation.NO_NETWORK_REFRESH)
            }

            else -> {
                with(binding) { newsRefreshBtn.setVisible() }
                requireView().snackBar(AppConstPresentation.NO_NETWORK_REFRESH)
            }
        }
    }

    private fun initiateCall() {
        val duration = NetworkChecker.checkTimePeriod(sharedPreference.getStoredTime())
        Log.i(AppConstPresentation.LOG_UI, "Duration: $duration")
        val isFirstRun = sharedPreference.getIsFirstRun()
        when {
            isFirstRun -> firstCall()
            (!isFirstRun && duration > AppConstPresentation.API_CALL_TIME) -> refreshData()
            else -> getCachedPlusSavedData()
        }
    }

    private fun firstCall() {
        newsViewModel.newsApiCall(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS)
        sharedPreference.storeFirstRunDone()
        sharedPreference.storeCurrentTime(System.currentTimeMillis())
    }

    private fun refreshData() {
        newsViewModel.clearCache()
        newsViewModel.newsApiCall(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS)
        newsViewModel.getSavedArticles()
        sharedPreference.storeCurrentTime(System.currentTimeMillis())
    }

    private fun getCachedPlusSavedData() {
        newsViewModel.getCachedArticles()
        newsViewModel.getSavedArticles()
    }

    private fun setToolbar() {
        val toolbar = binding.tbNewsFrag
        toolbar.title = "News"
    }

    /**
     * Connect TabLayout with the ViewPager Adapter by setting a Listener on the tabs
     * When a user clicks the tab, get the tab position and set ViewPager adapter's
     * current item to that position
     */
    private fun setTabLayout() {
        tabLayout = binding.newsTabLayout

        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.newsfrag_tab1_title)
                1 -> tab.text = getString(R.string.newsfrag_tab2_title)
            }
        }).apply { attach() }
    }

    private fun setViewPager() {
        val fragmentList = arrayListOf(
            NewsFragment(),
            SavedNewsFragment()
        )
        viewPager2 = binding.newsViewPager
        newsViewPagerAdapter = NewsViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        viewPager2.adapter = newsViewPagerAdapter
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentNewsContainerBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_news_container, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}