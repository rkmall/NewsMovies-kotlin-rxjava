package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import io.reactivex.Maybe
import io.reactivex.Observable

interface IMovieLocalDataSource {

    fun getSavedMovieList(): Observable<List<Movie>>
    fun addMovieToDb(movie: Movie): Maybe<Long>
    fun deleteMovieFromDb(id: Int): Maybe<Int>
}