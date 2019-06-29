package com.sesang06.lightnovellist.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class LightNovel(
    @SerializedName("publisher_id")
    val publisherId: Int = 0,
    @SerializedName("thumbnail")
    val thumbnail: String = "",
    @SerializedName("standard_price")
    val standardPrice: Int = 0,
    @SerializedName("author")
    val author: Author,
    @SerializedName("link")
    val link: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("created_at")
    val createdAt: Date = Date(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("aladin_id")
    val aladinId: Int = 0,
    @SerializedName("sales_point")
    val salesPoint: Int = 0,
    @SerializedName("hit_rank")
    val hitRank: Int = 0,
    @SerializedName("updated_at")
    val updatedAt: Date = Date(),
    @SerializedName("isbn13")
    val isbn: String = "",
    @SerializedName("sales_price")
    val salesPrice: Int = 0,
    @SerializedName("publication_date")
    val publicationDate: Date = Date(),
    @SerializedName("publisher")
    val publisher: Publisher,
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("recommend_rank")
    val recommendRank: Int = 0,
    @SerializedName("adult")
    val adult: Boolean = false,
    @SerializedName("author_id")
    val authorId: Int = 0,
    @SerializedName("publisher_description")
    val publisherDescription: String = "",
    @SerializedName("index_description")
    val indexDescription: String = "",
    @SerializedName("series")
    val series: LightNovelSeries
)
