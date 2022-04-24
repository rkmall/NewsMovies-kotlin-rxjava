package com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.NetworkMovieUseCase
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.SaveMovieUseCase
import com.rupesh.kotlinrxjavaex.domain.usecase.news.CacheNewsUseCase
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.UndeliverableException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.MovieTestData

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // for LiveData

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()      // for Retrofit

    private lateinit var networkMovieUseCase: NetworkMovieUseCase
    private lateinit var saveMovieUseCase: SaveMovieUseCase
    private lateinit var movieViewModel: MovieViewModel
    var movieTestData = MovieTestData()

    @Before
    fun setUp() {
        networkMovieUseCase = mock(NetworkMovieUseCase::class.java)
        saveMovieUseCase = mock(SaveMovieUseCase::class.java)
        movieViewModel = MovieViewModel(
            networkMovieUseCase,
            saveMovieUseCase
        )
    }

    @Test
    fun `getPopularMovies sets MovieLiveData with theReturnedResult OnSuccess`() {
        `when`(networkMovieUseCase.getPopularMovies())
            .thenReturn(Single.just(Resource.Success(movieTestData.movieList)))

        movieViewModel.getPopularMovies()

        movieViewModel.popularMovies.value?.let {
            assertThat(it.data).isNotNull()
            assertThat(it).isInstanceOf(Resource.Success::class.java)

            it.data.let { list ->
                assertThat(list).isNotEmpty()
                if (list != null) {
                    assertThat(list.size).isEqualTo(2)
                }
            }
        }
    }

    @Test
    fun `getPopularMovies sets MovieLiveData with error message on failure`() {
        `when`(networkMovieUseCase.getPopularMovies())
            .thenReturn(Single.just(Resource.Error(message = "Error: 400: Cannot fetch movies")))

        movieViewModel.getPopularMovies()

        movieViewModel.popularMovies.value?.let {
            assertThat(it.data).isNull()
            assertThat(it).isInstanceOf(Resource.Error::class.java)
            assertThat(it.message).isEqualTo("Error: 400: Cannot fetch movies")
        }
    }


    @Test
    fun `getSavedMovies sets SavedMovieLiveData returned from the app db`() {
        `when`(saveMovieUseCase.getSavedMovies())
            .thenReturn(Observable.just(movieTestData.movieList))

        movieViewModel.getSavedMovies()

        movieViewModel.savedMovies.value.let {
            assertThat(it).isNotEmpty()

            it?.let {
                assertThat(it.size).isEqualTo(2)
                assertThat(it[0].originalTitle).isEqualTo("First Movie")
                assertThat(it[1].originalTitle).isEqualTo("Second Movie")
            }
        }
    }

    @Test
    fun `saveMovie given the movie to be saved sets event message OnSuccess`() {
        val movieToBeSaved = movieTestData.movieList[0]

        val expected = Maybe.create<Long> { emitter -> emitter.onSuccess(1L) }

        `when`(saveMovieUseCase.savedMovie(movieToBeSaved)).thenReturn(expected)
        movieViewModel.saveMovie(movieToBeSaved)

        movieViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Movie Saved")
            }
        }
    }

    @Test
    fun `saveMovie given the dataToBeSaved sets failureStatusMessage OnFailure`() {
        val movieToBeSaved = movieTestData.movieList[0]

        val expected = Maybe.create<Long> { emitter ->
            emitter.onError(UndeliverableException(UnknownError()))
        }

        `when`(saveMovieUseCase.savedMovie(movieToBeSaved)).thenReturn(expected)
        movieViewModel.saveMovie(movieToBeSaved)

        movieViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not save the movie")
            }
        }
    }

    @Test
    fun `deleteMovie given the dataToBeDeleted sets successStatusMessage OnSuccess`() {
        val expected = Maybe.create<Int> { emitter -> emitter.onSuccess(1)  }

        `when`(saveMovieUseCase.deleteMovie(1)).thenReturn(expected)
        movieViewModel.deleteMovie(1)

        movieViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Movie Deleted")
            }
        }
    }

    @Test
    fun `deleteMovie given the dataToBeSaved sets failureStatusMessage OnFailure`() {
        val expected = Maybe.create<Int> { emitter ->
            emitter.onError(UndeliverableException(UnknownError()))
        }

        `when`(saveMovieUseCase.deleteMovie(1)).thenReturn(expected)
        movieViewModel.deleteMovie(1)

        movieViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not delete the Movie")
            }
        }
    }
}
