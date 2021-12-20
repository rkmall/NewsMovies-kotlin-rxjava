package com.rupesh.kotlinrxjavaex.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.databinding.MovieListItemBinding
import com.rupesh.kotlinrxjavaex.databinding.WatchListItemBinding
import com.rupesh.kotlinrxjavaex.db.entity.DbMovie

class WatchListAdapter(
    val context: Context,
    val movies: ArrayList<DbMovie>
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
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class WatchListViewHolder(val binding: WatchListItemBinding): RecyclerView.ViewHolder(binding.root) {

    }
}