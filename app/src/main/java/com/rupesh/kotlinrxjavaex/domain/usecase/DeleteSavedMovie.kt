package com.rupesh.kotlinrxjavaex.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.data.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository

class DeleteSavedMovie(val dbMovieRepository: DbMovieRepository) {

    fun execute(dbMovie: DbMovie): MutableLiveData<Int> {
        dbMovieRepository.deleteMovieFromDb(dbMovie)
        return dbMovieRepository.noOfRowIdDeleted
    }
}