package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsRemoteDataSource
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import io.reactivex.Single
import retrofit2.Response

class NewsRemoteDataSourceImpl(
    private val newsService: NewsService
) : INewsRemoteDataSource {

    override fun getTopHeadlines(
        country: String,
        page: Int
    ): Single<Response<NewsResponse>> {
        return newsService.getTopHeadlines(country = country, page =  page)
    }

    override fun getSearchedHeadlines(
        country: String,
        searchQuery: String,
        page: Int
    ): Single<Response<NewsResponse>> {
        return newsService.getSearchedHeadlines(country = country, searchQuery = searchQuery, page = page)
    }
}
