package com.sesang06.lightnovellist.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.sesang06.lightnovellist.controller.HitLightNovelListFragment
import com.sesang06.lightnovellist.controller.NewLightNovelListFragment
import com.sesang06.lightnovellist.controller.RecommendLightNovelListFragment

enum class LoadType {
    NEW, RECOMMEND, HIT
}

class MainPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return 3
    }

    override fun getItem(p0: Int): Fragment {
        when(p0) {
            0 -> return HitLightNovelListFragment()
            1 -> return NewLightNovelListFragment()
            2 -> return RecommendLightNovelListFragment()
        }
        return RecommendLightNovelListFragment()
    }
}