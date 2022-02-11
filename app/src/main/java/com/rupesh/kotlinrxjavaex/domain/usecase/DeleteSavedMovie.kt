package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Maybe
import javax.inject.Inject

class DeleteSavedMovie @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun execute(dbMovie: DbMovie): Maybe<Int> {
        return iMovieRepository.deleteMovieFromDb(dbMovie)
    }
}