package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import com.sesang06.lightnovellist.model.*
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject

interface LightNovelInfoViewModelType {
    val lightNovel: BehaviorSubject<LightNovel>
    val isLoading: BehaviorSubject<Boolean>

    fun load(id: Int, bookType: BookType): Disposable
}

class LightNovelInfoViewModel(val api: LightNovelListServiceApi) : ViewModel(), LightNovelInfoViewModelType {
    override val lightNovel: BehaviorSubject<LightNovel> = BehaviorSubject.create()
    override val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)

    override fun load(id: Int, bookType: BookType): Disposable {
       when (bookType) {
           BookType.LIGHTNOVEL -> return loadLightNovel(id)
           BookType.COMIC -> return loadComic(id)
       }

    }

    private fun loadLightNovel(id: Int): Disposable {
        return api.lightNovel(id)
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnTerminate { isLoading.onNext(false) }
            .map { response: DataResponse<LightNovelResponse> ->
                response.data.lightNovel
            }
            .subscribe( { item ->
                lightNovel.onNext(item)
            }, { throwable -> //handle error

            })
    }

    private fun loadComic(id: Int): Disposable {
        return api.comic(id)
            .doOnSubscribe { isLoading.onNext(true) }
            .doOnTerminate { isLoading.onNext(false) }
            .map { response: DataResponse<ComicResponse> ->
                response.data.comic
            }
            .subscribe( { item ->
                lightNovel.onNext(item)
            }, { throwable -> //handle error

            })
    }
}
