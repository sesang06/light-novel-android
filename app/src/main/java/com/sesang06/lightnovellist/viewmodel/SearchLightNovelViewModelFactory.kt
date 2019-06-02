package com.sesang06.lightnovellist.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.sesang06.lightnovellist.service.LightNovelListServiceApi

class SearchLightNovelViewModelFactory(val api: LightNovelListServiceApi) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return SearchLightNovelViewModel(api) as T
    }
}