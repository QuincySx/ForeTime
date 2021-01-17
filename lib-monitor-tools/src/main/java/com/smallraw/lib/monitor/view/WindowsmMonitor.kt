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
package com.smallraw.lib.monitor.view

import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.FrameMetrics
import android.view.Window

private val mMonitorHandlerThread = HandlerThread("monitor-tools-thread")
private val mMonitorHandler by lazy {
    mMonitorHandlerThread.start()
    Handler(mMonitorHandlerThread.looper)
}

/**
 * @author QuincySx
 * @see FrameMetrics
 */
fun Window.monitorMetrics(name: String = "") {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        return
    }
    this.addOnFrameMetricsAvailableListener(
        { _, frameMetrics, _ ->
            // 布局和测量时间
            val layoutMeasureDuration = frameMetrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION)
            // 布局绘制时间
            val drawDuration = frameMetrics.getMetric(FrameMetrics.DRAW_DURATION)
            Log.d(
                "monitor",
                """$name monitor view tims:
layoutMeasure time:${layoutMeasureDuration / 1000000.0} ms
draw time:${drawDuration / 1000000.0} ms
"""
            )
        },
        mMonitorHandler
    )
}
