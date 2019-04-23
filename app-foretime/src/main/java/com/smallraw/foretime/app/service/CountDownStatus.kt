package com.smallraw.foretime.app.service

import android.support.annotation.IntDef
import com.smallraw.foretime.app.service.CountDownStatus.Companion.PAUSE
import com.smallraw.foretime.app.service.CountDownStatus.Companion.RUNNING
import com.smallraw.foretime.app.service.CountDownStatus.Companion.SPARE

@IntDef(SPARE, RUNNING, PAUSE)
public annotation class CountDownStatus {
    companion object {
        public const val SPARE = 0
        public const val RUNNING = 1
        public const val PAUSE = 2
    }
}


