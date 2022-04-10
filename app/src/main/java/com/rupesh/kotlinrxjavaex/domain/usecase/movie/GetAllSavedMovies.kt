package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetAllSavedMovies @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun execute(): Observable<List<Movie>> {
        return iMovieRepository.getSavedMovieList()
    }
}