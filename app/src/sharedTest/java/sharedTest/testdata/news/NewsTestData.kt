package sharedTest.testdata.news

import com.rupesh.kotlinrxjavaex.data.news.model.NewsArticle
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.model.NewsSource
import io.reactivex.Single
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response


class NewsTestData {

    private val mNewsArticles = ArrayList<NewsArticle>()
    val newsArticles get() = mNewsArticles

    init { createNewsArticle() }

    private fun createNewsArticle() {
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
        newsArticles.add(newsArticle1)
        newsArticles.add(newsArticle2)
    }

    fun createSuccessNewsResponseObservable(): Single<Response<NewsResponse>> {
        val response: Response<NewsResponse> = Response.success(
            200,
            NewsResponse(
                articles = newsArticles,
                status = "ok",
                totalResults = 2
            )
        )
        return Single.create { emitter -> emitter.onSuccess(response) }
    }


    fun createErrorNewsResponseObservable(): Single<Response<NewsResponse>> {
        val response: Response<NewsResponse> = Response.error(
            "null".toResponseBody("application/json".toMediaTypeOrNull()),

            okhttp3.Response.Builder()
                .code(400)
                .message("bad-request")
                .addHeader("content-type", "application/json")
                .request(Request.Builder().url("http://localhost/").build())
                .protocol(Protocol.HTTP_1_1)
                .build()
        )
        return Single.create{emitter -> emitter.onSuccess(response)}
    }
}