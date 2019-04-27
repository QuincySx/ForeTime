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

    /**
     * 已完成
     */
    private var isFinish: Boolean = false

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
        mHandlerThread.priority = Thread.MAX_PRIORITY
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

        isRunning = true
        isPause = false
        isFinish = false

        mHandler.sendEmptyMessageDelayed(0, mIntervalTime)
        return this
    }

    /**
     * 暂停倒计时
     */
    fun pause(): CountDownTick {
        mSurplusTimeMillis = mEndTimeMillis - SystemClock.elapsedRealtime()

        isRunning = false
        isPause = true
        isFinish = false

        mHandler.sendEmptyMessage(0)
        return this
    }

    /**
     * 继续执行运行
     * 调用此方法前可以设置状态
     */
    fun resume(): CountDownTick {
        mEndTimeMillis = SystemClock.elapsedRealtime() + mSurplusTimeMillis

        isRunning = true
        isPause = false
        isFinish = false

        mHandler.sendEmptyMessage(0)
        return this
    }

    /**
     * 重新设置
     */
    fun reset(): CountDownTick {
        mStartTimeMillis = Date()
        mSurplusTimeMillis = mImplementTimeMillis
        mEndTimeMillis = SystemClock.elapsedRealtime() + mSurplusTimeMillis

        isRunning = false
        isPause = false
        isFinish = false

        mHandler.sendEmptyMessage(0)
        return this
    }

    fun cancel(): CountDownTick {
        isRunning = false
        isPause = false
        isFinish = false

        mHandler.removeCallbacksAndMessages(null)
        return this
    }

    override fun handleMessage(msg: Message): Boolean {
        if (mSurplusTimeMillis <= 0) {
            isFinish = true
            mOnCountDownTickListener.onCountDownFinish()
        } else {
            if (mSurplusTimeMillis < mIntervalTime) {
                mHandler.sendEmptyMessageDelayed(0, mSurplusTimeMillis)
            } else {
                mHandler.sendEmptyMessageDelayed(0, mIntervalTime)
            }
        }
        if (isRunning && !isPause && !isFinish) {
            mSurplusTimeMillis = mEndTimeMillis - SystemClock.elapsedRealtime()
            mOnCountDownTickListener.onCountDownTick(mSurplusTimeMillis)
        }
        return false
    }

    fun setOnCountDownTickListener(listener: OnCountDownTickListener): CountDownTick {
        mOnCountDownTickListener = listener;
        return this
    }

    /**
     * 设置倒计时时长
     * hint：暂停后才可生效
     */
    fun setImplementTimeMillis(millis: Long) {
        if (!isRunning) {
            mImplementTimeMillis = millis
        }
    }

    /**
     * 获取剩余时间
     */
    fun getSurplusTimeMillis() = mSurplusTimeMillis

    /**
     * 获取总时间
     */
    fun getImplementTimeMillis() = mImplementTimeMillis

    fun isRuning() = isRunning

    fun isPause() = isPause

    fun isFinish() = isFinish
}
