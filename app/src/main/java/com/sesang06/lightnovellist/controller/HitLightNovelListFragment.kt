package com.sesang06.lightnovellist.controller

import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.BookType

class HitLightNovelListFragment : LightNovelListFragment() {
    override val loadType: LoadType
        get() = LoadType.HIT

    override val bookType: BookType
        get() = BookType.LIGHTNOVEL
}