package com.sesang06.lightnovellist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.viewholder.LightNovelViewHolder
import com.sesang06.lightnovellist.model.LightNovel

class LightNovelAdapter : RecyclerView.Adapter<LightNovelViewHolder>() {

    private var items: MutableList<LightNovel> = mutableListOf()

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightNovelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_light_novel, parent, false)
        return LightNovelViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: LightNovelViewHolder, position: Int) {
        items[position].let { lightNovel ->
            with(holder.itemView) {

            }
            holder.bind(lightNovel)
        }
//        holder.bind(lightNovelList.get(position))
        return
    }

    fun clearItems() {
        this.items.clear()
    }

    fun setItems(items: List<LightNovel>) {
        this.items = items.toMutableList()
    }
}