package com.smallraw.foretime.app

import android.os.Bundle
import com.smallraw.foretime.app.base.BaseActivity
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.lib.monitor.view.monitorMetrics

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.monitorMetrics(MainActivity::class.java.simpleName)
    }

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_main)
    }
}