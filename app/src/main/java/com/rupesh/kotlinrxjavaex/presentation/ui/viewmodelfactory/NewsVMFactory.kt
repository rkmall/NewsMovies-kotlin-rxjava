package com.rupesh.kotlinrxjavaex.presentation.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.domain.usecase.news.*
import javax.inject.Inject

class NewsVMFactory @Inject constructor(
    private val getAllNewsArticles: GetAllNewsArticles,
    private val getSearchedNewsArticle: GetSearchedNewsArticle,
    private val getSavedNewsArticles: GetSavedNewsArticles,
    private val saveNewsArticleToDb: SaveNewsArticleToDb,
    private val deleteNewsArticleFromDb: DeleteNewsArticleFromDb,
    private val deleteAll: DeleteAll
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            GetAllNewsArticles::class.java,
            GetSearchedNewsArticle::class.java,
            GetSavedNewsArticles::class.java,
            SaveNewsArticleToDb::class.java,
            DeleteNewsArticleFromDb::class.java,
            DeleteAll::class.java
        ).newInstance(
            getAllNewsArticles,
            getSearchedNewsArticle,
            getSavedNewsArticles,
            saveNewsArticleToDb,
            deleteNewsArticleFromDb,
            deleteAll
        )
    }
}