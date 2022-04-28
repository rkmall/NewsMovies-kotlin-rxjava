package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.fragment

import android.os.Bundle
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
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieContainerBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.adapter.MovieViewPagerAdapter
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.util.*
import dagger.hilt.android.AndroidEntryPoint

/**
 * A simple [Fragment] subclass.
 * Use the [MovieContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MovieContainerFragment : BaseFragment<FragmentMovieContainerBinding>() {

    private val movieViewModel: MovieViewModel by viewModels()

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var movieViewPagerAdapter: MovieViewPagerAdapter
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setViewPager()
        setTabLayout()
        initiateCall()
    }

    private fun initiateCall() {
        if(NetworkChecker.isNetWorkAvailable(requireContext())) {
            loadData()
        }else {
            loadData()
            requireView().snackBar("Please turn on network")
        }
    }

    private fun loadData() {
        movieViewModel.getPopularMovies()
        movieViewModel.getSavedMovies()
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
        tabLayoutMediator = TabLayoutMediator(tabLayout, viewPager2, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            when(position) {
                0 -> tab.text = getString(R.string.moviefrag_tab1_title)
                1 -> tab.text = getString(R.string.moviefrag_tab2_title)
            }
        }).apply { attach() }
    }

    private fun setViewPager() {
        val fragmentList = arrayListOf(
            MovieFragment(),
            WatchListFragment()
        )
        viewPager2 = binding.movieViewPager
        movieViewPagerAdapter = MovieViewPagerAdapter(fragmentList, childFragmentManager, lifecycle)
        viewPager2.adapter = movieViewPagerAdapter
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMovieContainerBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_movie_container, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}