package com.sesang06.lightnovellist.service

import com.sesang06.lightnovellist.model.DataResponse
import com.sesang06.lightnovellist.model.LightNovelList
import com.sesang06.lightnovellist.model.LightNovelResponse
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface LightNovelListServiceApi {

    @GET("hit")
    fun hit(@Query("last_id") lastId: Int): Observable<DataResponse<LightNovelList>>


    @GET("recommend")
    fun recommend(@Query("last_id") lastId: Int): Observable<DataResponse<LightNovelList>>


    @GET("new")
    fun new(@Query("last_id") lastId: Int): Observable<DataResponse<LightNovelList>>

    @GET("light_novel")
    fun lightNovel(@Query("id") id: Int): Observable<DataResponse<LightNovelResponse>>

    @GET("search")
    fun search(@Query("query") query: String): Observable<DataResponse<LightNovelList>>

}
