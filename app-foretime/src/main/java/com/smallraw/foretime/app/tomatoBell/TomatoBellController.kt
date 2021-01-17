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
package com.smallraw.foretime.app.tomatoBell

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.BuildConfig

class TomatoBellController {
    private val mTomatoBellTypeLiveData: MutableLiveData<@CountDownType Int> by lazy {
        MutableLiveData(CountDownType.WORKING)
    }

    fun getTomatoBellType(): LiveData<@CountDownType Int> = mTomatoBellTypeLiveData

    /**
     * 根据类型获取时间
     */
    fun getCurrentTypeTime(): Long {
        if (BuildConfig.DEBUG) {
            return 1000 * 20
        } else {
            return when (mTomatoBellTypeLiveData.value) {
                CountDownType.WORKING -> App.getInstance().getCalendarConfig().focusTime
                CountDownType.REPOSE -> App.getInstance().getCalendarConfig().restTime
                else -> 0
            }
        }
    }

    fun setState(@CountDownType type: Int) {
        mTomatoBellTypeLiveData.postValue(type)
    }

    fun nextState() {
        if (getTomatoBellType().value == CountDownType.WORKING) {
            mTomatoBellTypeLiveData.postValue(CountDownType.REPOSE)
        } else {
            mTomatoBellTypeLiveData.postValue(CountDownType.WORKING)
        }
    }
}
