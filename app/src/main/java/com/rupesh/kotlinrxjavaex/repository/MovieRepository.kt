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

/**
 * MovieRepository class is repository that performs API call to
 * @see <a href="https://developers.themoviedb.org/3/getting-started/introduction"></a>
 * using Retrofit [com.rupesh.kotlinrxjavaex.service.RetrofitInstance] and
 * API service [com.rupesh.kotlinrxjavaex.service.MovieDataService]
 * It performs API call through RxJava reactive approach
 * using Observables and Observers.
 * Holds LiveData which ViewModel [com.rupesh.kotlinrxjavaex.viewmodel.MovieViewModel]
 * can get and subsequently pass to Views to observe
 * @author Rupesh Mall
 * @since 1.0
 */

class MovieRepository(private val service: MovieDataService) {

    // Intermediate holder to store API call result
    private val movies: ArrayList<Movie> = ArrayList()

    // MutableLiveData to hold the API call result
    private val moviesLiveData: MutableLiveData<List<Movie>> = MutableLiveData()

    // RxJava CompositeDisposables
    private val disposable: CompositeDisposable = CompositeDisposable()

    // RxJava Obseravables
    private var movieDBResponseObservable: Observable<MovieDBResponse>? = null

    /**
     * Gets a list of all Movies in succession from API call
     * @return the MutableLiveData<List<Movie> that wraps the result of API call
     */
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

    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     */
    fun clear() {
        disposable.clear()
    }
}