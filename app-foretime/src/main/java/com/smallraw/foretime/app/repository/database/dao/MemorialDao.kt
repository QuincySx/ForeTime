package com.smallraw.foretime.app.repository.database.dao

import androidx.sqlite.db.SupportSQLiteQuery
import androidx.room.*
import com.smallraw.foretime.app.repository.database.entity.MemorialDO

@Dao
interface MemorialDao {
    @RawQuery
    fun select(sql: SupportSQLiteQuery): List<MemorialDO>

    @Query("SELECT COUNT(*) FROM memorial")
    fun count(): Int

    @Insert
    fun insert(memorialDO: MemorialDO): Long

    @Insert
    fun insertAll(memorialDO: Array<MemorialDO>): List<Long>

    @Query("SELECT * FROM memorial")
    fun selectAll(): List<MemorialDO>

    @Query("SELECT * FROM memorial WHERE strike = 0 AND archive = 0")
    fun selectActive(): List<MemorialDO>

    @Query("SELECT * FROM memorial WHERE strike = 0 AND archive = 0 AND type = :display ORDER BY " + ":order DESC")
    fun selectOptionActive(display: Int, order: String): List<MemorialDO>

    @Query("SELECT * FROM memorial WHERE strike = 0 AND archive = 0 ORDER BY :order DESC")
    fun selectOptionActive(order: String): List<MemorialDO>

    @Query("SELECT * FROM memorial WHERE strike = 0 AND archive = 0 ORDER BY targetTime DESC")
    fun selectOptionActive(): List<MemorialDO>

    @Query("SELECT * FROM memorial WHERE id = :id")
    fun selectById(id: Long): MemorialDO

    @Update
    fun update(memorialDO: MemorialDO): Int

    @Update
    fun update(memorialDO: List<MemorialDO>): Int

    @Query("DELETE FROM memorial WHERE id = :id")
    fun deleteById(id: Long): Int

    @Query("DELETE FROM memorial")
    fun deleteAll(): Int

    @Delete
    fun deletes(memorialDO: List<MemorialDO>): Int
}