package com.sesang06.lightnovellist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sesang06.lightnovellist.service.LightNovelListServiceApi


class CategoryFilterViewModelFactory(val api: LightNovelListServiceApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return CategoryFilterViewModel(api) as T
    }
}