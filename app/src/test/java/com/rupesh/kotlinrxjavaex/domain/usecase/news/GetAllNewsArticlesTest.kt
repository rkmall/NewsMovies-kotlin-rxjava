package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.repository.NewsRepositoryImpl
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.Response
import sharedTest.testdata.news.NewsResponseTestData

class GetAllNewsArticlesTest {

    private lateinit var iNewsRepository: INewsRepository
    private lateinit var getAllNewsArticles: GetAllNewsArticles
    private lateinit var responseTestData: NewsResponseTestData
    private lateinit var testObserver: TestObserver<Response<NewsResponse>>

    @Before
    fun setUp() {
        iNewsRepository = mock(NewsRepositoryImpl::class.java)
        getAllNewsArticles = GetAllNewsArticles(iNewsRepository)
        responseTestData = NewsResponseTestData()
        testObserver = TestObserver()
    }

    @Test
    fun executeSuccess() {
        `when`(iNewsRepository.getTopHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS))
            .thenReturn(responseTestData.getNewsResponseDataSuccess())

        getAllNewsArticles.execute(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS).subscribe(testObserver)

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
        `when`(iNewsRepository.getTopHeadlines(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS))
            .thenReturn(responseTestData.getResponseDataError())

        getAllNewsArticles.execute(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS).subscribe(testObserver)

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