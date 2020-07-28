package com.smallraw.foretime.app.repository.database.dao

import androidx.room.*
import com.smallraw.foretime.app.repository.database.entity.ConfigDO

@Dao
interface ConfigDao {
    @Query("SELECT COUNT(*) FROM config")
    fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(configDO: ConfigDO): Long

    @Query("SELECT * FROM config WHERE id = :id")
    fun findById(id: Long): ConfigDO

    @Query("SELECT * FROM config WHERE name= :name")
    fun findByKey(name: String): List<ConfigDO>

    @Update
    fun update(configDO: ConfigDO): Int
}
