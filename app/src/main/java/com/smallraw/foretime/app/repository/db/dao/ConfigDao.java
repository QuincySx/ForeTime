package com.smallraw.foretime.app.repository.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.smallraw.foretime.app.repository.db.entity.ConfigEntity;

import java.util.List;

@Dao
public interface ConfigDao {
  @Query("SELECT COUNT(*) FROM config")
  int count();

  @Insert
  long insert(ConfigEntity configEntity);

  @Query("SELECT * FROM config WHERE id = :id")
  ConfigEntity findById(long id);

  @Query("SELECT * FROM config WHERE name= :name")
  List<ConfigEntity> findByKey(String name);

  @Update
  int update(ConfigEntity configEntity);
}
