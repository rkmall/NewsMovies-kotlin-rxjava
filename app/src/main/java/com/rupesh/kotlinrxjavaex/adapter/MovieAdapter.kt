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

class MovieAdapter(
    val context: Context,
    val movies: ArrayList<Movie>,
    val listener: MovieFragListener
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>()  {


    interface MovieFragListener {
        fun onClickOk(movie: Movie)
    }

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

    inner class MovieViewHolder(val binding: MovieListItemBinding): RecyclerView.ViewHolder(binding.root) {

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

        fun onMovieLongClick() {
            itemView.setOnLongClickListener {
                val position = adapterPosition

                if(position != RecyclerView.NO_POSITION) {
                    val selectedMovie = movies[position]
                    listener.onClickOk(selectedMovie)
                }else {
                    Toast.makeText(context, "Internal error", Toast.LENGTH_LONG).show()
                }
                return@setOnLongClickListener true
            }
        }
    }
}
