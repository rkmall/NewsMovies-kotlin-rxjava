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
                0 -> tab.text = getString(R.string.moviefrag_tab1_title)
                1 -> tab.text = getString(R.string.moviefrag_tab2_title)
            }
        }).attach()
    }

    // Connect ViewPager2 with ViewPager Adapter
    private fun setViewPager() {
        val fragmentList = arrayListOf(
            MovieFragment(),
            WatchListFragment()
        )
        viewPager2 = binding.movieViewPager
        movieViewPagerAdapter = MovieViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        viewPager2.adapter = movieViewPagerAdapter
    }

    /**
     * Unregister ViewPager2 callback
     */
    override fun onDestroy() {
        super.onDestroy()
    }
}