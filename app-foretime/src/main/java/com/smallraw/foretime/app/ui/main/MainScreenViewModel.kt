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
package com.smallraw.foretime.app.ui.main

import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

@IntDef(MainPageIndex.TOMATO_BELL, MainPageIndex.CALENDAR)
annotation class MainPageIndex {
    companion object {
        const val TOMATO_BELL = 0
        const val CALENDAR = 1
    }
}

class MainScreenViewModel : ViewModel() {
    var tomatoBellSuspensionRes = ObservableField<@DrawableRes Int>()
    var calendarSuspensionRes = ObservableField<@DrawableRes Int>()
    var mainPageIndex = MutableLiveData<@MainPageIndex Int>(MainPageIndex.TOMATO_BELL)
}
