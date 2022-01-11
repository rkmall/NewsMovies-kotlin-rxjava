package com.rupesh.kotlinrxjavaex.domain.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth
import com.rupesh.kotlinrxjavaex.data.movie.model.Movie
import com.rupesh.kotlinrxjavaex.data.movie.service.MovieDataService

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.MovieDBResponseTestData
import org.mockito.Mockito.mock
import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.BuildConfig
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MovieRepositoryTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    // To execute rx tasks on the current thread in the FIFO manner (synchronously)
    @get:Rule
    var rxRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var repository: MovieRepository
    lateinit var service: MovieDataService
    lateinit var testData: MovieDBResponseTestData


    @Before
    fun setUp() {
        service = mock(MovieDataService::class.java)
        repository = MovieRepository(service)
        testData = MovieDBResponseTestData()
    }

    @Test
    fun getMovieList_providedServiceAndKey_returnsMovieLiveData_size() {
        // Set expected data
        val expected: Int = 2

        // Mock return data with the TestData
        Mockito.`when`(service.getAllMoviesWithRx(BuildConfig.API_KEY))
            .thenReturn(testData.getTestObservable())

        // Test method
        val actual: MutableLiveData<List<Movie>> = repository.getMovieLiveData()

        // Assert
        Truth.assertThat(actual.value!!.size).isEqualTo(expected)
    }

    @Test
    fun getMovieList_providedServiceAndKey_returnsMovieLiveData_title() {
        // Set expected data
        val expected = "Movie2021"

        // Mock return data as test data
        Mockito.`when`(service.getAllMoviesWithRx(BuildConfig.API_KEY))
            .thenReturn(testData.getTestObservable())

        // Test method
        val actual = repository.getMovieLiveData()

        // Assert
        assertThat(actual.value!![1].original_title).isEqualTo(expected)
    }

    @After
    fun tearDown() {
        //
    }
}