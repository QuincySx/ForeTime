package com.smallraw.foretime.app.model

import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.os.SystemClock
import java.util.*

class CountDownTick
/**
 * CountDownTick 构造方法
 *
 * @param implementTimeMillis 需要执行的毫秒数
 * @param intervalTime        刷新间隔
 */
@JvmOverloads constructor(
        /**
         * 要执行的时间
         */
        private var mImplementTimeMillis: Long,
        private var mOnCountDownTickListener: OnCountDownTickListener,
        /**
         * 刷新间隔
         */
        private val mIntervalTime: Long = 20) : Handler.Callback {

    private val mHandlerThread = HandlerThread("CountDownTick-Thread")
    private val mHandler: Handler

    /**
     * 任务开始时间
     */
    private var mStartTimeMillis: Date? = null

    /**
     * 任务结束时间，暂停会增加
     */
    private var mEndTimeMillis: Long = 0

    /**
     * 剩余时间
     */
    private var mSurplusTimeMillis: Long = 0

    /**
     * 倒计时中
     */
    private var isRunning: Boolean = false

    /**
     * 暂停中
     */
    private var isPause: Boolean = false

    interface OnCountDownTickListener {

        /**
         * CountDownTimer 更改时触发的事件
         */
        fun onCountDownTick(millisUntilFinished: Long)

        /**
         * CountDownTimer 完成时触发的事件
         */
        fun onCountDownFinish()

    }

    init {
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper, this)
    }

    /**
     * 启动时间倒计时
     *
     * @return
     */
    @Synchronized
    fun start(): CountDownTick {
        mStartTimeMillis = Date()
        mSurplusTimeMillis = mImplementTimeMillis
        mEndTimeMillis = SystemClock.elapsedRealtime() + mSurplusTimeMillis
        mHandler.sendEmptyMessageDelayed(0, mIntervalTime)
        isRunning = true
        isPause = false
        return this
    }

    fun pause(): CountDownTick {
        isPause = true
        mHandler.sendEmptyMessage(0)
        mSurplusTimeMillis = mEndTimeMillis - SystemClock.elapsedRealtime()
        return this
    }

    fun resume(): CountDownTick {
        isPause = false
        mEndTimeMillis = SystemClock.elapsedRealtime() + mSurplusTimeMillis
        mHandler.sendEmptyMessage(0)
        return this
    }

    fun cancel(): CountDownTick {
        isRunning = false
        mHandler.removeCallbacksAndMessages(null)
        return this
    }

    override fun handleMessage(msg: Message): Boolean {
        if (mSurplusTimeMillis <= 0) {
            mOnCountDownTickListener?.onCountDownFinish()
        }else{
            mHandler.sendEmptyMessageDelayed(0, mIntervalTime)
        }
        if (isRunning) {
            mSurplusTimeMillis = mEndTimeMillis - SystemClock.elapsedRealtime()
            mOnCountDownTickListener?.onCountDownTick(mSurplusTimeMillis)
        }
        return false
    }

    fun setOnCountDownTickListener(listener: OnCountDownTickListener): CountDownTick {
        mOnCountDownTickListener = listener;
        return this
    }

    fun setImplementTimeMillis(millis: Long) {
        if (!isRunning) {
            mImplementTimeMillis = millis
        }
    }

    fun isRuning() = isRunning

    fun isPause() = isPause
}
