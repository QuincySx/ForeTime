package com.smallraw.foretime.app.tomatoBell

import androidx.annotation.IntDef
import com.smallraw.foretime.app.tomatoBell.CountDownType.Companion.REPOSE
import com.smallraw.foretime.app.tomatoBell.CountDownType.Companion.WORKING


@Target(AnnotationTarget.TYPE, AnnotationTarget.VALUE_PARAMETER)
@IntDef(WORKING, REPOSE)
public annotation class CountDownType {
    companion object {
        public const val WORKING = 0
        public const val REPOSE = 1
    }
}


