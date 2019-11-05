package com.sesang06.lightnovellist.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.viewholder.LightNovelSearchResultViewHolder
import com.sesang06.lightnovellist.model.LightNovel

class LightNovelSearchResultAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<LightNovelSearchResultViewHolder>() {

    private var items: MutableList<LightNovel> = mutableListOf()

    private var listener: ItemClickListener? = null


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightNovelSearchResultViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_light_novel_search_result, parent, false)
        return LightNovelSearchResultViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: LightNovelSearchResultViewHolder, position: Int) {
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