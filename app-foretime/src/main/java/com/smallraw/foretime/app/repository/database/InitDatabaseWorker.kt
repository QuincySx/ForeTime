package com.smallraw.foretime.app.repository.database

import android.content.Context
import android.util.Log
import androidx.room.withTransaction
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.smallraw.foretime.app.repository.db.entity.MemorialDO
import com.smallraw.foretime.app.repository.db.entity.MemorialTopDO
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