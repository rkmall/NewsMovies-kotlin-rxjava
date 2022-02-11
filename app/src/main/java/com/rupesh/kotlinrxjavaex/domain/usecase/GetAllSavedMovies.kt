package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetAllSavedMovies @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun execute(): Observable<List<DbMovie>> {
        return iMovieRepository.getSavedMovieList()
    }
}