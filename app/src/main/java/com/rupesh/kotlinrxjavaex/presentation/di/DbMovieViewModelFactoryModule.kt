package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository
import com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory.DbMovieVMFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DbMovieViewModelFactoryModule {

    @Singleton
    @Provides
    fun provideDbMovieViewModelFactory(
        dbMovieRepository: DbMovieRepository
    ): DbMovieVMFactory {
        return DbMovieVMFactory(dbMovieRepository)
    }
}