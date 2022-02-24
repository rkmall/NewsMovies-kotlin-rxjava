package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.repository.MovieRepositoryImpl
import io.reactivex.Maybe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import sharedTest.testdata.movie.DbMovieTestData

class SaveMovieToDbTest {

    private lateinit var iMovieRepository: MovieRepositoryImpl
    private lateinit var saveMovie: SaveMovieToDb
    private lateinit var movieTestData: DbMovieTestData

    @Before
    fun setUp() {
        iMovieRepository = Mockito.mock(MovieRepositoryImpl::class.java)
        saveMovie = SaveMovieToDb(iMovieRepository)
        movieTestData = DbMovieTestData()
    }

    @Test
    fun execute_returnsSavedMovieId() {

        val movieList = movieTestData.getMovieListTestData()
        val expected = Maybe.create<Long> { emitter -> emitter.onSuccess(1L)  }

        Mockito.`when`(iMovieRepository.addMovieToDb(movieList[0]))
            .thenReturn(expected)

        saveMovie.execute(movieList[0]).test()
            .assertValue {
                it == 1L
            }
    }

}