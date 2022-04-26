/*
package com.rupesh.kotlinrxjavaex.data.common.service

import android.content.Context
import com.rupesh.kotlinrxjavaex.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object ServiceGenerator {

    // OkHttpClient val
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()


    // Retrofit val by Kotlin lazy delegation
    val serviceApi: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.URL_NEWS)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}*/
