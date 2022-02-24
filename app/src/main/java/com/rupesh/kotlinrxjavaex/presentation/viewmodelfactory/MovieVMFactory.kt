package com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.*
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import javax.inject.Inject

/**
 * Factory to instantiate [com.rupesh.kotlinrxjavaex.viewmodel.MovieViewModel]
 * Implements [ViewModelProvider.Factory]
 * @param movieRepository the MovieRepository
 */
class MovieVMFactory @Inject constructor(
    private val getAllMovies: GetAllMovies,
    private val getAllSavedMovies: GetAllSavedMovies,
    private val saveMovieToDb: SaveMovieToDb,
    private val deleteSavedMovie: DeleteSavedMovie
): ViewModelProvider.Factory{


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(
            getAllMovies,
            getAllSavedMovies,
            saveMovieToDb,
            deleteSavedMovie
        ) as T
    }
}