package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDao
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieLocalDataSource
import io.reactivex.Maybe
import io.reactivex.Observable

class MovieLocalDataSourceImpl(
    private val movieDao: MovieDao
) : IMovieLocalDataSource {
    override fun getSavedMovieList(): Observable<List<Movie>> {
        return movieDao.getAllMovie()
    }

    override fun addMovieToDb(movie: Movie): Maybe<Long> {
        return movieDao.addMovie(movie)
    }

    override fun deleteMovieFromDb(id: Int): Maybe<Int> {
        return movieDao.deleteMovie(id)
    }
}