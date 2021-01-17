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
package com.smallraw.foretime.app.ui.tomatoSetting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.common.widget.dialog.SelectLongTimeDialog
import com.smallraw.foretime.app.config.saveConfig
import com.smallraw.foretime.app.databinding.ActivityTomatoSettingBinding

class TomatoSettingActivity : BaseTitleBarActivity() {
    private val mBinding by lazy {
        getBinding() as ActivityTomatoSettingBinding
    }

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_tomato_setting)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        App.getInstance().getMusicConfig()
        mBinding.tvLongTimeAbsorbed.text = "${App.getInstance().getCalendarConfig().focusTime / 60 / 1000} 分钟"
        mBinding.tvLongTimeRest.text = "${App.getInstance().getCalendarConfig().restTime / 60 / 1000} 分钟"
        mBinding.swbScreenAlwaysOn.isChecked = App.getInstance().getCalendarConfig().flipFocus

        mBinding.swbPlayMusic.isChecked = App.getInstance().getMusicConfig().playMusic
        mBinding.swbRestPlayMusic.isChecked = App.getInstance().getMusicConfig().restPlayMusic

        mBinding.swbScreenAlwaysOn.setOnCheckedChangeListener { buttonView, isChecked ->
            App.getInstance().getCalendarConfig().screenAlwaysOn = isChecked
        }
        mBinding.swbPlayMusic.setOnCheckedChangeListener { buttonView, isChecked ->
            App.getInstance().getMusicConfig().playMusic = isChecked
        }
        mBinding.swbRestPlayMusic.setOnCheckedChangeListener { buttonView, isChecked ->
            App.getInstance().getMusicConfig().restPlayMusic = isChecked
        }
    }

    @SuppressLint("SetTextI18n")
    fun onClick(view: View) {
        when (view.id) {
            R.id.tvLongTimeAbsorbed -> {
                SelectLongTimeDialog.Builder(this)
                    .setTitle("专注时间")
                    .setUnit("Min")
                    .select((App.getInstance().getCalendarConfig().focusTime / 60 / 1000).toInt())
                    .setOnWheelCallback {
                        mBinding.tvLongTimeAbsorbed.text = "$it 分钟"
                        App.getInstance().getCalendarConfig().focusTime = it.toInt() * 60 * 1000L
                    }
                    .atViewAuto(mBinding.tvLongTimeAbsorbed)
                    .build()
                    .show()
            }
            R.id.tvLongTimeRest -> {
                SelectLongTimeDialog.Builder(this)
                    .setTitle("休息时长")
                    .setUnit("Min")
                    .select((App.getInstance().getCalendarConfig().restTime / 60 / 1000).toInt())
                    .setOnWheelCallback {
                        mBinding.tvLongTimeRest.text = "$it 分钟"
                        App.getInstance().getCalendarConfig().restTime = it.toInt() * 60 * 1000L
                    }
                    .atViewAuto(mBinding.tvLongTimeAbsorbed)
                    .build()
                    .show()
            }
            else -> {
            }
        }
    }

    override fun onDestroy() {
        App.getInstance().getAppExecutors().diskIO().execute {
            App.getInstance().getMusicConfig().saveConfig()
            App.getInstance().getCalendarConfig().saveConfig()
        }
        super.onDestroy()
    }
}
