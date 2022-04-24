package com.rupesh.kotlinrxjavaex.data.news.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import io.reactivex.Maybe
import io.reactivex.Observable

@Dao
interface NewsSavedDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveArticle(newsSaved: NewsSaved): Maybe<Long>

    @Query("select * from news_saved")
    fun getSavedArticles(): Observable<List<NewsSaved>>

    @Query("delete from news_saved where id = :id")
    fun deleteSavedArticle(id: Int): Maybe<Int>

    @Query("delete from news_saved")
    fun clearSaved(): Maybe<Int>
}