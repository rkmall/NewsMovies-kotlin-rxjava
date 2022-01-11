package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.domain.repository.NewsRepository
import io.reactivex.Observable
import java.util.*

class GetAllNewsArticles(val newsRepository: NewsRepository) {

    fun execute(country: String, page:Int): Observable<List<NewsArticle>> {
        return newsRepository.getNewsArticleListFromAPI(country, page)
    }
}