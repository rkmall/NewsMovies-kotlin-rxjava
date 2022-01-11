package com.rupesh.kotlinrxjavaex.presentation.di

import com.rupesh.kotlinrxjavaex.data.movie.service.MovieDataService
import com.rupesh.kotlinrxjavaex.data.movie.service.RetrofitInstance
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ServiceModule {

    @Singleton
    @Provides
    fun provideRetrofitService(): MovieDataService {
        val retrofit = RetrofitInstance.retrofit
        return retrofit.create(MovieDataService::class.java)
    }
}