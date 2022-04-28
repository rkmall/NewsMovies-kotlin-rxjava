package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.MovieListItemBinding

/**
 * A simple [RecyclerView.Adapter] subclass.
 * This adapter is attached to [com.rupesh.kotlinrxjavaex.view.MovieFragment]
 * and handles the View operations associated with the RecyclerView
 * @author Rupesh Mall
 * @since 1.0
 */
class MovieAdapter(
    val listenerForClick: (movie: Movie) -> Unit,
    val listenerForLongClick: (movie: Movie) -> Unit
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>()  {

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
        val movie = differ.currentList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MovieViewHolder(val binding: MovieListItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(_movie: Movie) {
            binding.movie = _movie
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
                    val selectedMovie = differ.currentList[position]
                    listenerForClick(selectedMovie)
                } else {
                    Toast.makeText(binding.root.context, "Internal error", Toast.LENGTH_LONG).show()
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
                    val selectedMovie = differ.currentList[position]
                    listenerForLongClick(selectedMovie)
                } else {
                    Toast.makeText(binding.root.context, "Internal error", Toast.LENGTH_LONG).show()
                }
                return@setOnLongClickListener true
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.originalTitle == newItem.originalTitle
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
}
