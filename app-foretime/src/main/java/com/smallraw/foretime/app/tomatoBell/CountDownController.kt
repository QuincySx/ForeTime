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
package com.smallraw.foretime.app.tomatoBell

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smallraw.foretime.app.common.extensions.printCallStack

class CountDownController : CountDownTick.OnCountDownTickListener {
    companion object {
        private var TAG = CountDownController::class.java.simpleName
    }

    // 刷新间隔
    private val mRefreshIntervalTime: Long = 25

    private var mCountDownStatusLiveData: MutableLiveData<@CountDownStatus Int> =
        MutableLiveData(CountDownStatus.READY)
    private var mSurplusTimeMillisLiveData = MutableLiveData<Long>(0)
    private var mImplementTimeMillisLiveData = MutableLiveData<Long>(0)

    private var mCountTickTimer = CountDownTick(0, this, mRefreshIntervalTime)

    override fun onCountDownStateChange(@CountDownStatus status: Int) {
        mCountDownStatusLiveData.postValue(status)
    }

    override fun onCountDownTotalMillis(totalMillis: Long) {
        mImplementTimeMillisLiveData.postValue(totalMillis)
    }

    override fun onCountDownTick(millisUntilFinished: Long) {
        mSurplusTimeMillisLiveData.postValue(millisUntilFinished)
    }

    fun start(implementTime: Long) {
        mCountTickTimer.setImplementTimeMillis(implementTime)
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

    fun reset(implementTime: Long) {
        if (mCountTickTimer.getStatus() == CountDownStatus.RUNNING) {
            mCountTickTimer.pause()
        }
        mCountTickTimer.setImplementTimeMillis(implementTime)
        mCountTickTimer.reset()
        Log.d(TAG, "CountDown Type reset ")
        printCallStack()
    }

    fun stop() {
        mCountTickTimer.cancel()
        Log.d(TAG, "CountDown stop")
    }

    fun getStatus(): LiveData<@CountDownStatus Int> = mCountDownStatusLiveData

    fun getSurplusTimeMillis(): LiveData<Long> = mSurplusTimeMillisLiveData

    fun getImplementTimeMillis(): LiveData<Long> = mImplementTimeMillisLiveData

    fun refreshTimeMillis() {
        val value = getStatus().value
        if (value == CountDownStatus.READY || value == CountDownStatus.FINISH) {
            mCountTickTimer.reset()
        }
    }

    private fun printCallStack() {
        Exception().printCallStack()
    }
}
