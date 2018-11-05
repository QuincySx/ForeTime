package com.smallraw.foretime.app.ui.calendar.vm

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.system.Os.remove
import android.util.LongSparseArray

import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.repository.DataRepository
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity
import com.smallraw.foretime.app.repository.db.entity.MemorialTopEntity

import java.util.HashMap

class CalendarVewModle : ViewModel() {
    val mRepository: DataRepository
    private val mMediatorLiveData = MediatorLiveData<LiveData<*>>()

    private var mMemorialLiveData = MutableLiveData<List<MemorialEntity>>()
    private var mMemorialTopLiveData: LiveData<MutableList<MemorialTopEntity>>

     val mMemorialListLiveData: MutableLiveData<List<MemorialEntity>> = MutableLiveData<List<MemorialEntity>>()

    init {
        mRepository = App.getInstance().getRepository()
        mMemorialTopLiveData = mRepository.getTaskTopList(0)
        mMediatorLiveData.addSource(mMemorialTopLiveData) {
            var memorialEntities = mMemorialLiveData.value as ArrayList
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
                mMemorialListLiveData.value = (memorialEntities)
            }
        }

        mMediatorLiveData.addSource(mMemorialLiveData) {
            var memorialEntities = it as ArrayList
            var taskTopList = mMemorialTopLiveData.value
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
                mMemorialListLiveData.value = (memorialEntities)
            }
        }
    }

    fun queryActiveTask(display: Int, order: Int) {
        mMemorialLiveData = mRepository.getActiveTask(display, order) as MutableLiveData<List<MemorialEntity>>
    }
}
