package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class GetSearchedMovie @Inject constructor(
    private val iMovieRepository: IMovieRepository
) {
    fun execute(searchQuery: String): Single<Response<List<Movie>>> {
        return iMovieRepository.getSearchedMovies(searchQuery)
    }
}