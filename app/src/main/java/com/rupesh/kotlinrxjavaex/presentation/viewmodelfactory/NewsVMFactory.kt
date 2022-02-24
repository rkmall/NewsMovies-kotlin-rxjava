package com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.domain.usecase.news.*
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.NewsViewModel
import javax.inject.Inject

class NewsVMFactory @Inject constructor(
    private val getAllNewsArticles: GetAllNewsArticles,
    private val getSearchedNewsArticle: GetSearchedNewsArticle,
    private val getSavedNewsArticles: GetSavedNewsArticles,
    private val saveNewsArticleToDb: SaveNewsArticleToDb,
    private val deleteNewsArticleFromDb: DeleteNewsArticleFromDb
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(
            getAllNewsArticles,
            getSearchedNewsArticle,
            getSavedNewsArticles,
            saveNewsArticleToDb,
            deleteNewsArticleFromDb) as T
    }
}