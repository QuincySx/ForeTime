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

