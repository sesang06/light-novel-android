package com.sesang06.lightnovellist.response

import com.google.gson.annotations.SerializedName
import com.sesang06.lightnovellist.model.LightNovel

data class LightNovelResponse(
    @SerializedName("light_novel")
    val lightNovel: LightNovel
)

data class ComicResponse(
    @SerializedName("comic")
    val comic: LightNovel
)