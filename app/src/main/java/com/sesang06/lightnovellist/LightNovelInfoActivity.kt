package com.sesang06.lightnovellist

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.annotation.MainThread
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.Glide
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_light_novel_info)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[LightNovelInfoViewModel::class.java]

        val id = intent.getIntExtra(KEY_ID,0)
        viewModel.load(id)

        viewModel.lightNovel
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { lightNovel ->
                Glide
                    .with(this@LightNovelInfoActivity)
                    .load(lightNovel.thumbnail)
                    .into(thumbnail_image_view)
                title_text_view.text = lightNovel.title
                description_text_view.text = lightNovel.description
                val authorPublisher = lightNovel.author.name + " | " + lightNovel.publisher.name
                author_publisher_text_view.text = authorPublisher
                val sdf = java.text.SimpleDateFormat("yyyy년 MM월 dd일")
                val publicationDateString = sdf.format(lightNovel.publicationDate)
                publication_date_text_view.text = publicationDateString
                link_text_view.text = lightNovel.link
            }
    }
}