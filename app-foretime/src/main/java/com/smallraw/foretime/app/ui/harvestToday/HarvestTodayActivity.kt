package com.smallraw.foretime.app.ui.harvestToday

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.entity.Weather
import com.smallraw.foretime.app.ui.shape.ShapeActivity
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import com.smallraw.foretime.app.databinding.ActivityHarvestTodayBinding
import com.smallraw.time.model.BaseCallback
import com.smallraw.time.model.WeatherModel
import com.smallraw.time.utils.getWeekOfDate
import com.smallraw.foretime.app.utils.monthDayFormat
import java.util.*

class HarvestTodayActivity : BaseTitleBarActivity() {
    private val mBinding by lazy {
        ActivityHarvestTodayBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        setDateTime()
        initWeatherNow()

        mBinding.ivSuspensionShare.setOnClickListener {
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

    @SuppressLint("SetTextI18n")
    private fun setWeatherData(data: Weather?) {
        try {
            if (data == null) {
                mBinding.ivWeather.setBackgroundResource(R.drawable.ic_weather_qing)
                mBinding.tvWeather.text = "暂无 · 0°C"
            } else {
                val weatherImage = WeatherModel.getWeatherImage(data.cond_code!!)
                mBinding.ivWeather.setBackgroundResource(weatherImage)
                mBinding.tvWeather.text = "${data.cond_txt} · ${data.tmp}°C"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setDateTime() {
        mBinding.tvDate.text = monthDayFormat(Date())
        mBinding.tvWeek.text = getWeekOfDate(applicationContext, Date())
    }
}
