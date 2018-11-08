package com.smallraw.foretime.app.ui.main

import android.graphics.Rect
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.IntegerRes
import android.view.TouchDelegate
import android.view.View
import android.view.ViewGroup
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.R.id.*
import com.smallraw.foretime.app.common.adapter.ViewPagerAdapter
import com.smallraw.foretime.app.ui.calendar.CalendarFragment
import com.smallraw.foretime.app.ui.tomatoBell.TomatoBellFragment
import com.smallraw.time.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity() : BaseActivity(), OnMainActivityCallback {
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
        tomatoBellFragment = TomatoBellFragment.newInstance(this)
        calendarFragment = CalendarFragment.newInstance(this)
    }

    private fun initView() {
        viewPagerAdapter.addFragment(tomatoBellFragment)
        viewPagerAdapter.addFragment(calendarFragment)
        viewPager.adapter = viewPagerAdapter
        ivTomatoBell.isChecked = true

        setTouchDelegate(ivCalendar, 100)
        tomatoBellFragment.showViewAction()
    }

    fun onClick(view: View) {
        when (view.id) {
            R.id.ivTomatoBell -> {
                viewPager.currentItem = 0
                ivTomatoBell.isChecked = true
                ivCalendar.isChecked = false
                setTouchDelegate(ivCalendar, 100)
                tomatoBellFragment.showViewAction()
            }
            R.id.ivCalendar -> {
                viewPager.currentItem = 1
                ivTomatoBell.isChecked = false
                ivCalendar.isChecked = true
                setTouchDelegate(ivTomatoBell, 100)
                calendarFragment.showViewAction()
            }
        }
    }

    private fun setTouchDelegate(view: View, expandTouchWidth: Int) {
        val parentView = view.getParent() as ViewGroup
        view.post {
            val rect = Rect()
            view.getHitRect(rect)
            rect.top -= expandTouchWidth
            rect.bottom += expandTouchWidth
            rect.left -= expandTouchWidth
            rect.right += expandTouchWidth
            parentView.touchDelegate = TouchDelegate(rect, view)
        }
    }

    override fun onDestroy() {
        viewPagerAdapter.clear()
        super.onDestroy()
    }

    override fun onChangeIvSuspension(@DrawableRes id: Int) {
        if (ivSuspension != null) {
            ivSuspension.setBackgroundResource(id)
        }
    }

    override fun setOnTouchEventListener(onTouchListener: View.OnTouchListener?) {
        if (ivSuspension != null) {
            ivSuspension.setOnTouchListener(onTouchListener)
        }
    }

    override fun setOnLongClickListener(onLongClickListener: View.OnLongClickListener?) {
        if (ivSuspension != null) {
            ivSuspension.setOnLongClickListener(onLongClickListener)
        }
    }

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        if (ivSuspension != null) {
            ivSuspension.setOnClickListener(onClickListener)
        }
    }
}