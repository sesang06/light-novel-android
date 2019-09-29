package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import com.sesang06.lightnovellist.service.LightNovelListServiceApi
import io.reactivex.subjects.BehaviorSubject

interface CategoryFilterViewModelType {
    val categorySelectModelList: BehaviorSubject<List<CategorySelectModel>>

    fun load(categoryList: List<String>)

    fun select(position: Int)

}

class CategoryFilterViewModel(
    val api: LightNovelListServiceApi
) : ViewModel(),
    CategoryFilterViewModelType {


    override val categorySelectModelList: BehaviorSubject<List<CategorySelectModel>>
        = BehaviorSubject.create()


    override fun load(categoryList: List<String>) {
        val categoryModels = categoryList.map { category: String ->
            CategorySelectModel(category, false)
        }

        categorySelectModelList.onNext(categoryModels)
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


}