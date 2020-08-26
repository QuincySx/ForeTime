package com.smallraw.foretime.app

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.smallraw.foretime.app.config.getCalendarSettingConfig
import com.smallraw.foretime.app.config.getMusicSettingConfig
import com.smallraw.foretime.app.config.getTaskSettingConfig
import com.smallraw.foretime.app.config.saveConfig
import com.smallraw.foretime.app.entity.CalendarConfigInfo
import com.smallraw.foretime.app.entity.MusicConfigInfo
import com.smallraw.foretime.app.entity.TaskConfigInfo
import com.smallraw.foretime.app.executors.AppExecutors
import com.smallraw.foretime.app.repository.DataRepository
import com.smallraw.foretime.app.repository.database.AppDatabase
import com.smallraw.lib.monitor.view.DumpViewTimeInterceptor
import com.smallraw.library.core.utils.AppUtils
import io.github.inflationx.viewpump.ViewPump

class App : Application(), ViewModelStoreOwner {
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
        ViewModelStore()
    }

    override fun onCreate() {
        System.setProperty("kotlinx.coroutines.debug", "on")
        super.onCreate()
        handlerError()
        mApp = this
        AppUtils.init(this)
        mAppExecutors = AppExecutors()
        initConfig()

        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(DumpViewTimeInterceptor())
//                .addInterceptor(TextUpdatingInterceptor())
//                .addInterceptor(CustomTextViewInterceptor())
                .build()
        )
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

    override fun getViewModelStore() = mViewModelStore

    private fun handlerError() {
        // 捕获主线程 catch 防止闪退
        // 不能防止 Activity onCreate 主线程报错，这样会因为 Activity 生命周期没走完而崩溃。
        Handler().post(Runnable {
            while (true) {
                try {
                    Looper.loop()
                } catch (e: Throwable) {
                    // 异常发给 AppCenter
                    Log.e("Main Thread Catch", "如果软件 ANR 请检查报错信息是否在 Activity 的 onCreate() 方法。")
//                    Crashes.trackError(e)
                    e.printStackTrace()
                }
            }
        })
    }
}
