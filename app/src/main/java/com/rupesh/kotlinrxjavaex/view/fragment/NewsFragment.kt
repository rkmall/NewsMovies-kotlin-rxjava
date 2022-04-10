package com.rupesh.kotlinrxjavaex.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SearchView
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rupesh.kotlinrxjavaex.R
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.databinding.FragmentNewsBinding
import com.rupesh.kotlinrxjavaex.presentation.adapter.NewsAdapter
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
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
    private var disposable: CompositeDisposable = CompositeDisposable()
    val subject: PublishSubject<String> = PublishSubject.create()

    private var isProgressbarLoading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        fragmentNewsBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_news, container, false)
        return fragmentNewsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).newsViewModel
        initRecyclerView()
        observeTopHeadlines()
        searchNewsArticle()
        setSearchCloseButton()
    }


    private fun observeTopHeadlines() {
        viewModel.newsLiveDataResult.observe(requireParentFragment().viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    hideProgressbar()
                    val topHeadlines = it.data as? ArrayList<NewsArticle>
                    Log.i("newsList", "topListSize: ${topHeadlines?.size}")
                    newsAdapter.differ.submitList(topHeadlines)
                }
                is Resource.Error -> {
                    hideProgressbar()
                    makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> showProgressBar()
            }
        }
    }

    private fun observeSearchedHeadlines() {
     viewModel.searchedNewsLivedataResult.observe(requireParentFragment().viewLifecycleOwner) {
         when (it) {
             is Resource.Success -> {
                 hideProgressbar()
                 val searchedHeadlines = it.data as ArrayList<NewsArticle>
                 newsAdapter.differ.submitList(searchedHeadlines)
             }
             is Resource.Error -> {
                 hideProgressbar()
                 makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
             }
             is Resource.Loading -> {
                 showProgressBar()
             }
         }
     }
    }

    /**
     *
     * Subscribe PublishSubject first as it immediately start emitting values on creation
     */
    private fun searchNewsArticle() {
        disposable.add( subject
            // debounce of 1s on MainThread
            .debounce(3000, TimeUnit.MILLISECONDS, AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            // any future items emitted by PublishSubject will be available to this
            // observer as now the Observer is attached to PublishSubject before it has started emitting items
            .subscribeWith(object: DisposableObserver<String>()  {
                override fun onNext(t: String) {
                    // use the search query string (emitted)
                    if(t == "") {
                        observeTopHeadlines()
                    }else {
                        viewModel.getSearchedNewsList(AppConstantsData.DEFAULT_COUNTRY_NEWS, t, AppConstantsData.DEFAULT_PAGE_NEWS)
                        observeSearchedHeadlines()
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", e.message.toString())
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete")
                }
            })
        )
        setSearchQueryText() // search query string is set to PublishSubject here
    }

    /**
     * Set Search view with listener by pushing search query string to PublishSubject's onNext()
     * in OnQueryTextChange() method
     */
    private fun setSearchQueryText() {
        fragmentNewsBinding.svNews.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // do nothing, continue as is
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                when {
                    newText == null -> makeText(requireContext(), "Could not fetch search results", Toast.LENGTH_LONG).show()
                    newText.isNotEmpty() -> subject.onNext(newText)
                    newText.isEmpty() -> subject.onNext("")
                }
                return true
            }
        })
    }

    private fun setSearchCloseButton() {
        val searchCloseBtnId: Int = fragmentNewsBinding
            .svNews.context.resources.getIdentifier("android:id/search_close_btn", null, null)

        val closeBtn: ImageView = fragmentNewsBinding.svNews.findViewById(searchCloseBtnId)
        closeBtn.setOnClickListener {
            fragmentNewsBinding.svNews.setQuery("", true)
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

    private fun initRecyclerView() {
        fragmentNewsBinding.rvNews.apply {
            newsAdapter = NewsAdapter(requireContext()) { item -> onNewsItemClick(item) }
            layoutManager = LinearLayoutManager(activity)
            this.adapter = newsAdapter
        }
    }

    private fun showProgressBar() {
        isProgressbarLoading = true
        fragmentNewsBinding.progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressbar() {
        isProgressbarLoading = false
        fragmentNewsBinding.progressBar.visibility = View.INVISIBLE
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
