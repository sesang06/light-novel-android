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
        return 4
    }

    override fun getItem(p0: Int): Fragment {
        when (p0) {
            0 -> return NewLightNovelListFragment()
            1 -> return HitLightNovelListFragment()
            2 -> return NewComicListFragment()
            3 -> return HitComicListFragment()
        }
        return RecommendLightNovelListFragment()
    }
}