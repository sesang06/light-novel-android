package com.sesang06.lightnovellist.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.viewholder.LightNovelRecommendViewHolder
import com.sesang06.lightnovellist.adapter.viewholder.LightNovelViewHolder
import com.sesang06.lightnovellist.model.LightNovel

class LightNovelRecommendAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<LightNovelRecommendViewHolder>() {

    private var items: MutableList<LightNovel> = mutableListOf()

    private var listener: ItemClickListener? = null


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightNovelRecommendViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_light_novel_recommend, parent, false)
        return LightNovelRecommendViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: LightNovelRecommendViewHolder, position: Int) {
        items[position].let { lightNovel ->
            with(holder.itemView) {
                setOnClickListener { listener?.onItemClick(lightNovel) }
            }
            holder.bind(lightNovel)
        }
        return
    }

    fun clearItems() {
        this.items.clear()
    }

    fun setItems(items: List<LightNovel>) {
        this.items = items.toMutableList()
    }


    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun onItemClick(lightNovel: LightNovel)
    }
}