package com.smallraw.foretime.app.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.smallraw.foretime.app.model.CountDownTick

class CountDownService : Service(), CountDownTick.OnCountDownTickListener {
    override fun onCountDownTick(millisUntilFinished: Long) {
        Log.e("====sdf", "sdfsdfsdf $millisUntilFinished")
    }

    override fun onCountDownFinish() {
        Log.e("====sdf", "success")
    }

    //刷新间隔
    private val mRefreshIntervalTime: Long = 25
    private var mCountTickTimer = CountDownTick(1000 * 15, this, mRefreshIntervalTime)
//    private var mOnCountDownListener: CountDownModel.OnCountDownListener? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        mCountTickTimer.start()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onBind(intent: Intent): IBinder = CountDownBinder()

    override fun onUnbind(intent: Intent?): Boolean {
        return super.onUnbind(intent)
    }

    class CountDownBinder : Binder() {
        fun start() {

        }
    }
}
