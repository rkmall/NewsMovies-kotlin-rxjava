package com.rupesh.kotlinrxjavaex.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.data.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import javax.inject.Inject

class GetAllMovies @Inject constructor(private val movieRepository: MovieRepository) {

    fun execute(): MutableLiveData<List<Movie>> {
        movieRepository.getMovieLiveData()
        return movieRepository.movieListResult
    }

    fun clear() {
        movieRepository.clear()
    }
}