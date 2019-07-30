package com.sesang06.lightnovellist.controller

import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.BookType


class NewComicListFragment : LightNovelListFragment() {
    override val loadType: LoadType
        get() = LoadType.COMIC_NEW
    override val bookType: BookType
        get() = BookType.COMIC
}