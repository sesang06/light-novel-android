package com.sesang06.lightnovellist.controller

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesang06.lightnovellist.CategoryFilterActivity
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.LightNovelSeriesMainAdapter
import com.sesang06.lightnovellist.rx.AutoClearedDisposable
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelSeriesListViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelSeriesListViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_light_novel.view.*
import kotlinx.android.synthetic.main.fragment_light_novel_series_list.view.*

class LightNovelSeriesListFragment : Fragment() {


    internal val adapter by lazy {
        LightNovelSeriesMainAdapter().apply {
            //            setItemClickListener(this@LightNovelSeriesListFragment)
        }
    }

    internal val disposeables = AutoClearedDisposable(this)

    private lateinit var scrollListener: RecyclerView.OnScrollListener

    internal val viewDisposables = AutoClearedDisposable(this, false)
    internal val viewModelFactory by lazy {
        LightNovelSeriesListViewModelFactory(
            provideLightNovelListApi()
        )
    }
    internal val layoutManager by lazy {
        GridLayoutManager(this.context, 3)
    }
    lateinit var viewModel: LightNovelSeriesListViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_light_novel_series_list, container, false)
        view.light_novel_series_list_recycler_view.layoutManager = layoutManager
        view.light_novel_series_list_recycler_view.adapter = this@LightNovelSeriesListFragment.adapter


        viewModel = ViewModelProviders.of(this, viewModelFactory)[LightNovelSeriesListViewModel::class.java]


        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = layoutManager.itemCount
                if (totalItemCount == layoutManager.findLastVisibleItemPosition() + 1) {
                    if (viewModel.isLoading.value == false) {
                        viewModel.loadMore()
                        recyclerView.removeOnScrollListener(scrollListener)
                    }

                }
            }
        }
        viewDisposables.add(
            viewModel.lightNovelSeriesList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { items ->
                    with(adapter) {
                        if (items.isEmpty()) {
                            clearItems()
                        } else {
                            setItems(items)
                        }
                        notifyDataSetChanged()
                        if (viewModel.isLastPage.value == false) {
                            view.light_novel_series_list_recycler_view.addOnScrollListener(scrollListener)
                        }
                    }
                }
        )
        viewModel.load()
        viewDisposables.add(
            viewModel.isLoading
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ isLoading ->
                    view.light_novel_series_list_progress_bar.visibility = when (isLoading) {
                        true -> View.VISIBLE
                        false -> View.GONE
                    }
                }, {

                })
        )

        val intent = Intent(this.context, CategoryFilterActivity::class.java)
        this.context?.startActivity(intent)

        return view
    }

//    override fun onItemClick(lightNovel: LightNovel) {
//        val intent = Intent(this.context, LightNovelInfoActivity::class.java).apply {
//            putExtra(LightNovelInfoActivity.KEY_ID, lightNovel.id)
//            putExtra(LightNovelInfoActivity.BOOK_TYPE, bookType.ordinal)
//        }
//        this.context?.startActivity(intent)
//    }

}
