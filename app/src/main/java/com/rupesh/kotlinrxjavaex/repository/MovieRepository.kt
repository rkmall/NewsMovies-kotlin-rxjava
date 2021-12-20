package com.rupesh.kotlinrxjavaex.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.rupesh.kotlinrxjavaex.model.Movie
import com.rupesh.kotlinrxjavaex.model.MovieDBResponse
import com.rupesh.kotlinrxjavaex.service.MovieDataService
import com.rupesh.kotlinrxjavaex.utils.AppConstants
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.Function
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MovieRepository(private val service: MovieDataService) {

    private val movies: ArrayList<Movie> = ArrayList()

    private val moviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()

    private val disposable: CompositeDisposable = CompositeDisposable()

    private var movieDBResponseObservable: Observable<MovieDBResponse>? = null

    fun getMovieLiveData(): MutableLiveData<List<Movie>> {

        movieDBResponseObservable = service.getAllMoviesWithRx(AppConstants.api_key)

        disposable.add(
            movieDBResponseObservable!!
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(object: Function<MovieDBResponse, Observable<Movie>> {
                    override fun apply(t: MovieDBResponse): Observable<Movie> {
                        return Observable.fromIterable(t.movies)
                    }
                })
                .subscribeWith(object: DisposableObserver<Movie>() {
                    override fun onNext(t: Movie) {
                        movies.add(t)
                    }

                    override fun onError(e: Throwable) {

                    }

                    override fun onComplete() {
                        moviesLiveData.postValue(movies)
                    }
                })
        )
        return moviesLiveData
    }


    fun clear() {
        disposable.clear()
    }
}