package com.rupesh.kotlinrxjavaex.data.news.db

import androidx.room.*
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsArticleToDb(newsArticle: NewsArticle): Maybe<Long>

    @Query("select * from news")
    fun getSavedNewsArticleFromDb(): Observable<List<NewsArticle>>

    @Query("delete from news where id = :id")
    fun deleteNewsArticleFromDb(id: Int): Maybe<Int>

    @Query("delete from news")
    fun clear(): Maybe<Int>
}