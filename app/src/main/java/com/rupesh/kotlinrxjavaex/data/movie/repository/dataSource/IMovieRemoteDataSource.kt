package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieDBResponse
import io.reactivex.Observable
import io.reactivex.Single

interface IMovieRemoteDataSource {

    fun getTopMovies() : Observable<MovieDBResponse>
    fun getSearchedMovies(searchQuery: String): Single<List<Movie>>
}