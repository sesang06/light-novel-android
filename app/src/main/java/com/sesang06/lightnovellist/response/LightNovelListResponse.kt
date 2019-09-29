package com.sesang06.lightnovellist.response

import com.google.gson.annotations.SerializedName
import com.sesang06.lightnovellist.model.LightNovel

data class LightNovelListResponse(
    @SerializedName("is_last_page")
    val isLastPage: Boolean = false,
    @SerializedName("list")
    val list: List<LightNovel> = listOf()
)