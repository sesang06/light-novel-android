package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import com.sesang06.lightnovellist.model.BookType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.model.LightNovelSeries
import com.sesang06.lightnovellist.response.ComicResponse
import com.sesang06.lightnovellist.response.DataResponse
import com.sesang06.lightnovellist.response.LightNovelResponse
import com.sesang06.lightnovellist.response.LightNovelSeriesResponse
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import java.util.*


interface SeriesInfoViewModelType {
    val series: BehaviorSubject<LightNovelSeries>
    val isLoading: BehaviorSubject<Boolean>
    val seriesTitle: Observable<String>
    fun load(id: Int): Disposable
}

class SeriesInfoViewModel(val api: LightNovelListServiceApi) : ViewModel(), SeriesInfoViewModelType {
    override val series: BehaviorSubject<LightNovelSeries> = BehaviorSubject.create()
    override val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override val seriesTitle: Observable<String>
        get() {
            return series.map { "${it.title} (총 ${it.getBook(BookType.LIGHTNOVEL).size}권)" }
        }
    override fun load(id: Int): Disposable {
        return api.lightNovelSeries(id)
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnTerminate { isLoading.onNext(false) }
            .map { response: DataResponse<LightNovelSeriesResponse> ->
                response.data.lightNovelSeries
            }
            .subscribe( { item ->
                series.onNext(item)
            }, { throwable -> //handle error

            })
    }

}
