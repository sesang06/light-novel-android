package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.model.LightNovelSeries
import com.sesang06.lightnovellist.response.DataResponse
import com.sesang06.lightnovellist.response.LightNovelListResponse
import com.sesang06.lightnovellist.response.LightNovelSeriesListResponse
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject

interface LightNovelSeriesListViewModelType {

    val lightNovelSeriesList: BehaviorSubject<List<LightNovelSeries>>
    val isLoading: BehaviorSubject<Boolean>
    val isLastPage: BehaviorSubject<Boolean>
    fun load(): Disposable
    fun loadMore(): Disposable
}

class LightNovelSeriesListViewModel(
    val api: LightNovelListServiceApi
) : ViewModel(), LightNovelSeriesListViewModelType {
    override val lightNovelSeriesList: BehaviorSubject<List<LightNovelSeries>> = BehaviorSubject.create()
    override val isLastPage: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    override val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)


    override fun loadMore(): Disposable {
        val offset = lightNovelSeriesList.value?.size ?: 0
        val request = api.lightNovelSeriesList(offset, null)
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnTerminate { isLoading.onNext(false) }
            .share()
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            request
                .map { response: DataResponse<LightNovelSeriesListResponse> ->
                    response.data.list
                }
                .withLatestFrom(lightNovelSeriesList,
                    BiFunction { append: List<LightNovelSeries>, current: List<LightNovelSeries> ->
                        current + append
                    })
                .subscribe({ items ->
                    lightNovelSeriesList.onNext(items)
                }, {

                })
        )
        compositeDisposable.add(
            request
                .map { response: DataResponse<LightNovelSeriesListResponse> ->
                    response.data.isLastPage
                }
                .subscribe({ value ->
                    isLastPage.onNext(value)
                }, {

                })
        )
        return compositeDisposable
    }

    override fun load(): Disposable {
        return api.lightNovelSeriesList(0, null)
            .map { response: DataResponse<LightNovelSeriesListResponse> ->
                response.data.list
            }
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnTerminate { isLoading.onNext(false) }
            .subscribe({ items ->
                lightNovelSeriesList.onNext(items)
            }, {

            }
            )

    }
}