package com.rupesh.kotlinrxjavaex.data.service

import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.util.AppConstants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * RetrofitInstance is a Retrofit singleton object, a REST client for this app
 * It is used to authenticate and connect with API
 * @author Rupesh Mall
 * @since 1.0
 */
object RetrofitInstance {

    // OkHttpClient val
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    // Retrofit val by Kotlin lazy delegation
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Create Retrofit with API service by Kotlin lazy delegation
    /*val instance by lazy {
        retrofit.create(MovieDataService::class.java)
    }*/
}