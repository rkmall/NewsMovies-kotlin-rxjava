package com.rupesh.kotlinrxjavaex.presentation.ui.features.movie.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.databinding.WatchListItemBinding

/**
 * A simple [RecyclerView.Adapter] subclass.
 * This adapter is attached to [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
 * and handles the View operations associated with the RecyclerView
 * @author Rupesh Mall
 * @since 1.0
 */
class WatchListAdapter(
    val context: Context,
    val listener: (movie: Movie) -> Unit
): RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>(){

    var moviesList: List<Movie> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchListViewHolder {
        val binding: WatchListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.watch_list_item,
            parent,
            false
        )
        return WatchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListViewHolder, position: Int) {
        holder.bind(moviesList[position])
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }

    fun setList(_movieList: List<Movie>) {
        moviesList = _movieList
        notifyDataSetChanged()
    }

    /**
     * Inner class MovieViewHolder
     */
    inner class WatchListViewHolder(val binding: WatchListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(_movie: Movie) {
            binding.movie = _movie
            onRemoveButtonClicked()
        }

        /**
         * On "Cancel" button click gets the position of the selected item
         * and invokes the callback method implemented by
         * [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
         */
        private fun onRemoveButtonClicked() {
            binding.btnWatchlistRemove.setOnClickListener {
                val position = adapterPosition

                if(position != RecyclerView.NO_POSITION) {
                    val movie: Movie = moviesList[position]
                    listener(movie)
                }
            }
        }
    }
}