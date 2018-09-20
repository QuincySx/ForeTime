package com.smallraw.foretime.app.ui.main

import android.os.Bundle
import android.view.View
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.adapter.ViewPagerAdapter
import com.smallraw.foretime.app.ui.calendar.CalendarFragment
import com.smallraw.foretime.app.ui.tomatoBell.TomatoBellFragment
import com.smallraw.time.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private lateinit var tomatoBellFragment: TomatoBellFragment
    private lateinit var calendarFragment: CalendarFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        initFragment()
        initView()
    }

    private fun initFragment() {
        tomatoBellFragment = TomatoBellFragment()
        calendarFragment = CalendarFragment()
    }

    private fun initView() {
        viewPagerAdapter.addFragment(tomatoBellFragment)
        viewPagerAdapter.addFragment(calendarFragment)
        viewPager.adapter = viewPagerAdapter
        ivTomatoBell.isChecked = true
    }

    fun onCreate(view: View) {
        when (view.id) {
            R.id.ivTomatoBell -> {
                viewPager.currentItem = 0
                ivTomatoBell.isChecked = true
                ivCalendar.isChecked = false
            }
            R.id.ivCalendar -> {
                viewPager.currentItem = 1
                ivTomatoBell.isChecked = false
                ivCalendar.isChecked = true
            }
        }
    }

}
