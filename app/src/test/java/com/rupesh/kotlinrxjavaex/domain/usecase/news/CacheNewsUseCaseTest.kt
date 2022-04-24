package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.google.common.truth.Truth.assertThat
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
import sharedTest.testdata.news.NewsTestData

@RunWith(JUnit4::class)
class CacheNewsUseCaseTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var iNewsRepository: INewsRepository
    lateinit var cacheNewsUseCase: CacheNewsUseCase
    var newsTestData = NewsTestData()

    @Before
    fun setUp() {
        iNewsRepository = mock(INewsRepository::class.java)
        cacheNewsUseCase = CacheNewsUseCase(iNewsRepository)
    }

    @Test
    fun `addCacheArticle when given NewsArticle returns inserted itemRowId`() {
        val newsArticles = newsTestData.newsArticles
        `when`(iNewsRepository.addCacheArticle(newsArticles[0])).thenReturn(Maybe.just(1L))

        cacheNewsUseCase.addCacheArticle(newsArticles[0]).test().await()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun `addCacheArticle when given NewsArticle throws exception on failure`() {
        val newsArticles = newsTestData.newsArticles
        `when`(iNewsRepository.addCacheArticle(newsArticles[0]))
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        cacheNewsUseCase.addCacheArticle(newsArticles[0]).test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }


    @Test
    fun `deleteCacheArticle when given id deletes the article from db`() {
        `when`(iNewsRepository.deleteCacheArticle(1)).thenReturn(Maybe.just(1))

        cacheNewsUseCase.deleteCacheArticle(1).test().await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `deleteCacheArticle when given id throws Exception on failure`() {
        `when`(iNewsRepository.deleteCacheArticle(1))
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        cacheNewsUseCase.deleteCacheArticle(1).test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }

    @Test
    fun `getCachedArticles returns all NewsArticles`() {
        val newsArticles = newsTestData.newsArticles
        `when`(iNewsRepository.getCachedArticles()).thenReturn(Observable.just(newsArticles))

        cacheNewsUseCase.getCachedArticles().test().await()
            .assertValue {
                it[0].author == newsArticles[0].author
            }
            .assertValue {
                it[1].author == newsArticles[1].author
            }
    }

    @Test
    fun `getCacheArticle when given NewsArticle throws Exception on failure`() {
        `when`(iNewsRepository.getCachedArticles())
            .thenReturn(Observable.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        cacheNewsUseCase.getCachedArticles().test().await()
            .assertError(UndeliverableException::class.java)
            .assertValueCount(0)
            .assertNotComplete()
    }


    @Test
    fun `clearCache clears the NewsArticle table`() {
        `when`(iNewsRepository.clearCache()).thenReturn(Maybe.just(100))

        cacheNewsUseCase.clearCache().test().await()
            .assertValue {
                it == 100
            }
    }

    @Test
    fun `clearCache throws Exception on Failure`() {
        `when`(iNewsRepository.clearCache())
            .thenReturn(Maybe.create { emitter ->
                emitter.onError(UndeliverableException(UnknownError()))
            })

        cacheNewsUseCase.clearCache().test().await()
            .assertError(UndeliverableException::class.java)
            .assertNotComplete()
    }
}