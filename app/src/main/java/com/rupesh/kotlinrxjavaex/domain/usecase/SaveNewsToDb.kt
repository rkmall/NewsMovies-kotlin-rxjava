package com.rupesh.kotlinrxjavaex.domain.usecase

import com.rupesh.kotlinrxjavaex.domain.repository.NewsRepository
import javax.inject.Inject

class SaveNewsToDb @Inject constructor(
    private val newsRepository: NewsRepository
){
}