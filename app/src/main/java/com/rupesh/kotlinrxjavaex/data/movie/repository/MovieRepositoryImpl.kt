package com.rupesh.kotlinrxjavaex.data.movie.repository

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieLocalDataSource
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieRemoteDataSource
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response

class MovieRepositoryImpl(
    private val iMovieLocalDataSource: IMovieLocalDataSource,
    private val iMovieRemoteDataSource: IMovieRemoteDataSource
) : IMovieRepository {

    override fun getTopMovies(): Single<Response<MovieResponse>> {
        return iMovieRemoteDataSource.getTopMovies()
    }

    override fun getSavedMovieList(): Observable<List<DbMovie>> {
        return iMovieLocalDataSource.getSavedMovieList()
    }

    override fun addMovieToDb(dbMovie: DbMovie): Maybe<Long> {
        return iMovieLocalDataSource.addMovieToDb(dbMovie)
    }

    override fun deleteMovieFromDb(id: Int): Maybe<Int> {
        return iMovieLocalDataSource.deleteMovieFromDb(id)
    }
}