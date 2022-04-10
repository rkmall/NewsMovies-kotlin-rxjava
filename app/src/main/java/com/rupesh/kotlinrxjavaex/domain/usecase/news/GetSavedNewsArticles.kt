package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Observable
import javax.inject.Inject

class GetSavedNewsArticles @Inject constructor(
    private val iNewsRepository: INewsRepository
) {
    fun execute(): Observable<List<NewsArticle>> {
        return iNewsRepository.getSavedNewsArticles()
    }
}