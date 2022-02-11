package com.rupesh.kotlinrxjavaex.data.movie.repository

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieDBResponse
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieLocalDataSource
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieRemoteDataSource
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val iMovieLocalDataSource: IMovieLocalDataSource,
    private val iMovieRemoteDataSource: IMovieRemoteDataSource
) : IMovieRepository {

    override fun getTopMovies(): Observable<Response<MovieDBResponse>> {
        return iMovieRemoteDataSource.getTopMovies()
    }

    override fun getSearchedMovies(searchQuery: String): Single<Response<List<Movie>>> {
        return iMovieRemoteDataSource.getSearchedMovies(searchQuery)
    }

    override fun getSavedMovieList(): Observable<List<DbMovie>> {
        return iMovieLocalDataSource.getSavedMovieList()
    }

    override fun addMovieToDb(dbMovie: DbMovie): Maybe<Long> {
        return iMovieLocalDataSource.addMovieToDb(dbMovie)
    }

    override fun deleteMovieFromDb(dbMovie: DbMovie): Maybe<Int> {
        return iMovieLocalDataSource.deleteMovieFromDb(dbMovie)
    }
}