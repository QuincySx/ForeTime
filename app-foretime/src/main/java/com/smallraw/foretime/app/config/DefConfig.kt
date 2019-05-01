package com.smallraw.foretime.app.config

import com.google.gson.Gson
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.entity.CalendarInfo
import java.lang.ref.WeakReference

class DefConfig {
    companion object {
        @JvmStatic
        val mCalendarConfig = lazy {
            val open = App.getInstance().assets.open("config/DefCalendarConfig.json")
            WeakReference(Gson().fromJson<CalendarInfo>(String(open.readBytes(), Charsets.UTF_8), CalendarInfo::class.java))
        }.value

        @JvmStatic
        val mCalendarConfig = lazy {
            val open = App.getInstance().assets.open("config/DefCalendarConfig.json")
            WeakReference(Gson().fromJson<CalendarInfo>(String(open.readBytes(), Charsets.UTF_8), CalendarInfo::class.java))
        }.value
    }
}
