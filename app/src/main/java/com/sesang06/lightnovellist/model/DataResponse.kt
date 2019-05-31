package com.sesang06.lightnovellist.model

import com.google.gson.annotations.SerializedName

data class DataResponse<T>(@SerializedName("code")
                        val code: Int = 0,
                                  @SerializedName("data")
                        val data: T,
                                  @SerializedName("message")
                        val message: String = "")