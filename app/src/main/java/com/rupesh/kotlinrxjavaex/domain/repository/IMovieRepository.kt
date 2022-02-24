package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

interface IMovieRepository {

    fun getTopMovies(): Single<Response<MovieResponse>>
    fun getSavedMovieList(): Observable<List<DbMovie>>
    fun addMovieToDb(dbMovie: DbMovie): Maybe<Long>
    fun deleteMovieFromDb(id: Int): Maybe<Int>
}