package com.rupesh.kotlinrxjavaex.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsDetailBinding
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.view.activity.MainActivity


/**
 * A simple [Fragment] subclass.
 * Use the [NewsDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsDetailFragment : Fragment() {

    private lateinit var binding: FragmentNewsDetailBinding
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var newsArticle: NewsArticle

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_news_detail, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newsViewModel = (activity as MainActivity).newsViewModel

        setToolbar()
        getParcelableNewsArticle()
        loadNewsWebView()
        saveNewsArticle()
        observeStatusMessage()
    }

    private fun getParcelableNewsArticle() {
        val bundle = this.arguments

        if(bundle != null) {
            newsArticle = bundle.let {
                it.getParcelable <NewsArticle>("article") as NewsArticle
            }
        }
    }

    private fun loadNewsWebView() {
        // Set toolbar title with the article source name
        binding.tbNewsDetail.title = newsArticle.source?.name

        // Set web view
        binding.wvNews.apply {
            webViewClient = WebViewClient()
            newsArticle.url?.let { loadUrl(it) }
        }
    }

    private fun observeStatusMessage() {
        newsViewModel.statusMessageResult.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                Snackbar.make(requireActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private fun setToolbar() {
        val toolbar = binding.tbNewsDetail
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24)

        // Go back to the fragment that started this fragment
        toolbar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.popBackStack()
        }
    }

    private fun saveNewsArticle() {
        binding.fabNewsDetail.setOnClickListener {
            newsViewModel.saveNewsArticle(newsArticle)
                //Snackbar.make(requireActivity().findViewById(android.R.id.content), "Saved Successfully", Snackbar.LENGTH_LONG).show()
        }
    }
}