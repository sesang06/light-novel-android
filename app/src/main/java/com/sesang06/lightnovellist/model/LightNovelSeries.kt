package com.sesang06.lightnovellist.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class LightNovelSeries(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("title")
    val title: String = "",
    @SerializedName("aladin_id")
    val aladinId: Int = 0,
    @SerializedName("light_novels")
    val lightNovels: List<LightNovel> = listOf()
)