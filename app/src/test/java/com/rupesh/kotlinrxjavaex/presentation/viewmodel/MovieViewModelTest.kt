package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.DeleteSavedMovie
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.GetAllMovies
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.GetAllSavedMovies
import com.rupesh.kotlinrxjavaex.domain.usecase.movie.SaveMovieToDb
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import io.reactivex.Maybe
import io.reactivex.exceptions.UndeliverableException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.DbMovieTestData
import sharedTest.testdata.movie.MovieResponseTestData

@RunWith(MockitoJUnitRunner::class)
class MovieViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // for LiveData

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()      // for Retrofit

    private lateinit var getAllMovies: GetAllMovies
    private lateinit var getSavedMovies: GetAllSavedMovies
    private lateinit var saveMovieToDb: SaveMovieToDb
    private lateinit var deleteSavedMovies: DeleteSavedMovie
    private lateinit var movieViewModel: MovieViewModel

    @Before
    fun setUp() {
        getAllMovies = mock(GetAllMovies::class.java)
        getSavedMovies = mock(GetAllSavedMovies::class.java)
        saveMovieToDb = mock(SaveMovieToDb::class.java)
        deleteSavedMovies = mock(DeleteSavedMovie::class.java)
        movieViewModel = MovieViewModel(
            getAllMovies,
            getSavedMovies,
            saveMovieToDb,
            deleteSavedMovies
        )
    }

    @Test
    fun `getAllMovies sets NewsLiveData with theReturnedResult OnSuccess`() {
        val responseTestData = MovieResponseTestData()

        `when`(getAllMovies.execute()).thenReturn(responseTestData.getResponseDataSuccess())

        movieViewModel.getMovieList()

        movieViewModel.movieLiveDataResult.value?.let {
            assertThat(it.data).isNotNull()
            assertThat(it).isInstanceOf(Resource.Success::class.java)
            if(it is Resource.Success) {
                it.data.let { list ->
                    assertThat(list).isNotEmpty()
                    if (list != null) {
                        assertThat(list.size).isEqualTo(2)
                    }
                }
            }
        }
    }

    @Test
    fun `getAllMovies sets NewsLiveData with errorMessage OnFailure`() {
        val responseTestData = MovieResponseTestData()

        `when`(getAllMovies.execute()).thenReturn(responseTestData.getResponseDataError())

        movieViewModel.getMovieList()

        movieViewModel.movieLiveDataResult.value?.let {
            assertThat(it.data).isNull()
            assertThat(it).isInstanceOf(Resource.Error::class.java)
            assertThat(it.message).isEqualTo("Cannot fetch the movies")
        }
    }


    @Test
    fun `getSavedMoviesFromDb given the savedDataInDb returns the savedDataFromDb`() {
        val dbTestData = DbMovieTestData()

        `when`(getSavedMovies.execute()).thenReturn(dbTestData.getMovieListTestDataObservable())

        movieViewModel.getAllMovieFromDb()

        movieViewModel.dbMovieListResult.value.let {
            assertThat(it).isNotEmpty()

            it?.let {
                assertThat(it.size).isEqualTo(2)
                assertThat(it[0].title).isEqualTo("Movie-1")
                assertThat(it[1].title).isEqualTo("Movie-2")
            }
        }
    }

    @Test
    fun `saveMovie given the dataToBeSaved sets successStatusMessage OnSuccess`() {
        val movieToBeSaved = DbMovieTestData().getMovieListTestData()[0]
        val expected = Maybe.create<Long> { emitter -> emitter.onSuccess(1L)  }

        `when`(saveMovieToDb.execute(movieToBeSaved)).thenReturn(expected)
        movieViewModel.addMovieToDB(movieToBeSaved)

        movieViewModel.statusMessageResult.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Movie Saved")
            }
        }
    }


    @Test
    fun `saveMovie given the dataToBeSaved sets failureStatusMessage OnFailure`() {
        val movieToBeSaved = DbMovieTestData().getMovieListTestData()[0]
        val expected = Maybe.create<Long> { emitter ->
            emitter.onError(UndeliverableException(UnknownError()))
        }

        `when`(saveMovieToDb.execute(movieToBeSaved)).thenReturn(expected)
        movieViewModel.addMovieToDB(movieToBeSaved)

        movieViewModel.statusMessageResult.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not save the movie")
            }
        }
    }


    @Test
    fun `deleteMovie given the dataToBeDeleted sets successStatusMessage OnSuccess`() {
        val expected = Maybe.create<Int> { emitter -> emitter.onSuccess(1)  }

        `when`(deleteSavedMovies.execute(1)).thenReturn(expected)

        movieViewModel.deleteMovieFromDB(1)

        movieViewModel.statusMessageResult.value?.let {
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

        `when`(deleteSavedMovies.execute(1)).thenReturn(expected)
        movieViewModel.deleteMovieFromDB(1)

        movieViewModel.statusMessageResult.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not delete the movie")
            }
        }
    }
}