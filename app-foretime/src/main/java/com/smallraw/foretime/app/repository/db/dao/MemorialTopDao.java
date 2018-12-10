package com.smallraw.foretime.app.repository.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.smallraw.foretime.app.repository.db.entity.MemorialTopDO;

import java.util.List;

@Dao
public interface MemorialTopDao {
  @RawQuery
  List<MemorialTopDO> select(SupportSQLiteQuery sql);

  @Query("SELECT COUNT(*) FROM memorial_top")
  int count();

  @Insert
  long insert(MemorialTopDO memorialTopDO);

  @Query("SELECT COUNT(*) FROM memorial_top WHERE memorial_id = :taskId AND type = :type")
  int isTopTaskByTaskId(Long taskId, Long type);

  @Insert
  List<Long> insertAll(MemorialTopDO[] memorialTopEntities);

  @Query("SELECT * FROM memorial_top ORDER BY createTime DESC")
  List<MemorialTopDO> selectAll();

  @Query("SELECT * FROM memorial_top WHERE type = :type ORDER BY createTime DESC")
  LiveData<List<MemorialTopDO>> selectAllByType(int type);

  @Update
  int update(MemorialTopDO memorialEntity);

  @Query("DELETE FROM memorial_top WHERE id = :id")
  int deleteById(long id);

  @Query("DELETE FROM memorial_top WHERE memorial_id = :id")
  int deleteByTaskId(long id);

  @Query("DELETE FROM memorial_top")
  int deleteAll();

  @Query("DELETE FROM memorial_top WHERE memorial_id = :taskId AND type = :type")
  int deleteTaskByTaskId(Long taskId, Long type);
}
