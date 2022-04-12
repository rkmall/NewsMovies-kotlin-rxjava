package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsContainerBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.MainActivity
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.features.news.adapter.NewsViewPagerAdapter
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodelfactory.NewsVMFactory
import com.rupesh.kotlinrxjavaex.presentation.util.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [NewsContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NewsContainerFragment : BaseFragment<FragmentNewsContainerBinding>() {

    @Inject
    lateinit var newsVMFactory: NewsVMFactory
    lateinit var newsViewModel: NewsViewModel

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var newsViewPagerAdapter: NewsViewPagerAdapter
    private lateinit var preferenceHelper: SharedPreferenceHelper


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = ViewModelProvider(this, newsVMFactory)[NewsViewModel::class.java]
        displayBottomViewNav()
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
        if(NetworkChecker.isNetWorkAvailable(requireContext())) {
            initiateCall()
            with(binding) { newsRefreshBtn.setVisibleGone() }
        }else {
            newsViewModel.getCachedNewsArticles()
            with(binding) { newsRefreshBtn.setVisible() }
            requireView().snackBar("Please turn on network and click refresh")
        }
    }

    private fun initiateCall() {
        val sharedPrefFirstRun = activity?.getSharedPreferences(AppConstantsPresentation.FIRST_RUN, Context.MODE_PRIVATE)
        val sharedPrefTimePeriod = activity?.getSharedPreferences(AppConstantsPresentation.TIME_PERIOD, Context.MODE_PRIVATE)
        preferenceHelper = SharedPreferenceHelper(sharedPrefFirstRun!!, sharedPrefTimePeriod!!)
        val duration = NetworkChecker.checkTimePeriod(preferenceHelper.getStoredTime())
        val isFirstRun = sharedPrefFirstRun.getBoolean(AppConstantsPresentation.IS_FIRST_RUN, false)

        if(isFirstRun) {
            refreshData()
            preferenceHelper.storeSubsequentRun()
        } else if(!isFirstRun && duration > AppConstantsPresentation.API_CALL_TIME) {
            refreshData()
        }else {
            newsViewModel.getCachedNewsArticles()
        }
    }

    private fun refreshData() {
        newsViewModel.clearDb()
        newsViewModel.newsApiCall(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS)
    }

    private fun setToolbar() {
        val toolbar = binding.tbNewsFrag
        toolbar.title = "News"
    }

    private fun displayBottomViewNav() {
        val bottomNavView = (activity as MainActivity).bnv_main

        if(bottomNavView.visibility == View.INVISIBLE) {
            bottomNavView.visibility = View.VISIBLE
        }
    }

    /**
     * Connect TabLayout with the ViewPager Adapter by setting a Listener on the tabs
     * When a user clicks the tab, get the tab position and set ViewPager adapter's
     * current item to that position
     */
    private fun setTabLayout() {
        tabLayout = binding.newsTabLayout

        TabLayoutMediator(tabLayout, viewPager2, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.newsfrag_tab1_title)
                1 -> tab.text = getString(R.string.newsfrag_tab2_title)
            }
        }).attach()
    }

    // Connect ViewPager2 with ViewPager Adapter
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
}