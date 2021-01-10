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

    fun nextTomatoBellState() {
        mTomatoBellController.nextState()
        reset()
    }

    fun start() {
        mCountDownController.start(mTomatoBellController.getCurrentTypeTime())
    }

    fun pause() = mCountDownController.pause()

    fun resume() = mCountDownController.resume()

    fun reset() {
        mCountDownController.reset(mTomatoBellController.getCurrentTypeTime())
    }

    fun stop() = mCountDownController.stop()

    fun getType(): LiveData<@CountDownType Int> = mTomatoBellController.getTomatoBellType()

    fun getStatus() = mCountDownController.getStatus()

}