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
        ActivityTomatoSettingBinding.inflate(layoutInflater)
    }

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_tomato_setting)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
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
