package com.rupesh.kotlinrxjavaex.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.ActivityMovieDetailBinding
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.MovieVMFactory
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * A simple [AppCompatActivity] subclass.
 * This class displays the detail info about [com.rupesh.kotlinrxjavaex.model.Movie]
 * @author Rupesh Mall
 * @since 1.0
 */
@AndroidEntryPoint
class MovieDetailActivity : AppCompatActivity() {

    private var binding: ActivityMovieDetailBinding? = null

    @Inject
    lateinit var movieVMFactory: MovieVMFactory

    private lateinit var movieViewModel: MovieViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        val view: View = binding!!.root
        setContentView(view)

        initToolbar()

        movieViewModel = ViewModelProvider(this, movieVMFactory)[MovieViewModel::class.java]

        binding!!.lifecycleOwner = this

        binding!!.movieVM = movieViewModel

        getParcelizeMovie()

        initCollapsingToolBar()
    }

    // Get Parcelable from MovieAdapter
    private fun getParcelizeMovie() {
        val intentThatStartedThisActivity: Intent = intent

        if(intentThatStartedThisActivity.hasExtra("movie")) {
            val movie: Movie? = intentThatStartedThisActivity.getParcelableExtra("movie")
            binding!!.movieVM!!.movie = movie   // data binding

            // Bind Movie image with the View
            val image = movie!!.poster_path
            val posterPath = "${BuildConfig.POSTER_PATH}$image"
            binding?.let {

                Glide.with(this)
                    .load(posterPath)
                    .placeholder(R.drawable.loading)
                    .into(it.ivMovieDetailImage)
            }
        } else {
            Toast.makeText(this, "No movie data found", Toast.LENGTH_LONG).show()
        }
    }

    private fun initToolbar() {
        val toolbar = binding!!.toolbarMovieDetail
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Initializes Collapsing Toolbar
     */
    private fun initCollapsingToolBar() {
        val collapsingToolbarLayout: CollapsingToolbarLayout = binding!!.ctMovieDetail
        collapsingToolbarLayout.title = " "

        val appBarLayout: AppBarLayout = binding!!.appbarMovieDetail
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