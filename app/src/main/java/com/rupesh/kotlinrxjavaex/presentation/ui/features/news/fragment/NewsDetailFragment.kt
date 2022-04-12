package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsDetailBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodelfactory.NewsVMFactory
import com.rupesh.kotlinrxjavaex.presentation.util.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


/**
 * A simple [Fragment] subclass.
 * Use the [NewsDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding>() {

    @Inject
    lateinit var newsVMFactory: NewsVMFactory
    private val newsViewModel: NewsViewModel by viewModels{ newsVMFactory }

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
                requireView().snackBar(message)
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
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentNewsDetailBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_news_detail, container, false)
    }
}