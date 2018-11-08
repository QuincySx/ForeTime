package com.smallraw.foretime.app.ui.addTaskDay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat

import com.smallraw.foretime.app.R
import com.smallraw.time.base.BaseTitleBarActivity

class AddTaskDayActivity : BaseTitleBarActivity() {
    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, AddTaskDayActivity::class.java)
            ContextCompat.startActivity(context, intent, null)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_countdown_day)
        setTitleBarLeftImage(R.drawable.ic_back_black)
    }

}
