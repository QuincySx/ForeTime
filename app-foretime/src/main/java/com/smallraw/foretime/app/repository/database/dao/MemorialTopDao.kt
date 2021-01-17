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

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.smallraw.foretime.app.repository.database.entity.MemorialTopDO

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
