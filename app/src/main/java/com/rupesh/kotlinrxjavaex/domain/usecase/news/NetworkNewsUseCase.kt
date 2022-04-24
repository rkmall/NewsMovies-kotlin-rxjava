package com.rupesh.kotlinrxjavaex.domain.usecase.news

import android.util.Log
import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import com.rupesh.kotlinrxjavaex.presentation.util.AppConstPresentation
import com.rupesh.kotlinrxjavaex.presentation.util.Resource
import io.reactivex.Single
import javax.inject.Inject

class NetworkNewsUseCase @Inject constructor(
    private val iNewsRepository: INewsRepository
) {
    fun getTopHeadlines(country: String, page:Int): Single<Resource<List<NewsArticle>>> {
        return iNewsRepository
            .getTopHeadlines(country, page)
            .map {
                Log.i(AppConstPresentation.LOG_UI, "Response code news headlines: ${it.code()}")
                when {
                    it.isSuccessful -> it.body()?.let { resource -> Resource.Success(resource.articles) }
                    else -> Resource.Error(message = "Error: ${it.code()}: Cannot fetch news articles")
                }
            }
    }

    fun getSearchedHeadlines(
        country: String,
        searchQuery: String,
        page: Int
    ): Single<Resource<List<NewsArticle>>> {
        return iNewsRepository
            .getSearchedHeadlines(country, searchQuery, page)
            .map {
                Log.i(AppConstPresentation.LOG_UI, "Response code news search: ${it.code()}")
                when {
                    it.isSuccessful -> it.body()?.let { resource -> Resource.Success(resource.articles) }
                    else -> Resource.Error(message = "Error: ${it.code()}: Cannot fetch searched news articles")
                }
            }
    }
}