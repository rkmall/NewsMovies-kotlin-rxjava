package com.rupesh.kotlinrxjavaex.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.databinding.FragmentSavedNewsBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.NewsAdapter
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.view.activity.MainActivity
import kotlinx.android.synthetic.main.layout_content_movie_detail.view.*

/**
 * A simple [Fragment] subclass.
 * Use the [SavedNewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SavedNewsFragment : Fragment() {

    private lateinit var binding: FragmentSavedNewsBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var newsArticleToBeDeleted: NewsArticle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_saved_news, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        newsViewModel = (activity as MainActivity).newsViewModel
        initRecyclerView()
        observeSavedNewsArticles()
        observeStatusMessage()
        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(binding.rvSavedNews)
    }

    private fun observeSavedNewsArticles() {
        newsViewModel.savedNewsArticlesLiveDataResult.observe(viewLifecycleOwner) {
            newsAdapter.differ.submitList(it)
        }
    }

    private fun observeStatusMessage() {
        newsViewModel.statusMessageResult.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                when(message) {
                    "Article Deleted" -> {
                        Snackbar.make(
                            requireActivity().findViewById(android.R.id.content),
                            message,
                            Snackbar.LENGTH_LONG).apply {
                            setAction("Undo") {
                                newsViewModel.saveNewsArticle(newsArticleToBeDeleted)
                            }.show()
                        }
                    }
                    else -> Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun initRecyclerView() {
        binding.rvSavedNews.apply{
            newsAdapter = NewsAdapter(requireContext()) { item -> onNewsItemClick(item) }
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = newsAdapter
        }
    }

    private fun onNewsItemClick(article: NewsArticle) {
        val newsDetailFragment = NewsDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable("article", article)
        newsDetailFragment.arguments = bundle

        val fm = activity?.supportFragmentManager
        val fragmentTransaction = fm?.beginTransaction()
        fragmentTransaction?.replace(R.id.frame_layout_main, newsDetailFragment)
        fragmentTransaction?.addToBackStack(null)
        fragmentTransaction?.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        newsViewModel.clear()
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
            newsViewModel.deleteNewsArticle(newsArticleToBeDeleted.id!!)
        }
    }
}