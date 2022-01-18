package com.rupesh.kotlinrxjavaex.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.NewsAdapter
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.NewsViewModel
import com.rupesh.kotlinrxjavaex.view.activity.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

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

    private var disposable: CompositeDisposable = CompositeDisposable()

    val subject: PublishSubject<String> = PublishSubject.create()

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

        setToolbar()

        initRV()

        observeNewsList()

        searchNewsArticle()

        displayToastMessage()
    }

    // Observe the article list
    private fun observeNewsList() {
        viewModel.newsLiveDataResult.observe(viewLifecycleOwner, Observer {
            val articleList = it as ArrayList<NewsArticle>
            newsAdapter.differ.submitList(articleList)
        })
    }

    // Observe the searched news article list
    private fun observeSearchedNews() {
        viewModel.searchedNewsLivedataResult.observe(viewLifecycleOwner, Observer {
            val articleList = it as ArrayList<NewsArticle>
            newsAdapter.differ.submitList(articleList)
        })
    }

    /**
     *
     * Subscribe PublishSubject first as it immediately start emitting values on creation
     */
    private fun searchNewsArticle() {

        disposable.add( subject
            // debounce of 1s on MainThread
            .debounce(1000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .filter { str ->
                if (str.isEmpty()) {
                    observeNewsList()
                    false
                } else {
                    true
                }
            }
            .distinctUntilChanged()

            // Any future items emitted by PublishSubject will be available to this
            // Observer as now the Observer is attached to PublishSubject before it has started emitting items
            .subscribeWith(object: DisposableObserver<String>()  {
                override fun onNext(t: String) {
                    viewModel.getSearchedNewsList(defaultCountry, t, defaultPage) // use the search query string (emitted)
                    observeSearchedNews()
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", e.message.toString())
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete")
                }
            })
        )

        setSearchQueryText() // search query string is pushed to PublishSubject here
    }

    /**
     * Set Search view with listener by pushing search query string to PublishSubject' onNext()
     * in OnQueryTextChange() method
     */
    private fun setSearchQueryText() {
        fragmentNewsBinding.svNews.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                subject.onComplete()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    subject.onNext(newText) // set search query text, which is emitted by PublishSubject
                }else {
                    throw Exception("Internal RxJava Error")
                }
                return true
            }
        })
    }


    private fun displayToastMessage() {
        viewModel.statusMessageResult.observe(viewLifecycleOwner, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun setToolbar() {
        val toolbar = fragmentNewsBinding.tbNewsFrag
        toolbar.title = "News"
    }

    private fun initRV() {
        fragmentNewsBinding.rvNews.apply {
            newsAdapter = NewsAdapter()
            layoutManager = LinearLayoutManager(activity)
            this.adapter = newsAdapter
        }
    }

    override fun onPause() {
        super.onPause()
        disposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
        viewModel.clear()
    }

    /**
     * Set listener [OnQueryTextListener] to SearchView and implement the methods
     *//*
    private fun setSearchView() {
        fragmentNewsBinding.svNews.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            // This method is invoked when the user type search query and tap the enter button
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.getSearchedNewsList(defaultCountry, query.toString(), defaultPage)
                observeSearchedNews()
                return false
            }

            // This method is invoked for each text change in the Search View
            // This means each time a char is entered to removed, this method will be invoked
            override fun onQueryTextChange(newText: String?): Boolean {

                MainScope().launch {
                    delay(1000)      // delay one second to let user input search query
                    viewModel.getSearchedNewsList(defaultCountry, newText.toString(), defaultPage)
                    observeSearchedNews()
                }
                return false
            }
        })

        fragmentNewsBinding.svNews.setOnCloseListener(object : SearchView.OnCloseListener {
            override fun onClose(): Boolean {
                initRV()
                observeSearchedNews()
                return false
            }
        })
    }*/
}
