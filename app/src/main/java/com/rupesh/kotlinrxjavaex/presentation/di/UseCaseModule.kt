package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import com.rupesh.kotlinrxjavaex.domain.usecase.DeleteSavedMovie
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllMovies
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllSavedMovies
import com.rupesh.kotlinrxjavaex.domain.usecase.SaveMovieToDb
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    /*@Singleton
    @Provides
    fun provideGetAllMoviesUseCase(movieRepository: MovieRepository): GetAllMovies {
        return GetAllMovies(movieRepository)
    }*/

    @Singleton
    @Provides
    fun provideGetAllSavedMoviesUseCase(dbMovieRepository: DbMovieRepository): GetAllSavedMovies {
        return GetAllSavedMovies(dbMovieRepository)
    }

    @Singleton
    @Provides
    fun provideSaveMovieToDbUseCase(dbMovieRepository: DbMovieRepository): SaveMovieToDb {
        return SaveMovieToDb(dbMovieRepository)
    }

    @Singleton
    @Provides
    fun provideDeleteSavedMovieUseCase(dbMovieRepository: DbMovieRepository): DeleteSavedMovie {
        return DeleteSavedMovie(dbMovieRepository)
    }
}