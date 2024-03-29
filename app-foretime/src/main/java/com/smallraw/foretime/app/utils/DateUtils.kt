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
package com.smallraw.foretime.app.utils

import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.ceil

private val df = object : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat {
        return SimpleDateFormat("yyyy-MM-dd", Locale.CHINA)
    }
}

private val df1 = object : ThreadLocal<DateFormat>() {
    override fun initialValue(): DateFormat {
        return SimpleDateFormat("MM月dd日", Locale.CHINA)
    }
}

fun dateFormat(date: Date): String {
    return df.get()?.format(date) ?: ""
}

fun monthDayFormat(date: Date): String {
    return df1.get()?.format(date) ?: ""
}

fun dateParse(date: String): Date {
    return df.get()?.parse(date) ?: Date()
}

fun differentDays(date1: Date, date2: Date): Int {
    val cal1 = Calendar.getInstance()
    cal1.time = date1

    val cal2 = Calendar.getInstance()
    cal2.time = date2
    val day1 = cal1.get(Calendar.DAY_OF_YEAR)
    val day2 = cal2.get(Calendar.DAY_OF_YEAR)

    val year1 = cal1.get(Calendar.YEAR)
    val year2 = cal2.get(Calendar.YEAR)
    if (year1 != year2) {
        // 同一年
        var timeDistance = 0
        for (i in year1 until year2) {
            if (i % 4 == 0 && i % 100 != 0 || i % 400 == 0) {
                // 闰年
                timeDistance += 366
            } else {
                // 不是闰年
                timeDistance += 365
            }
        }

        return timeDistance + (day2 - day1)
    } else {
        // 不同年
        return day2 - day1
    }
}

fun ms2Minutes(ms: Long): String {
    val second = ceil(ms / 1000.0).toInt()
    val minutes = second / 60
    val unSecond = second % 60
    val str = StringBuilder()
    if (minutes < 10) {
        str.append("0")
    }
    str.append(minutes)
    str.append(":")
    if (unSecond < 10) {
        str.append("0")
    }
    str.append(unSecond)
    return str.toString()
}
