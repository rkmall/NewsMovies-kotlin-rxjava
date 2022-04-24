package com.rupesh.kotlinrxjavaex.presentation.util

open class Event<out T>(private val content: T) {

    var hasBeenHandled = false
        //private set

    /**
     * Returns the content and prevents its use again
     */
    fun getContentIfNotHandled(): T? {
        return if(hasBeenHandled) {
            null
        }else {
            hasBeenHandled = true
            content
        }
    }

    /**
     * Return the content, even if it's already been handled
     */
    fun peekContent(): T = content
}