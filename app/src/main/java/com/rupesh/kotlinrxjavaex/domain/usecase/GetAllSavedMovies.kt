package com.rupesh.kotlinrxjavaex.domain.usecase

import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.data.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository

class GetAllSavedMovies(val dbMovieRepository: DbMovieRepository) {

    fun execute(): MutableLiveData<List<DbMovie>> {
        dbMovieRepository.getMovieLiveDataFromDB()
        return dbMovieRepository.moviesLiveData
    }

    fun clear() {
        dbMovieRepository.clear()
    }
}