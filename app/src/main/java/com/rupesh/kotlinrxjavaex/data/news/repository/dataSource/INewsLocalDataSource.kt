package com.rupesh.kotlinrxjavaex.data.news.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import io.reactivex.Maybe
import io.reactivex.Observable

interface INewsLocalDataSource {

    fun addCacheArticle(newsArticle: NewsArticle): Maybe<Long>
    fun getCachedArticles(): Observable<List<NewsArticle>>
    fun deleteCacheArticle(id: Int): Maybe<Int>
    fun clearCache(): Maybe<Int>

    fun saveArticle(newsSaved: NewsSaved): Maybe<Long>
    fun getSavedArticles(): Observable<List<NewsSaved>>
    fun deleteSavedArticle(id: Int): Maybe<Int>
    fun clearSaved(): Maybe<Int>
}