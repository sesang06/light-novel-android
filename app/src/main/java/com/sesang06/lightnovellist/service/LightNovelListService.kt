package com.sesang06.lightnovellist.service

import com.sesang06.lightnovellist.model.*
import io.reactivex.Completable
import io.reactivex.Observable
import retrofit2.http.*

interface LightNovelListServiceApi {

    @GET("hit")
    fun hit(@Query("offset") offset: Int): Observable<DataResponse<LightNovelList>>


    @GET("recommend")
    fun recommend(@Query("offset") offset: Int): Observable<DataResponse<LightNovelList>>


    @GET("new")
    fun new(@Query("offset") offset: Int): Observable<DataResponse<LightNovelList>>

    @GET("light_novel/{id}")
    fun lightNovel(@Path("id") id: Int): Observable<DataResponse<LightNovelResponse>>

    @GET("comic/{id}")
    fun comic(@Path("id") id: Int): Observable<DataResponse<ComicResponse>>

    @GET("search")
    fun search(@Query("query") query: String, @Query("offset") offset: Int): Observable<DataResponse<LightNovelList>>

    @FormUrlEncoded
    @POST("token_info")
    fun tokenInfo(@Field("token") token: String): Completable

    @GET("comic")
    fun comic(@Query("offset") offset: Int, @Query("order") order: String): Observable<DataResponse<LightNovelList>>
}
