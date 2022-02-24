package com.rupesh.kotlinrxjavaex.data.movie.repository.dataSourceImpl

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDB
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sharedTest.DatabaseTestHelper
import sharedTest.testdata.movie.DbMovieTestData

class MovieLocalDataSourceImplTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var movieDb: MovieDB
    lateinit var movieTestData: DbMovieTestData
    lateinit var movieLocalDataSourceImpl: MovieLocalDataSourceImpl

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        movieDb = DatabaseTestHelper.initializeDb(context, MovieDB::class.java)
        movieTestData = DbMovieTestData()
        movieLocalDataSourceImpl = MovieLocalDataSourceImpl(movieDb.getMovieDao())
    }


    @Test
    fun addMovieToDb_whenGivenMovie_returnsInsertedItemRowId() {
        val movieList = movieTestData.getMovieListTestData()
        movieLocalDataSourceImpl.addMovieToDb(movieList[0]).test()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun deleteMovieFromDb_whenGivenMovie_returnsNoOfRowDeleted() {
        val movieList = movieTestData.getMovieListTestData()

        movieLocalDataSourceImpl.addMovieToDb(movieList[0]).test()
            .assertValue {
                it == 1L
            }

        movieLocalDataSourceImpl.addMovieToDb(movieList[1]).test()
            .assertValue {
                it == 2L
            }

        movieLocalDataSourceImpl.deleteMovieFromDb(movieList[0]).test()
            .assertValue {
                it == 1
            }

        movieLocalDataSourceImpl.getSavedMovieList().test()
            .assertValue {
                it.size == 1
            }
    }

    @Test
    fun getSavedMoviesFromDb_returnsAllItems() {
        val movieList = movieTestData.getMovieListTestData()

        movieLocalDataSourceImpl.addMovieToDb(movieList[0]).test()
            .assertValue {
                it == 1L
            }

        movieLocalDataSourceImpl.addMovieToDb(movieList[1]).test()
            .assertValue {
                it == 2L
            }

        movieLocalDataSourceImpl.getSavedMovieList().test()
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
}