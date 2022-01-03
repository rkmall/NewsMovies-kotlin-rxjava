package com.rupesh.kotlinrxjavaex.domain.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.data.db.MovieDB
import com.rupesh.kotlinrxjavaex.data.db.entity.DbMovie
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableMaybeObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

/**
 * DbMovieRepository class is repository that connects App's database
 * [com.rupesh.kotlinrxjavaex.db.MovieDB] using DAO
 * [com.rupesh.kotlinrxjavaex.db.MovieDao]
 * It implements the database operations through RxJava reactive approach
 * using Observables and Observers.
 * Holds LiveData which ViewModel [com.rupesh.kotlinrxjavaex.viewmodel.DbMovieViewModel]
 * can get and subsequently pass to Views to observe.
 * Instantiates DbMovieRepository
 * @param db the Application Database
 * @author Rupesh Mall
 * @since 1.0
 */
class DbMovieRepository(
    val context: Context,
    val movieDB: MovieDB
) {

    // MutableLiveData to hold the result of database operation
    private val moviesLiveData: MutableLiveData<List<DbMovie>> = MutableLiveData()

    // RxJava CompositeDisposable
    private val disposable: CompositeDisposable = CompositeDisposable()

    // RxJava observable
    private var movieObservable: Observable<List<DbMovie>>? = null


    /**
     * Gets a list of all Movies from Database [com.rupesh.kotlinrxjavaex.db.MovieDB]
     * @return the MutableLiveData<List<DMovie> that wraps the result of database operation
     */
    fun getMovieLiveDataFromDB(): MutableLiveData<List<DbMovie>> {

        movieObservable = movieDB.getMovieDao().getAllMovie()

        disposable.add(movieObservable!!
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object: DisposableObserver<List<DbMovie>>() {
                override fun onNext(t: List<DbMovie>) {
                    moviesLiveData.postValue(t)
                }

                override fun onError(e: Throwable) {
                }

                override fun onComplete() {
                }
            })
        )

        return moviesLiveData
    }

    /**
     * Adds and instance of [com.rupesh.kotlinrxjavaex.db.entity.DbMovie]
     * into the database
     * @param id the DMovie id
     * @param title the DMovie title
     * @param rating the DMovie rating
     * @param overview the DMovie overview
     * @param releaseDate the DMovie release date
     * @param posterPath the DMovie poster path (url)
     */
    fun addMovieToDb(
        id: Long, title: String,
        rating: Double, overview: String,
        releaseDate: String, posterPath: String
    ): Long {

        var rowIdInserted: Long = -1

        disposable.add(Completable.fromAction {
            val movie = DbMovie(id, title, rating, overview, releaseDate, posterPath)
            rowIdInserted = movieDB.getMovieDao().addMovie(movie)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableCompletableObserver() {
                override fun onComplete() {
                    //DisplayUtils.displayToast(context, "Movie added to Watch List: " + rowIdInserted);
                }

                override fun onError(e: Throwable) {
                    //DisplayUtils.displayToast(context, "Something went wrong");
                }
            })
        )
        return rowIdInserted
    }

    /**
     * Deletes the DbMovie from App's database provided the DbMovie instance
     * @param dbMovie the DbMovie instance that needs to be deleted from database
     */
    fun deleteMovieFromDb(dbMovie: DbMovie): Int {

        var noOfRowDeleted = -1;

        disposable.add(Maybe.fromAction<Any> {
            noOfRowDeleted = movieDB.getMovieDao().deleteMovie(dbMovie)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableMaybeObserver<Any?>() {
                override fun onSuccess(o: Any) {}
                override fun onError(e: Throwable) {}
                override fun onComplete() {
                    //DisplayUtils.displayToast(context, "No. of row deleted: " + noOfRowDeleted );
                }
            })
        )

        return noOfRowDeleted
    }


    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     */
    fun clear() {
        disposable.clear()
    }
}