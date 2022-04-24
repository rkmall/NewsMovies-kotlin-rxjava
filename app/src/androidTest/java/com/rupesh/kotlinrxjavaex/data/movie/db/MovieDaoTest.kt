package com.rupesh.kotlinrxjavaex.data.movie.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.data.common.db.AppDB
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import sharedTest.DatabaseTestHelper
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.MovieTestData

@RunWith(AndroidJUnit4::class)
class MovieDaoTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var appDB: AppDB
    lateinit var movieDao: MovieDao
    lateinit var movieTestData: MovieTestData

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDB = DatabaseTestHelper.initializeDb(context, AppDB::class.java)
        movieDao = appDB.getMovieDao()
        movieTestData = MovieTestData()
    }

    @Test
    fun insert_MovieToLocalDatabase_whenGivenMovie_Returns_InsertedItemRowId() {
        val movieList = movieTestData.movieList
        movieDao.insertMovie(movieList[0]).test()
            .assertValue {
                it == 1L
            }
    }


    @Test
    fun deleteMovieFromDb_whenGivenMovieId_deletesTheMovie() {
        val movieList = movieTestData.movieList

        val insertedId = movieDao.insertMovie(movieList[0]).blockingGet()
        assertThat(insertedId).isEqualTo(1)

        movieDao.getSavedMovies().subscribe({
            movieDao.deleteMovie(insertedId.toInt()).test().await()
        }, { throwable -> throwable.printStackTrace() })

        movieDao.getSavedMovies().test()
            .assertValue {
                it.isEmpty()
            }
    }

    @Test
    fun getSavedMoviesFromDb_returnsAllItems() {
        val movieList = movieTestData.movieList

        movieDao.insertMovie(movieList[0]).test()
            .assertValue {
                it == 1L
            }

        movieDao.insertMovie(movieList[1]).test()
            .assertValue {
                it == 2L
            }

        movieDao.getSavedMovies().test()
            .assertValue {
                it.size == 2
            }
            .assertValue {
                it[0].originalTitle == "First Movie"
            }
            .assertValue {
                it[1].originalTitle == "Second Movie"
            }
    }

    @Test
    fun clearSavedMoviesFromDb_clearsMovieTable() {
        val movieList = movieTestData.movieList

        movieDao.insertMovie(movieList[0]).test()
            .assertValue {
                it == 1L
            }

        movieDao.insertMovie(movieList[1]).test()
            .assertValue {
                it == 2L
            }

        movieDao.getSavedMovies().test()
            .assertValue {
                it.size == 2
            }

        movieDao.clear().test().await()

        movieDao.getSavedMovies().test()
            .assertValue {
                it.isEmpty()
            }
    }

    @After
    fun tearDown() {
        appDB.close()
    }
}