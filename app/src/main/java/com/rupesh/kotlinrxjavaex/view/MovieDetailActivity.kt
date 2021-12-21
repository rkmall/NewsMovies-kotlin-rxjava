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

/**
 * A simple [AppCompatActivity] subclass.
 * This class displays the detail info about [com.rupesh.kotlinrxjavaex.model.Movie]
 * @author Rupesh Mall
 * @since 1.0
 */
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

        // Get Parcelable from MovieAdapter
        val intentThatStartedThisActivity: Intent = intent

        if(intentThatStartedThisActivity.hasExtra("movie")) {

            val movie: Movie? = intentThatStartedThisActivity.getParcelableExtra("movie")

            movie?.let {
                image = it.poster_path
                name = it.original_title
                plot = it.overview
                rating = it.vote_average.toString()
                date = it.release_date
                id = it.id
            }

            // Init Collapsing toolbar
            initCollapsingToolBar()

            // Bind Movie object with the Views
            val posterPath = "${AppConstants.POSTER_PATH}$image"
            binding?.let {

                Glide.with(this)
                    .load(posterPath)
                    .placeholder(R.drawable.loading)
                    .into(it.ivMovieDetailImage)

                it.layoutContentMovieDetail.tvMovieDetailTitle.text = name
                it.layoutContentMovieDetail.tvMovieDetailPlot.text = plot
                it.layoutContentMovieDetail.tvMovieDetailRating.text = rating
                it.layoutContentMovieDetail.tvMovieDetailReleaseDate.text = date
            }
        } else {
            Toast.makeText(this, "No movie data found", Toast.LENGTH_LONG).show()
        }
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
                    collapsingToolbarLayout.title = " "
                    isShow = true
                } else if (isShow) {
                    collapsingToolbarLayout.title = name
                    isShow = false
                }
            }
        })
    }
}