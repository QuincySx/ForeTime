package com.smallraw.foretime.app.tomatoBell

import androidx.annotation.IntDef
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.FINISH
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.PAUSE
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.RUNNING
import com.smallraw.foretime.app.tomatoBell.CountDownStatus.Companion.SPARE

@IntDef(SPARE, RUNNING, PAUSE, FINISH)
public annotation class CountDownStatus {
    companion object {
        public const val SPARE = 0
        public const val RUNNING = 1
        public const val PAUSE = 2
        public const val FINISH = 3
    }
}


