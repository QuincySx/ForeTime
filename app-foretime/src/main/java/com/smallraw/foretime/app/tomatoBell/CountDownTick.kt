package com.smallraw.foretime.app.tomatoBell

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
    private val mIntervalTime: Long = 20
) : Handler.Callback {

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

    @CountDownStatus
    private var mStatus: Int = CountDownStatus.READY

    interface OnCountDownTickListener {
        /**
         * CountDownTimer 状态变化
         */
        fun onCountDownStateChange(@CountDownStatus status: Int)

        /**
         * CountDownTimer 倒计时更改时触发的事件
         */
        fun onCountDownTick(millisUntilFinished: Long)
    }

    init {
        mHandlerThread.priority = Thread.MAX_PRIORITY
        mHandlerThread.start()
        mHandler = Handler(mHandlerThread.looper, this)
    }

    fun getStatus(): Int {
        return mStatus
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

        mStatus = CountDownStatus.RUNNING

        mHandler.sendEmptyMessageDelayed(0, mIntervalTime)
        mOnCountDownTickListener.onCountDownStateChange(mStatus)
        return this
    }

    /**
     * 暂停倒计时
     */
    fun pause(): CountDownTick {
        mSurplusTimeMillis = mEndTimeMillis - SystemClock.elapsedRealtime()

        mStatus = CountDownStatus.PAUSE

        mHandler.sendEmptyMessage(0)
        mOnCountDownTickListener.onCountDownStateChange(mStatus)
        return this
    }

    /**
     * 继续执行运行
     * 调用此方法前可以设置状态
     */
    fun resume(): CountDownTick {
        mEndTimeMillis = SystemClock.elapsedRealtime() + mSurplusTimeMillis

        mStatus = CountDownStatus.RUNNING

        mHandler.sendEmptyMessage(0)
        mOnCountDownTickListener.onCountDownStateChange(mStatus)
        return this
    }

    /**
     * 重新设置
     */
    fun reset(): CountDownTick {
        mStartTimeMillis = Date()
        mSurplusTimeMillis = mImplementTimeMillis
        mEndTimeMillis = SystemClock.elapsedRealtime() + mSurplusTimeMillis

        mStatus = CountDownStatus.READY

        mHandler.sendEmptyMessage(0)
        mOnCountDownTickListener.onCountDownStateChange(mStatus)
        mOnCountDownTickListener.onCountDownTick(mSurplusTimeMillis)
        return this
    }

    /**
     * 取消执行
     */
    fun cancel(): CountDownTick {
        mStatus = CountDownStatus.CANCEL

        mHandler.removeCallbacksAndMessages(null)
        mOnCountDownTickListener.onCountDownStateChange(mStatus)
        return this
    }

    override fun handleMessage(msg: Message): Boolean {
        if (mSurplusTimeMillis <= 0) {
            mStatus = CountDownStatus.FINISH
            mOnCountDownTickListener.onCountDownTick(0)
            mOnCountDownTickListener.onCountDownStateChange(mStatus)
        } else {
            if (mSurplusTimeMillis < mIntervalTime) {
                mHandler.sendEmptyMessageDelayed(0, mSurplusTimeMillis)
            } else {
                mHandler.sendEmptyMessageDelayed(0, mIntervalTime)
            }
        }
        if (mStatus == CountDownStatus.RUNNING) {
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
        if (mStatus != CountDownStatus.RUNNING) {
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
}
