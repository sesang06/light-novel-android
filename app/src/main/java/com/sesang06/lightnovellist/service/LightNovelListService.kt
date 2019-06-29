package com.sesang06.lightnovellist.service

import com.sesang06.lightnovellist.model.DataResponse
import com.sesang06.lightnovellist.model.Empty
import com.sesang06.lightnovellist.model.LightNovelList
import com.sesang06.lightnovellist.model.LightNovelResponse
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

    @GET("search")
    fun search(@Query("query") query: String, @Query("offset") offset: Int): Observable<DataResponse<LightNovelList>>

    @FormUrlEncoded
    @POST("/token_info")
    fun tokenInfo(@Field("token") token: String): Observable<DataResponse<Empty>>
}
