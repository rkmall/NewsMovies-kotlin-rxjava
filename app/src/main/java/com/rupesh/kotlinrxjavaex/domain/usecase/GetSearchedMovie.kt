package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import io.reactivex.Observable
import java.util.*
import javax.inject.Inject

class GetSearchedMovie @Inject constructor(
    private val movieRepository: MovieRepository
) {
    fun execute(searchQuery: String): Observable<List<Movie>> {
        return movieRepository.getSearchedMovieFromAPI(searchQuery)
    }
}