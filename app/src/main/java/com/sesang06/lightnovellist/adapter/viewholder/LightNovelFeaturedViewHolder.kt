package com.sesang06.lightnovellist.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.sesang06.lightnovellist.model.LightNovel
import kotlinx.android.synthetic.main.item_light_novel_featured.view.*

class LightNovelFeaturedViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

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