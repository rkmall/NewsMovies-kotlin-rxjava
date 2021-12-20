package com.rupesh.kotlinrxjavaex.service

import com.rupesh.kotlinrxjavaex.model.MovieDBResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieDataService {

    @GET("movie/popular")
    fun getAllMoviesWithRx(@Query("api_key") apiKey: String): Observable<MovieDBResponse>
}