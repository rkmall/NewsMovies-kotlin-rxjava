package com.rupesh.kotlinrxjavaex.presentation.viewmodel

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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val getAllNewsArticles: GetAllNewsArticles,
    private val getSearchedNewsArticle: GetSearchedNewsArticle,
    private val getSavedNewsArticles: GetSavedNewsArticles,
    private val saveNewsArticleToDb: SaveNewsArticleToDb,
    private val deleteNewsArticleFromDb: DeleteNewsArticleFromDb
) : ViewModel() {

    // RxJava CompositeDisposables
    private val disposable: CompositeDisposable = CompositeDisposable()

    // Livedata for top NewsArticles
    private val newsLiveData: MutableLiveData<Resource<List<NewsArticle>>> = MutableLiveData()
    val newsLiveDataResult: LiveData<Resource<List<NewsArticle>>> get() = newsLiveData

    // Livedata for searched NewsArticle  for searched news
    private val searchedNewsLiveData: MutableLiveData<Resource<List<NewsArticle>>> = MutableLiveData()
    val searchedNewsLivedataResult: LiveData<Resource<List<NewsArticle>>> get() = searchedNewsLiveData

    // Livedata for saved NewsArticle
    private val savedNewsArticlesLiveData: MutableLiveData<List<NewsArticle>> = MutableLiveData()
    val savedNewsArticlesLiveDataResult: LiveData<List<NewsArticle>> get() = savedNewsArticlesLiveData

    // Status message to notify user about the completion of event
    private val statusMessage = MutableLiveData<Event<String>>()
    val statusMessageResult get() = statusMessage

    val testData = ArrayList<NewsArticle>()

    /**
     * Gets a list of NewsArticle wrapped inside MutableLiveData
     */
    fun getNewsList(country: String, page: Int) {
        newsLiveData.postValue(Resource.Loading())
        disposable.add(
            getAllNewsArticles.execute(country, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<Response<NewsResponse>>() {
                    override fun onNext(t: Response<NewsResponse>) {
                        val statusCode = t.code()
                        Log.i("MyTag", "onNextGetTopHeadlines response code: $statusCode")
                        if(statusCode == 200) {
                            newsLiveData.value = Resource.Success(t.body()!!.articles)
                        }else {
                            newsLiveData.value = Resource.Error(null, "Cannot fetch news")
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetTopHeadlines ${e.message}")
                        newsLiveData.value = Resource.Error(null, "Cannot fetch news")
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetTopHeadlines")
                    }
                })
        )
    }

    fun getSearchedNewsList(country: String, searchQuery: String, page: Int) {
        searchedNewsLiveData.postValue(Resource.Loading())
        disposable.add(
            getSearchedNewsArticle.execute(country, searchQuery, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<Response<NewsResponse>>() {
                    override fun onNext(t: Response<NewsResponse>) {
                        val statusCode = t.code()
                        Log.i("MyTag", "onNextGetSearchedHeadlines response code: $statusCode")
                        if(statusCode == 200) {
                            searchedNewsLiveData.value = Resource.Success(t.body()!!.articles)
                        } else {
                            searchedNewsLiveData.value = Resource.Error(null, "Cannot fetch search results")
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetSearchedNews: ${e.message}")
                        searchedNewsLiveData.value = Resource.Error(null, "Cannot fetch search results")
                    }


                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetSearchedNews")
                    }
                })
        )
    }

    fun getSavedNewsArticles() {
        disposable.add(
            getSavedNewsArticles.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<NewsArticle>>() {
                    override fun onNext(t: List<NewsArticle>) {
                        Log.i("MyTag", "onNextGetSavedHeadlines: ${t.size}")
                        savedNewsArticlesLiveData.value = t
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetSavedNews: ${e.message}")
                        statusMessage.value = Event("Could not fetch the saved articles")
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetSavedNews")
                    }
                })
        )
    }

    fun saveNewsArticle(newsArticle: NewsArticle) {
        disposable.add(
            saveNewsArticleToDb.execute(newsArticle)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableMaybeObserver<Long>() {
                    override fun onSuccess(t: Long) {
                        Log.i("MyTag", "onSuccessAddNewsArticleToDb: $t")
                        statusMessage.value = Event("Article Saved")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorAddNewsArticleToDb: ${e.message}")
                        statusMessage.value = Event("Could not save the article")
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteAddArticleToDb: Operation completed")
                    }
                })
        )
    }

    fun deleteNewsArticle(articleId: Int) {
        disposable.add(
            deleteNewsArticleFromDb.execute(articleId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableMaybeObserver<Int>() {
                    override fun onSuccess(t: Int) {
                        Log.i("MyTag", "onSuccessDeleteNewsArticleFromDb: $t deleted")
                        statusMessage.value = Event("Article Deleted")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorDeleteNewsArticleToDb: ${e.message}")
                        statusMessage.value = Event("Could not delete the article")
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteDeleteArticleToDb: Operation completed")
                    }
                })
        )
    }

    fun clear() {
        disposable.clear()
        super.onCleared()
    }
}