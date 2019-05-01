package com.smallraw.foretime.app.config

import com.google.gson.Gson
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.entity.CalendarConfigInfo
import com.smallraw.foretime.app.entity.MusicConfigInfo
import com.smallraw.foretime.app.entity.TaskConfigInfo
import java.lang.ref.WeakReference

class DefConfig {
    companion object {
        @JvmStatic
        val mCalendarSettingConfig = lazy {
            val open = App.getInstance().assets.open("config/DefCalendarConfig.json")
            WeakReference(Gson().fromJson<CalendarConfigInfo>(String(open.readBytes(), Charsets.UTF_8), CalendarConfigInfo::class.java))
        }.value

        @JvmStatic
        val mMusicSettingConfig = lazy {
            val open = App.getInstance().assets.open("config/DefMusicConfig.json")
            WeakReference(Gson().fromJson<MusicConfigInfo>(String(open.readBytes(), Charsets.UTF_8), MusicConfigInfo::class.java))
        }.value

        @JvmStatic
        val mTaskSettingConfig = lazy {
            val open = App.getInstance().assets.open("config/DefTaskConfig.json.json")
            WeakReference(Gson().fromJson<TaskConfigInfo>(String(open.readBytes(), Charsets.UTF_8), TaskConfigInfo::class.java))
        }.value
    }
}
