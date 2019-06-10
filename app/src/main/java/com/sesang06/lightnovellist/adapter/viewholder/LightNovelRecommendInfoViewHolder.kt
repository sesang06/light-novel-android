package com.sesang06.lightnovellist.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.sesang06.lightnovellist.model.LightNovel
import kotlinx.android.synthetic.main.item_light_novel_recommend_info.view.*

class LightNovelRecommendInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView by lazy { itemView.title_text_view }
    private val thumbnailImageView by lazy { itemView.thumbnail_image_view }


    fun bind(lightNovel: LightNovel) {
        Glide
            .with(itemView.context)
            .load(lightNovel.thumbnail)
            .into(thumbnailImageView)
        titleTextView.text = lightNovel.title

    }
}