package com.rupesh.kotlinrxjavaex.presentation.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
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
    //val moviesList: ArrayList<DbMovie>,
    val listener: (dbMovie: DbMovie) -> Unit
): RecyclerView.Adapter<WatchListAdapter.WatchListViewHolder>(), Filterable {

    var moviesList: List<DbMovie> = ArrayList()
    var filteredList: List<DbMovie> = ArrayList()

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
        holder.bind(filteredList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    fun setList(movieList: List<DbMovie>) {
        this.moviesList = movieList
        this.filteredList = movieList
        this.notifyDataSetChanged()
    }

    /**
     * Inner class MovieViewHolder
     */
    inner class WatchListViewHolder(val binding: WatchListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(_dbMovie: DbMovie) {
            Log.i("FilterListObs", _dbMovie.toString())
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
                    val dbMovie: DbMovie = filteredList[position]
                    listener(dbMovie)
                }
            }
        }
    }

    /**
     * This overridable method implements the logic for filtering the list
     * @return Filter for the adapter
     */
    override fun getFilter(): Filter {
        return object: Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchString = constraint.toString()    // get user input

                if(searchString.isEmpty()) {
                    filteredList = moviesList   // when search string is empty
                }else {
                    val tempFilteredList: ArrayList<DbMovie> = ArrayList()
                    for(item in moviesList) {
                        if(item.title.lowercase().contains(searchString.lowercase())) {
                            tempFilteredList.add(item)
                        }
                    }
                    filteredList = tempFilteredList
                }
                val filteredResults = FilterResults()
                filteredResults.values = filteredList
                return filteredResults
            }

            // Notify recycler view about the change of the data set
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                Log.i("FilterList", results?.values.toString())
                filteredList = results?.values as List<DbMovie>
                notifyDataSetChanged()
            }
        }
    }
}