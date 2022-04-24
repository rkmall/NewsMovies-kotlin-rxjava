package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import com.rupesh.kotlinrxjavaex.data.news.db.NewsDao
import com.rupesh.kotlinrxjavaex.data.news.db.NewsSavedDao
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
class NewsLocalDataSourceImplTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    lateinit var newsDao: NewsDao
    var newsTestData = NewsTestData()

    lateinit var newsSavedDao: NewsSavedDao
    var newsSavedTestData = NewsSavedTestData()

    lateinit var newsLocalDataSourceImpl: NewsLocalDataSourceImpl

    @Before
    fun setUp() {
        newsDao = mock(NewsDao::class.java)
        newsSavedDao = mock(NewsSavedDao::class.java)
        newsLocalDataSourceImpl = NewsLocalDataSourceImpl(newsDao, newsSavedDao)
    }


    @Test
    fun `addCacheArticle when given NewsArticle returns inserted itemRowId`() {
        val newsArticles = newsTestData.newsArticles
        `when`(newsDao.addCacheArticle(newsArticles[0])).thenReturn(Maybe.just(1L))

        newsLocalDataSourceImpl.addCacheArticle(newsArticles[0]).test()
            .await()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun `deleteCacheArticle when given id deletes the article from db`() {
        `when`(newsDao.deleteCacheArticle(1)).thenReturn(Maybe.just(1))

        newsLocalDataSourceImpl.deleteCacheArticle(1).test()
            .await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `getCachedArticles returns all NewsArticles`() {
        val newsArticles = newsTestData.newsArticles
        `when`(newsDao.getCachedArticles()).thenReturn(Observable.just(newsArticles))

        newsLocalDataSourceImpl.getCachedArticles().test()
            .assertValue {
                it[0].author == newsArticles[0].author
            }
            .assertValue {
                it[1].author == newsArticles[1].author
            }
    }

    @Test
    fun `clear clears the NewsArticle table`() {
        `when`(newsDao.clearCache()).thenReturn(Maybe.just(100))

        newsLocalDataSourceImpl.clearCache().test()
            .await()
            .assertValue {
                it == 100
            }
    }

    @Test
    fun `saveArticle when given NewsSaved inserts the item into db`() {
        val newsSavedList = newsSavedTestData.savedNewsArticles
        `when`(newsSavedDao.saveArticle(newsSavedList[0])).thenReturn(Maybe.just(1L))

        newsLocalDataSourceImpl.saveArticle(newsSavedList[0]).test()
            .await()
            .assertValue {
                it == 1L
            }
    }


    @Test
    fun `getSavedArticle returns all NewsSaved items from db`() {
        val newsSavedList = newsSavedTestData.savedNewsArticles
        `when`(newsSavedDao.getSavedArticles()).thenReturn(Observable.just(newsSavedList))

        newsLocalDataSourceImpl.getSavedArticles().test()
            .assertValue {
                it[0].author == newsSavedList[0].author
            }
            .assertValue {
                it[1].author == newsSavedList[1].author
            }
    }

    @Test
    fun `deleteSavedArticle when given id deletes the items from db`() {
        `when`(newsSavedDao.deleteSavedArticle(1)).thenReturn(Maybe.just(1))
        newsLocalDataSourceImpl.deleteSavedArticle(1).test()
            .await()
            .assertValue {
                it == 1
            }
    }

    @Test
    fun `clearSaved clears NewsSaved table`() {
        `when`(newsSavedDao.clearSaved()).thenReturn(Maybe.just(100))

        newsLocalDataSourceImpl.clearSaved().test()
            .await()
            .assertValue {
                it == 100
            }
    }
}