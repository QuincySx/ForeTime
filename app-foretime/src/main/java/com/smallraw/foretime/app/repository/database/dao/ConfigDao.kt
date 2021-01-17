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
import com.smallraw.foretime.app.repository.database.entity.ConfigDO

@Dao
interface ConfigDao {
    @Query("SELECT COUNT(*) FROM config")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(configDO: ConfigDO): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun inserts(configDOs: List<ConfigDO>): List<Long>

    @Query("SELECT * FROM config WHERE id = :id")
    fun findById(id: Long): ConfigDO

    @Query("SELECT * FROM config WHERE name= :name")
    fun findByKey(name: String): ConfigDO?

    @Update
    fun update(configDO: ConfigDO): Int
}
