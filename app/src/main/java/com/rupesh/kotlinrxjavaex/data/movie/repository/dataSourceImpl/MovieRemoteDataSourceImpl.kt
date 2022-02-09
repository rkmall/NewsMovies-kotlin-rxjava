package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieDBResponse
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieRemoteDataSource
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieDataService
import io.reactivex.Observable
import io.reactivex.Single

class MovieRemoteDataSourceImpl(
    private val movieDataService: MovieDataService
) : IMovieRemoteDataSource {
    override fun getTopMovies(): Observable<MovieDBResponse> {
        return movieDataService.getAllMoviesWithRx()
    }

    override fun getSearchedMovies(searchQuery: String): Single<List<Movie>> {
        return movieDataService.getSearchedMovie(searchQuery = searchQuery)
    }
}