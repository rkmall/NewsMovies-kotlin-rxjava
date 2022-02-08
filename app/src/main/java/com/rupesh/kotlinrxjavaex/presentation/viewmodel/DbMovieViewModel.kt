package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.usecase.DeleteSavedMovie
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllSavedMovies
import com.rupesh.kotlinrxjavaex.domain.usecase.SaveMovieToDb
import com.rupesh.kotlinrxjavaex.domain.util.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * DbMovieViewModel is [androidx.lifecycle.ViewModel]
 * Forwards database operations to Repository
 * [com.rupesh.kotlinrxjavaex.repository.DbMovieRepository]
 * and subsequently provides LiveData to Views to observe.
 * Instantiates DbMoviesViewModel
 * @param repository the DbMovieRepository
 * @author Rupesh Mall
 * @since 1.0
 */
@HiltViewModel
class DbMovieViewModel @Inject constructor(
    val getAllSavedMovies: GetAllSavedMovies,
    val saveMovieToDb: SaveMovieToDb,
    val deleteSavedMovie: DeleteSavedMovie
) : ViewModel()  {

    // RxJava CompositeDisposables
    private val disposable: CompositeDisposable = CompositeDisposable()

    // Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.view.fragment.WatchListFragment]
    private val dbMovieListLiveData: MutableLiveData<List<DbMovie>> = MutableLiveData()

    val dbMovieListResult: LiveData<List<DbMovie>> get() = dbMovieListLiveData


    // Status message to notify user about the completion of event
    private val statusMessage = MutableLiveData<Event<String>>()

    val statusMessageResult: LiveData<Event<String>> get() = statusMessage

    /**
     * Gets a list of DMovie wrapped inside MutableLiveData
     * @return the LiveData<List<DMovie>
     */
    fun getAllMovieFromDb() {

        disposable.add(
            getAllSavedMovies.execute()
                .subscribeWith(object : DisposableObserver<List<DbMovie>>() {
                    override fun onNext(t: List<DbMovie>) {
                        Log.i("MyTag", "onNextGetMovieListDB")
                        dbMovieListLiveData.postValue(t)
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorGetMovieListDB")
                        statusMessage.postValue(Event("Something went wrong"))
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteGetMovieListDB")
                        statusMessage.postValue(Event("Your saved movies"))
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
    fun addMovieToDB(id: Long, title: String,
                           rating: Double, overview: String,
                           releaseDate: String, posterPath: String ) {

        disposable.add(
            saveMovieToDb.execute(id, title, rating, overview, releaseDate, posterPath)
                .subscribeWith(object : DisposableMaybeObserver<Long>() {
                    override fun onSuccess(t: Long) {
                        Log.i("MyTag", "onSuccessAddDB: $t ")
                        statusMessage.postValue(Event("Saved movie $title"))
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorAddDB: ${e.message}")
                        statusMessage.postValue(Event("Something went wrong"))
                    }

                    override fun onComplete() {
                        Log.i("MyTag", "onCompleteAddDB")
                    }
                })
        )
    }

    /**
     * Forwards the operation to delete DbMovie to DbMovieRepository
     * @param dbMovie the DbMovie instance to be deleted
     */
    fun deleteMovieFromDB(dbMovie: DbMovie) {

        disposable.add(
            deleteSavedMovie.execute(dbMovie)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableMaybeObserver<Int>() {
                    override fun onSuccess(t: Int) {
                        Log.i("MyTag", "onSuccessDeleteDB: $t ")
                        statusMessage.postValue(Event("Deleted movie ${dbMovie.title}"))
                    }

                    override fun onError(e: Throwable) {
                        Log.i("MyTag", "onErrorDeleteDB: ${e.message}")
                        statusMessage.postValue(Event("Something went wrong"))
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