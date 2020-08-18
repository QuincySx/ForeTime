package com.smallraw.foretime.app.base

import android.os.Handler
import android.os.Message
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import android.view.View

abstract class BaseFragment : Fragment() {
    companion object {
        private val HANDLER_MSG_PROMPT = 1
    }

    protected lateinit var mView: View
    protected val mHandler = Handler()

    fun showPrompt(@StringRes res: Int) {
        showPrompt(getString(res))
    }

    fun showPrompt(msg: String) {
        mHandler.sendMessage(
            Message.obtain().apply {
                what = HANDLER_MSG_PROMPT
                obj = msg
            })
    }
}