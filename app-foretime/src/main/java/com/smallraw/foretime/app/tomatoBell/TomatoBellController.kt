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
}