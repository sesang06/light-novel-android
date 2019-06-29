package com.sesang06.lightnovellist.controller

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesang06.lightnovellist.LightNovelInfoActivity
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.LightNovelAdapter
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.rx.AutoClearedDisposable
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelListViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelListViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_light_novel.view.*

abstract class LightNovelListFragment : Fragment(), LightNovelAdapter.ItemClickListener {

    internal val adapter by lazy {
        LightNovelAdapter().apply { setItemClickListener(this@LightNovelListFragment) }
    }

    internal val disposeables = AutoClearedDisposable(this)
    private lateinit var scrollListener: RecyclerView.OnScrollListener
    internal val viewDisposables = AutoClearedDisposable(this, false)
    abstract internal val loadType: LoadType
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
        viewDisposables.add(
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
                        if (viewModel.isLastPage.value == false) {
                            view.light_novel_recycler_view.addOnScrollListener(scrollListener)
                        }
                    }
                }
        )
        viewModel.load()
        viewDisposables.add(
            viewModel.isLoading
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { isLoading ->
                    view.light_novel_progress_bar.visibility = when (isLoading) {
                        true -> View.VISIBLE
                        false -> View.GONE
                    }
                }
        )


        return view
    }

    override fun onItemClick(lightNovel: LightNovel) {
        val intent = Intent(this.context, LightNovelInfoActivity::class.java).apply {
            putExtra( LightNovelInfoActivity.KEY_ID, lightNovel.id)
        }
        this.context?.startActivity(intent)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
