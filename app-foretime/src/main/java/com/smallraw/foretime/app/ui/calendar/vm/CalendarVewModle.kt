package com.smallraw.foretime.app.ui.calendar.vm

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import android.arch.lifecycle.ViewModel
import android.system.Os.remove
import android.util.LongSparseArray

import com.smallraw.foretime.app.App
import com.smallraw.foretime.app.repository.db.entity.MemorialEntity
import com.smallraw.foretime.app.repository.db.entity.MemorialTopEntity

import java.util.HashMap

class CalendarVewModle : ViewModel() {
    var mDisplay = MutableLiveData<Int>()
    var mOrder = MutableLiveData<Int>()
    var mMemorialLiveData = MutableLiveData<List<MemorialEntity>>()

    val memorialLiveData: LiveData<List<MemorialEntity>>
        get() {
            queryActiveTask()
            return mMemorialLiveData
        }

    init {
        mDisplay.value = 0
        mOrder.value = 0

        Transformations.map(mDisplay) { input -> memorialLiveData }
        Transformations.map(mOrder) { input -> memorialLiveData }
    }

    fun setSelectQuery(display: Int, order: Int) {
        mDisplay.value = display
        mOrder.value = order
    }

    fun setDisplay(display: Int) {
        mDisplay.value = display
    }

    fun setOrder(order: Int) {
        mOrder.value = order
    }

    fun queryActiveTask() {
        App.getInstance().getAppExecutors().diskIO().execute(Runnable {
            val memorialEntities = App.getInstance().getRepository().getActiveTask(mDisplay.value!!, mOrder.value!!) as ArrayList
            val memorialMap = LongSparseArray<MemorialEntity>(memorialEntities.size)
            for (item in memorialEntities) {
                memorialMap.put(item.id!!, item)
            }

            val taskTopList = App.getInstance().getRepository().getTaskTopList(0)
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
                mMemorialLiveData.setValue(memorialEntities)
            }
        })
    }
}
