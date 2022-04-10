package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieRemoteDataSource
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieDataService
import io.reactivex.Single
import retrofit2.Response

class MovieRemoteDataSourceImpl(
    private val movieDataService: MovieDataService
) : IMovieRemoteDataSource {

    override fun getTopMovies(): Single<Response<MovieResponse>> {
        return movieDataService.getAllMoviesWithRx()
    }
}