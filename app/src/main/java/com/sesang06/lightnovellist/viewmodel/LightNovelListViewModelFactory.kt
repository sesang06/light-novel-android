package com.sesang06.lightnovellist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.service.LightNovelListServiceApi

class LightNovelListViewModelFactory(val api: LightNovelListServiceApi, val loadType: LoadType) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LightNovelListViewModel(api, loadType) as T
    }
}