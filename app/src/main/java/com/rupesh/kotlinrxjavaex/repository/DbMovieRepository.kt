package com.rupesh.kotlinrxjavaex.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.db.MovieDB
import com.rupesh.kotlinrxjavaex.db.entity.DbMovie
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class DbMovieRepository(
    val context: Context,
    val movieDB: MovieDB
) {

    private val movies: ArrayList<DbMovie> = ArrayList()

    private val moviesLiveData: MutableLiveData<List<DbMovie>> = MutableLiveData()

    private val disposable: CompositeDisposable = CompositeDisposable()

    private var movieObservable: Observable<List<DbMovie>>? = null


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
                    TODO("Not yet implemented")
                }

                override fun onComplete() {
                    TODO("Not yet implemented")
                }
            })
        )

        return moviesLiveData
    }

    fun addMovie(
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

    fun clear() {
        disposable.clear()
    }
}