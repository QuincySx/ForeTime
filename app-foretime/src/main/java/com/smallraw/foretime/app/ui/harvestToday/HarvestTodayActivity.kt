package com.smallraw.foretime.app.ui.harvestToday

import android.content.Intent
import android.os.Bundle
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.entity.Weather
import com.smallraw.foretime.app.ui.shape.ShapeActivity
import com.smallraw.time.base.BaseTitleBarActivity
import com.smallraw.time.model.BaseCallback
import com.smallraw.time.model.WeatherModel
import com.smallraw.time.utils.getWeekOfDate
import com.smallraw.time.utils.monthDayFormat
import kotlinx.android.synthetic.main.activity_harvest_today.*
import java.util.*

class HarvestTodayActivity : BaseTitleBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_harvest_today)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        setDateTime()
        initWeatherNow()

        ivSuspensionShare.setOnClickListener {
            val intent = Intent(this, ShapeActivity::class.java)
            startActivity(intent)
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
        tvWeek.text = getWeekOfDate(applicationContext, Date())
    }
}
