package com.smallraw.foretime.app.ui.backup

import android.os.Bundle

import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseTitleBarActivity

class BackupActivity : BaseTitleBarActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_backup)
        setTitleBarLeftImage(R.drawable.ic_back_black)
    }
}
