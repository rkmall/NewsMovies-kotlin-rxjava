package com.rupesh.kotlinrxjavaex.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.NewsAdapter
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.NewsViewModel

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : Fragment() {

    private lateinit var fragmentNewsBinding: FragmentNewsBinding

    private  lateinit var viewModel: NewsViewModel

    private lateinit var newsAdapter: NewsAdapter

    private var defaultCountry = "us"
    private var defaultPage = 1


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentNewsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)

        return fragmentNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).newsViewModel

        initRV()

        getNewsArticle()
    }

    private fun getNewsArticle() {
        viewModel.getMovieList(defaultCountry, defaultPage)
        viewModel.newsLiveDataResult.observe(viewLifecycleOwner, Observer {
            val articleList = it as ArrayList<NewsArticle>
            newsAdapter.differ.submitList(articleList)
        })
    }

    fun displayToastMessage() {
        viewModel.statusMessageResult.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initRV() {
        fragmentNewsBinding.rvNews.apply {
            newsAdapter = NewsAdapter()
            layoutManager = LinearLayoutManager(activity)
            this.adapter = newsAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.clear()
    }
}