package com.smallraw.foretime.app.service

import androidx.annotation.IntDef
import com.smallraw.foretime.app.service.CountDownType.Companion.REPOSE
import com.smallraw.foretime.app.service.CountDownType.Companion.WORKING


@IntDef(WORKING, REPOSE)
public annotation class CountDownType {
    companion object {
        public const val WORKING = 0
        public const val REPOSE = 1
    }
}


