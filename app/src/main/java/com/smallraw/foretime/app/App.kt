package com.smallraw.foretime.app

import android.app.Application
import com.smallraw.foretime.app.executors.AppExecutors
import com.smallraw.library.core.utils.AppUtils

class App : Application() {
    private lateinit var mAppExecutors: AppExecutors

    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
        mAppExecutors = AppExecutors()
    }

//    fun getDatabase(): AppDatabase {
//        return AppDatabase.getInstance(this, mAppExecutors)
//    }
//
//    fun getRepository(): DataRepository {
//        return DataRepository.getInstance(getDatabase())
//    }

    fun getAppExecutors(): AppExecutors {
        return mAppExecutors
    }
}
