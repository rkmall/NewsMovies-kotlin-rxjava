package com.rupesh.kotlinrxjavaex.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rupesh.kotlinrxjavaex.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.repository.DbMovieRepository

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
class DbMovieViewModel(
    val dbMovieRepository: DbMovieRepository
) : ViewModel()  {

    /**
     * Livedata of type DbMovie to be observed by [com.rupesh.kotlinrxjavaex.view.WatchListFragment]
     */
    var dbMovieMutableLiveData: MutableLiveData<List<DbMovie>> = MutableLiveData()

    /**
     * Gets a list of DMovie wrapped inside MutableLiveData
     * @return the LiveData<List<DMovie>
     */
    fun getAllMovieFromDb() {
        dbMovieMutableLiveData = dbMovieRepository.getMovieLiveDataFromDB()
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
                           releaseDate: String, posterPath: String ): Long {
        return dbMovieRepository.addMovieToDb(id, title, rating, overview, releaseDate, posterPath)
    }

    /**
     * Forwards the operation to delete DbMovie to DbMovieRepository
     * @param dbMovie the DbMovie instance to be deleted
     */
    fun deleteMovieFromDB(dbMovie: DbMovie): Int {
        return dbMovieRepository.deleteMovieFromDb(dbMovie)
    }

    /**
     * Clears the connection between Observables and Observers
     * added in CompositeDisposables
     */
    fun clear() {
        dbMovieRepository.clear()
        super.onCleared()
    }
}