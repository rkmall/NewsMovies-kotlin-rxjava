package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.repository.NewsRepositoryImpl
import io.reactivex.Maybe
import org.junit.Assert.*
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import sharedTest.testdata.news.NewsDbTestData

class DeleteNewsArticleFromDbTest {

    private lateinit var iNewsRepository: NewsRepositoryImpl
    private lateinit var deleteNewsArticleFromDb: DeleteNewsArticleFromDb

    @Before
    fun setUp() {
        iNewsRepository = mock(NewsRepositoryImpl::class.java)
        deleteNewsArticleFromDb = DeleteNewsArticleFromDb(iNewsRepository)
    }

    @Test
    fun execute_returnsNoOfRowDeleted() {
        val articleId = 10
        val expected = Maybe.create<Int> { emitter -> emitter.onSuccess(1)  }

        `when`(iNewsRepository.removeNewsArticle(articleId))
            .thenReturn(expected)

        deleteNewsArticleFromDb.execute(articleId).test()
            .assertValue {
                it == 1
            }
            .assertNoErrors()
    }
}