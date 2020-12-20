package com.smallraw.foretime.app.ui.calendarSetting

import android.content.Intent
import android.os.Bundle

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.ui.backup.BackupActivity
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import com.smallraw.foretime.app.databinding.ActivityCalendarSettingBinding

class CalendarSettingActivity : BaseTitleBarActivity() {
    private val mBinding by lazy {
        ActivityCalendarSettingBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setTitleBarLeftImage(R.drawable.ic_back_black)

        mBinding.layoutBackup.setOnClickListener {
            val i = Intent(this, BackupActivity::class.java)
            startActivity(i)
        }
    }
}
