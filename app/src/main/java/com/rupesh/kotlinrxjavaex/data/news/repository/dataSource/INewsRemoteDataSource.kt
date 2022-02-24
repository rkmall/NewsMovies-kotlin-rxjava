package com.rupesh.kotlinrxjavaex.data.news.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface INewsRemoteDataSource {

    fun getTopNewsHeadlines(country: String, page: Int): Single<Response<NewsResponse>>
    fun getSearchedNewsHeadlines(country: String, searchQuery: String, page: Int): Single<Response<NewsResponse>>
}