package com.sesang06.lightnovellist.controller

import com.sesang06.lightnovellist.adapter.LoadType

class NewLightNovelListFragment : LightNovelListFragment() {
    override val loadType: LoadType
        get() = LoadType.NEW
}