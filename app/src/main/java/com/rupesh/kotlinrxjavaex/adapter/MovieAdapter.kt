package com.rupesh.kotlinrxjavaex.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.databinding.MovieListItemBinding
import com.rupesh.kotlinrxjavaex.model.Movie
import com.rupesh.kotlinrxjavaex.utils.AppConstants
import com.rupesh.kotlinrxjavaex.view.MovieDetailActivity

/**
 * A simple [RecyclerView.Adapter] subclass.
 * This adapter is attached to [com.rupesh.kotlinrxjavaex.view.MovieFragment]
 * and handles the View operations associated with the RecyclerView
 * @author Rupesh Mall
 * @since 1.0
 */
class MovieAdapter(
    val context: Context,
    val movies: ArrayList<Movie>,
    val listener: (movie: Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieAdapter.MovieViewHolder {
        val binding: MovieListItemBinding = MovieListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieAdapter.MovieViewHolder, position: Int) {
        holder.binding.tvTitle.text = movies[position].original_title
        holder.binding.tvRating.text = movies[position].vote_average.toString()

        val posterPath: String = "${AppConstants.POSTER_PATH}${movies[position].poster_path}"

        Glide.with(context)
            .load(posterPath)
            .placeholder(R.drawable.loading)
            .into(holder.binding.ivMovie)

        holder.onMovieClick()
        holder.onMovieLongClick()
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    /**
     * Inner class MovieViewHolder
     */
    inner class MovieViewHolder(val binding: MovieListItemBinding): RecyclerView.ViewHolder(binding.root) {

        /**
         * On Movie item click redirects to [com.rupesh.kotlinrxjavaex.view.MovieDetailActivity]
         * passing the selected Movie item to the destination
         */
        fun onMovieClick() {
            itemView.setOnClickListener {
                val position = adapterPosition

                if(position != RecyclerView.NO_POSITION) {
                    val selectedMovie = movies[position]
                    val intent = Intent(context, MovieDetailActivity::class.java)
                    intent.putExtra("movie", selectedMovie)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    val movieTitle = selectedMovie.original_title
                    Toast.makeText(it.context, movieTitle, Toast.LENGTH_LONG).show()
                    context.startActivity(intent)
                }
            }
        }

        /**
         * On Movie item long click gets the position of the selected item
         * and invokes the callback method implemented by
         * [com.rupesh.kotlinrxjavaex.view.MovieFragment]
         */
        fun onMovieLongClick() {
            itemView.setOnLongClickListener {
                val position = adapterPosition

                if(position != RecyclerView.NO_POSITION) {
                    val selectedMovie = movies[position]
                    listener(selectedMovie)
                }else {
                    Toast.makeText(context, "Internal error", Toast.LENGTH_LONG).show()
                }
                return@setOnLongClickListener true
            }
        }
    }
}
