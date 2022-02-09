package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.domain.repository.NewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetSearchedNewsArticle @Inject constructor(
    private val newsRepository: NewsRepository
) {
    fun execute(country: String, searchQuery: String, page: Int): Observable<List<NewsArticle>> {
        return newsRepository.getSearchedNewsArticleListFromAPI(country, searchQuery, page)
    }
}