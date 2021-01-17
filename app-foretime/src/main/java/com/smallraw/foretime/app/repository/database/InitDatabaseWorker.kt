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
import android.util.Log
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smallraw.foretime.app.repository.database.entity.MemorialDO
import com.smallraw.foretime.app.repository.database.entity.MemorialTopDO
import java.util.*

class InitDatabaseWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {
    companion object {
        private val TAG = InitDatabaseWorker::class.java.simpleName
    }

    override suspend fun doWork(): Result {
        return try {
            val database = AppDatabase.getInstance(applicationContext)
            database.withTransaction {
                val memorialDO = MemorialDO(
                    "孵化日",
                    "第一次遇到[ForeTime]开心吗？",
                    0,
                    "#139EED",
                    Date(),
                    null,
                    Date()
                )
                val insert = database.memorialDao().insert(memorialDO)

                val memorialTopDO = MemorialTopDO(
                    insert,
                    0
                )
                database.memorialTopDao().insert(memorialTopDO)
                Result.success()
            }
        } catch (ex: Exception) {
            Log.e(TAG, "Error seeding database", ex)
            Result.failure()
        }
    }
}
