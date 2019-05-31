package com.sesang06.lightnovellist.controller

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.LightNovelAdapter
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.rx.AutoClearedDisposable
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelListViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelListViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_light_novel.view.*

abstract class LightNovelListFragment : Fragment() {

    internal val adapter by lazy {
        LightNovelAdapter()
    }

    internal val disposeables = AutoClearedDisposable(this)
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    internal val viewDisposables = AutoClearedDisposable(this, false)
    abstract internal val loadType: LoadType
    private val lastVisibleItemPosition: Int
        get() = layoutManager.findLastVisibleItemPosition()
    internal val viewModelFactory by lazy {
        LightNovelListViewModelFactory(
            provideLightNovelListApi(), loadType
        )
    }
    internal val layoutManager by lazy {
        LinearLayoutManager(this.context)
    }
    lateinit var viewModel: LightNovelListViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_light_novel, container, false)
        view.light_novel_recycler_view.layoutManager = layoutManager
        view.light_novel_recycler_view.adapter = this@LightNovelListFragment.adapter


        viewModel = ViewModelProviders.of(this, viewModelFactory)[LightNovelListViewModel::class.java]


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
        viewModel.lightNovels
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { items ->
                with(adapter) {
                    if (items.isEmpty()) {
                        clearItems()
                    } else {
                        setItems(items)
                    }
                    notifyDataSetChanged()
                    view.light_novel_recycler_view.addOnScrollListener(scrollListener)
                }
            }
        viewModel.load()
        viewModel.isLoading
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { isLoading ->
                view.light_novel_progress_bar.visibility = when (isLoading) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
            }


        return view
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
