package com.rupesh.kotlinrxjavaex.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsContainerBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.NewsViewPagerAdapter
import com.rupesh.kotlinrxjavaex.view.activity.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * A simple [Fragment] subclass.
 * Use the [NewsContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsContainerFragment : Fragment() {

    private lateinit var binding: FragmentNewsContainerBinding
    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var newsViewPagerAdapter: NewsViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_news_container, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavView = (activity as MainActivity).bnv_main
        if(bottomNavView.visibility == View.INVISIBLE) {
            bottomNavView.visibility = View.VISIBLE
        }

        setToolbar()
        setViewPager()
        setTabLayout()
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

    override fun onDestroy() {
        super.onDestroy()
    }
}