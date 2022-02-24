package com.rupesh.kotlinrxjavaex.data.movie.service

import com.google.common.truth.Truth
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sharedTest.ApiResponseTestHelper

@RunWith(JUnit4::class)
class MovieServiceTest {

    private lateinit var service: MovieDataService
    private lateinit var server: MockWebServer
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

        testObserver = TestObserver<Response<MovieResponse>>()
    }

    @Test
    fun getPopularMovies_requestPath () {
        ApiResponseTestHelper.enqueueMockResponse(server, "movie-response.json")
        service.getAllMoviesWithRx().subscribe(testObserver)
        val request = server.takeRequest()
        Truth.assertThat(request.path)
            .isEqualTo("/movie/popular?api_key=${BuildConfig.API_KEY}")
    }


    @Test
    fun getPopularMovies_requestHeaderAndResponseCode () {
        ApiResponseTestHelper.enqueueMockResponse(server, "news-response.json")
        service.getAllMoviesWithRx().subscribe(testObserver)
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
        ApiResponseTestHelper.enqueueMockResponse(server, "movie-response.json")
        service.getAllMoviesWithRx().subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.body()?.page == 1
            }
            .assertValue {
                return@assertValue it.body()?.totalPages == 32467
            }
    }

    @Test
    fun getPopularMovies_movieObject () {
        ApiResponseTestHelper.enqueueMockResponse(server, "movie-response.json")
        service.getAllMoviesWithRx().subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.body()!!.movies[0].original_title == "Spider-Man: No Way Home"
            }
            .assertValue {
                return@assertValue it.body()!!.movies[1].original_title == "The King's Man"
            }
    }

    @After
    fun tearDown() {
        testObserver.dispose()
        server.shutdown()
    }
}