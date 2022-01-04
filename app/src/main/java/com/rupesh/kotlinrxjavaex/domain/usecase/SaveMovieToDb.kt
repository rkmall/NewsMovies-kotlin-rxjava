package com.rupesh.kotlinrxjavaex.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository

class SaveMovieToDb(val dbMovieRepository: DbMovieRepository) {

    fun execute(id: Long, title: String,
                rating: Double, overview: String,
                releaseDate: String, posterPath: String): MutableLiveData<Long> {
        dbMovieRepository.addMovieToDb(id, title, rating, overview, releaseDate, posterPath)
        return dbMovieRepository.rowIdInserted
    }
}