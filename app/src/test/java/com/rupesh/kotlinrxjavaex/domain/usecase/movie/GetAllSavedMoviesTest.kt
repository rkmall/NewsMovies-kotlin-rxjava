package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.repository.MovieRepositoryImpl
import com.rupesh.kotlinrxjavaex.data.news.repository.NewsRepositoryImpl
import com.rupesh.kotlinrxjavaex.domain.usecase.news.GetSavedNewsArticles
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import sharedTest.testdata.movie.DbMovieTestData
import sharedTest.testdata.news.NewsDbTestData

class GetAllSavedMoviesTest {

    private lateinit var iMovieRepository: MovieRepositoryImpl
    private lateinit var getSavedMovies: GetAllSavedMovies
    private lateinit var movieTestData: DbMovieTestData

    @Before
    fun setUp() {
        iMovieRepository = Mockito.mock(MovieRepositoryImpl::class.java)
        getSavedMovies = GetAllSavedMovies(iMovieRepository)
        movieTestData = DbMovieTestData()
    }

    @Test
    fun execute_returnsSavedNewsListFromDb() {
        Mockito.`when`(iMovieRepository.getSavedMovieList())
            .thenReturn(movieTestData.getMovieListTestDataObservable())

        getSavedMovies.execute().test()
            .assertValue {
                it.size == 2
            }
            .assertNoErrors()
    }

}