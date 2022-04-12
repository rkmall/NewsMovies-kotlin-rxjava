package com.rupesh.kotlinrxjavaex.domain.usecase.news

import com.rupesh.kotlinrxjavaex.domain.repository.INewsRepository
import io.reactivex.Maybe
import javax.inject.Inject

class DeleteAll @Inject constructor(
    private val iNewsRepository: INewsRepository
){
    fun execute(): Maybe<Int> {
        return iNewsRepository.clear()
    }
}