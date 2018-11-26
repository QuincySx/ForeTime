package com.smallraw.foretime.app.ui.addTaskDay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.view.View
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.widget.dialog.MultipleItemDialog
import com.smallraw.foretime.app.common.widget.dialog.SelectDateDialog
import com.smallraw.time.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_add_countdown_day.*
import java.text.SimpleDateFormat
import java.util.*

class AddTaskDayActivity : BaseTitleBarActivity() {

    companion object {
        const val DaysMatter = 1
        const val DaysCumulative = 2

        private const val DAY_TYPE_EXTRA = "day_type_extra"

        @JvmStatic
        fun start(context: Context, @DayType daytype: Int) {
            val intent = Intent(context, AddTaskDayActivity::class.java)
            intent.putExtra(DAY_TYPE_EXTRA, daytype)
            ContextCompat.startActivity(context, intent, null)
        }

        private val COLOR_LIST = arrayListOf("#139EED", "#EE386D", "#FFC529", "#9092A5", "#FF8E9F", "#2B0050", "#FD92C4")
        private val REPEAT_LIST = arrayListOf("从不", "每周", "每月", "每年")
    }

    @DayType
    private var mCurrentDayType = DaysMatter

    private var mCalendar = Calendar.getInstance()

    private var mSelectIndex = 0

    private var dataFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @IntDef(DaysMatter, DaysCumulative)
    annotation class DayType

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_countdown_day)
        setTitleBarLeftImage(R.drawable.ic_back_black)

        mCurrentDayType = intent.getIntExtra(DAY_TYPE_EXTRA, DaysMatter)

        dispatchView()

        tvTargeDate.setOnClickListener {
            SelectDateDialog.Builder(this)
                    .setOnWheelCallback { date ->
                        tvTargeDate.text = dataFormat.format(date)
                    }
                    .build()
                    .showAtViewAuto(tvTargeDate)
        }
        tvRepeat.setOnClickListener {
            MultipleItemDialog.Builder(this)
                    .setDate(REPEAT_LIST)
                    .setSelectItem(mSelectIndex)
                    .setSelectItem { dialog, i ->
                        mSelectIndex = i
                        tvRepeat.text = REPEAT_LIST[i]
                        dialog.dismiss()
                    }
                    .build()
                    .showAtViewAuto(tvRepeat)
        }
        mCalendar.timeInMillis = System.currentTimeMillis()
        tvTargeDate.text = "${mCalendar.get(Calendar.YEAR)}-${mCalendar.get(Calendar.MONTH) + 1}-${mCalendar.get(Calendar.DAY_OF_MONTH)}"

        colorRecyclerView.setColors(COLOR_LIST)

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
