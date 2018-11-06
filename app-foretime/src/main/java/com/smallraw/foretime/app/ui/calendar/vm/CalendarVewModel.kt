package com.smallraw.foretime.app.ui.calendar.vm

import android.arch.lifecycle.*
import android.util.Log
import android.util.LongSparseArray

import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.repository.DataRepository
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity
import com.smallraw.foretime.app.repository.db.entity.MemorialTopEntity

class CalendarVewModel : ViewModel() {
    private val mRepository: DataRepository = App.getInstance().getRepository()
    private val mMediatorLiveData = MediatorLiveData<LiveData<*>>()

    private var mActiveTaskLiveData = MutableLiveData<List<MemorialEntity>>()
    private var mActiveTaskTopLiveData: LiveData<MutableList<MemorialTopEntity>>

    val mActiveTaskListLiveData: MutableLiveData<List<MemorialEntity>> = MutableLiveData()

    init {
        Log.e("LiveData", "初始化完成")
        mActiveTaskTopLiveData = mRepository.getTaskTopList(0)
        mMediatorLiveData.addSource(mActiveTaskTopLiveData) {
            Log.e("LiveData", "顶置发生了变化")
            var memorialEntities = mActiveTaskLiveData.value as ArrayList
            var taskTopList = it
            if (taskTopList == null) {
                taskTopList = mutableListOf()
            }
            if (memorialEntities == null) {
                memorialEntities = arrayListOf()
            }
            val memorialMap = LongSparseArray<MemorialEntity>(memorialEntities.size)
            for (item in memorialEntities) {
                memorialMap.put(item.id!!, item)
            }

            val topMemorialList = ArrayList<MemorialEntity>(memorialMap.size())

            for (item in taskTopList) {
                val get = memorialMap.get(item.memorial_id)
                if (get != null) {
                    topMemorialList.add(get)
                    memorialEntities.remove(get)
                }
            }
            memorialEntities.addAll(0, topMemorialList)
            App.getInstance().getAppExecutors().mainThread().execute {
                mActiveTaskListLiveData.value = (memorialEntities)
            }
        }

        mMediatorLiveData.addSource(mActiveTaskLiveData) {
            Log.e("LiveData", "任务卡发生了变化")
            var memorialEntities = it as ArrayList
            var taskTopList = mActiveTaskTopLiveData.value
            if (taskTopList == null) {
                taskTopList = mutableListOf()
            }
            if (memorialEntities == null) {
                memorialEntities = arrayListOf()
            }
            val memorialMap = LongSparseArray<MemorialEntity>(memorialEntities.size)
            for (item in memorialEntities) {
                memorialMap.put(item.id!!, item)
            }

            val topMemorialList = ArrayList<MemorialEntity>(memorialMap.size())

            for (item in taskTopList!!) {
                val get = memorialMap.get(item.memorial_id)
                if (get != null) {
                    topMemorialList.add(get)
                    memorialEntities.remove(get)
                }
            }
            memorialEntities.addAll(0, topMemorialList)
            App.getInstance().getAppExecutors().mainThread().execute {
                mActiveTaskListLiveData.value = (memorialEntities)
            }
        }
    }

    fun queryActiveTask(display: Int, order: Int) {
        App.getInstance().getAppExecutors().diskIO().execute {
            Log.e("Query DB", "查询新的任务数据")
            val activeTask = mRepository.getActiveTask(display, order)
            mActiveTaskLiveData.postValue(activeTask)
        }
    }
}
