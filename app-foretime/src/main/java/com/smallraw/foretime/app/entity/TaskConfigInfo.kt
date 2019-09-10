package com.smallraw.foretime.app.entity

data class TaskConfigInfo(
        /**
         * 临近提醒
         */
        var expireReminder: Boolean = false,
        /**
         * 临近几天提醒
         */
        var expireReminderDays: Int = 1,
        /**
         * 自动归档
         */
        var autoArchiving: Boolean = false,
        /**
         * 到期几天自动归档
         */
        var autoArchivingDays: Int = 3,
        /**
         * 倒数日显示范围
         */
        var DaysMatterShowLimit: Int = 5,
        /**
         * 倒数日排序方式
         */
        var DayCountdownSort: Int = 1,
        /**
         * 累计日提醒
         */
        var DaysCumulativeReminder: Boolean = false,
        /**
         * 累计日排序方式
         */
        var DaysCumulativeSort: Int = 1
)