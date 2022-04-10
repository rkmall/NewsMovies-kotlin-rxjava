package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import io.reactivex.Maybe
import io.reactivex.Observable

interface IMovieLocalDataSource {

    fun getSavedMovieList(): Observable<List<DbMovie>>
    fun addMovieToDb(dbMovie: DbMovie): Maybe<Long>
    fun deleteMovieFromDb(id: Int): Maybe<Int>
}