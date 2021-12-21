package com.rupesh.kotlinrxjavaex.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.repository.MovieRepository
import com.rupesh.kotlinrxjavaex.viewmodel.MovieViewModel

/**
 * Factory to instantiate [com.rupesh.kotlinrxjavaex.viewmodel.MovieViewModel]
 * Implements [ViewModelProvider.Factory]
 * @param movieRepository the MovieRepository
 */
class MovieVMFactory(
    val movieRepository: MovieRepository
): ViewModelProvider.Factory{


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MovieViewModel(movieRepository) as T
    }
}