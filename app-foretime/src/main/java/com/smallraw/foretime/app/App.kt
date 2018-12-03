package com.smallraw.foretime.app

import android.app.Application
import com.smallraw.foretime.app.executors.AppExecutors
import com.smallraw.foretime.app.repository.DataRepository
import com.smallraw.foretime.app.repository.db.AppDatabase
import com.smallraw.library.core.utils.AppUtils

class App : Application() {
    companion object {
        private lateinit var mApp: App

        @JvmStatic
        fun getInstance(): App {
            return mApp
        }


    }

    private lateinit var mAppExecutors: AppExecutors

    override fun onCreate() {
        super.onCreate()
        mApp = this
        AppUtils.init(this)
        mAppExecutors = AppExecutors()
    }

    fun getDatabase(): AppDatabase {
        return AppDatabase.getInstance(this, mAppExecutors)
    }

    fun getRepository(): DataRepository {
        return DataRepository.getInstance(getDatabase())
    }

    fun getAppExecutors(): AppExecutors {
        return mAppExecutors
    }
}
