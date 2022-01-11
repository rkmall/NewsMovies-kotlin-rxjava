package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDB
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