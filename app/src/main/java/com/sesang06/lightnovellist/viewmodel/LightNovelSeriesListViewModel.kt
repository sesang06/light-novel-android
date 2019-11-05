package com.sesang06.lightnovellist.viewmodel

import androidx.lifecycle.ViewModel
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.model.LightNovelSeries
import com.sesang06.lightnovellist.response.CategoryListResponse
import com.sesang06.lightnovellist.response.DataResponse
import com.sesang06.lightnovellist.response.LightNovelListResponse
import com.sesang06.lightnovellist.response.LightNovelSeriesListResponse
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.lang.StringBuilder

interface LightNovelSeriesListViewModelType {

    val lightNovelSeriesList: BehaviorSubject<List<LightNovelSeries>>
    val isLoading: BehaviorSubject<Boolean>
    val isLastPage: BehaviorSubject<Boolean>
    val categories: BehaviorSubject<List<CategorySelectModel>>
    val categoriesText: Observable<String>

    fun load(): Disposable
    fun loadMore(): Disposable
    fun filter(categorySelectModels: List<CategorySelectModel>): Disposable

}

class LightNovelSeriesListViewModel(
    val api: LightNovelListServiceApi
) : ViewModel(), LightNovelSeriesListViewModelType {
    override val lightNovelSeriesList: BehaviorSubject<List<LightNovelSeries>> = BehaviorSubject.create()
    override val isLastPage: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    override val isLoading: BehaviorSubject<Boolean> = BehaviorSubject.createDefault(false)
    override val categories: BehaviorSubject<List<CategorySelectModel>> = BehaviorSubject.createDefault(listOf())

    override val categoriesText: Observable<String>
        get() {
            val text = categories.map { lists ->
                val selectedList = lists.filter { it.isSelected }
                if (selectedList.isNullOrEmpty()) {
                    return@map "전체 보기"
                }
                val stringBuilder = StringBuilder()

                selectedList
                    .map { it.category }
                    .forEach { stringBuilder.append(it)
                        stringBuilder.append(",")
                    }
                stringBuilder.deleteCharAt(stringBuilder.length - 1)
                val filteredString = stringBuilder.toString()
                return@map filteredString

            }
            return text.map {
                "카테고리 : $it"
            }

        }
    override fun loadMore(): Disposable {
        val offset = lightNovelSeriesList.value?.size ?: 0
        val request = api.lightNovelSeriesList(offset, currentFilter())
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
        val disposable = CompositeDisposable()

        disposable.add(
            api.lightNovelSeriesList(0, null)
                .map { response: DataResponse<LightNovelSeriesListResponse> ->
                    response.data.list
                }
                .doOnSubscribe { isLoading.onNext(true) }
                .doOnTerminate { isLoading.onNext(false) }
                .subscribe({ items ->
                    lightNovelSeriesList.onNext(items)
                }, {

                })
        )
        disposable.add(
            api.categoryList()
                .map { response: DataResponse<CategoryListResponse> ->
                    response.data.categories
                }
                .map {
                    it.map {
                        CategorySelectModel(it)
                    }
                }
                .subscribe({ items ->
                    categories.onNext(items)
                }, {

                })

        )
        return disposable

    }

    override fun filter(categorySelectModels: List<CategorySelectModel>): Disposable {
        categories.onNext(categorySelectModels)

        val disposable = CompositeDisposable()
        val filtered = getFilterString(categorySelectModels)
        disposable.add(
            api.lightNovelSeriesList(0, filtered)
                .map { response: DataResponse<LightNovelSeriesListResponse> ->
                    response.data.list
                }
                .doOnSubscribe { isLoading.onNext(true) }
                .doOnTerminate { isLoading.onNext(false) }
                .subscribe({ items ->
                    lightNovelSeriesList.onNext(items)
                }, {

                })
        )
        return disposable
    }


    private fun currentFilter(): String? {
        val lastValue = categories.value
        if (lastValue.isNullOrEmpty()) {
            return null
        }
        return getFilterString(lastValue)
    }

    private fun getFilterString(categorySelectModels: List<CategorySelectModel>): String? {
        val selectedModel = categorySelectModels.filter { it.isSelected }

        if (selectedModel.isNullOrEmpty()) {
            return null
        }
        val stringBuilder = StringBuilder()

        selectedModel
            .map { it.category }
            .forEach { stringBuilder.append(it)
                stringBuilder.append(",")
            }
        stringBuilder.deleteCharAt(stringBuilder.length - 1)
        return stringBuilder.toString()

    }
}