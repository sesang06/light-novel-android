package com.sesang06.lightnovellist.model

import com.google.gson.annotations.SerializedName

data class LightNovelResponse(@SerializedName("light_novel")
                             val lightNovel: LightNovel)

data class ComicResponse(@SerializedName("comic")
                         val comic: LightNovel)