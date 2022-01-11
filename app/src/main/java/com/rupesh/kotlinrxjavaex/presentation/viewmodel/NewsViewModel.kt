package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllNewsArticles
import com.rupesh.kotlinrxjavaex.domain.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getAllNewsArticles: GetAllNewsArticles
) : ViewModel() {

    // RxJava CompositeDisposables
    private val disposable: CompositeDisposable = CompositeDisposable()

    // Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
    private val newsLiveData: MutableLiveData<List<NewsArticle>> = MutableLiveData()

    val newsLiveDataResult: LiveData<List<NewsArticle>> get() = newsLiveData

    // Status message to notify user about the completion of event
    private val statusMessage = MutableLiveData<Event<String>>()

    val statusMessageResult: LiveData<Event<String>> get() = statusMessage

    /**
     * Gets a list of NewsArticle wrapped inside MutableLiveData
     */
    fun getMovieList(country: String, page: Int) {
        disposable.add(
            getAllNewsArticles.execute(country, page)
                .subscribeWith(object: DisposableObserver<List<NewsArticle>>() {
                    override fun onNext(t: List<NewsArticle>) {
                        Log.i("MyTag", "onNextGetNewsListAPI: $t")
                        newsLiveData.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetNewsListAPI")
                        statusMessage.postValue(Event("Something went wrong"))
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetNewsListAPI")
                        statusMessage.postValue(Event("Top headlines"))
                    }
                })
        )
    }

    fun clear() {
        disposable.clear()
        super.onCleared()
    }
}