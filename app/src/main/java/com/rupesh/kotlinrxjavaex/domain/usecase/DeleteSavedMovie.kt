package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository
import io.reactivex.Maybe

class DeleteSavedMovie(val dbMovieRepository: DbMovieRepository) {

    fun execute(dbMovie: DbMovie): Maybe<Int> {
        return dbMovieRepository.deleteMovieFromDB(dbMovie)
    }
}