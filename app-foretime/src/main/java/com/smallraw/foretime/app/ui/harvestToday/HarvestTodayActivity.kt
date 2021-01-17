/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.foretime.app.ui.harvestToday

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.databinding.ActivityHarvestTodayBinding
import com.smallraw.foretime.app.entity.Weather
import com.smallraw.foretime.app.ui.shape.ShapeActivity
import com.smallraw.foretime.app.utils.monthDayFormat
import com.smallraw.time.model.BaseCallback
import com.smallraw.time.model.WeatherModel
import com.smallraw.time.utils.getWeekOfDate
import java.util.*

class HarvestTodayActivity : BaseTitleBarActivity() {
    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_harvest_today)
    }

    override fun getBinding(): ActivityHarvestTodayBinding {
        return super.getBinding() as ActivityHarvestTodayBinding
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        setDateTime()
        initWeatherNow()

        getBinding().ivSuspensionShare.setOnClickListener {
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
                getBinding().ivWeather.setBackgroundResource(R.drawable.ic_weather_qing)
                getBinding().tvWeather.text = "暂无 · 0°C"
            } else {
                val weatherImage = WeatherModel.getWeatherImage(data.cond_code!!)
                getBinding().ivWeather.setBackgroundResource(weatherImage)
                getBinding().tvWeather.text = "${data.cond_txt} · ${data.tmp}°C"
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun setDateTime() {
        getBinding().tvDate.text = monthDayFormat(Date())
        getBinding().tvWeek.text = getWeekOfDate(applicationContext, Date())
    }
}
