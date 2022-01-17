package com.rupesh.kotlinrxjavaex.presentation.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.domain.usecase.GetAllNewsArticles
import com.rupesh.kotlinrxjavaex.domain.usecase.GetSearchedNewsArticle
import com.rupesh.kotlinrxjavaex.presentation.viewmodel.NewsViewModel
import javax.inject.Inject

class NewsVMFactory @Inject constructor(
    private val getAllNewsArticles: GetAllNewsArticles,
    private val getSearchedNewsArticle: GetSearchedNewsArticle
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NewsViewModel(getAllNewsArticles, getSearchedNewsArticle) as T
    }
}