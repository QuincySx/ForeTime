package com.smallraw.foretime.app.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import androidx.room.*
import com.smallraw.foretime.app.repository.db.entity.MemorialTopDO

@Dao
interface MemorialTopDao {
    @RawQuery
    fun select(sql: SupportSQLiteQuery): List<MemorialTopDO>

    @Query("SELECT COUNT(*) FROM memorial_top")
    fun count(): Int

    @Insert
    fun insert(memorialTopDO: MemorialTopDO): Long

    @Query("SELECT COUNT(*) FROM memorial_top WHERE memorial_id = :taskId AND type = :type")
    fun isTopTaskByTaskId(taskId: Long?, type: Long?): Int

    @Insert
    fun insertAll(memorialTopEntities: Array<MemorialTopDO>): List<Long>

    @Query("SELECT * FROM memorial_top ORDER BY createTime DESC")
    fun selectAll(): List<MemorialTopDO>

    @Query("SELECT * FROM memorial_top WHERE type = :type ORDER BY createTime DESC")
    fun selectAllByType(type: Int): LiveData<MutableList<MemorialTopDO>>

    @Update
    fun update(memorialEntity: MemorialTopDO): Int

    @Query("DELETE FROM memorial_top WHERE id = :id")
    fun deleteById(id: Long): Int

    @Query("DELETE FROM memorial_top WHERE memorial_id = :id")
    fun deleteByTaskId(id: Long): Int

    @Query("DELETE FROM memorial_top")
    fun deleteAll(): Int

    @Query("DELETE FROM memorial_top WHERE memorial_id = :taskId AND type = :type")
    fun deleteTaskByTaskId(taskId: Long?, type: Long?): Int
}
