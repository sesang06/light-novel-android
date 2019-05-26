package com.sesang06.lightnovellist.model

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp
import java.util.*
import kotlin.collections.ArrayList

class LightNovel(val id: Int) {

    @SerializedName("title")
    lateinit var title: String

    @SerializedName("description")
    lateinit var description: String

    @SerializedName("created_at")
    lateinit var createdAt: Timestamp

    @SerializedName("thumbnail")
    lateinit var thumnbnailURL: String

    @SerializedName("author")
    lateinit var author: Author

    @SerializedName("publisher")
    lateinit var publisher: Publisher

    @SerializedName("publication_date")
    lateinit var publicationDate: Date

    companion object {
        fun sampleLightNovel(): List<LightNovel> {
            var lightNovels: ArrayList<LightNovel> = ArrayList()
            lightNovels.add(bisco())
            lightNovels.add(osigoto())
            lightNovels.add(bisco())
            lightNovels.add(osigoto())
            return lightNovels
        }
        fun bisco(): LightNovel {
            val lightNovel = LightNovel(1)
            lightNovel.title = "녹을 먹는 비스코"
            lightNovel.thumnbnailURL = "https://image.aladin.co.kr/product/18760/48/cover150/k662635276_1.jpg"
            return lightNovel
        }
        fun osigoto(): LightNovel {
            val lightNovel = LightNovel(2)
            lightNovel.title = "용왕이 하는 일!"
            lightNovel.thumnbnailURL = "https://image.aladin.co.kr/product/18704/73/cover500/k602635871_1.jpg"
            return lightNovel

        }

    }
}