package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.repository.MovieRepositoryImpl
import io.reactivex.Maybe
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import sharedTest.testdata.movie.DbMovieTestData

class DeleteSavedMovieTest {

    private lateinit var iMovieRepository: MovieRepositoryImpl
    private lateinit var deleteMovie: DeleteSavedMovie
    private lateinit var movieTestData: DbMovieTestData

    @Before
    fun setUp() {
        iMovieRepository = Mockito.mock(MovieRepositoryImpl::class.java)
        deleteMovie = DeleteSavedMovie(iMovieRepository)
        movieTestData = DbMovieTestData()
    }

    @Test
    fun execute_returnsNoOfRowsDeleted() {
        val expected = Maybe.create<Int> { emitter -> emitter.onSuccess(1)  }

        Mockito.`when`(iMovieRepository.deleteMovieFromDb(1))
            .thenReturn(expected)

        deleteMovie.execute(1).test()
            .assertValue {
                it == 1
            }
    }
}