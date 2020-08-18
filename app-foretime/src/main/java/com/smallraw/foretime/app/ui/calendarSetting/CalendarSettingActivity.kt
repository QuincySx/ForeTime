package com.smallraw.foretime.app.ui.calendarSetting

import android.content.Intent
import android.os.Bundle

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.ui.backup.BackupActivity
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_calendar_setting.*

class CalendarSettingActivity : BaseTitleBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_setting)
        setTitleBarLeftImage(R.drawable.ic_back_black)

        layoutBackup.setOnClickListener {
            val i = Intent(this, BackupActivity::class.java)
            startActivity(i)
        }
    }
}
