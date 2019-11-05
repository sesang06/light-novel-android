package com.sesang06.lightnovellist.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.sesang06.lightnovellist.model.LightNovelSeries
import kotlinx.android.synthetic.main.item_light_novel_series_main.view.*

class LightNovelSeriesMainViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    private val titleTextView by lazy { itemView.title_text_view }
    private val thumbnailImageView by lazy { itemView.thumbnail_image_view }
    private val categoriesTextView by lazy { itemView.categories_text_view }


    fun reset() {

    }
    fun bind(lightNovelSeries: LightNovelSeries) {
        Glide
            .with(itemView.context)
            .load(lightNovelSeries.thumbnail)
            .into(thumbnailImageView)
        titleTextView.text = lightNovelSeries.title
        categoriesTextView.text = lightNovelSeries.categoriesDescription
    }
}