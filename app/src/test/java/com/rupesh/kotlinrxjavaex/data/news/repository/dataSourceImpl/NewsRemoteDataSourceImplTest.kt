package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sharedTest.ApiResponseTestHelper

class NewsRemoteDataSourceImplTest {

    private lateinit var service: NewsService
    private lateinit var server: MockWebServer
    private lateinit var newsRemoteDataSourceImpl: NewsRemoteDataSourceImpl
    private lateinit var testObserver: TestObserver<Response<NewsResponse>>

    @Before
    fun setUp() {
        server = MockWebServer()

        service = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NewsService::class.java)

        newsRemoteDataSourceImpl = NewsRemoteDataSourceImpl(service)
        testObserver = TestObserver<Response<NewsResponse>>()
    }

    @Test
    fun `getTopNewsHeadlines given countryAndPage returns NewsResponse`() {
        val expectedListSize = 20
        ApiResponseTestHelper.enqueueMockResponse(server, "news-response.json")

        newsRemoteDataSourceImpl
            .getTopNewsHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS)
            .subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.body() is NewsResponse
            }
            .assertValue {
                return@assertValue it.body()!!.articles.size == expectedListSize
            }
            .assertComplete()
            .assertNoErrors()
    }

    @Test
    fun getSearchedNewsHeadlines() {
        val searchKey = "cnn"
        val expectedListSize = 5

        ApiResponseTestHelper.enqueueMockResponse(server, "news-search-response-cnn.json")

        newsRemoteDataSourceImpl
            .getSearchedNewsHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS)
            .subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.body() is NewsResponse
            }
            .assertValue {
                return@assertValue it.body()!!.articles.size == expectedListSize
            }
            .assertComplete()
            .assertNoErrors()
    }


    @After
    fun tearDown() {
        testObserver.dispose()
        server.shutdown()
    }
}