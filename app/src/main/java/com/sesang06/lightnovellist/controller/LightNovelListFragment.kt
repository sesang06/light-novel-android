package com.sesang06.lightnovellist.controller

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.LightNovelAdapter
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.rx.AutoClearedDisposable
import com.sesang06.lightnovellist.service.LightNovelListServiceDummyAPI
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelListViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelListViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_light_novel.*
import kotlinx.android.synthetic.main.fragment_light_novel.view.*

abstract class LightNovelListFragment : Fragment() {

    internal val adapter by lazy {
        LightNovelAdapter()
    }

    internal val disposeables = AutoClearedDisposable(this)

    internal val viewDisposables = AutoClearedDisposable(this, false)
    abstract internal val loadType: LoadType

    internal val viewModelFactory by lazy {
        LightNovelListViewModelFactory(
            provideLightNovelListApi(), loadType
        )
    }
    lateinit var viewModel: LightNovelListViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_light_novel, container, false)
        view.light_novel_recycler_view.layoutManager = LinearLayoutManager(this.context)
        view.light_novel_recycler_view.adapter = this@LightNovelListFragment.adapter

        viewModel = ViewModelProviders.of(this, viewModelFactory)[LightNovelListViewModel::class.java]

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
                }
            }
        viewModel.load()

        return view
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
}
