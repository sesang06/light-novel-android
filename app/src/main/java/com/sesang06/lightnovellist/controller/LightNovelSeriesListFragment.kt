package com.sesang06.lightnovellist.controller

import android.app.Activity.RESULT_OK
import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sesang06.lightnovellist.CategoryFilterActivity
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.SeriesInfoActivity
import com.sesang06.lightnovellist.adapter.LightNovelSeriesAdapter
import com.sesang06.lightnovellist.adapter.LightNovelSeriesMainAdapter
import com.sesang06.lightnovellist.model.LightNovelSeries
import com.sesang06.lightnovellist.rx.AutoClearedDisposable
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.CategorySelectModel
import com.sesang06.lightnovellist.viewmodel.LightNovelSeriesListViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelSeriesListViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_light_novel.view.*
import kotlinx.android.synthetic.main.fragment_light_novel_series_list.view.*

class LightNovelSeriesListFragment : androidx.fragment.app.Fragment(), LightNovelSeriesMainAdapter.ItemClickListener {


    companion object {
        const val SERIES_SELECT = 1000;
    }


    internal val adapter by lazy {
        LightNovelSeriesMainAdapter().apply {
            setItemClickListener(this@LightNovelSeriesListFragment)
        }
    }

    internal val disposeables = AutoClearedDisposable(this)

    private lateinit var scrollListener: androidx.recyclerview.widget.RecyclerView.OnScrollListener

    private var categories: ArrayList<CategorySelectModel> = ArrayList()

    internal val viewDisposables = AutoClearedDisposable(this, false)
    internal val viewModelFactory by lazy {
        LightNovelSeriesListViewModelFactory(
            provideLightNovelListApi()
        )
    }
    internal val layoutManager by lazy {
        androidx.recyclerview.widget.GridLayoutManager(this.context, 3)
    }
    lateinit var viewModel: LightNovelSeriesListViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_light_novel_series_list, container, false)
        view.light_novel_series_list_recycler_view.layoutManager = layoutManager
        view.light_novel_series_list_recycler_view.adapter = this@LightNovelSeriesListFragment.adapter


        viewModel = ViewModelProviders.of(this, viewModelFactory)[LightNovelSeriesListViewModel::class.java]


        scrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
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

        viewDisposables.add(
            viewModel.categories
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ categories ->
                    this.categories = ArrayList(categories)

                   }, {

                })
        )

        viewDisposables.add(
            viewModel.categoriesText
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ text ->
                    view.light_novel_category_text_view.text = text
                }, {

                })
        )

        view.light_novel_series_list_select_button.setOnClickListener {
            val intent = Intent(this.context, CategoryFilterActivity::class.java).apply {
                putParcelableArrayListExtra(
                    CategoryFilterActivity.CATEGORY_LIST,
                    this@LightNovelSeriesListFragment.categories
                )
            }
            this.startActivityForResult(intent, SERIES_SELECT)

        }


        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode != RESULT_OK) {
            return
        }

        if (requestCode != SERIES_SELECT) {
            return
        }
        val categories = data?.getParcelableArrayListExtra<CategorySelectModel>(CategoryFilterActivity.CATEGORY_LIST)

        if (categories != null) {
            viewDisposables.add(viewModel.filter(categories))
        }
    }



    override fun onItemClick(lightNovelSeries: LightNovelSeries) {
        var intent = Intent(this.context, SeriesInfoActivity::class.java).apply {
            putExtra(SeriesInfoActivity.KEY_ID, lightNovelSeries.id)
        }
        this.startActivity(intent)
    }

}
