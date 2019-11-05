package com.sesang06.lightnovellist.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.sesang06.lightnovellist.controller.*

enum class LoadType {
    NEW, RECOMMEND, HIT, COMIC_NEW, COMIC_HIT
}

class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 5
    }

    override fun getItem(p0: Int): Fragment {
        when (p0) {
            0 -> return LightNovelSeriesListFragment()
            1 -> return NewLightNovelListFragment()
            2 -> return HitLightNovelListFragment()
            3 -> return NewComicListFragment()
            4 -> return HitComicListFragment()
        }
        return RecommendLightNovelListFragment()
    }
}