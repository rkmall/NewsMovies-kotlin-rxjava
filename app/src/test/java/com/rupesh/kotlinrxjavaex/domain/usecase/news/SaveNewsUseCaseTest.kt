package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.exceptions.UndeliverableException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.news.NewsSavedTestData

@RunWith(JUnit4::class)
class SaveNewsUseCaseTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var iNewsRepository: INewsRepository
    private lateinit var saveNewsUseCase: SaveNewsUseCase
    private var newsSavedTestData = NewsSavedTestData()

    @Before
    fun setUp() {
        iNewsRepository = mock(INewsRepository::class.java)
        saveNewsUseCase = SaveNewsUseCase(iNewsRepository)
    }

    @Test
    fun `saveArticle when given NewsArticle returns inserted itemRowId`() {
        val savedArticles = newsSavedTestData.savedNewsArticles
        `when`(iNewsRepository.saveArticle(savedArticles[0])).thenReturn(Maybe.just(1L))

        saveNewsUseCase.saveArticle(savedArticles[0]).test()
            .await()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun `saveArticle when given NewsArticle throws exception on failure`() {
        val newsArticles = newsSavedTestData.savedNewsArticles
        `when`(iNewsRepository.saveArticle(newsArticles[0]))
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        saveNewsUseCase.saveArticle(newsArticles[0]).test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }


    @Test
    fun `deleteSavedArticle when given id deletes the article from db`() {
        `when`(iNewsRepository.deleteSavedArticle(1)).thenReturn(Maybe.just(1))

        saveNewsUseCase.deleteSavedArticle(1).test()
            .await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `deleteSavedArticle when given NewsArticle throws exception on failure`() {
        `when`(iNewsRepository.deleteSavedArticle(1))
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        saveNewsUseCase.deleteSavedArticle(1).test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }

    @Test
    fun `getSavedArticles returns all NewsArticles`() {
        val savedArticles = newsSavedTestData.savedNewsArticles

        `when`(iNewsRepository.getSavedArticles()).thenReturn(Observable.just(savedArticles))

        saveNewsUseCase.getSavedArticles().test()
            .assertValue {
                it[0].author == savedArticles[0].author
            }
            .assertValue {
                it[1].author == savedArticles[1].author
            }
    }

    @Test
    fun `getSavedArticle throws exception on failure`() {
        `when`(iNewsRepository.getSavedArticles())
            .thenReturn(Observable.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        saveNewsUseCase.getSavedArticles().test().await()
            .assertError(UndeliverableException::class.java)
            .assertValueCount(0)
            .assertNotComplete()
    }

    @Test
    fun `clearSaved clears the NewsArticle table`() {
        `when`(iNewsRepository.clearSaved()).thenReturn(Maybe.just(100))

        saveNewsUseCase.clearSaved().test()
            .await()
            .assertValue {
                it == 100
            }
    }

    @Test
    fun `clearSaved throws exception on failure`() {
        `when`(iNewsRepository.clearSaved())
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        saveNewsUseCase.clearSaved().test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }
}