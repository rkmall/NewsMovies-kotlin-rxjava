package com.rupesh.kotlinrxjavaex.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.rupesh.kotlinrxjavaex.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.utils.AppConstants

@Database(entities = [DbMovie::class], version = 1)
abstract class MovieDB: RoomDatabase(){

    abstract fun getMovieDao(): MovieDao

    companion object {

        private var INSTANCE: MovieDB? = null

        fun getDB(context: Context): MovieDB {
            val tempInstance = INSTANCE

            if(tempInstance != null) return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(context.applicationContext, MovieDB::class.java, AppConstants.DB_NAME)
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
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }
}