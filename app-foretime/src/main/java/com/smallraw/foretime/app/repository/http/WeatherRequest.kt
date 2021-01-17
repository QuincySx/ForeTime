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
package com.smallraw.time.http

import android.util.Log
import com.google.gson.Gson
import com.smallraw.foretime.app.entity.Weather
import com.smallraw.foretime.app.repository.http.response.WeatherResponse
import com.smallraw.foretime.app.utils.is2String
import java.net.HttpURLConnection
import java.net.URL

class WeatherRequest {
    companion object {
        @JvmStatic
        fun getWeatherNow(location: String): Weather? {
            try {
                val url = URL("http://weather.smallraw.com/weather/now?location=$location")

                Log.e("==net request==", url.toURI().toString())
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connectTimeout = 12000
                connection.readTimeout = 5000
                connection.connect()

                if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = connection.inputStream
                    val response = is2String(inputStream)
                    Log.e("==net response==", response)
                    connection.disconnect()
                    inputStream.close()
                    val baseResponse = Gson().fromJson<WeatherResponse>(response, WeatherResponse::class.java)
                    if (baseResponse != null && baseResponse.error == 0) {
                        return baseResponse.data
                    } else {
                        return null
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }
}
