package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.Single
import retrofit2.Response

interface IMovieRemoteDataSource {

    fun getTopMovies(): Single<Response<MovieResponse>>
}