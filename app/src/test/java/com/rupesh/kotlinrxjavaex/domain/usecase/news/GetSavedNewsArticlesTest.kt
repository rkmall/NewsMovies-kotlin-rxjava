package com.rupesh.kotlinrxjavaex.domain.usecase.news

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.rupesh.kotlinrxjavaex.data.news.repository.NewsRepositoryImpl
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import sharedTest.testdata.news.NewsDbTestData
import java.lang.AssertionError

class GetSavedNewsArticlesTest {

    private lateinit var iNewsRepository: NewsRepositoryImpl
    private lateinit var getSavedNewsArticles: GetSavedNewsArticles
    private lateinit var newsDbTestData: NewsDbTestData

    @Before
    fun setUp() {
        iNewsRepository = Mockito.mock(NewsRepositoryImpl::class.java)
        getSavedNewsArticles = GetSavedNewsArticles(iNewsRepository)
        newsDbTestData = NewsDbTestData()
    }

    @Test
    fun execute_returnsSavedArticleListFromDb() {
        Mockito.`when`(iNewsRepository.getSavedNewsArticles())
            .thenReturn(newsDbTestData.getNewsDbTestDataObservable())

        getSavedNewsArticles.execute().test()
            .assertValue {
                println(it.size)
                it.size == 3
            }
            .assertNoErrors()
    }
}