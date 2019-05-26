package com.sesang06.lightnovellist

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.view.ViewPager
import android.view.ViewParent
import com.sesang06.lightnovellist.adapter.MainPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var pagerAdapter: MainPagerAdapter

    private var navigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener {
    item ->
         when (item.itemId) {
            R.id.action_hot -> view_pager.currentItem = 0
            R.id.action_new -> view_pager.currentItem = 1
             R.id.action_recommend -> view_pager.currentItem = 2
        }
        true
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        pagerAdapter = MainPagerAdapter(supportFragmentManager)
        view_pager.adapter = pagerAdapter
        bottom_navigation.setOnNavigationItemSelectedListener(navigationItemSelectedListener)
        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
            override fun onPageSelected(position: Int) {
                when (position) {
                    0 ->  bottom_navigation.selectedItemId =   R.id.action_hot
                    1 ->  bottom_navigation.selectedItemId =   R.id.action_new
                        2 ->  bottom_navigation.selectedItemId =   R.id.action_recommend
                }

            }

        })
    }
}
