package com.smallraw.foretime.app.ui.setting

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.widget.dialog.SelectLongTimeDialog
import com.smallraw.time.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : BaseTitleBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        setTitleBarLeftImage(R.drawable.ic_back_black)
    }

    @SuppressLint("SetTextI18n")
    fun onClick(view: View) {
        when (view.id) {
            R.id.tvLongTimeAbsorbed -> {
                SelectLongTimeDialog.Builder(this)
                        .setTitle("专注时间")
                        .setUnit("Min")
                        .setOnWheelCallback {
                            tvLongTimeAbsorbed.text = "$it 分钟"
                        }
                        .build()
                        .showAtViewAuto(tvLongTimeAbsorbed)
            }
            else -> {
            }
        }
    }

}
