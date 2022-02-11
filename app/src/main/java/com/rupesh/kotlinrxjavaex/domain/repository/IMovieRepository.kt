package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieDBResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface IMovieRepository {

    fun getTopMovies(): Observable<Response<MovieDBResponse>>
    fun getSearchedMovies(searchQuery: String): Single<Response<List<Movie>>>
    fun getSavedMovieList(): Observable<List<DbMovie>>
    fun addMovieToDb(dbMovie: DbMovie): Maybe<Long>
    fun deleteMovieFromDb(dbMovie: DbMovie): Maybe<Int>
}