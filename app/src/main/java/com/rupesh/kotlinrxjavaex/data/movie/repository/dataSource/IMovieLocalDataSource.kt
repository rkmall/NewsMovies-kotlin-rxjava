package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import io.reactivex.Maybe
import io.reactivex.Observable

interface IMovieLocalDataSource {

    fun getSavedMovieList(): Observable<List<DbMovie>>
    fun addMovie(dbMovie: DbMovie): Maybe<Long>
    fun deleteMovie(dbMovie: DbMovie): Maybe<Int>
}