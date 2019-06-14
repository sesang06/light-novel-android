package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.DataResponse
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.model.LightNovelList
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

interface SearchLightNovelViewModelType {
    val lightNovels: BehaviorSubject<List<LightNovel>>
    val isLoading: BehaviorSubject<Boolean>
    val isLastPage: BehaviorSubject<Boolean>

    val previewLightNovels: Observable<List<LightNovel>>

    fun preview(query: String)
    fun load(query: String): Disposable
}

class SearchLightNovelViewModel(val api: LightNovelListServiceApi) : ViewModel(),
    SearchLightNovelViewModelType {

    override val previewLightNovels: Observable<List<LightNovel>>
    get() =
    query.throttleLast(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
    .flatMap {
            query -> api.search(query)
    }
    .map { response: DataResponse<LightNovelList> ->
        response.data.list
    }
    .doOnSubscribe { isLoading.onNext(true) }
    .doOnTerminate { isLoading.onNext(false) }

    override val lightNovels: BehaviorSubject<List<LightNovel>> = BehaviorSubject.create()
    override val isLastPage: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    override val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    val query: PublishSubject<String> = PublishSubject.create()


    override fun preview(query: String) {
        if (query.isNotEmpty()) {
            this.query.onNext(query)
        }
    }

    override fun load(query: String): Disposable {
        val compositeDisposable = CompositeDisposable()

        if (query.isNotEmpty()) {
            val request = api.search(query)
                .doOnSubscribe { isLoading.onNext(true) }
                .doOnTerminate { isLoading.onNext(false) }
                .share()
            compositeDisposable.add(
                request
                    .map { response: DataResponse<LightNovelList> ->
                        response.data.list
                    }
//                    .withLatestFrom(lightNovels, BiFunction { append: List<LightNovel>, current: List<LightNovel> ->
//                        current + append
//                    })
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
        }
        return compositeDisposable
    }
}