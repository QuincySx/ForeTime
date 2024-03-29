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

import androidx.lifecycle.LiveData

class TomatoBellKit {
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

    private val mCountDownController by lazy { CountDownController() }
    private val mTomatoBellController by lazy { TomatoBellController() }

    fun actionLong() {
        val type = getType().value
        val status = getStatus().value

        if (null == type || null == status) {
            return
        }

        when (type) {
            CountDownType.WORKING -> {
                when (status) {
                    CountDownStatus.PAUSE, CountDownStatus.RUNNING -> {
                        reset(CountDownType.REPOSE)
                        start()
                    }
                }
            }
            CountDownType.REPOSE -> {
                when (status) {
                    CountDownStatus.RUNNING -> {
                        reset(CountDownType.WORKING)
                    }
                }
            }
        }
    }

    fun action() {
        val type = getType().value
        val status = getStatus().value

        if (null == type || null == status) {
            return
        }

        when (type) {
            CountDownType.WORKING -> {
                when (status) {
                    CountDownStatus.READY -> {
                        start()
                    }
                    CountDownStatus.RUNNING -> {
                        pause()
                    }
                    CountDownStatus.PAUSE -> {
                        resume()
                    }
                    else -> {
                        mTomatoBellController.nextState()
                        reset()
                    }
                }
            }
            CountDownType.REPOSE -> {
                when (status) {
                    CountDownStatus.READY -> {
                        start()
                    }
                    else -> {
                        mTomatoBellController.nextState()
                        reset()
                    }
                }
            }
            else -> {
                start()
            }
        }
    }

    fun nextState() = mTomatoBellController.nextState()

    fun start() {
        mCountDownController.start(mTomatoBellController.getCurrentTypeTime())
    }

    fun pause() = mCountDownController.pause()

    fun resume() = mCountDownController.resume()

    fun reset() {
        mCountDownController.reset(mTomatoBellController.getCurrentTypeTime())
    }

    fun reset(@CountDownType type: Int) {
        mTomatoBellController.setState(type)
        mCountDownController.reset(mTomatoBellController.getCurrentTypeTime())
    }

    fun stop() = mCountDownController.stop()

    fun getType(): LiveData<@CountDownType Int> = mTomatoBellController.getTomatoBellType()

    fun getStatus() = mCountDownController.getStatus()
    fun getImplementTimeMillis(): LiveData<Long> = mCountDownController.getImplementTimeMillis()
    fun getSurplusTimeMillis(): LiveData<Long> = mCountDownController.getSurplusTimeMillis()
}
