package com.rupesh.kotlinrxjavaex.presentation.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.databinding.WatchListItemBinding
import com.rupesh.kotlinrxjavaex.data.db.entity.DbMovie

/**
 * A simple [RecyclerView.Adapter] subclass.
 * This adapter is attached to [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
 * and handles the View operations associated with the RecyclerView
 * @author Rupesh Mall
 * @since 1.0
 */
class WatchListAdapter(
    val context: Context,
    val movies: ArrayList<DbMovie>,
    val listener: (dbMovie: DbMovie) -> Unit
): RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>()  {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WatchListAdapter.WatchListViewHolder {
        val binding: WatchListItemBinding = WatchListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WatchListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WatchListAdapter.WatchListViewHolder, position: Int) {
        holder.binding.tvWatchlistTitle.text = movies[position].title
        holder.binding.tvWatchlistDate.text = movies[position].releaseDate
        holder.binding.tvWatchlistRating.text = movies[position].rating.toString()

        holder.onRemoveButtonClicked()
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    /**
     * Inner class MovieViewHolder
     */
    inner class WatchListViewHolder(val binding: WatchListItemBinding): RecyclerView.ViewHolder(binding.root) {

        /**
         * On "Cancel" button click gets the position of the selected item
         * and invokes the callback method implemented by
         * [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
         */
        fun onRemoveButtonClicked() {
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