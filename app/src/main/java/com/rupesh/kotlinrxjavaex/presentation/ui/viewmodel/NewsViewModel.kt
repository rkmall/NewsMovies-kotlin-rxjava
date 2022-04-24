package com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import com.rupesh.kotlinrxjavaex.domain.usecase.news.*
import com.rupesh.kotlinrxjavaex.presentation.util.AppConstPresentation
import com.rupesh.kotlinrxjavaex.presentation.util.Event
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.MaybeObserver
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val networkNewsUseCase: NetworkNewsUseCase,
    private val cacheNewsUseCase: CacheNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase,
) : ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    val mTopHeadlines: MutableLiveData<Resource<List<NewsArticle>>> = MutableLiveData()
    val topHeadlines: LiveData<Resource<List<NewsArticle>>> get() = mTopHeadlines

    private val mSearchedHeadlines: MutableLiveData<Resource<List<NewsArticle>>> = MutableLiveData()
    val searchedHeadlines: LiveData<Resource<List<NewsArticle>>> get() = mSearchedHeadlines

    private val mSavedNews: MutableLiveData<List<NewsSaved>> = MutableLiveData()
    val savedNews: LiveData<List<NewsSaved>> get() = mSavedNews

    private val mEventMessage = MutableLiveData<Event<String>>()
    val eventMessage get() = mEventMessage

    fun newsApiCall(country: String, page: Int) {
        mTopHeadlines.value = Resource.Loading()
        networkNewsUseCase.getTopHeadlines(country, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: SingleObserver<Resource<List<NewsArticle>>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onSuccess(t: Resource<List<NewsArticle>>) {
                    Log.i(AppConstPresentation.LOG_UI, "onNext getTopHeadlines ${t.data?.size}")
                    mTopHeadlines.value = t
                    t.data?.forEach {
                        cacheArticle(it)
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError getTopHeadlines ${e.message}")
                }
            })

    }

    fun cacheArticle(newsArticle: NewsArticle) {
        cacheNewsUseCase.addCacheArticle(newsArticle)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Long> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onSuccess(t: Long) {
                    Log.i(AppConstPresentation.LOG_UI, "onSuccess cache news articles: $t")
                    mEventMessage.value = Event("Cache news article success").also {
                        it.hasBeenHandled = true
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError cache news articles: ${e.message}")
                    mEventMessage.value = Event("Cache internal error").also {
                        it.hasBeenHandled = true
                    }
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete cache news articles")
                }
            })
    }

    fun getCachedArticles() {
        mTopHeadlines.value = Resource.Loading()
        cacheNewsUseCase.getCachedArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<NewsArticle>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onNext(t: List<NewsArticle>) {
                    Log.i(AppConstPresentation.LOG_UI, "onNext getCachedHeadlines: ${t.size}")
                    mTopHeadlines.value = Resource.Success(t)
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError getCachedHeadlines: ${e.message}")
                    mTopHeadlines.value = Resource.Error(message = "Something went wrong!")
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete getCachedHeadlines")
                }
            })
    }

    fun getSearchedHeadlines(country: String, searchQuery: String, page: Int) {
        mSearchedHeadlines.postValue(Resource.Loading())
        networkNewsUseCase.getSearchedHeadlines(country, searchQuery, page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<Resource<List<NewsArticle>>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onSuccess(t: Resource<List<NewsArticle>>) {
                    Log.i(AppConstPresentation.LOG_UI, "onNext getSearchedHeadlines ${t.data?.size}")
                    mSearchedHeadlines.value = t
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError getSearchedHeadlines: ${e.message}")
                    mSearchedHeadlines.value = Resource.Error("Cannot fetch search results")
                }
            })
    }

    fun clearCache() {
        cacheNewsUseCase.clearCache()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onSuccess(t: Int) {
                    Log.i(AppConstPresentation.LOG_UI, "onSuccess clear CacheDB news: $t deleted")
                    mEventMessage.value = Event("Cleared cached article list, $t deleted").also {
                        it.hasBeenHandled = true
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError clear CacheDB news: ${e.message}")
                    mEventMessage.value = Event("Clear cache internal error").also {
                        it.hasBeenHandled = true
                    }
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete clear CacheDB news")
                }
            })
    }

    fun getSavedArticles() {
        saveNewsUseCase.getSavedArticles()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<List<NewsSaved>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onNext(t: List<NewsSaved>) {
                    Log.i(AppConstPresentation.LOG_UI, "onNext getSavedHeadlines: ${t.size}")
                    mSavedNews.value = t
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError getSavedHeadlines: ${e.message}")
                    mEventMessage.value = Event("Could not fetch the saved articles")
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete getSavedHeadlines")
                }
            })
    }

    fun saveArticle(newsSaved: NewsSaved) {
        saveNewsUseCase.saveArticle(newsSaved)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Long> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onSuccess(t: Long) {
                    Log.i(AppConstPresentation.LOG_UI, "onSuccess addNewsArticle: $t")
                    mEventMessage.value = Event("Article Saved")
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError addNewsArticle: ${e.message}")
                    mEventMessage.value = Event("Could not save the article")
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete addNewsArticle")
                }
            })
    }

    fun deleteSavedArticle(id: Int) {
        saveNewsUseCase.deleteSavedArticle(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onSuccess(t: Int) {
                    Log.i(AppConstPresentation.LOG_UI, "onSuccess deleteNewsArticle: $t deleted")
                    mEventMessage.value = Event("Article Deleted")
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError deleteNewsArticle: ${e.message}")
                    mEventMessage.value = Event("Could not delete the article")
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete deleteNewArticle")
                }
            })
    }

    fun clearSaved() {
        saveNewsUseCase.clearSaved()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: MaybeObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onSuccess(t: Int) {
                    Log.i(AppConstPresentation.LOG_UI, "onSuccess clear Saved news: $t deleted")
                    mEventMessage.value = Event("Cleared saved article list, $t deleted").also {
                        it.hasBeenHandled = true
                    }
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError clear Saved news: ${e.message}")
                    mEventMessage.value = Event("Clear saved internal error").also {
                        it.hasBeenHandled = true
                    }
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete clear Saved news")
                }
            })
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}