package com.sesang06.lightnovellist.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.sesang06.lightnovellist.model.LightNovel
import kotlinx.android.synthetic.main.item_light_novel.view.*

class LightNovelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val titleTextView by lazy { itemView.title_text_view }
    private val thumbnailImageView by lazy { itemView.thumbnail_image_view }
    private val descriptionTextView by lazy { itemView.description_text_view }
    private val authorPublisherTextView by lazy { itemView.author_publisher_text_view }
//    private val categoryTextView by lazy { itemView.category_text_view }
    private val publicationdDateTextView by lazy { itemView.publication_date_text_view }

    fun reset() {

    }
    fun bind(lightNovel: LightNovel) {
        Glide
            .with(itemView.context)
            .load(lightNovel.thumbnail)
            .into(thumbnailImageView)
        titleTextView.text = lightNovel.title
        descriptionTextView.text = lightNovel.description
        val authorPublisher = lightNovel.author.name + " | " + lightNovel.publisher.name
        authorPublisherTextView.text = authorPublisher
        val sdf = java.text.SimpleDateFormat("yyyy년 MM월 dd일")
        val publicationDateString = sdf.format(lightNovel.publicationDate)
        publicationdDateTextView.text = publicationDateString
    }
}