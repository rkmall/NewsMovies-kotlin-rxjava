package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetAllSavedMovies @Inject constructor(val dbMovieRepository: DbMovieRepository) {

    fun execute(): Observable<List<DbMovie>> {
        return dbMovieRepository.getMovieListFromDB()
    }
}