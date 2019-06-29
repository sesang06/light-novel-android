package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.DataResponse
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.model.LightNovelList
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

interface LightNovelListViewModelType {
    val lightNovels: BehaviorSubject<List<LightNovel>>
    //    var message: PublishSubject<String>
    val isLoading: BehaviorSubject<Boolean>
    val isLastPage: BehaviorSubject<Boolean>

    fun request(lastId: Int): Observable<DataResponse<LightNovelList>>
    fun load(): Disposable
    fun loadMore(): Disposable
}

class LightNovelListViewModel(val api: LightNovelListServiceApi, val loadType: LoadType) : ViewModel(),
    LightNovelListViewModelType {
    override val lightNovels: BehaviorSubject<List<LightNovel>> = BehaviorSubject.create()
    override val isLastPage: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    override val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override fun request(offset: Int): Observable<DataResponse<LightNovelList>> {
        return when (loadType) {
            LoadType.HIT -> api.hit(offset)
            LoadType.RECOMMEND -> api.recommend(offset)
            LoadType.NEW -> api.new(offset)
        }
    }

    override fun loadMore(): Disposable {
        val offset = lightNovels.value?.size ?: 0
        val request = request(offset)
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnTerminate { isLoading.onNext(false) }
            .share()
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
        request
            .map { response: DataResponse<LightNovelList> ->
                response.data.list
            }
            .withLatestFrom(lightNovels, BiFunction { append: List<LightNovel>, current: List<LightNovel> ->
                current + append
            })
            .subscribe { items ->
                lightNovels.onNext(items)
            }
        )
        compositeDisposable.add(
        request
            .map  { response: DataResponse<LightNovelList> ->
                response.data.isLastPage
            }
            .subscribe { value ->
                isLastPage.onNext(value)
            }
        )
        return compositeDisposable
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