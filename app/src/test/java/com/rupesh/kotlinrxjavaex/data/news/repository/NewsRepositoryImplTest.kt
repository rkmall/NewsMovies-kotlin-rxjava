package com.rupesh.kotlinrxjavaex.data.news.repository

import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsLocalDataSource
import com.rupesh.kotlinrxjavaex.data.news.repository.dataSource.INewsRemoteDataSource
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import io.reactivex.Maybe
import io.reactivex.Observable
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.news.NewsSavedTestData
import sharedTest.testdata.news.NewsTestData

@RunWith(JUnit4::class)
class NewsRepositoryImplTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var iNewsRemoteDataSource: INewsRemoteDataSource
    lateinit var iNewsLocalDataSource: INewsLocalDataSource
    lateinit var newsRepositoryImpl: NewsRepositoryImpl
    var newsTestData = NewsTestData()
    var newsSavedTestData = NewsSavedTestData()

    @Before
    fun setUp() {
        iNewsRemoteDataSource = mock(INewsRemoteDataSource::class.java)
        iNewsLocalDataSource = mock(INewsLocalDataSource::class.java)
        newsRepositoryImpl = NewsRepositoryImpl(iNewsLocalDataSource, iNewsRemoteDataSource)
    }

    @Test
    fun `getTopNewsHeadlines given countryAndPage returns NewsResponse on success`() {
        `when`(
            iNewsRemoteDataSource.getTopHeadlines(
                AppConstantsData.DEFAULT_COUNTRY_NEWS,
                AppConstantsData.DEFAULT_PAGE_NEWS
            )
        ).thenReturn(newsTestData.createSuccessNewsResponseObservable())

        val testObserver = newsRepositoryImpl.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).test()

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
        `when`(
            iNewsRemoteDataSource.getTopHeadlines(
                AppConstantsData.DEFAULT_COUNTRY_NEWS,
                AppConstantsData.DEFAULT_PAGE_NEWS
            )
        ).thenReturn(newsTestData.createErrorNewsResponseObservable())

        val testObserver = newsRepositoryImpl.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).test()

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

        `when`(
            iNewsRemoteDataSource.getSearchedHeadlines(
                AppConstantsData.DEFAULT_COUNTRY_NEWS,
                searchKey,
                AppConstantsData.DEFAULT_PAGE_NEWS
            )
        ).thenReturn(newsTestData.createSuccessNewsResponseObservable())

        val testObserver = newsRepositoryImpl.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).test()

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

        `when`(
            iNewsRemoteDataSource.getSearchedHeadlines(
                AppConstantsData.DEFAULT_COUNTRY_NEWS,
                searchKey,
                AppConstantsData.DEFAULT_PAGE_NEWS
            )
        ).thenReturn(newsTestData.createErrorNewsResponseObservable())

        val testObserver = newsRepositoryImpl.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).test()

        testObserver.await()
            .assertValue {
                it.code() == 400
            }
            .assertValue {
                it.message() == "bad-request"
            }
    }

    @Test
    fun `addCacheArticle when given NewsArticle returns inserted itemRowId`() {
        val newsArticles = newsTestData.newsArticles
        `when`(iNewsLocalDataSource.addCacheArticle(newsArticles[0])).thenReturn(Maybe.just(1L))

        newsRepositoryImpl.addCacheArticle(newsArticles[0]).test()
            .await()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun `deleteCacheArticle when given id deletes the article from db`() {
        `when`(iNewsLocalDataSource.deleteCacheArticle(1)).thenReturn(Maybe.just(1))

        newsRepositoryImpl.deleteCacheArticle(1).test()
            .await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `getCachedArticles returns all NewsArticles`() {
        val newsArticles = newsTestData.newsArticles
        `when`(iNewsLocalDataSource.getCachedArticles()).thenReturn(Observable.just(newsArticles))

        newsRepositoryImpl.getCachedArticles().test()
            .assertValue {
                it[0].author == newsArticles[0].author
            }
            .assertValue {
                it[1].author == newsArticles[1].author
            }
    }

    @Test
    fun `clear clears the NewsArticle table`() {
        `when`(iNewsLocalDataSource.clearCache()).thenReturn(Maybe.just(100))

        newsRepositoryImpl.clearCache().test()
            .await()
            .assertValue {
                it == 100
            }
    }

    @Test
    fun `saveArticle when given NewsSaved inserts the item into db`() {
        val newsSavedList = newsSavedTestData.savedNewsArticles
        `when`(iNewsLocalDataSource.saveArticle(newsSavedList[0])).thenReturn(Maybe.just(1L))

        newsRepositoryImpl.saveArticle(newsSavedList[0]).test()
            .await()
            .assertValue {
                it == 1L
            }
    }


    @Test
    fun `getSavedArticle returns all NewsSaved items from db`() {
        val newsSavedList = newsSavedTestData.savedNewsArticles
        `when`(iNewsLocalDataSource.getSavedArticles()).thenReturn(Observable.just(newsSavedList))

        newsRepositoryImpl.getSavedArticles().test()
            .assertValue {
                it[0].author == newsSavedList[0].author
            }
            .assertValue {
                it[1].author == newsSavedList[1].author
            }
    }

    @Test
    fun `deleteSavedArticle when given id deletes the items from db`() {
        `when`(iNewsLocalDataSource.deleteSavedArticle(1)).thenReturn(Maybe.just(1))

        newsRepositoryImpl.deleteSavedArticle(1).test()
            .await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `clearSaved clears NewsSaved table`() {
        `when`(iNewsLocalDataSource.clearSaved()).thenReturn(Maybe.just(100))

        newsRepositoryImpl.clearSaved().test()
            .await()
            .assertValue {
                it == 100
            }
    }
}