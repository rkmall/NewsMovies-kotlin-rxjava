package com.rupesh.kotlinrxjavaex.data.news.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import io.reactivex.Observable

interface INewsRemoteDataSource {

    fun getNewsHeadlines(country: String, page: Int): Observable<NewsResponse>
    fun getSearchedNewsHeadlines(country: String, searchQuery: String, page: Int): Observable<NewsResponse>
}