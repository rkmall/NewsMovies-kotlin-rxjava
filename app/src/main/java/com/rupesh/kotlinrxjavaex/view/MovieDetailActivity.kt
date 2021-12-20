package com.rupesh.kotlinrxjavaex.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.databinding.ActivityMovieDetailBinding
import com.rupesh.kotlinrxjavaex.model.Movie
import com.rupesh.kotlinrxjavaex.utils.AppConstants

class MovieDetailActivity : AppCompatActivity() {

    private var binding: ActivityMovieDetailBinding? = null

    private var image: String? = null
    private var name: String? = null
    private var plot: String? = null
    private var rating: String? = null
    private var date: String? = null
    private var id: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMovieDetailBinding.inflate(layoutInflater)
        val view: View = binding!!.root
        setContentView(view)

        val toolbar = binding!!.toolbarMovieDetail
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val intentThatStartedThisActivity: Intent = intent
        if(intentThatStartedThisActivity.hasExtra("movie")) {
            val movie: Movie? = intentThatStartedThisActivity.getParcelableExtra("movie")
            image = movie!!.poster_path
            name = movie.original_title
            plot = movie.overview
            rating = movie.vote_average.toString()
            date = movie.release_date
            id = movie.id

            initCollapsingToolBar()

            val posterPath = "${AppConstants.POSTER_PATH}$image"

            Glide.with(this)
                .load(posterPath)
                .placeholder(R.drawable.loading)
                .into(binding!!.ivMovieDetailImage)

            binding!!.layoutContentMovieDetail.tvMovieDetailTitle.text = name
            binding!!.layoutContentMovieDetail.tvMovieDetailPlot.text = plot
            binding!!.layoutContentMovieDetail.tvMovieDetailRating.text = rating
            binding!!.layoutContentMovieDetail.tvMovieDetailReleaseDate.text = date
        } else {
            Toast.makeText(this, "No movie data found", Toast.LENGTH_LONG).show()
        }
    }

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
                    collapsingToolbarLayout.title = name
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = name
                    isShow = false
                }
            }
        })
    }
}