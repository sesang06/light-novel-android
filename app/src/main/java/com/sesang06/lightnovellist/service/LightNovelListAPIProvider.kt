package com.sesang06.lightnovellist.service

import com.google.firebase.iid.FirebaseInstanceId
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

const val baseUrl = "http://157.230.250.134:4000/api/"
fun provideLightNovelListApi(): LightNovelListServiceApi = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(provideOkHttpClient(provideLoggingInterceptor()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(LightNovelListServiceApi::class.java)

private fun provideOkHttpClient(
    interceptor: HttpLoggingInterceptor
): OkHttpClient = OkHttpClient.Builder()
    .run {

        val id = FirebaseInstanceId.getInstance().id
        val authInterceptor = AuthInterceptor(id)
        val userAuthInterceptor = UserAgentIntercepter()
        addInterceptor(interceptor)
        addInterceptor(authInterceptor)
        addInterceptor(userAuthInterceptor)
        build()
    }

private fun provideLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

