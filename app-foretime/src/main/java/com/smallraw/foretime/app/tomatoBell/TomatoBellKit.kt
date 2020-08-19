package com.smallraw.foretime.app.tomatoBell

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.BuildConfig

class TomatoBellKit : CountDownTick.OnCountDownTickListener {
    companion object {
        private var TAG = TomatoBellKit::class.java.simpleName

        @Volatile
        private var instance: TomatoBellKit? = null

        fun getInstance(): TomatoBellKit {
            return instance ?: synchronized(this) {
                instance ?: TomatoBellKit().also { instance = it }
            }
        }
    }

    //刷新间隔
    private val mRefreshIntervalTime: Long = 25

    var mCountDownTypeLiveData = MutableLiveData<@CountDownType Int>(CountDownType.WORKING)
    var mCountDownStatusLiveData = MutableLiveData<@CountDownStatus Int>(CountDownStatus.READY)
    var mSurplusTimeMillisLiveData = MutableLiveData<Long>(0)
    var mImplementTimeMillisLiveData = MutableLiveData<Long>(0)

    private var mCountTickTimer = CountDownTick(getCurrentTypeTime(), this, mRefreshIntervalTime)

    override fun onCountDownStateChange(status: Int) {
        mCountDownStatusLiveData.postValue(status)
        if (status == CountDownStatus.FINISH) {
            changeType()
        }
        if (status == CountDownStatus.CANCEL) {
            reset(CountDownType.WORKING)
        }
    }

    override fun onCountDownTotalMillis(totalMillis: Long) {
        mImplementTimeMillisLiveData.postValue(totalMillis)
    }

    override fun onCountDownTick(millisUntilFinished: Long) {
        mSurplusTimeMillisLiveData.postValue(millisUntilFinished)
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
        mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
        mCountTickTimer.start()
        Log.d(TAG, "CountDown start")
    }

    fun pause() {
        mCountTickTimer.pause()
        Log.d(TAG, "CountDown pause")
    }

    fun resume() {
        mCountTickTimer.resume()
        Log.d(TAG, "CountDown resume")
    }

    fun reset(type: Int) {
        mCountDownTypeLiveData.value = type
        if (mCountTickTimer.getStatus() == CountDownStatus.RUNNING) {
            mCountTickTimer.pause()
        }
        val implementTimeMillis = getCurrentTypeTime()
        mCountTickTimer.setImplementTimeMillis(implementTimeMillis)
        mCountTickTimer.reset()
        Log.d(TAG, "CountDown Type reset ")
        printCallStatck()
    }

    fun stop() {
        mCountTickTimer.cancel()
        Log.d(TAG, "CountDown stop")
    }

    fun getType() = mCountDownTypeLiveData.value

    fun getStatus() = mCountDownStatusLiveData.value

    fun getSurplusTimeMillis() = mSurplusTimeMillisLiveData.value

    fun getImplementTimeMillis() = mImplementTimeMillisLiveData.value

    fun refreshTimeMillis() {
        if (getStatus() == CountDownStatus.READY || getStatus() == CountDownStatus.FINISH) {
            mCountTickTimer.setImplementTimeMillis(getCurrentTypeTime())
            mCountTickTimer.reset()
        }
    }

    fun printCallStatck() {
        val ex = Throwable()
        val stackElements = ex.stackTrace
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

    /**
     * 根据类型获取时间
     */
    private fun getCurrentTypeTime(): Long {
        if (BuildConfig.DEBUG) {
            return 1000 * 20
        } else {
            return when (mCountDownTypeLiveData.value) {
                CountDownType.WORKING -> App.getInstance().getCalendarConfig().focusTime
                CountDownType.REPOSE -> App.getInstance().getCalendarConfig().restTime
                else -> 0
            }
        }
    }
}