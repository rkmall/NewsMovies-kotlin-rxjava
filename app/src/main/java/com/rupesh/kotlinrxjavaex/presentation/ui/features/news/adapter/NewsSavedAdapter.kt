package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import com.rupesh.kotlinrxjavaex.databinding.SavedNewsListItemBinding

class NewsSavedAdapter(
    val listenerForClick: (article: NewsSaved) -> Unit
): RecyclerView.Adapter<NewsSavedAdapter.NewsSavedViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsSavedAdapter.NewsSavedViewHolder {
        val binding: SavedNewsListItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.saved_news_list_item,
            parent,
            false
        )
        return NewsSavedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NewsSavedViewHolder, position: Int) {
        val newsSaved = differ.currentList[position]
        holder.bind(newsSaved)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class NewsSavedViewHolder(val binding: SavedNewsListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(articleSaved: NewsSaved) {
            binding.newsSaved = articleSaved
            onNewsItemClick()
        }

        private fun onNewsItemClick() {
            binding.mainLayoutNews.setOnClickListener {
                val position = adapterPosition

                if(position != RecyclerView.NO_POSITION) {
                    val selectedArticle = differ.currentList[position]
                    listenerForClick(selectedArticle)
                } else {
                    Toast.makeText(binding.root.context, "Internal error", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<NewsSaved>() {
        override fun areItemsTheSame(oldItem: NewsSaved, newItem: NewsSaved): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: NewsSaved, newItem: NewsSaved): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, diffCallback)
}