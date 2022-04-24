package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
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
class NetworkNewsUseCaseTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var iNewsRepository: INewsRepository
    lateinit var networkNewsUseCase: NetworkNewsUseCase
    var newsTestData = NewsTestData()

    @Before
    fun setUp() {
        iNewsRepository = mock(INewsRepository::class.java)
        networkNewsUseCase = NetworkNewsUseCase(iNewsRepository)
    }

    @Test
    fun `getTopHeadlines  returns a Resource Success with movie list on Response Success`() {
        `when`(iNewsRepository.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        )).thenReturn(newsTestData.createSuccessNewsResponseObservable())

        val testObserver = networkNewsUseCase.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ) .test()

        testObserver.await()
            .assertValue {
                it.data is List<NewsArticle>
            }
            .assertValue {
                it.data?.size == 2
            }
            .assertValue {
                it.data?.get(0)?.author == "author name 1"
            }
            .assertValue {
                it.data?.get(1)?.author == "author name 2"
            }
            .assertNoErrors()
            .assertComplete()
    }


    @Test
    fun `getTopHeadlines return Resource Error with error message on Response Error`() {
        val errorResponse = newsTestData.createErrorNewsResponseObservable()
        val errorResponseCode = errorResponse.blockingGet().code()

        `when`(iNewsRepository.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        )).thenReturn(errorResponse)

        val testObserver = networkNewsUseCase.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).test()

        testObserver.await()
            .assertValue {
                it.data == null
            }
            .assertValue {
                it.message == "Error: $errorResponseCode: Cannot fetch news articles"
            }
    }

    @Test
    fun `getSearchedHeadlines  returns a Resource Success with movie list on Response Success`() {
        val searchKey = "cnn"
        `when`(iNewsRepository.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        )).thenReturn(newsTestData.createSuccessNewsResponseObservable())

        val testObserver = networkNewsUseCase.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ) .test()

        testObserver.await()
            .assertValue {
                it.data is List<NewsArticle>
            }
            .assertValue {
                it.data?.size == 2
            }
            .assertValue {
                it.data?.get(0)?.author == "author name 1"
            }
            .assertValue {
                it.data?.get(1)?.author == "author name 2"
            }
            .assertNoErrors()
            .assertComplete()
    }


    @Test
    fun `getSearchedHeadlines return Resource Error with error message on Response Error`() {
        val searchKey = "cnn"
        val errorResponse = newsTestData.createErrorNewsResponseObservable()
        val errorResponseCode = errorResponse.blockingGet().code()

        `when`(iNewsRepository.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        )).thenReturn(errorResponse)

        val testObserver = networkNewsUseCase.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).test()

        testObserver.await()
            .assertValue {
                it.data == null
            }
            .assertValue {
                it.message == "Error: $errorResponseCode: Cannot fetch searched news articles"
            }
    }
}