package com.sesang06.lightnovellist

import android.Manifest
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
import com.sesang06.lightnovellist.adapter.LightNovelAdapter
import com.sesang06.lightnovellist.adapter.LightNovelSeriesAdapter
import com.sesang06.lightnovellist.adapter.SeriesInfoNovelAdapter
import com.sesang06.lightnovellist.model.BookType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.service.CaptureManager
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelInfoViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelInfoViewModelFactory
import com.sesang06.lightnovellist.viewmodel.SeriesInfoViewModel
import com.sesang06.lightnovellist.viewmodel.SeriesInfoViewModelFactory
import com.tedpark.tedpermission.rx2.TedRx2Permission
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.PublishSubject

import kotlinx.android.synthetic.main.activity_series_info.*
import kotlinx.android.synthetic.main.fragment_light_novel_series_list.view.*
import java.util.HashMap

class SeriesInfoActivity: AppCompatActivity(), SeriesInfoNovelAdapter.ItemClickListener {

    companion object {
        const val KEY_ID = "key_id"
    }


    private val compositeDisposable = CompositeDisposable()

    internal val viewModelFactory by lazy {
        SeriesInfoViewModelFactory(
            provideLightNovelListApi()
        )
    }
    lateinit var viewModel: SeriesInfoViewModel

    private val seriesAdapter by lazy {
        SeriesInfoNovelAdapter().apply {
            setItemClickListener(this@SeriesInfoActivity)
        }
    }

    private val seriesLayoutManager by lazy {
        GridLayoutManager(this, 4)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_series_info)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = ""
        toolbar.subtitle = ""
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[SeriesInfoViewModel::class.java]


        novel_info_recycler_view.adapter = seriesAdapter
        novel_info_recycler_view.layoutManager = seriesLayoutManager

        val id = intent.getIntExtra(KEY_ID, 0)


        viewModel.load(id)


        compositeDisposable.add(
            viewModel.series
                .map { it.getBook(BookType.LIGHTNOVEL) }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( { items ->
                    with(seriesAdapter) {
                        if (items.isEmpty()) {
                            clearItems()
                        } else {
                            setItems(items)
                        }
                        notifyDataSetChanged()
                      /*  if (viewModel.isLastPage.value == false) {
                            view.light_novel_series_list_recycler_view.addOnScrollListener(scrollListener)
                        }*/
                    }
                }, {})
        )

        compositeDisposable.add(
            viewModel.seriesTitle
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ title ->
                    toolbar.title = title
                    series_title_view.text = title
                }, {})
        )

        compositeDisposable.add(
            viewModel.series
                .map { it.description }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ description ->
                    series_description_view.text = description
                }, {})
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> this.onBackPressed()
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


    override fun onItemClick(lightNovel: LightNovel) {
        var intent = Intent(this, LightNovelInfoActivity::class.java).apply {
            putExtra(LightNovelInfoActivity.KEY_ID, lightNovel.id)
            putExtra(LightNovelInfoActivity.BOOK_TYPE, BookType.LIGHTNOVEL.ordinal)
        }
        this.startActivity(intent)
    }
}