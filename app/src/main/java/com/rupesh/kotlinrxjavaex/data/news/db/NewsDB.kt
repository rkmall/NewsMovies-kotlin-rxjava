package com.rupesh.kotlinrxjavaex.data.news.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData

@Database(entities = [NewsArticle::class], version = 1)
@TypeConverters(NewsConverters::class)
abstract class NewsDB : RoomDatabase() {

    abstract fun getNewsDao(): NewsDao

    companion object {

        private var INSTANCE: NewsDB? = null

        /**
         * This method returns a thread safe singleton of MovieDb
         * Singleton pattern is used to lazily create the instance
         * @param context Android application context
         * @return the singleton instance of MovieDB
         */
        fun getDB(context: Context): NewsDB {
            val tempInstance = INSTANCE

            if(tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    NewsDB::class.java,
                    AppConstantsData.DB_NEWS)
                    .addCallback(object: RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("NewsDb", "NewsDb onCreate() invoked")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            Log.d("NewsDb", "NewsDb onOpen() invoked")
                        }
                    })
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}