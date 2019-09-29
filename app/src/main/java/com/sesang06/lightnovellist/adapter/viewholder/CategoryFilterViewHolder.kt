package com.sesang06.lightnovellist.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.viewmodel.CategorySelectModel
import kotlinx.android.synthetic.main.item_category_select.view.*

class CategoryFilterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


    private val categoryTextView by lazy { itemView.category_text_view }


    fun reset() {

    }

    fun bind(categorySelectModel: CategorySelectModel) {
        categoryTextView.text = categorySelectModel.category
        itemView.isSelected = categorySelectModel.isSelected
    }
}