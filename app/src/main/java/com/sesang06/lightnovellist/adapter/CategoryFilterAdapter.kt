package com.sesang06.lightnovellist.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.viewholder.CategoryFilterViewHolder
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.viewmodel.CategorySelectModel

class CategoryFilterAdapter : androidx.recyclerview.widget.RecyclerView.Adapter<CategoryFilterViewHolder>() {

    private var items: MutableList<CategorySelectModel> = mutableListOf()

    private var listener: ItemClickListener? = null


    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFilterViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_category_select, parent, false)

        return CategoryFilterViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: CategoryFilterViewHolder, position: Int) {
        items[position].let { categorySelectModel ->
            with(holder.itemView) {
                setOnClickListener { listener?.onItemClick(position) }
            }
            holder.bind(categorySelectModel)
        }
        return
    }

    fun clearItems() {
        this.items.clear()
    }

    fun setItems(items: List<CategorySelectModel>) {
        this.items = items.toMutableList()
    }


    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun onItemClick(position: Int)
    }
}