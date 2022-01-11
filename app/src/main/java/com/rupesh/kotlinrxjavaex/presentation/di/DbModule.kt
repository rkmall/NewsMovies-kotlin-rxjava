package com.rupesh.kotlinrxjavaex.presentation.di

import android.content.Context
import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDB
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DbModule {

    @Singleton
    @Provides
    fun provideMovieDb(@ApplicationContext context: Context): MovieDB {
        return MovieDB.getDB(context)
    }
}


