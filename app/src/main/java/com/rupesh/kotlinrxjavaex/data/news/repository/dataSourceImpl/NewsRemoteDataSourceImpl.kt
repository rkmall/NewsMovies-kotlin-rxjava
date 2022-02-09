package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsRemoteDataSource
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import io.reactivex.Observable

class NewsRemoteDataSourceImpl(
    private val newsService: NewsService
) : INewsRemoteDataSource {

    override fun getNewsHeadlines(country: String, page: Int): Observable<NewsResponse> {
        return newsService.getTopHeadlines(country, page)
    }

    override fun getSearchedNewsHeadlines(country: String, searchQuery: String, page: Int): Observable<NewsResponse> {
        return newsService.getSearchedTopHeadlines(country, searchQuery, page)
    }
}