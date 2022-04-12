package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.databinding.MovieListItemBinding
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie

/**
 * A simple [RecyclerView.Adapter] subclass.
 * This adapter is attached to [com.rupesh.kotlinrxjavaex.view.MovieFragment]
 * and handles the View operations associated with the RecyclerView
 * @author Rupesh Mall
 * @since 1.0
 */
class MovieAdapter(
    val context: Context,
    val listenerForClick: (movie: Movie) -> Unit,
    val listenerForLongClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>()  {

    var movies: List<Movie> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val binding: MovieListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.movie_list_item,
            parent,
            false
        )
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setList(movieList: List<Movie>) {
        movies = movieList
        notifyDataSetChanged()
    }

    /**
     * Inner class MovieViewHolder
     */
    inner class MovieViewHolder(val binding: MovieListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(_movie: Movie) {
            binding.movie = _movie
            val posterPath = "${BuildConfig.MOVIE_POSTER_PATH}${_movie.posterPath}"

            Glide.with(binding.ivMovie.context)
                .load(posterPath)
                .into(binding.ivMovie)

            onMovieClick()
            onMovieLongClick()
        }

        /**
         * On Movie item click redirects to [com.rupesh.kotlinrxjavaex.view.MovieDetailActivity]
         * passing the selected Movie item to the destination
         */
        private fun onMovieClick() {
            binding.cvMovie.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    val selectedMovie = movies[position]
                    listenerForClick(selectedMovie)
                }
            }
        }

        /**
         * On Movie item long click gets the position of the selected item
         * and invokes the callback method implemented by
         * [com.rupesh.kotlinrxjavaex.view.MovieFragment]
         */
        private fun onMovieLongClick() {
            binding.cvMovie.setOnLongClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION) {
                    val selectedMovie = movies[position]
                    listenerForLongClick(selectedMovie)
                }else {
                    Toast.makeText(context, "Internal error", Toast.LENGTH_LONG).show()
                }

                return@setOnLongClickListener true
            }
        }
    }
}
