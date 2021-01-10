package com.smallraw.foretime.app.base.databinding

import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

/**
 * 与 DataBinding 绑定的 vm ObservableField 和 LiveData 都可以使用。
 * ObservableField 的特点是支持防抖，设置重复的值不会刷新视图；
 * LiveData 没有防抖。
 */
abstract class DataBindingActivity : AppCompatActivity() {
    companion object {
        private val TAG = DataBindingActivity::class.simpleName
    }

    private var mBinding: ViewDataBinding? = null
    private var mTvStrictModeTip: TextView? = null

    protected abstract fun initViewModel()

    protected abstract fun getDataBindingConfig(): DataBindingConfig

    protected open fun getBinding(): ViewDataBinding {
        if (isDebug() && mBinding != null) {
            if (mTvStrictModeTip == null) {
                mTvStrictModeTip = TextView(applicationContext).apply {
                    alpha = 0.4f
                    textSize = 14f
                    setBackgroundColor(Color.WHITE)
                    text = "Debug"
                }
                try {
                    (mBinding?.root as ViewGroup?)?.addView(mTvStrictModeTip)
                } catch (e: IllegalStateException) {
                }
            }
        }
        return mBinding ?: throw RuntimeException("Please call after the life cycle of onCreate")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        val dataBindingConfig: DataBindingConfig = getDataBindingConfig()

        val databindRootViewId = getDataBindingRootViewId()
        val binding: ViewDataBinding = if (databindRootViewId != -1) {
            super.setContentView(getDataBindingRootViewId())
            val bindingContentView = getDataBindingContentView()
            assert(bindingContentView == null) {
                Log.e(TAG, "getDataBindingContentView() 需要重写")
            }
            DataBindingUtil.inflate<ViewDataBinding>(
                layoutInflater,
                dataBindingConfig.getLayout(),
                bindingContentView,
                false
            ).apply {
                bindingContentView?.addView(root)
            }
        } else {
            DataBindingUtil.setContentView(this, dataBindingConfig.getLayout())
        }

        initDataBinding(binding, dataBindingConfig)
    }

    /**
     * 获取嵌套 databinding 布局的 ViewGroup
     * 如果 databinding 外面需要嵌套布局，则需要重写该方法。
     * 举个例子比如需要一个 BaseTitleActivity
     */
    open fun getDataBindingContentView(): ViewGroup? {
        return null
    }

    /**
     * 获取嵌套 databinding 布局的布局
     * 如果 databinding 外面需要嵌套布局，则需要重写该方法。配合 getDataBindingContentView 使用。
     */
    open fun getDataBindingRootViewId(): Int {
        return -1
    }

    open fun initDataBinding(binding: ViewDataBinding, dataBindingConfig: DataBindingConfig) {
        // DataBing 使用 LiveData，则需要在在视图控制器中为 DataBinding 绑定生命周期 Owner
        binding.lifecycleOwner = this

        if (dataBindingConfig.getVmVariableId() != null) {
            binding.setVariable(
                dataBindingConfig.getVmVariableId()!!,
                dataBindingConfig.getStateViewModel()
            )
        }

        val bindingParams: SparseArray<Any> = dataBindingConfig.getBindingParams()
        var i = 0
        val length = bindingParams.size()
        while (i < length) {
            binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i))
            i++
        }
        mBinding = binding
    }

    fun isDebug(): Boolean {
        return applicationContext.applicationInfo != null &&
                applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    override fun onDestroy() {
        super.onDestroy()
        mBinding?.unbind()
        mBinding = null
    }
}