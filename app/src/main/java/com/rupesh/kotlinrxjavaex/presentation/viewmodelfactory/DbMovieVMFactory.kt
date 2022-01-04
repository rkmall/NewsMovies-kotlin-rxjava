package com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository
import com.rupesh.kotlinrxjavaex.domain.usecase.DeleteSavedMovie
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllMovies
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllSavedMovies
import com.rupesh.kotlinrxjavaex.domain.usecase.SaveMovieToDb
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.DbMovieViewModel

/**
 * Factory to instantiate [com.rupesh.kotlinrxjavaex.viewmodel.DbMovieViewModel]
 * Implements [ViewModelProvider.Factory]
 * @param dbMovieRepository the DbMovieRepository
 */
class DbMovieVMFactory(
    val getAllSavedMovies: GetAllSavedMovies,
    val saveMovieToDb: SaveMovieToDb,
    val deleteSavedMovie: DeleteSavedMovie
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DbMovieViewModel(getAllSavedMovies, saveMovieToDb, deleteSavedMovie) as T
    }
}