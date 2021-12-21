package com.rupesh.kotlinrxjavaex.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.repository.DbMovieRepository
import com.rupesh.kotlinrxjavaex.viewmodel.DbMovieViewModel

/**
 * Factory to instantiate [com.rupesh.kotlinrxjavaex.viewmodel.DbMovieViewModel]
 * Implements [ViewModelProvider.Factory]
 * @param dbMovieRepository the DbMovieRepository
 */
class DbMovieVMFactory(
    val dbMovieRepository: DbMovieRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DbMovieViewModel(dbMovieRepository) as T
    }
}