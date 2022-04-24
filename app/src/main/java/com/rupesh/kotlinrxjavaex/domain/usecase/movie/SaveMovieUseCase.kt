package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class SaveMovieUseCase @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun getSavedMovies(): Observable<List<Movie>> {
        return iMovieRepository.getSavedMovies()
    }

    fun savedMovie(movie: Movie): Maybe<Long> {
        return iMovieRepository.saveMovie(movie)
    }

    fun deleteMovie(id: Int): Maybe<Int> {
        return iMovieRepository.deleteMovie(id)
    }

    fun clear() : Maybe<Int> {
        return iMovieRepository.clear()
    }
}