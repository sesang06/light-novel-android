package com.sesang06.lightnovellist.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.viewholder.LightNovelSeriesMainViewHolder
import com.sesang06.lightnovellist.model.LightNovelSeries

class LightNovelSeriesMainAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<LightNovelSeriesMainViewHolder>() {

    private var items: MutableList<LightNovelSeries> = mutableListOf()

    private var listener: ItemClickListener? = null


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LightNovelSeriesMainViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_light_novel_series_main, parent, false)
        return LightNovelSeriesMainViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: LightNovelSeriesMainViewHolder, position: Int) {
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

    fun setItems(items: List<LightNovelSeries>) {
        this.items = items.toMutableList()
    }


    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun onItemClick(lightNovelSeries: LightNovelSeries)
    }
}