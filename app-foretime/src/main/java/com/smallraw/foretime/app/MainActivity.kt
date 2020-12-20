package com.smallraw.foretime.app

import android.os.Bundle
import com.smallraw.foretime.app.base.BaseActivity
import com.smallraw.lib.monitor.view.monitorMetrics

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.monitorMetrics(MainActivity::class.java.simpleName)
    }
}