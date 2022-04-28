package sharedTest.testdata.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsSaved
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSource

class NewsSavedTestData {

    private val mNewsArticles = ArrayList<NewsSaved>()
    val savedNewsArticles get() = mNewsArticles

    init { createNewsArticle() }

    private fun createNewsArticle() {
        val newsArticle1 = NewsSaved(
            id = 1,
            author = "author name 1",
            content = "news content section 1",
            description = "news description section 1",
            publishedAt = "news published section 1",
            source = NewsSource(
                id = "source-id 1",
                name = "source-name 1"
            ),
            title = "title-1",
            url = "https://news1.com",
            urlToImage = "https://imageToUrl1.com"
        )

        val newsArticle2 = NewsSaved(
            id = 2,
            author = "author name 2",
            content = "news content section 2",
            description = "news description section 2",
            publishedAt = "news published section 2",
            source = NewsSource(
                id = "source-id 2",
                name = "source-name 2"
            ),
            title = "title-2",
            url = "https://news2.com",
            urlToImage = "https://imageToUrl2.com"
        )
        savedNewsArticles.add(newsArticle1)
        savedNewsArticles.add(newsArticle2)
    }
}