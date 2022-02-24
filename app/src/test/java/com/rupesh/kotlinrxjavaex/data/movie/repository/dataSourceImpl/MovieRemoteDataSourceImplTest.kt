package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieDataService
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sharedTest.ApiResponseTestHelper

class MovieRemoteDataSourceImplTest {

    private lateinit var service: MovieDataService
    private lateinit var server: MockWebServer
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl
    private lateinit var testObserver: TestObserver<Response<MovieResponse>>

    @Before
    fun setUp() {
        server = MockWebServer()

        service = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieDataService::class.java)

        movieRemoteDataSourceImpl = MovieRemoteDataSourceImpl(service)
        testObserver = TestObserver<Response<MovieResponse>>()
    }

    @Test
    fun `getPopularMovies return MovieResponse`() {
        val expectedListSize = 20
        ApiResponseTestHelper.enqueueMockResponse(server, "movie-response.json")

        movieRemoteDataSourceImpl
            .getTopMovies()
            .subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.body() is MovieResponse
            }
            .assertValue {
                return@assertValue it.body()!!.movies.size == expectedListSize
            }
            .assertComplete()
            .assertNoErrors()
    }

}