package com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.usecase.*
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.*
import com.rupesh.kotlinrxjavaex.presentation.util.AppConstPresentation
import com.rupesh.kotlinrxjavaex.presentation.util.Event
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.MaybeObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
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
    private val networkMovieUseCase: NetworkMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase
): ViewModel() {

    private val disposable: CompositeDisposable = CompositeDisposable()

    private val mPopularMovies: MutableLiveData<Resource<List<Movie>>> = MutableLiveData()
    val popularMovies: LiveData<Resource<List<Movie>>> get() = mPopularMovies

    private val mSavedMovies: MutableLiveData<List<Movie>> = MutableLiveData()
    val savedMovies: LiveData<List<Movie>> get() = mSavedMovies

    private val mEventMessage = MutableLiveData< Event<String>>()
    val eventMessage get() = mEventMessage

    fun getPopularMovies() {
        mPopularMovies.value = Resource.Loading()
        networkMovieUseCase.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object: SingleObserver<Resource<List<Movie>>> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onSuccess(t: Resource<List<Movie>>) {
                    Log.i(AppConstPresentation.LOG_UI, "onSuccess getPopularMovies ${t.data?.size}")
                    mPopularMovies.value = t
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError getPopularMovies: ${e.message}")
                }
            })
    }

    fun getSavedMovies() {
         saveMovieUseCase.getSavedMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : DisposableObserver<List<Movie>>() {
                override fun onNext(t: List<Movie>) {
                    Log.i(AppConstPresentation.LOG_UI, "onNext getSavedMovies: ${t.size}")
                    mSavedMovies.value = t
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError getSavedMovies ${e.message}")
                    mEventMessage.value = Event("Could not fetch the saved movies")
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete getSavedMovies")
                }
            })
    }

    fun saveMovie(movie: Movie) {
        saveMovieUseCase.savedMovie(movie)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<Long> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }
                override fun onSuccess(t: Long) {
                    Log.i(AppConstPresentation.LOG_UI, "onSuccess saveMovie: $t")
                    mEventMessage.value = Event("Movie Saved")
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError saveMovie: ${e.message}")
                    mEventMessage.value = Event("Could not save the movie")
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete saveMovie")
                }
            })
    }

    fun deleteMovie(id: Int) {
         saveMovieUseCase.deleteMovie(id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : MaybeObserver<Int> {
                override fun onSubscribe(d: Disposable) {
                    disposable.add(d)
                }

                override fun onSuccess(t: Int) {
                    Log.i(AppConstPresentation.LOG_UI, "onSuccess deleteMovie: $t ")
                    mEventMessage.value = Event("Movie Deleted")
                }

                override fun onError(e: Throwable) {
                    Log.i(AppConstPresentation.LOG_UI, "onError deleteMovie: ${e.message}")
                    mEventMessage.value = Event("Could not delete the Movie")
                }

                override fun onComplete() {
                    Log.i(AppConstPresentation.LOG_UI, "onComplete deleteMovie")
                }
            })
    }

    override fun onCleared() {
        disposable.clear()
        super.onCleared()
    }
}