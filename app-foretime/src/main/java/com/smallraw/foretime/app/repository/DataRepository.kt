package com.smallraw.foretime.app.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.smallraw.foretime.app.repository.database.AppDatabase
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
import com.smallraw.foretime.app.repository.database.entity.MemorialTopDO

class DataRepository(database: AppDatabase) {
    private val mDatabase: AppDatabase = database

    companion object {
        @JvmStatic
        private var sInstance: DataRepository? = null

        @JvmStatic
        fun getInstance(database: AppDatabase): DataRepository {
            if (sInstance == null) {
                synchronized(DataRepository::class.java) {
                    if (sInstance == null) {
                        sInstance = DataRepository(database)
                    }
                }
            }
            return sInstance!!
        }
    }

    /**
     * @param display 需要显示的内容 0::显示全部 1::显示倒数日 2::显示累计日
     * @param order 按条件排序 0::默认排序 1::日期排序 2::颜色排序
     * @return 返回按顺序查找的任务列表
     */
    fun getActiveTask(display: Int, order: Int): List<MemorialDO> {
        val displayOption = when (display) {
            -1 -> {
                null
            }
            0 -> {
                0
            }
            1 -> {
                1
            }
            else -> {
                null
            }
        }

        val orderBy = when (order) {
            0 -> {
                "id"
            }
            1 -> {
                "beginTime"
            }
            2 -> {
                "color"
            }
            else -> {
                "id"
            }
        }
        val query: SimpleSQLiteQuery
        query = if (displayOption == null) {
            SimpleSQLiteQuery("SELECT * FROM memorial WHERE strike = 0 AND archive = 0 ORDER BY $orderBy DESC")
        } else {
            SimpleSQLiteQuery("SELECT * FROM memorial WHERE strike = 0 AND archive = 0 AND type = ? ORDER BY $orderBy DESC",
                    arrayOf<Any>(displayOption))
        }

        return mDatabase.memorialDao().select(query)
    }

    fun getTask(strike: Boolean = false, archive: Boolean = false): List<MemorialDO> {
        val query = SimpleSQLiteQuery("SELECT * FROM memorial WHERE strike = ? AND archive = ? ORDER BY createTime DESC", arrayOf<Any>(strike, archive))
        return mDatabase.memorialDao().select(query)
    }

    fun getTaskStrike(strike: Boolean = false): List<MemorialDO> {
        val query = SimpleSQLiteQuery("SELECT * FROM memorial WHERE strike = ? ORDER BY createTime DESC", arrayOf<Any>(strike))
        return mDatabase.memorialDao().select(query)
    }

    fun getTask(id: Long): MemorialDO {
        return mDatabase.memorialDao().selectById(id)
    }

    fun insertTask(memorialDO: MemorialDO): Long {
        return mDatabase.memorialDao().insert(memorialDO)
    }

    fun update(memorialDO: MemorialDO) {
        mDatabase.memorialDao().update(memorialDO)
    }

    fun update(memorialDOS: List<MemorialDO>) {
        mDatabase.memorialDao().update(memorialDOS)
    }

    fun delete(id: Long) {
        mDatabase.memorialTopDao().deleteByTaskId(id)
        mDatabase.memorialDao().deleteById(id)
    }

    fun deleteTask(memorialDOS: List<MemorialDO>) {
        mDatabase.memorialDao().deletes(memorialDOS)
    }

    fun getTaskTopList(type: Int): LiveData<MutableList<MemorialTopDO>> {
        return mDatabase.memorialTopDao().selectAllByType(type)
    }

    fun isTopTask(id: Long, type: Long): Boolean {
        val int = mDatabase.memorialTopDao().isTopTaskByTaskId(id, type)
        return int > 0
    }

    fun insertTopTask(memorialDO: MemorialTopDO): Long {
        return mDatabase.memorialTopDao().insert(memorialDO)
    }

    fun deleteTopTask(id: Long, type: Long): Int {
        return mDatabase.memorialTopDao().deleteTaskByTaskId(id, type)
    }

    fun deleteTopTaskAll(id: Long): Int {
        return mDatabase.memorialTopDao().deleteByTaskId(id)
    }
}