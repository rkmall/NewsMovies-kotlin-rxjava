package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.data.movie.service.MovieDataService
import com.rupesh.kotlinrxjavaex.data.movie.service.RetrofitInstance
import com.rupesh.kotlinrxjavaex.data.news.service.NewsRetrofitInstance
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NewsServiceModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): NewsService {
        val retrofit = NewsRetrofitInstance.retrofit
        return retrofit.create(NewsService::class.java)
    }
}