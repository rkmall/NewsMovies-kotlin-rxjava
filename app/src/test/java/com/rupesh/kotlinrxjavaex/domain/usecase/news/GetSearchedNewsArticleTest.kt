package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.repository.NewsRepositoryImpl
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.observers.TestObserver
import org.junit.Before

import org.junit.Test
import org.mockito.Mockito
import retrofit2.Response
import sharedTest.testdata.news.NewsResponseTestData

class GetSearchedNewsArticleTest {

    private lateinit var iNewsRepository: INewsRepository
    private lateinit var getSearchedNewsArticle: GetSearchedNewsArticle
    private lateinit var responseTestData: NewsResponseTestData
    private lateinit var testObserver: TestObserver<Response<NewsResponse>>

    @Before
    fun setUp() {
        iNewsRepository = Mockito.mock(NewsRepositoryImpl::class.java)
        getSearchedNewsArticle = GetSearchedNewsArticle(iNewsRepository)
        responseTestData = NewsResponseTestData()
        testObserver = TestObserver()
    }

    @Test
    fun executeSuccess() {
        val searchKey = "news"
        Mockito.`when`(
            iNewsRepository.getSearchedHeadlines(
                AppConstantsData.DEFAULT_COUNTRY_NEWS,
                searchKey,
                AppConstantsData.DEFAULT_PAGE_NEWS
            )
        )
            .thenReturn(responseTestData.getNewsResponseDataSuccess())

        getSearchedNewsArticle.execute(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                it.code() == 200
            }
            .assertValue {
                it.body()!!.articles.size == 2
            }
    }

    @Test
    fun executeError() {
        val searchKey = "news"
        Mockito.`when`(
            iNewsRepository.getSearchedHeadlines(
                AppConstantsData.DEFAULT_COUNTRY_NEWS,
                searchKey,
                AppConstantsData.DEFAULT_PAGE_NEWS
            )
        )
            .thenReturn(responseTestData.getResponseDataError())

        getSearchedNewsArticle.execute(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                it.code() == 400
            }
            .assertValue {
                it.message().toString() == "Response.error(), bad-request"
            }
            .assertValue {
                it.body().toString() == "null"
            }
    }

    fun tearDown() {
        testObserver.dispose()
    }
}