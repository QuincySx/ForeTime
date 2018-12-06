package com.smallraw.foretime.app.ui.taskInfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import com.smallraw.foretime.app.R
import com.smallraw.time.base.BaseTitleBarActivity
import kotlinx.android.synthetic.main.activity_task_info.*
import me.jessyan.autosize.utils.AutoSizeUtils
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.event.TaskChangeEvent
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity
import com.smallraw.foretime.app.ui.addTaskDay.AddTaskDayActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class TaskInfoActivity : BaseTitleBarActivity() {
    companion object {
        private const val EXT_TASK_ID = "ext_task_id"

        @JvmStatic
        fun start(activity: Activity?, taskId: Long) {
            val intent = Intent(activity, TaskInfoActivity::class.java)
            intent.putExtra(EXT_TASK_ID, taskId)
            activity?.startActivity(intent)
        }

        private val colorList = arrayListOf("#139EED", "#EE386D", "#FFC529", "#9092A5", "#FF8E9F", "#2B0050", "#FD92C4")
    }

    private var task: MemorialEntity? = null
    private var taskInfoAdapter: TaskInfoAdapter? = null
    private var pagerSnapHelper = PagerSnapHelper()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_info)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        EventBus.getDefault().register(this)

        addRightView(newEditView(View.OnClickListener {
            //编辑
            AddTaskDayActivity.startEdit(this, task!!.id, task!!.type)
        }))
        addRightView(newShareView(View.OnClickListener {
            //分享
        }))

        val taskIDExtra = intent.getLongExtra(EXT_TASK_ID, -1L)

        if (taskIDExtra == -1L) {
            finish()
        }

        initRecyclerView()

        App.getInstance().getAppExecutors().diskIO().execute {
            task = App.getInstance().getRepository().getTask(taskIDExtra)
            taskInfoAdapter = TaskInfoAdapter(task!!, colorList)

            App.getInstance().getAppExecutors().mainThread().execute {
                setAdapterDate()
            }
        }
    }

    private fun refreshTask(taskId: Long) {
        App.getInstance().getAppExecutors().diskIO().execute {
            val tempTask: MemorialEntity = App.getInstance().getRepository().getTask(taskId)
                    ?: return@execute
            task = tempTask
            App.getInstance().getAppExecutors().mainThread().execute {
                taskInfoAdapter?.setData(task!!)
                taskInfoAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun setAdapterDate() {
        recyclerView.adapter = taskInfoAdapter
    }

    private fun initRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = linearLayoutManager
        pagerSnapHelper.attachToRecyclerView(recyclerView)
    }

    private fun newShareView(onClickListener: View.OnClickListener? = null): View {
        val right = ImageView(this)
        val layoutParams = ViewGroup.LayoutParams(AutoSizeUtils.dp2px(this, 35F), AutoSizeUtils.dp2px(this, 35F))
        right.layoutParams = layoutParams
        right.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_share_black, null))
        right.setOnClickListener(onClickListener)
        return right
    }

    private fun newEditView(onClickListener: View.OnClickListener? = null): View {
        val right = ImageView(this)
        val layoutParams = ViewGroup.LayoutParams(AutoSizeUtils.dp2px(this, 35F), AutoSizeUtils.dp2px(this, 35F))
        right.layoutParams = layoutParams
        right.setImageDrawable(ResourcesCompat.getDrawable(resources, R.drawable.ic_edit_black, null))
        right.setOnClickListener(onClickListener)
        return right
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: TaskChangeEvent) {
        if (event.changeType == TaskChangeEvent.UPDATE
                && task != null
                && event.taskID == task!!.id) {
            refreshTask(task!!.id)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
