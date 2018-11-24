package com.smallraw.foretime.app.ui.addTaskDay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.smallraw.foretime.app.R
import com.smallraw.time.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_add_countdown_day.*

class AddTaskDayActivity : BaseTitleBarActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context) {
            val intent = Intent(context, AddTaskDayActivity::class.java)
            ContextCompat.startActivity(context, intent, null)
        }

        private var CLOLR_LISR = arrayListOf<String>("#139EED","#EE386D","#FFC529","#9092A5","#FF8E9F","#2B0050","#FD92C4")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_countdown_day)
        setTitleBarLeftImage(R.drawable.ic_back_black)

        colorRecyclerView.setColors(CLOLR_LISR)

    }

}
