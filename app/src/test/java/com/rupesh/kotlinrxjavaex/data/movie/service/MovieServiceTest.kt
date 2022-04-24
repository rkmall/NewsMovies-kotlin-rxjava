package com.rupesh.kotlinrxjavaex.data.movie.service

import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sharedTest.ApiResponseTestHelper
import sharedTest.RxImmediateSchedulerRule
import java.util.concurrent.TimeUnit


class MovieServiceTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var service: MovieService
    private lateinit var mockServer: MockWebServer
    private var mockUrl = "/movie/popular"
    private lateinit var testObserver: TestObserver<Response<MovieResponse>>

    @Before
    fun setUp() {
        mockServer = MockWebServer()

        service = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieService::class.java)

        testObserver = TestObserver<Response<MovieResponse>>()
    }

    @Test
    fun getPopularMovies_requestPath () {
        ApiResponseTestHelper.enqueueMockResponse(mockServer, "movie-response.json")
        service.getPopularMovies(url = mockUrl).subscribe(testObserver)
        val request = mockServer.takeRequest(3, TimeUnit.SECONDS)
        assertThat(request?.path)
            .isEqualTo("/movie/popular?api_key=${BuildConfig.API_MOVIE}")
    }

    @Test
    fun getPopularMovies_requestHeaderAndResponseCode () {
        ApiResponseTestHelper.enqueueMockResponse(mockServer, "movie-response.json")
        service.getPopularMovies(url = mockUrl).subscribe(testObserver)
        testObserver.await()
            .assertValue {
                return@assertValue it.headers()["content-type"]!! == "application/json"
            }
            .assertValue {
                return@assertValue it.code() == 200
            }
    }

    @Test
    fun getPopularMovies_pageAndTotalPages () {
        ApiResponseTestHelper.enqueueMockResponse(mockServer, "movie-response.json")
        service.getPopularMovies(url = mockUrl).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                println(it.body()!!.movies)
                return@assertValue it.body()?.page == 1
            }
            .assertValue {
                return@assertValue it.body()?.totalPages == 32467
            }
    }

    @Test
    fun getPopularMovies_movieObject () {
        ApiResponseTestHelper.enqueueMockResponse(mockServer, "movie-response.json")
        service.getPopularMovies(url = mockUrl).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.body()!!.movies[0].originalTitle == "Spider-Man: No Way Home"
            }
            .assertValue {
                return@assertValue it.body()!!.movies[1].originalTitle == "The King's Man"
            }
    }

    @After
    fun tearDown() {
        testObserver.dispose()
        mockServer.shutdown()
    }
}