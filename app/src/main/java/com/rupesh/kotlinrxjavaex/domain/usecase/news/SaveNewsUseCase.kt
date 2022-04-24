package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class SaveNewsUseCase @Inject constructor(
    private val iNewsRepository: INewsRepository
) {
    fun getSavedArticles(): Observable<List<NewsSaved>> {
        return iNewsRepository.getSavedArticles()
    }

    fun saveArticle(newsSaved: NewsSaved): Maybe<Long> {
        return iNewsRepository.saveArticle(newsSaved)
    }

    fun deleteSavedArticle(id: Int): Maybe<Int> {
        return iNewsRepository.deleteSavedArticle(id)
    }

    fun clearSaved(): Maybe<Int> {
        return iNewsRepository.clearSaved()
    }
}