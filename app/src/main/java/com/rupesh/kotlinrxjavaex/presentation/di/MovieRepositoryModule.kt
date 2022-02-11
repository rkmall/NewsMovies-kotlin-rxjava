package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.data.movie.repository.MovieRepositoryImpl
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieLocalDataSource
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieRemoteDataSource
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MovieRepositoryModule {

    @Singleton
    @Provides
    fun provideIMovieRepository(
        iMovieLocalDataSource: IMovieLocalDataSource,
        iMovieRemoteDataSource: IMovieRemoteDataSource
    ): IMovieRepository {
        return MovieRepositoryImpl(iMovieLocalDataSource, iMovieRemoteDataSource);
    }
}