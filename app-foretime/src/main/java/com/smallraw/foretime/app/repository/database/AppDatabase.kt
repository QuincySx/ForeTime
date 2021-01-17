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
