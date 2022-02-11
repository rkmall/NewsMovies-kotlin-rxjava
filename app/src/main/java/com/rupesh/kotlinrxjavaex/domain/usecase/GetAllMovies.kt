package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieDBResponse
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Observable
import retrofit2.Response
import javax.inject.Inject

class GetAllMovies @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun execute(): Observable<Response<MovieDBResponse>> {
        return iMovieRepository.getTopMovies()
    }
}