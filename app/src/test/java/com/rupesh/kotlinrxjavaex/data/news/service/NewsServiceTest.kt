package com.rupesh.kotlinrxjavaex.data.news.service

import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sharedTest.ApiResponseTestHelper


@RunWith(JUnit4::class)
class NewsServiceTest {

    private lateinit var service: NewsService
    private lateinit var server: MockWebServer
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

        testObserver = TestObserver<Response<NewsResponse>>()
    }

    @Test
    fun getTopHeadlines_requestPath () {
        ApiResponseTestHelper.enqueueMockResponse(server, "news-response.json")
        service.getTopHeadlines("us", 1).subscribe(testObserver)
        val request = server.takeRequest()
        assertThat(request.path).isEqualTo("/v2/top-headlines?country=us&page=1&apiKey=${BuildConfig.API_NEWS}")
    }

    @Test
    fun getTopHeadlines_requestHeaderAndResponseCode () {
        ApiResponseTestHelper.enqueueMockResponse(server, "news-response.json")
        service.getTopHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS).subscribe(testObserver)
        testObserver.await()
            .assertValue {
                return@assertValue it.headers()["content-type"]!! == "application/json"
            }
            .assertValue {
                return@assertValue it.code() == 200
            }
    }

    @Test
    fun getTopHeadlines_givenCountryAndPage_statusAndTotalResults () {
        ApiResponseTestHelper.enqueueMockResponse(server, "news-response.json")
        service.getTopHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.body()?.status == "ok"
            }
            .assertValue {
                return@assertValue it.body()?.totalResults == 20
            }
    }

    @Test
    fun getSearchedHeadlines_requestPath () {
        ApiResponseTestHelper.enqueueMockResponse(server, "news-response.json")
        service.getSearchedTopHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, "cnn", AppConstantsData.DEFAULT_PAGE_NEWS).subscribe(testObserver)
        val request = server.takeRequest()
        assertThat(request.path).isEqualTo("/v2/top-headlines?country=us&q=cnn&page=1&apiKey=${BuildConfig.API_NEWS}")
    }

    @Test
    fun getSearchedHeadlines_requestPath2 () {
        ApiResponseTestHelper.enqueueMockResponse(server, "news-search-response-cnn.json")
        val searchKey = "cnn"
        service.getSearchedTopHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS).subscribe(testObserver)
        testObserver.await()
            .assertValue {
                var result = 0
                var containsSearchKey: Boolean
                for(article in it.body()!!.articles) {
                    containsSearchKey = when {
                        article.source!!.id.lowercase().contains(searchKey) -> true
                        article.source!!.name.lowercase().contains(searchKey) -> true
                        article.url!!.lowercase().contains(searchKey) -> true
                        article.description!!.lowercase().contains(searchKey) -> true
                        else -> false
                    }
                    if (containsSearchKey) result++
                }
                return@assertValue result == 5
            }
    }

    @After
    fun tearDown() {
        testObserver.dispose()
        server.shutdown()
    }
}