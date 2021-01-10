package com.smallraw.foretime.app.base

import android.app.Activity
import android.app.Application
import android.os.Handler
import android.os.Message
import android.view.View
import android.view.animation.Animation
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smallraw.foretime.app.base.databinding.DataBindingFragment

abstract class BaseFragment : DataBindingFragment() {
    companion object {
        private val HANDLER_MSG_PROMPT = 1
    }

    protected lateinit var mView: View
    protected val mHandler = Handler()
    protected var mAnimationLoaded = false

    private var mFragmentProvider: ViewModelProvider? = null
    private var mActivityProvider: ViewModelProvider? = null
    private var mApplicationProvider: ViewModelProvider? = null

    protected open fun <T : ViewModel?> getFragmentScopeViewModel(modelClass: Class<T>): T {
        if (mFragmentProvider == null) {
            mFragmentProvider = ViewModelProvider(this)
        }
        return mFragmentProvider!!.get(modelClass)
    }

    protected open fun <T : ViewModel?> getActivityScopeViewModel(modelClass: Class<T>): T {
        if (mActivityProvider == null && mActivity != null) {
            mActivityProvider = ViewModelProvider(mActivity!!)
        }
        return mActivityProvider!!.get(modelClass)
    }

    protected open fun <T : ViewModel?> getApplicationScopeViewModel(modelClass: Class<T>): T {
        if (mApplicationProvider == null && mActivity != null) {
            mApplicationProvider = ViewModelProvider(
                (mActivity?.applicationContext as IViewModelStoreApp).getViewModelStore(),
                getApplicationFactory(mActivity!!)
            )
        }
        return mApplicationProvider!!.get(modelClass)
    }

    private fun getApplicationFactory(activity: Activity): ViewModelProvider.Factory {
        checkActivity(this)
        val application = checkApplication(activity)
        return ViewModelProvider.AndroidViewModelFactory.getInstance(application)
    }

    @Throws(IllegalStateException::class)
    private fun checkApplication(activity: Activity): Application {
        return activity.application
            ?: throw IllegalStateException(
                "Your activity/fragment is not yet attached to "
                        + "Application. You can't request ViewModel before onCreate call."
            )
    }

    @Throws(IllegalStateException::class)
    private fun checkActivity(fragment: Fragment) {
        fragment.activity
            ?: throw IllegalStateException("Can't create ViewModelProvider for detached fragment")
    }

    override fun onCreateAnimation(transit: Int, enter: Boolean, nextAnim: Int): Animation? {
        //TODO 错开动画转场与 UI 刷新的时机，避免掉帧卡顿的现象
        mHandler.postDelayed(Runnable {
            if (!mAnimationLoaded) {
                mAnimationLoaded = true
                loadInitData()
            }
        }, 280)
        return super.onCreateAnimation(transit, enter, nextAnim)
    }

    protected open fun loadInitData() {}

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