package com.sesang06.lightnovellist.service

import com.sesang06.lightnovellist.model.DataResponse
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.model.LightNovelList
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
}

class LightNovelListServiceDummyAPI: LightNovelListServiceApi {

    override fun new(lastIndex: Int): Observable<DataResponse<LightNovelList>> {
        val list = LightNovel.sampleLightNovel()
        val lightNovelList = LightNovelList(list)
        val data = DataResponse<LightNovelList>(200, "hello", lightNovelList)
        return Observable.just(data)
    }

    override fun hit(lastIndex: Int): Observable<DataResponse<LightNovelList>> {
        val list = LightNovel.sampleLightNovel()
        val lightNovelList = LightNovelList(list)
        val data = DataResponse<LightNovelList>(200, "hello", lightNovelList)
        return Observable.just(data)
    }

    override fun recommend(lastIndex: Int): Observable<DataResponse<LightNovelList>>{
        val list = LightNovel.sampleLightNovel()
        val lightNovelList = LightNovelList(list)
        val data = DataResponse<LightNovelList>(200, "hello", lightNovelList)
        return Observable.just(data)
    }
}