/*
package com.rupesh.kotlinrxjavaex.data.news.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl.NewsLocalDataSourceImpl
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl.NewsRemoteDataSourceImpl
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sharedTest.ApiResponseTestHelper
import sharedTest.DatabaseTestHelper
import sharedTest.testdata.news.NewsDbTestData

class NewsRepositoryImplTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var service: NewsService
    private lateinit var server: MockWebServer
    private lateinit var newsRemoteDataSourceImpl: NewsRemoteDataSourceImpl
    private lateinit var newsRepositoryImpl: NewsRepositoryImpl
    private lateinit var testObserver: TestObserver<Response<NewsResponse>>

    private lateinit var newsDb: NewsDB
    private lateinit var newsLocalDataSourceImpl: NewsLocalDataSourceImpl
    private lateinit var newsDbTestData: NewsDbTestData

    @Before
    fun setUpFor() {
        // Setup for RemoteDataSource
        server = MockWebServer()
        service = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NewsService::class.java)

        newsRemoteDataSourceImpl = NewsRemoteDataSourceImpl(service)
        testObserver = TestObserver<Response<NewsResponse>>()

        // Setup for LocalDataSource
        val context: Context = ApplicationProvider.getApplicationContext()
        newsDb = DatabaseTestHelper.initializeDb(context, NewsDB::class.java)
        newsDbTestData = NewsDbTestData()
        newsLocalDataSourceImpl = NewsLocalDataSourceImpl(newsDb.getNewsDao())

        newsRepositoryImpl = NewsRepositoryImpl(newsLocalDataSourceImpl, newsRemoteDataSourceImpl)
    }


    @Test
    fun getTopHeadlines() {
        val expectedListSize = 20
        ApiResponseTestHelper.enqueueMockResponse(server, "news-response.json")

        newsRepositoryImpl.getTopHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS)
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
    fun getSearchedHeadlines() {
        val searchKey = "cnn"
        val expectedListSize = 5

        ApiResponseTestHelper.enqueueMockResponse(server, "news-search-response-cnn.json")

        newsRepositoryImpl.getSearchedHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS)
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
    fun getSavedNewsArticles() {
        val newsArticleList = newsDbTestData.getNewsDbTestData()

        newsRepositoryImpl.addCacheArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }

        newsRepositoryImpl.addCacheArticle(newsArticleList[1]).test()
            .assertValue {
                it == 2L
            }

        newsRepositoryImpl.getCachedArticles().test()
            .assertValue {
                it.size == 2
            }
            .assertValue {
                it[0].title.equals("title-1")
            }
            .assertValue {
                it[0].author.equals("author name 1")
            }
            .assertValue {
                it[1].title.equals("title-2")
            }
            .assertValue {
                it[1].author.equals("author name 2")
            }
    }

    @Test
    fun saveNewsArticle() {
        val newsArticleList = newsDbTestData.getNewsDbTestData()
        newsRepositoryImpl.addCacheArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun removeNewsArticle() {
        val newsArticleList = newsDbTestData.getNewsDbTestData()

        newsRepositoryImpl.addCacheArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }

        newsRepositoryImpl.addCacheArticle(newsArticleList[1]).test()
            .assertValue {
                it == 2L
            }

        newsRepositoryImpl.deleteCacheArticle(2).test()
            .assertValue {
                it == 1
            }

        newsRepositoryImpl.getCachedArticles().test()
            .assertValue {
                it.size == 1
            }
    }

    @After
    fun tearDown() {
        testObserver.dispose()
        server.shutdown()
        newsDb.close()
    }
}*/
