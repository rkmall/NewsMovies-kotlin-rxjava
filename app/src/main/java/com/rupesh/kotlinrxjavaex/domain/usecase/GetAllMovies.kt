package com.rupesh.kotlinrxjavaex.domain.usecase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.data.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository

class GetAllMovies(private val movieRepository: MovieRepository) {

    fun execute(): MutableLiveData<List<Movie>> {
        movieRepository.getMovieLiveData()
        return movieRepository.movieListResult
    }

    fun clear() {
        movieRepository.clear()
    }
}