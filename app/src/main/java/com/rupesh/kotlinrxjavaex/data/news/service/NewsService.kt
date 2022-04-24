package com.rupesh.kotlinrxjavaex.data.news.service

import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface NewsService {

    @GET
    fun getTopHeadlines(
        @Url url: String = "${BuildConfig.URL_NEWS}v2/top-headlines",
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_NEWS
    ): Single<Response<NewsResponse>>


    @GET
    fun getSearchedHeadlines(
        @Url url: String = "${BuildConfig.URL_NEWS}v2/top-headlines",
        @Query("country") country: String,
        @Query("q") searchQuery: String,
        @Query("page") page: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_NEWS
    ): Single<Response<NewsResponse>>
}