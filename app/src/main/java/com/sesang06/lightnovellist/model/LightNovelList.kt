package com.sesang06.lightnovellist.model

import com.google.gson.annotations.SerializedName

data class LightNovelList(@SerializedName("is_last_page")
                val isLastPage: Boolean = false,
                @SerializedName("list")
                val list: List<LightNovel> = listOf())