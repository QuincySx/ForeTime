package com.smallraw.foretime.app.entity

data class CalendarInfo(
        /**
         * 工作时长
         */
        var focusTime: Long = 0,

        /**
         * 休息时长
         */
        var restTime: Long = 0,

        /**
         * 长休息时长
         */
        var restLongTime: Long = 0,

        /**
         * 长休息间隔
         */
        var restLonginterval: Int = 3,

        /**
         * 翻转自动专注
         */
        var flipFocus: Boolean = false
)
