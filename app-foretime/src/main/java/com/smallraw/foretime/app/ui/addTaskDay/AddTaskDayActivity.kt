package com.smallraw.foretime.app.ui.addTaskDay

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.view.View
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.widget.dialog.SelectDateDialog
import com.smallraw.time.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_add_countdown_day.*
import java.util.*

class AddTaskDayActivity : BaseTitleBarActivity() {

    companion object {
        const val DaysMatter = 1
        const val DaysCumulative = 2

        private const val DAYTYPE_EXTRA = "daytype_extra"

        @JvmStatic
        fun start(context: Context, @DayType daytype: Int) {
            val intent = Intent(context, AddTaskDayActivity::class.java)
            intent.putExtra(DAYTYPE_EXTRA, daytype)
            ContextCompat.startActivity(context, intent, null)
        }

        private val CLOLR_LISR = arrayListOf<String>("#139EED", "#EE386D", "#FFC529", "#9092A5", "#FF8E9F", "#2B0050", "#FD92C4")
    }

    @DayType
    var mCurrentDayType = DaysMatter

    var mCalendar = Calendar.getInstance()

    @IntDef(DaysMatter, DaysCumulative)
    annotation class DayType

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_countdown_day)
        setTitleBarLeftImage(R.drawable.ic_back_black)

        mCurrentDayType = intent.getIntExtra(DAYTYPE_EXTRA, DaysMatter)

        dispatchView()

        tvTargeDate.setOnClickListener {
            SelectDateDialog.Builder(this)
                    .setOnWheelCallback { year, _month, day ->
                        tvTargeDate.text = "${year}-${_month}-${day}"
                    }
                    .build()
                    .showAtViewAuto(tvTargeDate)
        }
        mCalendar.timeInMillis = System.currentTimeMillis()
        tvTargeDate.text = "${mCalendar.get(Calendar.YEAR)}-${mCalendar.get(Calendar.MONTH) + 1}-${mCalendar.get(Calendar.DAY_OF_MONTH)}"

        colorRecyclerView.setColors(CLOLR_LISR)

    }

    private fun dispatchView() {
        when (mCurrentDayType) {
            DaysMatter -> {
                titleName.hint = "倒数日名称"
                layoutCyclePeriod.visibility = View.VISIBLE
            }
            DaysCumulative -> {
                titleName.hint = "累计日名称"
                layoutCyclePeriod.visibility = View.GONE
            }
        }
    }

}
