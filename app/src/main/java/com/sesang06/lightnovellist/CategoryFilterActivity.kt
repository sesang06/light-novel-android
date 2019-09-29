package com.sesang06.lightnovellist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.sesang06.lightnovellist.adapter.CategoryFilterAdapter
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.CategoryFilterViewModel
import com.sesang06.lightnovellist.viewmodel.CategoryFilterViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_category_filter.*
import kotlinx.android.synthetic.main.activity_light_novel_info.toolbar
import kotlinx.android.synthetic.main.fragment_light_novel.view.*

class CategoryFilterActivity : AppCompatActivity(), CategoryFilterAdapter.ItemClickListener {

    private val compositeDisposable = CompositeDisposable()

    lateinit var viewModel: CategoryFilterViewModel

    private val categoryLayoutManager by lazy {
        LinearLayoutManager(this)
    }


    internal val adapter by lazy {
        CategoryFilterAdapter()
            .apply { setItemClickListener(this@CategoryFilterActivity) }
    }

    internal val viewModelFactory by lazy {
        CategoryFilterViewModelFactory(
            provideLightNovelListApi()
        )
    }

    override fun onItemClick(position: Int) {
        viewModel.select(position)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_filter)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = ""
        toolbar.subtitle = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(
            this@CategoryFilterActivity,
            viewModelFactory
        )[CategoryFilterViewModel::class.java]

        category_filter_recycler_view.adapter = adapter
        category_filter_recycler_view.layoutManager = categoryLayoutManager


        viewModel.load(listOf("A", "B", "C"))

        compositeDisposable.add(
            viewModel.categorySelectModelList
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
        )

    }


    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}