package com.smallraw.foretime.app.repository.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.smallraw.foretime.app.repository.database.dao.ConfigDao
import com.smallraw.foretime.app.repository.database.dao.MemorialDao
import com.smallraw.foretime.app.repository.database.dao.MemorialTopDao
import com.smallraw.foretime.app.repository.database.entity.ConfigDO
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
import com.smallraw.foretime.app.repository.database.entity.MemorialTopDO
import com.smallraw.time.database.converter.DateConverter

@Database(entities = [ConfigDO::class, MemorialDO::class, MemorialTopDO::class], version = 1, exportSchema = false)
@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun memorialDao(): MemorialDao

    abstract fun memorialTopDao(): MemorialTopDao

    abstract fun configDao(): ConfigDao

    companion object {
        private const val DATABASE_NAME = "timeDb"
        @Volatile
        private var sInstance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return sInstance ?: synchronized(this) {
                sInstance ?: buildDatabase(context).also { sInstance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object : RoomDatabase.Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                        val request = OneTimeWorkRequestBuilder<InitDatabaseWorker>().build()
                        WorkManager.getInstance(context).enqueue(request)
                    }
                })
                .build()
        }
    }
}
