package com.rupesh.kotlinrxjavaex.data.news.repository.dataSourceImpl

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.rupesh.kotlinrxjavaex.data.news.db.NewsDB
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import sharedTest.DatabaseTestHelper
import sharedTest.testdata.news.NewsDbTestData

class NewsLocalDataSourceImplTest {

    // To execute each task synchronously in the background instead of default background executor
    // used by Architecture components
    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var newsDb: NewsDB
    lateinit var newsDbTestData: NewsDbTestData
    lateinit var newsLocalDataSourceImpl: NewsLocalDataSourceImpl

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        newsDb = DatabaseTestHelper.initializeDb(context, NewsDB::class.java)
        newsDbTestData = NewsDbTestData()
        newsLocalDataSourceImpl = NewsLocalDataSourceImpl(newsDb.getNewsDao())
    }

    @Test
    fun addNewsArticleToDb_whenGivenNewsArticle_returnsInsertedItemRowId() {
        val newsArticleList = newsDbTestData.getNewsDbTestData()
        newsLocalDataSourceImpl.addNewsArticleToDb(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun deleteNewsArticleFromDb_whenGivenArticleId_returnsNoOfRowDeleted() {
        val newsArticleList = newsDbTestData.getNewsDbTestData()

        newsLocalDataSourceImpl.addNewsArticleToDb(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }

        newsLocalDataSourceImpl.addNewsArticleToDb(newsArticleList[1]).test()
            .assertValue {
                it == 2L
            }

        newsLocalDataSourceImpl.deleteNewsArticleFromDb(2).test()
            .assertValue {
                it == 1
            }

        newsLocalDataSourceImpl.getSavedNewsArticles().test()
            .assertValue {
                it.size == 1
            }
    }

    @Test
    fun getNewsArticleFromDb_returnsAllItems() {
        val newsArticleList = newsDbTestData.getNewsDbTestData()

        newsLocalDataSourceImpl.addNewsArticleToDb(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }

        newsLocalDataSourceImpl.addNewsArticleToDb(newsArticleList[1]).test()
            .assertValue {
                it == 2L
            }

        newsLocalDataSourceImpl.getSavedNewsArticles().test()
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

    @After
    fun tearDown() {
        newsDb.close()
    }
}