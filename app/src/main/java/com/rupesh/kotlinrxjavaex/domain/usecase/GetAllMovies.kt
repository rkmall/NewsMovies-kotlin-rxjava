package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetAllMovies @Inject constructor(
    private val movieRepository: MovieRepository
) {
    fun execute(): Observable<List<Movie>> {
        return movieRepository.getMovieListFromAPI()
    }
}