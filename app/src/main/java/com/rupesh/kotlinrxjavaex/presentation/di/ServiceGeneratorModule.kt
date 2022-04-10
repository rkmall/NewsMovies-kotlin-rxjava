package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.data.movie.service.MovieService
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import com.rupesh.kotlinrxjavaex.data.common.service.ServiceGenerator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ServiceGeneratorModule {

    @Singleton
    @Provides
    fun provideNewsService(): NewsService {
        return ServiceGenerator.serviceApi.create(NewsService::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieService(): MovieService {
        return ServiceGenerator.serviceApi.create(MovieService::class.java)
    }

}