package com.unicamp.dslearn.data.network.fake

import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody

class FakeResponseInterceptor() : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        if (chain.request().url.toUri().toString().contains("/search")) {
            val responseString = FakeSearchResponse.searchJson
            return chain.proceed(chain.request())
                .newBuilder()
                .code(200)
                .protocol(Protocol.HTTP_2)
                .message(responseString)
                .body(
                    responseString
                        .toByteArray()
                        .toResponseBody(
                            "application/json".toMediaTypeOrNull()
                        )
                )
                .addHeader("content-type", "application/json")
                .build()
        }
        return chain.proceed(chain.request())
    }
}