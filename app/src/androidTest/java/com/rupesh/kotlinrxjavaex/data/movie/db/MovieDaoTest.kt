package com.rupesh.kotlinrxjavaex.data.movie.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sharedTest.DatabaseTestHelper
import sharedTest.testdata.movie.DbMovieTestData

class MovieDaoTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var movieDb: MovieDB
    lateinit var movieDao: MovieDao
    lateinit var movieTestData: DbMovieTestData

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        movieDb = DatabaseTestHelper.initializeDb(context, MovieDB::class.java)
        movieDao = movieDb.getMovieDao()
        movieTestData = DbMovieTestData()
    }

    @Test
    fun addMovieToDb_whenGivenMovie_returnsInsertedItemRowId() {
        val movieList = movieTestData.getMovieListTestData()
        movieDao.addMovie(movieList[0]).test()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun deleteMovieFromDb_whenGivenMovie_returnsNoOfRowDeleted() {
        val movieList = movieTestData.getMovieListTestData()

        movieDao.addMovie(movieList[0]).test()
            .assertValue {
                it == 1L
            }

        movieDao.addMovie(movieList[1]).test()
            .assertValue {
                it == 2L
            }

        movieDao.deleteMovie(movieList[0]).test()
            .assertValue {
                it == 1
            }

        movieDao.getAllMovie().test()
            .assertValue {
                it.size == 1
            }
    }

    @Test
    fun getSavedMoviesFromDb_returnsAllItems() {
        val movieList = movieTestData.getMovieListTestData()

        movieDao.addMovie(movieList[0]).test()
            .assertValue {
                it == 1L
            }

        movieDao.addMovie(movieList[1]).test()
            .assertValue {
                it == 2L
            }

        movieDao.getAllMovie().test()
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

    @After
    fun tearDown() {
        movieDb.close()
    }
}