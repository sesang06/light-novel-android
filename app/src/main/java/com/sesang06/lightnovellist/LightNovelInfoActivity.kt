package com.sesang06.lightnovellist

import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.sesang06.lightnovellist.adapter.LightNovelSearchResultAdapter
import com.sesang06.lightnovellist.adapter.LightNovelSeriesAdapter
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelInfoViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelInfoViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.activity_light_novel_info.*

class LightNovelInfoActivity : AppCompatActivity() {
    companion object {
        const val KEY_ID = "key_id"
    }

    internal val viewModelFactory by lazy {
        LightNovelInfoViewModelFactory(
            provideLightNovelListApi()
        )
    }
    lateinit var viewModel: LightNovelInfoViewModel

    private val seriesAdapter by lazy {
        LightNovelSeriesAdapter().apply {
            setItemClickListener(searchResultClickListener)
        }
    }

    private val seriesLayoutManager by lazy {
        LinearLayoutManager(this)
    }

    private val searchResultClickListener = object : LightNovelSeriesAdapter.ItemClickListener {
        override fun onItemClick(lightNovel: LightNovel) {
            val intent = Intent(
                this@LightNovelInfoActivity,
                LightNovelInfoActivity::class.java
            ).apply {
                putExtra(LightNovelInfoActivity.KEY_ID, lightNovel.id)
            }
            this@LightNovelInfoActivity.startActivity(intent)

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_novel_info)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel = ViewModelProviders.of(this, viewModelFactory)[LightNovelInfoViewModel::class.java]

        series_recycler_view.adapter = seriesAdapter
        series_recycler_view.layoutManager = seriesLayoutManager
        seriesAdapter.setItems(listOf(sampleLightNovel(),sampleLightNovel(),sampleLightNovel()))

        val id = intent.getIntExtra(KEY_ID, 0)
        viewModel.load(id)


        description_expand_text_view.setOnClickListener { view: View? ->
            description_expand_text_view.visibility = View.GONE
            description_text_view.maxLines = Integer.MAX_VALUE
        }

        index_expand_text_view.setOnClickListener { view: View? ->
            index_expand_text_view.visibility = View.GONE
            index_text_view.maxLines = Integer.MAX_VALUE
        }
        viewModel.lightNovel
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lightNovel ->
                toolbar.title = lightNovel.title
                Glide
                    .with(this@LightNovelInfoActivity)
                    .load(lightNovel.thumbnail)
                    .into(thumbnail_image_view)
                title_text_view.text = lightNovel.title
                description_text_view.text = lightNovel.description
                author_text_view.text = lightNovel.author.name
                publisher_text_view.text = lightNovel.publisher.name
                val sdf = java.text.SimpleDateFormat("yyyy년 MM월 dd일")
                val publicationDateString = sdf.format(lightNovel.publicationDate)
                publication_date_text_view.text = publicationDateString
                link_text_view.text = lightNovel.link
            }
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