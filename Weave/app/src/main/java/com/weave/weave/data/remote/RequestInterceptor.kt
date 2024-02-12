package com.weave.weave.data.remote

import okhttp3.Interceptor
import okhttp3.Response


// HTTP FAILED: java.io.IOException: unexpected end of stream on [주소] 에러 방지용 Interceptor
class RequestInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val builder = chain.request().newBuilder()
        builder.addHeader("Connection", "close")

        return chain.proceed(builder.build())
    }
}