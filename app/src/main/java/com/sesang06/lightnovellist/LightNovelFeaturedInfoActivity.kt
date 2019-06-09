package com.sesang06.lightnovellist

import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.sesang06.lightnovellist.adapter.LightNovelFeaturedInfoAdapter
import com.sesang06.lightnovellist.model.LightNovel
import kotlinx.android.synthetic.main.activity_light_novel_featured_info.*
import kotlinx.android.synthetic.main.activity_light_novel_featured_info.toolbar
import android.opengl.ETC1.getHeight
import android.support.design.widget.AppBarLayout
import android.util.Log
import kotlinx.android.synthetic.main.activity_light_novel_featured_info.app_bar
import kotlinx.android.synthetic.main.activity_light_novel_info.*


class LightNovelFeaturedInfoActivity: AppCompatActivity() {

    internal val featuredInfoAdapter by lazy {
        LightNovelFeaturedInfoAdapter().apply { setItemClickListener(featuredInfoClickListener) }
    }

    internal val featuredInfoLayoutManager by lazy {
        LinearLayoutManager(this).apply { orientation = LinearLayoutManager.VERTICAL }
    }

    internal val featuredInfoClickListener = object : LightNovelFeaturedInfoAdapter.ItemClickListener {
        override fun onItemClick(lightNovel: LightNovel) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_novel_featured_info)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        featured_info_recycler_view.adapter = featuredInfoAdapter
        featured_info_recycler_view.layoutManager = featuredInfoLayoutManager

        featuredInfoAdapter.setItems(listOf(sampleLightNovel(),sampleLightNovel(),sampleLightNovel()))

//        app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
//            val TAG = "hello"
//            if (verticalOffset == 0) {
//                Log.d(TAG,"Expanded");
//                toolbar.title = ""
//            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
//                toolbar.title = "aa"
//                Log.d(TAG,"Collapsed");
//                 } else {
//                Log.d(TAG,"Idle");
//
//            }
//        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            this.onBackPressed()
            return false
        }
        return super.onOptionsItemSelected(item)
    }


    private fun sampleLightNovel(): LightNovel {
        val gson = Gson()
        return gson.fromJson("{\"id\":25,\"title\":\"무직전생 18 - Premium Extreme Novel\",\"description\":\"강적을 물리치고, 아리엘을 다음 아슬라 왕으로 만들라는 임무를 완수한 루데우스. 그 이후로도 수많은 지령을 수행하거나 노예 상인에게서 리니아를 구해 주다 보니, 1년 반이 흘렀다. 그런 어느 날, 리니아는 돌디어족 마을에서 보낸 편지를 받는데...\",\"publication_date\":\"2019-06-10T00:00:00.000Z\",\"thumbnail\":\"https://image.aladin.co.kr/product/19345/67/cover500/k982635813_1.jpg\",\"recommend_rank\":0,\"created_at\":\"2019-05-29T16:33:47.000Z\",\"updated_at\":\"2019-05-29T16:33:47.000Z\",\"author_id\":24,\"publisher_id\":10,\"author\":{\"id\":24,\"name\":\"리후진 나 마고노테 (지은이), 시로타카 (그림), 한신남 (옮긴이)\",\"created_at\":\"2019-05-29T16:33:47.000Z\",\"updated_at\":\"2019-05-29T16:33:47.000Z\"},\"publisher\":{\"id\":10,\"name\":\"학산문화사(라이트노벨)\",\"created_at\":\"2019-05-29T16:33:47.000Z\",\"updated_at\":\"2019-05-29T16:33:47.000Z\"},\"categories\":[]}",
            LightNovel::class.java)
    }
}