package com.sesang06.lightnovellist.controller

import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.BookType

class NewLightNovelListFragment : LightNovelListFragment() {
    override val loadType: LoadType
        get() = LoadType.NEW
    override val bookType: BookType
        get() = BookType.LIGHTNOVEL
}