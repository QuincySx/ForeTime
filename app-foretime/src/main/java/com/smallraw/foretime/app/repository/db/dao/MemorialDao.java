package com.smallraw.foretime.app.repository.db.dao;

import android.arch.persistence.db.SupportSQLiteQuery;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RawQuery;
import android.arch.persistence.room.Update;

import com.smallraw.foretime.app.repository.db.entity.MemorialDO;

import java.util.List;

@Dao
public interface MemorialDao {
    @RawQuery
    List<MemorialDO> select(SupportSQLiteQuery sql);

    @Query("SELECT COUNT(*) FROM memorial")
    int count();

    @Insert
    long insert(MemorialDO memorialDO);

    @Insert
    List<Long> insertAll(MemorialDO[] memorialDO);

    @Query("SELECT * FROM memorial")
    List<MemorialDO> selectAll();

    @Query("SELECT * FROM memorial WHERE strike = 0 AND archive = 0")
    List<MemorialDO> selectActive();

    @Query("SELECT * FROM memorial WHERE strike = 0 AND archive = 0 AND type = :display ORDER BY " +
            ":order DESC")
    List<MemorialDO> selectOptionActive(int display, String order);

    @Query("SELECT * FROM memorial WHERE strike = 0 AND archive = 0 ORDER BY :order DESC")
    List<MemorialDO> selectOptionActive(String order);

    @Query("SELECT * FROM memorial WHERE strike = 0 AND archive = 0 ORDER BY targetTime DESC")
    List<MemorialDO> selectOptionActive();

    @Query("SELECT * FROM memorial WHERE id = :id")
    MemorialDO selectById(long id);

    @Update
    int update(MemorialDO memorialDO);

    @Update
    int update(List<MemorialDO> memorialDO);

    @Query("DELETE FROM memorial WHERE id = :id")
    int deleteById(long id);

    @Query("DELETE FROM memorial")
    int deleteAll();

    @Delete
    int deletes(List<MemorialDO> memorialDO);
}