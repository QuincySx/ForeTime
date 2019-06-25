package com.smallraw.foretime.app.ui.addTaskDay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.annotation.IntDef
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.common.widget.dialog.MultipleItemDialog
import com.smallraw.foretime.app.common.widget.dialog.SelectDateDialog
import com.smallraw.foretime.app.constant.TaskTypeConsts
import com.smallraw.foretime.app.event.TaskChangeEvent
import com.smallraw.foretime.app.repository.db.entity.MemorialDO
import com.smallraw.time.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_add_countdown_day.*
import org.greenrobot.eventbus.EventBus
import java.text.SimpleDateFormat
import java.util.*

class AddTaskDayActivity : BaseTitleBarActivity() {

    @IntDef(DaysMatter, DaysCumulative)
    annotation class DayType

    @IntDef(OptionTypeAdd, OptionTypeEdit)
    annotation class OptionType

    companion object {
        /**
         * 累计日
         */
        const val DaysCumulative = TaskTypeConsts.CUMULATIVE_DAY
        /**
         * 倒数日
         */
        const val DaysMatter = TaskTypeConsts.COUNTDOWN_DAY

        /**
         * 添加
         */
        const val OptionTypeAdd = 0
        /**
         * 修改
         */
        const val OptionTypeEdit = 1

        /**
         *  添加的日期类型
         */
        private const val DAY_TYPE_EXTRA = "day_type_extra"

        /**
         * 修改的任务 ID
         */
        private const val TASK_ID_EXTRA = "task_id_extra"

        /**
         * 操作类型：修改、删除
         */
        private const val DAY_OPTION_TYPE_EXTRA = "day_option_type_extra"

        @JvmStatic
        fun startAdd(context: Context, @DayType dayType: Int) {
            val intent = Intent(context, AddTaskDayActivity::class.java)
            intent.putExtra(DAY_TYPE_EXTRA, dayType)
            intent.putExtra(DAY_OPTION_TYPE_EXTRA, OptionTypeAdd)
            ContextCompat.startActivity(context, intent, null)
        }

        @JvmStatic
        fun startEdit(context: Context, taskId: Long, @DayType dayType: Int) {
            val intent = Intent(context, AddTaskDayActivity::class.java)
            intent.putExtra(DAY_TYPE_EXTRA, dayType)
            intent.putExtra(TASK_ID_EXTRA, taskId)
            intent.putExtra(DAY_OPTION_TYPE_EXTRA, OptionTypeEdit)
            ContextCompat.startActivity(context, intent, null)
        }

        private val COLOR_LIST = arrayListOf("#139EED", "#EE386D", "#FFC529", "#9092A5", "#FF8E9F", "#2B0050", "#FD92C4")
        private val REPEAT_LIST = arrayListOf("从不", "每周", "每月", "每年")
    }

    private var mCalendar = Calendar.getInstance()

    private val mDataRepository = App.getInstance().getRepository()

    private val mSimpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    @DayType
    private var mCurrentDayType = DaysMatter

    @OptionType
    private var mCurrentDayOptionType = OptionTypeAdd

    private var mEditTaskId: Long = -1

    /**
     * 选择的重复模式
     */
    private var mSelectRepeatIndex = 0

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_countdown_day)
        setTitleBarLeftImage(R.drawable.ic_back_black)

        mCurrentDayOptionType = intent.getIntExtra(DAY_OPTION_TYPE_EXTRA, OptionTypeAdd)
        when (mCurrentDayOptionType) {
            OptionTypeAdd -> {
                mCalendar.timeInMillis = System.currentTimeMillis()
                tvTargetDate.text = "${mCalendar.get(Calendar.YEAR)}-${mCalendar.get(Calendar.MONTH) + 1}-${mCalendar.get(Calendar.DAY_OF_MONTH)}"
            }
            OptionTypeEdit -> {
                mEditTaskId = intent.getLongExtra(TASK_ID_EXTRA, mEditTaskId)
                if (mEditTaskId == -1L) {
                    finish()
                }
                queryTask(mEditTaskId)
            }
        }

        mCurrentDayType = intent.getIntExtra(DAY_TYPE_EXTRA, DaysMatter)

        dispatchTypeTitle(mCurrentDayType)

        tvTargetDate.setOnClickListener {
            var date: Long
            try {
                val parse = mSimpleDateFormat.parse(tvTargetDate.text.toString())
                date = parse.time
            } catch (e: Exception) {
                date = System.currentTimeMillis()
                e.printStackTrace()
            }
            SelectDateDialog.Builder(this)
                    .setOnWheelCallback { date ->
                        tvTargetDate.text = mSimpleDateFormat.format(date)
                    }
                    .setTime(date)
                    .atViewAuto(tvTargetDate)
                    .build()
                    .show()
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
                    .atViewAuto(tvRepeat)
                    .build()
                    .show()
        }

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
                when (mCurrentDayOptionType) {
                    OptionTypeAdd -> {
                        val memorial = MemorialDO(titleName, note, mCurrentDayType, color, date, repeatTime, Date())
                        mDataRepository.insertTask(memorial)
                        EventBus.getDefault().post(TaskChangeEvent(TaskChangeEvent.ADD))
                    }
                    OptionTypeEdit -> {
                        val memorial = MemorialDO(titleName, note, mCurrentDayType, color, date, repeatTime, Date())
                        memorial.id = mEditTaskId
                        mDataRepository.update(memorial)
                        EventBus.getDefault().post(TaskChangeEvent(TaskChangeEvent.UPDATE, mEditTaskId, mCurrentDayType))
                    }
                }
            }
            finish()
        }
    }

    private fun queryTask(i: Long) {
        App.getInstance().getAppExecutors().diskIO().execute {
            val task = mDataRepository.getTask(i)
            App.getInstance().getAppExecutors().mainThread().execute {
                setContentViewData(task)
            }
        }

    }

    private fun setContentViewData(task: MemorialDO) {
        mCalendar.timeInMillis = task.targetTime!!.time
        tvTargetDate.text = "${mCalendar.get(Calendar.YEAR)}-${mCalendar.get(Calendar.MONTH) + 1}-${mCalendar.get(Calendar.DAY_OF_MONTH)}"

        titleName.setText(task.name)
        tvNote.setText(task.description)

        mSelectRepeatIndex = when (task.repeatTime) {
            "none" -> {
                0
            }
            "1E" -> {
                1
            }
            "1M" -> {
                2
            }
            "1y" -> {
                3
            }
            else -> {
                0
            }
        }
    }

    private fun dispatchTypeTitle(@DayType dayType: Int) {
        when (dayType) {
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
