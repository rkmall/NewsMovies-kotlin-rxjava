package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieDBResponse
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import com.rupesh.kotlinrxjavaex.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import retrofit2.Response
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
    private val getAllMovies: GetAllMovies,
    private val getSearchedMovie: GetSearchedMovie,
): ViewModel() {

    // RxJava CompositeDisposables
    private val disposable: CompositeDisposable = CompositeDisposable()

    // Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.view.fragment.WatchListFragment]
    private val movieLiveData: MutableLiveData<Resource<List<Movie>>> = MutableLiveData()
    val movieLiveDataResult: LiveData<Resource<List<Movie>>> get() = movieLiveData

    //private val searchedMovieLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    //val searchedMovieLiveDataResult: LiveData<List<Movie>> get() = movieLiveData

    // Status message to notify user about the completion of event
    private val statusMessage = MutableLiveData<Resource<String>>()

    val statusMessageResult: LiveData<Resource<String>> get() = statusMessage

    // Used for data binding by [com.rupesh.kotlinrxjavaex.view.MovieDetailActivity]
    var movie: Movie? = null

    /**
     * Gets a list of Movie wrapped inside MutableLiveData
     * @return the LiveData<List<DMovie>
     */
    fun getMovieList() {
        disposable.add(
            getAllMovies.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object: DisposableObserver<Response<MovieDBResponse>>() {
                    override fun onNext(t: Response<MovieDBResponse>) {
                        Log.i("MyTag", "onNextGetMovieListAPI response code: ${t.code()}")
                        movieLiveData.postValue(Resource.Success(t.body()!!.movies))
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetMovieListAPI")
                        statusMessage.postValue(Resource.Error(null, "Cannot fetch items"))
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetMovieListAPI")
                    }
                })
        )
    }


    /*fun getSearchedMovie(searchQuery: String) {
        disposable.add(
            getSearchedMovie.execute(searchQuery)
                .subscribeWith(object: DisposableObserver<List<Movie>>() {
                    override fun onNext(t: List<Movie>) {
                        Log.i("MyTag", "onNextGetMovieListAPI: $t")
                        searchedMovieLiveData.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetMovieListAPI")
                        statusMessage.postValue(Event("Something went wrong"))
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetMovieListAPI")
                        statusMessage.postValue(Event("Popular movies"))
                    }
                })
        )
    }
*/
    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     */
    fun clear() {
        disposable.clear()
        super.onCleared()
    }
}