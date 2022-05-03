package com.rupesh.kotlinrxjavaex.presentation.util

import com.google.common.truth.Truth.assertThat
import org.junit.Before

import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class EventTest {

    private lateinit var event: Event<String>

    @Before
    fun setUp() {
        event = Event("message")
    }

    @Test
    fun `call to getContentIfNotHandled returns content And SetsHandledToTrue`() {
        val result = event.getContentIfNotHandled()
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("message")
        assertThat(event.hasBeenHandled).isTrue()
    }


    @Test
    fun `call to getContentIfNotHandled when eventAlreadyHandled returns null`() {
        val resultOfFirstCall = event.getContentIfNotHandled()
        assertThat(resultOfFirstCall).isInstanceOf(String::class.java)
        assertThat(event.hasBeenHandled).isTrue()

        val resultOfSecondCall = event.getContentIfNotHandled()
        assertThat(resultOfSecondCall).isNull()
    }


    @Test
    fun `call to peekContent returns theContent even if the eventAlreadyHandled`() {
        val result = event.getContentIfNotHandled()
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo("message")
        assertThat(event.hasBeenHandled).isTrue()

        val peekResult = event.peekContent()
        assertThat(peekResult).isNotNull()
        assertThat(peekResult).isEqualTo("message")
    }
}