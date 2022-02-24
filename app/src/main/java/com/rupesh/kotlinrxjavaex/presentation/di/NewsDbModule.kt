package com.rupesh.kotlinrxjavaex.presentation.di

import android.content.Context
import com.rupesh.kotlinrxjavaex.data.news.db.NewsDB
import com.rupesh.kotlinrxjavaex.data.news.db.NewsDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NewsDbModule {

    @Singleton
    @Provides
    fun provideNewsDao(@ApplicationContext context: Context): NewsDao {
        return NewsDB.getDB(context).getNewsDao()
    }
}