package com.rupesh.kotlinrxjavaex.presentation.ui.features.news.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsBinding
import com.rupesh.kotlinrxjavaex.presentation.ui.features.BaseFragment
import com.rupesh.kotlinrxjavaex.presentation.ui.features.news.adapter.NewsAdapter
import com.rupesh.kotlinrxjavaex.presentation.util.*
import com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel.NewsViewModel
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

/**
 * A simple [Fragment] subclass.
 * Use the [NewsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class NewsFragment : BaseFragment<FragmentNewsBinding>() {

    private val viewModel: NewsViewModel by viewModels(ownerProducer = {requireParentFragment()})
    private lateinit var newsAdapter: NewsAdapter
    private var disposable: CompositeDisposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        observeTopHeadlines()
        searchNewsArticle()
        setSearchCloseButton()
    }

    private fun observeTopHeadlines() {
        viewModel.newsLiveDataResult.observe(requireParentFragment().viewLifecycleOwner) {
            if (it != null) {
                render(it)
            }
        }
    }

    private fun observeSearchedHeadlines() {
        viewModel.searchedNewsLivedataResult.observe(requireParentFragment().viewLifecycleOwner) {
            if(it != null) {
                 render(it)
            }
        }
    }

    private fun render(resource: Resource<List<NewsArticle>>) {
        when(resource) {
            is Resource.Loading -> onLoad()
            is Resource.Success -> onSuccess(resource)
            is Resource.Error -> onError(resource)
        }
    }

    private fun onLoad() = with(binding) {
        progressBarNews.setVisible()
    }

    private fun onSuccess(resource: Resource<List<NewsArticle>>) = with(binding) {
        progressBarNews.setVisibleGone()
        newsAdapter.differ.submitList(resource.data as? ArrayList<NewsArticle>)
    }

    private fun onError(resource: Resource<List<NewsArticle>>) = with(binding) {
        progressBarNews.setVisibleGone()
        requireParentFragment().requireView().snackBar(resource.message as String)
    }

    private fun searchNewsArticle() {
        Observable.create(ObservableOnSubscribe<String> { emitter ->
            setSearchQueryText(emitter)     // create Observable from queryString
        })
            .debounce(1000, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d);
                }

                override fun onNext(queryText: String) {
                    when {
                        queryText.isEmpty() -> observeTopHeadlines()
                        queryText.isNotEmpty() -> {
                            viewModel.getSearchedNewsList(
                                AppConstantsData.DEFAULT_COUNTRY_NEWS,
                                queryText,
                                AppConstantsData.DEFAULT_PAGE_NEWS
                            )
                            observeSearchedHeadlines()
                        }
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", e.message.toString())
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete")
                }
            })
    }

    private fun setSearchQueryText(emitter: ObservableEmitter<String>) {
        binding.svNews.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if(!emitter.isDisposed) {
                    emitter.onNext(newText)
                }
                return false
            }
        })
    }

    private fun setSearchCloseButton() {
        val searchCloseBtnId: Int = binding
            .svNews.context.resources.getIdentifier("android:id/search_close_btn", null, null)
        val closeBtn: ImageView = binding.svNews.findViewById(searchCloseBtnId)
        closeBtn.setOnClickListener {
            binding.svNews.setQuery("", true)
        }
    }

    private fun onNewsItemClick(article: NewsArticle) {
        val newsDetailFragment = NewsDetailFragment()
        val bundle = Bundle()
        bundle.putParcelable("article", article)
        newsDetailFragment.arguments = bundle
        activity?.transaction(R.id.frame_layout_main, newsDetailFragment)
    }

    private fun initRecyclerView() {
        binding.rvNews.apply {
            newsAdapter = NewsAdapter(requireContext()) { item -> onNewsItemClick(item) }
            layoutManager = LinearLayoutManager(activity)
            this.adapter = newsAdapter
        }
    }

    override fun getBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
    ): FragmentNewsBinding {
        return DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}
