package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.data.news.repository.NewsRepositoryImpl
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsLocalDataSource
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsRemoteDataSource
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NewsRepositoryModule {

    @Singleton
    @Provides
    fun provideINewsRepository(
        iNewsLocalDataSource: INewsLocalDataSource,
        iNewsRemoteDataSource: INewsRemoteDataSource
    ): INewsRepository {
        return NewsRepositoryImpl(iNewsLocalDataSource, iNewsRemoteDataSource)
    }
}