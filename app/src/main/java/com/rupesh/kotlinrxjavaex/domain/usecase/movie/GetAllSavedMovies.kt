package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

class GetAllSavedMovies @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun execute(): Observable<List<DbMovie>> {
        return iMovieRepository.getSavedMovieList()
    }
}