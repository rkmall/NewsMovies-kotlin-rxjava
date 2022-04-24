package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieService
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import retrofit2.Response
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.MovieTestData


@RunWith(JUnit4::class)
class MovieRemoteDataSourceImplTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var service: MovieService
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl
    private lateinit var responseTestData: MovieTestData
    private lateinit var testObserver: TestObserver<Response<MovieResponse>>

    @Before
    fun setUp() {
        service = Mockito.mock(MovieService::class.java)
        movieRemoteDataSourceImpl = MovieRemoteDataSourceImpl(service)
        responseTestData = MovieTestData()
        testObserver = TestObserver<Response<MovieResponse>>()
    }

    @Test
    fun `getPopularMovies returns SuccessMovieResponse`() {
        `when`(service.getPopularMovies())
            .thenReturn(responseTestData.createSuccessResponseMovieObservable())

        movieRemoteDataSourceImpl.getPopularMovies().subscribe(testObserver)

        val expectedListSize = 2

        testObserver.await()
            .assertValue {
                return@assertValue it.body() is MovieResponse
            }
            .assertValue {
                return@assertValue it.body()!!.movies.size == expectedListSize
            }
            .assertValue {
                return@assertValue it.code() == 200
            }
            .assertComplete()
            .assertNoErrors()
    }


    @Test
    fun `getPopularMovies returns ErrorResponse on unsuccessful response`() {
        `when`(service.getPopularMovies())
            .thenReturn(responseTestData.createErrorResponseMovieObservable())

        movieRemoteDataSourceImpl.getPopularMovies().subscribe(testObserver)

        testObserver.await()
            .assertValue {
                it.code() == 400
            }
            .assertValue {
                it.message() == "bad-request"
            }
    }

    @After
    fun tearDown() {
        testObserver.dispose()
    }
}