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
package com.smallraw.foretime.app.config

import androidx.annotation.WorkerThread
import com.google.gson.Gson
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.config.ConfigName.Companion.CalendarConfig
import com.smallraw.foretime.app.config.ConfigName.Companion.MusicConfig
import com.smallraw.foretime.app.config.ConfigName.Companion.TaskConfig
import com.smallraw.foretime.app.entity.CalendarConfigInfo
import com.smallraw.foretime.app.entity.MusicConfigInfo
import com.smallraw.foretime.app.entity.TaskConfigInfo

internal val sConfigList = lazy {
    mapOf(
        Pair(TaskConfig, "config/DefTaskConfig.json" to TaskConfigInfo::class.java),
        Pair(CalendarConfig, "config/DefCalendarConfig.json" to CalendarConfigInfo::class.java),
        Pair(MusicConfig, "config/DefMusicConfig.json" to MusicConfigInfo::class.java)
    )
}.value

@WorkerThread
fun getDefTaskSettingConfig(): TaskConfigInfo = loadAssetsConfig(TaskConfig)

@WorkerThread
fun getDefCalendarSettingConfig(): CalendarConfigInfo = loadAssetsConfig(CalendarConfig)

@WorkerThread
fun getDefMusicSettingConfig(): MusicConfigInfo = loadAssetsConfig(MusicConfig)

internal fun <T> loadAssetsConfig(@ConfigName config: String): T {
    val pair = sConfigList.get(config)
    return loadAssetsConfig(pair!!.first, pair.second as Class<T>)
}

private fun <T> loadAssetsConfig(path: String, classOfT: Class<T>): T {
    val open = App.getInstance().assets.open(path)
    return Gson().fromJson<T>(String(open.readBytes(), Charsets.UTF_8), classOfT)
}
