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
package com.smallraw.foretime.app.common.widget.wheel;

/**
 * 选择器方法接口
 *
 * <p>Interface of WheelMonthPicker
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public interface IWheelDayPicker {
    /**
     * 获取日期选择器初始化时选择的日期
     *
     * @return 选择的日期
     */
    int getSelectedDay();

    /**
     * 设置日期选择器初始化时选择的日期
     *
     * @param day 选择的日期
     */
    void setSelectedDay(int day);

    /**
     * 获取当前选择的日期
     *
     * @return 选择的日期
     */
    int getCurrentDay();

    /**
     * 设置年份和月份
     *
     * @param year 年份
     * @param month 月份
     */
    void setYearAndMonth(int year, int month);

    /**
     * 获取年份
     *
     * @return 年份
     */
    int getYear();

    /**
     * 设置年份
     *
     * @param year ...
     */
    void setYear(int year);

    /**
     * 获取月份
     *
     * @return 月份
     */
    int getMonth();

    /**
     * 设置月份
     *
     * @param month 月份
     */
    void setMonth(int month);
}
