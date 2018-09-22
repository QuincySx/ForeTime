package com.smallraw.foretime.app.ui.calendar

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.support.v4.util.LongSparseArray
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smallraw.foretime.app.App

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.entity.Weather
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity
import com.smallraw.time.base.BaseFragment
import com.smallraw.time.model.BaseCallback
import com.smallraw.time.model.WeatherModel
import com.smallraw.time.utils.getWeekOfDate
import com.smallraw.time.utils.monthDayFormat
import kotlinx.android.synthetic.main.fragment_calendar.*
import java.util.*

class CalendarFragment : BaseFragment() {
    private val mCalendarList = ArrayList<MemorialEntity>()
    private val mCalendarAdapter = CalendarAdapter(mCalendarList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        newData(0, 0)
        setDateTime()
        initWeatherNow()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        recyclerView.adapter = mCalendarAdapter
        mCalendarAdapter.notifyDataSetChanged()
    }

    private fun newData(display: Int, order: Int) {
        val application = activity!!.application as App
        application.getAppExecutors().networkIO().execute {
            val memorialList = application.getRepository().getActiveTask(display, order) as ArrayList

            val memorialMap = LongSparseArray<MemorialEntity>(memorialList.size)
            for (item in memorialList) {
                memorialMap.put(item.id, item)
            }

            val taskTopList = application.getRepository().getTaskTopList(0)
            val topMemorialList = ArrayList<MemorialEntity>(memorialMap.size());
            for (item in taskTopList) {
                val get = memorialMap.get(item.memorial_id)
                if (get != null) {
                    topMemorialList.add(get)
                    memorialList.remove(get)
                }
            }
            memorialList.addAll(0, topMemorialList)

            mCalendarList.clear()
            mCalendarList.addAll(memorialList.distinct())
        }
    }

    private fun initWeatherNow() {
        WeatherModel().getWeatherCache(object : BaseCallback<Weather> {
            override fun onSuccess(data: Weather) {
                setWeatherData(data)
            }

            override fun onError(e: Exception) {
                setWeatherData(null)
            }
        })
        WeatherModel().getWeatherNow(object : BaseCallback<Weather> {
            override fun onSuccess(data: Weather) {
                setWeatherData(data)
            }

            override fun onError(e: Exception) {
                setWeatherData(null)
            }
        })
    }

    private fun setWeatherData(data: Weather?) {
        try {
            if (data == null) {
                ivWeather.setBackgroundResource(R.drawable.ic_weather_qing)
                tvWeather.text = "暂无 · 0°C"
            } else {
                val weatherImage = WeatherModel.getWeatherImage(data.cond_code)
                ivWeather.setBackgroundResource(weatherImage)
                tvWeather.text = "${data.cond_txt} · ${data.tmp}°C"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setDateTime() {
        tvDate.text = monthDayFormat(Date())
        tvWeek.text = getWeekOfDate(activity!!.applicationContext, Date())
    }
}
