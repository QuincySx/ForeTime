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
