package com.rupesh.kotlinrxjavaex.data.movie.repository

import com.rupesh.kotlinrxjavaex.data.movie.model.MovieResponse
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieLocalDataSource
import com.rupesh.kotlinrxjavaex.data.movie.repository.dataSource.IMovieRemoteDataSource
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.HttpException
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.MovieTestData

@RunWith(JUnit4::class)
class MovieRepositoryImplTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var movieLocalDataSourceImpl: IMovieLocalDataSource
    private lateinit var movieRemoteDataSourceImpl: IMovieRemoteDataSource
    private lateinit var movieRepositoryImpl: MovieRepositoryImpl
    private lateinit var movieTestData: MovieTestData

    @Before
    fun setUp() {
        movieRemoteDataSourceImpl = mock(IMovieRemoteDataSource::class.java)
        movieLocalDataSourceImpl = mock(IMovieLocalDataSource::class.java)
        movieRepositoryImpl = MovieRepositoryImpl(movieLocalDataSourceImpl, movieRemoteDataSourceImpl)
        movieTestData = MovieTestData()
    }

    @Test
    fun `getPopularMovie returns a list of popular movies on success`() {
        `when`(movieRemoteDataSourceImpl.getPopularMovies())
            .thenReturn(movieTestData.createSuccessResponseMovieObservable())

        val testObserver = movieRepositoryImpl.getPopularMovies().test()

        val expectedListSize = movieTestData.movieList.size

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
    fun `getPopularMovies throws Error on Response unsuccessful`() {
        `when`(movieRemoteDataSourceImpl.getPopularMovies())
            .thenReturn(movieTestData.createErrorResponseMovieObservable())

        val testObserver = movieRepositoryImpl.getPopularMovies().test()

        testObserver.await()
            .assertError(HttpException::class.java)
            .assertNotComplete()
            .assertErrorMessage("HTTP 400 bad-request")
    }

    @Test
    fun `saveMovie when given Movie returns Inserted ItemRowId`() {
        val movieList = movieTestData.movieList
        `when`(movieLocalDataSourceImpl.saveMovie(movieList[0])).thenReturn(Maybe.just(1L))

        movieRepositoryImpl.saveMovie(movieList[0]).test()
            .await()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun `deleteMovie when given movieId deletes the movie from db`() {
        val movieList = movieTestData.movieList
        `when`(movieLocalDataSourceImpl.deleteMovie(movieList[0].id)).thenReturn(Maybe.just(1))
        movieRepositoryImpl.deleteMovie(movieList[0].id).test()
            .await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `getSavedMovies returns all Movie items from db`() {
        val movieList = movieTestData.movieList
        `when`(movieLocalDataSourceImpl.getSavedMovies()).thenReturn(Observable.just(movieList))

        movieRepositoryImpl.getSavedMovies().test()
            .assertValue {
                it[0].originalTitle == movieList[0].originalTitle
            }
            .assertValue {
                it[1].originalTitle == movieList[1].originalTitle
            }
    }

    @Test
    fun `clear clears the Movie table`() {
        `when`(movieLocalDataSourceImpl.clear()).thenReturn(Maybe.just(100))

        movieRepositoryImpl.clear().test()
            .await()
            .assertValue {
                it == 100
            }
    }
}