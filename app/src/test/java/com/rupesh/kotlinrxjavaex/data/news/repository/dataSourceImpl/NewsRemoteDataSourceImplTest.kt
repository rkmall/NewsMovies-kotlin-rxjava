package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import io.reactivex.observers.TestObserver
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.news.NewsTestData

@RunWith(JUnit4::class)
class NewsRemoteDataSourceImplTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var service: NewsService
    private lateinit var newsRemoteDataSourceImpl: NewsRemoteDataSourceImpl
    private val responseTestData = NewsTestData()
    private lateinit var testObserver: TestObserver<Response<NewsResponse>>

    @Before
    fun setUp() {
        service = mock(NewsService::class.java)
        newsRemoteDataSourceImpl = NewsRemoteDataSourceImpl(service)
        testObserver = TestObserver<Response<NewsResponse>>()
    }

    @Test
    fun `getTopNewsHeadlines given countryAndPage returns NewsResponse on success`() {
        `when`(service.getTopHeadlines(
                country = AppConstantsData.DEFAULT_COUNTRY_NEWS,
                page = AppConstantsData.DEFAULT_PAGE_NEWS
            )
        ).thenReturn(responseTestData.createSuccessNewsResponseObservable())

        newsRemoteDataSourceImpl.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).subscribe(testObserver)

        val expectedListSize = 2

        testObserver
            .assertValue {
                return@assertValue it.body() is NewsResponse
            }
            .assertValue {
                return@assertValue it.body()!!.articles.size == expectedListSize
            }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `getTopHeadlines returns ErrorResponse on error`() {
        `when`(service.getTopHeadlines(
                country = AppConstantsData.DEFAULT_COUNTRY_NEWS,
                page = AppConstantsData.DEFAULT_PAGE_NEWS
            )
        ).thenReturn(responseTestData.createErrorNewsResponseObservable())

        newsRemoteDataSourceImpl.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                it.code() == 400
            }
            .assertValue {
                it.message() == "bad-request"
            }
    }

    @Test
    fun `getSearchedHeadlines given countryAndPage returns NewsResponse on success`() {
        val searchKey = "cnn"

        `when`(service.getSearchedHeadlines(
                country = AppConstantsData.DEFAULT_COUNTRY_NEWS,
                searchQuery = searchKey,
                page = AppConstantsData.DEFAULT_PAGE_NEWS
            )
        ).thenReturn(responseTestData.createSuccessNewsResponseObservable())

        newsRemoteDataSourceImpl.getSearchedHeadlines(
                AppConstantsData.DEFAULT_COUNTRY_NEWS,
                searchKey,
                AppConstantsData.DEFAULT_PAGE_NEWS
            ).subscribe(testObserver)

        val expectedListSize = 2

        testObserver.await()
            .assertValue {
                return@assertValue it.body() is NewsResponse
            }
            .assertValue {
                return@assertValue it.body()!!.articles.size == expectedListSize
            }
            .assertNoErrors()
            .assertComplete()
    }

    @Test
    fun `getSearchedHeadlines returns ErrorResponse on error`() {
        val searchKey = "cnn"

        `when`(service.getSearchedHeadlines(
                country = AppConstantsData.DEFAULT_COUNTRY_NEWS,
                searchQuery = searchKey,
                page = AppConstantsData.DEFAULT_PAGE_NEWS
            )
        ).thenReturn(responseTestData.createErrorNewsResponseObservable())

        newsRemoteDataSourceImpl.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                it.code() == 400
            }
            .assertValue {
                it.message() == "bad-request"
            }
    }

    @After
    fun tearDown() {
        testObserver.dispose()
    }
}
