/*
 * Copyright 2021 Smallraw Labs Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.smallraw.foretime.app.ui.taskInfo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.res.ResourcesCompat
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.R
import com.smallraw.foretime.app.base.BaseTitleBarActivity
import com.smallraw.foretime.app.base.databinding.DataBindingConfig
import com.smallraw.foretime.app.common.event.TaskChangeEvent
import com.smallraw.foretime.app.databinding.ActivityTaskInfoBinding
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
import com.smallraw.foretime.app.ui.addTaskDay.AddTaskDayActivity
import me.jessyan.autosize.utils.AutoSizeUtils
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

    private val mBinding by lazy {
        getBinding() as ActivityTaskInfoBinding
    }
    private var mTask: MemorialDO? = null
    private var taskInfoAdapter: TaskInfoAdapter? = null
    private var pagerSnapHelper = androidx.recyclerview.widget.PagerSnapHelper()

    override fun initViewModel() {
    }

    override fun getDataBindingConfig(): DataBindingConfig {
        return DataBindingConfig(R.layout.activity_task_info)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitleBarLeftImage(R.drawable.ic_back_black)
        EventBus.getDefault().register(this)

        addRightView(
            newEditView(
                View.OnClickListener {
                    // 编辑
                    AddTaskDayActivity.startEdit(this, mTask!!.id!!, mTask!!.type)
                }
            )
        )
        addRightView(
            newShareView(
                View.OnClickListener {
                    // 分享
                }
            )
        )

        val taskIDExtra = intent.getLongExtra(EXT_TASK_ID, -1L)

        if (taskIDExtra == -1L) {
            finish()
        }

        initRecyclerView()

        App.getInstance().getAppExecutors().diskIO().execute {
            mTask = App.getInstance().getRepository().getTask(taskIDExtra)
            taskInfoAdapter = TaskInfoAdapter(mTask!!, colorList)

            App.getInstance().getAppExecutors().mainThread().execute {
                setAdapterDate()
            }
        }
    }

    private fun refreshTask(taskId: Long) {
        App.getInstance().getAppExecutors().diskIO().execute {
            val tempTask: MemorialDO = App.getInstance().getRepository().getTask(taskId)
                ?: return@execute
            mTask = tempTask
            App.getInstance().getAppExecutors().mainThread().execute {
                taskInfoAdapter?.setData(mTask!!)
                taskInfoAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun setAdapterDate() {
        mBinding.recyclerView.adapter = taskInfoAdapter
    }

    private fun initRecyclerView() {
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL, false)
        mBinding.recyclerView.layoutManager = linearLayoutManager
        mBinding.recyclerView.setHasFixedSize(true)
        pagerSnapHelper.attachToRecyclerView(mBinding.recyclerView)
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
        if (event.changeType == TaskChangeEvent.UPDATE &&
            mTask != null &&
            event.taskID == mTask!!.id
        ) {
            refreshTask(mTask!!.id!!)
        }
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }
}
