package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import com.rupesh.kotlinrxjavaex.data.db.entity.DbMovie
import com.rupesh.kotlinrxjavaex.domain.repository.DbMovieRepository

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.DbMovieTestData
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DbMovieViewModelTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    // To execute rx tasks on the current thread in the FIFO manner (synchronously)
    @get:Rule
    var rxRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var viewModel: DbMovieViewModel
    lateinit var repository: DbMovieRepository
    lateinit var testData: DbMovieTestData
    var movieMutableLiveData: MutableLiveData<List<DbMovie>> = MutableLiveData()

    @Before
    fun setUp() {
        repository = mock(DbMovieRepository::class.java)
        viewModel = DbMovieViewModel(repository)
        testData = DbMovieTestData()
    }

    @Test
    fun getMovieList_givenDbMovieRepository_returnsMovieLiveData_size() {
        // Set test data
        movieMutableLiveData.value = testData.getTestMovieList()

        // Mock return data with the test data
        Mockito.`when`(repository.getMovieLiveDataFromDB()).thenReturn(movieMutableLiveData)

        // Test method
        viewModel.getAllMovieFromDb()
        val expected: LiveData<List<DbMovie>> = movieMutableLiveData
        val actual: LiveData<List<DbMovie>> = viewModel.dbMovieMutableLiveData
        val expectedListSize = expected.value!!.size
        val actualListSize = actual.value!!.size

        // Assert
        Truth.assertThat(actualListSize).isEqualTo(expectedListSize)
    }

    @Test
    fun getMovieList_givenDbMovieRepository_returnsMovieLiveData_title() {
        // Set test data
        movieMutableLiveData.value = testData.getTestMovieList()

        // Mock return data with test data
        Mockito.`when`(repository.getMovieLiveDataFromDB()).thenReturn(movieMutableLiveData)

        // Test method
        viewModel.getAllMovieFromDb()
        val expected: LiveData<List<DbMovie>> = movieMutableLiveData
        val actual: LiveData<List<DbMovie>> = viewModel.dbMovieMutableLiveData
        val expectedMovie = expected.value?.get(0)
        val actualMovie = actual.value?.get(0)

        // Assert
        Truth.assertThat(expectedMovie?.title).isEqualTo(actualMovie?.title)
    }

    @Test
    fun addMovie_givenDbMovieRepository_addsMovieToDatabase() {

        // Create test data
        val id = 10L
        val title = "New Movie"
        val rating = 8.0
        val overview = "Action movie of the year"
        val releaseDate = "10/12/2021"
        val posterPath = "https://poster_path456.com"

        // Mock return data with test data
        Mockito.`when`(repository.addMovieToDb(id, title, rating, overview, releaseDate, posterPath))
            .thenReturn(10L)

        // Test method
        val actual: Long = viewModel.addMovieToDB(id, title, rating, overview, releaseDate, posterPath)

        // Assert
        Truth.assertThat(actual).isEqualTo(id)
    }

    @Test
    fun addMovie_givenDbMovieRepository_deletesMovieFromDatabase() {

        // Create test data
        val movie = DbMovie(10L,
                            "New Movie",
                            8.0,
                            "Action movie of the year",
                            "10/12/2021",
                            "https://poster_path456.com"
        )

        val expected = 1

        // Mock return data with test data
        Mockito.`when`(repository.deleteMovieFromDb(movie)).thenReturn(expected)

        // Test method
        val actual: Int = viewModel.deleteMovieFromDB(movie)

        // Assert
        Truth.assertThat(actual).isEqualTo(expected)
    }


    @After
    fun tearDown() {
        //
    }
}