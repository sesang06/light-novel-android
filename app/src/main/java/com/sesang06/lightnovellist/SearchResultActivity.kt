package com.sesang06.lightnovellist

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.sesang06.lightnovellist.adapter.LightNovelSearchResultAdapter
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.SearchLightNovelViewModel
import com.sesang06.lightnovellist.viewmodel.SearchLightNovelViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_search.*

class SearchResultActivity : AppCompatActivity(), LightNovelSearchResultAdapter.ItemClickListener {

//    companion object {
//        const val KEY_QUERY = "key_query"
//    }
//
//    internal val viewModelFactory by lazy {
//        SearchLightNovelViewModelFactory(
//            provideLightNovelListApi()
//        )
//    }
//    lateinit var viewModel: SearchLightNovelViewModel
//
//    internal val adapter by lazy {
//        LightNovelSearchResultAdapter().apply { setItemClickListener(this@SearchResultActivity) }
//    }
//
//    internal val layoutManager by lazy {
//        LinearLayoutManager(this)
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_search_result)
//        setSupportActionBar(toolbar)
//
//
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//        viewModel = ViewModelProviders.of(this, viewModelFactory)[SearchLightNovelViewModel::class.java]
//        light_novel_recycler_view.layoutManager = layoutManager
//        light_novel_recycler_view.adapter = this.adapter
//        val query = intent.getStringExtra(SearchResultActivity.KEY_QUERY)
//
//        viewModel.lightNovels
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe { items ->
//                with(adapter) {
//                    if (items.isEmpty()) {
//                        clearItems()
//                    } else {
//                        setItems(items)
//                    }
//                    notifyDataSetChanged()
//                }
//            }
//        viewModel.load(query)
//        setupSearchView()
//        search_view.setQuery(query,false)
//    }
//
//
//    private fun setupSearchView() {
//        search_view.setBackgroundColor(Color.WHITE)
//
//
//        search_view.setIconifiedByDefault(true)
//        search_view.isIconified = false
//
//        search_view.setOnSearchClickListener(object : View.OnClickListener {
//            override fun onClick(v: View?) {
//                print("A")
//            }
//        })
//
//    }
    override fun onItemClick(lightNovel: LightNovel) {
//        val intent = Intent(this, LightNovelInfoActivity::class.java).apply {
//            putExtra( LightNovelInfoActivity.KEY_ID, lightNovel.id)
//        }
//        this.startActivity(intent)
    }

}