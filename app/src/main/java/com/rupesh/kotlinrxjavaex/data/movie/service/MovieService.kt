package com.rupesh.kotlinrxjavaex.data.movie.service

import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {
    /**
     * Gets the Retrofit Call<MovieResponse> as HTTP response which
     * is the converted to RxJava Observable using RxJava2CallAdapterFactory
     * @see [com.rupesh.kotlinrxjavaex.service.RetrofitInstance]
     * @param apiKey the REST API key
     * @return the RxJava Observables containing [com.rupesh.kotlinrxjavaex.model.MovieResponse]
     */
    @GET("${BuildConfig.URL_MOVIE}movie/popular")
    fun getAllMoviesWithRx(
        @Query("api_key") apiKey: String = BuildConfig.API_MOVIE
    ): Single<Response<MovieResponse>>

    /*@GET("search/movie")
    fun getSearchedMovie(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") searchQuery: String
    ): Single<Response<List<Movie>>>*/
}