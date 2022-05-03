package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDao
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieLocalDataSource
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieRemoteDataSource
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl.MovieLocalDataSourceImpl
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl.MovieRemoteDataSourceImpl
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieDataSourceModule {

    @Singleton
    @Provides
    fun provideIMovieRemoteDataSource(
        movieService: MovieService
    ): IMovieRemoteDataSource {
        return MovieRemoteDataSourceImpl(movieService);
    }


    @Singleton
    @Provides
    fun provideIMovieLocalDataSource(
        movieDao: MovieDao
    ): IMovieLocalDataSource {
        return MovieLocalDataSourceImpl(movieDao);
    }
}