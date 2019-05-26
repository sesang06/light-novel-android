package com.sesang06.lightnovellist.service

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

const val baseUrl = "http://192.168.200.147:4000/api/"
fun provideLightNovelListApi(): LightNovelListServiceApi
        = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(provideOkHttpClient(provideLoggingInterceptor()))
    .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
    .addConverterFactory(GsonConverterFactory.create())
    .build()
    .create(LightNovelListServiceApi::class.java)

private fun provideOkHttpClient(
    interceptor: HttpLoggingInterceptor): OkHttpClient
        = OkHttpClient.Builder()
    .run {

        addInterceptor(interceptor)
        build()
    }

private fun provideLoggingInterceptor(): HttpLoggingInterceptor
        = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
