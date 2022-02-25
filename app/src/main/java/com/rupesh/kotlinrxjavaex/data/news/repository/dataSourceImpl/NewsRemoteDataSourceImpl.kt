package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsRemoteDataSource
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsService: NewsService
) : INewsRemoteDataSource {

    override fun getTopNewsHeadlines(
        country: String,
        page: Int
    ): Observable<Response<NewsResponse>> {
        return newsService.getTopHeadlines(country, page)
    }

    override fun getSearchedNewsHeadlines(
        country: String,
        searchQuery: String,
        page: Int
    ): Observable<Response<NewsResponse>> {
        return newsService.getSearchedTopHeadlines(country, searchQuery, page)
    }
}