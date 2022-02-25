package com.rupesh.kotlinrxjavaex.data.news.service

import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsService {

    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_NEWS
    ): Observable<Response<NewsResponse>>


    @GET("v2/top-headlines")
    fun getSearchedTopHeadlines(
        @Query("country") country: String,
        @Query("q") searchQuery: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_NEWS
    ): Observable<Response<NewsResponse>>
}