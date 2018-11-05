package com.smallraw.foretime.app.ui.calendar

import android.annotation.SuppressLint
import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.util.LongSparseArray
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.smallraw.foretime.app.App

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseDialogView
import com.smallraw.foretime.app.entity.Weather
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity
import com.smallraw.foretime.app.ui.calendar.vm.CalendarVewModle
import com.smallraw.foretime.app.ui.dialog.SelectDayTypeDialog
import com.smallraw.foretime.app.ui.main.OnMainActivityCallback
import com.smallraw.foretime.app.ui.tomatoBell.TomatoBellFragment
import com.smallraw.time.base.BaseFragment
import com.smallraw.time.model.BaseCallback
import com.smallraw.time.model.WeatherModel
import com.smallraw.time.utils.getWeekOfDate
import com.smallraw.time.utils.monthDayFormat
import kotlinx.android.synthetic.main.fragment_calendar.*
import me.jessyan.autosize.utils.AutoSizeUtils
import java.util.*

class CalendarFragment : BaseFragment() {
    companion object {
        @JvmStatic
        fun newInstance(onMainActivityCallback: OnMainActivityCallback): CalendarFragment {
            val fragment = CalendarFragment()
            fragment.onMainActivityCallback = onMainActivityCallback
            return fragment
        }
    }

    lateinit var mCalendarVewModle: CalendarVewModle

    var onMainActivityCallback: OnMainActivityCallback? = null
    private val mCalendarList = ArrayList<MemorialEntity>()
    private val mCalendarAdapter = CalendarAdapter(mCalendarList)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mCalendarVewModle = ViewModelProviders.of(this).get(CalendarVewModle::class.java)
        initView()
        mCalendarVewModle.mMemorialListLiveData.observe(this, android.arch.lifecycle.Observer {
            mCalendarList.clear()
            mCalendarList.addAll(it!!.distinct())
            mCalendarAdapter.notifyDataSetChanged()
        })

        setDateTime()
        initWeatherNow()
    }

    private fun initView() {
        recyclerView.layoutManager = LinearLayoutManager(activity?.applicationContext)
        recyclerView.adapter = mCalendarAdapter
        mCalendarAdapter.notifyDataSetChanged()
    }

    fun showViewAction() {
        onMainActivityCallback?.setOnLongClickListener(null)
        onMainActivityCallback?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                SelectDayTypeDialog(context).showAtViewAuto(v, AutoSizeUtils.dp2px(context, 56F))
            }
        })
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
