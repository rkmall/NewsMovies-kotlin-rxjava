package com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllMovies
import com.rupesh.kotlinrxjavaex.domain.usecase.GetSearchedMovie
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.MovieViewModel
import javax.inject.Inject

/**
 * Factory to instantiate [com.rupesh.kotlinrxjavaex.viewmodel.MovieViewModel]
 * Implements [ViewModelProvider.Factory]
 * @param movieRepository the MovieRepository
 */
class MovieVMFactory @Inject constructor(
    val getAllMovies: GetAllMovies,
    val getSearchedMovie: GetSearchedMovie
): ViewModelProvider.Factory{


    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return MovieViewModel(getAllMovies, getSearchedMovie) as T
    }
}