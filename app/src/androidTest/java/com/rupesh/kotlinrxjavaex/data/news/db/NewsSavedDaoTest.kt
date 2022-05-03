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
import sharedTest.testdata.news.NewsSavedTestData

@RunWith(JUnit4::class)
class NewsSavedDaoTest {

    @get:Rule
    var instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()


    lateinit var appDB: AppDB
    lateinit var newsSavedDao: NewsSavedDao
    var newsTestData = NewsSavedTestData()

    @Before
    fun setUp() {
        val context: Context = ApplicationProvider.getApplicationContext()
        appDB = DatabaseTestHelper.initializeDb(context, AppDB::class.java)
        newsSavedDao = appDB.getNewsSavedDao()
    }

    @Test
    fun saveArticle_whenGivenNewsArticle_returnsInsertedItemRowId() {
        val newsArticleList = newsTestData.savedNewsArticles
        newsSavedDao.saveArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }
    }

    @Test
    fun deleteNewsArticleFromDb_whenGivenArticleId_deletesTheNews() {
        val newsArticleList = newsTestData.savedNewsArticles

        val insertedId = newsSavedDao.saveArticle(newsArticleList[0]).blockingGet()

        newsSavedDao.getSavedArticles().subscribe({
            newsSavedDao.deleteSavedArticle(insertedId.toInt()).test().await()
        }, { throwable -> throwable.printStackTrace() })

        newsSavedDao.getSavedArticles().test()
            .assertValue {
                it.isEmpty()
            }
    }

    @Test
    fun getCachedArticles_returnsAllItems() {
        val newsArticleList = newsTestData.savedNewsArticles

        newsSavedDao.saveArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }

        newsSavedDao.saveArticle(newsArticleList[1]).test()
            .assertValue {
                it == 2L
            }

        newsSavedDao.getSavedArticles().test()
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
        val newsArticleList = newsTestData.savedNewsArticles

        newsSavedDao.saveArticle(newsArticleList[0]).test()
            .assertValue {
                it == 1L
            }

        newsSavedDao.saveArticle(newsArticleList[1]).test()
            .assertValue {
                it == 2L
            }


        newsSavedDao.getSavedArticles().test()
            .assertValue {
                it.size == 2
            }

        newsSavedDao.clearSaved().test().await()

        newsSavedDao.getSavedArticles().test()
            .assertValue {
                it.isEmpty()
            }
    }

    @After
    fun tearDown() {
        appDB.close()
    }
}