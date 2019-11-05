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
    @SerializedName("thumbnail")
    val thumbnail: String = "",
    @SerializedName("description")
    val description: String = "",
    @SerializedName("light_novels")
    private val lightNovels: List<LightNovel> = listOf(),
    @SerializedName("comics")
    private val comics: List<LightNovel> = listOf(),
    private val categories: List<String> = listOf()
) {
    fun getBook(bookType: BookType): List<LightNovel> {
        when (bookType) {
            BookType.LIGHTNOVEL -> return lightNovels
            BookType.COMIC -> return comics
        }
    }

    val categoriesDescription: String
        get() {
            if (categories.isEmpty()) {
                return ""
            }

            val sb = StringBuilder()
            categories.forEach { category ->
                sb.append(category)
                sb.append(", ")
            }
            val length = sb.length

            return sb.substring(0, length - 2)
        }


}