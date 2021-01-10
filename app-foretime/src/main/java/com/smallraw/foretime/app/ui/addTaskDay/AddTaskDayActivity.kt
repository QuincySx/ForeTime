package com.smallraw.foretime.app.ui.addTaskDay

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.common.widget.dialog.MultipleItemDialog
import com.smallraw.foretime.app.common.widget.dialog.SelectDateDialog
import com.smallraw.foretime.app.constant.TaskTypeConsts
import com.smallraw.foretime.app.databinding.ActivityAddCountdownDayBinding
import com.smallraw.foretime.app.event.TaskChangeEvent
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
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

        private val COLOR_LIST =
            arrayListOf("#139EED", "#EE386D", "#FFC529", "#9092A5", "#FF8E9F", "#2B0050", "#FD92C4")
        private val REPEAT_LIST = arrayListOf("从不", "每周", "每月", "每年")
    }

    private val mBinding by lazy {
        ActivityAddCountdownDayBinding.inflate(layoutInflater)
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

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_add_countdown_day)
    }

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mBinding.root)
        setTitleBarLeftImage(R.drawable.ic_back_black)

        mCurrentDayOptionType = intent.getIntExtra(DAY_OPTION_TYPE_EXTRA, OptionTypeAdd)
        when (mCurrentDayOptionType) {
            OptionTypeAdd -> {
                mCalendar.timeInMillis = System.currentTimeMillis()
                mBinding.tvTargetDate.text =
                    "${mCalendar.get(Calendar.YEAR)}-${mCalendar.get(Calendar.MONTH) + 1}-${
                        mCalendar.get(Calendar.DAY_OF_MONTH)
                    }"
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

        mBinding.tvTargetDate.setOnClickListener {
            var date: Long
            try {
                val parse = mSimpleDateFormat.parse(mBinding.tvTargetDate.text.toString())
                date = parse.time
            } catch (e: Exception) {
                date = System.currentTimeMillis()
                e.printStackTrace()
            }
            SelectDateDialog.Builder(this)
                .setOnWheelCallback { date ->
                    mBinding.tvTargetDate.text = mSimpleDateFormat.format(date)
                }
                .setTime(date)
                .atViewAuto(mBinding.tvTargetDate)
                .build()
                .show()
        }

        mBinding.tvRepeat.setOnClickListener {
            MultipleItemDialog.Builder(this)
                .setDate(REPEAT_LIST)
                .setSelectItem(mSelectRepeatIndex)
                .setSelectItem { dialog, i ->
                    mSelectRepeatIndex = i
                    mBinding.tvRepeat.text = REPEAT_LIST[i]
                    dialog.dismiss()
                }
                .atViewAuto(mBinding.tvRepeat)
                .build()
                .show()
        }

        mBinding.colorRecyclerView.setColors(COLOR_LIST)

        mBinding.ivTomatoBellSuspension.setOnClickListener {
            val titleName = mBinding.titleName.text.toString()
            val note = mBinding.tvNote.text.toString()
            val date = mSimpleDateFormat.parse(mBinding.tvTargetDate.text.toString())
            val repeat = mBinding.tvRepeat.text.toString()
            val color = mBinding.colorRecyclerView.selectColor

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
                        val memorial = MemorialDO(
                            titleName,
                            note,
                            mCurrentDayType,
                            color,
                            date,
                            repeatTime,
                            Date()
                        )
                        mDataRepository.insertTask(memorial)
                        EventBus.getDefault().post(TaskChangeEvent(TaskChangeEvent.ADD))
                    }
                    OptionTypeEdit -> {
                        val memorial = MemorialDO(
                            titleName,
                            note,
                            mCurrentDayType,
                            color,
                            date,
                            repeatTime,
                            Date()
                        )
                        memorial.id = mEditTaskId
                        mDataRepository.update(memorial)
                        EventBus.getDefault().post(
                            TaskChangeEvent(
                                TaskChangeEvent.UPDATE,
                                mEditTaskId,
                                mCurrentDayType
                            )
                        )
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
        mBinding.tvTargetDate.text =
            "${mCalendar.get(Calendar.YEAR)}-${mCalendar.get(Calendar.MONTH) + 1}-${
                mCalendar.get(Calendar.DAY_OF_MONTH)
            }"

        mBinding.titleName.setText(task.name)
        mBinding.tvNote.setText(task.description)

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
                mBinding.titleName.hint = "倒数日名称"
                mBinding.layoutCyclePeriod.visibility = View.VISIBLE
            }
            DaysCumulative -> {
                mBinding.titleName.hint = "累计日名称"
                mBinding.layoutCyclePeriod.visibility = View.GONE
            }
        }
    }

}
