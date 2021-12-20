package com.rupesh.kotlinrxjavaex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.model.Movie
import com.rupesh.kotlinrxjavaex.repository.MovieRepository

class MovieViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {

    var movieLiveData: LiveData<List<Movie>> = MutableLiveData()

    fun getMovieList() {
        movieLiveData = movieRepository.getMovieLiveData()
    }

    fun clear() {
        movieRepository.clear()
        super.onCleared()
    }
}