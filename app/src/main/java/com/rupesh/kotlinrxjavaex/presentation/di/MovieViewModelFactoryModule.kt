package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllMovies
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.MovieVMFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieViewModelFactoryModule {

    @Singleton
    @Provides
    fun provideMovieViewModelFactory(
        getAllMovies: GetAllMovies
    ): MovieVMFactory {
        return MovieVMFactory(getAllMovies)
    }
}