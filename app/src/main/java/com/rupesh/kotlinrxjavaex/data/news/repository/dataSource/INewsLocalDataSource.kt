package com.rupesh.kotlinrxjavaex.data.news.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

interface INewsLocalDataSource {

    fun getSavedNewsArticles(): Observable<List<NewsArticle>>
    fun addNewsArticleToDb(newsArticle: NewsArticle): Maybe<Long>
    fun deleteNewsArticleFromDb(id: Int): Maybe<Int>
    fun clear(): Maybe<Int>
}