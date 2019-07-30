package com.sesang06.lightnovellist

import android.Manifest
import android.app.Dialog
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.PixelCopy
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.kakao.kakaolink.v2.KakaoLinkResponse
import com.kakao.kakaolink.v2.KakaoLinkService
import com.kakao.message.template.ButtonObject
import com.kakao.message.template.ContentObject
import com.kakao.message.template.FeedTemplate
import com.kakao.message.template.LinkObject
import com.kakao.network.ErrorResult
import com.kakao.network.callback.ResponseCallback
import com.sesang06.lightnovellist.adapter.LightNovelSeriesAdapter
import com.sesang06.lightnovellist.model.BookType
import com.sesang06.lightnovellist.model.LightNovel
import com.sesang06.lightnovellist.service.CaptureManager
import com.sesang06.lightnovellist.service.provideLightNovelListApi
import com.sesang06.lightnovellist.viewmodel.LightNovelInfoViewModel
import com.sesang06.lightnovellist.viewmodel.LightNovelInfoViewModelFactory
import com.tedpark.tedpermission.rx2.TedRx2Permission
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_light_novel_info.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class LightNovelInfoActivity : AppCompatActivity() {

    companion object {
        const val KEY_ID = "key_id"
        const val BOOK_TYPE = "book_type"
    }

    private lateinit var bookType: BookType

    private val compositeDisposable = CompositeDisposable()


    private val didKakaoShareClick = PublishSubject.create<Unit>()

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


    private val captureManager by lazy {
        CaptureManager(this)
    }

    private val searchResultClickListener = object : LightNovelSeriesAdapter.ItemClickListener {
        override fun onItemClick(lightNovel: LightNovel) {
            val intent = Intent(
                this@LightNovelInfoActivity,
                LightNovelInfoActivity::class.java
            ).apply {
                putExtra(KEY_ID, lightNovel.id)
                putExtra(BOOK_TYPE, bookType.ordinal)
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
        when (intent.getIntExtra(BOOK_TYPE, BookType.LIGHTNOVEL.ordinal)) {
            BookType.COMIC.ordinal -> this.bookType = BookType.COMIC
            else -> this.bookType = BookType.LIGHTNOVEL
        }
        viewModel.load(id, this.bookType)


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
                    val books = lightNovel.series.getBook(bookType)
                    val size = books.size + 1
                    series_title_view.text = "이 책의 시리즈 (총 ${size.toString()}권)"
                    seriesAdapter.setItems(books)
                    seriesAdapter.notifyDataSetChanged()
                }
            }
        compositeDisposable.add(disposeable)

        compositeDisposable.add(
            this.didKakaoShareClick
                .withLatestFrom(viewModel.lightNovel, BiFunction { _: Unit, model: LightNovel ->
                    model
                })
                .subscribe({ lightNovel ->
                    this.shareToKakaoTalk(lightNovel)
                }, {

                })
        )
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> this.onBackPressed()
            R.id.info_capture -> this.captureViews()
            R.id.info_kakao_share -> this.didKakaoShareClick.onNext(Unit)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.info_menu, menu)
        return true
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    override fun onDestroy() {
        super.onDestroy()

        Glide.with(this.applicationContext).pauseRequests()
    }

    private fun captureViews() {
        compositeDisposable.add(
            TedRx2Permission.with(this)
                .setRationaleTitle("저장 권한 요청")
                .setRationaleMessage("캡처한 사진을 저장하기 위해서 저장 권한이 필요합니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .setGotoSettingButton(true)
                .setGotoSettingButtonText("설정으로 이동")
                .setDeniedTitle("권한 거부 안내")
                .setDeniedMessage("권한을 거부하시면 사진을 캡처할 수 없습니다.")
                .request()
                .subscribe({ tedPermissionResult ->
                    if (tedPermissionResult.isGranted()) {
                        this.captureManager.getScreenShot(info_linear_layout) {
                            Toast.makeText(this, "캡처를 완료했습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }, { throwable -> })
        )
    }



    private fun shareToKakaoTalk(lightNovel: LightNovel) {
        val params = FeedTemplate
            .newBuilder(
                ContentObject.newBuilder(
                    lightNovel.title,
                    lightNovel.largeThumbnail,
                    LinkObject.newBuilder().build()
                )
                    .setDescrption(lightNovel.description)
                    .setImageWidth(210)
                    .setImageHeight(297)
                    .build()
            )

            .addButton(
                ButtonObject(
                    "앱에서 보기", LinkObject.newBuilder()
                        .setAndroidExecutionParams("id=${lightNovel.id}&bookType=${bookType.name}")
                        .build()
                )
            )
            .build()
        val serverCallbackArgs: Map<String, String> = HashMap<String, String>()
        KakaoLinkService.getInstance().sendDefault(
            this,
            params,
            serverCallbackArgs,
            object : ResponseCallback<KakaoLinkResponse>() {
                override fun onSuccess(result: KakaoLinkResponse?) {
                }

                override fun onFailure(errorResult: ErrorResult?) {
                }
            }
        )

    }
}