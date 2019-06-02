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
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit

interface SearchLightNovelViewModelType {
    val lightNovels: Observable<List<LightNovel>>
    val isLoading: BehaviorSubject<Boolean>
    val isLastPage: BehaviorSubject<Boolean>

    fun load(query: String)
}

class SearchLightNovelViewModel(val api: LightNovelListServiceApi) : ViewModel(),
    SearchLightNovelViewModelType {

    override val lightNovels: Observable<List<LightNovel>>
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

    override val isLastPage: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    override val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    val query: PublishSubject<String> = PublishSubject.create()


    override fun load(query: String) {
        if (query.isNotEmpty()) {
            this.query.onNext(query)
        }
    }
}