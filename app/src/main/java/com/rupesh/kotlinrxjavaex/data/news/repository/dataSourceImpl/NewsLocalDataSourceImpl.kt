package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.news.db.NewsDao
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsLocalDataSource
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

class NewsLocalDataSourceImpl(
    private val newsDao: NewsDao
) : INewsLocalDataSource {

    override fun getSavedNewsArticles(): Observable<List<NewsArticle>> {
        return newsDao.getSavedNewsArticleFromDb()
    }

    override fun addNewsArticleToDb(newsArticle: NewsArticle): Maybe<Long> {
        return newsDao.addNewsArticleToDb(newsArticle)
    }

    override fun deleteNewsArticleFromDb(id: Int): Maybe<Int> {
        return newsDao.deleteNewsArticleFromDb(id)
    }
}