package com.rupesh.kotlinrxjavaex.data.news.repository

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsLocalDataSource
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsRemoteDataSource
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

class NewsRepositoryImpl(
    private val iNewsLocalDataSource: INewsLocalDataSource,
    private val iNewsRemoteDataSource: INewsRemoteDataSource
) : INewsRepository {

    override fun getTopHeadlines(
        country: String,
        page: Int
    ): Observable<Response<NewsResponse>> {
        return iNewsRemoteDataSource.getTopNewsHeadlines(country, page)
    }

    override fun getSearchedHeadlines(
        country: String,
        searchQuery: String,
        page: Int
    ): Observable<Response<NewsResponse>> {
        return iNewsRemoteDataSource.getSearchedNewsHeadlines(country, searchQuery, page)
    }

    override fun getSavedNewsArticles(): Observable<List<NewsArticle>> {
        return iNewsLocalDataSource.getSavedNewsArticles()
    }

    override fun saveNewsArticle(newsArticle: NewsArticle): Maybe<Long> {
        return iNewsLocalDataSource.addNewsArticleToDb(newsArticle)
    }

    override fun removeNewsArticle(id: Int): Maybe<Int> {
        return iNewsLocalDataSource.deleteNewsArticleFromDb(id)
    }
}