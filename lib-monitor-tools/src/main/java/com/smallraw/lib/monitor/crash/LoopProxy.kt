package com.smallraw.lib.monitor.crash

import android.os.Handler
import android.os.Looper
import android.util.Log

object LoopProxy {
    fun proxyCrash(catchCall: ((Throwable) -> Unit)? = null) {
        Handler(Looper.getMainLooper()).post {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    // 异常发给 AppCenter
                    Log.e("Main Thread Catch", "如果软件 ANR 请检查报错信息是否在 Activity 的 onCreate() 方法。")
                    e.printStackTrace()
                    catchCall?.invoke(e)
                }
            }
        }
    }
}