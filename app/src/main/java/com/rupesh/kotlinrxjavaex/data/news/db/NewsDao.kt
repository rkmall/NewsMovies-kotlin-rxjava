package com.rupesh.kotlinrxjavaex.data.news.db

import androidx.room.*
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addNewsArticleToDb(newsArticle: NewsArticle): Maybe<Long>

    @Query("delete from news where id = :id")
    fun deleteNewsArticleFromDb(id: Int): Maybe<Int>

    @Query("select * from news")
    fun getSavedNewsArticleFromDb(): Observable<List<NewsArticle>>
}