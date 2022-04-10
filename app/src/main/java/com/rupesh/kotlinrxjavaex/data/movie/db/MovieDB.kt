package com.rupesh.kotlinrxjavaex.data.movie.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData

/**
 * MovieDB represents a Room database with entity
 * [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]
 * @author Rupesh Mall
 * @since 1.0
 */
@Database(entities = [DbMovie::class], version = 1, exportSchema = false)
abstract class MovieDB : RoomDatabase(){

    /**
     * In other classes where we need to instantiate MovieDB to perform
     * database operation, we can get singleton MovieDB object and
     * and use it to invoke this method to get MovieDao that can be used
     * to invoke all database operation methods declared in MovieDao interface
     * @return the MovieDao [com.rupesh.kotlinrxjavaex.db.MovieDao]
     */
    abstract fun getMovieDao(): MovieDao

    companion object {

        private var INSTANCE: MovieDB? = null

        /**
         * This method returns a thread safe singleton of MovieDb
         * Singleton pattern is used to lazily create the instance
         * @param context Android application context
         * @return the singleton instance of MovieDB
         */
        fun getDB(context: Context): MovieDB {
            val tempInstance = INSTANCE

            if(tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context,
                    MovieDB::class.java,
                    AppConstantsData.DB_NAME)
                    .addCallback(object: RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("MovieDb", "MovieDb onCreate() invoked")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)
                            Log.d("MovieDb", "MovieDb onOpen() invoked")
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