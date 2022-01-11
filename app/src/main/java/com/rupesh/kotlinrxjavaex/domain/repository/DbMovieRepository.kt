package com.rupesh.kotlinrxjavaex.domain.repository

import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDB
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
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
    val movieDB: MovieDB
) {
    /**
     * Gets a list of all Movies from Database [com.rupesh.kotlinrxjavaex.db.MovieDB]
     * @return the MutableLiveData<List<DMovie> that wraps the result of database operation
     */
    fun getMovieListFromDB(): Observable<List<DbMovie>> {
        val movieObservable = movieDB.getMovieDao().getAllMovie()

        return movieObservable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
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
    fun addMovieToDB(
        id: Long, title: String,
        rating: Double, overview: String,
        releaseDate: String, posterPath: String
    ): Maybe<Long> {

        val movie = DbMovie(id, title, rating, overview, releaseDate, posterPath)
        val result: Maybe<Long> = movieDB.getMovieDao().addMovie(movie)

        return result
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }


    /**
     * Deletes the DbMovie from App's database provided the DbMovie instance
     * @param dbMovie the DbMovie instance that needs to be deleted from database
     */
    fun deleteMovieFromDB(dbMovie: DbMovie): Maybe<Int> {
        val result: Maybe<Int> = movieDB.getMovieDao().deleteMovie(dbMovie)

        return result
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
    }
}