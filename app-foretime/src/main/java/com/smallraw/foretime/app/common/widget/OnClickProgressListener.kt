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
package com.smallraw.foretime.app.common.widget

import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.MotionEvent
import android.view.View

abstract class OnClickProgressListener : Handler.Callback, View.OnTouchListener {
    companion object {
        private val DELAY_CLICK = 1
        private val DELAY_CLICK_START = 2
    }

    private var isClick = false
    private val mHandler = Handler(Looper.getMainLooper(), this)
    private val mLongPressDelay = 1200.0
    private var mStartClickTime = 0L

    private fun onTouchEvent(event: MotionEvent) {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mHandler.removeCallbacksAndMessages(null)
                performLongClick()
            }
            MotionEvent.ACTION_MOVE -> {
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                isClick = false
                mHandler.removeCallbacksAndMessages(null)
                onCancel()
            }
        }
    }

    private fun performLongClick() {
        val message = Message.obtain(mHandler, DELAY_CLICK_START)
        mHandler.sendMessageDelayed(message, 230)
    }

    override fun handleMessage(msg: Message?): Boolean {
        when (msg?.what) {
            DELAY_CLICK -> {
                if (isClick) {
                    if (System.currentTimeMillis() - mLongPressDelay <= mStartClickTime) {
                        val message = Message.obtain(mHandler, DELAY_CLICK)
                        mHandler.sendMessageDelayed(message, 10)
                        val progress = (System.currentTimeMillis() - mStartClickTime) / mLongPressDelay
                        onProgress(progress)
                    } else {
                        onProgress(1.0)
                        onSuccess()
                    }
                } else {
                    onCancel()
                }
            }
            DELAY_CLICK_START -> {
                mStartClickTime = System.currentTimeMillis()
                isClick = true
                onStart()
                val message = Message.obtain(mHandler, DELAY_CLICK)
                mHandler.sendMessageDelayed(message, 10)
            }
        }
        return false
    }

    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        onTouchEvent(event)
        return false
    }

    abstract fun onStart()
    abstract fun onProgress(progress: Double)
    abstract fun onSuccess()
    abstract fun onCancel()
}
