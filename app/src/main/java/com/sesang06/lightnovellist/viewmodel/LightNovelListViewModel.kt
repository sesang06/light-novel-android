package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.DataResponse
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.model.LightNovelList
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface LightNovelListViewModelType {
    val lightNovels: BehaviorSubject<List<LightNovel>>
//    var message: PublishSubject<String>
    fun load(): Disposable
}

class LightNovelListViewModel(val api: LightNovelListServiceApi, val loadType: LoadType): ViewModel(), LightNovelListViewModelType {
    override val lightNovels: BehaviorSubject<List<LightNovel>> = BehaviorSubject.create()

    override fun load(): Disposable {
        val request = when(loadType) {
            LoadType.HIT -> api.hit(0)
            LoadType.RECOMMEND -> api.recommend(0)
            LoadType.NEW -> api.new(0)
        }
       return request
           .map { response: DataResponse<LightNovelList> ->
               response.data.list
           }
            .subscribe({ items ->
                lightNovels.onNext(items)
            })

    }
}