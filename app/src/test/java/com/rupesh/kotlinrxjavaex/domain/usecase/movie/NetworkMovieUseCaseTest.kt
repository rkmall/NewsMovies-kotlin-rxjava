package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.MovieTestData

class NetworkMovieUseCaseTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var iMovieRepository: IMovieRepository
    private lateinit var networkMovieUseCase: NetworkMovieUseCase
    var movieTestData = MovieTestData()

    @Before
    fun setUp() {
        iMovieRepository = mock(IMovieRepository::class.java)
        networkMovieUseCase = NetworkMovieUseCase(iMovieRepository)
    }

    @Test
    fun `getPopular  returns a Resource Success with movie list on Response Success`() {
        `when`(iMovieRepository.getPopularMovies())
            .thenReturn(movieTestData.createSuccessResponseMovieObservable())

        val testObserver = networkMovieUseCase.getPopularMovies().test()

        testObserver.await()
            .assertValue {
                it.data is List<Movie>
            }
            .assertValue {
                it.data?.size == 2
            }
            .assertValue {
                it.data?.get(0)?.originalTitle == "First Movie"
            }
            .assertValue {
                it.data?.get(1)?.originalTitle == "Second Movie"
            }
            .assertNoErrors()
            .assertComplete()
    }


    @Test
    fun `getPopular return Resource Error with error message on Response Error`() {
        val errorResponse = movieTestData.createErrorResponseMovieObservable()
        val errorResponseCode = errorResponse.blockingGet().code()

        `when`(iMovieRepository.getPopularMovies())
            .thenReturn(errorResponse)

        val testObserver = networkMovieUseCase.getPopularMovies().test()

        testObserver.await()
            .assertValue {
                it.data == null
            }
            .assertValue {
                it.message == "Error: $errorResponseCode: Cannot fetch movies"
            }
    }
}