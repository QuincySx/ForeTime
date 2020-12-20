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
 * @see  FrameMetrics
 */
fun Window.monitorMetrics(name: String = "") {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
        return
    }
    this.addOnFrameMetricsAvailableListener(
        { window, frameMetrics, dropCountSinceLastInvocation ->
            // 布局和测量时间
            val layoutMeasureDuration = frameMetrics.getMetric(FrameMetrics.LAYOUT_MEASURE_DURATION)
            // 布局绘制时间
            val drawDuration = frameMetrics.getMetric(FrameMetrics.DRAW_DURATION)
            Log.d(
                "monitor",
                """${name} monitor view tims: 
layoutMeasure time:${layoutMeasureDuration / 1000000.0} ms
draw time:${drawDuration / 1000000.0} ms 
"""
            )
        },
        mMonitorHandler
    )
}