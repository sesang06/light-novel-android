package com.sesang06.lightnovellist.controller

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.sesang06.lightnovellist.R
import com.sesang06.lightnovellist.adapter.LightNovelAdapter
import com.sesang06.lightnovellist.adapter.LightNovelFeaturedAdapter
import com.sesang06.lightnovellist.adapter.LightNovelRecommendAdapter
import com.sesang06.lightnovellist.adapter.LoadType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.rx.AutoClearedDisposable
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelListViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.view.*





class MainFragment: Fragment(), LightNovelFeaturedAdapter.ItemClickListener, LightNovelRecommendAdapter.ItemClickListener {

    internal val featuredAdapter by lazy {
        LightNovelFeaturedAdapter().apply { setItemClickListener(this@MainFragment) }
    }

    internal val featuredLayoutManager by lazy {
        LinearLayoutManager(this.context).apply { orientation = LinearLayoutManager.HORIZONTAL }

//        object : LinearLayoutManager(this.context) {
//            override fun checkLayoutParams(lp: RecyclerView.LayoutParams?): Boolean {
//                lp?.width = lp!!.width / 2
//                return super.checkLayoutParams(lp)
//            }
//        }.apply { orientation = LinearLayoutManager.HORIZONTAL }
    }

    internal val recommendAdapter by lazy {
        LightNovelRecommendAdapter().apply { setItemClickListener(this@MainFragment) }
    }

    internal val recommentLayoutManager by lazy {
        LinearLayoutManager(this.context).apply { orientation = LinearLayoutManager.HORIZONTAL }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        // featured Recylcer View
        view.featured_recycler_view.adapter = featuredAdapter
        view.featured_recycler_view.layoutManager = featuredLayoutManager

        view.recommend_recycler_view.adapter = recommendAdapter
        view.recommend_recycler_view.layoutManager = recommentLayoutManager
        featuredAdapter.setItems(listOf(sampleLightNovel(),sampleLightNovel(),sampleLightNovel()))
        recommendAdapter.setItems(listOf(sampleLightNovel(),sampleLightNovel(),sampleLightNovel()))
        return view
    }

    override fun onItemClick(lightNovel: LightNovel) {

    }

    private fun sampleLightNovel(): LightNovel {
        val gson = Gson()
        return gson.fromJson("{\"id\":25,\"title\":\"무직전생 18 - Premium Extreme Novel\",\"description\":\"강적을 물리치고, 아리엘을 다음 아슬라 왕으로 만들라는 임무를 완수한 루데우스. 그 이후로도 수많은 지령을 수행하거나 노예 상인에게서 리니아를 구해 주다 보니, 1년 반이 흘렀다. 그런 어느 날, 리니아는 돌디어족 마을에서 보낸 편지를 받는데...\",\"publication_date\":\"2019-06-10T00:00:00.000Z\",\"thumbnail\":\"https://image.aladin.co.kr/product/19345/67/coversum/k982635813_1.jpg\",\"recommend_rank\":0,\"created_at\":\"2019-05-29T16:33:47.000Z\",\"updated_at\":\"2019-05-29T16:33:47.000Z\",\"author_id\":24,\"publisher_id\":10,\"author\":{\"id\":24,\"name\":\"리후진 나 마고노테 (지은이), 시로타카 (그림), 한신남 (옮긴이)\",\"created_at\":\"2019-05-29T16:33:47.000Z\",\"updated_at\":\"2019-05-29T16:33:47.000Z\"},\"publisher\":{\"id\":10,\"name\":\"학산문화사(라이트노벨)\",\"created_at\":\"2019-05-29T16:33:47.000Z\",\"updated_at\":\"2019-05-29T16:33:47.000Z\"},\"categories\":[]}",
            LightNovel::class.java)
    }
}