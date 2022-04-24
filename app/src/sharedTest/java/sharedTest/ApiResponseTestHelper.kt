package sharedTest

import com.rupesh.kotlinrxjavaex.BuildConfig
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okio.buffer
import okio.source


object ApiResponseTestHelper {

    fun enqueueMockResponse(server: MockWebServer, fileName: String) {
        val inputStream = javaClass.classLoader!!.getResourceAsStream(fileName)
        val source = inputStream.source().buffer()
        val mockResponse = MockResponse()

        server.enqueue(mockResponse
            .setBody(source.readString(Charsets.UTF_8))
            .setHeader("content-type", "application/json")
            .setResponseCode(200)
        )
    }
}