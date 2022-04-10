package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Maybe
import javax.inject.Inject

class SaveMovieToDb @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {

    fun execute(movie: Movie): Maybe<Long> {
        return iMovieRepository.addMovieToDb(movie)
    }
}