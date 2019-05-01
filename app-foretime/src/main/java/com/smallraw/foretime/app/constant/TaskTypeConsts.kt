package com.smallraw.foretime.app.constant

import android.support.annotation.IntDef

import com.smallraw.foretime.app.constant.TaskTypeConsts.Companion.ALL
import com.smallraw.foretime.app.constant.TaskTypeConsts.Companion.COUNTDOWN_DAY
import com.smallraw.foretime.app.constant.TaskTypeConsts.Companion.CUMULATIVE_DAY

@IntDef(ALL, CUMULATIVE_DAY, COUNTDOWN_DAY)
annotation class TaskTypeConsts {
    companion object {
        const val ALL = -1
        /**
         * 累计日
         */
        const val CUMULATIVE_DAY = 0
        /**
         * 倒数日
         */
        const val COUNTDOWN_DAY = 1
    }
}
