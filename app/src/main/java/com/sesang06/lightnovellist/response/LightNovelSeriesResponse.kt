package com.sesang06.lightnovellist.response

import com.google.gson.annotations.SerializedName
import com.sesang06.lightnovellist.model.LightNovelSeries

data class LightNovelSeriesResponse(
    @SerializedName("light_novel_series")
    val lightNovelSeries: LightNovelSeries
)