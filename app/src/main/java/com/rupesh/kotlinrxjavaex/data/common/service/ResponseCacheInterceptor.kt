package com.rupesh.kotlinrxjavaex.data.common.service

import android.util.Log
import com.rupesh.kotlinrxjavaex.presentation.util.AppConstPresentation
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

private class ResponseCacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        return if (Boolean.equals(request.header("ApplyResponseCache"))) {
            Log.i(AppConstPresentation.LOG_DATA,"Response cache applied")
            val originalResponse: Response = chain.proceed(chain.request())

            originalResponse.newBuilder()
                .removeHeader("ApplyResponseCache")
                .header("Cache-Control", "public, max-age=" + 60)
                .build()
        } else {
            Log.i(AppConstPresentation.LOG_DATA,"Response cache not applied")
            chain.proceed(chain.request())
        }
    }
}