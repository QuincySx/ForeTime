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
package com.smallraw.foretime.app.base

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.StrictMode
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smallraw.foretime.app.base.databinding.DataBindingActivity
import com.smallraw.library.core.utils.BarUtils
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import qiu.niorgai.StatusBarCompat

abstract class BaseActivity : DataBindingActivity(), Handler.Callback {
    companion object {
        private const val HANDLER_MSG_PROMPT = 1
    }

    protected val mHandler = Handler()
    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase?.let { ViewPumpContextWrapper.wrap(it) })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        // Strict mode allows us to check that no writes or reads are blocking the UI thread.
        StrictMode.setThreadPolicy(
            StrictMode.ThreadPolicy.Builder()
                .detectDiskReads()
                .detectDiskWrites()
                .penaltyDeath()
                .build()
        )

        super.onCreate(savedInstanceState)
        StatusBarCompat.translucentStatusBar(this, true)

        if (useStatusBarLightMode()) {
            BarUtils.setStatusBarLightMode(this, true)
        }
    }

    open fun useStatusBarLightMode() = true

    protected open fun <T : ViewModel?> getActivityScopeViewModel(modelClass: Class<T>): T {
        if (mActivityProvider == null) {
            mActivityProvider = ViewModelProvider(this)
        }
        return mActivityProvider!!.get(modelClass)
    }

    protected open fun <T : ViewModel?> getApplicationScopeViewModel(modelClass: Class<T>): T {
        if (mApplicationProvider == null) {
            mApplicationProvider = ViewModelProvider(
                (this.applicationContext as IViewModelStoreApp).getViewModelStore(),
                getAppFactory(this)
            )
        }
        return mApplicationProvider!![modelClass]
    }

    private fun getAppFactory(activity: Activity): ViewModelProvider.Factory {
        val application: Application = checkApplication(activity)
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    @Throws(IllegalStateException::class)
    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to " +
                    "Application. You can't request ViewModel before onCreate call."
            )
    }

    fun showPrompt(@StringRes res: Int) {
        showPrompt(getString(res))
    }

    fun showPrompt(msg: String) {
        val message = Message.obtain()
        message.what = HANDLER_MSG_PROMPT
        message.obj = msg
        mHandler.sendMessage(message)
    }

    override fun handleMessage(msg: Message?): Boolean {
        when (msg?.what) {
            HANDLER_MSG_PROMPT -> {
                Toast.makeText(applicationContext, "${msg.obj}", Toast.LENGTH_LONG).show()
            }
        }
        return false
    }

    override fun onDestroy() {
        mHandler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}
