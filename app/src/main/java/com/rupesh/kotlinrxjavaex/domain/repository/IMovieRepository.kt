package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface IMovieRepository {

    fun getPopularMovies(): Single<Response<MovieResponse>>
    fun getSavedMovies(): Observable<List<Movie>>
    fun saveMovie(movie: Movie): Maybe<Long>
    fun deleteMovie(id: Int): Maybe<Int>
    fun clear(): Maybe<Int>
}