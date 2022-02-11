package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class NewsRepository @Inject constructor(val service: NewsService) {

    /**
     * Gets a list of all NewsArticle
     * @return the Observable<List<NewsArticle> that wraps the result of API call
     */
    fun getNewsArticleListFromAPI(country: String, page: Int): Observable<List<NewsArticle>> {
        val newsResponseObservable = service.getTopHeadlines(country, page)
        return newsResponseObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles}
    }

    /**
     * Gets a list of all NewsArticle
     * @return the Observable<List<NewsArticle> that wraps the result of API call
     */
    fun getSearchedNewsArticleListFromAPI(country: String, searchQuery: String, page: Int): Observable<List<NewsArticle>> {
        val newsResponseObservable = service.getSearchedTopHeadlines(country, searchQuery, page)
        return newsResponseObservable
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles}
    }
}