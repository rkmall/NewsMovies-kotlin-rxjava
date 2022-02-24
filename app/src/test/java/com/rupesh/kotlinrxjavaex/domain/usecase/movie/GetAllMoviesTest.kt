package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.data.movie.repository.MovieRepositoryImpl
import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response
import sharedTest.testdata.movie.MovieResponseTestData

class GetAllMoviesTest {

    private lateinit var iMovieRepository: IMovieRepository
    private lateinit var getAllMovies: GetAllMovies
    private lateinit var responseTestData: MovieResponseTestData
    private lateinit var testObserver: TestObserver<Response<MovieResponse>>

    @Before
    fun setUp() {
        iMovieRepository = Mockito.mock(MovieRepositoryImpl::class.java)
        getAllMovies = GetAllMovies(iMovieRepository)
        responseTestData = MovieResponseTestData()
        testObserver = TestObserver()
    }

    @Test
    fun executeSuccess() {
        Mockito.`when`(iMovieRepository.getTopMovies())
            .thenReturn(responseTestData.getResponseDataSuccess())

        getAllMovies.execute().subscribe(testObserver)

        testObserver.await()
            .assertValue {
                it.code() == 200
            }
            .assertValue {
                it.body()!!.movies.size == 2
            }
    }

    @Test
    fun executeError() {
        Mockito.`when`(iMovieRepository.getTopMovies())
            .thenReturn(responseTestData.getResponseDataError())

        getAllMovies.execute().subscribe(testObserver)

        testObserver.await()
            .assertValue {
                it.code() == 400
            }
            .assertValue {
                it.message().toString() == "Response.error(), bad-request"
            }
            .assertValue {
                it.body().toString() == "null"
            }
    }
}