package com.rupesh.kotlinrxjavaex.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.databinding.WatchListItemBinding
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie

/**
 * A simple [RecyclerView.Adapter] subclass.
 * This adapter is attached to [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
 * and handles the View operations associated with the RecyclerView
 * @author Rupesh Mall
 * @since 1.0
 */
class WatchListAdapter(
    val context: Context,
    val listener: (dbMovie: DbMovie) -> Unit
): RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>()  {

    var movies: List<DbMovie> = ArrayList()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchListAdapter.WatchListViewHolder {
        val binding: WatchListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.watch_list_item,
            parent,
            false
        )
        return WatchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListAdapter.WatchListViewHolder, position: Int) {
        holder.bind(movies[position])
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    fun setList(movieList: List<DbMovie>) {
        movies = movieList
    }

    /**
     * Inner class MovieViewHolder
     */
    inner class WatchListViewHolder(val binding: WatchListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(_dbMovie: DbMovie) {
            binding.dbMovie = _dbMovie

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
                    val dbMovie: DbMovie = movies[position]
                    listener(dbMovie)
                }
            }
        }
    }
}