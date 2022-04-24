package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Maybe
import io.reactivex.Observable
import javax.inject.Inject

class CacheNewsUseCase @Inject constructor(
    private val iNewsRepository: INewsRepository
) {

    fun getCachedArticles(): Observable<List<NewsArticle>> {
        return iNewsRepository.getCachedArticles()
    }

    fun addCacheArticle(newsArticle: NewsArticle): Maybe<Long> {
        return iNewsRepository.addCacheArticle(newsArticle)
    }

    fun deleteCacheArticle(id: Int): Maybe<Int>  {
        return iNewsRepository.deleteCacheArticle(id)
    }

    fun clearCache(): Maybe<Int> {
        return iNewsRepository.clearCache()
    }
}