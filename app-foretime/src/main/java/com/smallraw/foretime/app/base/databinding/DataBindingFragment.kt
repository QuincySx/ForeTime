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
package com.smallraw.foretime.app.base.databinding

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.ApplicationInfo
import android.graphics.Color
import android.os.Bundle
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class DataBindingFragment : Fragment() {
    protected var mActivity: AppCompatActivity? = null
    private var mBinding: ViewDataBinding? = null
    private var mTvStrictModeTip: TextView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as AppCompatActivity
    }

    protected abstract fun initViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
    }

    protected abstract fun getDataBindingConfig(): DataBindingConfig

    @SuppressLint("SetTextI18n")
    protected open fun getBinding(): ViewDataBinding {
        if (isDebug() && mBinding != null) {
            if (mTvStrictModeTip == null) {
                mTvStrictModeTip = TextView(context).apply {
                    alpha = 0.5f
                    textSize = 16f
                    setBackgroundColor(Color.WHITE)
                    text = "Debug"
                }
                (mBinding?.root as ViewGroup?)?.addView(mTvStrictModeTip)
            }
        }
        return mBinding
            ?: throw RuntimeException("Please call after the life cycle of onCreateView")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dataBindingConfig = getDataBindingConfig()

        val binding: ViewDataBinding =
            DataBindingUtil.inflate(inflater, dataBindingConfig.getLayout(), container, false)
        binding.lifecycleOwner = this

        if (dataBindingConfig.getVmVariableId() != null) {
            binding.setVariable(
                dataBindingConfig.getVmVariableId()!!,
                dataBindingConfig.getStateViewModel()
            )
        }

        val bindingParams: SparseArray<*> = dataBindingConfig.getBindingParams()
        var i = 0
        val length = bindingParams.size()
        while (i < length) {
            binding.setVariable(bindingParams.keyAt(i), bindingParams.valueAt(i))
            i++
        }
        mBinding = binding
        return binding.root
    }

    fun isDebug(): Boolean {
        return mActivity!!.applicationContext.applicationInfo != null &&
            mActivity!!.applicationContext.applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE != 0
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mBinding?.unbind()
        mBinding = null
    }
}
