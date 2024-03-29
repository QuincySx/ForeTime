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
package com.smallraw.foretime.app.ui.main.calendar.vm

import android.util.Log
import android.util.LongSparseArray
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.common.event.TaskChangeEvent
import com.smallraw.foretime.app.repository.DataRepository
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
import com.smallraw.foretime.app.repository.database.entity.MemorialTopDO
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class CalendarVewModel : ViewModel() {
    private var mDisplay = -1
    private var mOrder = 0

    private val mRepository: DataRepository = App.getInstance().getRepository()
    val mActiveTaskListLiveData = MediatorLiveData<List<MemorialDO>>()

    private var mActiveTaskLiveData = MutableLiveData<ArrayList<MemorialDO>>()
    private var mActiveTaskTopLiveData: LiveData<MutableList<MemorialTopDO>>

    init {
        EventBus.getDefault().register(this)

        Log.e("LiveData", "初始化完成")
        mActiveTaskTopLiveData = mRepository.getTaskTopList(0)
        mActiveTaskListLiveData.addSource(mActiveTaskTopLiveData) {
            App.getInstance().getAppExecutors().diskIO().execute {
                Log.e("LiveData", "顶置发生了变化")
                val memorialEntities = mActiveTaskLiveData.value
                val taskTopList = it

                settleMemorialList(memorialEntities, taskTopList)
            }
        }

        mActiveTaskListLiveData.addSource(mActiveTaskLiveData) {
            App.getInstance().getAppExecutors().diskIO().execute {
                Log.e("LiveData", "顶置发生了变化")
                val memorialEntities = it
                val taskTopList = mActiveTaskTopLiveData.value

                settleMemorialList(memorialEntities, taskTopList)
            }
        }

//        mActiveTaskListLiveData.addSource(App.getInstance().getDatabase().databaseCreated) {
//            Log.e("LiveData", "数据库创建状态")
//            App.getInstance().getAppExecutors().diskIO().execute {
//                val memorialEntities = mRepository.getActiveTask(mDisplay, mOrder)
//                val taskTopList = mActiveTaskTopLiveData.value
//
//                settleMemorialList(memorialEntities as ArrayList<MemorialDO>?, taskTopList)
//            }
//        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: TaskChangeEvent) {
        App.getInstance().getAppExecutors().diskIO().execute {
            val memorialEntities = mRepository.getActiveTask(mDisplay, mOrder)
            val taskTopList = mActiveTaskTopLiveData.value

            settleMemorialList(memorialEntities as ArrayList<MemorialDO>?, taskTopList)
        }
    }

    private fun settleMemorialList(
        memorialList: ArrayList<MemorialDO>?,
        taskTopLists: MutableList<MemorialTopDO>?
    ) {
        var memorialEntities = memorialList
        var taskTopList = taskTopLists

        if (taskTopList == null) {
            taskTopList = mutableListOf()
        }
        if (memorialEntities == null) {
            memorialEntities = arrayListOf()
        }
        val memorialMap = LongSparseArray<MemorialDO>(memorialEntities.size)
        for (item in memorialEntities) {
            memorialMap.put(item.id!!, item)
        }

        val topMemorialList = ArrayList<MemorialDO>(memorialMap.size())

        for (item in taskTopList) {
            val get = memorialMap.get(item.memorial_id!!)
            if (get != null) {
                topMemorialList.add(get)
                memorialEntities.remove(get)
            }
        }
        memorialEntities.addAll(0, topMemorialList)

        memorialEntities.let(mActiveTaskListLiveData::postValue)
    }

    fun queryActiveTask(display: Int, order: Int) {
        mDisplay = display
        mOrder = order
        App.getInstance().getAppExecutors().diskIO().execute {
            Log.e("Query DB", "查询新的任务数据")
            val activeTask = mRepository.getActiveTask(display, order)
            mActiveTaskLiveData.postValue(activeTask as ArrayList<MemorialDO>)
        }
    }

    fun getDisplay(): Int {
        return mDisplay
    }

    override fun onCleared() {
        EventBus.getDefault().unregister(this)
        super.onCleared()
    }
}
