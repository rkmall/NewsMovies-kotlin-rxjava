package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import com.rupesh.kotlinrxjavaex.data.util.fromNewsSavedToNewsArticle
import com.rupesh.kotlinrxjavaex.databinding.FragmentSavedNewsBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.features.news.adapter.NewsSavedAdapter
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.presentation.util.snackBar
import com.rupesh.kotlinrxjavaex.presentation.util.transaction

/**
 * A simple [Fragment] subclass.
 * Use the [SavedNewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedNewsFragment : BaseFragment<FragmentSavedNewsBinding>() {

    private val newsViewModel: NewsViewModel by viewModels(ownerProducer = {requireParentFragment()})

    private lateinit var newsAdapter: NewsSavedAdapter
    private lateinit var newsArticleToBeDeleted: NewsSaved


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeSavedNewsArticles()
        observeStatusMessage()
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvSavedNews)
    }

    private fun observeSavedNewsArticles() {
        newsViewModel.savedNews.observe(requireParentFragment().viewLifecycleOwner) {
            newsAdapter.differ.submitList(it)
        }
    }

    private fun observeStatusMessage() {
        newsViewModel.eventMessage.observe(requireParentFragment().viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                when(message) {
                    "Article Deleted" -> {
                        Snackbar.make(
                            requireParentFragment().requireView(),
                            message,
                            Snackbar.LENGTH_LONG).apply {
                            setAction("Undo") {
                                newsViewModel.saveArticle(newsArticleToBeDeleted)
                            }.show()
                        }
                    }
                    else -> requireParentFragment().requireView().snackBar(message)
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvSavedNews.apply{
            newsAdapter = NewsSavedAdapter(requireContext()) { item -> onNewsItemClick(item) }
            layoutManager = LinearLayoutManager(requireContext())
            adapter = newsAdapter
        }
    }

    private fun onNewsItemClick(article: NewsSaved) {
        val newsDetailFragment = NewsDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable("article", fromNewsSavedToNewsArticle(article))
        newsDetailFragment.arguments = bundle
        activity?.transaction(R.id.frame_layout_main, newsDetailFragment)
    }

    private val itemTouchHelperCallback: ItemTouchHelper.Callback = object : ItemTouchHelper.SimpleCallback(
        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            newsArticleToBeDeleted = newsAdapter.differ.currentList[viewHolder.adapterPosition]
            newsViewModel.deleteSavedArticle(newsArticleToBeDeleted.id!!)
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentSavedNewsBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_saved_news, container, false)
    }
}