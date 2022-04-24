package com.rupesh.kotlinrxjavaex.presentation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import com.rupesh.kotlinrxjavaex.domain.usecase.news.CacheNewsUseCase
import com.rupesh.kotlinrxjavaex.domain.usecase.news.NetworkNewsUseCase
import com.rupesh.kotlinrxjavaex.domain.usecase.news.SaveNewsUseCase
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.exceptions.UndeliverableException
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.inOrder
import org.mockito.kotlin.times
import sharedTest.RxImmediateSchedulerRule
import sharedTest.testdata.news.NewsSavedTestData
import sharedTest.testdata.news.NewsTestData

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule() // for LiveData

    @Rule
    @JvmField
    var testSchedulerRule = RxImmediateSchedulerRule()      // for Retrofit

    private lateinit var networkNewsUseCase: NetworkNewsUseCase
    private lateinit var cacheNewsUseCase: CacheNewsUseCase
    private lateinit var saveNewsUseCase: SaveNewsUseCase
    private lateinit var newsViewModel: NewsViewModel

    private val newsArticleTestData = NewsTestData()
    private val newsSavedTestData = NewsSavedTestData()

    @Before
    fun setUp() {
        networkNewsUseCase = mock(NetworkNewsUseCase::class.java)
        cacheNewsUseCase = mock(CacheNewsUseCase::class.java)
        saveNewsUseCase = mock(SaveNewsUseCase::class.java)
        newsViewModel = NewsViewModel(
            networkNewsUseCase,
            cacheNewsUseCase,
            saveNewsUseCase
        )
    }

    @Test
    fun `cacheArticle given news article and sets the event message on cache Success`() {
        val articleToBeCached = newsArticleTestData.newsArticles[0]
        `when`(cacheNewsUseCase.addCacheArticle(articleToBeCached))
            .thenReturn(Maybe.just(1L))

        newsViewModel.cacheArticle(articleToBeCached)

        newsViewModel.eventMessage.value?.let {
            it.peekContent().let { message ->
                assertThat(message).isEqualTo("Cache news article success")
            }
        }
    }

    @Test
    fun `cacheNewsArticle given the dataToBeSaved sets failureStatusMessage OnFailure`() {
        val articleToBeCached = newsArticleTestData.newsArticles[0]
        `when`(cacheNewsUseCase.addCacheArticle(articleToBeCached))
            .thenReturn(Maybe.create { e -> e.onError(UndeliverableException(UnknownError()))})

        newsViewModel.cacheArticle(articleToBeCached)

        newsViewModel.eventMessage.value?.let {
            it.peekContent().let { message ->
                assertThat(message).isEqualTo("Cache internal error")
            }
        }
    }


    @Test
    fun `newsApiCall given CountryAndPage sets NewsLiveData and caches the fetched data to NewsArticle db OnSuccess`() {
        val newsArticles = newsArticleTestData.newsArticles
        `when`(cacheNewsUseCase.addCacheArticle(newsArticles[0])).thenReturn(Maybe.just(1L))
        `when`(cacheNewsUseCase.addCacheArticle(newsArticles[1])).thenReturn(Maybe.just(2L))

        `when`(networkNewsUseCase.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS)
        ).thenReturn(Single.just(Resource.Success(newsArticleTestData.newsArticles)))

        newsViewModel.newsApiCall (
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        )

        newsViewModel.topHeadlines.value?.let {
            assertThat(it.data).isNotNull()
            assertThat(it).isInstanceOf(Resource.Success::class.java)
            if(it is Resource.Success) {
                it.data?.let { list ->
                    assertThat(list).isNotEmpty()
                    assertThat(list.size).isEqualTo(2)
                    assertThat(list[0].author).isEqualTo("author name 1")
                    assertThat(list[1].author).isEqualTo("author name 2")
                }
            }
        }

        val order = inOrder(cacheNewsUseCase)
        order.verify(cacheNewsUseCase, times(1)).addCacheArticle(newsArticles[0])
        order.verify(cacheNewsUseCase, times(1)).addCacheArticle(newsArticles[1])
    }

    @Test
    fun `newsApiCall given CountryAndPage sets NewsLiveData with ErrorMessage OnFailure`() {
        val newsArticles = newsArticleTestData.newsArticles
        val errorMessage = "Error: 400: Cannot fetch news articles"

        `when`(networkNewsUseCase.getTopHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS)
        ).thenReturn(Single.just(Resource.Error(message = errorMessage)))


        newsViewModel.topHeadlines.value?.let {
            assertThat(it.data).isNull()
            assertThat(it).isInstanceOf(Resource.Error::class.java)
            assertThat(it.message).isEqualTo(errorMessage)
        }

        verify(cacheNewsUseCase, times(0)).addCacheArticle(newsArticles[0])
    }


    @Test
    fun `getSearchedHeadlines given CountryPageAndSearchKey sets SearchedNewsLiveData with theReturnedResult OnSuccess`() {
        val keyword = "news"
        `when`(networkNewsUseCase.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            keyword,
            AppConstantsData.DEFAULT_PAGE_NEWS)
        ).thenReturn(Single.just(Resource.Success(newsArticleTestData.newsArticles)))


        newsViewModel.getSearchedHeadlines (
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            keyword,
            AppConstantsData.DEFAULT_PAGE_NEWS
        )
        newsViewModel.searchedHeadlines.value?.let {
            assertThat(it.data).isNotNull()
            assertThat(it).isInstanceOf(Resource.Success::class.java)
            if(it is Resource.Success) {
                it.data?.let { list ->
                    assertThat(list).isNotEmpty()
                    assertThat(list.size).isEqualTo(2)
                    assertThat(list[0].author).isEqualTo("author name 1")
                    assertThat(list[1].author).isEqualTo("author name 2")
                }
            }
        }
    }

    @Test
    fun `getSearchedHeadlines given CountryPageAndSearchKey sets SearchedNewsLiveData with ErrorMessage OnFailure`() {
        val keyword = "news"
        val errorMessage = "Error: 400: Cannot fetch searched news articles"

        `when`(networkNewsUseCase.getSearchedHeadlines(
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            keyword,
            AppConstantsData.DEFAULT_PAGE_NEWS)
        ).thenReturn(Single.just(Resource.Error(message = errorMessage)))

        newsViewModel.searchedHeadlines.value?.let {
            assertThat(it.data).isNull()
            assertThat(it).isInstanceOf(Resource.Error::class.java)
            assertThat(it.message).isEqualTo(errorMessage)
        }
    }


    @Test
    fun `getCachedNewsArticles returns the cachedNewsArticlesFromDb`() {
        `when`(cacheNewsUseCase.getCachedArticles())
            .thenReturn(Observable.just(newsArticleTestData.newsArticles))

        newsViewModel.topHeadlines.value?.let {
            assertThat(it.data).isNotNull()
            assertThat(it).isInstanceOf(Resource.Success::class.java)
            if(it is Resource.Success) {
                it.data.let { list ->
                    assertThat(list).isNotEmpty()
                    assertThat(list?.size).isEqualTo(2)
                    assertThat(list?.get(0)?.author).isEqualTo("author name 1")
                    assertThat(list?.get(1)?.author).isEqualTo("author name 2")
                }
            }
        }
    }

    @Test
    fun `clearCache clears the NewsArticle table and sets the event message on Success`() {
        val noOfItemsDeleted = 100
        `when`(cacheNewsUseCase.clearCache())
            .thenReturn(Maybe.just(noOfItemsDeleted))

        newsViewModel.clearCache()

        newsViewModel.eventMessage.value?.let {
            it.peekContent().let { message ->
                assertThat(message).isEqualTo("Cleared cached article list, $noOfItemsDeleted deleted")
            }
        }
    }

    @Test
    fun `clearCache sets failureStatusMessage OnFailure`() {
        `when`(cacheNewsUseCase.clearCache())
            .thenReturn(Maybe.create { e -> e.onError(UndeliverableException(UnknownError()))})

        newsViewModel.clearCache()

        newsViewModel.eventMessage.value?.let {
            it.peekContent().let { message ->
                assertThat(message).isEqualTo("Clear cache internal error")
            }
        }
    }

    @Test
    fun `getSavedNewsArticles returns the cachedNewsArticlesFromDb`() {
        `when`(saveNewsUseCase.getSavedArticles())
            .thenReturn(Observable.just(newsSavedTestData.savedNewsArticles))

        newsViewModel.savedNews.value?.let {
            assertThat(it).isNotNull()
            assertThat(it).isInstanceOf(Resource.Success::class.java)
            assertThat(it).isNotEmpty()
            assertThat(it.size).isEqualTo(2)
            assertThat(it[0].author).isEqualTo("author name 1")
            assertThat(it[1].author).isEqualTo("author name 2")
        }
    }

    @Test
    fun `getSavedNewsArticles sets failureStatusMessage OnFailure`() {
        `when`(saveNewsUseCase.getSavedArticles())
            .thenReturn(Observable.create {e -> e.onError(UndeliverableException(UnknownError()))})

        newsViewModel.getSavedArticles()

        newsViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not fetch the saved articles")
            }
        }
    }


    @Test
    fun `saveNewsArticle given the dataToBeSaved sets successStatusMessage OnSuccess`() {
        val articleToBeSaved = newsSavedTestData.savedNewsArticles[0]
        `when`(saveNewsUseCase.saveArticle(articleToBeSaved))
            .thenReturn(Maybe.just(1L))

        newsViewModel.saveArticle(articleToBeSaved)

        newsViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Article Saved")
            }
        }
    }

    @Test
    fun `saveNewsArticle given the dataToBeSaved sets failureStatusMessage OnFailure`() {
        val articleToBeSaved = newsSavedTestData.savedNewsArticles[0]
        `when`(saveNewsUseCase.saveArticle(articleToBeSaved))
            .thenReturn(Maybe.create { e -> e.onError(UndeliverableException(UnknownError()))})

        newsViewModel.saveArticle(articleToBeSaved)

        newsViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not save the article")
            }
        }
    }

    @Test
    fun `deleteNewsArticle given the dataToBeDeleted sets successStatusMessage OnSuccess`() {
        `when`(saveNewsUseCase.deleteSavedArticle(1))
            .thenReturn(Maybe.just(1))

        newsViewModel.deleteSavedArticle(1)

        newsViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Article Deleted")
            }
        }
    }

    @Test
    fun `deleteNewsArticle given the dataToBeSaved sets failureStatusMessage OnFailure`() {
        `when`(saveNewsUseCase.deleteSavedArticle(1))
            .thenReturn(Maybe.create { e -> e.onError(UndeliverableException(UnknownError()))})

        newsViewModel.deleteSavedArticle(1)

        newsViewModel.eventMessage.value?.let {
            it.getContentIfNotHandled().let { message ->
                assertThat(message).isEqualTo("Could not delete the article")
            }
        }
    }


    @Test
    fun `clearSaved clears the NewsSaved table and sets the event message on Success`() {
        val noOfItemsDeleted = 100
        `when`(saveNewsUseCase.clearSaved())
            .thenReturn(Maybe.just(noOfItemsDeleted))

        newsViewModel.clearSaved()

        newsViewModel.eventMessage.value?.let {
            it.peekContent().let { message ->
                assertThat(message).isEqualTo("Cleared saved article list, $noOfItemsDeleted deleted")
            }
        }
    }

    @Test
    fun `clearSaved sets failureStatusMessage OnFailure`() {
        `when`(saveNewsUseCase.clearSaved())
            .thenReturn(Maybe.create { e -> e.onError(UndeliverableException(UnknownError()))})

        newsViewModel.clearSaved()

        newsViewModel.eventMessage.value?.let {
            it.peekContent().let { message ->
                assertThat(message).isEqualTo("Clear saved internal error")
            }
        }
    }

}
