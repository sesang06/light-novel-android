package com.sesang06.lightnovellist.controller

import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.BookType

class RecommendLightNovelListFragment : LightNovelListFragment() {
    override val loadType: LoadType
        get() = LoadType.RECOMMEND
    override val bookType: BookType
        get() = BookType.LIGHTNOVEL
}