package com.rupesh.kotlinrxjavaex.data.movie.service

import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * MovieDataService represents the service provided by API
 * @see <a href="https://developers.themoviedb.org/3/getting-started/introduction"></a>
 * It contains the methods to perform REST operations
 * The state of the transfer is JSON format [com.rupesh.kotlinrxjavaex.model.MovieDBResponse]
 * @author Rupesh Mall
 * @since 1.0
 */
interface MovieDataService {

    /**
     * Gets the Retrofit Call<MovieResponse> as HTTP response which
     * is the converted to RxJava Observable using RxJava2CallAdapterFactory
     * @see [com.rupesh.kotlinrxjavaex.service.RetrofitInstance]
     * @param apiKey the REST API key
     * @return the RxJava Observables containing [com.rupesh.kotlinrxjavaex.model.MovieResponse]
     */
    @GET("movie/popular")
    fun getAllMoviesWithRx(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY
    ): Single<Response<MovieResponse>>

    /*@GET("search/movie")
    fun getSearchedMovie(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") searchQuery: String
    ): Single<Response<List<Movie>>>*/
}