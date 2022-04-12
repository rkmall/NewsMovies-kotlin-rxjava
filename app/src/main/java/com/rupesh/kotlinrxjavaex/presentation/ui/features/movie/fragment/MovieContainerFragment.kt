package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.fragment

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
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieContainerBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.adapter.MovieViewPagerAdapter
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodelfactory.MovieVMFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [MovieContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MovieContainerFragment : BaseFragment<FragmentMovieContainerBinding>() {

    @Inject
    lateinit var movieVMFactory: MovieVMFactory
    lateinit var movieViewModel: MovieViewModel

    private lateinit var viewPager2: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var movieViewPagerAdapter: MovieViewPagerAdapter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieViewModel = ViewModelProvider(this, movieVMFactory)[MovieViewModel::class.java]
        setToolbar()
        setViewPager()
        setTabLayout()
        loadData()
    }

    private fun loadData() {
        movieViewModel.getMovieList()
        movieViewModel.getAllMovieFromDb()
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

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMovieContainerBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_movie_container, container, false)
    }
}