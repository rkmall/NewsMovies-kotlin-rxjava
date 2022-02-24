package sharedTest.testdata.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSource
import io.reactivex.Observable

class NewsDbTestData {

    private val newsArticles = ArrayList<NewsArticle>()

    private fun createTestData() {
        val newsArticle1 = NewsArticle(
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

        val newsArticle2 = NewsArticle(
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

        val newsArticle3 = NewsArticle(
            author = "author name 3",
            content = "news content section 3",
            description = "news description section 3",
            publishedAt = "news published section 3",
            source = NewsSource(
                id = "source-id 3",
                name = "source-name 3"
            ),
            title = "title-3",
            url = "https://news3.com",
            urlToImage = "https://imageToUrl3.com"
        )

        newsArticles.add(newsArticle1)
        newsArticles.add(newsArticle2)
        newsArticles.add(newsArticle3)
    }


    fun getNewsDbTestData(): List<NewsArticle> {
        createTestData()
        return newsArticles
    }


    fun getNewsDbTestDataObservable(): Observable<List<NewsArticle>> {
        createTestData()
        return Observable.create {emitter -> emitter.onNext(newsArticles)}
    }
}