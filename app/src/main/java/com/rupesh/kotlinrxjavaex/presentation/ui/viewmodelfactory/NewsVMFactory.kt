/*
package com.rupesh.kotlinrxjavaex.presentation.ui.viewmodelfactory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.rupesh.kotlinrxjavaex.domain.usecase.news.*
import javax.inject.Inject

class NewsVMFactory @Inject constructor(
    private val networkNewsUseCase: NetworkNewsUseCase,
    private val cacheNewsUseCase: CacheNewsUseCase,
    private val saveNewsUseCase: SaveNewsUseCase
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(
            NetworkNewsUseCase::class.java,
            CacheNewsUseCase::class.java,
            SaveNewsUseCase::class.java
        ).newInstance(
            networkNewsUseCase,
            cacheNewsUseCase,
            saveNewsUseCase
        )
    }
}*/
