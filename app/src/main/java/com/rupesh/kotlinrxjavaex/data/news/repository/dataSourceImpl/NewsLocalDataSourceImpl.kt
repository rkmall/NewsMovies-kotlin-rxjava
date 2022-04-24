package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.news.db.NewsDao
import com.rupesh.kotlinrxjavaex.data.news.db.NewsSavedDao
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsLocalDataSource
import io.reactivex.Maybe
import io.reactivex.Observable

class NewsLocalDataSourceImpl(
    private val newsDao: NewsDao,
    private val newsSavedDao: NewsSavedDao
) : INewsLocalDataSource {

    override fun addCacheArticle(newsArticle: NewsArticle): Maybe<Long> {
        return newsDao.addCacheArticle(newsArticle)
    }

    override fun getCachedArticles(): Observable<List<NewsArticle>> {
        return newsDao.getCachedArticles()
    }

    override fun deleteCacheArticle(id: Int): Maybe<Int> {
        return newsDao.deleteCacheArticle(id)
    }

    override fun clearCache(): Maybe<Int> {
        return newsDao.clearCache()
    }

    override fun saveArticle(newsSaved: NewsSaved): Maybe<Long> {
        return newsSavedDao.saveArticle(newsSaved)
    }

    override fun getSavedArticles(): Observable<List<NewsSaved>> {
        return newsSavedDao.getSavedArticles()
    }

    override fun deleteSavedArticle(id: Int): Maybe<Int> {
        return newsSavedDao.deleteSavedArticle(id)
    }

    override fun clearSaved(): Maybe<Int> {
        return newsSavedDao.clearSaved()
    }
}