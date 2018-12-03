package com.smallraw.foretime.app.ui.addTaskDay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.Switch
import android.widget.Toast
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.widget.dialog.MultipleItemDialog
import com.smallraw.foretime.app.common.widget.dialog.SelectDateDialog
import com.smallraw.foretime.app.event.TaskChangeEvent
import com.smallraw.foretime.app.repository.DataRepository
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity
import com.smallraw.time.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_add_countdown_day.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class AddTaskDayActivity : BaseTitleBarActivity() {

    companion object {
        /**
         * 累计日
         */
        const val DaysCumulative = 0
        /**
         * 倒数日
         */
        const val DaysMatter = 1

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

    private val mDataRepository = App.getInstance().getRepository()

    private val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @DayType
    private var mCurrentDayType = DaysMatter

    private var mCalendar = Calendar.getInstance()

    /**
     * 选择的重复模式
     */
    private var mSelectRepeatIndex = 0

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

        tvTargetDate.setOnClickListener {
            SelectDateDialog.Builder(this)
                    .setOnWheelCallback { date ->
                        tvTargetDate.text = dataFormat.format(date)
                    }
                    .build()
                    .showAtViewAuto(tvTargetDate)
        }
        tvRepeat.setOnClickListener {
            MultipleItemDialog.Builder(this)
                    .setDate(REPEAT_LIST)
                    .setSelectItem(mSelectRepeatIndex)
                    .setSelectItem { dialog, i ->
                        mSelectRepeatIndex = i
                        tvRepeat.text = REPEAT_LIST[i]
                        dialog.dismiss()
                    }
                    .build()
                    .showAtViewAuto(tvRepeat)
        }
        mCalendar.timeInMillis = System.currentTimeMillis()
        tvTargetDate.text = "${mCalendar.get(Calendar.YEAR)}-${mCalendar.get(Calendar.MONTH) + 1}-${mCalendar.get(Calendar.DAY_OF_MONTH)}"

        colorRecyclerView.setColors(COLOR_LIST)

        ivSuspension.setOnClickListener {
            val titleName = titleName.text.toString()
            val note = tvNote.text.toString()
            val date = mSimpleDateFormat.parse(tvTargetDate.text.toString())
            val repeat = tvRepeat.text.toString()
            val color = colorRecyclerView.selectColor

            if (TextUtils.isEmpty(titleName)) {
                Toast.makeText(applicationContext, "标题不能为空", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val repeatTime: String
            when (repeat) {
                REPEAT_LIST[0] -> {
                    repeatTime = "none"
                }
                REPEAT_LIST[1] -> {
                    repeatTime = "1E"
                }
                REPEAT_LIST[2] -> {
                    repeatTime = "1M"
                }
                REPEAT_LIST[3] -> {
                    repeatTime = "1y"
                }
                else -> {
                    repeatTime = "none"
                }
            }
            App.getInstance().getAppExecutors().diskIO().execute {
                val memorial = MemorialEntity(titleName, note, mCurrentDayType, color, date, repeatTime, Date())
                mDataRepository.insertTask(memorial)
                EventBus.getDefault().post(TaskChangeEvent(TaskChangeEvent.ADD))
            }
            finish()
        }
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
