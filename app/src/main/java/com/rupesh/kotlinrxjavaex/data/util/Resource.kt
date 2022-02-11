package com.rupesh.kotlinrxjavaex.data.util

sealed class Resource<T>(
    data: T? = null,
    message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T>(data: T? = null) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
}
