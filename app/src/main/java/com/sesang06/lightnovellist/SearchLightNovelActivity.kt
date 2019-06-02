package com.sesang06.lightnovellist

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.support.v7.widget.SearchView
import com.sesang06.lightnovellist.adapter.LightNovelAdapter
import com.sesang06.lightnovellist.adapter.MainPagerAdapter
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelInfoViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelInfoViewModelFactory
import com.sesang06.lightnovellist.viewmodel.SearchLightNovelViewModel
import com.sesang06.lightnovellist.viewmodel.SearchLightNovelViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.activity_search.*
import kotlinx.android.synthetic.main.fragment_light_novel.view.*

class SearchLightNovelActivity : AppCompatActivity(), LightNovelAdapter.ItemClickListener {

    internal val viewModelFactory by lazy {
        SearchLightNovelViewModelFactory(
            provideLightNovelListApi()
        )
    }
    lateinit var viewModel: SearchLightNovelViewModel

    internal val adapter by lazy {
        LightNovelAdapter().apply { setItemClickListener(this@SearchLightNovelActivity) }
    }

    internal val layoutManager by lazy {
        LinearLayoutManager(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[SearchLightNovelViewModel::class.java]
        light_novel_recycler_view.layoutManager = layoutManager
        light_novel_recycler_view.adapter = this.adapter
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
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                viewModel.load(newText)
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                // task HERE
                return false
            }

        })

        searchView.queryHint = "라이트노벨 제목을 입력하세요."
        searchView.maxWidth = Integer.MAX_VALUE
        return super.onCreateOptionsMenu(menu)
    }

    override fun onItemClick(lightNovel: LightNovel) {
        val intent = Intent(this, LightNovelInfoActivity::class.java).apply {
            putExtra( LightNovelInfoActivity.KEY_ID, lightNovel.id)
        }
        this.startActivity(intent)
    }

}