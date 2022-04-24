package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import io.reactivex.Maybe
import io.reactivex.Observable

interface IMovieLocalDataSource {

    fun getSavedMovies(): Observable<List<Movie>>
    fun saveMovie(movie: Movie): Maybe<Long>
    fun deleteMovie(id: Int): Maybe<Int>
    fun clear(): Maybe<Int>
}