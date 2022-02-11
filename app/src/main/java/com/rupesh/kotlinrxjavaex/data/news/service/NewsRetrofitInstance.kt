package com.rupesh.kotlinrxjavaex.data.news.service

import com.rupesh.kotlinrxjavaex.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NewsRetrofitInstance {

    // OkHttpClient val
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()


    // Retrofit val by Kotlin lazy delegation
    val retrofitNews: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_NEWS)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}