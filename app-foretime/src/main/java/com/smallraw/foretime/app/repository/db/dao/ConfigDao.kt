package com.smallraw.foretime.app.repository.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

import com.smallraw.foretime.app.repository.db.entity.ConfigDO

@Dao
interface ConfigDao {
    @Query("SELECT COUNT(*) FROM config")
    fun count(): Int

    @Insert
    fun insert(configDO: ConfigDO): Long

    @Query("SELECT * FROM config WHERE id = :id")
    fun findById(id: Long): ConfigDO

    @Query("SELECT * FROM config WHERE name= :name")
    fun findByKey(name: String): List<ConfigDO>

    @Update
    fun update(configDO: ConfigDO): Int
}
