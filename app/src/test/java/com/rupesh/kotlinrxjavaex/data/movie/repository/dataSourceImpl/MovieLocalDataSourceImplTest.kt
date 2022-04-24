package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDao
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.MovieTestData

@RunWith(JUnit4::class)
class MovieLocalDataSourceImplTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var movieDao: MovieDao
    lateinit var movieTestData: MovieTestData
    lateinit var movieLocalDataSourceImpl: MovieLocalDataSourceImpl

    @Before
    fun setUp() {
        movieDao = mock(MovieDao::class.java)
        movieLocalDataSourceImpl = MovieLocalDataSourceImpl(movieDao)
        movieTestData = MovieTestData()
    }


    @Test
    fun `add Movie to Db when given Movie returns Inserted ItemRowId`() {
        val movieList = movieTestData.movieList
        `when`(movieDao.insertMovie(movieList[0])).thenReturn(Maybe.just(1L))

        movieLocalDataSourceImpl.saveMovie(movieList[0]).test()
            .await()
            .assertValue {
                it == 1L
            }
    }


    @Test
    fun `deleteMovie when given movieId deletes the movie from db`() {
        val movieList = movieTestData.movieList
        `when`(movieDao.deleteMovie(movieList[0].id)).thenReturn(Maybe.just(1))
        movieLocalDataSourceImpl.deleteMovie(movieList[0].id).test()
            .await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `getSavedMovies returns all Movie items from db`() {
        val movieList = movieTestData.movieList
        `when`(movieDao.getSavedMovies()).thenReturn(Observable.just(movieList))

        movieLocalDataSourceImpl.getSavedMovies().test()
            .assertValue {
                it[0].originalTitle == movieList[0].originalTitle
            }
            .assertValue {
                it[1].originalTitle == movieList[1].originalTitle
            }
    }

    @Test
    fun `clear clears the Movie table`() {
        `when`(movieDao.clear()).thenReturn(Maybe.just(100))

        movieLocalDataSourceImpl.clear().test()
            .await()
            .assertValue {
                it == 100
            }
    }
}