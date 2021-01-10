package com.smallraw.foretime.app

import android.app.Application
import com.smallraw.foretime.app.base.AppViewModelStore
import com.smallraw.foretime.app.base.IViewModelStoreApp
import com.smallraw.foretime.app.config.getCalendarSettingConfig
import com.smallraw.foretime.app.config.getMusicSettingConfig
import com.smallraw.foretime.app.config.getTaskSettingConfig
import com.smallraw.foretime.app.config.saveConfig
import com.smallraw.foretime.app.entity.CalendarConfigInfo
import com.smallraw.foretime.app.entity.MusicConfigInfo
import com.smallraw.foretime.app.entity.TaskConfigInfo
import com.smallraw.foretime.app.common.executors.AppExecutors
import com.smallraw.foretime.app.repository.DataRepository
import com.smallraw.foretime.app.repository.database.AppDatabase
import com.smallraw.foretime.app.tomatoBell.TomatoBellKit
import com.smallraw.lib.monitor.crash.LoopProxy
import com.smallraw.lib.monitor.view.ViewInitTimeInterceptor
import com.smallraw.library.core.utils.AppUtils
import io.github.inflationx.viewpump.ViewPump

class App : Application(), IViewModelStoreApp {
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

    private val mViewModelStore by lazy {
        AppViewModelStore()
    }

    override fun onCreate() {
        System.setProperty("kotlinx.coroutines.debug", "on")
        super.onCreate()
        LoopProxy.proxyCrash()
        mApp = this
        AppUtils.init(this)
        mAppExecutors = AppExecutors()
        initConfig()
        initTomatoBell()

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(ViewInitTimeInterceptor())
//                .addInterceptor(TextUpdatingInterceptor())
//                .addInterceptor(CustomTextViewInterceptor())
                .build()
        )
    }

    private fun initTomatoBell() {
        mAppExecutors.diskIO().execute {
            TomatoBellKit.getInstance().reset()
        }
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
        return AppDatabase.getInstance(this)
    }

    fun getRepository(): DataRepository {
        return DataRepository.getInstance(getDatabase())
    }

    fun getAppExecutors(): AppExecutors {
        return mAppExecutors
    }

    override fun getViewModelStore() = mViewModelStore.viewModelStore
}
