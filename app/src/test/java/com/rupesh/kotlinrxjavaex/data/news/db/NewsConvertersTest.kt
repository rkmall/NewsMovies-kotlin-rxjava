package com.rupesh.kotlinrxjavaex.data.news.db

import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSource
import org.junit.Before
import org.junit.Test

class NewsConvertersTest {

    private lateinit var newsSource: NewsSource
    private lateinit var newsConverters: NewsConverters

    @Before
    fun setUp() {
        newsSource = NewsSource("news-id-1", "news-channel-1")
        newsConverters = NewsConverters()
    }

    @Test
    fun `fromSource given newsSource return newsSourceName`() {
        val sourceName = newsConverters.fromSource(newsSource)
        assertThat(sourceName).isEqualTo(newsSource.name)
    }

    @Test
    fun toSource() {
        val sourceName = "news-channel-new"
        val source = newsConverters.toSource(sourceName)
        assertThat(source).isInstanceOf(NewsSource::class.java)
        assertThat(source.id).isEqualTo(sourceName)
        assertThat(source.name).isEqualTo(sourceName)
    }
}