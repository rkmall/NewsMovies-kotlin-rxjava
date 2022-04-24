package com.rupesh.kotlinrxjavaex.presentation.di

import android.content.Context
import com.rupesh.kotlinrxjavaex.data.common.db.AppDB
import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDao
import com.rupesh.kotlinrxjavaex.data.news.db.NewsDao
import com.rupesh.kotlinrxjavaex.data.news.db.NewsSavedDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppDbModule {

    @Singleton
    @Provides
    fun provideNewsDao(@ApplicationContext context: Context): NewsDao {
        return AppDB.getDb(context).getNewsDao()
    }

    @Singleton
    @Provides
    fun provideNewsSavedDao(@ApplicationContext context: Context): NewsSavedDao {
        return AppDB.getDb(context).getNewsSavedDao()
    }

    @Singleton
    @Provides
    fun provideMovieDao(@ApplicationContext context: Context): MovieDao {
        return AppDB.getDb(context).getMovieDao()
    }
}