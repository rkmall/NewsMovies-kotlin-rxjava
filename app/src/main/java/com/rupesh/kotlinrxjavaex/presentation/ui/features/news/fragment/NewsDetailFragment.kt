package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.util.fromNewsArticleToNewsSaved
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsDetailBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.presentation.util.snackBar
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [NewsDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding>() {

    private val newsViewModel: NewsViewModel by viewModels()
    private lateinit var newsArticle: NewsArticle

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        getParcelableNewsArticle()
        loadNewsWebView()
        saveNewsArticle()
        observeStatusMessage()
    }

    private fun getParcelableNewsArticle() {
        arguments?.let {
            newsArticle = it.getParcelable<NewsArticle>("article") as NewsArticle
        }
    }

    private fun loadNewsWebView() {
        binding.tbNewsDetail.title = newsArticle.source.name
        binding.wvNews.apply {
            webViewClient = WebViewClient()
            loadUrl(newsArticle.url)
        }
    }

    private fun observeStatusMessage() {
        newsViewModel.eventMessage.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { message ->
                requireView().snackBar(message)
            }
        }
    }

    private fun saveNewsArticle() {
        binding.fabNewsDetail.setOnClickListener {
            newsViewModel.saveArticle(fromNewsArticleToNewsSaved(newsArticle))
        }
    }

    private fun setToolbar() {
        with(binding) {
            tbNewsDetail.setNavigationOnClickListener {
                activity?.supportFragmentManager?.popBackStack()
            }
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentNewsDetailBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail, container, false)
    }

    override fun onDestroy() {
        binding.wvNews.stopLoading()
        super.onDestroy()
    }
}