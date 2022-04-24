package com.rupesh.kotlinrxjavaex.data.news.db

import androidx.room.*
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCacheArticle(newsArticle: NewsArticle): Maybe<Long>

    @Query("select * from news")
    fun getCachedArticles(): Observable<List<NewsArticle>>

    @Query("delete from news where id = :id")
    fun deleteCacheArticle(id: Int): Maybe<Int>

    @Query("delete from news")
    fun clearCache(): Maybe<Int>
}