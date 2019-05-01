package com.smallraw.time.model

import android.support.annotation.DrawableRes
import android.util.Log
import com.google.gson.Gson
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.entity.Weather
import com.smallraw.time.http.WeatherRequest

public class WeatherModel() {
    companion object {
        val WeatherNow = "WeatherNow"

        val exception = App.getInstance().getAppExecutors()

        @JvmStatic
        @DrawableRes
        fun getWeatherImage(state: String): Int {
            val stateCode = state.toInt()
            val drawableInt: Int
            when (stateCode) {
                100 -> {
                    drawableInt = R.drawable.ic_weather_qing
                }
                101, 102, 103 -> {
                    drawableInt = R.drawable.ic_weather_duoyun
                }
                104 -> {
                    drawableInt = R.drawable.ic_weather_yin
                }
                200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213 -> {
                    drawableInt = R.drawable.ic_weather_feng
                }
                504, 507, 508 -> {
                    drawableInt = R.drawable.ic_weather_feng
                }
                300, 301, 306, 307, 308, 310, 311, 312, 313, 314, 315, 316, 317, 318, 399 -> {
                    drawableInt = R.drawable.ic_weather_zhongyu
                }
                302, 303, 304 -> {
                    drawableInt = R.drawable.ic_weather_lei
                }
                305, 309 -> {
                    drawableInt = R.drawable.ic_weather_xiaoyu
                }
                400, 404, 406 -> {
                    drawableInt = R.drawable.ic_weather_xiaoxue
                }
                401, 402, 403, 405, 407, 408, 409, 499 -> {
                    drawableInt = R.drawable.ic_weather_zhongxue
                }
                500, 501, 509, 510, 514, 515 -> {
                    drawableInt = R.drawable.ic_weather_wu
                }
                502, 511, 512, 513, 503 -> {
                    drawableInt = R.drawable.ic_weather_wumai
                }
                900, 901, 999 -> {
                    drawableInt = R.drawable.ic_weather_qing
                }
                else -> {
                    drawableInt = R.drawable.ic_weather_qing
                }
            }
            Log.e("=WeatherState=", drawableInt.toString())
            return drawableInt
        }
    }

    fun getWeatherNow(callback: BaseCallback<Weather>) {
        exception.networkIO().execute {
            try {
                val locationModel = LocationModel()
                val currentLocation = locationModel.getCurrentLocation(App.getInstance())

                val weatherNow = WeatherRequest.getWeatherNow(currentLocation)
                if (weatherNow != null) {
                    ConfigModel.set(WeatherNow, Gson().toJson(weatherNow), 1000 * 60 * 60 * 24)
                    exception.mainThread().execute {
                        callback.onSuccess(weatherNow)
                    }
                } else {
                    getWeatherCache(callback)
                }
            } catch (e: Exception) {
                exception.mainThread().execute {
                    callback.onError(e)
                }
            }
        }
    }

    fun getWeatherCache(callback: BaseCallback<Weather>) {
        exception.networkIO().execute {
            try {
                val get = ConfigModel.get(WeatherNow, false)
                if (get != "") {
                    val weather = Gson().fromJson<Weather>(get, Weather::class.java)
                    exception.mainThread().execute {
                        callback.onSuccess(weather)
                    }
                } else {
                    exception.mainThread().execute {
                        callback.onError(Exception())
                    }
                }
            } catch (e: Exception) {
                exception.mainThread().execute {
                    callback.onError(e)
                }
            }
        }
    }
}
