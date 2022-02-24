package com.rupesh.kotlinrxjavaex.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.domain.usecase.news.*
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import io.reactivex.Maybe
import io.reactivex.exceptions.UndeliverableException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.news.NewsDbTestData
import sharedTest.testdata.news.NewsResponseTestData

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // for LiveData

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()      // for Retrofit

    private lateinit var getAllNewsArticles: GetAllNewsArticles
    private lateinit var getSearchedNewsArticle: GetSearchedNewsArticle
    private lateinit var getSavedNewsArticles: GetSavedNewsArticles
    private lateinit var saveNewsArticleToDb: SaveNewsArticleToDb
    private lateinit var deleteNewsArticleFromDb: DeleteNewsArticleFromDb
    private lateinit var newsViewModel: NewsViewModel

    @Before
    fun setUp() {
        getAllNewsArticles = mock(GetAllNewsArticles::class.java)
        getSearchedNewsArticle = mock(GetSearchedNewsArticle::class.java)
        getSavedNewsArticles = mock(GetSavedNewsArticles::class.java)
        saveNewsArticleToDb = mock(SaveNewsArticleToDb::class.java)
        deleteNewsArticleFromDb = mock(DeleteNewsArticleFromDb::class.java)
        newsViewModel = NewsViewModel(
            getAllNewsArticles,
            getSearchedNewsArticle,
            getSavedNewsArticles,
            saveNewsArticleToDb,
            deleteNewsArticleFromDb
        )
    }

    @Test
    fun `getNewsList given CountryAndPage sets NewsLiveData with theReturnedResult OnSuccess`() {
        val responseTestData = NewsResponseTestData()

        `when`(getAllNewsArticles.execute(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS))
            .thenReturn(responseTestData.getNewsResponseDataSuccess())

        newsViewModel.getNewsList(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS)

        newsViewModel.newsLiveDataResult.value.let {
            assertThat(it?.data).isNotNull()
            assertThat(it).isInstanceOf(Resource.Success::class.java)
            if(it is Resource.Success) {
                it.data.let { list ->
                    assertThat(list).isNotEmpty()
                    if (list != null) {
                        assertThat(list.size).isEqualTo(2)
                    }
                }
            }
        }
    }

    @Test
    fun `getNewsList given CountryAndPage sets NewsLiveData with ErrorMessage OnFailure`() {
        val responseTestData = NewsResponseTestData()

        `when`(getAllNewsArticles.execute(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS))
            .thenReturn(responseTestData.getResponseDataError())

        newsViewModel.getNewsList(AppConstantsData.DEFAULT_COUNTRY_NEWS, AppConstantsData.DEFAULT_PAGE_NEWS)

        newsViewModel.newsLiveDataResult.value?.let {
            assertThat(it.data).isNull()
            assertThat(it).isInstanceOf(Resource.Error::class.java)
            assertThat(it.message).isEqualTo("Cannot fetch news")
        }
    }


    @Test
    fun `getSearchedList given CountryPageAndSearchKey sets SearchedNewsLiveData with theReturnedResult OnSuccess`() {
        val responseTestData = NewsResponseTestData()
        val searchKey = "news"

        `when`(getSearchedNewsArticle.execute(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS))
            .thenReturn(responseTestData.getNewsResponseDataSuccess())

        newsViewModel.getSearchedNewsList(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS)

        newsViewModel.searchedNewsLivedataResult.value.let {
            assertThat(it?.data).isNotNull()
            assertThat(it).isInstanceOf(Resource.Success::class.java)
            if(it is Resource.Success) {
                it.data.let { list ->
                    assertThat(list).isNotEmpty()
                    if (list != null) {
                        assertThat(list.size).isEqualTo(2)
                    }
                }
            }
        }
    }


    @Test
    fun `getSearchedList given CountryPageAndSearchKey sets SearchedNewsLiveData with ErrorMessage OnFailure`() {
        val responseTestData = NewsResponseTestData()
        val searchKey = "news"

        `when`(getSearchedNewsArticle.execute(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS))
            .thenReturn(responseTestData.getResponseDataError())

        newsViewModel.getSearchedNewsList(AppConstantsData.DEFAULT_COUNTRY_NEWS, searchKey, AppConstantsData.DEFAULT_PAGE_NEWS)

        newsViewModel.searchedNewsLivedataResult.value.let {
            assertThat(it?.data).isNull()
            assertThat(it).isInstanceOf(Resource.Error::class.java)
            if(it is Resource.Error) {
                assertThat(it.message).isEqualTo("Cannot fetch search results")
            }
        }
    }


    @Test
    fun `getSavedNewsArticles given the savedDataInDb returns the savedDataFromDb`() {
        val dbTestData = NewsDbTestData()

        `when`(getSavedNewsArticles.execute()).thenReturn(dbTestData.getNewsDbTestDataObservable())

        newsViewModel.getSavedNewsArticles()

        newsViewModel.savedNewsArticlesLiveDataResult.value.let {
            assertThat(it).isNotEmpty()

            it?.let {
                assertThat(it.size).isEqualTo(3)
                assertThat(it[0].title).isEqualTo("title-1")
                assertThat(it[1].title).isEqualTo("title-2")
                assertThat(it[2].title).isEqualTo("title-3")
            }
        }
    }

    @Test
    fun `saveNewsArticle given the dataToBeSaved sets successStatusMessage OnSuccess`() {
        val testArticleToBeSaved = NewsDbTestData().getNewsDbTestData()[0]
        val expected = Maybe.create<Long> { emitter -> emitter.onSuccess(1L)  }

        `when`(saveNewsArticleToDb.execute(testArticleToBeSaved)).thenReturn(expected)
        newsViewModel.saveNewsArticle(testArticleToBeSaved)

        newsViewModel.statusMessageResult.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Article Saved")
            }
        }
    }


    @Test
    fun `saveNewsArticle given the dataToBeSaved sets failureStatusMessage OnFailure`() {
        val testArticleToBeSaved = NewsDbTestData().getNewsDbTestData()[0]
        val expected = Maybe.create<Long> { emitter ->
            emitter.onError(UndeliverableException(UnknownError()))
        }

        `when`(saveNewsArticleToDb.execute(testArticleToBeSaved)).thenReturn(expected)
        newsViewModel.saveNewsArticle(testArticleToBeSaved)

        newsViewModel.statusMessageResult.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not save the article")
            }
        }
    }

    @Test
    fun `deleteNewsArticle given the dataToBeDeleted sets successStatusMessage OnSuccess`() {
        val expected = Maybe.create<Int> { emitter -> emitter.onSuccess(1)  }

        `when`(deleteNewsArticleFromDb.execute(1)).thenReturn(expected)

        newsViewModel.deleteNewsArticle(1)

        newsViewModel.statusMessageResult.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Article Deleted")
            }
        }
    }

    @Test
    fun `deleteNewsArticle given the dataToBeSaved sets failureStatusMessage OnFailure`() {
        val expected = Maybe.create<Int> { emitter ->
            emitter.onError(UndeliverableException(UnknownError()))
        }

        `when`(deleteNewsArticleFromDb.execute(1)).thenReturn(expected)
        newsViewModel.deleteNewsArticle(1)

        newsViewModel.statusMessageResult.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not delete the article")
            }
        }
    }
}