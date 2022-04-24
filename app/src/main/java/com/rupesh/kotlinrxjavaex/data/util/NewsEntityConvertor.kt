package com.rupesh.kotlinrxjavaex.data.util

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved


fun fromNewsArticleToNewsSaved(newsArticle: NewsArticle): NewsSaved {
    return newsArticle.let {
        NewsSaved(
            author = it.author,
            content = it.content,
            description = it.description ,
            publishedAt = it.publishedAt,
            source = it.source,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }
}


fun fromNewsSavedToNewsArticle(newsSaved: NewsSaved): NewsArticle {
    return newsSaved.let {
        NewsArticle(
            author = it.author,
            content = it.content,
            description = it.description ,
            publishedAt = it.publishedAt,
            source = it.source,
            title = it.title,
            url = it.url,
            urlToImage = it.urlToImage
        )
    }
}