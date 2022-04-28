package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieDetailBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : BaseFragment<FragmentMovieDetailBinding>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        setMovieContents()
        initCollapsingToolBar()
    }

    private fun setMovieContents() {
        arguments?.let {
            val movie = it.getParcelable<Movie>("movie")
            binding.movie = movie   // data binding
        }
    }

    private fun setToolbar() {
        with(binding) {
            toolbarMovieDetail.setNavigationOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }
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