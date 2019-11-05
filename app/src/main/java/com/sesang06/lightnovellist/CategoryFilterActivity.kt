package com.sesang06.lightnovellist

import android.app.Activity
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import com.sesang06.lightnovellist.adapter.CategoryFilterAdapter
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.CategoryFilterViewModel
import com.sesang06.lightnovellist.viewmodel.CategoryFilterViewModelFactory
import com.sesang06.lightnovellist.viewmodel.CategorySelectModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_category_filter.*
import kotlinx.android.synthetic.main.activity_light_novel_info.toolbar
import kotlinx.android.synthetic.main.fragment_light_novel.view.*

class CategoryFilterActivity : AppCompatActivity(), CategoryFilterAdapter.ItemClickListener {


    companion object {
        const val CATEGORY_LIST = "category_list"
    }
    private val compositeDisposable = CompositeDisposable()

    lateinit var viewModel: CategoryFilterViewModel

    private val categoryLayoutManager by lazy {
        GridLayoutManager(this, 4)
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
        toolbar.title = "카테고리 변경"
        toolbar.subtitle = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val categoryList = intent.extras.getParcelableArrayList<CategorySelectModel>(CATEGORY_LIST)

        viewModel = ViewModelProviders.of(
            this@CategoryFilterActivity,
            viewModelFactory
        )[CategoryFilterViewModel::class.java]

        category_filter_recycler_view.adapter = adapter
        category_filter_recycler_view.layoutManager = categoryLayoutManager


        categoryList?.toList()?.let {
            viewModel.load(it)
        }

        compositeDisposable.add(
            viewModel.categorySelectModelList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { items ->
                    with(adapter) {
                        if (items.isEmpty()) {
                            clearItems()
                        } else {
                            setItems(items)
                        }
                        notifyDataSetChanged()
                    }
                }, {})
        )

        category_filter_submit_button.setOnClickListener {
            viewModel.submit()
        }

        compositeDisposable.add(
            viewModel.finalCategorySelectModelList
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { items ->
                    val intent = Intent()
                    val arrayList = ArrayList(items)
                    intent.putParcelableArrayListExtra(CATEGORY_LIST, arrayList)
                    setResult(Activity.RESULT_OK, intent)
                    finish()
                }, {})
        )


    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> this.onBackPressed()
          }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

}