package com.rupesh.kotlinrxjavaex.data.common.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDao
import com.rupesh.kotlinrxjavaex.data.movie.db.MovieGenreListConverter
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.news.db.NewsConverters
import com.rupesh.kotlinrxjavaex.data.news.db.NewsDao
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData

/**
 * AppDB represents a Room database with entities
 * [com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle]
 * [com.rupesh.kotlinrxjavaex.data.movie.model.Movie]
 * @author Rupesh Mall
 * @since 1.0
 */
@Database(entities = [NewsArticle::class, Movie::class], version = 1, exportSchema = false)
@TypeConverters(NewsConverters::class, MovieGenreListConverter::class)
abstract class AppDB : RoomDatabase() {
    /**
     * In other classes where we need to instantiate AppDB
     * to perform database operation, we can get singleton
     * AppDB object and use it to invoke this method to get
     * NewsDao and MovieDao that can be used to invoke all
     * database operation methods declared in NewsDao
     * @return the AppDB [com.rupesh.kotlinrxjavaex.data.common.db.AppDB]
     */
    abstract fun getNewsDao(): NewsDao
    abstract fun getMovieDao(): MovieDao

    companion object {
        private var INSTANCE: AppDB? = null

        /**
         * This method returns a thread safe singleton of AppDB
         * Singleton pattern is used to lazily create the instance
         * @param context Android application context
         * @return the singleton instance of AppDB
         */
        fun getDb(context: Context): AppDB {
            if(INSTANCE == null) {
                synchronized(AppDB::class) {
                    INSTANCE = buildRoomDb(context)
                }
            }
            return INSTANCE!!
        }

        private fun buildRoomDb(context: Context) = Room.databaseBuilder(
            context,
            AppDB::class.java,
            AppConstantsData.DB_NAME
        ).addCallback(object : RoomDatabase.Callback(){
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)
                    Log.d("AppDb", "AppDb onCreate() invoked")
                }

                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    Log.d("AppDb", "AppDb onOpen() invoked")
                }
            }).build()
    }
}