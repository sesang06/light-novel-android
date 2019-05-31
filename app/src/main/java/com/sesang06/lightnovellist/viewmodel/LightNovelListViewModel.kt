package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.DataResponse
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.model.LightNovelList
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

interface LightNovelListViewModelType {
    val lightNovels: BehaviorSubject<List<LightNovel>>
    //    var message: PublishSubject<String>
    val isLoading: BehaviorSubject<Boolean>

    fun request(lastId: Int): Observable<DataResponse<LightNovelList>>
    fun load(): Disposable
    fun loadMore(): Disposable
}

class LightNovelListViewModel(val api: LightNovelListServiceApi, val loadType: LoadType) : ViewModel(),
    LightNovelListViewModelType {
    override val lightNovels: BehaviorSubject<List<LightNovel>> = BehaviorSubject.create()

    override val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override fun request(lastId: Int): Observable<DataResponse<LightNovelList>> {
        return when (loadType) {
            LoadType.HIT -> api.hit(lastId)
            LoadType.RECOMMEND -> api.recommend(lastId)
            LoadType.NEW -> api.new(lastId)
        }
    }

    override fun loadMore(): Disposable {
        val lastId = lightNovels.value?.last()?.id ?: 0
        return request(lastId)
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnTerminate { isLoading.onNext(false) }
            .map { response: DataResponse<LightNovelList> ->
                response.data.list
            }
            .withLatestFrom(lightNovels, BiFunction { append: List<LightNovel>, current: List<LightNovel> ->
                current + append
            })
            .subscribe { items ->
                lightNovels.onNext(items)
            }

    }

    override fun load(): Disposable {
        return request(0)
            .map { response: DataResponse<LightNovelList> ->
                response.data.list
            }
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnTerminate { isLoading.onNext(false) }
            .subscribe { items ->
                lightNovels.onNext(items)
            }

    }
}