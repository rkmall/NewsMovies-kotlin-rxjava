package com.rupesh.kotlinrxjavaex.presentation.di

import android.app.Application
import android.content.Context
import com.rupesh.kotlinrxjavaex.data.db.MovieDB
import com.rupesh.kotlinrxjavaex.data.db.MovieDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Singleton
    @Provides
    fun provideMovieDb(app: Application): MovieDB {
        return MovieDB.getDB(app)
    }
}