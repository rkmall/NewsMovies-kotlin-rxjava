package com.rupesh.kotlinrxjavaex.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.model.Movie
import com.rupesh.kotlinrxjavaex.repository.MovieRepository

/**
 * DbMovieViewModel is [androidx.lifecycle.ViewModel]
 * Forwards database operations to Repository
 * [com.rupesh.kotlinrxjavaex.repository.MovieRepository]
 * and subsequently provides LiveData to Views to observe.
 * Instantiates MoviesViewModel
 * @param repository the MovieRepository
 * @author Rupesh Mall
 * @since 1.0
 */
class MovieViewModel(
    private val movieRepository: MovieRepository
): ViewModel() {

    /**
     * Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
     */
    var movieLiveData: LiveData<List<Movie>> = MutableLiveData()

    /**
     * Gets a list of Movie wrapped inside MutableLiveData
     * @return the LiveData<List<DMovie>
     */
    fun getMovieList() {
        movieLiveData = movieRepository.getMovieLiveData()
    }

    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     */
    fun clear() {
        movieRepository.clear()
        super.onCleared()
    }
}