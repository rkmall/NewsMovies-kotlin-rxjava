package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsRepository @Inject constructor(val service: NewsService) {

    /**
     * Gets a list of all Movies in succession from API call
     * @return the Observable<List<Movie> that wraps the result of API call
     */
    fun getNewsArticleListFromAPI(country: String, page: Int): Observable<List<NewsArticle>> {
        val newsResponseObservable = service.getTopHeadlines(country, page)

        return newsResponseObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { it.articles}
    }
}