package com.sesang06.lightnovellist.service

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class AuthInterceptor(private val id: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain)
            : Response = with(chain) {
        val newRequest = request().newBuilder().run {
            addHeader("FIREBASE-ID", id)
            build()
        }
        proceed(newRequest)
    }
}
