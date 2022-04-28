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
import com.rupesh.kotlinrxjavaex.databinding.WatchListItemBinding

/**
 * A simple [RecyclerView.Adapter] subclass.
 * This adapter is attached to [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
 * and handles the View operations associated with the RecyclerView
 * @author Rupesh Mall
 * @since 1.0
 */
class WatchListAdapter(
    val listener: (movie: Movie) -> Unit
): RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>(){

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
        val movie = differ.currentList[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class WatchListViewHolder(val binding: WatchListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(_movie: Movie) {
            binding.movie = _movie
            onRemoveButtonClicked()
        }

        private fun onRemoveButtonClicked() {
            binding.btnWatchlistRemove.setOnClickListener {
                val position = adapterPosition

                if(position != RecyclerView.NO_POSITION) {
                    val movie: Movie = differ.currentList[position]
                    listener(movie)
                } else {
                    Toast.makeText(binding.root.context, "Internal error", Toast.LENGTH_LONG).show()
                }
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