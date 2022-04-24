package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDao
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieLocalDataSource
import io.reactivex.Maybe
import io.reactivex.Observable

class MovieLocalDataSourceImpl(
    private val movieDao: MovieDao
) : IMovieLocalDataSource {
    override fun getSavedMovies(): Observable<List<Movie>> {
        return movieDao.getSavedMovies()
    }

    override fun saveMovie(movie: Movie): Maybe<Long> {
        return movieDao.insertMovie(movie)
    }

    override fun deleteMovie(id: Int): Maybe<Int> {
        return movieDao.deleteMovie(id)
    }

    override fun clear(): Maybe<Int> {
        return movieDao.clear()
    }
}