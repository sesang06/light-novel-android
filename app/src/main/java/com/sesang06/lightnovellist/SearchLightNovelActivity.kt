package com.sesang06.lightnovellist

import androidx.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.transition.Visibility
import android.view.View
import android.widget.SearchView
import com.sesang06.lightnovellist.adapter.LightNovelSearchPreviewAdapter
import com.sesang06.lightnovellist.adapter.LightNovelSearchResultAdapter
import com.sesang06.lightnovellist.model.BookType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.SearchLightNovelViewModel
import com.sesang06.lightnovellist.viewmodel.SearchLightNovelViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.activity_search.search_view
import kotlinx.android.synthetic.main.activity_search.toolbar
import kotlinx.android.synthetic.main.activity_search_result.*
import kotlinx.android.synthetic.main.fragment_light_novel.view.*


class SearchLightNovelActivity : AppCompatActivity() {

    private val viewModelFactory by lazy {
        SearchLightNovelViewModelFactory(
            provideLightNovelListApi()
        )
    }
    lateinit var viewModel: SearchLightNovelViewModel

    private val searchResultAdapter by lazy {
        LightNovelSearchResultAdapter().apply {
            setItemClickListener(searchResultClickListener)
        }
    }

    private val searchResultLayoutManager by lazy {
        androidx.recyclerview.widget.LinearLayoutManager(this)
    }

    private val searchResultClickListener = object : LightNovelSearchResultAdapter.ItemClickListener {
        override fun onItemClick(lightNovel: LightNovel) {
            val intent = Intent(
                this@SearchLightNovelActivity,
                LightNovelInfoActivity::class.java
            ).apply {
                putExtra(LightNovelInfoActivity.KEY_ID, lightNovel.id)
                putExtra(LightNovelInfoActivity.BOOK_TYPE, BookType.LIGHTNOVEL.ordinal)
            }
            this@SearchLightNovelActivity.startActivity(intent)

        }
    }

    private val searchPreviewAdapter by lazy {
        LightNovelSearchPreviewAdapter().apply {
            setItemClickListener(searchPreviewClickListener)
        }
    }


    internal val searchPreviewLayoutManager by lazy {
        androidx.recyclerview.widget.LinearLayoutManager(this)
    }

    internal val searchPreviewClickListener = object : LightNovelSearchPreviewAdapter.ItemClickListener {
        override fun onItemClick(lightNovel: LightNovel) {
            search_view.setQuery(lightNovel.title, true)
        }
    }

    internal val queryTextListener = object : android.widget.SearchView.OnQueryTextListener {

        override fun onQueryTextChange(newText: String): Boolean {
            viewModel.preview(newText)
            return false
        }

        override fun onQueryTextSubmit(query: String): Boolean {
            viewModel.load(query)
            search_view.clearFocus()
            return true
        }

    }



    private val disposable = CompositeDisposable()
    private lateinit var scrollListener: androidx.recyclerview.widget.RecyclerView.OnScrollListener


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SearchLightNovelViewModel::class.java]

        search_preview_recycler_view.layoutManager = searchPreviewLayoutManager
        search_preview_recycler_view.adapter = searchPreviewAdapter

        search_result_recycler_view.layoutManager = searchResultLayoutManager
        search_result_recycler_view.adapter = searchResultAdapter


        scrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val totalItemCount = searchResultLayoutManager.itemCount
                if (totalItemCount == searchResultLayoutManager.findLastVisibleItemPosition() + 1) {
                    if (viewModel.isLoading.value == false) {
                        viewModel.loadMore()
                        recyclerView.removeOnScrollListener(scrollListener)
                    }

                }
            }
        }
        disposable.add(
            viewModel.previewLightNovels
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { items ->
                    with(searchPreviewAdapter) {
                        if (items.isEmpty()) {
                            clearItems()
                            search_result_empty_text_view.visibility = View.VISIBLE
                        } else {
                            search_result_empty_text_view.visibility = View.GONE
                            setItems(items)
                        }
                        notifyDataSetChanged()
                    }
                }, {})
        )

        disposable.add(
            viewModel.lightNovels
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { items ->
                    with(searchResultAdapter) {
                        if (items.isEmpty()) {
                            clearItems()
                        } else {
                            setItems(items)
                        }
                        if (viewModel.isLastPage.value == false) {
                            search_result_recycler_view.addOnScrollListener(scrollListener)
                        }
                        notifyDataSetChanged()
                    }
                }, {})
        )

        disposable.add(
            viewModel.isLoading
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { isLoading ->
                    light_novel_progress_bar.visibility = when (isLoading) {
                        true -> View.VISIBLE
                        false -> View.GONE
                    }
                }, {})
        )
        setupSearchView()

    }

    private fun setupSearchView() {
        search_view.setBackgroundColor(Color.WHITE)

        search_view.setIconifiedByDefault(true);
        search_view.isIconified = false
        search_view.requestFocusFromTouch();
        search_view.queryHint = "라이트노벨 제목을 입력하세요."
        search_view.setOnQueryTextListener(queryTextListener)
        search_view.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                search_result_constraint_layout.visibility = View.GONE
                search_preview_recycler_view.visibility = View.VISIBLE
            } else {
                search_result_constraint_layout.visibility = View.VISIBLE
                search_preview_recycler_view.visibility = View.GONE
                search_view.setQuery(viewModel.lastSearchQuery.value ?: "", false)
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}