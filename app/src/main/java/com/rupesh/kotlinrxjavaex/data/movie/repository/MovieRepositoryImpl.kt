package com.rupesh.kotlinrxjavaex.data.movie.repository

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

    override fun getPopularMovies(): Single<Response<MovieResponse>> {
        return iMovieRemoteDataSource.getPopularMovies()
    }

    override fun getSavedMovies(): Observable<List<Movie>> {
        return iMovieLocalDataSource.getSavedMovies()
    }

    override fun saveMovie(movie: Movie): Maybe<Long> {
        return iMovieLocalDataSource.saveMovie(movie)
    }

    override fun deleteMovie(id: Int): Maybe<Int> {
        return iMovieLocalDataSource.deleteMovie(id)
    }

    override fun clear(): Maybe<Int> {
        return iMovieLocalDataSource.clear()
    }
}