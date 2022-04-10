package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class GetAllMovies @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun execute(): Single<Response<MovieResponse>> {
        return iMovieRepository.getTopMovies()
    }
}