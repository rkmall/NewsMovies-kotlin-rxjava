package com.rupesh.kotlinrxjavaex.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository
import io.reactivex.Maybe

class SaveMovieToDb(val dbMovieRepository: DbMovieRepository) {

    fun execute(id: Long, title: String,
                rating: Double, overview: String,
                releaseDate: String, posterPath: String): Maybe<Long> {
        return dbMovieRepository.addMovieToDB (id, title, rating, overview, releaseDate, posterPath)
    }
}