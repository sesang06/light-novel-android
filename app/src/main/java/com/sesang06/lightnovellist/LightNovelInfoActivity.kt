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
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_light_novel_info.*
import kotlinx.android.synthetic.main.activity_light_novel_info.toolbar
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_search.*

class LightNovelInfoActivity : AppCompatActivity() {
    companion object {
        const val KEY_ID = "key_id"
    }

    private val compositeDisposable = CompositeDisposable()

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
        toolbar.title = ""
        toolbar.subtitle = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[LightNovelInfoViewModel::class.java]

        series_recycler_view.adapter = seriesAdapter
        series_recycler_view.layoutManager = seriesLayoutManager

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
        publisher_description_expand_text_view.setOnClickListener { view: View? ->
            publisher_description_expand_text_view.visibility = View.GONE
            publisher_description_text_view.maxLines = Integer.MAX_VALUE
        }
        val disposeable = viewModel.lightNovel
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

                if (lightNovel.indexDescription.isEmpty()) {
                    index_constraint_layout.visibility = View.GONE
                } else {
                    index_constraint_layout.visibility = View.VISIBLE
                }
                index_text_view.text = lightNovel.indexDescription

                if (lightNovel.publisherDescription.isEmpty()) {
                    publisher_description_constraint_layout.visibility = View.GONE
                } else {
                    publisher_description_constraint_layout.visibility = View.VISIBLE
                }
                publisher_description_text_view.text = lightNovel.publisherDescription

                if (lightNovel.series.id == 0) {
                    series_linear_layout.visibility = View.GONE
                } else {
                    series_linear_layout.visibility = View.VISIBLE
                    val size = lightNovel.series.lightNovels.size + 1
                    series_title_view.text = "이 책의 시리즈 (총 "+ size.toString() + "권)"
                    seriesAdapter.setItems(lightNovel.series.lightNovels)
                    seriesAdapter.notifyDataSetChanged()
                }
            }
        compositeDisposable.add(disposeable)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            this.onBackPressed()
            return false
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()

        Glide.with(this.applicationContext).pauseRequests()
    }
}