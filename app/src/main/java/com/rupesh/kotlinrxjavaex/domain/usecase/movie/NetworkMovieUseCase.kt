package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import android.util.Log
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import com.rupesh.kotlinrxjavaex.presentation.util.AppConstPresentation
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import io.reactivex.Single
import javax.inject.Inject

class NetworkMovieUseCase @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun getPopularMovies(): Single<Resource<List<Movie>>> {
        return iMovieRepository
            .getPopularMovies()
            .map {
                Log.d(AppConstPresentation.LOG_UI, "Response code movie: ${it.code()}")
                when {
                    it.isSuccessful -> it.body()?.let { resource -> Resource.Success(resource.movies) }
                    else -> Resource.Error(message = "Error: ${it.code()}: Cannot fetch movies")
                }
            }
    }
}