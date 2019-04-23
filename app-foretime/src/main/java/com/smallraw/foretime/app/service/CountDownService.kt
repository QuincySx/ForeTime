package com.smallraw.foretime.app.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.smallraw.foretime.app.model.CountDownTick
import com.smallraw.foretime.app.service.CountDownType.Companion.REPOSE
import com.smallraw.foretime.app.service.CountDownType.Companion.SPARE
import com.smallraw.foretime.app.service.CountDownType.Companion.WORKING

class CountDownService : Service(), CountDownTick.OnCountDownTickListener {
    private var TAG = CountDownService::class.simpleName
    private var mType = CountDownType.SPARE
    private var mOnCountDownTickListener: CountDownTick.OnCountDownTickListener? = null

    override fun onCountDownTick(millisUntilFinished: Long) {
        Log.e(TAG, "Countdown Residual $millisUntilFinished second")
        mOnCountDownTickListener?.onCountDownTick(millisUntilFinished)
    }

    override fun onCountDownFinish() {
        Log.e(TAG, "success")
        mOnCountDownTickListener?.onCountDownFinish()
    }

    //刷新间隔
    private val mRefreshIntervalTime: Long = 25

    private var mCountTickTimer = CountDownTick(1000 * 15, this, mRefreshIntervalTime)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder = CountDownBinder()

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    inner class CountDownBinder : Binder() {
        fun start() {
            startCountDown()
        }

        fun pause() {
            pauseCountDown()
        }

        fun change(type: Int) {
            changeCountDown(type)
        }

        fun stop() {
            stopCountDown()
        }

        fun setListener(listener: CountDownTick.OnCountDownTickListener) {
            mOnCountDownTickListener = listener
        }

        fun getType() = getCountDownType()

        fun getStatus() = getCountDownStatus()
    }

    private fun getCountDownStatus(): Int {
        return if (mCountTickTimer.isPause())
            CountDownStatus.PAUSE
        else if (mCountTickTimer.isRuning())
            CountDownStatus.RUNNING
        else
            CountDownStatus.SPARE
    }

    fun getCountDownType() = mType

    fun startCountDown() {
        mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
        mCountTickTimer.start()
    }

    fun pauseCountDown() {
        mCountTickTimer.pause()
    }

    fun stopCountDown() {
        mCountTickTimer.pause()
    }

    fun changeCountDown(type: Int) {
        mType = type
        mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
        mCountTickTimer.resume()
    }

    /**
     * 根据类型获取时间
     */
    fun getCurrentTypeTime(): Long {
        return when (mType) {
            SPARE -> 0
            WORKING -> 15 * 1000
            REPOSE -> 10 * 1000
            else -> 0
        }
    }
}
