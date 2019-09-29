package com.sesang06.lightnovellist.response

import com.google.gson.annotations.SerializedName

data class DataResponse<T>(
    @SerializedName("code")
    val code: Int = 0,
    @SerializedName("data")
    val data: T,
    @SerializedName("message")
    val message: String = ""
)

class Empty()