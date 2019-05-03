package com.smallraw.foretime.app.ui.tomatoSetting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.widget.dialog.SelectLongTimeDialog
import com.smallraw.foretime.app.config.saveConfig
import com.smallraw.time.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_tomato_setting.*

class TomatoSettingActivity : BaseTitleBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tomato_setting)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        initView()
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        App.getInstance().getMusicConfig()
        tvLongTimeAbsorbed.text = "${App.getInstance().getCalendarConfig().focusTime / 60 / 1000} 分钟"
        tvLongTimeRest.text = "${App.getInstance().getCalendarConfig().restTime / 60 / 1000} 分钟"
        swbScreenAlwaysOn.isChecked = App.getInstance().getCalendarConfig().flipFocus

        swbPlayMusic.isChecked = App.getInstance().getMusicConfig().playMusic
        swbRestPlayMusic.isChecked = App.getInstance().getMusicConfig().restPlayMusic

        swbScreenAlwaysOn.setOnCheckedChangeListener { buttonView, isChecked ->
            App.getInstance().getCalendarConfig().screenAlwaysOn = isChecked
        }
        swbPlayMusic.setOnCheckedChangeListener { buttonView, isChecked ->
            App.getInstance().getMusicConfig().playMusic = isChecked
        }
        swbRestPlayMusic.setOnCheckedChangeListener { buttonView, isChecked ->
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
                            tvLongTimeAbsorbed.text = "$it 分钟"
                            App.getInstance().getCalendarConfig().focusTime = it.toInt() * 60 * 1000L
                        }
                        .build()
                        .showAtViewAuto(tvLongTimeAbsorbed)
            }
            R.id.tvLongTimeRest -> {
                SelectLongTimeDialog.Builder(this)
                        .setTitle("休息时长")
                        .setUnit("Min")
                        .select((App.getInstance().getCalendarConfig().restTime / 60 / 1000).toInt())
                        .setOnWheelCallback {
                            tvLongTimeRest.text = "$it 分钟"
                            App.getInstance().getCalendarConfig().restTime = it.toInt() * 60 * 1000L
                        }
                        .build()
                        .showAtViewAuto(tvLongTimeAbsorbed)
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
