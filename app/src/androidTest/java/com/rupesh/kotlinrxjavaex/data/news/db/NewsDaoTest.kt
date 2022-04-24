package com.rupesh.kotlinrxjavaex.data.news.db

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.rupesh.kotlinrxjavaex.data.common.db.AppDB
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import sharedTest.DatabaseTestHelper
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.news.NewsTestData

@RunWith(JUnit4::class)
class NewsDaoTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()


    lateinit var appDB: AppDB
    lateinit var newsDao: NewsDao
    var newsTestData = NewsTestData()

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDB = DatabaseTestHelper.initializeDb(context, AppDB::class.java)
        newsDao = appDB.getNewsDao()
    }

    @Test
    fun addCacheArticles_whenGivenNewsArticle_returnsInsertedItemRowId() {
        val newsArticleList = newsTestData.newsArticles
        newsDao.addCacheArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun deleteNewsArticleFromDb_whenGivenArticleId_deletesTheNews() {
        val newsArticleList = newsTestData.newsArticles

        val insertedId = newsDao.addCacheArticle(newsArticleList[0]).blockingGet()

        newsDao.getCachedArticles().subscribe({
            newsDao.deleteCacheArticle(insertedId.toInt()).test().await()
        }, { throwable -> throwable.printStackTrace() })

        newsDao.getCachedArticles().test()
            .assertValue {
                it.isEmpty()
            }
    }

    @Test
    fun getCachedArticles_returnsAllItems() {
        val newsArticleList = newsTestData.newsArticles

        newsDao.addCacheArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }

        newsDao.addCacheArticle(newsArticleList[1]).test()
            .assertValue {
                it == 2L
            }

        newsDao.getCachedArticles().test()
            .assertValue {
                it.size == 2
            }
            .assertValue {
                it[0].author == "author name 1"
            }
            .assertValue {
                it[1].author == "author name 2"
            }
    }

    @Test
    fun clear_clearsNewsArticleTable() {
        val newsArticleList = newsTestData.newsArticles

        newsDao.addCacheArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }

        newsDao.addCacheArticle(newsArticleList[0]).test()
            .assertValue {
                it == 2L
            }


        newsDao.getCachedArticles().test()
            .assertValue {
                it.size == 2
            }

        newsDao.clearCache().test().await()

        newsDao.getCachedArticles().test()
            .assertValue {
                it.isEmpty()
            }
    }

    @After
    fun tearDown() {
        appDB.close()
    }
}
