package com.rupesh.kotlinrxjavaex.domain.usecase.movie

import com.rupesh.kotlinrxjavaex.domain.repository.IMovieRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.exceptions.UndeliverableException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.movie.MovieTestData

@RunWith(JUnit4::class)
class SaveMovieUseCaseTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var iMovieRepository: IMovieRepository
    private lateinit var saveMovieUseCase: SaveMovieUseCase
    private lateinit var movieTestData: MovieTestData

    @Before
    fun setUp() {
        iMovieRepository = mock(IMovieRepository::class.java)
        saveMovieUseCase = SaveMovieUseCase(iMovieRepository)
        movieTestData = MovieTestData()
    }


    @Test
    fun `saveMovie when given Movie returns Inserted ItemRowId`() {
        val movieList = movieTestData.movieList
        `when`(iMovieRepository.saveMovie(movieList[0])).thenReturn(Maybe.just(1L))

        saveMovieUseCase.savedMovie(movieList[0]).test()
            .await()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun `saveMovie when given Movie throws exception on failure`() {
        val movieList = movieTestData.movieList

        `when`(iMovieRepository.saveMovie(movieList[0]))
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        saveMovieUseCase.savedMovie(movieList[0]).test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }

    @Test
    fun `deleteMovie when given movieId deletes the movie from db`() {
        val movieList = movieTestData.movieList
        `when`(iMovieRepository.deleteMovie(movieList[0].itemId)).thenReturn(Maybe.just(1))
        saveMovieUseCase.deleteMovie(movieList[0].itemId).test()
            .await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `deleteMovie when given id throws exception on failure`() {
        val movieId = movieTestData.movieList[0].itemId

        `when`(iMovieRepository.deleteMovie(movieId))
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        saveMovieUseCase.deleteMovie(movieId).test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }

    @Test
    fun `getSavedMovies returns all Movie items from db`() {
        val movieList = movieTestData.movieList
        `when`(iMovieRepository.getSavedMovies()).thenReturn(Observable.just(movieList))

        saveMovieUseCase.getSavedMovies().test()
            .assertValue {
                it[0].originalTitle == movieList[0].originalTitle
            }
            .assertValue {
                it[1].originalTitle == movieList[1].originalTitle
            }
    }

    @Test
    fun `getSavedMovie throws exception on failure`() {
        `when`(iMovieRepository.getSavedMovies())
            .thenReturn(Observable.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        saveMovieUseCase.getSavedMovies().test().await()
            .assertError(UndeliverableException::class.java)
            .assertValueCount(0)
            .assertNotComplete()
    }

    @Test
    fun `clear clears the Movie table`() {
        `when`(iMovieRepository.clear()).thenReturn(Maybe.just(100))

        saveMovieUseCase.clear().test()
            .await()
            .assertValue {
                it == 100
            }
    }

    @Test
    fun `clear throws exception on failure`() {
        `when`(iMovieRepository.clear())
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        saveMovieUseCase.clear().test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }
}