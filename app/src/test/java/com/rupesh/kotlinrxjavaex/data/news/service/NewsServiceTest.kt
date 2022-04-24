import com.google.common.truth.Truth.assertThat
import com.rupesh.kotlinrxjavaex.BuildConfig
import com.rupesh.kotlinrxjavaex.data.news.model.NewsResponse
import com.rupesh.kotlinrxjavaex.data.news.service.NewsService
import com.rupesh.kotlinrxjavaex.data.util.AppConstantsData
import io.reactivex.observers.TestObserver
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import sharedTest.ApiResponseTestHelper
import sharedTest.RxImmediateSchedulerRule

@RunWith(JUnit4::class)
class NewsServiceTest {

    @get:Rule
    var rxImmediateSchedulerRule: RxImmediateSchedulerRule = RxImmediateSchedulerRule()

    private lateinit var service: NewsService
    private lateinit var mockServer: MockWebServer
    private var mockUrl = "/v2/top-headlines"
    private lateinit var testObserver: TestObserver<Response<NewsResponse>>

    @Before
    fun setUp() {
        mockServer = MockWebServer()

        service = Retrofit.Builder()
            .baseUrl(mockServer.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NewsService::class.java)

        testObserver = TestObserver<Response<NewsResponse>>()
    }

    @Test
    fun `getTopHeadlines requestPath`() {
        ApiResponseTestHelper.enqueueMockResponse(mockServer, "news-response.json")

        service.getTopHeadlines(
            url = mockUrl,
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).subscribe(testObserver)
        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo("/v2/top-headlines?country=us&page=1&apiKey=${BuildConfig.API_NEWS}")
    }

    @Test
    fun `getTopHeadlines requestHeader and ResponseCode`() {
        ApiResponseTestHelper.enqueueMockResponse(mockServer, "news-response.json")

        service.getTopHeadlines(
            url = mockUrl,
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.headers()["content-type"]!! == "application/json"
            }
            .assertValue {
                return@assertValue it.code() == 200
            }
    }

    @Test
    fun `getTopHeadlines given CountryAndPage status and totalResults`() {
        ApiResponseTestHelper.enqueueMockResponse(mockServer, "news-response.json")

        service.getTopHeadlines(
            url = mockUrl,
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                return@assertValue it.body()?.status == "ok"
            }
            .assertValue {
                return@assertValue it.body()?.totalResults == 20
            }
    }

    @Test
    fun `getSearchedHeadlines requestPath`() {
        val searchKey = "cnn"
        mockUrl = "/v2/top-headlines"
        ApiResponseTestHelper.enqueueMockResponse(mockServer, "news-response.json")
        service.getSearchedHeadlines(
            url = mockUrl,
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).subscribe(testObserver)
        val request = mockServer.takeRequest()
        assertThat(request.path).isEqualTo("/v2/top-headlines?country=us&q=cnn&page=1&apiKey=${BuildConfig.API_NEWS}")
    }

    @Test
    fun getSearchedHeadlines_requestPath2 () {
        val searchKey = "cnn"
        val expectedNoOfSearchedArticles = 5

        ApiResponseTestHelper.enqueueMockResponse(mockServer, "news-search-response-cnn.json")
        service.getSearchedHeadlines(
            url = mockUrl,
            AppConstantsData.DEFAULT_COUNTRY_NEWS,
            searchKey,
            AppConstantsData.DEFAULT_PAGE_NEWS
        ).subscribe(testObserver)

        testObserver.await()
            .assertValue {
                var result = 0
                var containsSearchKey: Boolean
                for(article in it.body()!!.articles) {
                    containsSearchKey = when {
                        article.source!!.id.lowercase().contains(searchKey) -> true
                        article.source!!.name.lowercase().contains(searchKey) -> true
                        article.url!!.lowercase().contains(searchKey) -> true
                        article.description!!.lowercase().contains(searchKey) -> true
                        article.content!!.lowercase().contains(searchKey) -> true
                        else -> false
                    }
                    if (containsSearchKey) result++
                }
                return@assertValue result == expectedNoOfSearchedArticles
            }
    }

    @After
    fun tearDown() {
        testObserver.dispose()
        mockServer.shutdown()
    }
}