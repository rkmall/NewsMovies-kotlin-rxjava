package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.domain.usecase.*
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.*
import com.rupesh.kotlinrxjavaex.presentation.util.Event
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.observers.DisposableSingleObserver
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
    private val getAllSavedMovies: GetAllSavedMovies,
    private val saveMovieToDb: SaveMovieToDb,
    private val deleteSavedMovie: DeleteSavedMovie
): ViewModel() {

    // RxJava CompositeDisposables
    private val disposable: CompositeDisposable = CompositeDisposable()

    // Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.presentation.fragment.WatchListFragment]
    private val movieLiveData: MutableLiveData<Resource<List<Movie>>> = MutableLiveData()
    val movieLiveDataResult: LiveData<Resource<List<Movie>>> get() = movieLiveData

    // Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.presentation.fragment.WatchListFragment]
    private val dbMovieListLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
    val dbMovieListResult: LiveData<List<Movie>> get() = dbMovieListLiveData

    // Status message to notify user about the completion of event
    private val statusMessage = MutableLiveData< Event<String>>()
    val statusMessageResult get() = statusMessage

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
                .subscribeWith(object: DisposableSingleObserver<Response<MovieResponse>>() {
                    override fun onSuccess(t: Response<MovieResponse>) {
                        val statusCode = t.code()
                        Log.i("MyTag", "onNextGetMovieListAPI response code: $statusCode")
                        if(statusCode == 200) {
                            movieLiveData.value = Resource.Success(t.body()!!.movies)
                        }else {
                            Log.i("MyTag", "onNextGetMovieListAPI error message: ${t.message()}")
                            //movieLiveData.postValue(Resource.Error(null, "Cannot fetch the movies"))
                        }
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetMovieListAPI message: ${e.message}")
                        movieLiveData.value = Resource.Error(null, "${e.message}")
                    }
                })
        )
    }


    /**
     * Gets a list of DMovie wrapped inside MutableLiveData
     * @return the LiveData<List<DMovie>
     */
    fun getAllMovieFromDb() {
        disposable.add(
            getAllSavedMovies.execute()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableObserver<List<Movie>>() {
                    override fun onNext(t: List<Movie>) {
                        Log.i("MyTag", "onNextGetMovieListDb: ${t.size}")
                        dbMovieListLiveData.value = t
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetMovieListDb")
                        statusMessage.value = Event("Could not fetch the saved movies")
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetMovieListDb")
                    }
                })
        )
    }

    /**
     * Forwards the operation to add DbMovie to DbMovieRepository
     * @param id the DMovie id
     * @param title the DMovie title
     * @param rating the DMovie rating
     * @param overview the DMovie overview
     * @param releaseDate the DMovie release date
     * @param posterPath the DMovie poster path (url)
     */
    fun addMovieToDB(movie: Movie) {

        disposable.add(
            saveMovieToDb.execute(movie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableMaybeObserver<Long>() {
                    override fun onSuccess(t: Long) {
                        Log.i("MyTag", "onSuccessAddMovieToDb: $t")
                        statusMessage.value = Event("Movie Saved")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorAddMovieTiDb: ${e.message}")
                        statusMessage.value = Event("Could not save the movie")
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteAddMovieToDb: Operation completed")
                    }
                })
        )
    }

    /**
     * Forwards the operation to delete DbMovie to DbMovieRepository
     * @param dbMovie the DbMovie instance to be deleted
     */
    fun deleteMovieFromDB(id: Int) {
        disposable.add(
            deleteSavedMovie.execute(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableMaybeObserver<Int>() {
                    override fun onSuccess(t: Int) {
                        Log.i("MyTag", "onSuccessDeleteDB: $t ")
                        statusMessage.value = Event("Movie Deleted")
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorDeleteDB: ${e.message}")
                        statusMessage.value = Event("Could not delete the movie")
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteDeleteDB")
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