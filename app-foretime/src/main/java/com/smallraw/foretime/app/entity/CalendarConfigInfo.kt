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
