package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieDBResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface IMovieRemoteDataSource {

    fun getTopMovies() : Observable<Response<MovieDBResponse>>
    fun getSearchedMovies(searchQuery: String): Single<Response<List<Movie>>>
}