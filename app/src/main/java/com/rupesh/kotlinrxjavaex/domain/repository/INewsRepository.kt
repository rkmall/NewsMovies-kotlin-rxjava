package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface INewsRepository {

    fun getTopHeadlines(country: String, page: Int): Single<Response<NewsResponse>>
    fun getSearchedHeadlines(country: String, searchQuery: String, page: Int): Single<Response<NewsResponse>>
    fun getSavedNewsArticles(): Observable<List<NewsArticle>>
    fun saveNewsArticle(newsArticle: NewsArticle): Maybe<Long>
    fun removeNewsArticle(id: Int): Maybe<Int>
}