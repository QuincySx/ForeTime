package com.smallraw.foretime.app.ui.calendar

import android.app.Application
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.smallraw.foretime.app.R
import com.smallraw.time.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_calendar.*

import java.util.ArrayList

class CalendarFragment : BaseFragment() {
    private val mCalendarList = ArrayList<String>()
    private val mCalendarAdapter = CalendarAdapter(mCalendarList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mCalendarList.add("asdasdasd")
        mCalendarList.add("asdasdasd")
        mCalendarList.add("asdasdasd")
        mCalendarList.add("asdasdasd")
        mCalendarList.add("asdasdasd")
        mCalendarList.add("asdasdasd")
        mCalendarList.add("asdasdasd")

        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        recyclerView.adapter = mCalendarAdapter
        mCalendarAdapter.notifyDataSetChanged()
    }
}
