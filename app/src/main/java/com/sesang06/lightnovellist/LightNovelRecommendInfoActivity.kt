package com.sesang06.lightnovellist

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.sesang06.lightnovellist.adapter.LightNovelRecommendInfoAdapter
import com.sesang06.lightnovellist.model.LightNovel
import kotlinx.android.synthetic.main.activity_light_novel_featured_info.toolbar
import kotlinx.android.synthetic.main.activity_light_novel_recommend_info.*


class LightNovelRecommendInfoActivity: AppCompatActivity() {

    internal val recommendInfoAdapter by lazy {
        LightNovelRecommendInfoAdapter().apply { setItemClickListener(recommendInfoClickListener) }
    }

    internal val recommendInfoLayoutManager by lazy {
        androidx.recyclerview.widget.GridLayoutManager(this, 3)
    }

    internal val recommendInfoClickListener = object : LightNovelRecommendInfoAdapter.ItemClickListener {
        override fun onItemClick(lightNovel: LightNovel) {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_novel_recommend_info)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        recommend_info_recycler_view.adapter = recommendInfoAdapter
        recommend_info_recycler_view.layoutManager = recommendInfoLayoutManager

        recommendInfoAdapter.setItems(listOf(sampleLightNovel(),sampleLightNovel(),sampleLightNovel(),sampleLightNovel()))

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