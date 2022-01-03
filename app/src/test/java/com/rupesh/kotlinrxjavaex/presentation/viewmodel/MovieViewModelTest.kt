package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.data.model.Movie
import com.rupesh.kotlinrxjavaex.domain.repository.MovieRepository
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.MovieTestData

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    // To execute rx tasks on the current thread in the FIFO manner (synchronously)
    @get:Rule
    var rxRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var movieViewModel: MovieViewModel
    lateinit var movieRepository: MovieRepository
    lateinit var movieTestData: MovieTestData
    var movieMutableLiveData: MutableLiveData<List<Movie>> = MutableLiveData()

    /**
     * MovieRepository is mocked using Mockito
     * Instantiate MovieViewModel providing mocked repository
     * Test Data is initialized
     */
    @Before
    fun setUp() {
        movieRepository = mock(MovieRepository::class.java)
        movieViewModel = MovieViewModel(movieRepository)
        movieTestData = MovieTestData()
    }

    @Test
    fun getMovieList_givenMovieRepository_returnsMovieLiveData() {
        // Set test data
        movieMutableLiveData.value = movieTestData.getTestMovieList()

        // Mock return data with the test data
        Mockito.`when`(movieRepository.getMovieLiveData()).thenReturn(movieMutableLiveData)

        // Test method
        movieViewModel.getMovieList()
        val expected: LiveData<List<Movie>> = movieMutableLiveData
        val actual: LiveData<List<Movie>> = movieViewModel.movieLiveData
        val expectedMovie = expected.value?.get(0)
        val actualMovie = actual.value?.get(0)

        // Assert
        assertThat(actualMovie?.original_title).isEqualTo(expectedMovie?.original_title)
    }

    @After
    fun tearDown() {
       //
    }
}