package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface INewsRepository {

    fun getTopHeadlines(country: String, page: Int): Single<Response<NewsResponse>>
    fun getSearchedHeadlines(country: String, searchQuery: String, page: Int): Single<Response<NewsResponse>>

    fun getCachedArticles(): Observable<List<NewsArticle>>
    fun addCacheArticle(newsArticle: NewsArticle): Maybe<Long>
    fun deleteCacheArticle(id: Int): Maybe<Int>
    fun clearCache(): Maybe<Int>

    fun getSavedArticles(): Observable<List<NewsSaved>>
    fun saveArticle(newsSaved: NewsSaved): Maybe<Long>
    fun deleteSavedArticle(id: Int): Maybe<Int>
    fun clearSaved(): Maybe<Int>
}