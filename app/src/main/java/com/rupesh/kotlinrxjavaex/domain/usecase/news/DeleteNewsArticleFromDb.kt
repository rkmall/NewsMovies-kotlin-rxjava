package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Maybe
import javax.inject.Inject

class DeleteNewsArticleFromDb @Inject constructor(
    private val iNewsRepository: INewsRepository
) {

    fun execute(id: Int): Maybe<Int>  {
        return iNewsRepository.removeNewsArticle(id)
    }
}