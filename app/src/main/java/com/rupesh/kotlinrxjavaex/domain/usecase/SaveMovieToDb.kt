package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Maybe
import javax.inject.Inject

class SaveMovieToDb @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {

    fun execute(dbMovie: DbMovie): Maybe<Long> {
        return iMovieRepository.addMovieToDb(dbMovie)
    }

    /*fun execute(id: Long, title: String,
                rating: Double, overview: String,
                releaseDate: String, posterPath: String): Maybe<Long> {
        return dbMovieRepository.addMovieToDB (id, title, rating, overview, releaseDate, posterPath)
    }*/
}