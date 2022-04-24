package com.rupesh.kotlinrxjavaex.data.news.repository

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
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
    ): Single<Response<NewsResponse>> {
        return iNewsRemoteDataSource.getTopHeadlines(country, page)
    }

    override fun getSearchedHeadlines(
        country: String,
        searchQuery: String,
        page: Int
    ): Single<Response<NewsResponse>> {
        return iNewsRemoteDataSource.getSearchedHeadlines(country, searchQuery, page)
    }

    override fun getCachedArticles(): Observable<List<NewsArticle>> {
        return iNewsLocalDataSource.getCachedArticles()
    }

    override fun addCacheArticle(newsArticle: NewsArticle): Maybe<Long> {
        return iNewsLocalDataSource.addCacheArticle(newsArticle)
    }

    override fun deleteCacheArticle(id: Int): Maybe<Int> {
        return iNewsLocalDataSource.deleteCacheArticle(id)
    }

    override fun clearCache(): Maybe<Int> {
        return iNewsLocalDataSource.clearCache()
    }

    override fun getSavedArticles(): Observable<List<NewsSaved>> {
        return iNewsLocalDataSource.getSavedArticles()
    }

    override fun saveArticle(newsSaved: NewsSaved): Maybe<Long> {
        return iNewsLocalDataSource.saveArticle(newsSaved)
    }

    override fun deleteSavedArticle(id: Int): Maybe<Int> {
        return iNewsLocalDataSource.deleteSavedArticle(id)
    }

    override fun clearSaved(): Maybe<Int> {
        return iNewsLocalDataSource.clearSaved()
    }
}