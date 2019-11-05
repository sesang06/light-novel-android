package com.sesang06.lightnovellist.response

import com.google.gson.annotations.SerializedName

data class CategoryListResponse(
    @SerializedName("categories")
    val categories: List<String> = listOf()
)