package com.sesang06.lightnovellist.adapter.viewholder

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.sesang06.lightnovellist.model.LightNovel
import kotlinx.android.synthetic.main.item_light_novel_series.view.*


class LightNovelSeriesViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

    private val titleTextView by lazy { itemView.title_text_view }

    fun reset() {

    }
    fun bind(lightNovel: LightNovel) {
        titleTextView.text = lightNovel.title
    }
}