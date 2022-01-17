package com.rupesh.kotlinrxjavaex.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieContainerBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.MovieViewPagerAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [MovieContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieContainerFragment : Fragment() {

    private lateinit var binding: FragmentMovieContainerBinding

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var movieViewPagerAdapter: MovieViewPagerAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_container, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setToolbar()

        /**
         * Note: ViewPager must be set first before calling setTabLayout() as
         *       the method uses the initialized ViewPager to set the Tabs
         */
        setViewPager()

        setTabLayout()
    }


    private fun setToolbar() {
        val toolbar = binding.tbMovieFrag
        toolbar.title = "Movies"
    }

    /**
     * Connect TabLayout with the ViewPager Adapter by setting a Listener on the tabs
     * When a user clicks the tab, get the tab position and set ViewPager adapter's
     * current item to that position
     */
    private fun setTabLayout() {
        tabLayout = binding.movieTabLayout

        TabLayoutMediator(tabLayout, viewPager2, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> {tab.text = "Movie Info"}
                1 -> tab.text = "Watch List"
            }
        }).attach()
    }

    // Connect ViewPager2 with ViewPager Adapter
    private fun setViewPager() {
        viewPager2 = binding.movieViewPager
        val fragmentManager: FragmentManager = childFragmentManager
        movieViewPagerAdapter = MovieViewPagerAdapter(fragmentManager, lifecycle)
        viewPager2.adapter = movieViewPagerAdapter
    }

    /**
     * Unregister ViewPager2 callback
     */
    override fun onDestroy() {
        super.onDestroy()
        //viewPager2.unregisterOnPageChangeCallback(viewPagerCallBack)
    }

    /**
     * Set the correct tab item when a user swipes the fragment
     * On ViewPager2 page change, gets the tabLayout position and
     * sets the correct tab as selected
     */
    /*private val viewPagerCallBack = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            tabLayout.selectTab(tabLayout.getTabAt(position))
        }
    }*/

    /*// Connect TabLayout with ViewPagerAdapter
    fun setTabLayout1() {
        tabLayout = binding.mainTabLayout
        tabLayout.addTab(tabLayout.newTab().setText("Movie Info"))
        tabLayout.addTab(tabLayout.newTab().setText("Watch List"))

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                viewPager2.currentItem = tab!!.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
    }*/
}