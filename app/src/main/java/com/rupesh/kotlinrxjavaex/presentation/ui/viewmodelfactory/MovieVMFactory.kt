/*
package com.rupesh.kotlinrxjavaex.presentation.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.*
import javax.inject.Inject

*/
/**
 * Factory to instantiate [com.rupesh.kotlinrxjavaex.viewmodel.MovieViewModel]
 * Implements [ViewModelProvider.Factory]
 * @param movieRepository the MovieRepository
 *//*

class MovieVMFactory @Inject constructor(
    private val networkMovieUseCase: NetworkMovieUseCase,
    private val saveMovieUseCase: SaveMovieUseCase,
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            NetworkMovieUseCase::class.java,
            SaveMovieUseCase::class.java
        ).newInstance(
            networkMovieUseCase,
            saveMovieUseCase
        )
    }
}*/
