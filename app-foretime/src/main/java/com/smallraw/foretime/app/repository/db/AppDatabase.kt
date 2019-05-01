package com.smallraw.foretime.app.repository.db

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.smallraw.foretime.app.executors.AppExecutors
import com.smallraw.foretime.app.repository.db.dao.ConfigDao
import com.smallraw.foretime.app.repository.db.dao.MemorialDao
import com.smallraw.foretime.app.repository.db.dao.MemorialTopDao
import com.smallraw.foretime.app.repository.db.entity.ConfigDO
import com.smallraw.foretime.app.repository.db.entity.MemorialDO
import com.smallraw.foretime.app.repository.db.entity.MemorialTopDO
import com.smallraw.time.db.converter.DateConverter
import java.util.*

@Database(entities = [ConfigDO::class, MemorialDO::class, MemorialTopDO::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    private val mIsDatabaseCreated = MutableLiveData<Boolean>()

    val databaseCreated: LiveData<Boolean>
        get() = mIsDatabaseCreated

    abstract fun memorialDao(): MemorialDao

    abstract fun memorialTopDao(): MemorialTopDao

    abstract fun configDao(): ConfigDao

    private fun updateDatabaseCreated(context: Context) {
        if (context.getDatabasePath(MemorialDO.TABLE_NAME).exists()) {
            setDatabaseCreated()
        }
    }

    private fun setDatabaseCreated() {
        mIsDatabaseCreated.postValue(true)
    }

    companion object {
        private val DATABASE_NAME = "timeDb"
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context, appExecutors: AppExecutors): AppDatabase {
            if (sInstance == null) {
                synchronized(AppDatabase::class.java) {
                    if (sInstance == null) {
                        sInstance = buildDatabase(context.applicationContext, appExecutors)
                        sInstance!!.updateDatabaseCreated(context.applicationContext)
                    }
                }
            }
            return sInstance!!
        }

        private fun buildDatabase(appContext: Context, appExecutors: AppExecutors): AppDatabase {
            return Room.databaseBuilder(appContext, AppDatabase::class.java, AppDatabase.DATABASE_NAME)
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            appExecutors.diskIO().execute {
                                val database = AppDatabase.getInstance(appContext,
                                        appExecutors)
                                db.beginTransaction()
                                try {
                                    val contentValues = ContentValues()
                                    contentValues.put("name", "孵化日")
                                    contentValues.put("description", "第一次遇到[ForeTime]开心吗？")
                                    contentValues.put("type", 0)
                                    contentValues.put("color", "#139EED")
                                    contentValues.put("targetTime", Date().time)
                                    contentValues.put("strike", false)
                                    contentValues.put("archive", false)
                                    val insert = db.insert(MemorialDO.TABLE_NAME,
                                            SQLiteDatabase.CONFLICT_REPLACE, contentValues)
                                    val contentTopValues = ContentValues()
                                    contentTopValues.put("memorial_id", insert)
                                    contentTopValues.put("type", 0)
                                    contentTopValues.put("createTime", Date().time)
                                    db.insert(MemorialTopDO.TABLE_NAME,
                                            SQLiteDatabase.CONFLICT_REPLACE, contentTopValues)
                                    db.setTransactionSuccessful()
                                } finally {
                                    db.endTransaction()
                                }
                                database.setDatabaseCreated()
                            }
                        }
                    })
                    //            .addMigrations()
                    .build()
        }
    }
}
