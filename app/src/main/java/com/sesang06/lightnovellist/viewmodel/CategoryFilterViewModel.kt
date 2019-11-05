package com.sesang06.lightnovellist.viewmodel

import androidx.lifecycle.ViewModel
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface CategoryFilterViewModelType {
    val categorySelectModelList: BehaviorSubject<List<CategorySelectModel>>

    fun load(categoryList: List<CategorySelectModel>)

    fun select(position: Int)

    fun submit()

    val finalCategorySelectModelList: PublishSubject<List<CategorySelectModel>>

}

class CategoryFilterViewModel(
    val api: LightNovelListServiceApi
) : ViewModel(),
    CategoryFilterViewModelType {


    override val categorySelectModelList: BehaviorSubject<List<CategorySelectModel>>
        = BehaviorSubject.create()

    override val finalCategorySelectModelList: PublishSubject<List<CategorySelectModel>>
        = PublishSubject.create()

    override fun load(categoryList: List<CategorySelectModel>) {

        categorySelectModelList.onNext(categoryList)
    }


    override fun select(position: Int) {

        val categoryModels = categorySelectModelList.value?.map {
                categorySelectModel ->
            categorySelectModel.copy()
        }
        if (categoryModels != null) {
            categoryModels[position].isSelected = !categoryModels[position].isSelected
            categorySelectModelList.onNext(categoryModels)
        }
    }


    override fun submit() {
        val lastValue = categorySelectModelList.value
        if (lastValue != null) {
            finalCategorySelectModelList.onNext(lastValue)
        }
    }
}