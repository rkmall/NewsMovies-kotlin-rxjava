package com.rupesh.kotlinrxjavaex.data.news.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import io.reactivex.Single
import retrofit2.Response

interface INewsRemoteDataSource {

    fun getTopHeadlines(country: String, page: Int): Single<Response<NewsResponse>>
    fun getSearchedHeadlines(country: String, searchQuery: String, page: Int): Single<Response<NewsResponse>>
}