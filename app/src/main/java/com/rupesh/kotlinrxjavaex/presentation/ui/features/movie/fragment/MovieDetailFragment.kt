package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieDetailBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodelfactory.MovieVMFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    @Inject
    lateinit var movieVMFactory: MovieVMFactory
    private val movieViewModel: MovieViewModel by viewModels{ movieVMFactory }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.movieVM = movieViewModel

        setToolbar()
        getParcelizeMovie()
        initCollapsingToolBar()
    }

    private fun getParcelizeMovie() {
        arguments?.let {
            val movie = it.getParcelable<Movie>("movie")
            binding.movieVM?.movie = movie   // data binding
            val image = movie?.posterPath
            val posterPath = "${BuildConfig.MOVIE_POSTER_PATH}$image"

            binding.let {
                Glide.with(this)
                    .load(posterPath)
                    .placeholder(R.drawable.loading)
                    .into(it.ivMovieDetailImage)
            }
        }
    }

    private fun setToolbar() {
        val toolbar = binding.toolbarMovieDetail
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)

        // Go back to the fragment previous fragment
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    /**
     * Initializes Collapsing Toolbar
     */
    private fun initCollapsingToolBar() {
        val collapsingToolbarLayout: CollapsingToolbarLayout = binding.ctMovieDetail
        collapsingToolbarLayout.title = ""

        val appBarLayout: AppBarLayout = binding.appbarMovieDetail
        appBarLayout.setExpanded(true)

        appBarLayout.addOnOffsetChangedListener(object: AppBarLayout.OnOffsetChangedListener {

            var isShow = false
            var scrollRange = -1

            override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout!!.totalScrollRange
                }
                if (scrollRange + verticalOffset == 0) {
                    isShow = true
                } else if (isShow) {
                    isShow = false
                }
            }
        })
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentMovieDetailBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_movie_detail, container, false)
    }
}