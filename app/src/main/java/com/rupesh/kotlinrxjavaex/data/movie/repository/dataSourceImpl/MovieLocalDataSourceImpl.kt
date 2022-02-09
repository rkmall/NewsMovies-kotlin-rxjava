package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDao
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieLocalDataSource
import io.reactivex.Maybe
import io.reactivex.Observable

class MovieLocalDataSourceImpl(
    private val movieDao: MovieDao
) : IMovieLocalDataSource {
    override fun getSavedMovieList(): Observable<List<DbMovie>> {
        return movieDao.getAllMovie()
    }

    override fun addMovie(dbMovie: DbMovie): Maybe<Long> {
        return movieDao.addMovie(dbMovie)
    }

    override fun deleteMovie(dbMovie: DbMovie): Maybe<Int> {
        return movieDao.deleteMovie(dbMovie)
    }
}