package com.sesang06.lightnovellist.response

import com.google.gson.annotations.SerializedName
import com.sesang06.lightnovellist.model.LightNovelSeries

data class LightNovelSeriesListResponse(
    @SerializedName("is_last_page")
    val isLastPage: Boolean = false,
    @SerializedName("list")
    val list: List<LightNovelSeries> = listOf(),
    @SerializedName("last_page")
    val lastPage: Int = 0
)