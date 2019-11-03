package com.sesang06.lightnovellist.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.viewholder.LightNovelSeriesMainViewHolder
import com.sesang06.lightnovellist.adapter.viewholder.SeriesInfoNovelViewHolder
import com.sesang06.lightnovellist.model.LightNovel


class SeriesInfoNovelAdapter : RecyclerView.Adapter<SeriesInfoNovelViewHolder>() {

    private var items: MutableList<LightNovel> = mutableListOf()

    private var listener: ItemClickListener? = null


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesInfoNovelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_series_info_novel, parent, false)
        return SeriesInfoNovelViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: SeriesInfoNovelViewHolder, position: Int) {
        items[position].let { lightNovelSeries ->
            with(holder.itemView) {
                setOnClickListener { listener?.onItemClick(lightNovelSeries) }
            }
            holder.bind(lightNovelSeries)
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