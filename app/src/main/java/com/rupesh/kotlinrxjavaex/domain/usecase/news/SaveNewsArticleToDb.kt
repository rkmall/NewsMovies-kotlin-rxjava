package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Maybe
import javax.inject.Inject

class SaveNewsArticleToDb @Inject constructor(
    private val iNewsRepository: INewsRepository
) {
    fun execute(newsArticle: NewsArticle): Maybe<Long> {
        return iNewsRepository.saveNewsArticle(newsArticle)
    }
}