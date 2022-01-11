package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieDataService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

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

class MovieRepository @Inject constructor(private val service: MovieDataService) {

    /**
     * Gets a list of all Movies in succession from API call
     * @return the Observable<List<Movie> that wraps the result of API call
     */
    fun getMovieListFromAPI(): Observable<List<Movie>> {
        val movieDBResponseObservable = service.getAllMoviesWithRx(BuildConfig.API_KEY)

        return movieDBResponseObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map { t -> t.movies }
    }


    /*fun getMovieLiveData() {
        movieDBResponseObservable = service.getAllMoviesWithRx(BuildConfig.API_KEY)

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
                        Log.i("RxError: ", "Get all movie error")
                    }

                    override fun onComplete() {
                        movieListResult.postValue(movies)
                    }
                })
        )
    }*/
}