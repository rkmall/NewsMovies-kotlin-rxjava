package com.rupesh.kotlinrxjavaex.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.FragmentMovieDetailBinding
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.view.activity.MainActivity

/**
 * A simple [Fragment] subclass.
 * Use the [MovieDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovieDetailFragment : Fragment() {

    private lateinit var binding: FragmentMovieDetailBinding
    private lateinit var movieViewModel: MovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_movie_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = (activity as MainActivity).movieViewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.movieVM = movieViewModel

        setToolbar()
        getParcelizeMovie()
        initCollapsingToolBar()
    }

    private fun getParcelizeMovie() {
        val bundle = this.arguments

        if(bundle != null) {
            val movie = bundle.getParcelable<Movie>("movie")
            binding.movieVM!!.movie = movie   // data binding

            // Bind Movie image with the View
            val image = movie!!.posterPath
            val posterPath = "${BuildConfig.MOVIE_POSTER_PATH}$image"
            binding.let {

                Glide.with(this)
                    .load(posterPath)
                    .placeholder(R.drawable.loading)
                    .into(it.ivMovieDetailImage)
            }
        } else {
            Toast.makeText(requireContext(), "No movie data found", Toast.LENGTH_LONG).show()
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
}