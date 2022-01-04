package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllMovies
import com.rupesh.kotlinrxjavaex.domain.util.Event
import com.rupesh.kotlinrxjavaex.presentation.util.NetworkChecker

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
    private val getAllMovies: GetAllMovies
): ViewModel() {

    /**
     * Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
     */
    var movieLiveData: MutableLiveData<List<Movie>> = MutableLiveData()

    val statusMessage = MutableLiveData<Event<String>>()

    var movie: Movie? = null

    /**
     * Gets a list of Movie wrapped inside MutableLiveData
     * @return the LiveData<List<DMovie>
     */
    fun getMovieList() {
        movieLiveData = getAllMovies.execute()
        statusMessage.value = Event("Current popular movies")
    }


    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     */
    fun clear() {
        getAllMovies.clear()
        super.onCleared()
    }
}