package com.sesang06.lightnovellist.service

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import com.sesang06.lightnovellist.BuildConfig

class UserAgentIntercepter() : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain)
            : Response = with(chain) {

        val newRequest = request().newBuilder().run {
            addHeader("User-Agent", userAgent)
            build()
        }
        proceed(newRequest)
    }

    private val product = "LightNovelList"
    private val type = "Android"
    private val version: String
    get() {
        return BuildConfig.VERSION_NAME
    }

    private val userAgent: String
    get () {
        return "$product/$type/$version"
    }
}
