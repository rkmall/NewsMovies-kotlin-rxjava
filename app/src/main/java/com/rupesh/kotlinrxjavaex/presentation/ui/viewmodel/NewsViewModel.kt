package com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.domain.usecase.news.*
import com.rupesh.kotlinrxjavaex.presentation.util.Event
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.MaybeObserver
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getAllNewsArticles: GetAllNewsArticles,
    private val getSearchedNewsArticle: GetSearchedNewsArticle,
    private val getSavedNewsArticles: GetSavedNewsArticles,
    private val saveNewsArticleToDb: SaveNewsArticleToDb,
    private val deleteNewsArticleFromDb: DeleteNewsArticleFromDb,
    private val deleteAll: DeleteAll
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    val newsLiveData: MutableLiveData<Resource<List<NewsArticle>>> = MutableLiveData()
    val newsLiveDataResult: LiveData<Resource<List<NewsArticle>>> get() = newsLiveData

    private val searchedNewsLiveData: MutableLiveData<Resource<List<NewsArticle>>> = MutableLiveData()
    val searchedNewsLivedataResult: LiveData<Resource<List<NewsArticle>>> get() = searchedNewsLiveData

    private val savedNewsArticlesLiveData: MutableLiveData<List<NewsArticle>> = MutableLiveData()
    val savedNewsArticlesLiveDataResult: LiveData<List<NewsArticle>> get() = savedNewsArticlesLiveData

    private val statusMessage = MutableLiveData<Event<String>>()
    val statusMessageResult get() = statusMessage

    /**
     * Gets a list of NewsArticle wrapped inside MutableLiveData
     */
    fun getNewsList(country: String, page: Int) {
        newsLiveData.value = Resource.Loading()
        getAllNewsArticles.execute(country, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Response<NewsResponse>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onNext(t: Response<NewsResponse>) {
                    val statusCode = t.code()
                    Log.i("MyTag", "onNextGetSearchedHeadlines response code: $statusCode")
                    if(statusCode == 200) {
                        newsLiveData.value = Resource.Success(t.body()!!.articles)
                    } else {
                        newsLiveData.value = Resource.Error(message = "$statusCode: Cannot fetch search results")
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onErrorGetTopHeadlines ${e.message}")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onCompleteGetTopHeadlines")
                }
            })
    }

    fun newsApiCall(country: String, page: Int) {
        newsLiveData.value = Resource.Loading()
        getAllNewsArticles.execute(country, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: Observer<Response<NewsResponse>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onNext(t: Response<NewsResponse>) {
                    val statusCode = t.code()
                    Log.i("MyTag", "onNext getTopHeadlines response code: $statusCode")
                    if(statusCode == 200) {
                        val list = t.body()!!.articles
                        newsLiveData.value = Resource.Success(list)
                        list.forEach {
                            cacheNewsArticle(it)
                        }
                    } else {
                        newsLiveData.value = Resource.Error("$statusCode: Cannot fetch search results")
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onError getTopHeadlines ${e.message}")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete getTopHeadlines")
                }
            })
    }

    private fun cacheNewsArticle(newsArticle: NewsArticle) {
        saveNewsArticleToDb.execute(newsArticle)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Long> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onSuccess(t: Long) {
                    Log.i("MyTag", "onSuccess cache news articles: $t")
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onError cache news articles: ${e.message}")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete cache news articles")
                }
            })
    }

    fun getCachedNewsArticles() {
        getSavedNewsArticles.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<NewsArticle>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onNext(t: List<NewsArticle>) {
                    Log.i("MyTag", "onNextGetSavedHeadlines: ${t.size}")
                    newsLiveData.value = Resource.Success(t)
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onErrorGetSavedNews: ${e.message}")
                    newsLiveData.value = Resource.Error("Could not fetch the articles")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onCompleteGetSavedNews")
                }
            })

    }

    fun getSearchedNewsList(country: String, searchQuery: String, page: Int) {
        searchedNewsLiveData.postValue(Resource.Loading())
        getSearchedNewsArticle.execute(country, searchQuery, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<NewsResponse>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: Response<NewsResponse>) {
                    val statusCode = t.code()
                    Log.i("MyTag", "onNext getSearchedHeadlines response code: $statusCode")
                    if(statusCode == 200) {
                        searchedNewsLiveData.value = Resource.Success(t.body()!!.articles)
                    } else {
                        searchedNewsLiveData.value = Resource.Error("$statusCode: Cannot fetch search results")
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onError getSearchedNews: ${e.message}")
                    searchedNewsLiveData.value = Resource.Error("Cannot fetch search results")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete getSearchedNews")
                }
            })
    }

    fun getSavedNewsArticles() {
        getSavedNewsArticles.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<NewsArticle>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onNext(t: List<NewsArticle>) {
                    Log.i("MyTag", "onNext getSavedHeadlines: ${t.size}")
                    savedNewsArticlesLiveData.value = t
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onError getSavedNews: ${e.message}")
                    statusMessage.value = Event("Could not fetch the saved articles")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete getSavedNews")
                }
            })
    }

    fun saveNewsArticle(newsArticle: NewsArticle) {
        saveNewsArticleToDb.execute(newsArticle)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Long> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onSuccess(t: Long) {
                    Log.i("MyTag", "onSuccess addNewsArticleToDb: $t")
                    statusMessage.value = Event("Article Saved")
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onError addNewsArticleToDb: ${e.message}")
                    statusMessage.value = Event("Could not save the article")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete addArticleToDb: Operation completed")
                }
            })
    }

    fun deleteNewsArticle(articleId: Int) {
        deleteNewsArticleFromDb.execute(articleId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onSuccess(t: Int) {
                    Log.i("MyTag", "onSuccess deleteNewsArticleFromDb: $t deleted")
                    statusMessage.value = Event("Article Deleted")
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onError deleteNewsArticleToDb: ${e.message}")
                    statusMessage.value = Event("Could not delete the article")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete deleteArticleToDb")
                }
            })
    }

    fun clearDb() {
        deleteAll.execute()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onSuccess(t: Int) {
                    Log.i("MyTag", "onSuccess clearDb: $t deleted")
                }

                override fun onError(e: Throwable) {
                    Log.i("MyTag", "onError clearDb: ${e.message}")
                }

                override fun onComplete() {
                    Log.i("MyTag", "onComplete clearDb")
                }
            })
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}