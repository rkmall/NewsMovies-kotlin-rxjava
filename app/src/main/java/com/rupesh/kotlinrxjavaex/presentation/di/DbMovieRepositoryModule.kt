package com.rupesh.kotlinrxjavaex.presentation.di

import android.content.Context
import com.rupesh.kotlinrxjavaex.data.db.MovieDB
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DbMovieRepositoryModule {

    @Singleton
    @Provides
    fun provideDbMovieRepository(
        movieDB: MovieDB
    ): DbMovieRepository {
        return DbMovieRepository(movieDB)
    }
}