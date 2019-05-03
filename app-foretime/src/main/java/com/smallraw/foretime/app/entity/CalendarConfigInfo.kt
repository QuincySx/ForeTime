package com.smallraw.foretime.app.entity

data class CalendarConfigInfo(
        /**
         * 沉浸模式
         */
        var automatic: Boolean = false,
        /**
         * 工作时长
         */
        var focusTime: Long = 25 * 60 * 1000,

        /**
         * 休息时长
         */
        var restTime: Long = 5 * 60 * 1000,

        /**
         * 长休息时长
         */
        var restLongTime: Long = 10 * 60 * 1000,

        /**
         * 长休息间隔
         */
        var restLonginterval: Int = 3,

        /**
         * 翻转自动专注
         */
        var flipFocus: Boolean = false,

        /**
         * 屏幕常亮
         */
        var screenAlwaysOn: Boolean = false
)
