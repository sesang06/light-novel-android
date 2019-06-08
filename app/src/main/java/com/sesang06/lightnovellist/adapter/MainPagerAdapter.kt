package com.sesang06.lightnovellist.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import com.sesang06.lightnovellist.controller.HitLightNovelListFragment
import com.sesang06.lightnovellist.controller.MainFragment
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
        when (p0) {
            0 -> return MainFragment()
            1 -> return HitLightNovelListFragment()
            2 -> return NewLightNovelListFragment()
        }
        return RecommendLightNovelListFragment()
    }
}