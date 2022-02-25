package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.Response
import javax.inject.Inject

class GetAllNewsArticles @Inject constructor(
    private val iNewsRepository: INewsRepository
) {
    fun execute(country: String, page:Int): Observable<Response<NewsResponse>> {
        return iNewsRepository.getTopHeadlines(country, page)
    }
}