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
 * 年份选择器方法接口
 *
 * <p>Interface of WheelYearPicker
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public interface IWheelYearPicker {
    /**
     * 设置年份范围
     *
     * @param start 开始的年份
     * @param end 结束的年份
     */
    void setYearFrame(int start, int end);

    /**
     * 获取开始的年份
     *
     * @return 开始的年份
     */
    int getYearStart();

    /**
     * 设置开始的年份
     *
     * @param start 开始的年份
     */
    void setYearStart(int start);

    /**
     * 获取结束的年份
     *
     * @return 结束的年份
     */
    int getYearEnd();

    /**
     * 设置结束的年份
     *
     * @param end 结束的年份
     */
    void setYearEnd(int end);

    /**
     * 获取年份选择器初始化时选中的年份
     *
     * @return 年份选择器初始化时选中的年份
     */
    int getSelectedYear();

    /**
     * 设置年份选择器初始化时选中的年份
     *
     * @param year 年份选择器初始化时选中的年份
     */
    void setSelectedYear(int year);

    /**
     * 获取当前选中的年份
     *
     * @return 当前选中的年份
     */
    int getCurrentYear();
}
