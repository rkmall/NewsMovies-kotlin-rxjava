package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllMovies
import com.rupesh.kotlinrxjavaex.domain.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import javax.inject.Inject

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
@HiltViewModel
class MovieViewModel @Inject constructor(
    private val getAllMovies: GetAllMovies
): ViewModel() {

    // RxJava CompositeDisposables
    private val disposable: CompositeDisposable = CompositeDisposable()

    // Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
    private val movieLiveData: MutableLiveData<List<Movie>> = MutableLiveData()

    val movieLiveDataResult: LiveData<List<Movie>> get() = movieLiveData

    // Status message to notify user about the completion of event
    private val statusMessage = MutableLiveData<Event<String>>()

    val statusMessageResult: LiveData<Event<String>> get() = statusMessage

    // Used for data binding by [com.rupesh.kotlinrxjavaex.view.MovieDetailActivity]
    var movie: Movie? = null


    /**
     * Gets a list of Movie wrapped inside MutableLiveData
     * @return the LiveData<List<DMovie>
     */
    fun getMovieList() {
        disposable.add(
            getAllMovies.execute()
                .subscribeWith(object: DisposableObserver<List<Movie>>() {
                    override fun onNext(t: List<Movie>) {
                        Log.i("MyTag", "onNextGetListAPI: $t")
                        movieLiveData.value = t
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetListAPI")
                        statusMessage.value = Event("Something went wrong")

                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetListAPI")
                        statusMessage.value = Event("Popular movies")
                    }
                })
        )
    }


    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     */
    fun clear() {
        disposable.clear()
        super.onCleared()
    }
}