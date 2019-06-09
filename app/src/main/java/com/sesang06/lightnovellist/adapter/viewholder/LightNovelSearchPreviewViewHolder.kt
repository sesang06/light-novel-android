package com.sesang06.lightnovellist.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.sesang06.lightnovellist.model.LightNovel
import kotlinx.android.synthetic.main.item_light_novel_search_preview.view.*

class LightNovelSearchPreviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView by lazy { itemView.title_text_view }


    fun bind(lightNovel: LightNovel) {

        titleTextView.text = lightNovel.title

    }
}