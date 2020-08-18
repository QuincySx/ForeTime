package com.smallraw.foretime.app.tomatoBell

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.model.CountDownTick

class TomatoBellKit : CountDownTick.OnCountDownTickListener {
    companion object {
        private var TAG = TomatoBellKit::class.java.simpleName

        @Volatile
        private var instance: TomatoBellKit? = null

        fun getInstance(): TomatoBellKit {
            return instance ?: synchronized(this) {
                instance ?: TomatoBellKit().also { instance }
            }
        }
    }

    //刷新间隔
    private val mRefreshIntervalTime: Long = 25

    var mCountDownTypeLiveData = MutableLiveData<@CountDownType Int>()
    var mCountDownStatusLiveData = MutableLiveData<@CountDownStatus Int>()
    var mSurplusTimeMillisLiveData = MutableLiveData<Long>()
    var mImplementTimeMillisLiveData = MutableLiveData<Long>()

    private var mCountTickTimer = CountDownTick(0, this, mRefreshIntervalTime)

    override fun onCountDownTick(millisUntilFinished: Long) {
        mSurplusTimeMillisLiveData.postValue(millisUntilFinished)
    }

    override fun onCountDownFinish() {
        mSurplusTimeMillisLiveData.postValue(0)
        changeType()
    }

    /**
     * 修改状态
     */
    private fun changeType() {
        when (mCountDownTypeLiveData.value) {
            CountDownType.WORKING -> {
                reset(CountDownType.REPOSE)
                start()
            }
            CountDownType.REPOSE -> {
                reset(CountDownType.WORKING)
            }
            else -> {
                reset(CountDownType.WORKING)
            }
        }
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

    fun reset(type: Int) {
        resetCountDown(type)
    }

    fun stop() {
        stopCountDown()
    }

    fun getType() = mCountDownTypeLiveData.value

    fun getStatus() = mCountDownStatusLiveData.value

    fun getSurplusTimeMillis() = mSurplusTimeMillisLiveData.value

    fun getImplementTimeMillis() = mImplementTimeMillisLiveData.value

    fun refreshTimeMillis() {
        if (getStatus() == CountDownStatus.SPARE || getStatus() == CountDownStatus.FINISH) {
            mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
            mCountTickTimer.reset()
        }
    }

    private fun startCountDown() {
        mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
        mCountTickTimer.start()
        mCountDownStatusLiveData.value = getCountDownStatus()
        Log.d(TAG, "CountDown start")
    }

    private fun pauseCountDown() {
        mCountTickTimer.pause()
        mCountDownStatusLiveData.value = getCountDownStatus()
        Log.d(TAG, "CountDown pause")
    }

    private fun resumeCountDown() {
        mCountTickTimer.resume()
        mCountDownStatusLiveData.value = getCountDownStatus()
        Log.d(TAG, "CountDown resume")
    }

    private fun stopCountDown() {
        mCountTickTimer.cancel()
        mCountDownStatusLiveData.value = getCountDownStatus()
        Log.d(TAG, "CountDown stop")
    }

    private fun resetCountDown(type: Int) {
        mCountDownTypeLiveData.value = type
        if (mCountTickTimer.isRuning()) {
            mCountTickTimer.pause()
        }
        val implementTimeMillis = getCurrentTypeTime()
        mCountTickTimer.setImplementTimeMillis(implementTimeMillis)
        mImplementTimeMillisLiveData.value = implementTimeMillis
        mSurplusTimeMillisLiveData.value = implementTimeMillis
        mCountTickTimer.reset()
        mCountDownStatusLiveData.value = getCountDownStatus()
        Log.d(TAG, "CountDown Type reset ")
        printCallStatck()
    }

    fun printCallStatck() {
        val ex = Throwable()
        val stackElements = ex.stackTrace
        if (stackElements != null) {
            Log.e("Statck", "-----------------------------------")
            for (i in stackElements.indices) {
                val buffer = StringBuffer()
                buffer.append(stackElements[i].className).append("\t")
                buffer.append(stackElements[i].fileName).append("\t")
                buffer.append(stackElements[i].lineNumber).append("\t")
                buffer.append(stackElements[i].methodName)

                Log.e("Statck", buffer.toString())
            }
            Log.e("Statck", "-----------------------------------")
        }
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
        return when (mCountDownTypeLiveData.value) {
            CountDownType.WORKING -> App.getInstance().getCalendarConfig().focusTime
            CountDownType.REPOSE -> App.getInstance().getCalendarConfig().restTime
            else -> 0
        }
    }
}