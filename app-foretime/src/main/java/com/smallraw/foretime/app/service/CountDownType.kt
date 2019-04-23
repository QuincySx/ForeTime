package com.smallraw.foretime.app.service

import android.support.annotation.IntDef
import com.smallraw.foretime.app.service.CountDownType.Companion.REPOSE
import com.smallraw.foretime.app.service.CountDownType.Companion.SPARE
import com.smallraw.foretime.app.service.CountDownType.Companion.WORKING


@IntDef(SPARE, WORKING, REPOSE)
public annotation class CountDownType {
    companion object {
        public const val SPARE = 0
        public const val WORKING = 1
        public const val REPOSE = 2
    }
}


