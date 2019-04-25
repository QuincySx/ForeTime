package com.smallraw.foretime.app.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.smallraw.foretime.app.model.CountDownTick
import com.smallraw.foretime.app.service.CountDownType.Companion.REPOSE
import com.smallraw.foretime.app.service.CountDownType.Companion.WORKING

class CountDownService : Service(), CountDownTick.OnCountDownTickListener {
    private var TAG = CountDownService::class.java.simpleName
    private var mType = CountDownType.WORKING
    private var onCountDownServiceListener: OnCountDownServiceListener? = null

    override fun onCountDownTick(millisUntilFinished: Long) {
        Log.d(TAG, "Countdown Residual $millisUntilFinished second")
        onCountDownServiceListener?.onCountDownTick(millisUntilFinished)
    }

    override fun onCountDownFinish() {
        Log.d(TAG, "success")
        onCountDownServiceListener?.onCountDownFinish()
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
        fun getService() = this@CountDownService
    }

    fun start() {
        startCountDown()
    }

    fun pause() {
        pauseCountDown()
    }

    fun resume() {
        resumeCountDown()
    }

    fun change(type: Int) {
        changeCountDown(type)
    }

    fun stop() {
        stopCountDown()
    }

    fun setCountDownTickListener(listener: OnCountDownServiceListener) {
        onCountDownServiceListener = listener
    }

    fun getType() = getCountDownType()

    fun getStatus() = getCountDownStatus()

    fun getCountDownType() = mType

    fun getSurplusTimeMillis() = mCountTickTimer.getSurplusTimeMillis()

    fun getImplementTimeMillis() = mCountTickTimer.getImplementTimeMillis()

    private fun startCountDown() {
        mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
        mCountTickTimer.start()
        Log.d(TAG, "CountDown Type Change")
        onCountDownServiceListener?.onCountDownChange()
    }

    private fun pauseCountDown() {
        mCountTickTimer.pause()
    }

    private fun resumeCountDown() {
        mCountTickTimer.resume()
    }

    private fun stopCountDown() {
        mCountTickTimer.cancel()
    }

    private fun changeCountDown(type: Int) {
        mType = type
        mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
        mCountTickTimer.resume()
        Log.d(TAG, "CountDown Type Change")
        onCountDownServiceListener?.onCountDownChange()
    }

    private fun getCountDownStatus(): Int {
        return if (mCountTickTimer.isPause())
            CountDownStatus.PAUSE
        else if (mCountTickTimer.isRuning())
            CountDownStatus.RUNNING
        else if (mCountTickTimer.isFinish())
            CountDownStatus.FINISH
        else
            CountDownStatus.SPARE
    }

    /**
     * 根据类型获取时间
     */
    private fun getCurrentTypeTime(): Long {
        return when (mType) {
            WORKING -> 15 * 1000
            REPOSE -> 10 * 1000
            else -> 0
        }
    }

    interface OnCountDownServiceListener {

        /**
         * CountDownService 时间变化触发的事件
         */
        fun onCountDownTick(millisUntilFinished: Long)

        /**
         * CountDownService 变更任务时触发的事件
         */
        fun onCountDownChange()

        /**
         * CountDownService 完成任务时触发的事件
         */
        fun onCountDownFinish()

    }
}
