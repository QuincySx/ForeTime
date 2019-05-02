package com.smallraw.foretime.app

import android.app.Application
import com.smallraw.foretime.app.config.getCalendarSettingConfig
import com.smallraw.foretime.app.config.getMusicSettingConfig
import com.smallraw.foretime.app.config.getTaskSettingConfig
import com.smallraw.foretime.app.config.saveConfig
import com.smallraw.foretime.app.entity.CalendarConfigInfo
import com.smallraw.foretime.app.entity.MusicConfigInfo
import com.smallraw.foretime.app.entity.TaskConfigInfo
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
    private lateinit var mCalendarConfigInfo: CalendarConfigInfo
    private lateinit var mMusicConfigInfo: MusicConfigInfo
    private lateinit var mTaskConfigInfo: TaskConfigInfo

    override fun onCreate() {
        super.onCreate()
        mApp = this
        AppUtils.init(this)
        mAppExecutors = AppExecutors()
        initConfig()
    }

    private fun initConfig() {
        mAppExecutors.diskIO().execute {
            mCalendarConfigInfo = getCalendarSettingConfig()
            mMusicConfigInfo = getMusicSettingConfig()
            mTaskConfigInfo = getTaskSettingConfig()
        }
    }

    private fun saveConfig() {
        mAppExecutors.diskIO().execute {
            mCalendarConfigInfo.saveConfig()
            mMusicConfigInfo.saveConfig()
            mTaskConfigInfo.saveConfig()
        }
    }

    override fun onTerminate() {
        saveConfig()
        super.onTerminate()
    }

    fun getCalendarConfig() = mCalendarConfigInfo
    fun getMusicConfig() = mMusicConfigInfo
    fun getTaskConfig() = mTaskConfigInfo

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
