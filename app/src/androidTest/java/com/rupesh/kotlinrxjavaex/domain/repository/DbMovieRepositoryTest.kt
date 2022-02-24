/*
package com.rupesh.kotlinrxjavaex.domain.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.rupesh.kotlinrxjavaex.data.movie.db.MovieDB
import com.rupesh.kotlinrxjavaex.data.movie.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.data.news.db.NewsDB
import com.rupesh.kotlinrxjavaex.data.news.db.NewsDao
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.DbMovieTestData


@RunWith(MockitoJUnitRunner::class)
class DbMovieRepositoryTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    // To execute rx tasks on the current thread in the FIFO manner (synchronously)
    @get:Rule
    var rxRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var newsDb: NewsDB

    lateinit var newsDao: NewsDao

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        newsDb = Room.inMemoryDatabaseBuilder(context, NewsDB::class.java).build()
        newsDao = newsDb.getNewsDao()
    }





    @Test
    fun addMovie_whenGivenDMovie_addDMovieToDatabase_id() {

        // Set test data
        val id = 1L
        val title = "New Movie"
        val rating = 8.0
        val overview = "Action movie of the year"
        val releaseDate = "10/12/2021"
        val posterPath = "https://poster_path456.com"

        // Test method
        val num: Long = repository.addMovieToDb(id, title, rating, overview, releaseDate, posterPath)
        assertEquals(1L, num)
    }

    @Test
    fun getMovieList_whenGivenDMovie_getDMovieList_name() {
        // Set test data using DbMovieTestData
        val testData = DbMovieTestData()
        val testDataList: List<DbMovie> = testData.getTestMovieList()

        for (movie in testDataList) {
            repository.addMovieToDb(
                movie.id,
                movie.title,
                movie.rating,
                movie.overview,
                movie.releaseDate,
                movie.posterPath
            )
        }

        // Test method
        val actual: LiveData<List<DbMovie>> = repository.getMovieLiveDataFromDB()
        val actualList: List<DbMovie> = actual.value!!
        assertEquals(testDataList[0].title, actualList[0].title)
    }

    @Test
    fun getMovieList_whenGivenDMovie_deleteDMovie() {

        // Set test data

        // First movie will be deleted
        val movie = DbMovie(0L,
            "New Movie",
            8.0,
            "Action movie of the year",
            "10/12/2020",
            "https://poster_path123.com"
        )

        repository.addMovieToDb(0L,
            "New Movie",
            8.0,
            "Action movie of the year",
            "10/12/2020",
            "https://poster_path123.com")


        repository.addMovieToDb(0L,
            "New Movie 2020",
            9.0,
            "New movie of the year",
            "11/10/2021",
            "https://poster_path456.com")


        val expected = 1

        // Test method
        repository.deleteMovieFromDb(movie)

        val actual: LiveData<List<DbMovie>> = repository.getMovieLiveDataFromDB()

        val actualList: List<DbMovie> = actual.value!!

        assertEquals(expected, actualList.size)
    }

    @After
    fun tearDown() {
        //
    }
}
*/
