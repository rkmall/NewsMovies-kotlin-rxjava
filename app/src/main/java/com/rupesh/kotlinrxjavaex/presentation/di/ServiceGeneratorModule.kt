package com.rupesh.kotlinrxjavaex.presentation.di

import android.content.Context
import android.util.Log
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieService
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import com.rupesh.kotlinrxjavaex.presentation.util.AppConstPresentation
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object ServiceGeneratorModule {

    @Singleton
    @Provides
    fun provideCache(@ApplicationContext context: Context): Cache {
        val cache = 10 * 1024 * 1024
        val httpCacheDir = File(context.cacheDir, "movie-response")
        return Cache(httpCacheDir, cache.toLong())
    }


    @Singleton
    @Provides
    fun provideCacheInterceptor(): Interceptor {
        val cacheControl = CacheControl.Builder()
            .maxAge(1, TimeUnit.SECONDS)
            .build()

        return Interceptor {
            val request = it.request()
            if(request.header("ApplyResponseCache").toBoolean()) {
                Log.i(AppConstPresentation.LOG_DATA, "Response cache applied")
                val originalResponse = it.proceed(it.request())
                return@Interceptor originalResponse.newBuilder()
                    .removeHeader("ApplyResponseCache")
                    .header("Cache-Control", cacheControl.toString())
                    .build()
            }else {
                Log.i(AppConstPresentation.LOG_DATA, "Response cache not applied")
                return@Interceptor it.proceed(it.request())
            }
        }
    }


    @Singleton
    @Provides
    fun provideHttpClient(cache: Cache, networkInterceptor: Interceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(networkInterceptor)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): GsonConverterFactory {
        return GsonConverterFactory.create()
    }


    @Singleton
    @Provides
    fun provideRetrofitInstance(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.URL_NEWS)
            .client(okHttpClient)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideNewsService(retrofit: Retrofit): NewsService {
        return retrofit.create(NewsService::class.java)
    }

    @Singleton
    @Provides
    fun provideMovieService(retrofit: Retrofit): MovieService {
        return retrofit.create(MovieService::class.java)
    }
}