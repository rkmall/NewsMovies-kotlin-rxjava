package com.rupesh.kotlinrxjavaex.data.movie.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDB
import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl.MovieLocalDataSourceImpl
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl.MovieRemoteDataSourceImpl
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieDataService
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
import sharedTest.DatabaseTestHelper
import sharedTest.testdata.movie.DbMovieTestData

class MovieRepositoryImplTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: MovieDataService
    private lateinit var server: MockWebServer
    private lateinit var movieRemoteDataSourceImpl: MovieRemoteDataSourceImpl
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl
    private lateinit var testObserver: TestObserver<Response<MovieResponse>>

    private lateinit var movieDb: MovieDB
    private lateinit var movieLocalDataSourceImpl: MovieLocalDataSourceImpl
    private lateinit var movieTestData: DbMovieTestData

    @Before
    fun setUpFor() {
        // Setup for RemoteDataSource
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MovieDataService::class.java)

        movieRemoteDataSourceImpl = MovieRemoteDataSourceImpl(service)
        testObserver = TestObserver<Response<MovieResponse>>()

        // Setup for LocalDataSource
        val context: Context = ApplicationProvider.getApplicationContext()
        movieDb = DatabaseTestHelper.initializeDb(context, MovieDB::class.java)
        movieTestData = DbMovieTestData()
        movieLocalDataSourceImpl = MovieLocalDataSourceImpl(movieDb.getMovieDao())

        movieRepositoryImpl = MovieRepositoryImpl(movieLocalDataSourceImpl, movieRemoteDataSourceImpl)
    }

    @Test
    fun getPopularMovies() {
        val expectedListSize = 20
        ApiResponseTestHelper.enqueueMockResponse(server, "movie-response.json")

        movieRepositoryImpl.getTopMovies().subscribe(testObserver)

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

    @Test
    fun getSavedMovieFromDb_returnsAllItems() {
        val movieList = movieTestData.getMovieListTestData()

        movieRepositoryImpl.addMovieToDb(movieList[0]).test()
            .assertValue {
                it == 1L
            }

        movieRepositoryImpl.addMovieToDb(movieList[1]).test()
            .assertValue {
                it == 2L
            }

        movieRepositoryImpl.getSavedMovieList().test()
            .assertValue {
                it.size == 2
            }
            .assertValue {
                it[0].title == "Movie-1"
            }
            .assertValue {
                it[0].releaseDate == "20/01/2020"
            }
            .assertValue {
                it[1].title == "Movie-2"
            }
            .assertValue {
                it[1].releaseDate == "20/01/2021"
            }
    }

    @Test
    fun addMovie_givenMovie_returnsTheInsertedMovieId() {
        val movieList = movieTestData.getMovieListTestData()
        movieRepositoryImpl.addMovieToDb(movieList[0]).test()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun deleteMovieFromDb_whenGivenMovie_returnsNoOfRowDeleted() {
        val movieList = movieTestData.getMovieListTestData()

        movieRepositoryImpl.addMovieToDb(movieList[0]).test()
            .assertValue {
                it == 1L
            }

        movieRepositoryImpl.addMovieToDb(movieList[1]).test()
            .assertValue {
                it == 2L
            }

        movieRepositoryImpl.deleteMovieFromDb(1).test()
            .assertValue {
                it == 1
            }

        movieRepositoryImpl.getSavedMovieList().test()
            .assertValue {
                it.size == 1
            }
    }

    @After
    fun tearDown() {
        testObserver.dispose()
        server.shutdown()
        movieDb.close()
    }
}