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
package com.smallraw.foretime.app.repository.database.dao

import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
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
