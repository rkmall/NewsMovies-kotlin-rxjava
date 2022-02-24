package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.repository.NewsRepositoryImpl
import io.reactivex.Maybe
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito
import sharedTest.testdata.news.NewsDbTestData

class SaveNewsArticleToDbTest {

    private lateinit var iNewsRepository: NewsRepositoryImpl
    private lateinit var saveNewsArticleToDb: SaveNewsArticleToDb
    private lateinit var newsDbTestData: NewsDbTestData

    @Before
    fun setUp() {
        iNewsRepository = Mockito.mock(NewsRepositoryImpl::class.java)
        saveNewsArticleToDb = SaveNewsArticleToDb(iNewsRepository)
        newsDbTestData = NewsDbTestData()
    }

    @Test
    fun execute_returnsSavedArticleId() {

        val articleList = newsDbTestData.getNewsDbTestData()
        val expected = Maybe.create<Long> { emitter -> emitter.onSuccess(1L)  }

        Mockito.`when`(iNewsRepository.saveNewsArticle(articleList[0]))
            .thenReturn(expected)

        saveNewsArticleToDb.execute(articleList[0]).test()
            .assertValue {
                it == 1L
            }
    }
}