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
 * 月份选择器方法接口
 *
 * <p>Interface of WheelMonthPicker
 *
 * @author AigeStudio 2016-07-12
 * @version 1
 */
public interface IWheelMonthPicker {
    /**
     * 获取月份选择器初始化时选择的月份
     *
     * @return 选择的月份
     */
    int getSelectedMonth();

    /**
     * 设置月份选择器初始化时选择的月份
     *
     * @param month 选择的月份
     */
    void setSelectedMonth(int month);

    /**
     * 获取当前选择的月份
     *
     * @return 当前选择的月份
     */
    int getCurrentMonth();
}
