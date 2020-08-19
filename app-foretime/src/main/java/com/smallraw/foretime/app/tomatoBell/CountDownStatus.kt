package com.smallraw.foretime.app.tomatoBell

import androidx.annotation.IntDef
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.CANCEL
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.FINISH
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.PAUSE
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.READY
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.RUNNING

@IntDef(READY, RUNNING, PAUSE, FINISH, CANCEL)
public annotation class CountDownStatus {
    companion object {
        public const val READY = 0
        public const val RUNNING = 1
        public const val PAUSE = 2
        public const val FINISH = 3
        public const val CANCEL = 4
    }
}


