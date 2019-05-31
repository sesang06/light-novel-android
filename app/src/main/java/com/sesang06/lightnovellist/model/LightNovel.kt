package com.sesang06.lightnovellist.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class LightNovel(@SerializedName("publisher_id")
                      val publisherId: Int = 0,
                      @SerializedName("thumbnail")
                      val thumbnail: String = "",
                      @SerializedName("author")
                      val author: Author,
                      @SerializedName("description")
                      val description: String = "",
                      @SerializedName("created_at")
                      val createdAt: String = "",
                      @SerializedName("title")
                      val title: String = "",
                      @SerializedName("hit_rank")
                      val hitRank: Int = 0,
                      @SerializedName("updated_at")
                      val updatedAt: Date = Date(),
                      @SerializedName("publication_date")
                      val publicationDate: Date = Date(),
                      @SerializedName("publisher")
                      val publisher: Publisher,
                      @SerializedName("id")
                      val id: Int = 0,
                      @SerializedName("author_id")
                      val authorId: Int = 0)