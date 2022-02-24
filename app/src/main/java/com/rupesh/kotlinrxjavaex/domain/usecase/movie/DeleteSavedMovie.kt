package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Maybe
import javax.inject.Inject

class DeleteSavedMovie @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun execute(id: Int): Maybe<Int> {
        return iMovieRepository.deleteMovieFromDb(id)
    }
}