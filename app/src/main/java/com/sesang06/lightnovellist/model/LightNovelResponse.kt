package com.sesang06.lightnovellist.model

import com.google.gson.annotations.SerializedName

data class LightNovelResponse(@SerializedName("light_novel")
                             val lightNovel: LightNovel)