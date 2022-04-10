package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieRemoteDataSource
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieService
import io.reactivex.Single
import retrofit2.Response

class MovieRemoteDataSourceImpl(
    private val movieService: MovieService
) : IMovieRemoteDataSource {

    override fun getTopMovies(): Single<Response<MovieResponse>> {
        return movieService.getAllMoviesWithRx()
    }
}