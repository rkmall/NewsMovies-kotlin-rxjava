package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.data.service.MovieDataService
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class MovieRepositoryModule  {

    @Singleton
    @Provides
    fun provideRepository(
        service: MovieDataService
    ): MovieRepository {
        return MovieRepository(service)
    }
}